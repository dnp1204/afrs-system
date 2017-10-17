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
        //format should be retrieve,passenger,origin,destination
        //needs to handle ommision of origin or omision of destination
        //always sort return by origin
        String passenger = params[1];
        String origin = params[2];
        String destination = params[3];
        ArrayList<Reservation> list = ReservationDatabase.retrieve(passenger, origin, destination);
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("retrieve,"+list.size());
        for(int i = 0; i < list.size(); i++) {
            stringList.add((i+1)+","+list.get(i).getItinerary().toString());
        }

        return stringList;
    }
}
