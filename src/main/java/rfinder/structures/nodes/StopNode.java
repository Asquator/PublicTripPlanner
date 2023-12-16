package rfinder.structures.nodes;

import rfinder.structures.general.Location;

public class StopNode extends PathNode {

     private final String stopId;

     public StopNode(Location location, String stopId){
         super(location);
         this.stopId = stopId;
     }

    public String getStopId() {
        return stopId;
    }

    @Override
    public String toString() {
        return super.toString() + "[stop id" + stopId + "]";
    }
}
