package AFRS.RequestFactories;

import AFRS.FileHandler;
import AFRS.RequestFactories.RequestFactory;
import AFRS.Requests.ReserveRequest;
import AFRS.ReservationDatabase;

import java.util.ArrayList;

public class ReserveRequestFactory implements RequestFactory {
    @Override
    public ArrayList<String> makeRequest(FileHandler fh, ReservationDatabase rd, String[] params) {
        return new ReserveRequest(rd).doRequest(params);
    }
}
