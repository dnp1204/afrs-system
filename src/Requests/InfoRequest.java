package Requests;

import Model.Flight;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class InfoRequest implements Request {

    private final String DIR = "/Data/";
    private final String FLIGHTS = "flights.txt";
    private final String DELAYS = "delays.txt";
    private final String CONNECTTIONS = "connections.txt";

    private ArrayList<Flight> flightList;
    private HashMap<String, Integer> connectionTimeMap;
    private HashMap<String, Integer> delayTimeMap;

    public InfoRequest() {
        flightList = new ArrayList<>();
        connectionTimeMap = new HashMap<>();
        delayTimeMap = new HashMap<>();
        readData();
    }

    @Override
    public ArrayList<String> doRequest(String[] params) {
        return null;
    }

    private void readData() {
        BufferedReader br;
        String line;

        // Read delays
        br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(DIR + DELAYS)));
        try {
            line = br.readLine();
            while (line != null) {
                String[] delay = line.split(",");
                delayTimeMap.put(delay[0], Integer.parseInt(delay[1]));
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read connections
        br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(DIR + CONNECTTIONS)));
        try {
            line = br.readLine();
            while (line != null) {
                String[] connection = line.split(",");
                connectionTimeMap.put(connection[0], Integer.parseInt(connection[1]));
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read flights
        br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(DIR + FLIGHTS)));
        try {
            line = br.readLine();
            while (line != null) {
                String[] flight = line.split(",");
                DateFormat df = new SimpleDateFormat("h:mm a");
                try {
                    Date departureTime;
                    Date arrivalTime;
                    String tempTime;

                   if (flight[2].contains("p")) {
                       tempTime = flight[2].substring(0, flight[2].length() - 2) + " PM";
                   } else {
                       tempTime = flight[2].substring(0, flight[2].length() - 2) + " AM";
                   }

                   departureTime = df.parse(tempTime);

                    if (flight[3].contains("p")) {
                        tempTime = flight[3].substring(0, flight[3].length() - 2) + " PM";
                    } else {
                        tempTime = flight[3].substring(0, flight[3].length() - 2) + " AM";
                    }

                    arrivalTime = df.parse(tempTime);

                    Flight temp = new Flight(flight[0], flight[1], departureTime, arrivalTime, Integer.parseInt(flight[4]), Integer.parseInt(flight[5]));
                    flightList.add(temp);
                } catch (ParseException p) {
                    p.printStackTrace();
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(flightList);
    }
}
