package AFRS.Requests;

import AFRS.Model.Airport;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The concrete command for querying an airport for it's Airport Information.
 * Implements the Request interface.
 */
public class AirportRequest implements Request {

    private HashMap<String, Airport> airportHashMap;
    private HashMap<String, Airport> airportServicesHashMap;

    /**
     * constructor for AirportRequest
     */
    public AirportRequest(HashMap<String, Airport> airportHashMap, HashMap<String, Airport> airportServicesHashMap) {
        this.airportHashMap = airportHashMap;
        this.airportServicesHashMap = airportServicesHashMap;
    }

    /**
     * the doRequest method that override from Request interface. It will be
     * called by the RequestController
     * @return result: ArrayList - return the final result to the request view
     * to display it to the client
     */
    @Override
    public ArrayList<String> doRequest(String[] params) {
        ArrayList<String> result = new ArrayList<>();

        if (airportHashMap.containsKey(params[0])) {
            result.add(airportHashMap.get(params[0]).displayAirportInfo());
        } else {
            result.add("error,unknown airport");
        }

        return result;
    }
}
