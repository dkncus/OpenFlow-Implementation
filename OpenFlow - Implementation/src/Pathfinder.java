import java.util.ArrayList;

public class Pathfinder {
	
	public static void printMatrix(ArrayList<ArrayList<Integer>> matrix, String type) {
		if(type.equals("binary")) System.out.println("MATRIX - binary");
		if(type.equals("ports")) System.out.println("MATRIX - portlisting");
		for(int i = 0; i < matrix.size(); i++) {
			if(i < 10) System.out.println((i) + ":  " + matrix.get(i).toString());
			if(i >= 10)System.out.println((i) + ": " + matrix.get(i).toString());
		}
		System.out.println();
	}
	
	public static ArrayList<Integer> findPath(ArrayList<ArrayList<Integer>> matrix, ArrayList<Integer> path, int start, int end){
		if(start == end) {
			path.add(start);
			return path;
		}
		path.add(start);
		
		ArrayList<Integer> node = matrix.get(start); //Node list (Row of the given matrix)
		ArrayList<ArrayList<Integer>> paths = new ArrayList<ArrayList<Integer>>(); //For each entry in Matrix Row
		
		for(int i = 0; i < node.size(); i++) { 
			if(!path.contains(i) && node.get(i) != 0){	//if a 1 is scanned from the array & is not in the path already
				ArrayList<Integer> p = new ArrayList<Integer>(path); 	//Copy the path array
				p = findPath(matrix, p, i, end);						//Find the next path down
				if(p != null) paths.add(p);								//Add the path to the pathlist if null is not returned											
			}
		}
		//Return shortest array
		if(!paths.isEmpty()) {
			ArrayList<Integer> shortest = new ArrayList<Integer>(paths.get(0));
			for(ArrayList<Integer> p : paths) if(p.size() < shortest.size()) shortest = p;
			return shortest;
		} else return null;
	}
	
	//Returns *a* path. Does not return the most *efficient* path. But it does it faster.
	public static ArrayList<Integer> findPathFast(ArrayList<ArrayList<Integer>> matrix, ArrayList<Integer> path, int start, int end){
		if(start == end) {
			path.add(start);
			return path;
		}
		path.add(start);
		
		ArrayList<Integer> node = matrix.get(start); //Node list (Row of the given matrix)
		
		for(int i = 0; i < node.size(); i++) { 
			if(!path.contains(i) && node.get(i) != 0){	//if a 1 is scanned from the array & is not in the path already
				ArrayList<Integer> p = new ArrayList<Integer>(path); 	//Copy the path array
				p = findPathFast(matrix, p, i, end);						//Find the next path down
				if(p != null) return p;								//Add the path to the pathlist if null is not returned											
			}
		} 
		return null;
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
