//---------------------------------------------------------
//authors: Dimitris Louridas(p3200281) / Xristos Giapitzakis(p3200034)
//---------------------------------------------------------

public class FamilyMember implements Comparable<FamilyMember> {

    protected int crossingTime;
   //default
    public FamilyMember() {
    }
   //constructor
    public FamilyMember(int crossingTime) {
        this.crossingTime = crossingTime;
    }
    //set/get
    public void setCrossingTime(int crossingTime) {
        this.crossingTime = crossingTime;
    }

    public int getCrossingTime() {
        return crossingTime;
    }

    // compares 2 Family members based on time crossing them
    @Override
    public int compareTo(FamilyMember p) {
        return Integer.compare(p.getCrossingTime(), this.crossingTime);
    }

}
