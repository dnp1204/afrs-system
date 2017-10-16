package AFRS;

import AFRS.Requests.*;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestController {

    private static HashMap<String, Request> requests;
    private static ReservationDatabase reservationDB;

    public RequestController() {
        reservationDB = new ReservationDatabase();
        reservationDB.startUp();
        requests = new HashMap<>();
        requests.put("airport", new AirportRequest());
        requests.put("delete", new DeleteRequest(reservationDB));
        requests.put("info", new InfoRequest(reservationDB));
        requests.put("reserve", new ReserveRequest(reservationDB));
        requests.put("retrieve", new RetrieveRequest(reservationDB));
    }

    public ArrayList<String> parse(String str) {
        String input = str.substring(0, str.length() - 1);
        String[] requestAndParams = safeSplit(input);
        String request = requestAndParams[0];
        if(request.equals("quit")) {
            reservationDB.shutDown();

            ArrayList<String> response = new ArrayList<>();
            response.add("quit");
            return response;
        }
        String[] params = new String[requestAndParams.length - 1];
        for(int i = 1; i < requestAndParams.length; i++) {
            params[i-1] = requestAndParams[i];
        }

        if(requests.get(request) == null) {
            ArrayList<String> response = new ArrayList<>();
            response.add("error,unknown request");
            return response;
        }

        return requests.get(request).doRequest(params);
    }

    private String[] safeSplit(String input) {
        String[] initArray = input.split(",");
        for (String s:
             initArray) {
            if(s == null)
                s = "";
        }

        return initArray;
    }

}
