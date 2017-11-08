package AFRS;

import AFRS.RequestFactories.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class RequestController {

    private static ArrayList<RequestFactory> requestFactories;
    private static FileHandler fileHandler;
    private static ReservationDatabase reservationDB;
    private static ArrayList<String> clientIDs;

    public RequestController() {
        reservationDB = new ReservationDatabase();
        fileHandler = new FileHandler();
        clientIDs = new ArrayList<>();
        reservationDB.startUp();
        requestFactories = new ArrayList<>();
        requestFactories.add(new AirportRequestFactory());
        requestFactories.add(new InfoRequestFactory());
        requestFactories.add(new ReservationRequestFactory());
    }

    public ArrayList<String> parse(String str) {
        String input = str.substring(0, str.length() - 1);
        String[] requestAndParams = safeSplit(input);
        if(requestAndParams[0].equals("connect")) {
            ArrayList<String> response = new ArrayList<>();
            String newID = createUUID();
            clientIDs.add(newID);
            fileHandler.setAirportInfo(newID);
            response.add("connect,"+newID);
            return response;
        }
        String clientID = requestAndParams[0];
        if(!clientIDs.contains(clientID)) {
            ArrayList<String> response = new ArrayList<>();
            response.add("error,invalid connection");
            return response;
        }
        String request = requestAndParams[1];
        if(request.equals("disconnect")) {
            ArrayList<String> response = new ArrayList<>();
            fileHandler.removeClient(clientID);
            reservationDB.removeClient(clientID);
            clientIDs.remove(clientID);
            response.add(clientID+",disconnect");
            return response;
        }
        if(request.equals("quit")) {
            try {
                reservationDB.shutDown();
            } catch(IOException e) {
                System.err.println("Error while shutting down reservation database. Data may not persist.");
            }

            ArrayList<String> response = new ArrayList<>();
            response.add("quit");
            return response;
        }
        String[] params = new String[requestAndParams.length - 2];
        for(int i = 2; i < requestAndParams.length; i++) {
            params[i-2] = requestAndParams[i];
        }

        boolean requestNotFound = true;
        ArrayList<String> response = new ArrayList<>();
        for (RequestFactory rf:
             requestFactories) {
            if(rf.getRequests().contains(request))
            {
                response = rf.makeRequest(request, clientID, fileHandler, reservationDB, params);
                requestNotFound = false;
            }
        }

        if(requestNotFound) {
            response.add(clientID+",error,unknown request");
            return response;
        } else {
            String firstResponse = clientID+","+response.get(0);
            response.set(0,firstResponse);
            return response;
        }
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

    private String createUUID() {
        return UUID.randomUUID().toString();
    }

}
