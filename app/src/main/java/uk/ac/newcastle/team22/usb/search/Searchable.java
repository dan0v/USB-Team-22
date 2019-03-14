package uk.ac.newcastle.team22.usb.search;

import java.util.List;

/**
 * Interface that marks objects as searchable.
 *
 * @author Patrick Lindley
 * @version 1.0
 */

public interface Searchable {

    List<ResultReason> getSearchableReasons();
}
