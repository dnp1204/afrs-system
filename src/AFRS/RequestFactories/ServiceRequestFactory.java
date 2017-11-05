package AFRS.RequestFactories;

import AFRS.FileHandler;
import AFRS.ReservationDatabase;
import AFRS.Requests.ServiceRequest;

import java.util.ArrayList;

public class ServiceRequestFactory implements RequestFactory {
    @Override
    public ArrayList<String> makeRequest(FileHandler fh, ReservationDatabase rd, String[] params) {
        return new ServiceRequest(fh).doRequest(params);
    }
}
