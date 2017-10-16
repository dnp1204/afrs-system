package AFRS.Requests;

import AFRS.ReservationDatabase;

import java.util.ArrayList;

public class DeleteRequest implements Request {

    private ReservationDatabase reservationDB;

    public DeleteRequest(ReservationDatabase reservationDB) {
        this.reservationDB = reservationDB;
    }

    @Override
    public ArrayList<String> doRequest(String[] params) {
        //format should be delete,passenger,origin,destination
        String passenger = params[1];
        String origin = params[2];
        String destination = params[3];
        ReservationDatabase.delete(passenger, origin, destination);

        return null;
    }
}
