package AFRS.Requests;

import AFRS.ReservationDatabase;
import AFRS.Model.*;
import AFRS.SortTypes.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InfoRequest implements Request {

    private ArrayList<String> requestParams;
    private ArrayList<String> flightDataList = new ArrayList<>();
    private Map<String, String> connectionMap = new HashMap<String, String>();
    private Map<String, String> delayMap = new HashMap<String, String>();

    private ArrayList<Itinerary> itineraryList = new ArrayList<>();

    private int connectionLimit;
    private ItinerarySort sortBy = new DepartureTimeSort();

    private ReservationDatabase reservationDB;


    /*
    ** Will set the default sorter as DepartureSort unless requested
    ** Sets default number of connections, can be changed if needed
    ** Will read in all files and return an error if needed
     */
    public InfoRequest(ReservationDatabase reservationDB) {

        this.reservationDB = reservationDB;

        connectionLimit = 2;

        if (readInFlightFile() && readInConnectionFile() && readInDelayFile()) {
            System.out.println("Files were read in without error");

        }

    }

    /*
    * Function will read in the flights that the system will be using
    * This will be done once upon start up of system
    * @Returns: boolean that is true if no errors were read, false if error occurred
     */

    private boolean readInFlightFile() {

        String flightFileName = "/AFRS/Data/flights.txt";
        String line;

        try {

            InputStreamReader fileReader = new InputStreamReader(getClass().getResourceAsStream(flightFileName));
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {

                flightDataList.add(line);

            }

            bufferedReader.close();


        }

        catch(IOException ioEX) {
            System.out.println("Error reading file '" + flightFileName + "'");
            return false;
        }

        return true;
    }


    /*
    * Function will read in the connections that the system will be using to determine connections
    * This will be done once upon start up of system
    * @Returns: boolean that is true if no errors were caught, false if error occurred
     */

    private boolean readInConnectionFile() {
        String connectionFileName = "/AFRS/Data/connections.txt";
        String line = null;

        try {

            InputStreamReader fileReader = new InputStreamReader(getClass().getResourceAsStream(connectionFileName));
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {

                connectionMap.put(line.split(",")[0], line.split(",")[1]);

            }

            bufferedReader.close();


        }

        catch(IOException ioEX) {
            System.out.println("Error reading file '" + connectionFileName + "'");
            return false;
        }

        return true;
    }

    /*
    * Function will read in the delays that the system will be using to determine connections
    * This will be done once upon start up of system
    * @Returns: boolean that is true if no errors were read, false if error occurred
     */

    private boolean readInDelayFile() {

        String delayFileName = "/AFRS/Data/delays.txt";
        String line = null;

        try {

            InputStreamReader fileReader = new InputStreamReader(getClass().getResourceAsStream(delayFileName));
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {

                delayMap.put(line.split(",")[0], line.split(",")[1]);

            }

            bufferedReader.close();


        }

        catch(FileNotFoundException noFileEx) {
            System.out.println("Unable to read file '" + delayFileName + "'");
            return false;
        }

        catch(IOException ioEX) {
            System.out.println("Error reading file '" + delayFileName + "'");
            return false;
        }

        return true;

    }

    /*
    * Class used to set connection limit if one is requested
    * @Parameters: int limit is the number of max connections wanted
     */

    private void setConnectionLimit(String limit) {
        connectionLimit = Integer.parseInt(limit);
    }

    /*
    * This will set the sorter if one is requested
    * Sorter is automatically defaulted to departureSort
    * @Parameters: ItinerarySort sortType: a Itinerary sort that determines returned info order
     */

    private boolean setSorter(String sortType) {

        switch (sortType) {

            case "departure":
                sortBy = new DepartureTimeSort();
                return true;

            case "arrival":
                sortBy = new ArrivalTimeSort();
                return true;

            case "airfare":
                sortBy = new AirfareSort();
                return true;

            default:
                return false;

        }

    }

    /*
    * This will call the sort function from the sorter determined
    * Will determine if no itineraries were available
     */

    private void sort() {

        if (itineraryList == null) {
            System.out.println("List is null");
        }

        itineraryList = sortBy.doSort(itineraryList);

        reservationDB.updateItineraryList(itineraryList);

    }

    /*
    * This in the class implemented by <<Request>>
    *  This is called on when a new request is received
    *  @Parameters: String[] params: these are the given parameters that have origin, destination, connection limit, and sort type
     */

    public ArrayList<String> doRequest(String[] params) {

        ArrayList<String> itineraryListString = new ArrayList<>();
        itineraryList.clear();

        if (params.length == 4) {
            try {
                Integer.parseInt(params[2]);
                if (-1 < Integer.parseInt(params[2]) &&  Integer.parseInt(params[2]) < 3) {
                    setConnectionLimit(params[2]);
                } else {
                    itineraryListString.add("error,invalid connection limit");
                    return itineraryListString;
                }
            } catch (NumberFormatException e) {
                if(!params[2].equals("")) {
                    itineraryListString.add("error,invalid connection limit");
                    return itineraryListString;
                }
            }
            if (!setSorter(params[3])) {
                itineraryListString.add("error,invalid sort order");
                return itineraryListString;
            }
        } else if (params.length == 3) {
            try {
                Integer.parseInt(params[2]);
                if (-1 < Integer.parseInt(params[2]) &&  Integer.parseInt(params[2]) < 3) {
                    setConnectionLimit(params[2]);
                } else {
                    itineraryListString.add("error,invalid connection limit");
                    return itineraryListString;
                }
            } catch (NumberFormatException e) {
                if (!setSorter(params[2])) {
                    itineraryListString.add("error,invalid sort order");
                    return itineraryListString;
                }
            }
        } else {
            setSorter("departure");
            setConnectionLimit("2");
        }


        requestParams = new ArrayList<>();
        requestParams.add(params[0]);
        requestParams.add(params[1]);

        calculateRoute(new ArrayList<>(), requestParams.get(0), requestParams.get(1));

        sort();

        itineraryListString.add("info, " + itineraryList.size());

        for (int i = 0; i < itineraryList.size(); i++) {
            itineraryListString.add(i + 1 + "," + itineraryList.get(i).toString());
        }

        return itineraryListString;

    }

    /*
    * This will handle finding all the possible routes that a flight can take from the origin
    *
    * @params: ArrayList<Flight> possibleFlight : This is an arraylist of a possible itinerary
    *           String origin : This is the destination of the previous flight so in turn, the
    *                           origin of the next needed flight
    *           String destination: This is the overall destination of the flights
    *
    */

    public void calculateRoute(ArrayList<Flight> possibleFlight, String origin, String destination) {

        if (possibleFlight.size() > connectionLimit) { return; }

        for (String flight : flightDataList) {

            //flightComponents[0] is origin, flightComponents[1] is destination, flightComponents[2] is departure time, flightComponents[3] is arrival time, flightComponent[4] is airfare, flightComponent[5] is flight number

            String[] flightComponents = flight.split(",");
            String flightOrigin = flightComponents[0];
            String flightDest = flightComponents[1];
            LocalTime departureTime = calculateTime(flightComponents[2]);
            LocalTime arrivalTime = calculateTime(flightComponents[3]);

            Flight flightLeg = new Flight(Integer.parseInt(flightComponents[5]), flightOrigin, departureTime, flightDest, arrivalTime, Integer.parseInt(flightComponents[4]));

            if (destination.equals(flightDest) && origin.equals(flightOrigin)) {

                if (possibleFlight.size() == 0 || canMakeFlight(possibleFlight.get(possibleFlight.size() - 1), flightLeg)) {
                    possibleFlight.add(flightLeg);
                    createItinerary(possibleFlight);
                    possibleFlight.remove(flightLeg);
                }

            } else if (flightOrigin.equals(origin)) {

                if (possibleFlight.size() == 0) {
                    possibleFlight.add(flightLeg);
                    calculateRoute(possibleFlight, flightDest, destination);
                    possibleFlight.remove(flightLeg);
                } else if (!flightLeg.getDestination().equals(possibleFlight.get(possibleFlight.size() - 1).getOrigin()) && canMakeFlight(possibleFlight.get(possibleFlight.size() - 1), flightLeg)) {
                    possibleFlight.add(flightLeg);
                    calculateRoute(possibleFlight, flightDest, destination);
                    possibleFlight.remove(flightLeg);
                }

            }

        }

        return;

    }


    private boolean canMakeFlight(Flight flightA, Flight flightB) {

        int addedMinutes = Integer.parseInt(delayMap.get(flightA.getOrigin())) + Integer.parseInt(connectionMap.get(flightA.getDestination()));

        LocalTime modFlightA = flightA.getArrivalTime().plusMinutes(addedMinutes);

        if (modFlightA.getHour() == 0 && flightA.getArrivalTime().getHour() > 0) { return false; }

        if (modFlightA.isBefore(flightB.getDepartureTime())) { return true; }

        return false;

    }

    private LocalTime calculateTime(String time) {

        int hours = Integer.parseInt(time.split(":")[0]);
        if (time.split(":")[1].contains("p")) {
            hours += 12;
        }

        if (hours == 12) {
            hours = 0;
        } else if (hours == 24) {
            hours = 12;
        }

        int minutes = Integer.parseInt(time.split(":")[1].substring(0,2));

        return LocalTime.of(hours, minutes);

    }

    private Itinerary createItinerary(ArrayList<Flight> flights) {
        Itinerary connectedFlight = new Itinerary(flights);
        itineraryList.add(connectedFlight);
        return connectedFlight;
    }

}
