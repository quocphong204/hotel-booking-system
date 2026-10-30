package com.hotel.booking.patterns;

import com.hotel.booking.entity.BookingEntity;
import com.hotel.booking.entity.RoomEntity;
import com.hotel.booking.entity.RoomStatus;
import com.hotel.booking.patterns.behavioral.state.RoomStateContext;
import com.hotel.booking.patterns.behavioral.strategy.*;
import com.hotel.booking.patterns.creational.builder.BookingOrderBuilder;
import com.hotel.booking.patterns.creational.factory.*;
import com.hotel.booking.patterns.creational.singleton.SystemAuditLogSingleton;
import com.hotel.booking.patterns.structural.decorator.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit Tests for 6 Design Patterns")
public class DesignPatternsTest {

    @BeforeEach
    void setUp() {
        SystemAuditLogSingleton.getInstance().clear();
    }

    @Test
    @DisplayName("TC01 - Factory Method Pattern: Instantiates correct room products")
    void testFactoryMethodPattern() {
        IRoomProduct standard = RoomFactory.createRoomProduct("STANDARD");
        assertEquals("STANDARD", standard.getRoomType());
        assertEquals(500_000.0, standard.getBasePrice());
        assertEquals(2, standard.getMaxOccupancy());

        IRoomProduct deluxe = RoomFactory.createRoomProduct("DELUXE");
        assertEquals("DELUXE", deluxe.getRoomType());
        assertEquals(1_000_000.0, deluxe.getBasePrice());
        assertEquals(3, deluxe.getMaxOccupancy());

        IRoomProduct suite = RoomFactory.createRoomProduct("SUITE");
        assertEquals("SUITE", suite.getRoomType());
        assertEquals(2_500_000.0, suite.getBasePrice());
        assertEquals(4, suite.getMaxOccupancy());

        RoomEntity roomEntity = RoomFactory.createRoomEntity("101", "STANDARD");
        assertNotNull(roomEntity);
        assertEquals("101", roomEntity.getRoomNumber());
        assertEquals(RoomStatus.AVAILABLE, roomEntity.getStatus());

        assertThrows(IllegalArgumentException.class, () -> RoomFactory.createRoomProduct("PRESIDENTIAL_INVALID"));
    }

    @Test
    @DisplayName("TC02 - Builder Pattern: Assembles BookingEntity with validation and add-ons")
    void testBuilderPattern() {
        RoomEntity room = RoomFactory.createRoomEntity("201", "DELUXE");

        BookingEntity booking = new BookingOrderBuilder()
                .customerInfo("Nguyễn Văn A", "0901234567")
                .room(room)
                .dates(LocalDate.now(), LocalDate.now().plusDays(2))
                .withBreakfast(true)
                .withSpaPackage(true)
                .withAirportPickup(true)
                .withSpecialRequests("Phòng tầng cao, view thoáng")
                .build();

        assertNotNull(booking);
        assertEquals("Nguyễn Văn A", booking.getCustomerName());
        assertEquals("0901234567", booking.getCustomerPhone());
        assertEquals(2, booking.getNights());
        assertTrue(booking.isBreakfastIncluded());
        assertTrue(booking.isSpaPackage());
        assertTrue(booking.isAirportPickup());
        assertFalse(booking.isLaundryService());
        assertEquals("CONFIRMED", booking.getStatus());

        // Validate mandatory failure
        BookingOrderBuilder invalidBuilder = new BookingOrderBuilder();
        assertThrows(IllegalStateException.class, invalidBuilder::build);
    }

    @Test
    @DisplayName("TC03 - Singleton Pattern: Ensures single instance and logs trace")
    void testSingletonPattern() {
        SystemAuditLogSingleton instance1 = SystemAuditLogSingleton.getInstance();
        SystemAuditLogSingleton instance2 = SystemAuditLogSingleton.getInstance();
        assertSame(instance1, instance2);

        instance1.log("Creational", "Singleton Pattern", "Test trace entry");
        assertFalse(instance2.getLogs().isEmpty());
    }

    @Test
    @DisplayName("TC04 - Decorator Pattern: Dynamically computes layered pricing & breakdown")
    void testDecoratorPattern() {
        // Base room: Deluxe 1,000,000 x 2 nights = 2,000,000 + extra bed 150,000 x 2 = 300,000 => 2,300,000
        IBookingCostComponent cost = new BaseRoomCost("201", "DELUXE", 1_000_000.0, 2, true);
        assertEquals(2_300_000.0, cost.calculateTotalCost());

        // Add Breakfast: +200,000 x 2 = 400,000 => 2,700,000
        cost = new BreakfastDecorator(cost, 2);
        assertEquals(2_700_000.0, cost.calculateTotalCost());

        // Add Spa: +500,000 flat => 3,200,000
        cost = new SpaDecorator(cost);
        assertEquals(3_200_000.0, cost.calculateTotalCost());

        // Add Laundry: +150,000 flat => 3,350,000
        cost = new LaundryDecorator(cost);
        assertEquals(3_350_000.0, cost.calculateTotalCost());

        // Add Airport Pickup: +300,000 flat => 3,650,000
        cost = new AirportPickupDecorator(cost);
        assertEquals(3_650_000.0, cost.calculateTotalCost());

        String breakdown = cost.getCostBreakdown();
        assertTrue(breakdown.contains("Tiền phòng cơ bản"));
        assertTrue(breakdown.contains("Buffet sáng"));
        assertTrue(breakdown.contains("Spa"));
        assertTrue(breakdown.contains("Giặt ủi"));
        assertTrue(breakdown.contains("sân bay"));
    }

    @Test
    @DisplayName("TC05 - State Pattern: Validates full room lifecycle and rejects invalid transitions")
    void testStatePattern() {
        RoomEntity room = new RoomEntity("301", "SUITE", 2_500_000.0, "Luxury", 4);
        RoomStateContext context = new RoomStateContext(room);

        // 1. Initial State: AVAILABLE
        assertEquals(RoomStatus.AVAILABLE, room.getStatus());

        // Invalid action: checkIn when available
        assertThrows(IllegalStateException.class, context::checkIn);
        assertThrows(IllegalStateException.class, context::checkOut);

        // 2. Action: book -> BOOKED
        context.book();
        assertEquals(RoomStatus.BOOKED, room.getStatus());
        assertThrows(IllegalStateException.class, context::book); // Can't book again

        // 3. Action: checkIn -> OCCUPIED
        context.checkIn();
        assertEquals(RoomStatus.OCCUPIED, room.getStatus());
        assertThrows(IllegalStateException.class, context::checkIn);

        // 4. Action: checkOut -> MAINTENANCE
        context.checkOut();
        assertEquals(RoomStatus.MAINTENANCE, room.getStatus());
        assertThrows(IllegalStateException.class, context::checkOut);
        assertThrows(IllegalStateException.class, context::book);

        // 5. Action: finishMaintenance -> AVAILABLE
        context.finishMaintenance();
        assertEquals(RoomStatus.AVAILABLE, room.getStatus());
    }

    @Test
    @DisplayName("TC06 - Strategy Pattern: Verifies Cash, Credit Card (+1.5%), and VNPay (-5%)")
    void testStrategyPattern() {
        double baseAmount = 1_000_000.0;
        PaymentContext context = new PaymentContext();

        // Cash: 0% adjustment
        context.setStrategy(new CashPaymentStrategy());
        PaymentResult cashResult = context.executePayment(baseAmount, "Khách hàng Cash");
        assertEquals(baseAmount, cashResult.getFinalAmount());
        assertEquals(0.0, cashResult.getAdjustmentAmount());
        assertTrue(cashResult.getTransactionRef().startsWith("CASH-"));

        // Credit Card: +1.5% fee
        context.setStrategy(new CreditCardPaymentStrategy());
        PaymentResult ccResult = context.executePayment(baseAmount, "Khách hàng Visa");
        assertEquals(1_015_000.0, ccResult.getFinalAmount());
        assertEquals(15_000.0, ccResult.getAdjustmentAmount());
        assertTrue(ccResult.getTransactionRef().startsWith("CC-AUTH-"));

        // VNPay QR: -5% discount
        context.setStrategy(new VNPayPaymentStrategy());
        PaymentResult vnPayResult = context.executePayment(baseAmount, "Khách hàng VNPay");
        assertEquals(950_000.0, vnPayResult.getFinalAmount());
        assertEquals(-50_000.0, vnPayResult.getAdjustmentAmount());
        assertTrue(vnPayResult.getTransactionRef().startsWith("VNPAY-QR-"));
    }
}
