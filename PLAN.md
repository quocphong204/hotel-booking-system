# PROJECT SPECIFICATION: HOTEL BOOKING MANAGEMENT SYSTEM (SPRING BOOT + 6 DESIGN PATTERNS)

## 1.Role & Objective
Build a complete, runnable Java Spring Boot 3.x Web MVC application for a **Hotel Room Booking Management System**.
This is an academic software architecture project requiring strict adherence to **MVC Architecture**, **SOLID Principles**, and **6 Explicit Design Patterns** (2 Creational, 2 Structural, 2 Behavioral).

## 2. Critical Implementation Rules (MUST FOLLOW)
1. **NO Lombok `@Builder` for the Builder Pattern**: You must write every Design Pattern class explicitly with clean interfaces and concrete implementations under `com.hotel.booking.patterns.*`.
2. **Zero-Config Database**: Use Spring Data JPA with H2 In-Memory Database (`jdbc:h2:mem:hoteldb`) and create a `DataSeeder` (`CommandLineRunner`) that pre-populates 6 rooms across Standard, Deluxe, and Suite categories so the app works immediately on startup.
3. **Visual Pattern Traceability**: Every time a Design Pattern is triggered from the Web UI, log the exact pattern name, class name, and action into `SystemAuditLogSingleton` and display this live "Pattern Execution Log" on the Web UI sidebar so the evaluator can see the patterns working in real time.

## 3. Detailed Specification of the 6 Design Patterns

### Group 1: Creational Patterns (Minimum 2)
1. **Factory Method Pattern (`patterns.creational.factory`)**
   - `IRoomProduct`: Interface with `getRoomType()`, `getBasePrice()`, `getDefaultAmenities()`, `getMaxOccupancy()`.
   - Concrete classes: `StandardRoomProduct` (500,000 VND), `DeluxeRoomProduct` (1,000,000 VND), `SuiteRoomProduct` (2,500,000 VND).
   - `RoomFactory`: Static/Factory method `createRoomProduct(String type)` that instantiates the right room configuration and initializes `RoomEntity`.
2. **Builder Pattern (`patterns.creational.builder`)**
   - `BookingOrderBuilder`: Explicit fluent builder to construct `BookingEntity` with mandatory fields (`customerName`, `customerPhone`, `room`, `checkInDate`, `checkOutDate`, `nights`) and optional add-ons (`extraBed`, `breakfastIncluded`, `spaPackage`, `laundryService`, `airportPickup`, `specialRequests`).
   - Include `SystemAuditLogSingleton` (`patterns.creational.singleton`) using thread-safe double-checked locking to store live execution logs of all patterns for the UI console.

### Group 2: Structural Patterns (Minimum 2)
3. **Decorator Pattern (`patterns.structural.decorator`)**
   - `IBookingCostComponent`: Interface with `double calculateTotalCost()` and `String getCostBreakdown()`.
   - `BaseRoomCost`: Takes `basePrice * nights` (plus extra bed fee if applicable).
   - `RoomServiceDecorator`: Abstract decorator implementing `IBookingCostComponent`.
   - Concrete Decorators:
     - `BreakfastDecorator` (+200,000 VND/night)
     - `SpaDecorator` (+500,000 VND flat)
     - `LaundryDecorator` (+150,000 VND flat)
     - `AirportPickupDecorator` (+300,000 VND flat)
4. **Facade Pattern (`patterns.structural.facade`)**
   - `HotelManagementFacade` (`@Service`): Simplifies complex workflows for `DashboardController`:
     - `createRoomViaFactory(String roomNumber, String type)`
     - `bookRoomWithBuilder(BookingFormDTO dto)`
     - `changeRoomState(Long roomId, String action)`
     - `previewCheckoutCost(Long bookingId)`
     - `processCompleteCheckout(Long bookingId, String paymentMethod)`

### Group 3: Behavioral Patterns (Minimum 2)
5. **State Pattern (`patterns.behavioral.state`)**
   - `IRoomState`: Interface with methods `book(RoomStateContext ctx)`, `checkIn(RoomStateContext ctx)`, `checkOut(RoomStateContext ctx)`, `finishMaintenance(RoomStateContext ctx)`, `getStateName()`, `getBadgeColor()`.
   - Concrete States:
     - `AvailableState`: Allows `book()` -> transitions to `BOOKED`. Rejects `checkIn()`, `checkOut()`.
     - `BookedState`: Allows `checkIn()` -> transitions to `OCCUPIED`, or cancel -> `AVAILABLE`. Rejects `checkOut()`.
     - `OccupiedState`: Allows `checkOut()` -> transitions to `MAINTENANCE`. Rejects `book()`, `checkIn()`.
     - `MaintenanceState`: Allows `finishMaintenance()` -> transitions to `AVAILABLE`. Rejects all other actions.
   - Invalid transitions must throw `IllegalStateException` with a clear Vietnamese message displayed as an alert on the Web UI.
6. **Strategy Pattern (`patterns.behavioral.strategy`)**
   - `IPaymentStrategy`: Interface with `PaymentResult pay(double amount, String customerName)` and `String getMethodName()`.
   - Concrete Strategies:
     - `CashPaymentStrategy`: Direct cash receipt calculation.
     - `CreditCardPaymentStrategy`: Applies 1.5% card processing fee & simulates card authorization.
     - `VNPayPaymentStrategy`: Applies 5% QR promotion discount & generates simulated transaction ref.
   - `PaymentContext`: Dynamically sets and executes the chosen `IPaymentStrategy` at runtime.

## 4. Phased Implementation Plan (Execute in Order)

### Phase 1: Project Setup, Entities & Repositories
- Generate `pom.xml` with `spring-boot-starter-web`, `spring-boot-starter-thymeleaf`, `spring-boot-starter-data-jpa`, `com.h2database:h2`.
- Create `RoomEntity`, `BookingEntity`, `InvoiceEntity`, and their JPA Repositories.
- Configure `application.properties` (server port 8080, H2 console enabled).

### Phase 2: Implement All 6 Design Patterns
- Implement Creational (`Factory`, `Builder`, `Singleton`), Structural (`Decorator`), and Behavioral (`State`, `Strategy`) packages.
- Ensure every pattern writes a clear Vietnamese trace entry into `SystemAuditLogSingleton.getInstance().log(patternGroup, patternName, detailMessage)`.

### Phase 3: Implement Facade, DataSeeder & MVC Controllers
- Implement `HotelManagementFacade` integrating repositories and all 6 patterns.
- Implement `DataSeeder` to create 6 sample rooms (101-Standard, 102-Standard, 201-Deluxe, 202-Deluxe, 301-Suite, 302-Suite) using `RoomFactory`.
- Implement `DashboardController` (room grid, booking modal, state buttons, checkout page) and `PatternDemoController` (automated 6-test-case runner).

### Phase 4: Build Thymeleaf Web UI (Bootstrap 5)
- `dashboard.html`:
  - Top summary cards (Total Rooms, Available, Booked/Occupied, Maintenance).
  - Room Grid showing Room Number, Type, Price, Amenities, current State Badge, and State Action Buttons (Đặt phòng, Check-in, Check-out, Hoàn tất dọn phòng).
  - Booking Form Modal demonstrating **Builder Pattern** (customer info + optional checkboxes for Extra Bed, Breakfast, Spa, Laundry, Airport Pickup).
  - Right-hand live panel: **"Real-time Design Pattern Execution Log"** pulling from `SystemAuditLogSingleton` so every click shows which Pattern just ran.
- `checkout.html`:
  - Displays itemized bill built dynamically by **Decorator Pattern**.
  - Allows selecting payment method (**Strategy Pattern**: Tiền mặt, Thẻ tín dụng +1.5% phí, VNPay QR -5% giảm giá) and completing checkout via **Facade Pattern**.
- `pattern-lab.html`:
  - A dedicated "Kiểm thử 6 Design Patterns (Test Cases TC01 - TC06)" page with a "Run All 6 Test Cases" button that executes all 6 patterns sequentially and renders a pass/fail table with detailed output for the project defense.

### Phase 5: Verification & Build Check
- Run `mvn clean compile` and fix any compilation or Thymeleaf template binding issues.