package uk.ac.newcastle.team22.usb.search;

import java.util.List;

/**
 * An interface which defines an object as searchable.
 *
 * @author Patrick Lindley
 * @version 1.0
 */
public interface Searchable {

    /**
     * @return The potential reasons for a search result.
     */
    List<ResultReason> getSearchableReasons();
}
