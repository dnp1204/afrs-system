package AFRS.RequestFactories;

import AFRS.FileHandler;
import AFRS.ReservationDatabase;

import java.util.ArrayList;

public interface RequestFactory {

    ArrayList<String> makeRequest(String request, String clientID, FileHandler fh, ReservationDatabase rd, String[] params);

    ArrayList<String> getRequests();
}
