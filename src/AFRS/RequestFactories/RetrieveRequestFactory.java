package AFRS.RequestFactories;

import AFRS.FileHandler;
import AFRS.RequestFactories.RequestFactory;
import AFRS.Requests.RetrieveRequest;
import AFRS.ReservationDatabase;

import java.util.ArrayList;

public class RetrieveRequestFactory implements RequestFactory {
    @Override
    public ArrayList<String> makeRequest(FileHandler fh, ReservationDatabase rd, String[] params) {
        return new RetrieveRequest(fh.getAirportMap(), rd).doRequest(params);
    }
}
