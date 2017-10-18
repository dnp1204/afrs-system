package AFRS.Requests;

import AFRS.Model.Reservation;
import AFRS.ReservationDatabase;

import java.util.ArrayList;

public class RetrieveRequest implements Request {

    private ReservationDatabase reservationDB;

    public RetrieveRequest(ReservationDatabase reservationDB) {
        this.reservationDB = reservationDB;
    }

    @Override
    public ArrayList<String> doRequest(String[] params) {
        //format should be passenger,origin,destination
        //needs to handle omission of origin or omission of destination
        //always sort return by origin
        String passenger = "", origin = "", destination = "";
        ArrayList<Reservation> list;
        switch(params.length) {
            case 1:
                passenger = params[0];
                list = reservationDB.retrieve(passenger);
                break;
            case 2:
                passenger = params[0];
                origin = params[1];
                list = reservationDB.retrieve(passenger, origin, true);
                break;
            case 3:
                passenger = params[0];
                origin = params[1];
                destination = params[2];
                if(origin.equals("")) {
                    list = reservationDB.retrieve(passenger, destination, false);
                } else {
                    list = reservationDB.retrieve(passenger, origin, destination);
                }
                break;
            default:
                list = new ArrayList<>(0);
        }
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("retrieve,"+list.size());
        for(int i = 0; i < list.size(); i++) {
            stringList.add((i+1)+","+list.get(i).getItinerary().toString());
        }

        return stringList;
    }
}
