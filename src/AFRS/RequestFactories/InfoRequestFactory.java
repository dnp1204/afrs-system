package AFRS.RequestFactories;

import AFRS.FileHandler;
import AFRS.Requests.InfoRequest;
import AFRS.ReservationDatabase;

import java.util.ArrayList;

public class InfoRequestFactory implements RequestFactory {

    private ArrayList<String> requests;

    public InfoRequestFactory() {
        requests = new ArrayList<>();
        requests.add("info");
    }

    @Override
    public ArrayList<String> makeRequest(String request, String clientID, FileHandler fh, ReservationDatabase rd, String[] params) {
        return new InfoRequest(fh.getFlightDataList(), fh.getAirportInfo(clientID), rd).doRequest(clientID, params);
    }

    public ArrayList<String> getRequests() {
        return requests;
    }
}
