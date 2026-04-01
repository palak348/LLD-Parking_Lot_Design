import enums.SlotType;
import enums.VehicleType;
import models.*;
import services.ParkingLotService;
import strategies.HourlyPricingStrategy;
import strategies.NearestSlotAssignmentStrategy;

import java.time.LocalDateTime;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        // 1. Configure Rules (Requirement 6: Smaller in larger slots)
        Map<SlotType, List<SlotType>> compatibilityMap = Map.of(
                SlotType.SMALL, Arrays.asList(SlotType.SMALL, SlotType.MEDIUM, SlotType.LARGE),
                SlotType.MEDIUM, Arrays.asList(SlotType.MEDIUM, SlotType.LARGE),
                SlotType.LARGE, List.of(SlotType.LARGE));

        // 2. Configure Pricing (Requirement 2: Different hourly rates)
        Map<SlotType, Double> hourlyRates = Map.of(
                SlotType.SMALL, 50.0,
                SlotType.MEDIUM, 100.0,
                SlotType.LARGE, 200.0);

        // 3. Create Slots across MULTIPLE LEVELS (Ground Floor = 0, First Floor = 1)
        List<ParkingSlot> slots = Arrays.asList(
                // Ground Floor (Floor 0) - Short distance to gates
                new ParkingSlot("S1", 0, SlotType.SMALL, Map.of("Gate1", 10, "Gate2", 60)),
                new ParkingSlot("M1", 0, SlotType.MEDIUM, Map.of("Gate1", 20, "Gate2", 50)),
                new ParkingSlot("L1", 0, SlotType.LARGE, Map.of("Gate1", 30, "Gate2", 40)),

                // First Floor (Floor 1) - Longer distance to gates
                new ParkingSlot("S2", 1, SlotType.SMALL, Map.of("Gate1", 110, "Gate2", 160)),
                new ParkingSlot("M2", 1, SlotType.MEDIUM, Map.of("Gate1", 120, "Gate2", 150)),
                new ParkingSlot("L2", 1, SlotType.LARGE, Map.of("Gate1", 130, "Gate2", 140)));

        // 4. Initialize Service
        ParkingLotService parkingService = new ParkingLotService(
                slots,
                new NearestSlotAssignmentStrategy(compatibilityMap),
                new HourlyPricingStrategy(hourlyRates));

        // Simulated Database for active tickets
        Map<String, ParkingTicket> activeTickets = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n--- Multilevel Parking Lot System Started ---");
        System.out.println("Type commands to test:");
        System.out.println("1. park <Plate> <VehicleType> <SlotType> <GateId>");
        System.out.println("2. exit <TicketID> <HoursParked>");
        System.out.println("3. status");
        System.out.println("4. quit");

        // 5. User Input Loop
        while (true) {
            System.out.print("\n> ");
            String[] parts = scanner.nextLine().trim().split(" ");
            String cmd = parts[0].toLowerCase();

            try {
                if (cmd.equals("park")) {
                    if (parts.length != 5)
                        throw new IllegalArgumentException("Usage: park <Plate> <Type> <RequestedSlot> <GateId>");

                    Vehicle v = new Vehicle(parts[1], VehicleType.valueOf(parts[2].toUpperCase()));
                    SlotType st = SlotType.valueOf(parts[3].toUpperCase());

                    // Call API
                    ParkingTicket ticket = parkingService.park(v, LocalDateTime.now(), st, parts[4]);
                    activeTickets.put(ticket.getTicketId(), ticket);

                    // Find the allocated slot to print the floor number
                    ParkingSlot allocated = slots.stream().filter(s -> s.getId().equals(ticket.getSlotId())).findFirst()
                            .get();

                    System.out.println("SUCCESS! Allocated Slot: " + ticket.getSlotId() +
                            " (Floor " + allocated.getFloor() + ")" +
                            " | Ticket ID: " + ticket.getTicketId());

                } else if (cmd.equals("exit")) {
                    if (parts.length != 3)
                        throw new IllegalArgumentException("Usage: exit <TicketID> <Hours>");

                    String tId = parts[1];
                    if (!activeTickets.containsKey(tId)) {
                        System.out.println("Error: Ticket ID not found.");
                        continue;
                    }

                    ParkingTicket ticket = activeTickets.get(tId);
                    long hours = Long.parseLong(parts[2]);

                    // Call API
                    double bill = parkingService.exit(ticket, ticket.getEntryTime().plusHours(hours));
                    activeTickets.remove(tId);

                    System.out.println("Vehicle Exited. Total Bill: Rs. " + bill);

                } else if (cmd.equals("status")) {
                    // Call API
                    Map<SlotType, Long> status = parkingService.status();
                    System.out.println("Available Slots:");
                    if (status.isEmpty()) {
                        System.out.println("PARKING LOT IS FULL!");
                    } else {
                        status.forEach((k, v) -> System.out.println(" - " + k + ": " + v));
                    }

                } else if (cmd.equals("quit")) {
                    System.out.println("Shutting down...");
                    break;
                } else {
                    System.out.println("Unknown command.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }
}
