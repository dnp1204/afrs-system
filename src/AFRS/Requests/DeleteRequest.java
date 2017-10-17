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
        //format should passenger,origin,destination
        String passenger = params[0];
        String origin = params[1];
        String destination = params[2];
        ArrayList<String> response = new ArrayList<String>();
        response.add(ReservationDatabase.delete(passenger, origin, destination));

        return response;
    }
}
