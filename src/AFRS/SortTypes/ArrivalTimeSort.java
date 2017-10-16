package AFRS.SortTypes;

import AFRS.Model.Itinerary;

import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Created by tylercollins on 10/16/17.
 */
public class ArrivalTimeSort implements ItinerarySort {

    public ArrayList<Itinerary> doSort(ArrayList<Itinerary> itineraryList) {

        ArrayList<Itinerary> newSort = new ArrayList<>();

        for (Itinerary itin : itineraryList) {

            if (newSort.isEmpty()) {
                newSort.add(itin);
                continue;
            }

            boolean didAdd = false;

            LocalTime arrivalTime = itin.getArrivalTime();

            for (int i = 0; i < newSort.size(); i++) {

                LocalTime iArrivalTime = newSort.get(i).getArrivalTime();

                if (arrivalTime.isBefore(iArrivalTime) || arrivalTime == iArrivalTime) {
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
