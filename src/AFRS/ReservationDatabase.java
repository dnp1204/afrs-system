package AFRS;

import AFRS.Model.Itinerary;
import AFRS.Model.Reservation;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class ReservationDatabase {
    private static ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
    //this needs to read from file to populate and be persistent reservation database
    public static ArrayList<Itinerary> recentItineraryList = new ArrayList<Itinerary>();

    private final String FILEPATH = "/AFRS/DATA/reservations.txt";


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
            BufferedReader inFile = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(FILEPATH)));
            String inputLine;

            while ((inputLine = inFile.readLine()) != null) {
                //each line is a new reservation
                Reservation new_reservation = new Reservation();
                //separate passenger name from rest
                String[] reservationInfo = inputLine.split(",",2);
                new_reservation.setPassengerName(reservationInfo[0]);
                //call Itinerary constructor with string parameter
                Itinerary new_itinerary_from_string = new Itinerary(reservationInfo[2]);
                new_reservation.setItinerary(new_itinerary_from_string);

                reservationList.add(new_reservation);
                System.out.print(new_reservation);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //write to new txt file to save current db
    public void shutDown() throws IOException{
        StringBuffer output = new StringBuffer();
        for ( Reservation r : reservationList){
            output.append(r.toString());
            output.append("/n");
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(FILEPATH));
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
            Itinerary itinerary = recentItineraryList.get(id + 1); // use id + 1 to handle 0-based array index
            for (Reservation r : reservationList) {
                if (r.getPassengerName().equals(passengerName)){
                    Itinerary check_itinerary = r.getItinerary();
                    if (check_itinerary.toString().equals(itinerary.toString())){
                        return "error,duplicate reservation";

                    }
                }

            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return "error, invalid id";
        }

        Reservation new_reservation = new Reservation();
        new_reservation.setPassengerName(passengerName);
        new_reservation.setItinerary(recentItineraryList.get(id + 1));
        //use id + 1 to handle the 0-based index of array
        reservationList.add(new_reservation);
        return("reserve,successful");

        //TODO; error handle duplicate reservation
    }

    public static String delete(String passenger, String origin, String destination) {
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

    public static ArrayList<Reservation> retrieve(String passenger){
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
    public static ArrayList<Reservation> retrieve(String passenger, String origin){
        ArrayList<Reservation> temp_list = new ArrayList<Reservation>();
        for (Reservation r : reservationList){
            if (r.getPassengerName().equals(passenger)){
                if (r.getItinerary().getOrigin().equals(origin)) {
                    temp_list.add(r);
                }
            }
        }
        Collections.sort(temp_list);
        return temp_list;

    }

//    public ArrayList<Reservation> retrieve(String passenger, String destination){
//       //same as above
//        return;
//    }

    public static ArrayList<Reservation> retrieve(String passenger, String origin, String destination){
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

    public void updateItineraryList(ArrayList list){
        recentItineraryList = list;
    }
}

