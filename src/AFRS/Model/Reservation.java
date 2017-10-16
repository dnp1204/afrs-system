package AFRS.Model;

public class Reservation {
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
}
