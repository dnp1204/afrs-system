package AFRS.Requests;

import AFRS.ReservationDatabase;
import AFRS.Model.*;
import AFRS.SortTypes.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalTime;

public class InfoRequest implements Request {

    private ArrayList<String> requestParams;
    private ArrayList<String> flightDataList = new ArrayList<>();
    private Map<String, String> connectionMap = new HashMap<String, String>();
    private Map<String, String> delayMap = new HashMap<String, String>();

    private ArrayList<Itinerary> itineraryList = new ArrayList<>();

    private int connectionLimit;
    private ItinerarySort sortBy;

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

    public boolean readInFlightFile() {

        String flightFileName = "flights.txt";
        String line;

        try {

            FileReader fileReader = new FileReader(flightFileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {

                flightDataList.add(line);

            }

            bufferedReader.close();


        }

        catch(FileNotFoundException noFileEx) {
            System.out.println("Unable to read file '" + flightFileName + "'");
            return false;
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

    public boolean readInConnectionFile() {
        String connectionFileName = "connections.txt";
        String line = null;

        try {

            FileReader fileReader = new FileReader(connectionFileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {

                connectionMap.put(line.split(",")[0], line.split(",")[1]);

            }

            bufferedReader.close();


        }

        catch(FileNotFoundException noFileEx) {
            System.out.println("Unable to read file '" + connectionFileName + "'");
            return false;
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

    public boolean readInDelayFile() {

        String delayFileName = "delays.txt";
        String line = null;

        try {

            FileReader fileReader = new FileReader(delayFileName);
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

    public void setConnectionLimit(String limit) {
        if (!limit.equals("") && Integer.parseInt(limit) < 3) {
            connectionLimit = Integer.parseInt(limit);
        }
    }

    /*
    * This will set the sorter if one is requested
    * Sorter is automatically defaulted to departureSort
    * @Parameters: ItinerarySort sortType: a Itinerary sort that determines returned info order
     */

    public void setSorter(String sortType) {

        switch (sortType) {

            case "":
                sortBy = new DepartureTimeSort();

            case "departure":
                sortBy = new DepartureTimeSort();

            case "arrival":
                sortBy = new ArrivalTimeSort();

            case "airfare":
                sortBy = new AirfareSort();

        }

    }

    /*
    * This will call the sort function from the sorter determined
    * Will determine if no itineraries were available
     */

    public void sort() {

        ArrayList<Itinerary> newlySorted = sortBy.doSort(itineraryList);
        itineraryList = newlySorted;

        for (Itinerary itin : itineraryList) {
            System.out.println(itin);
        }

        if (itineraryList.size() == 0) {
            System.out.println("There are no possible routes");
        }

        //ReservationDatabase.updateItineraries(itineraryList);

    }

    /*
    * This in the class implemented by <<Request>>
    *  This is called on when a new request is received
    *  @Parameters: String[] params: these are the given parameters that have origin, destination, connection limit, and sort type
     */

    public ArrayList<String> doRequest(String[] params) {


        setConnectionLimit(params[2]);
        setSorter(params[3]);

        requestParams = new ArrayList<>();
        requestParams.add(params[0]);
        requestParams.add(params[1]);

        String origin = requestParams.get(0);

        //Loops through the whole flight list finding flights that start from the parameter origin
        for (String flight : flightDataList) {

            if (flight.split(",")[0].equals(origin)) {

                //If there is a flight from the origin, the calculateRoute will be call
                //This will send the flight information to find any possible routes to destination steming from origin
                Itinerary connectedRoute = calculateRoute(flight);
                if (connectedRoute != null && connectedRoute.getFlightCount() <= connectionLimit + 1) {
                    itineraryList.add(connectedRoute);
                }
            }

        }

        sort();

        ArrayList<String> itineraryListString = new ArrayList<>();

        for (Itinerary itin : itineraryList) {
            itineraryListString.add(itin.toString());
        }

        return itineraryListString;

    }

    /*
    * This will handle finding all the possible routes that a flight can take from the origin
    *
     */

    public Itinerary calculateRoute(String currentFlight) {

        ArrayList<Flight> possibleFlight = new ArrayList<>();

        String[] flightComponents = currentFlight.split(",");

        LocalTime arrivalTime = calculateArrivalTime(currentFlight);
        LocalTime departureTime = calculateDepartureTime(currentFlight);
        Flight addedFlight = new Flight(flightComponents[0], flightComponents[1], departureTime, arrivalTime, Integer.parseInt(flightComponents[4]), Integer.parseInt(flightComponents[5]));
        possibleFlight.add(addedFlight);

        if (flightComponents[1].equals(requestParams.get(1))) {
            return createItinerary(possibleFlight);
        }

        //This is the origin of the next leg of the flight that we are looking for
        //So it is also the destination of the first leg of the flight
        String destOrigin = flightComponents[1];

        for (String possibleLeg : flightDataList) {

            flightComponents = possibleLeg.split(",");

            if (flightComponents[1].equals(requestParams.get(1)) && canMakeFlight(currentFlight, possibleLeg) && flightComponents[0].equals(destOrigin)) {
                arrivalTime = calculateArrivalTime(possibleLeg);
                departureTime = calculateDepartureTime(possibleLeg);
                addedFlight = new Flight(flightComponents[0], flightComponents[1], departureTime, arrivalTime, Integer.parseInt(flightComponents[4]), Integer.parseInt(flightComponents[5]));
                possibleFlight.add(addedFlight);
                return createItinerary(possibleFlight);
            }

            if (flightComponents[0].equals(destOrigin) && !flightComponents[1].equals(possibleFlight.get(0).getOrigin())) {

                if (canMakeFlight(currentFlight, possibleLeg)) {

                    arrivalTime = calculateArrivalTime(possibleLeg);
                    departureTime = calculateDepartureTime(possibleLeg);

                    addedFlight = new Flight(flightComponents[0], flightComponents[1], arrivalTime, departureTime, Integer.parseInt(flightComponents[4]), Integer.parseInt(flightComponents[5]));
                    possibleFlight.add(addedFlight);
                    destOrigin = flightComponents[1];
                    currentFlight = possibleLeg;

                    for (String nextPossibleLeg : flightDataList) {

                        flightComponents = nextPossibleLeg.split(",");

                        if (flightComponents[1].equals(requestParams.get(1)) && canMakeFlight(possibleLeg, nextPossibleLeg) && flightComponents[0].equals(destOrigin)) {
                            arrivalTime = calculateArrivalTime(nextPossibleLeg);
                            departureTime = calculateDepartureTime(nextPossibleLeg);
                            addedFlight = new Flight(flightComponents[0], flightComponents[1], departureTime, arrivalTime, Integer.parseInt(flightComponents[4]), Integer.parseInt(flightComponents[5]));
                            possibleFlight.add(addedFlight);
                            return createItinerary(possibleFlight);
                        }


                        if (flightComponents[0].equals(destOrigin) && !flightComponents[1].equals(possibleFlight.get(0).getOrigin())) {

                            if (canMakeFlight(possibleLeg, nextPossibleLeg)) {
                                arrivalTime = calculateArrivalTime(nextPossibleLeg);
                                departureTime = calculateDepartureTime(nextPossibleLeg);
                                addedFlight = new Flight(flightComponents[0], flightComponents[1], arrivalTime, departureTime, Integer.parseInt(flightComponents[4]), Integer.parseInt(flightComponents[5]));
                                possibleFlight.add(addedFlight);
                                if (destOrigin.equals(requestParams.get(1))) {
                                    return createItinerary(possibleFlight);
                                } else {
                                    return null;
                                }
                            }
                        }
                    }

                    possibleFlight.remove(possibleFlight.size() - 1);

                }
            }
        }

        return null;


    }

    public boolean canMakeFlight(String flightA, String flightB) {

        String[] flightAComponents = flightA.split(",");
        LocalTime destArrival = calculateArrivalTime(flightA);

        destArrival.plusMinutes(Integer.parseInt(delayMap.get(flightAComponents[0])) + Integer.parseInt(connectionMap.get(flightAComponents[1])));

        LocalTime destDeparture = calculateDepartureTime(flightB);

        boolean canMake = false;

        if (destArrival.isBefore(destDeparture)) {
            canMake =  true;
        } else if (destArrival == destDeparture) {
            if (destArrival.isBefore(destDeparture)) {
                canMake = true;
            }
        }

        return canMake;

    }


    public LocalTime calculateArrivalTime(String flight) {


        String[] flightComponents = flight.split(",");
        int hours = Integer.parseInt(flightComponents[3].split(":")[0]);
        if (flightComponents[3].split(":")[1].contains("p")) {
            hours += 12;
        }

        if (hours == 12) {
            hours = 0;
        } else if (hours == 24) {
            hours = 12;
        }

        int minutes = Integer.parseInt(flightComponents[3].split(":")[1].substring(0, 2));


        LocalTime time = LocalTime.of(hours, minutes);

        return time;

    }

    public LocalTime calculateDepartureTime(String flight) {


        String[] flightComponents = flight.split(",");
        int hours = Integer.parseInt(flightComponents[3].split(":")[0]);
        if (flightComponents[3].split(":")[1].contains("p")) {
            hours += 12;
        }

        if (hours == 12) {
            hours = 0;
        } else if (hours == 24) {
            hours = 12;
        }

        int minutes = Integer.parseInt(flightComponents[3].split(":")[1].substring(0, 2));


        LocalTime time = LocalTime.of(hours, minutes);

        return time;

    }

    public Itinerary createItinerary(ArrayList<Flight> flights) {
        Itinerary connectedFlight = new Itinerary(flights);
        return connectedFlight;
    }

}
