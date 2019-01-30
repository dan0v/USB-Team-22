package uk.ac.newcastle.team22.usb.coreUSB;

/**
 * A class which represents a floor in USB.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class Resource {

    private ResourceType type;
    private boolean free = false; //usable computer? (if computer type)

    public Resource(ResourceType type, boolean free){
        this.type = type;
        this.free = free;
    }

    public ResourceType getType(){
        return type;
    }

    public boolean isFree(){
        return free;
    }
}
