package AFRS.Model;

import java.time.LocalTime;
import java.util.Date;

public interface FlightComponent {
    int getAirfare();
    LocalTime getDepartureTime();
    LocalTime getArrivalTime();
    String getOrigin();
    String getDestination();
}
