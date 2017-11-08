package AFRS;

import AFRS.Model.Itinerary;
import AFRS.Model.Reservation;

import java.io.*;
import java.util.*;

public class ReservationDatabase {
    private static ArrayList<Reservation> reservationList = new ArrayList<>();
    //this needs to read from file to populate and be persistent reservation database
    private static HashMap<String,ArrayList<Itinerary>> recentItineraryLists = new HashMap<>();

    private final String FILEPATH = "/reservations.txt";

    private static HashMap<String,Stack<ReservationWithState>> undoStackMap = new HashMap<>();
    private static HashMap<String,Stack<ReservationWithState>> redoStackMap = new HashMap<>();


    //+ redo() : String
    //+ undo() : String
    //+ removeClient() : void
    //+ startUp() : void
    //+ shutDown(): void
    //+ reserve(params) : String
    //+ delete(params) : String
    //+ retrieve(params) : ArrayList<Reservation>
    //+ updateItineraryList(ArrayList) : void

    /**
     *
     * @param clientID (String)
     * @return error or success message
     *
     * This method handles "redo" command. Pop from redoStack and determine the state of the request (either delete or reserve) and
     * executes that request through an overloaded reserve() or delete() method.
     *
     * Note: this method pushes the request to undoStack. Be careful to not duplicate that in the execution of the command.
     */
    public String redo(String clientID){
        //check if the redoStack is empty
        if (!redoStackMap.containsKey(clientID)) {
            return "error,no request available";
        }
        try {
            ReservationWithState rs = redoStackMap.get(clientID).pop();
            undoStackMap.get(clientID).push(rs);
            if (rs.getState().equals("delete")) {
                return "redo,delete," + delete(rs);
            } else {
                return "redo,reserve," + reserve(rs);
            }
        }
        // handles empty stack
        catch (EmptyStackException e){
            return "error, no request available";
        }
    }

    /**
     *
     * @param clientID
     * @return error or success message
     *
     * This method handles "undo" command. Pop from undoStack and determine the state of the request (either delete or reserve) and
     * executes the opposite request through an overloaded reserve() or delete() method.
     *
     * Note: this method pushes the request to redoStack. Be careful to not duplicate that in the execution of the command.
     */
    public String undo(String clientID){
        //check if the undoStack is empty
        if (!undoStackMap.containsKey(clientID)) {
            return "error,no requests available";
        }
        // create redo stack if it does not already exist
        if (!redoStackMap.containsKey(clientID)) {
            redoStackMap.put(clientID, new Stack<>());
        }
        try {
            ReservationWithState rs = undoStackMap.get(clientID).pop();
            redoStackMap.get(clientID).push(rs);
            if (rs.getState().equals("delete")) {
                return "undo,delete," + reserve(rs);
            } else {
                return "undo,reserve," + delete(rs);
            }
        }
        // handles empty stack
        catch (EmptyStackException e){
            return "error, no request available";
        }
    }

    /**
     *
     * @param clientID
     *
     * Clears the data stored for a client who is closing their session
     */
    public void removeClient(String clientID) {
        recentItineraryLists.remove(clientID);
    }

    /**
     * Try to populate reservationList from existing text file on start up, loading the data from
     * the previous session.
     */
    public void startUp(){
        try {
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

    /**
     * Write reservationList to txt file to save current data. This will be read later on startUp() allowing the
     * system to load data from a previous session.
     */

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


    /**
     *
     * @param clientID (String)
     * @param itineraryID (int) - the id of the itinerary form the list of recent itineraries, starting with 1.
     * @param passengerName (String) - passenger name
     * @return String - error or success message
     *
     *  Called by client to reserve an itinerary that was just queried with info request.
     *  When called, this method will create a ReservationWithState Object and push that onto the Undo stack.
     *  Additionally, it will reset the Redo Stack.
     */

    public static String reserve(String clientID, int itineraryID, String passengerName) {

        // check if id is valid
        // then check if this reservation already exists

        try{
            Itinerary itinerary = recentItineraryLists.get(clientID).get(itineraryID - 1); // use id-1 to handle 0-based array index
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
            return "error, invalid id";
        }
        catch (NullPointerException e){
            return "error, invalid id";
        }
        catch (NumberFormatException e) {
            return "error,invalid id";
        }

        Reservation new_reservation = new Reservation();
        new_reservation.setPassengerName(passengerName);
        new_reservation.setItinerary(recentItineraryLists.get(clientID).get(itineraryID - 1));
        //use id-1 to handle the 0-based index of array
        reservationList.add(new_reservation);

        // reserve request is valid so instantiate a undoStack if one does not already exist.
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


    /**
     *
     * @param reservationFromStack (ReservationWithState) - A reservation object obtained from undo or redo stack
     * @return error or success message
     *
     * This method is only called by undo() and redo(). It is an overload of the reserve method above to accept the
     * ReservationWithState object as input.
     *
     * Note: This method does not push a request to the undoStack as that is handled by the calling function.
     */
     public static String reserve(ReservationWithState reservationFromStack) {

        // check if id is valid
        // then check if this reservation already exists

        String passenger = reservationFromStack.getReservation().getPassengerName();
        Itinerary RFS_itinerary = reservationFromStack.getReservation().getItinerary();
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
        return passenger+","+RFS_itinerary;
    }



    /**
     *
     * @param clientID (String)
     * @param passenger (String) - passenger name
     * @param origin (String) - origin airport code
     * @param destination (String) - destination airport code
     * @return String - error or success message
     *
     *  When called, this method will create a ReservationWithState Object with the "delete" state and
     *  push that onto the Undo stack for the given client.
     *  Additionally, it will reset the Redo Stack.
     */
        public String delete(String clientID, String passenger, String origin, String destination) {
        for (Reservation r : reservationList){
            if (r.getPassengerName().equals(passenger)){
                Itinerary itinerary = r.getItinerary();
                if (itinerary.getOrigin().equals(origin) && itinerary.getDestination().equals(destination)){
                    reservationList.remove(r);

                    if (!undoStackMap.containsKey(clientID)) {
                        undoStackMap.put(clientID, new Stack<>());
                    }
                    ReservationWithState rs = new ReservationWithState("delete", r);
                    undoStackMap.get(clientID).push(rs);

                    //check if that client had an existing redoStack and clear it
                    if (redoStackMap.containsKey(clientID)) {
                        redoStackMap.remove(clientID);
                    }

                    return "delete,successful";
                }
            }
        }
        return "error,reservation not found";

    }

    /**
     *
     * @param reservationFromStack (ReservationWithState) - A reservation object obtained from undo or redo stack
     * @return error or success message
     *
     * This method is only called by undo() and redo(). It is an overload of the delete method above to accept the
     * ReservationWithState object as input.
     *
     * Note: This method does not push a request to the undoStack as that is handled by the calling function.
     */
    public String delete(ReservationWithState reservationFromStack) {
        String passenger = reservationFromStack.getReservation().getPassengerName();
        Itinerary itinerary = reservationFromStack.getReservation().getItinerary();

        for (Reservation r : reservationList){
            if (r.getPassengerName().equals(passenger)){
                Itinerary check_itinerary = r.getItinerary();
                if (check_itinerary.getOrigin().equals(itinerary.getOrigin()) && check_itinerary.getDestination().equals(itinerary.getDestination())){
                    reservationList.remove(r);
                    return passenger+","+itinerary;
                }
            }
        }
        return "error,reservation not found";

    }

    /**
     *
     * @param passenger (String) - passenger name
     * @return temp_list (ArrayList<Reservation>) - A list of reservations for the given passenger
     *
     * This method handles just one parameter. It is overloaded below to handle the 2 optional parameters.
     */
    public ArrayList<Reservation> retrieve(String passenger){
        ArrayList<Reservation> temp_list = new ArrayList<>();
        for (Reservation r : reservationList){
            if (r.getPassengerName().equals(passenger)){
                temp_list.add(r);
            }
        }
        Collections.sort(temp_list);
        return temp_list;
    }

    /**
     *
     * @param passenger (String) - passenger name
     * @param airport (String) - airport code (this could be a destination or origin)
     * @param origin (boolean) - indicator if the lone airport provided is an origin airport (false means it is a destination airport)
     * @return temp_list (ArrayList<Reservation>) - A list of reservations for the given passenger containing the lone origin/destination airport
     *
     * This overloads the method to handle 2 parameters
     */
    public ArrayList<Reservation> retrieve(String passenger, String airport, boolean origin){
        ArrayList<Reservation> temp_list = new ArrayList<>();
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

    /**
     *
     * @param passenger (String) - passenger name
     * @param origin (String) - origin airport code
     * @param destination (String) - destination airport code
     * @return temp_list (ArrayList<Reservation>) - A single reservation for the passenger with specified origin and destination
     *
     * This overloads the method to handle all 3 parameters
     */
    public ArrayList<Reservation> retrieve(String passenger, String origin, String destination){
        ArrayList<Reservation> temp_list = new ArrayList<>();
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

    /**
     *
     * @param list (ArrayList<Itinerary>) - the most recent list of Itineraries generated by an info request
     * @param clientID (String)
     *
     *  Updates the recentItineraryLists Hash Map with the most recent info request per client
     */
    public void updateItineraryList(ArrayList<Itinerary> list, String clientID){
        recentItineraryLists.put(clientID, list);
    }

    /**
     * This internal class is used in the undoStack and redoStack in order to identify the state
     * in which the reservations are added to the stack. The state will be either reserve or delete.
     */
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

