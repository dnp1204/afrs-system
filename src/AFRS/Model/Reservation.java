package AFRS.Model;

public class Reservation implements Comparable<Reservation> {
    private String passengerName;
    private Itinerary itinerary;

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String name){
        this.passengerName = name;
    }

    public Itinerary getItinerary() {
        return itinerary;
    }

    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
    }

    //this allows reservations to be sorted by origin flight. Necessary for retrieve command
    @Override
    public int compareTo(Reservation reservation) {
        String compare_origin = reservation.getItinerary().getOrigin();
        String my_origin = this.getItinerary().getOrigin();


        /* For Ascending order*/
        return compare_origin.compareToIgnoreCase(my_origin);

        /* For Descending order do like this */
            //return compareage-this.studentage;
        }

    @Override
    public String toString(){
        StringBuffer output = new StringBuffer();
        output.append(this.getPassengerName());
        output.append(",");
        output.append(this.getItinerary().toString());
        return String.valueOf(output);
    }

}
