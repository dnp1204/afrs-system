package AFRS.SortTypes;

import AFRS.Model.Itinerary;

import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Created by tylercollins on 10/14/17.
 */
public class DepartureTimeSort implements ItinerarySort {

    public ArrayList<Itinerary> doSort(ArrayList<Itinerary> itineraryList) {

        ArrayList<Itinerary> newSort = new ArrayList<>();

        for (Itinerary itin : itineraryList) {

            if (newSort.isEmpty()) {
                newSort.add(itin);
                continue;
            }

            boolean didAdd = false;

            LocalTime departureTime = itin.getDepartureTime();

            for (int i = 0; i < newSort.size(); i++) {

                LocalTime idepartureTime = newSort.get(i).getDepartureTime();

                if (departureTime.isBefore(idepartureTime) || departureTime == idepartureTime) {
                    newSort.add(i, itin);
                    didAdd = true;
                    break;
                }
            }
            if (!didAdd)
                newSort.add(itin);
        }

        return newSort;

    }

}