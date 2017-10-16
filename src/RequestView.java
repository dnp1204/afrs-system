import Requests.AirportRequest;
import Requests.InfoRequest;

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

        // Check info request
        System.out.println("\nInfo Request");
        InfoRequest infoRequest = new InfoRequest();
        String[] infoParams = {"SFO", "LAS"};

        ArrayList<String> result4 = infoRequest.doRequest(infoParams);
        System.out.println(result4.get(0));
    }
}
