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
    private static HashMap<String, Stack<String>> undoRequestStack;
    private static HashMap<String, Stack<String[]>> undoParamStack;
    private static HashMap<String, Stack<String>> redoRequestStack;
    private static HashMap<String, Stack<String[]>> redoParamStack;

    private ArrayList<String> requests;

    public ReservationRequestFactory() {
        undoRequestStack = new HashMap<>();
        undoParamStack = new HashMap<>();
        redoRequestStack = new HashMap<>();
        redoParamStack = new HashMap<>();
        requests = new ArrayList<>();
        requests.add("delete");
        requests.add("redo");
        requests.add("reserve");
        requests.add("retrieve");
        requests.add("undo");
    }

    @Override
    public ArrayList<String> makeRequest(String request, String clientID, FileHandler fh, ReservationDatabase rd, String[] params) {
        checkStacks(clientID);
        ArrayList<String> response;
        String[] responseSplit;
        switch(request) {
            case "retrieve":
                return new RetrieveRequest(fh.getAirportInfo(clientID), rd).doRequest(clientID, params);
            case "reserve":
                response = new ReserveRequest(rd).doRequest(clientID, params);
                responseSplit = response.get(0).split(",");
                if(!responseSplit[1].equals("error")) {
                    undoRequestStack.get(clientID).push(request);
                    undoParamStack.get(clientID).push(params);
                }
                return response;
            case "delete":
                response = new DeleteRequest(rd).doRequest(clientID, params);
                responseSplit = response.get(0).split(",");
                if(!responseSplit[1].equals("error")) {
                    undoRequestStack.get(clientID).push(request);
                    undoParamStack.get(clientID).push(params);
                }
                return response;
            case "redo":
                if(redoRequestStack.get(clientID).empty() || redoParamStack.get(clientID).empty())
                {
                    response = new ArrayList<>();
                    response.add(clientID+",error,no request available");
                    return response;
                }
                String redoRequest = redoRequestStack.get(clientID).pop();
                String[] redoParam = redoParamStack.get(clientID).pop();
                undoRequestStack.get(clientID).push(redoRequest);
                undoParamStack.get(clientID).push(redoParam);
                if(redoRequest.equals("delete"))
                    return new DeleteRequest(rd).doRequest(clientID, redoParam);
                else
                    return new ReserveRequest(rd).doRequest(clientID, redoParam);
            case "undo":
                if(undoRequestStack.get(clientID).empty() || undoParamStack.get(clientID).empty())
                {
                    response = new ArrayList<>();
                    response.add(clientID+",error,no request available");
                    return response;
                }
                String undoRequest = undoRequestStack.get(clientID).pop();
                String[] undoParam = undoParamStack.get(clientID).pop();
                redoRequestStack.get(clientID).push(undoRequest);
                redoParamStack.get(clientID).push(undoParam);
                if(undoRequest.equals("delete"))
                    return new DeleteRequest(rd).doRequest(clientID, undoParam);
                else
                    return new ReserveRequest(rd).doRequest(clientID, undoParam);
        }
        return null;
    }

    public ArrayList<String> getRequests() {
        return requests;
    }

    private static void checkStacks(String clientID) {
        undoRequestStack.computeIfAbsent(clientID, k -> new Stack<>());
        undoParamStack.computeIfAbsent(clientID, k -> new Stack<>());
        redoRequestStack.computeIfAbsent(clientID, k -> new Stack<>());
        redoParamStack.computeIfAbsent(clientID, k -> new Stack<>());
    }
}
