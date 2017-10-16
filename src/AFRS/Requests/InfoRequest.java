package AFRS.Requests;

import AFRS.ReservationDatabase;

import java.util.ArrayList;

public class InfoRequest implements Request {

    private ReservationDatabase reservationDB;

    public InfoRequest(ReservationDatabase reservationDB) {
        this.reservationDB = reservationDB;
    }

    @Override
    public ArrayList<String> doRequest(String[] params) {
        return null;
    }
}