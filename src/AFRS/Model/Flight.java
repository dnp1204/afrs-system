package AFRS.Model;

import java.time.LocalTime;
import java.util.Date;

public class Flight implements FlightComponent {

    private String origin;
    private String destination;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private int airfare;
    private int flightNumber;

    public Flight(String ori, String dest, LocalTime departTime, LocalTime arriveTime, int airfareCost, int flightNumber) {

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

        String flightRundown = getFlightNumber() + "," + getOrigin() + "," + getDepartureTime() + "," + getDestination() + "," + getArrivalTime();

        return flightRundown;
    }


}