package AFRS.Model;

import java.util.Date;

public class Flight implements FlightComponent {

    public Flight() { }

    @Override
    public int getAirfare() {
        return 0;
    }

    @Override
    public Date getDepartureTime() {
        return null;
    }

    @Override
    public Date getArrivalTime() {
        return null;
    }

    @Override
    public String getOrigin() {
        return null;
    }

    @Override
    public String getDestination() {
        return null;
    }

    public int getFlightNumber() {
        return 0;
    }
}
