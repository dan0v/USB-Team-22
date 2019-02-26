package uk.ac.newcastle.team22.usb.search;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Map;

import uk.ac.newcastle.team22.usb.R;

/**
 * The class from which the search algorithm is called.
 * Searches through all fields in staff, rooms, cafe menu, room resources
 * Each object that is found to match will have a copy created, assigned an enum for reason, and a priority based on % string match to the field
 * This SearchResult object will be placed in Search class map, which will be returned to display the results.
 */
public class Search extends AppCompatActivity {

    private Map<ResultReason, SearchResult> searchResults;

    public Map<ResultReason, SearchResult> getSearchResults() {
        return searchResults;
    }

    //actual search algorithm
    public Map<ResultReason, SearchResult> search(){
        return getSearchResults();
    }

    //% string match algorithm called in search function
    public void assignPriority(){

    }
}
