package Strategy;

import Model.Itinerary;

import java.util.ArrayList;

public interface ItinerarySort {
    public ArrayList<Itinerary> doSort(ArrayList<Itinerary> itineraryList);
}
