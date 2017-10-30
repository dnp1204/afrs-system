package AFRS.RequestFactories;

import AFRS.FileHandler;
import AFRS.ReservationDatabase;

import java.util.ArrayList;

public interface RequestFactory {
    public ArrayList<String> makeRequest(FileHandler fh, ReservationDatabase rd, String[] params);
}
