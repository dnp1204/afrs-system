package AFRS;

import AFRS.Model.Itinerary;
import AFRS.Model.Reservation;
import com.sun.org.apache.regexp.internal.RE;
import jdk.nashorn.internal.runtime.arrays.ArrayIndex;
import org.omg.CORBA.OBJECT_NOT_EXIST;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class ReservationDatabase {
    private ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
    //this needs to read from file to populate and be persistent reservation database
    public ArrayList<Itinerary> recentItineraryList = new ArrayList<Itinerary>();


//- startUp() : void
//- shutDown(): void
//+ reserve(params) : void
//+ delete(params) : void
//+ retrieve(params) : reservation
//+ updateInventoryList(ArrayList) : void


    //try to creat a db from existing text file on start up
    private void startUp(){
        try {
            //ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
            BufferedReader inFile = new BufferedReader(new FileReader("ReservationDB.txt"));
            String inputLine;

            while ((inputLine = inFile.readLine()) != null) {
                Reservation new_reservation = new Reservation();
                String[] reservationInfo = inputLine.split(",");
                //split into each reservation
                //then split passenger name and Itinerary string
                new_reservation.setPassengerName(reservationInfo[0])
                //call Itinerary constructor with string parameter
                Itinerary new_itinerary_from_string = new Itinerary(reservationInfo[1]); //TODO: create a Itinerary constructor for String param
                new_reservation.setItinerary(new_itinerary_from_string);

                reservationList.add(new_reservation);
                System.out.print(new_reservation);
            }

        } catch (FileNotFoundException e) {
            // this is what happens if there is no previous reservation list to populate db
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //write to new txt file to save current db
    private void shutDown() throws IOException{
        StringBuffer output = new StringBuffer();
        for ( Reservation r : reservationList){
            output.append(r.toString());
            output.append("/n");
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter("ReservationDB.txt"));
        writer.write(String.valueOf(output));
        writer.close();
        }

    //called by client to reserve an itinerary that was just queried
    //params: itinerary (Itinerary) - the itinerary to be reserved4

    private String reserve(int id, String passengerName) {

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
    public ArrayList<Reservation> retrieve(String passenger, String origin){
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

    public ArrayList<Reservation> retrieve(String passenger, String destination){
       //same as above
        return;
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


}

