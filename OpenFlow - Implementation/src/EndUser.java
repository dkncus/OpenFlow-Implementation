import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EndUser extends Node implements Runnable {
	Terminal t;
	String username;
	int connectedRouter;
	int port;
	InetSocketAddress address;
	ArrayList<EndUser> networkUsers = new ArrayList<EndUser>();
	
	EndUser(String inputName) throws SocketException{
		t = new Terminal(inputName);
		this.username = inputName;
		socket = new DatagramSocket();
		this.port = socket.getLocalPort();
		address = new InetSocketAddress("localhost", port);
		listener.go();
	}
	
	public void onReceipt(DatagramPacket packet) {
		PacketData p = new PacketData(packet);
		if(p.type.equals("MSG")) { 
			long ms = System.currentTimeMillis();
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");    
			Date resultdate = new Date(ms);
			String sendUser = "";
			for(int i = 0; i < networkUsers.size(); i++) 
				if(networkUsers.get(i).port == p.srcPort) 
					sendUser = networkUsers.get(i).username;
			t.println(sendUser + " : " + sdf.format(resultdate));
			t.println(p.msg);
			PacketData ack = new PacketData(username, "ACK", this.port, p.srcPort);
			DatagramPacket pack = ack.prepSend(p.srcPort);
			try {
				socket.send(pack);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(p.type.equals("ACK")) {
			t.println(p.msg + " recieved your message!");
		}
	}
	
	public void run() {
		boolean firstRun = true;
		while(true) {
			if(firstRun) {
				t.read("Press Any Key...");
				printUserList();
				firstRun = false;
			} else {
				String s = t.read("User?");
				int index = -1;
				int sendPort = -1;
				String userName= "-1";
				for(int i = 0; i < networkUsers.size(); i++) {
					if(s.equals(networkUsers.get(i).username)) {
						index = i;
						sendPort = networkUsers.get(i).port;
						userName = networkUsers.get(i).username;
					}
				}
				if(index != -1) {
					t.println("MESSAGE - Sending to " + userName);
					s = t.read("Message?");
					t.println(s);
					t.println("");
					PacketData p = new PacketData(s, "MSG", port, sendPort);
					System.out.println(connectedRouter);
					DatagramPacket packet = p.prepSend(connectedRouter);
					try {socket.send(packet);}
					catch (IOException e) {e.printStackTrace();}
				} else {
					t.println("Unrecognized User");
					t.println("");
				}
			}
		}
	}
	
	private void printUserList() {
		t.println(":Users on the Network:");
		for(EndUser user : networkUsers){
			String augString = "Port " + user.port + " : " + user.username;
			t.println(augString);
		}
		t.println("");
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