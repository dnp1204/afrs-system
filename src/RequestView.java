import Requests.AirportRequest;

import java.util.ArrayList;

public class RequestView {
    public static void main(String[] args) {
        // Check airport request
        System.out.println("Airport Request");
        AirportRequest airportRequest = new AirportRequest();
        String[] params = {"ATL"};

        ArrayList<String> result = airportRequest.doRequest(params);
        System.out.println(result.get(0));

        ArrayList<String> result2 = airportRequest.doRequest(params);
        System.out.println(result2.get(0));

        ArrayList<String> result3 = airportRequest.doRequest(params);
        System.out.println(result3.get(0));
    }
}
