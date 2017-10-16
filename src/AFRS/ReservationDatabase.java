package AFRS;

import Model.Itinerary;
import Model.Reservation;

public class ReservationDatabase {
    private ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
    //this needs to read from file to populate and be persistent reservation database
    public ArrayList<Itinerary> recentInventoryList = new ArrayList<Itinerary>();


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
            BufferedReader  inFile = new BufferedReader (new FileReader ("ReservationDB.txt"));
            String inputLine;

            while ((inputLine = inFile.readLine())!=null) {
                Reservation new_reservation = new Reservation();
                String[] reservationInfo = inputLine.split(",");
                //split into each reservation
                //then split passenger name and Itinerary string
                new_reservation.setPassengerName(reservationInfo[0])
                //call Itinerary constructor with string parameter
                Itinerary new_itinerary_from_string = new Itinerary(reservationInfo[1]);
                new_reservation.setItinerary(new_itinerary_from_string);

                reservationList.add(new_reservation);
            }
            System.out.print(student);
        } catch (FileNotFoundException e) {
            // this is what happens if there is no previous reservation list to populate db
            e.printStackTrace();
        }
    }
    //write to new txt file to save current db
    private void shutDown() {
        //write to a file new db CSV
    }

    //called by client to reserve an itinerary that was just queried
    //params: itinerary (Itinerary) - the itinerary to be reserved4

    private void reserve(Itinerary itinerary, String pasengerName) {

    }

}

