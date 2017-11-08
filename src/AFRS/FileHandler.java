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
    private HashMap<String, Airport> airportServicesMap;
    private HashMap<String, HashMap<String, Airport>> airportInfo;
    private ArrayList<String> flightDataList;

    public HashMap<String, Airport> getAirportInfo(String clientID) {
        return airportInfo.get(clientID);
    }

    public void removeClient(String clientID) {
        airportInfo.remove(clientID);
    }

    public void setAirportInfo(String clientID, String type) {
        if (type.equals("faa")) {
            tryBuildAirportServicesMap();
            airportInfo.put(clientID,airportServicesMap);
        } else if (type.equals("local")){
            airportInfo.put(clientID,airportMap);
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
        airportInfo = new HashMap<>();
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

    private boolean tryBuildAirportServicesMap() {
        BufferedReader br;
        String line;
        List<String> airportCode = new ArrayList<>();
        try {
            br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filePath + airportFile)));
            line = br.readLine();
            while (line != null) {
                String[] airport = line.split(",");
                airportCode.add(airport[0]);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Unable to read airport file.");
            return false;
        }

        for (String code: airportCode) {
            String airportUrl = URI + code + FORMAT;
            try {
                URL airportService = new URL(airportUrl);
                HttpURLConnection con = (HttpURLConnection) airportService.openConnection();

                con.setRequestMethod("GET");
                con.setConnectTimeout(10000);
                con.setReadTimeout(10000);

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(con.getInputStream());
                NodeList nodeList;

                // Get airport name
                nodeList = document.getElementsByTagName("Name");
                if (nodeList.item(0) instanceof Element) {
                    String airportName = nodeList.item(0).getLastChild().getTextContent().trim();
                    airportServicesMap.put(code, new Airport(code, airportName));
                }

                // Get airport delay
                nodeList = document.getElementsByTagName("Status");
                if (nodeList.item(0) instanceof Element) {
                    NodeList childNodeList = nodeList.item(0).getChildNodes();
                    for (int i = 0; i < childNodeList.getLength(); i++) {
                        Node childNode = childNodeList.item(i);
                        if (childNode instanceof Element && childNode.getNodeName().equals("AvgDelay")) {
                            if (childNode.getLastChild() != null) {
                                airportServicesMap.get(code).setDelay(childNode.getLastChild().getTextContent().trim());
                            } else {
                                airportServicesMap.get(code).setDelay("0");
                            }
                            break;
                        }
                    }
                }

                // Get weather information
                nodeList = document.getElementsByTagName("Weather");
                String condition = "", temperature = "";
                for (int i = 0; i < nodeList.item(0).getChildNodes().getLength(); i++) {
                    Node node = nodeList.item(0).getChildNodes().item(i);
                    if (node instanceof Element) {
                        String content = node.getLastChild().getTextContent().trim();
                        switch (node.getNodeName()) {
                            case "Weather":
                                condition = content;
                                break;
                            case "Temp":
                                temperature = content;
                        }
                    }
                }
                WeatherInformation temp = new WeatherInformation(condition, temperature);
                airportServicesMap.get(code).addWeatherToList(temp);

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Unable to get data from webservices");
                return false;
            }

        }

        return true;
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
