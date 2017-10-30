package AFRS.Model;

import AFRS.Model.Flight;
import AFRS.Model.FlightComponent;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

public class Itinerary implements FlightComponent {

    private String origin;
    private String destination;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private int airfare;
    private int flightCount;

    private ArrayList<Flight> flightList;

    public Itinerary(ArrayList<Flight> flightArray) {

        origin = flightArray.get(0).getOrigin();
        destination = flightArray.get(flightArray.size() - 1).getDestination();
        departureTime = flightArray.get(0).getDepartureTime();
        arrivalTime = flightArray.get(flightArray.size() - 1).getArrivalTime();

        flightList = new ArrayList<>(flightArray);
        airfare = calculateFlightAirfare();
        flightCount = flightList.size();

    }

    public Itinerary(String itineraryInformation) {

        ArrayList<String> itineraryComponents = new ArrayList<>(Arrays.asList(itineraryInformation.split(",")));
        flightList = new ArrayList<>();
        airfare = Integer.parseInt(itineraryComponents.get(0));
        flightCount = Integer.parseInt(itineraryComponents.get(1));

        for (int i = 2; i < itineraryComponents.size(); i += 5) {
            LocalTime departTime = calculateTime(itineraryComponents.get(i + 2));
            LocalTime arriveTime = calculateTime(itineraryComponents.get(i + 4));
            Flight newFlight = new Flight(Integer.parseInt(itineraryComponents.get(i)), itineraryComponents.get(i + 1), departTime, itineraryComponents.get(i + 3), arriveTime, 0);
            flightList.add(newFlight);
        }

        origin = itineraryComponents.get(3);
        destination = itineraryComponents.get(itineraryComponents.size() - 2);

    }

    private LocalTime calculateTime(String givenTime) {

        int hours = Integer.parseInt(givenTime.split(":")[0]);
        if (givenTime.split(":")[1].contains("p")) {
            hours += 12;
        }

        if (hours == 12) {
            hours = 0;
        } else if (hours == 24) {
            hours = 12;
        }

        int minutes = Integer.parseInt(givenTime.split(":")[1].substring(0, 2));


        LocalTime time = LocalTime.of(hours, minutes);

        return time;

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

        return itineraryRundown.substring(0, itineraryRundown.length() - 1);
    }

}