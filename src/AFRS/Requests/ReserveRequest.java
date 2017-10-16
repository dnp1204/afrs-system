package AFRS.Requests;

import AFRS.ReservationDatabase;

import java.util.ArrayList;

public class ReserveRequest implements Request {

    private ReservationDatabase reservationDB;

    public ReserveRequest(ReservationDatabase reservationDB) {
        this.reservationDB = reservationDB;
    }

    @Override
    public ArrayList<String> doRequest(String[] params) {
        //params should be reserve,id,passenger
        int id = Integer.parseInt(params[1]);
        String passenger = params[2];
        ReservationDatabase.reserve(id, passenger);
        return null;
    }
}
