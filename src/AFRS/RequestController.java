package AFRS;

import AFRS.RequestFactories.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RequestController {

    private static HashMap<String, RequestFactory> requestFactories;
    private static FileHandler fileHandler;
    private static ReservationDatabase reservationDB;

    public RequestController() {
        reservationDB = new ReservationDatabase();
        fileHandler = new FileHandler();
        reservationDB.startUp();
        requestFactories = new HashMap<>();
        requestFactories.put("airport", new AirportRequestFactory());
        requestFactories.put("delete", new DeleteRequestFactory());
        requestFactories.put("info", new InfoRequestFactory());
        requestFactories.put("reserve", new ReserveRequestFactory());
        requestFactories.put("retrieve", new RetrieveRequestFactory());
    }

    public ArrayList<String> parse(String str) throws IOException{
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

        if(requestFactories.get(request) == null) {
            ArrayList<String> response = new ArrayList<>();
            response.add("error,unknown request");
            return response;
        }

        return requestFactories.get(request).makeRequest(fileHandler, reservationDB, params);
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
