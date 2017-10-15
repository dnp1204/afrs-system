package Requests;

import Model.Airport;
import Model.WeatherInformation;
import Requests.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class AirportRequest implements Request {

    private final String DIR = "/Data/";
    private final String AIRPORTSCODE = "airports.txt";
    private final String WEATHERINFO = "weather.txt";

    private HashMap<String, Airport> airportHashMap;

    public AirportRequest() {
        airportHashMap = new HashMap<>();
        readData();
    }

    public void readData() {
        BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(DIR + AIRPORTSCODE)));
        String line;

        try {
            line = br.readLine();
            while (line != null) {
                String[] airport = line.split(",");
                airportHashMap.put(airport[0], new Airport(airport[0], airport[1]));
                line = br.readLine();
            }

            br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(DIR + WEATHERINFO)));
            line = br.readLine();
            while (line != null) {
                String[] weather = line.split(",");
                String airportCode = weather[0];

                for (int i = 0; i < weather.length; i += 2) {
                    WeatherInformation temp = new WeatherInformation(weather[i], weather[i + 1]);
                    airportHashMap.get(airportCode).addWeatherToList(temp);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<String> doRequest(String[] params) {
        ArrayList<String> result = new ArrayList<>();

        if (airportHashMap.containsKey(params)) {
            result.add(airportHashMap.get(params).displayAirportInfo());
        } else {
            result.add("error,unknown airport");
        }

        return result;
    }
}
