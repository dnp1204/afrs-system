package AFRS.Requests;

import AFRS.Requests.Request;
import AFRS.ReservationDatabase;

import java.util.ArrayList;

public class ReserveRequest implements Request {

    private ReservationDatabase reservationDB;

    public ReserveRequest(ReservationDatabase reservationDB) {
        this.reservationDB = reservationDB;
    }


    /**
     *
     * @param clientId (String)
     * @param params (String[]) - contains id (id of an itinerary from the most recent info request), passengerName
     * @return error or success message
     *
     * This method overrides the doRequest method to handle making a new reservation. This method parses the params and
     * calls reserve() from ReservationDB
     */
    @Override
    public ArrayList<String> doRequest(String clientId, String[] params) {
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
        response.add(ReservationDatabase.reserve(clientId, id, passenger));
        return response;
    }
}
