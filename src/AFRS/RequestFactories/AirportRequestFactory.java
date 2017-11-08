package AFRS.RequestFactories;

import AFRS.FileHandler;
import AFRS.Requests.AirportRequest;
import AFRS.Requests.ServerRequest;
import AFRS.ReservationDatabase;

import java.util.ArrayList;

public class AirportRequestFactory implements RequestFactory {

    private ArrayList<String> requests;

    public AirportRequestFactory() {
        requests = new ArrayList<>();
        requests.add("airport");
        requests.add("server");
    }

    @Override
    public ArrayList<String> makeRequest(String request, String clientID, FileHandler fh, ReservationDatabase rd, String[] params) {
        switch(request) {
            case "airport":
                return new AirportRequest(fh.getAirportInfo(clientID)).doRequest(clientID, params);
            case "server":
                return new ServerRequest(fh).doRequest(clientID, params);
        }
        return null;
    }

    public ArrayList<String> getRequests() {
        return requests;
    }
}
