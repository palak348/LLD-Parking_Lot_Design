package models;

import enums.SlotType;
import java.time.LocalDateTime;

// Ticket data (Requirement 3 & 7)
public class ParkingTicket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final String slotId;
    private final SlotType allocatedSlotType;
    private final LocalDateTime entryTime;

    public ParkingTicket(String tId, Vehicle v, String sId, SlotType sType, LocalDateTime eTime) {
        this.ticketId = tId;
        this.vehicle = v;
        this.slotId = sId;
        this.allocatedSlotType = sType;
        this.entryTime = eTime;
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getSlotId() {
        return slotId;
    }

    public SlotType getAllocatedSlotType() {
        return allocatedSlotType;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}