package AFRS.AirportInfo;

import AFRS.Model.Airport;

import java.util.HashMap;

public class LocalAirportInfo implements AirportInfo {
    private HashMap<String, Airport> airportInfoHashMap;

    public LocalAirportInfo(HashMap<String, Airport> airportInfoHashMap) {
        this.airportInfoHashMap = airportInfoHashMap;
    }

    @Override
    public HashMap<String, Airport> getInfo() {
       return airportInfoHashMap;
    }
}
