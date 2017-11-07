package AFRS.RequestFactories;

import AFRS.FileHandler;
import AFRS.Requests.InfoRequest;
import AFRS.ReservationDatabase;

import java.util.ArrayList;

public class InfoRequestFactory implements RequestFactory {
    @Override
    public ArrayList<String> makeRequest(FileHandler fh, ReservationDatabase rd, String[] params) {
        return new InfoRequest(fh.getFlightDataList(), fh.getAirportMap(), rd, "asd").doRequest(params);
    }
}
