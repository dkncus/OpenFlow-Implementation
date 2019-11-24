import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class Controller extends Node{
	ArrayList<Router> routers = new ArrayList<Router>();
	ArrayList<EndUser> users = new ArrayList<EndUser>(); 
	int numRoutersAvail; 
	ArrayList<ArrayList<Integer>> matrix;
	int port;
	Terminal t;
	
	Controller(ArrayList<ArrayList<Integer>> connections, ArrayList<String> names) throws SocketException {
		t = new Terminal("CONTROLLER");
		numRoutersAvail = connections.size();
		
		//Create the socket and the port
		socket = new DatagramSocket();
		port = socket.getLocalPort();
		listener.go();
		for(int i = 0; i < connections.size(); i++) {
			Router r = new Router(names.get(i), connections.get(i), this.port);
			routers.add(r);
		}
		
		//Connect Users on detached thread
		connectUser("Dave", "a");
		connectUser("Clara", "g");
		connectUser("Seamus", "d");
		connectUser("Harry", "n");
		
		System.out.println();
		System.out.println("Filling Connection List...");
		matrix = new ArrayList<ArrayList<Integer>>(connections);
		fillTable(connections);
		updateUserNetwork();
		System.out.println();
		System.out.println("Initialization completed successfully.");
	}
	
	public void fillTable(ArrayList<ArrayList<Integer>> matrix) {
		replaceConnections();
		//printMatrix(matrix, "ports");
		
		for(int i = 0; i < users.size(); i++) {		//For each user
			//Creates list of users other than current user
			ArrayList<EndUser> tempUsers = new ArrayList<EndUser>();
			for(int j = 0; j < users.size(); j++) {
				if(j != i) {
					tempUsers.add(users.get(j));	//Ports of the users that are not being read to
				}
			}
			
			ArrayList<Integer> flow = new ArrayList<Integer>(); //Will contain proper flow when finished
			
			for(int j = 0; j < tempUsers.size(); j++) {	//For each user that needs to be built to 
				flow = new ArrayList<Integer>();
				ArrayList<Integer> path = new ArrayList<Integer>();
				flow = Pathfinder.findPath(	matrix, 
									path, 
									getIndexFromPort(users.get(i).connectedRouter), 
									getIndexFromPort(tempUsers.get(j).connectedRouter));
				/* Will find an inefficient, although correct, path. Takes significantly less time than findPath.
				flow = Pathfinder.findPathFast(	matrix, 
						path, 
						getIndexFromPort(users.get(i).connectedRouter), 
						getIndexFromPort(tempUsers.get(j).connectedRouter));
				*/
				System.out.println("Path from " + users.get(i).username + " to " + tempUsers.get(j).username + ":" + flow);
				updateFlowTables(flow, users.get(i).port, tempUsers.get(j).port);
			}
		}
		
		for(int i = 0; i < routers.size(); i++) {
			if(!routers.get(i).flowTable.isEmpty()) routers.get(i).printFlowTable();
		}
	}
	
	private void replaceConnections(){	//Replaces only connection t/f with ports
		for(int i = 0; i < matrix.size(); i++) { 
			ArrayList<Integer> x = matrix.get(i);
			for(int j = 0; j < x.size(); j++) if(x.get(j) == 1) matrix.get(i).set(j, routers.get(j).port);
		}
	}
	
	private void updateUserNetwork() {
		for(EndUser user : users) {
			ArrayList<EndUser> tempUsers = new ArrayList<EndUser>();
			for(int i = 0; i < users.size(); i++) if(users.get(i) != user) tempUsers.add(users.get(i));
			for(int i = 0; i < tempUsers.size(); i++) user.networkUsers.add(tempUsers.get(i));
		}
	}
	
	private void updateFlowTables(ArrayList<Integer> flow, int senderPort, int recieverPort) {
		if(flow.size() > 1) {
			//Update flow tables of flow(0) to reflect start point and flow(1)
			int x = flow.get(0);
			routers.get(x).updateTable(senderPort, recieverPort);
			routers.get(x).updateTable(routers.get(flow.get(1)).port);
	
			//Update middle flow tables
			for(int i = 1; i < flow.size() - 1; i++) {
				x = flow.get(i);
				routers.get(x).updateTable(routers.get(flow.get(i - 1)).port, recieverPort); //previous port 
				routers.get(x).updateTable(routers.get(flow.get(i + 1)).port); //next port
			}
			//Update flow tables of flow(flow.size() - 1) to reflect flow(flow.size() - 2) point and end
			x = flow.get(flow.size() - 1);
			routers.get(x).updateTable(routers.get((flow.size() - 1)).port, recieverPort);
			routers.get(x).updateTable(recieverPort);
		}
	}
	
	public void printMatrix(ArrayList<ArrayList<Integer>> matrix, String type) {
		if(type.equals("binary")) System.out.println("MATRIX - binary");
		if(type.equals("ports")) System.out.println("MATRIX - portlisting");
		for(int i = 0; i < matrix.size(); i++) {
			System.out.println((i+1) + ":" + matrix.get(i).toString());
		}
		System.out.println();
	}
	
	public void connectUser(String name, String router) throws SocketException {
		if(numRoutersAvail > 0) {
			EndUser u = new EndUser(name);
			Thread t = new Thread(u);
			t.start();
			int x = getPortFromName(router);
			if(x != 0) {
				u.connectedRouter = x;
				System.out.println("User " + name + " successfully connected to Router " + router + " : Port " + x + ".");
				numRoutersAvail--;
				users.add(u);
			}
			else System.out.println("No router found of that name.");
		} else System.out.println("No available routers for connection.");
	}
	
	private int getPortFromName(String routerName) {
		for(int i = 0; i < routers.size(); i++) {
			if(routers.get(i).routerName.equals(routerName)) {
				return routers.get(i).port;
			}
		}
		return 0;
	}
	
	private int getIndexFromPort(int portNum) {
		for(int i = 0; i < routers.size(); i++) {
			if(routers.get(i).port == portNum) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void onReceipt(DatagramPacket packet) {
		PacketData p = new PacketData(packet);
		t.println("Recieved Wake from Router " + p.msg + " on Port " + p.srcPort);
	}
}
/*
             /
\\\' ,      / //
 \\\//    _/ //'
  \_-//' /  //<'
    \ ///  >   \\\`
    /,)-^>>  _\`
    (/   \\ / \\\
         //  //\\\
        ((`
        
     David Kubala
 */