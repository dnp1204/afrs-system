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
            System.out.println(params[0]+" "+params[1]+" "+params[2]+" "+params[3]);
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

        String origin = requestParams.get(0);

        //Loops through the whole flight list finding flights that start from the parameter origin
        for (String flight : flightDataList) {

            if (!connectionMap.containsKey(origin)) {
                itineraryListString.add("error,unknown origin");
                return itineraryListString;
            } else if (!connectionMap.containsKey(params[1])) {
                itineraryListString.add("error,unknown destination");
                return itineraryListString;
            }

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

        int i = 0;
        String info = "info, " + itineraryList.size();
        itineraryListString.add(info);

        for (Itinerary itin : itineraryList) {
            i++;
            itineraryListString.add(i + "," + itin.toString());
        }

        return itineraryListString;

    }

    /*
    * This will handle finding all the possible routes that a flight can take from the origin
    *
     */

    private Itinerary calculateRoute(String startingFlight) {

        ArrayList<Flight> possibleFlight = new ArrayList<>();

        String[] flightComponents = startingFlight.split(",");

        LocalTime arrivalTime = calculateArrivalTime(startingFlight);
        LocalTime departureTime = calculateDepartureTime(startingFlight);

        //flightComponents[0] is origin, flightComponents[1] is destination, flightComponents[2] is departure time, flightComponents[3] is arrival time, flightComponent[4] is airfare, flightComponent[5] is flight number

        Flight addedFlight = new Flight(Integer.parseInt(flightComponents[5]), flightComponents[0], departureTime, flightComponents[1], arrivalTime, Integer.parseInt(flightComponents[4]));
        possibleFlight.add(addedFlight);

        if (flightComponents[1].equals(requestParams.get(1))) {
            return createItinerary(possibleFlight);
        }

        //This is the origin of the next leg of the flight that we are looking for
        //So it is also the destination of the first leg of the flight
        String destOrigin = flightComponents[1];

        for (String possibleSecondLeg : flightDataList) {

            flightComponents = possibleSecondLeg.split(",");

            if (flightComponents[1].equals(requestParams.get(1)) && canMakeFlight(startingFlight, possibleSecondLeg) && !(flightComponents[0].equals(destOrigin))) {
                arrivalTime = calculateArrivalTime(possibleSecondLeg);
                departureTime = calculateDepartureTime(possibleSecondLeg);
                addedFlight = new Flight(Integer.parseInt(flightComponents[5]), flightComponents[0], departureTime, flightComponents[1], arrivalTime, Integer.parseInt(flightComponents[4]));
                possibleFlight.add(addedFlight);
                return createItinerary(possibleFlight);
            }

            if (flightComponents[0].equals(destOrigin) && !flightComponents[1].equals(possibleFlight.get(0).getOrigin())) {

                if (canMakeFlight(startingFlight, possibleSecondLeg)) {

                    arrivalTime = calculateArrivalTime(possibleSecondLeg);
                    departureTime = calculateDepartureTime(possibleSecondLeg);

                    addedFlight = new Flight(Integer.parseInt(flightComponents[5]), flightComponents[0], departureTime, flightComponents[1], arrivalTime, Integer.parseInt(flightComponents[4]));
                    possibleFlight.add(addedFlight);
                    destOrigin = flightComponents[1];
                    startingFlight = possibleSecondLeg;

                    for (String possibleThirdLeg : flightDataList) {

                        flightComponents = possibleThirdLeg.split(",");

                        if (flightComponents[1].equals(requestParams.get(1)) && canMakeFlight(possibleSecondLeg, possibleThirdLeg) && !(flightComponents[0].equals(destOrigin))) {
                            arrivalTime = calculateArrivalTime(possibleThirdLeg);
                            departureTime = calculateDepartureTime(possibleThirdLeg);
                            addedFlight = new Flight(Integer.parseInt(flightComponents[5]), flightComponents[0], departureTime, flightComponents[1], arrivalTime, Integer.parseInt(flightComponents[4]));
                            possibleFlight.add(addedFlight);
                            return createItinerary(possibleFlight);
                        }


                        if (flightComponents[0].equals(destOrigin) && !flightComponents[1].equals(possibleFlight.get(0).getOrigin())) {

                            if (canMakeFlight(possibleSecondLeg, possibleThirdLeg)) {
                                arrivalTime = calculateArrivalTime(possibleThirdLeg);
                                departureTime = calculateDepartureTime(possibleThirdLeg);
                                addedFlight = new Flight(Integer.parseInt(flightComponents[5]), flightComponents[0], departureTime, flightComponents[1], arrivalTime, Integer.parseInt(flightComponents[4]));
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

    private boolean canMakeFlight(String flightA, String flightB) {

        String[] flightAComponents = flightA.split(",");
        LocalTime destArrival = calculateArrivalTime(flightA);

        destArrival.plusMinutes(Integer.parseInt(delayMap.get(flightAComponents[0])) + Integer.parseInt(connectionMap.get(flightAComponents[1])));

        LocalTime destDeparture = calculateDepartureTime(flightB);

        boolean canMake = false;

        if (destArrival.isBefore(destDeparture)) {
            canMake =  true;
        } else if (destArrival == destDeparture) {
            canMake = true;
        }

        return canMake;

    }


    private LocalTime calculateArrivalTime(String flight) {


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

    private LocalTime calculateDepartureTime(String flight) {


        String[] flightComponents = flight.split(",");
        int hours = Integer.parseInt(flightComponents[2].split(":")[0]);
        if (flightComponents[2].split(":")[1].contains("p")) {
            hours += 12;
        }

        if (hours == 12) {
            hours = 0;
        } else if (hours == 24) {
            hours = 12;
        }

        int minutes = Integer.parseInt(flightComponents[2].split(":")[1].substring(0, 2));


        LocalTime time = LocalTime.of(hours, minutes);

        return time;

    }

    private Itinerary createItinerary(ArrayList<Flight> flights) {
        Itinerary connectedFlight = new Itinerary(flights);
        return connectedFlight;
    }

}
