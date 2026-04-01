# Multilevel Parking Lot Design (LLD)

This repository contains the Low-Level Design (LLD) and Java implementation for a Multilevel Parking Lot System. The solution is designed to be modular, extensible, and strictly adheres to SOLID object-oriented design principles without overengineering.

## 🎯 Problem Statement Highlights
The system provides a programmatic API to manage a parking lot with the following core constraints:
1. Supports three slot types (`SMALL`, `MEDIUM`, `LARGE`) and vehicle types (`TWO_WHEELER`, `CAR`, `BUS`).
2. Pricing is calculated hourly based on the **allocated slot type**, not the vehicle type.
3. Vehicles entering through different gates are assigned the **nearest available compatible slot**.
4. Smaller vehicles can park in larger slots if their designated slots are full.

## 🏗️ Design Approach & Architecture

### 1. Strategy Pattern (Open/Closed Principle)
To ensure the system is easily extensible, core business rules are abstracted behind interfaces rather than hardcoded into the service layer:
* **`SlotAssignmentStrategy`**: Encapsulates the logic for finding the nearest compatible slot. The current implementation (`NearestSlotAssignmentStrategy`) uses a compatibility map to allow smaller vehicles into larger slots and calculates the shortest distance from the entry gate.
* **`PricingStrategy`**: Encapsulates the billing logic. The current implementation (`HourlyPricingStrategy`) calculates the bill based on dynamic hourly rates passed during initialization.
* *Benefit:* If the parking lot introduces "Surge Pricing" or "VIP Slot Assignment", new strategies can be injected without modifying the `ParkingLotService`.

### 2. Loose Coupling & Dependency Injection
The `ParkingLotService` acts as an orchestrator. It does not create its own slots or define its own rules. All configurations (slot distances, compatibility rules, pricing rates) are injected via the constructor in `Main.java`. This makes the core domain logic highly testable.

### 3. Handling Multiple Gates & Floors (Nearest Slot Logic)
To satisfy the requirement of finding the nearest slot from multiple gates, each `ParkingSlot` object maintains a `distanceToGates` map (e.g., `Gate1 -> 10m`, `Gate2 -> 50m`). The system filters available slots and uses Java Streams to find the minimum distance relative to the requested `entryGateID`. Floors are inherently handled by assigning larger distances to slots on higher levels.

## 📂 Project Structure

```text
src/
├── Main.java                               # Interactive CLI and configuration setup
├── enums/
│   ├── SlotType.java                       # SMALL, MEDIUM, LARGE
│   └── VehicleType.java                    # TWO_WHEELER, CAR, BUS
├── exceptions/
│   └── ParkingLotException.java            # Custom domain exceptions
├── models/
│   ├── ParkingSlot.java                    # Maintains state, floor, and distance metrics
│   ├── ParkingTicket.java                  # Immutable record of entry
│   └── Vehicle.java                        # Vehicle details
├── strategies/
│   ├── HourlyPricingStrategy.java          # Calculates bill (Ceiling hours * Rate)
│   ├── NearestSlotAssignmentStrategy.java  # Finds closest compatible slot
│   ├── PricingStrategy.java                # Interface
│   └── SlotAssignmentStrategy.java         # Interface
└── services/
    └── ParkingLotService.java              # Exposes park(), exit(), and status() APIs
```

## 🚀 How to Run

1. **Navigate to the source directory:**
   ```bash
   cd src
   ```

2. **Compile the Java files:**
   ```bash
   javac Main.java
   ```

3. **Run the interactive application:**
   ```bash
   java Main
   ```

## 💻 Available CLI Commands
Once the application starts, you can interact with the system using the following commands:

* **Park a Vehicle:** `park <LicensePlate> <VehicleType> <RequestedSlotType> <GateID>`
  * *Example:* `park KA-01 CAR MEDIUM Gate1`
* **Check Status:** `status`
  * *Shows the current count of available slots grouped by type.*
* **Exit a Vehicle:** `exit <TicketID> <SimulatedHoursParked>`
  * *Example:* `exit 5be47400 2`
* **Quit:** `quit`

## 📊 UML Class Diagram
![Parking Lot UML Diagram](./uml.png)