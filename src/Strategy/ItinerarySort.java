package Strategy;

import AFRS.Model.Itinerary;

import java.util.ArrayList;

public interface ItinerarySort {
    public ArrayList<Itinerary> doSort(ArrayList<Itinerary> itineraryList);
}
