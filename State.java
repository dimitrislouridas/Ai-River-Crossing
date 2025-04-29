//---------------------------------------------------------
//authors: Dimitris Louridas(p3200281) / Xristos Giapitzakis(p3200034)
//---------------------------------------------------------
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

public class State implements Comparable<State> {
    private String lamp;
    private ArrayList<FamilyMember> left;
    private ArrayList<FamilyMember> right;
    private int g;
    private int f;

    static int all_min1;
    static int all_min2;

    // default
    public State() {
        this.f = 0;
        this.g = 0;
        this.lamp = "right";
        this.left = new ArrayList<FamilyMember>();
        this.right = new ArrayList<FamilyMember>();
    }

    // Parameterized constructor
    public State(String lamp, ArrayList<FamilyMember> left, ArrayList<FamilyMember> right, int g) {
        this.lamp = lamp;
        this.g = g;
        this.left = new ArrayList<FamilyMember>();
        this.right = new ArrayList<FamilyMember>();
        for (FamilyMember p : left) {
            this.left.add(p);
        }
        for (FamilyMember p : right) {
            this.right.add(p);
        }
    }

    
    public ArrayList<State> get_children() {
        String children_lamp;
        ArrayList<State> children = new ArrayList<State>();
        if (lamp.equals("left")) {

            children_lamp = "right";
            Set<Set<FamilyMember>> comb = combinations(this.left);

            // Creates the children based on the combinations of people
            for (Set<FamilyMember> s : comb) {

                if (s.size() == 1) {

                    int children_g = this.g;
                    ArrayList<FamilyMember> l = new ArrayList<FamilyMember>();
                    for (FamilyMember i : this.left) {
                        l.add(i);
                    }
                    ArrayList<FamilyMember> r = new ArrayList<FamilyMember>();
                    for (FamilyMember i : this.right) {
                        r.add(i);
                    }

                    // Finds the maximum crossing time of the people moving
                    int max = -1;
                    for (FamilyMember p : s) {
                        if (max < p.getCrossingTime()) {
                            max = p.getCrossingTime();
                        }
                        l.remove(p);
                        r.add(p);
                    }
                    children_g += max;

                    // Creation of child
                    State n = new State(children_lamp, l, r, children_g);
                    n.find_f();
                    children.add(n);

                }
            }
        } else {

            children_lamp = "left";
            Set<Set<FamilyMember>> comb = combinations(this.right);

            // Creates the children based on the combinations of people
            for (Set<FamilyMember> s : comb) {

                if (s.size() == 2) {

                    int children_g = this.g;
                    ArrayList<FamilyMember> l = new ArrayList<FamilyMember>();
                    for (FamilyMember i : this.left) {
                        l.add(i);
                    }
                    ArrayList<FamilyMember> r = new ArrayList<FamilyMember>();
                    for (FamilyMember i : this.right) {
                        r.add(i);
                    }

                    // Finds the maximum crossing time of the people moving
                    int max = -1;
                    for (FamilyMember p : s) {
                        if (max < p.getCrossingTime()) {
                            max = p.getCrossingTime();
                        }
                        r.remove(p);
                        l.add(p);
                    }
                    children_g += max;

                    // Creation of child
                    State n = new State(children_lamp, l, r, children_g);
                    n.find_f();
                    children.add(n);
                }
            }
        }

        return children;
    }

    // Produces a set of all the possible combinations of people in arr
    public Set<Set<FamilyMember>> combinations(ArrayList<FamilyMember> arr) {
        Set<Set<FamilyMember>> comb = new HashSet<Set<FamilyMember>>();
        for (FamilyMember i : arr) {
            for (FamilyMember j : arr) {
                Set<FamilyMember> s = new HashSet<FamilyMember>();
                s.add(i);
                s.add(j);
                comb.add(s);
            }
        }
        return comb;
    }

    // Contains heuristic
    public void find_f() {
        int h = 0;
        Collections.sort(right);// In descending order
        int n = left.size() + right.size();// The number of people
        int min1;
        int min2;

        // Finds the total cost for crossing without the lamp constraint
        int[] helper = new int[right.size()];
        int i = 0;
        for (FamilyMember p : this.right) {
            helper[i] = p.getCrossingTime();
            i++;
        }
        bubbleSort(helper);

        for (i = 0; i < right.size(); i = i + 2) {
            // Do not include the two smallest crossing times
            if ((helper[i] != State.all_min1) && (helper[i] != State.all_min2)) {
                h = h + helper[i];
            }
        }

        // Finds the two smallest crossing times on the left side
        int min1left = Integer.MAX_VALUE;
        int min2left = Integer.MAX_VALUE;
        for (FamilyMember l : this.left) {
            if (min1left > l.getCrossingTime()) {
                min2left = min1left;
                min1left = l.getCrossingTime();
            } else if (min2left > l.getCrossingTime()) {
                min2left = l.getCrossingTime();
            }
        }

        // Finds the two smallest crossing times on the right side
        int min1right = Integer.MAX_VALUE;
        int min2right = Integer.MAX_VALUE;
        for (FamilyMember l : this.right) {
            if (min1right > l.getCrossingTime()) {
                min2right = min1right;
                min1right = l.getCrossingTime();
            } else if (min2right > l.getCrossingTime()) {
                min2right = l.getCrossingTime();
            }
        }

        if (this.lamp.equals("left")) {

            // Checks if the number of people on the left side is even or odd
            if (left.size() % 2 == 0) {
                min2 = min2left;
                min1 = min1left;
            } else {
                min2 = min1left;
                min1 = min1right;
            }

            if ((right.size() == 1) && (n % 2 != 0)) {
                if (right.get(0).getCrossingTime() == State.all_min1) {
                    h = h + min1left;
                } else if (right.get(0).getCrossingTime() == State.all_min2) {
                    h = h + min1right;
                }
            }

            // Heuristic function
            h = h + ((right.size() + n % 2) / 2) * min1 + ((right.size() + 1 - n % 2) / 2) * 2 * min2;

        } else {

            // Checks if the number of the people on the right side is odd or even//
            if (((right.size() % 2 == 0) && (n % 2 != 0)) || (right.size() % 2 != 0) && (n % 2 == 0)) {
                min2 = min1left;
                // Resolves a case of tie between States with equal f
                if (min2 == State.all_min1) {
                    min2 = State.all_min2;
                }
                min1 = min1right;
            } else {
                min2 = min2right;
                min1 = min1right;
            }

            // Heuristic function
            if (right.size() > 1 + n % 2) {
                h = h + (right.size() - 1 - n % 2) * min2;
            }

            if (right.size() + n % 2 > 2) {
                h = h + (((right.size() - 2 + n % 2) / 2) * min1);
            }
        }

        this.f = h + this.g;
    }

    public int getG() {
        return this.g;
    }

    public String get_lamp() {
        return this.lamp;
    }

    public ArrayList<FamilyMember> get_left() {
        return this.left;
    }

    public ArrayList<FamilyMember> get_right() {
        return this.right;
    }

    public Boolean isTerminal() {
        return this.right.isEmpty();
    }

    public int getF() {
        return this.f;
    }

    @Override
    public int compareTo(State n) {
        return Integer.compare(this.f, n.getF());
    }

    // sorts a group of integers from the older to the younger
    public void bubbleSort(int[] arr) {
        int n = arr.length;
        int temp = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {
                if (arr[j - 1] < arr[j]) {
                    // swap elements
                    temp = arr[j - 1];
                    arr[j - 1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }
}