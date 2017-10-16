import Requests.AirportRequest;
import Requests.InfoRequest;

import java.util.ArrayList;

public class RequestView {
    public static void main(String[] args) {
        // Check airport request
        AirportRequest airportRequest = new AirportRequest();

        String[] params = {"ATL"};
        ArrayList<String> result = airportRequest.doRequest(params);
        System.out.println(result.get(0));

        ArrayList<String> result2 = airportRequest.doRequest(params);
        System.out.println(result2.get(0));

        ArrayList<String> result3 = airportRequest.doRequest(params);
        System.out.println(result3.get(0));

        // Check info request
        InfoRequest infoRequest = new InfoRequest();
    }
}
