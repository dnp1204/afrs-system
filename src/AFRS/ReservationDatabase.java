package AFRS;

import AFRS.Model.Itinerary;
import AFRS.Model.Reservation;
import AFRS.Requests.Request;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Stack;

public class ReservationDatabase {
    private static ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
    //this needs to read from file to populate and be persistent reservation database
    public static ArrayList<Itinerary> recentItineraryList = new ArrayList<Itinerary>();

    private final String FILEPATH = "/reservations.txt";

    private static Map<String,Stack<ReservationWithState>> undoStackMap;//never assigned?
    private static Map<String,Stack<ReservationWithState>> redoStackMap;

    public String redo(String clientID){
        //check if the redoStack is empty
        if (!redoStackMap.containsKey(clientID)) {
            return "sorry charlie, nothing to redo yet! try out some of our nifty commands...or are they requests? and then undo them";
        }
        ReservationWithState rs = redoStackMap.get(clientID).pop();
        undoStackMap.get(clientID).push(rs);
        if (rs.getState().equals("delete")){
            return delete(rs.reservation);
        }
        else {
            return reserve(rs.reservation);
        }
    }

    public String undo(String clientID){
        //check if the undoStack is empty
        if (!undoStackMap.containsKey(clientID)) {
            return "sorry charlie, nothing to undo yet! try out some of our nifty commands...or are they requests?";
        }
        // create redo stack if it does not already exist
        if (!redoStackMap.containsKey(clientID)) {
            redoStackMap.put(clientID, new Stack<>());
        }
        ReservationWithState rs = undoStackMap.get(clientID).pop();
        redoStackMap.get(clientID).push(rs);
        if (rs.getState().equals("delete")){
            return reserve(rs.reservation);
        }
        else {
            return delete(rs.reservation);
        }
    }



//- startUp() : void
//- shutDown(): void
//+ reserve(params) : String
//+ delete(params) : String
//+ retrieve(params) : ArrayList<Reservation>
//+ updateItineraryList(ArrayList) : void


    //try to create a db from existing text file on start up
    public void startUp(){
        try {
            //ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
            String relativePath = getClass().getProtectionDomain().getCodeSource().getLocation().toString();
            relativePath = relativePath.substring(relativePath.indexOf(":")+2);
            BufferedReader inFile = new BufferedReader(new FileReader(relativePath+FILEPATH));
            String inputLine;

            while ((inputLine = inFile.readLine()) != null) {
                //each line is a new reservation
                Reservation new_reservation = new Reservation();
                //separate passenger name from rest
                String[] reservationInfo = inputLine.split(",",2);
                new_reservation.setPassengerName(reservationInfo[0]);
                //call Itinerary constructor with string parameter
                Itinerary new_itinerary_from_string = new Itinerary(reservationInfo[1]);
                new_reservation.setItinerary(new_itinerary_from_string);

                reservationList.add(new_reservation);
            }

        } catch (IOException e) {

        }
    }
    //write to new txt file to save current db
    public void shutDown() throws IOException{
        StringBuffer output = new StringBuffer();
        for ( Reservation r : reservationList){
            output.append(r.toString());
            output.append("\n");
        }

        String relativePath = getClass().getProtectionDomain().getCodeSource().getLocation().toString();
        relativePath = relativePath.substring(relativePath.indexOf(":")+2);
        BufferedWriter writer = new BufferedWriter(new FileWriter(relativePath+FILEPATH, false));
        writer.write(String.valueOf(output));
        writer.close();
        }

    //called by client to reserve an itinerary that was just queried
    //params: id (int) - the id of the itinerary form the list of recent itineraries, starting with 1.
    //        passengerName (String) - passenger name

    public static String reserve(int id, String passengerName) {

        // check if id is valid
        // then check if this reservation already exists

        try{
            Itinerary itinerary = recentItineraryList.get(id-1); // use id-1 to handle 0-based array index
            for (Reservation r : reservationList) {
                if (r.getPassengerName().equals(passengerName)){
                    Itinerary check_itinerary = r.getItinerary();
                    if (check_itinerary.getOrigin().equals(itinerary.getOrigin()) &&
                            check_itinerary.getDestination().equals(itinerary.getDestination())){
                        return "error,duplicate reservation";

                    }
                }

            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return "error, invalid id";
        }
        catch (NumberFormatException e) {
            return "error,invalid id";
        }

        Reservation new_reservation = new Reservation();
        new_reservation.setPassengerName(passengerName);
        new_reservation.setItinerary(recentItineraryList.get(id-1));
        //use id-1 to handle the 0-based index of array
        reservationList.add(new_reservation);

        //TODO: Make reservations associated with client ID??????????
        //TODO: Wait so can two clients have the same name? if so then name cannot be a constraint for reservation.
        //TODO: get client ID in here


        //reserve request is valid so instanciate a undo stack if one does not already exist.
        // and reset the redo stack if one did already exist

        if (!undoStackMap.containsKey(clientID)) {
            undoStackMap.put(clientID, new Stack<>());
        }
        ReservationWithState rs = new ReservationWithState("reserve", new_reservation);
        undoStackMap.get(clientID).push(rs);

        //check if that client had an existing redoStack and clear it
        if (redoStackMap.containsKey(clientID)) {
            redoStackMap.remove(clientID);
        }
        return("reserve,successful");
    }

    public static String reserve(Reservation reservationFromStack) {

        // check if id is valid
        // then check if this reservation already exists

        String passenger = reservationFromStack.getPassengerName();
        Itinerary RFS_itinerary = reservationFromStack.getItinerary();
        for (Reservation r : reservationList) {
            if (r.getPassengerName().equals(passenger)){
                Itinerary check_itinerary = r.getItinerary();
                if (check_itinerary.getOrigin().equals(RFS_itinerary.getOrigin()) &&
                        check_itinerary.getDestination().equals(RFS_itinerary.getDestination())){
                    return "error,duplicate reservation";

                }
            }

        }

        Reservation new_reservation = new Reservation();
        new_reservation.setPassengerName(passenger);
        new_reservation.setItinerary(RFS_itinerary);
        reservationList.add(new_reservation);
        return("reserve,successful");
    }

    public String delete(String passenger, String origin, String destination) {
        for (Reservation r : reservationList){
            if (r.getPassengerName().equals(passenger)){
                Itinerary itinerary = r.getItinerary();
                if (itinerary.getOrigin().equals(origin) && itinerary.getDestination().equals(destination)){
                    reservationList.remove(r);
                    return "delete,successful";
                }
            }
        }
        return "error,reservation not found";

    }

    public String delete(Reservation reservation) {
        String passenger = reservation.getPassengerName();
        Itinerary itinerary = reservation.getItinerary();

        for (Reservation r : reservationList){
            if (r.getPassengerName().equals(passenger)){
                Itinerary check_itinerary = r.getItinerary();
                if (check_itinerary.getOrigin().equals(itinerary.getOrigin()) && check_itinerary.getDestination().equals(itinerary.getDestination())){
                    reservationList.remove(r);
                    return "delete,successful";
                }
            }
        }
        return "error,reservation not found";

    }

    public ArrayList<Reservation> retrieve(String passenger){
        ArrayList<Reservation> temp_list = new ArrayList<Reservation>();
        for (Reservation r : reservationList){
            if (r.getPassengerName().equals(passenger)){
                temp_list.add(r);
            }
        }
        Collections.sort(temp_list);
        return temp_list;
    }
    //how to tell if i am given just origin or just destination. It would just be a string
    public ArrayList<Reservation> retrieve(String passenger, String airport, boolean origin){
        ArrayList<Reservation> temp_list = new ArrayList<Reservation>();
        for (Reservation r : reservationList){
            if (r.getPassengerName().equals(passenger)){
                if(origin) {
                    if (r.getItinerary().getOrigin().equals(airport)) {
                        temp_list.add(r);
                    }
                } else {
                    if (r.getItinerary().getDestination().equals(airport)) {
                        temp_list.add(r);
                    }
                }
            }
        }
        Collections.sort(temp_list);
        return temp_list;

    }

    public ArrayList<Reservation> retrieve(String passenger, String origin, String destination){
        ArrayList<Reservation> temp_list = new ArrayList<Reservation>();
        for (Reservation r : reservationList){
            if (r.getPassengerName().equals(passenger)){
                if (r.getItinerary().getOrigin().equals(origin) && r.getItinerary().getDestination().equals(destination)) {
                    temp_list.add(r);
                }
            }
        }
        Collections.sort(temp_list);
        return temp_list;

    }

    public void updateItineraryList(ArrayList<Itinerary> list){
        recentItineraryList = list;
    }

    static class ReservationWithState{
        String state; // delete OR reserve - representing the INITIAL state in which this reservation was last called. This should never change!
        Reservation reservation;
        public ReservationWithState(String state, Reservation reservation){
            this.reservation = reservation;
            this.state = state;
        }
        public String getState(){
            return state;
        }
        public Reservation getReservation(){
            return reservation;
        }
    }
}

