//---------------------------------------------------------
//authors: Dimitris Louridas(p3200281) / Xristos Giapitzakis(p3200034)
//---------------------------------------------------------
import java.util.ArrayList;
import java.util.Collections;

public class Solver {

    private ArrayList<State> front;

    // executes the A* algorithm and removes the States that are equal to the one
   
    public ArrayList<State> searcher(State root) {
        ArrayList<State> return_list = new ArrayList<State>();// The list contains the States of the path to the end
        this.front = new ArrayList<State>();
        this.front.add(root);
        State current;
        // Search for the path
        while (!this.front.isEmpty()) {
            current = this.front.remove(0);
            // Removes States with f equal to current's
            for (int i = 0; i < this.front.size(); i++) {
                if (this.front.get(i).getF() == current.getF()) {
                    this.front.remove(i);
                }
            }
            // Returns the solution if the State is terminal
            if (current.isTerminal()) {
                return_list.add(current);
                return return_list;
            }
            // Adds the current State and its children to the front and sorts it
            return_list.add(current);
            this.front.addAll(current.get_children());
            Collections.sort(this.front);
        }

        System.out.println("Null");
        return null;
    }

}
