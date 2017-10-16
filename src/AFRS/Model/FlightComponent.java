package AFRS.Model;

import java.util.Date;

public interface FlightComponent {
    int getAirfare();
    Date getDepartureTime();
    Date getArrivalTime();
    String getOrigin();
    String getDestination();
}
