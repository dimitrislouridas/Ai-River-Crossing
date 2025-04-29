//---------------------------------------------------------
//authors: Dimitris Louridas(p3200281) / Xristos Giapitzakis(p3200034)
//---------------------------------------------------------
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Receives inputs
        Scanner s = new Scanner(System.in);
        ArrayList<FamilyMember> family = new ArrayList<FamilyMember>();
        System.out.println("How many are the members of the family?");
        int N = Integer.parseInt(s.nextLine());
        for (int i = 0; i < N; i++) {
            System.out.println("What is the crossing time of the Family Member " + (i + 1) + "?");
            int ct = Integer.parseInt(s.nextLine());
            family.add(new FamilyMember(ct));
        }
        System.out.println("What is the crossing time limit? ");
        int limit = Integer.parseInt(s.nextLine());
        s.close();
        Collections.sort(family);

        // Two smallest crossing times
        State.all_min1 = family.get(family.size() - 1).getCrossingTime();
        State.all_min2 = family.get(family.size() - 2).getCrossingTime();

        // Creation of root
        State root = new State("right", new ArrayList<FamilyMember>(), family, 0);
        root.find_f();

        Solver a = new Solver();
        long start = System.currentTimeMillis();
        ArrayList<State> return_list = a.searcher(root);
        long end = System.currentTimeMillis();
        // Prints solution
        System.out.println("Execution time: " + (end - start) + " msec");
        if (limit < return_list.get(return_list.size() - 1).getG()) {
            System.out.println("Solution not found");
        } else {
            for (State i : return_list) {

                System.out.println("the lamp is on the " + i.get_lamp() + " side");

                Collections.sort(i.get_left());
                System.out.println("The left side after the change has: ");
                for (FamilyMember j : i.get_left()) {
                    System.out.println(j.getCrossingTime());
                }

                System.out.println(" ");

                System.out.println("The right side after the change has: ");
                for (FamilyMember j : i.get_right()) {
                    System.out.println(j.getCrossingTime());
                }

                System.out.println("----------------------------------------------");
            }
        }
        System.out.println("Crossing time : " + (return_list.get(return_list.size() - 1).getG()));

    }

}
