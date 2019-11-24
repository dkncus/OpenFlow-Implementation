import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class Router extends Node {
	FlowTable flowTable = new FlowTable();
	InetSocketAddress address;
	int port;
	String routerName;
	int controllerPort;
	
	Router(String name, ArrayList<Integer> x, int controllerPort) throws SocketException{
		super();
		this.routerName = name;
		this.controllerPort = controllerPort;
		socket = new DatagramSocket();
		this.port = socket.getLocalPort();
		System.out.println("Router " + this.routerName + " Connected on PORT " + this.port);
		this.address = new InetSocketAddress("localhost", port);
		listener.go();
		
		PacketData p = new PacketData(this.routerName, "MSG", this.port, this.controllerPort);
		DatagramPacket q = p.prepSend(controllerPort);
		try {
			socket.send(q);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onReceipt(DatagramPacket packet) {
		System.out.println("Packet Recieved by router " + this.routerName);
		PacketData p = new PacketData(packet);
		int forward = p.destPort;
		System.out.println("DESTINATION : " + p.destPort);
		System.out.println("TYPE        : " + p.type);
		int f = findForwardPort(forward);
		System.out.println("Forwarding to " + f);
		DatagramPacket sendPacket = p.prepSend(f);
		try {
			socket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int findForwardPort(int n) {
		for(int i = 0; i < flowTable.names.size(); i++) {
			if(flowTable.names.get(i) == n) return flowTable.portsOut.get(i);
		} 
		return -1;
	}
	public void updateTable(int port, int name) {
		flowTable.portsIn.add(port);
		flowTable.names.add(name);
	}
	
	public void updateTable(int port) {
		flowTable.portsOut.add(port);
	}
	
	public void printFlowTable() {
		System.out.println();
		System.out.println("Router " + routerName + " on Port " + port);
		System.out.println("  -DEST-  -PORTSIN- -PORTS OUT-");
		for(int i = 0; i < flowTable.portsIn.size(); i++) 
			System.out.println(i + ": " + flowTable.names.get(i) + "    " + flowTable.portsIn.get(i) + "      " + flowTable.portsOut.get(i)); 
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