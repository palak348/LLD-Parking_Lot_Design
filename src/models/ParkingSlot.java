package models;

import enums.SlotType;
import java.util.Map;

public class ParkingSlot {
    private final String id;
    private final int floor; // Naya field add kiya
    private final SlotType type;
    private boolean isAvailable = true;
    private final Map<String, Integer> distanceToGates;

    public ParkingSlot(String id, int floor, SlotType type, Map<String, Integer> distanceToGates) {
        this.id = id;
        this.floor = floor; // Set kiya
        this.type = type;
        this.distanceToGates = distanceToGates;
    }

    public String getId() {
        return id;
    }

    public int getFloor() {
        return floor;
    } // Naya getter

    public SlotType getType() {
        return type;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    public int getDistanceToGate(String gateId) {
        return distanceToGates.getOrDefault(gateId, Integer.MAX_VALUE);
    }
}