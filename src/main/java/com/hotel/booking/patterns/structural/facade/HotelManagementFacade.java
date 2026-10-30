package com.hotel.booking.patterns.structural.facade;

import com.hotel.booking.dto.BookingFormDTO;
import com.hotel.booking.dto.CostPreviewDTO;
import com.hotel.booking.entity.BookingEntity;
import com.hotel.booking.entity.InvoiceEntity;
import com.hotel.booking.entity.RoomEntity;
import com.hotel.booking.patterns.behavioral.state.RoomStateContext;
import com.hotel.booking.patterns.behavioral.strategy.IPaymentStrategy;
import com.hotel.booking.patterns.behavioral.strategy.PaymentContext;
import com.hotel.booking.patterns.behavioral.strategy.PaymentResult;
import com.hotel.booking.patterns.creational.builder.BookingOrderBuilder;
import com.hotel.booking.patterns.creational.factory.RoomFactory;
import com.hotel.booking.patterns.creational.singleton.SystemAuditLogSingleton;
import com.hotel.booking.patterns.structural.decorator.*;
import com.hotel.booking.repository.BookingRepository;
import com.hotel.booking.repository.InvoiceRepository;
import com.hotel.booking.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

/**
 * Facade Pattern (Structural Group)
 * Provides a unified, high-level interface to the complex subsystems consisting of:
 * - Creational: Factory Method, Builder, Singleton
 * - Structural: Decorator
 * - Behavioral: State, Strategy
 */
@Service
@Transactional
public class HotelManagementFacade {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final InvoiceRepository invoiceRepository;

    public HotelManagementFacade(RoomRepository roomRepository,
                                 BookingRepository bookingRepository,
                                 InvoiceRepository invoiceRepository) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * 1. Factory Method Integration: Creates a new room using RoomFactory
     */
    public RoomEntity createRoomViaFactory(String roomNumber, String type) {
        SystemAuditLogSingleton.getInstance().log(
                "Structural",
                "Facade Pattern",
                String.format("HotelManagementFacade: Điều phối tạo phòng mới [%s - %s] qua RoomFactory.", roomNumber, type)
        );

        RoomEntity room = RoomFactory.createRoomEntity(roomNumber, type);
        return roomRepository.save(room);
    }

    /**
     * 2. Builder + Decorator + State Integration: Books a room
     */
    public BookingEntity bookRoomWithBuilder(BookingFormDTO dto) {
        SystemAuditLogSingleton.getInstance().log(
                "Structural",
                "Facade Pattern",
                String.format("HotelManagementFacade: Bắt đầu quy trình đặt phòng số ID [%d] cho khách [%s].",
                        dto.getRoomId(), dto.getCustomerName())
        );

        RoomEntity room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thông tin phòng với ID: " + dto.getRoomId()));

        // Apply State Pattern to transition room to BOOKED
        RoomStateContext stateContext = new RoomStateContext(room);
        stateContext.book(); // Throws IllegalStateException if not in AVAILABLE state
        roomRepository.save(room);

        // Calculate nights
        long days = ChronoUnit.DAYS.between(dto.getCheckInDate(), dto.getCheckOutDate());
        int nights = (int) Math.max(1, days);

        // Apply Decorator Pattern to calculate estimated cost
        IBookingCostComponent costComponent = assembleCostDecorator(
                room,
                nights,
                dto.isExtraBed(),
                dto.isBreakfastIncluded(),
                dto.isSpaPackage(),
                dto.isLaundryService(),
                dto.isAirportPickup()
        );
        double estimatedTotal = costComponent.calculateTotalCost();

        // Apply Builder Pattern to construct the BookingEntity
        BookingOrderBuilder builder = new BookingOrderBuilder();
        BookingEntity booking = builder
                .customerInfo(dto.getCustomerName(), dto.getCustomerPhone())
                .room(room)
                .dates(dto.getCheckInDate(), dto.getCheckOutDate())
                .nights(nights)
                .withExtraBed(dto.isExtraBed())
                .withBreakfast(dto.isBreakfastIncluded())
                .withSpaPackage(dto.isSpaPackage())
                .withLaundryService(dto.isLaundryService())
                .withAirportPickup(dto.isAirportPickup())
                .withSpecialRequests(dto.getSpecialRequests())
                .totalEstimatedCost(estimatedTotal)
                .status("CONFIRMED")
                .build();

        return bookingRepository.save(booking);
    }

    /**
     * 3. State Pattern Integration: Dispatches lifecycle transitions
     */
    public RoomEntity changeRoomState(Long roomId, String action) {
        RoomEntity room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng với ID: " + roomId));

        RoomStateContext stateContext = new RoomStateContext(room);
        String normalizedAction = action != null ? action.trim().toLowerCase() : "";

        SystemAuditLogSingleton.getInstance().log(
                "Structural",
                "Facade Pattern",
                String.format("HotelManagementFacade: Nhận lệnh chuyển trạng thái '%s' cho phòng %s.", action, room.getRoomNumber())
        );

        switch (normalizedAction) {
            case "book" -> stateContext.book();
            case "checkin", "check-in" -> {
                stateContext.checkIn();
                // Update associated confirmed booking to CHECKED_IN
                bookingRepository.findFirstByRoomIdAndStatusInOrderByCreatedAtDesc(roomId, List.of("CONFIRMED"))
                        .ifPresent(b -> {
                            b.setStatus("CHECKED_IN");
                            bookingRepository.save(b);
                        });
            }
            case "checkout", "check-out" -> stateContext.checkOut();
            case "finishmaintenance", "finish-maintenance", "clean" -> stateContext.finishMaintenance();
            case "cancel" -> {
                stateContext.cancelBooking();
                bookingRepository.findFirstByRoomIdAndStatusInOrderByCreatedAtDesc(roomId, List.of("CONFIRMED"))
                        .ifPresent(b -> {
                            b.setStatus("CANCELLED");
                            bookingRepository.save(b);
                        });
            }
            default -> throw new IllegalArgumentException("Hành động trạng thái không hợp lệ: " + action);
        }

        return roomRepository.save(room);
    }

    /**
     * 4. Decorator Pattern Integration: Previews itemized cost before checkout
     */
    @Transactional(readOnly = true)
    public CostPreviewDTO previewCheckoutCost(Long bookingId) {
        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn đặt phòng với ID: " + bookingId));

        RoomEntity room = booking.getRoom();
        IBookingCostComponent costComponent = assembleCostDecorator(
                room,
                booking.getNights(),
                booking.isExtraBed(),
                booking.isBreakfastIncluded(),
                booking.isSpaPackage(),
                booking.isLaundryService(),
                booking.isAirportPickup()
        );

        double totalAmount = costComponent.calculateTotalCost();
        String costBreakdown = costComponent.getCostBreakdown();

        SystemAuditLogSingleton.getInstance().log(
                "Structural",
                "Facade Pattern",
                String.format("HotelManagementFacade: Tạo bảng kê chi phí thanh toán cho đơn [%s] - Tổng tiền: %,.0f VNĐ.",
                        booking.getBookingCode(), totalAmount)
        );

        return new CostPreviewDTO(
                booking.getId(),
                booking.getBookingCode(),
                booking.getCustomerName(),
                booking.getCustomerPhone(),
                room.getRoomNumber(),
                room.getRoomType(),
                booking.getNights(),
                room.getBasePrice(),
                totalAmount,
                costBreakdown
        );
    }

    /**
     * 5. Strategy + State + Decorator Integration: Completes checkout and issues invoice
     */
    public InvoiceEntity processCompleteCheckout(Long bookingId, String paymentMethod) {
        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn đặt phòng với ID: " + bookingId));

        RoomEntity room = booking.getRoom();

        // 1. Calculate final cost via Decorator Pattern
        IBookingCostComponent costComponent = assembleCostDecorator(
                room,
                booking.getNights(),
                booking.isExtraBed(),
                booking.isBreakfastIncluded(),
                booking.isSpaPackage(),
                booking.isLaundryService(),
                booking.isAirportPickup()
        );
        double subTotal = costComponent.calculateTotalCost();
        String costBreakdown = costComponent.getCostBreakdown();

        // 2. Execute Payment via Strategy Pattern
        PaymentContext paymentContext = new PaymentContext();
        paymentContext.setStrategyByCode(paymentMethod);
        PaymentResult paymentResult = paymentContext.executePayment(subTotal, booking.getCustomerName());

        // 3. Issue and persist InvoiceEntity
        String invoiceCode = "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        InvoiceEntity invoice = new InvoiceEntity(
                invoiceCode,
                booking,
                paymentResult.getSubTotal(),
                paymentResult.getAdjustmentAmount(),
                paymentResult.getFinalAmount(),
                paymentMethod,
                costBreakdown + "\n" + paymentResult.getMessage(),
                paymentResult.getTransactionRef()
        );
        InvoiceEntity savedInvoice = invoiceRepository.save(invoice);

        // 4. Update Booking status to CHECKED_OUT
        booking.setStatus("CHECKED_OUT");
        bookingRepository.save(booking);

        // 5. Transition Room State via State Pattern (Occupied -> Maintenance)
        RoomStateContext stateContext = new RoomStateContext(room);
        stateContext.checkOut();
        roomRepository.save(room);

        SystemAuditLogSingleton.getInstance().log(
                "Structural",
                "Facade Pattern",
                String.format("HotelManagementFacade: Đã hoàn tất quy trình Check-out toàn diện cho đơn [%s]. Hóa đơn xuất: [%s] trị giá %,.0f VNĐ.",
                        booking.getBookingCode(), invoiceCode, paymentResult.getFinalAmount())
        );

        return savedInvoice;
    }

    /**
     * Helper to dynamically assemble Decorator chain
     */
    public IBookingCostComponent assembleCostDecorator(RoomEntity room, int nights,
                                                        boolean extraBed, boolean breakfast,
                                                        boolean spa, boolean laundry,
                                                        boolean airportPickup) {
        IBookingCostComponent component = new BaseRoomCost(
                room.getRoomNumber(),
                room.getRoomType(),
                room.getBasePrice(),
                nights,
                extraBed
        );

        if (breakfast) {
            component = new BreakfastDecorator(component, nights);
        }
        if (spa) {
            component = new SpaDecorator(component);
        }
        if (laundry) {
            component = new LaundryDecorator(component);
        }
        if (airportPickup) {
            component = new AirportPickupDecorator(component);
        }

        return component;
    }

    // Facade Query Methods for Controllers
    @Transactional(readOnly = true)
    public List<RoomEntity> getAllRooms() {
        return roomRepository.findAll();
    }

    @Transactional(readOnly = true)
    public RoomEntity getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng với ID: " + id));
    }

    @Transactional(readOnly = true)
    public java.util.Optional<BookingEntity> getActiveBookingForRoom(Long roomId) {
        return bookingRepository.findFirstByRoomIdAndStatusInOrderByCreatedAtDesc(roomId, List.of("CONFIRMED", "CHECKED_IN"));
    }

    @Transactional(readOnly = true)
    public List<BookingEntity> getAllBookings() {
        return bookingRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<InvoiceEntity> getAllInvoices() {
        return invoiceRepository.findAllByOrderByPaymentDateDesc();
    }

    public List<com.hotel.booking.patterns.creational.singleton.AuditLogEntry> getAuditLogs() {
        return SystemAuditLogSingleton.getInstance().getLogs();
    }

    public void clearAuditLogs() {
        SystemAuditLogSingleton.getInstance().clear();
    }
}
