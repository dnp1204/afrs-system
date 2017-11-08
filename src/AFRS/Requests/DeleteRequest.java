package AFRS.Requests;

import AFRS.Requests.Request;
import AFRS.ReservationDatabase;

import java.util.ArrayList;

public class DeleteRequest implements Request {

    private ReservationDatabase reservationDB;

    public DeleteRequest(ReservationDatabase reservationDB) {
        this.reservationDB = reservationDB;
    }

    /**
     *
     * @param clientID (String)
     * @param params (String[]) - contains passenger, origin, destination
     * @return error or success message
     *
     * This method overrides doRequest to handle delete requests. It parses the params and calls delete() from ReservationDB.
     */
    @Override
    public ArrayList<String> doRequest(String clientID, String[] params) {
        //format should passenger,origin,destination
        ArrayList<String> response = new ArrayList<>();
        if(params.length != 3) {
            response.add("error,reservation not found");
            return response;
        }
        String passenger = params[0];
        String origin = params[1];
        String destination = params[2];
        response.add(reservationDB.delete(clientID, passenger, origin, destination));

        return response;
    }
}
