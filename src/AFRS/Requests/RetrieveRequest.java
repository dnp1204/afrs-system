package AFRS.Requests;

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
        ArrayList<String> list = ReservationDatabase.retrieve(passenger, origin, destination);

        return list;
    }
}
