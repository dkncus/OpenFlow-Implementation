import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GraphGenerator {
	ArrayList<ArrayList<Integer>> graph = new ArrayList<ArrayList<Integer>>();
	
	
	public ArrayList<ArrayList<Integer>> generateGraph(int numNodes, int S){ //S is however many edges there should be in addition to each connected node
		//Initialize Graph
		for(int i = 0; i < numNodes; i++) {
			ArrayList<Integer> row = new ArrayList<Integer>();
			for(int j = 0; j < numNodes; j++) {
				row.add(0);
			}
			graph.add(row);
		}
		
		ArrayList<Integer> nodesSet = new ArrayList<Integer>();
		
		//Create several connected nodes at random
		int prevNode = 0;
		for(int i = 0; i < numNodes - 2; i++) {
			//Creates a random node with excluded values from nodesSet
			int randomNode = generateRandom(-1, numNodes - 3, nodesSet);
			
			//Sets Nodes in the graph
			graph.get(prevNode).set(randomNode, 1);
			graph.get(randomNode).set(prevNode, 1);
			prevNode = randomNode;
			
			//Makes sure the node is not repeated in the chain
			nodesSet.add(randomNode);
		}
		Random r = new Random();
		//Create random links based on S
		for(int i = 0; i < S; i++) {
			int first = r.nextInt(numNodes);
			int second = r.nextInt(numNodes);
			if(first != second) {
				graph.get(first).set(second, 1);
				graph.get(second).set(first, 1);
			} else i--;
		}
		return graph;
		
	}
	
	public int generateRandom(int start, int end, ArrayList<Integer> excludeRows) {
	    Random rand = new Random();
	    int range = end - start + 1;
	    
	    Collections.sort(excludeRows);
	    
	    int random = rand.nextInt(range) + 1;
	    while(excludeRows.contains(random)) {
	        random = rand.nextInt(range) + 1;
	    }

	    return random;
	}
	
	public ArrayList<String> generateNames(int y) {
		int counter = 33;
		ArrayList<String> names = new ArrayList<String>();
		for(int i = 0; i < y; i++) {
			if((counter >= 33 && counter < 126)||(counter > 160)) {
				names.add(Character.toString((char) counter));
			} else {
				names.add(Integer.toString(counter));
			}
			counter++;
		}
		return names;
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