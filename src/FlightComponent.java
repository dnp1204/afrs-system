import java.util.Date;

public interface FlightComponent {
    public int getAirfare();
    public Date getDepartureTime();
    public Date getArrivalTime();
    public String getOrigin();
    public String getDestination();
}
