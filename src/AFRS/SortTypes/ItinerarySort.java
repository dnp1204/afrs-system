package AFRS.SortTypes;

import AFRS.Model.Itinerary;
import java.util.ArrayList;

/**
 * Created by tylercollins on 10/16/17.
 */
public interface ItinerarySort {

    public ArrayList<Itinerary> doSort(ArrayList<Itinerary> itineraryList);

}