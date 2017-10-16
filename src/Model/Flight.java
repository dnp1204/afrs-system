package Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Flight implements FlightComponent {
    private String origin;
    private String destination;
    private int flightNumber;
    private int airfare;
    private Date departureTime;
    private Date arrivalTime;

    public Flight(String origin, String destination, Date departureTime, Date arrivalTime, int flightNumber, int airfare) {
        this.origin = origin;
        this.destination = destination;
        this.flightNumber = flightNumber;
        this.airfare = airfare;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    @Override
    public int getAirfare() {
        return airfare;
    }

    @Override
    public Date getDepartureTime() {
        return departureTime;
    }

    @Override
    public Date getArrivalTime() {
        return arrivalTime;
    }

    @Override
    public String getOrigin() {
        return origin;
    }

    @Override
    public String getDestination() {
        return destination;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public String toString() {
        DateFormat df = new SimpleDateFormat("h:mm a");
        return airfare + "," + flightNumber + "," + origin + "," + df.format(departureTime) + "," + destination + "," + df.format(arrivalTime);
    }
}
