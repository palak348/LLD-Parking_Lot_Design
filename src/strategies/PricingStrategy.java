package strategies;

import models.ParkingTicket;
import java.time.LocalDateTime;

public interface PricingStrategy {
    double calculatePrice(ParkingTicket ticket, LocalDateTime exitTime);
}