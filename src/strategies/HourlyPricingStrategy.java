package strategies;

import enums.SlotType;
import models.ParkingTicket;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

// Calculates bill based on hourly rate of allocated slot (Requirement 2, 4, 7)
public class HourlyPricingStrategy implements PricingStrategy {
    private final Map<SlotType, Double> hourlyRates;

    public HourlyPricingStrategy(Map<SlotType, Double> hourlyRates) {
        this.hourlyRates = hourlyRates;
    }

    @Override
    public double calculatePrice(ParkingTicket ticket, LocalDateTime exitTime) {
        long minutes = ChronoUnit.MINUTES.between(ticket.getEntryTime(), exitTime);
        long hours = (long) Math.ceil(minutes / 60.0);
        return Math.max(1, hours) * hourlyRates.get(ticket.getAllocatedSlotType());
    }
}