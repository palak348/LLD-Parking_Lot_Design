package strategies;

import enums.SlotType;
import exceptions.ParkingLotException;
import models.ParkingSlot;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

// Finds nearest slot and allows small vehicles in big slots (Requirement 5 & 6)
public class NearestSlotAssignmentStrategy implements SlotAssignmentStrategy {
    private final Map<SlotType, List<SlotType>> compatibilityMap;

    public NearestSlotAssignmentStrategy(Map<SlotType, List<SlotType>> compatibilityMap) {
        this.compatibilityMap = compatibilityMap;
    }

    @Override
    public ParkingSlot findSlot(List<ParkingSlot> slots, SlotType requestedType, String gateId) {
        List<SlotType> allowedTypes = compatibilityMap.getOrDefault(requestedType, List.of(requestedType));
        return slots.stream()
                .filter(ParkingSlot::isAvailable)
                .filter(slot -> allowedTypes.contains(slot.getType()))
                .min(Comparator.comparingInt(slot -> slot.getDistanceToGate(gateId)))
                .orElseThrow(() -> new ParkingLotException("No available slots found"));
    }
}