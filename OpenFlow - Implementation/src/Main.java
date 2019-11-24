import java.net.SocketException;
import java.util.ArrayList;

public class Main {
	public static void main(String args[]) throws SocketException {
		
		GraphGenerator g = new GraphGenerator();
		
		//Generates a random network
		//First operand is the # of Routers
		//second operand is the number of random connections between them 
		ArrayList<ArrayList<Integer>> y = g.generateGraph(100, 12);
		
		//Creates list of random router names
		ArrayList<String> names = g.generateNames(y.size());
		
		//Starts program
		Controller c = new Controller(y, names);
		c.toString();
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