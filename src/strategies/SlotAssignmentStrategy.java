package strategies;

import enums.SlotType;
import models.ParkingSlot;
import java.util.List;

public interface SlotAssignmentStrategy {
    ParkingSlot findSlot(List<ParkingSlot> slots, SlotType requestedType, String gateId);
}