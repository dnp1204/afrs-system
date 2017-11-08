package AFRS.Requests;

import java.util.ArrayList;

public interface Request {
    ArrayList<String> doRequest(String clientID, String[] params);
}
