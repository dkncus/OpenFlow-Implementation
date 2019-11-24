import java.util.ArrayList;

public class FlowTable {
	ArrayList<Integer> portsIn = new ArrayList<Integer>();
	ArrayList<Integer> portsOut = new ArrayList<Integer>();
	ArrayList<Integer> names = new ArrayList<Integer>();
	
	boolean isEmpty() {
		if(portsIn.isEmpty() && portsOut.isEmpty()) return true;
		else return false;
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