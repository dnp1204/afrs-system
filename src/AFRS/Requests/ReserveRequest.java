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
        int id;
        ArrayList<String> response = new ArrayList<String>();
        try {
            id = Integer.parseInt(params[0]);
        } catch (NumberFormatException e) {
            response.add("error,invalid id");
            return response;
        }
        String passenger = params[1];
        response.add(ReservationDatabase.reserve(id, passenger));
        return response;
    }
}
