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
        return null;
    }
}
