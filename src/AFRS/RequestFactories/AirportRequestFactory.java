package AFRS.RequestFactories;

import AFRS.FileHandler;
import AFRS.Requests.AirportRequest;
import AFRS.ReservationDatabase;

import java.util.ArrayList;

public class AirportRequestFactory implements RequestFactory {
    @Override
    public ArrayList<String> makeRequest(FileHandler fh, ReservationDatabase rd, String[] params) {
        return new AirportRequest(fh.getAirportMap()).doRequest(params);
    }
}
