package AFRS.RequestFactories;

import AFRS.FileHandler;
import AFRS.Requests.DeleteRequest;
import AFRS.Requests.Request;
import AFRS.Requests.ReserveRequest;
import AFRS.Requests.RetrieveRequest;
import AFRS.ReservationDatabase;

import java.util.ArrayList;
import java.util.Stack;

public class ReservationRequestFactory implements RequestFactory {
    private Stack<Request> undoStack;//never assigned?
    private Stack<Request> redoStack;

    public void redo(){
        Request request = redoStack.pop();
        //make a reservation request
        undoStack.push(request);
    }

    public void undo(){
        Request request = undoStack.pop();
        //call delete
        redoStack.push(request);
    }

    @Override
    public ArrayList<String> makeRequest(FileHandler fh, ReservationDatabase rd, String[] params) {
        //choose logic
        switch (params[1]) {
            case "retrieve":
                return new RetrieveRequest(fh.getAirportMap(), rd).doRequest(params);
            case "reserve":
                return new ReserveRequest(rd).doRequest(params);
            case "delete":
                return new DeleteRequest(rd).doRequest(params);
            case "redo":
                redo();
                return  null;
            case "undo":
                undo();
                return null;
            default:
                break;
        }
        return null;
    }
}
