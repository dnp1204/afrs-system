package AFRS.Model;

import java.time.LocalTime;
import java.util.ArrayList;

public class Itinerary implements FlightComponent {

    public String origin;
    public String destination;
    public LocalTime departureTime;
    public LocalTime arrivalTime;
    public int airfare;
    public int flightCount;

    public ArrayList<Flight> flightList;

    public Itinerary(ArrayList<Flight> flightArray) {

        origin = flightArray.get(0).getOrigin();
        destination = flightArray.get(flightArray.size() - 1).getDestination();
        departureTime = flightArray.get(0).getDepartureTime();
        arrivalTime = flightArray.get(flightArray.size() - 1).getArrivalTime();

        flightList = flightArray;
        airfare = calculateFlightAirfare();
        flightCount = flightList.size();

    }

    public int calculateFlightAirfare() {

        int totalCost = 0;

        for (Flight f : flightList) { totalCost += f.getAirfare(); }

        return totalCost;
    }


    public int getAirfare() { return airfare; }

    public int getFlightCount() { return flightCount; }

    public LocalTime getDepartureTime() { return departureTime; }

    public LocalTime getArrivalTime() { return arrivalTime; }

    public String getOrigin() { return origin; }

    public String getDestination() {  return destination; }

    public String toString() {

        String itineraryRundown = getAirfare() + "," + getFlightCount() + ",";

        for (Flight leg : flightList) {
            itineraryRundown += leg.toString() + ",";
        }

        itineraryRundown.substring(0, itineraryRundown.length() - 1);

        return itineraryRundown;
    }

}