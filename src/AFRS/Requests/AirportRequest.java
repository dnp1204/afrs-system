package AFRS.Requests;

import AFRS.Model.Airport;
import AFRS.Model.WeatherInformation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class AirportRequest implements Request {

    private final String DIR = "/AFRS/Data/";
    private final String AIRPORTSCODE = "airports.txt";
    private final String WEATHERINFO = "weather.txt";

    private HashMap<String, Airport> airportHashMap;

    public AirportRequest() {
        airportHashMap = new HashMap<>();
        readData();
    }

    public void readData() {
        BufferedReader br;
        String line;

        // Read airport's name
        br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(DIR + AIRPORTSCODE)));
        try {
            line = br.readLine();
            while (line != null) {
                String[] airport = line.split(",");
                airportHashMap.put(airport[0], new Airport(airport[0], airport[1]));
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read airport weather
        br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(DIR + WEATHERINFO)));
        try {
            line = br.readLine();
            while (line != null) {
                String[] weather = line.split(",");
                String airportCode = weather[0];

                for (int i = 1; i < weather.length; i += 2) {
                    WeatherInformation temp = new WeatherInformation(weather[i], weather[i + 1]);
                    airportHashMap.get(airportCode).addWeatherToList(temp);
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Airport> getAirportHashMap() {
        return airportHashMap;
    }

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
