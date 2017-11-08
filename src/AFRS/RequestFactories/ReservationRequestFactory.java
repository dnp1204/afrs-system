package AFRS.RequestFactories;

import AFRS.FileHandler;
import AFRS.Requests.DeleteRequest;
import AFRS.Requests.Request;
import AFRS.Requests.ReserveRequest;
import AFRS.Requests.RetrieveRequest;
import AFRS.ReservationDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class ReservationRequestFactory implements RequestFactory {

    private ArrayList<String> requests;

    public ReservationRequestFactory() {
        requests = new ArrayList<>();
        requests.add("delete");
        requests.add("redo");
        requests.add("reserve");
        requests.add("retrieve");
        requests.add("undo");
    }

    @Override
    public ArrayList<String> makeRequest(String request, String clientID, FileHandler fh, ReservationDatabase rd, String[] params) {
        ArrayList<String> response;
        switch(request) {
            case "retrieve":
                return new RetrieveRequest(fh.getAirportInfo(clientID), rd).doRequest(clientID, params);
            case "reserve":
                return new ReserveRequest(rd).doRequest(clientID, params);
            case "delete":
                return new DeleteRequest(rd).doRequest(clientID, params);
            case "redo":
                response = new ArrayList<>();
                response.add(rd.redo(clientID));
                return response;
            case "undo":
                response = new ArrayList<>();
                response.add(rd.undo(clientID));
                return response;
        }
        return null;
    }

    public ArrayList<String> getRequests() {
        return requests;
    }
}
