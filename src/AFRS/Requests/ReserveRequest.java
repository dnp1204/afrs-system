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
        //params should be id,passenger
        int id = Integer.parseInt(params[0]);
        String passenger = params[1];
        ArrayList<String> response = new ArrayList<String>();
        response.add(ReservationDatabase.reserve(id, passenger));
        return response;
    }
}
