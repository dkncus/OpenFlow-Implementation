import java.net.DatagramPacket;
import java.net.InetSocketAddress;

public class PacketData {
	String msg;
	String type; 
	int srcPort;
	int destPort;
	
	PacketData(String msgString, String msgType, int src, int dst){ //New Packet Data
		this.msg = msgString;
		this.type = msgType;
		this.srcPort = src;
		this.destPort = dst;
	}
	
	PacketData(DatagramPacket packet){	//Create Packet from incoming packet
		String incoming = new String(packet.getData(), 0, packet.getLength());
		this.srcPort = Integer.parseInt(incoming.substring(0, 5));	//Length 5
		this.destPort = Integer.parseInt(incoming.substring(5, 10)); 	//Length 5
		this.type = incoming.substring(10, 13); 						//Length 3
		this.msg = incoming.substring(13);
	}
	
	public DatagramPacket prepSend(int port) { //Condense data into a datagram packet
		String augString = Integer.toString(this.srcPort);
		augString += Integer.toString(this.destPort);
		augString += this.type;
		augString += this.msg;
		byte[] msg = augString.getBytes();
		InetSocketAddress adr = new InetSocketAddress("localhost", port);
		DatagramPacket p = new DatagramPacket(msg, msg.length, adr);
		return p;
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