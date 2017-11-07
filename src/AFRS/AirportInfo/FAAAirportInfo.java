package AFRS.AirportInfo;

import AFRS.Model.Airport;
import AFRS.Model.WeatherInformation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

public class FAAAirportInfo implements AirportInfo {
    private static final String URI = "http://services.faa.gov/airport/status/";
    private static final String FORMAT = "?format=application/xml";

    private HashMap<String, Airport> airportServicesMap;
    private HashMap<String, Airport> airportInfoHashMap;
    private boolean isConnected;

    public FAAAirportInfo(HashMap<String, Airport> airportInfoHashMap) {
        this.airportInfoHashMap = airportInfoHashMap;
        airportServicesMap = new HashMap<>();
        isConnected = tryBuildAirportServicesMap();
    }

    @Override
    public HashMap<String, Airport> getInfo() {
       if (isConnected) {
           return airportServicesMap;
       }
       return airportInfoHashMap;
    }

    private boolean tryBuildAirportServicesMap() {
        Set<String> airportCode = airportInfoHashMap.keySet();
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
}
