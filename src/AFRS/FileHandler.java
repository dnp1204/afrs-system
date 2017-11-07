package AFRS;

import AFRS.AirportInfo.AirportInfo;
import AFRS.AirportInfo.FAAAirportInfo;
import AFRS.AirportInfo.LocalAirportInfo;
import AFRS.Model.Airport;
import AFRS.Model.WeatherInformation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileHandler {

    private static final String filePath = "./Data/";
    private static final String airportFile = "airports.txt";
    private static final String connectionFile = "connections.txt";
    private static final String delayFile = "delays.txt";
    private static final String flightFile = "flights.txt";
    private static final String weatherFile = "weather.txt";

    private AirportInfo airportInfo;

    private HashMap<String, Airport> airportMap;
    private ArrayList<String> flightDataList;

    public HashMap<String, Airport> getAirportMap() {
        return airportMap;
    }

    public ArrayList<String> getFlightDataList() {
        return flightDataList;
    }

    public HashMap<String, Airport> getAirportInfo() {
        return airportInfo.getInfo();
    }

    public void setAirportInfo(AirportInfo airportInfo) {
        this.airportInfo = airportInfo;
    }

    public FileHandler() {
        airportMap = new HashMap<>();
        flightDataList = new ArrayList<>();
        if (!tryBuildAirportMap()) {
            System.err.println("Unable to build airport map. Exiting program.");
            System.exit(1);
        } else {
            airportInfo = new LocalAirportInfo(airportMap);
        }
        if (!tryBuildFlightDataList()) {
            System.err.println("Unable to build flight data list. Exiting program.");
            System.exit(1);
        }
    }

    private boolean tryBuildAirportMap() {
        BufferedReader br;
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filePath+airportFile)));
            line = br.readLine();
            while (line != null) {
                String[] airport = line.split(",");
                airportMap.put(airport[0], new Airport(airport[0], airport[1]));
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Unable to read airport file.");
            return false;
        }

        try {
            br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filePath+weatherFile)));
            line = br.readLine();
            while (line != null) {
                String[] weather = line.split(",");
                String airportCode = weather[0];

                for (int i = 1; i < weather.length; i += 2) {
                    WeatherInformation temp = new WeatherInformation(weather[i], weather[i + 1]);
                    airportMap.get(airportCode).addWeatherToList(temp);
                }
                line = br.readLine();
            }
        } catch (Exception e) {
            System.err.println("Unable to read weather file.");
            return false;
        }

        try {
            br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filePath+delayFile)));
            line = br.readLine();
            while (line != null) {
                String[] airport = line.split(",");
                airportMap.get(airport[0]).setDelay(airport[1]);
                line = br.readLine();
            }
        } catch (Exception e) {
            System.err.println("Unable to read delay file.");
            return false;
        }

        try {
            br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filePath+connectionFile)));
            line = br.readLine();
            while (line != null) {
                String[] airport = line.split(",");
                airportMap.get(airport[0]).setConnection(Integer.parseInt(airport[1]));
                line = br.readLine();
            }
        } catch (Exception e) {
            System.err.println("Unable to read connection file.");
            return false;
        }

        return true;
    }

    private boolean tryBuildFlightDataList() {
        BufferedReader br;
        String line;

        try {
            br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filePath+flightFile)));
            line = br.readLine();
            while (line != null) {
                flightDataList.add(line);
                line = br.readLine();
            }
        } catch (Exception e) {
            System.err.println("Unable to read flight file.");
            return false;
        }
        return true;
    }
}
