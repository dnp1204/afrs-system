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
        return null;
    }
}
