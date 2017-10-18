package AFRS.Requests;

import AFRS.Model.Airport;
import AFRS.Model.Reservation;
import AFRS.ReservationDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RetrieveRequest implements Request {

    private ReservationDatabase reservationDB;

    private ArrayList<String> airportList = new ArrayList<>();

    public RetrieveRequest(ReservationDatabase reservationDB) {
        this.reservationDB = reservationDB;
        BufferedReader br;
        String line;

        // Read airport's name
        br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/AFRS/Data/airports.txt")));
        try {
            line = br.readLine();
            while (line != null) {
                String[] airport = line.split(",");
                airportList.add(airport[0]);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<String> doRequest(String[] params) {
        //format should be passenger,origin,destination
        //needs to handle omission of origin or omission of destination
        //always sort return by origin
        String passenger = "", origin = "", destination = "";
        ArrayList<Reservation> list;
        ArrayList<String> stringList = new ArrayList<>();
        boolean sendError = true;
        switch(params.length) {
            case 1:
                passenger = params[0];
                list = reservationDB.retrieve(passenger);
                break;
            case 2:
                passenger = params[0];
                origin = params[1];

                for (String s:
                     airportList) {
                    if(origin.equals(s))
                        sendError = false;
                }
                if (sendError) {
                    stringList.add("error,unknown origin");
                    return stringList;
                }

                list = reservationDB.retrieve(passenger, origin, true);
                break;
            case 3:
                passenger = params[0];
                origin = params[1];

                for (String s:
                        airportList) {
                    if(origin.equals(s))
                        sendError = false;
                }
                if (sendError && !origin.equals("")) {
                    stringList.add("error,unknown origin");
                    return stringList;
                }

                destination = params[2];

                sendError = true;
                for (String s:
                        airportList) {
                    if(destination.equals(s))
                        sendError = false;
                }
                if (sendError) {
                    stringList.add("error,unknown destination");
                    return stringList;
                }

                if(origin.equals("")) {
                    list = reservationDB.retrieve(passenger, destination, false);
                } else {
                    list = reservationDB.retrieve(passenger, origin, destination);
                }
                break;
            default:
                list = new ArrayList<>(0);
        }
        stringList.add("retrieve,"+list.size());
        for(int i = 0; i < list.size(); i++) {
            stringList.add((i+1)+","+list.get(i).getItinerary().toString());
        }

        return stringList;
    }
}
