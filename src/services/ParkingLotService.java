package services;

import enums.SlotType;
import exceptions.ParkingLotException;
import models.*;
import strategies.PricingStrategy;
import strategies.SlotAssignmentStrategy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

// Exposes main APIs
public class ParkingLotService {
    private final List<ParkingSlot> slots;
    private final SlotAssignmentStrategy assignmentStrategy;
    private final PricingStrategy pricingStrategy;

    public ParkingLotService(List<ParkingSlot> slots, SlotAssignmentStrategy as, PricingStrategy ps) {
        this.slots = slots;
        this.assignmentStrategy = as;
        this.pricingStrategy = ps;
    }

    public ParkingTicket park(Vehicle vehicle, LocalDateTime entryTime, SlotType requestedType, String entryGateId) {
        ParkingSlot allocatedSlot = assignmentStrategy.findSlot(slots, requestedType, entryGateId);
        allocatedSlot.setAvailable(false);
        String ticketId = UUID.randomUUID().toString().substring(0, 8);
        return new ParkingTicket(ticketId, vehicle, allocatedSlot.getId(), allocatedSlot.getType(), entryTime);
    }

    public double exit(ParkingTicket ticket, LocalDateTime exitTime) {
        ParkingSlot slot = slots.stream().filter(s -> s.getId().equals(ticket.getSlotId())).findFirst()
                .orElseThrow(() -> new ParkingLotException("Invalid Slot ID"));
        slot.setAvailable(true);
        return pricingStrategy.calculatePrice(ticket, exitTime);
    }

    public Map<SlotType, Long> status() {
        return slots.stream().filter(ParkingSlot::isAvailable)
                .collect(Collectors.groupingBy(ParkingSlot::getType, Collectors.counting()));
    }
}