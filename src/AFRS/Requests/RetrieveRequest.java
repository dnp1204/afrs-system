package AFRS.Requests;

import AFRS.Model.Airport;
import AFRS.Model.Reservation;
import AFRS.ReservationDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class RetrieveRequest implements Request {

    private ReservationDatabase reservationDB;
    private HashMap<String, Airport> airportMap;

    public RetrieveRequest(HashMap<String, Airport> airportMap, ReservationDatabase reservationDB) {
        this.airportMap = airportMap;
        this.reservationDB = reservationDB;
    }

    @Override
    public ArrayList<String> doRequest(String clientID, String[] params) {
        //format should be passenger,origin,destination
        //needs to handle omission of origin or omission of destination
        //always sort return by origin
        String passenger, origin, destination;
        ArrayList<Reservation> list;
        ArrayList<String> stringList = new ArrayList<>();
        switch(params.length) {
            case 1:
                passenger = params[0];
                list = reservationDB.retrieve(passenger);
                break;
            case 2:
                passenger = params[0];
                origin = params[1];

                if  (!airportMap.containsKey(origin)) {
                    stringList.add("error,unknown origin");
                    return stringList;
                }

                list = reservationDB.retrieve(passenger, origin, true);
                break;
            case 3:
                passenger = params[0];
                origin = params[1];
                if (!airportMap.containsKey(origin) && !origin.equals("")) {
                    stringList.add("error,unknown origin");
                    return stringList;
                }

                destination = params[2];
                if (!airportMap.containsKey(destination)) {
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
