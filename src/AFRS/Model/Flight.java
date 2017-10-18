package AFRS.Model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Flight implements FlightComponent {

    private String origin;
    private String destination;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private int airfare;
    private int flightNumber;

    //public Flight(String ori, String dest, LocalTime departTime, LocalTime arriveTime, int airfareCost, int flightNumber) {
    public Flight(int flightNumber, String ori, LocalTime departTime, String dest, LocalTime arriveTime, int airfareCost) {

        origin = ori;
        destination = dest;
        departureTime = departTime;
        arrivalTime = arriveTime;
        airfare = airfareCost;
        this.flightNumber = flightNumber;

    }


    public int getAirfare() {
        return airfare;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public int getFlightNumber() { return flightNumber; }

    public String toString() {

        DateTimeFormatter format = DateTimeFormatter.ofPattern("hh:mm");

        String arrivalM = "a";
        String departM = "a";

        if (arrivalTime.getHour() >= 12) {
            arrivalM = "p";
        }

        if (departureTime.getHour() >= 12) {
            departM = "p";
        }

        String flightRundown = getFlightNumber() + "," + getOrigin() + "," + getDepartureTime().format(format) + departM + "," + getDestination() + "," + getArrivalTime().format(format) + arrivalM;

        return flightRundown;
    }


}