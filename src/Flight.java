import java.util.Date;

public class Flight implements FlightComponent {
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
}
