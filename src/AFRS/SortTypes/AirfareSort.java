package AFRS.SortTypes;

import AFRS.Model.Itinerary;

import java.util.ArrayList;

/**
 * Created by tylercollins on 10/16/17.
 */
public class AirfareSort implements ItinerarySort {

    public ArrayList<Itinerary> doSort(ArrayList<Itinerary> itineraryList) {

        ArrayList<Itinerary> newSort = new ArrayList<Itinerary>();

        for (Itinerary itin : itineraryList) {

            if (newSort.isEmpty()) {
                newSort.add(itin);
                continue;
            }

            boolean didAdd = false;

            for (int i = 0; i < newSort.size(); i++) {

                if (itin.getAirfare() <= newSort.get(i).getAirfare()) {
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
