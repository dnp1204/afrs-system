package AFRS.RequestFactories;

import AFRS.FileHandler;
import AFRS.RequestFactories.RequestFactory;
import AFRS.Requests.DeleteRequest;
import AFRS.ReservationDatabase;

import java.util.ArrayList;

public class DeleteRequestFactory implements RequestFactory {
    @Override
    public ArrayList<String> makeRequest(FileHandler fh, ReservationDatabase rd, String[] params) {
        return new DeleteRequest(rd).doRequest(params);
    }
}
