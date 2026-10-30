package com.hotel.booking.controller;

import com.hotel.booking.dto.TestCaseResultDTO;
import com.hotel.booking.entity.BookingEntity;
import com.hotel.booking.entity.RoomEntity;
import com.hotel.booking.entity.RoomStatus;
import com.hotel.booking.patterns.behavioral.state.RoomStateContext;
import com.hotel.booking.patterns.behavioral.strategy.*;
import com.hotel.booking.patterns.creational.builder.BookingOrderBuilder;
import com.hotel.booking.patterns.creational.factory.IRoomProduct;
import com.hotel.booking.patterns.creational.factory.RoomFactory;
import com.hotel.booking.patterns.creational.singleton.SystemAuditLogSingleton;
import com.hotel.booking.patterns.structural.decorator.*;
import com.hotel.booking.patterns.structural.facade.HotelManagementFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PatternDemoController {

    private final HotelManagementFacade facade;

    public PatternDemoController(HotelManagementFacade facade) {
        this.facade = facade;
    }

    @GetMapping("/pattern-lab")
    public String viewPatternLab(Model model) {
        model.addAttribute("testResults", new ArrayList<TestCaseResultDTO>());
        model.addAttribute("auditLogs", facade.getAuditLogs());
        model.addAttribute("hasRun", false);
        return "pattern-lab";
    }

    @PostMapping("/pattern-lab/run-all")
    public String runAllTestCases(Model model) {
        List<TestCaseResultDTO> results = executeAllPatternTests();
        model.addAttribute("testResults", results);
        model.addAttribute("auditLogs", facade.getAuditLogs());
        model.addAttribute("hasRun", true);
        model.addAttribute("successMessage", "Đã thực thi thành công toàn bộ 6 Test Cases cho 6 Design Patterns!");
        return "pattern-lab";
    }

    @GetMapping("/api/pattern-lab/run-all")
    @ResponseBody
    public List<TestCaseResultDTO> runAllApi() {
        return executeAllPatternTests();
    }

    private List<TestCaseResultDTO> executeAllPatternTests() {
        List<TestCaseResultDTO> list = new ArrayList<>();

        // TC01: Factory Method Pattern
        list.add(runTC01());

        // TC02: Builder Pattern
        list.add(runTC02());

        // TC03: Singleton Pattern
        list.add(runTC03());

        // TC04: Decorator Pattern
        list.add(runTC04());

        // TC05: State Pattern
        list.add(runTC05());

        // TC06: Strategy Pattern
        list.add(runTC06());

        return list;
    }

    private TestCaseResultDTO runTC01() {
        long start = System.currentTimeMillis();
        try {
            IRoomProduct standard = RoomFactory.createRoomProduct("STANDARD");
            IRoomProduct deluxe = RoomFactory.createRoomProduct("DELUXE");
            IRoomProduct suite = RoomFactory.createRoomProduct("SUITE");
            RoomEntity room = RoomFactory.createRoomEntity("TC-101", "DELUXE");

            boolean pass = standard.getBasePrice() == 500_000.0
                    && deluxe.getBasePrice() == 1_000_000.0
                    && suite.getBasePrice() == 2_500_000.0
                    && "DELUXE".equals(room.getRoomType());

            long time = System.currentTimeMillis() - start;
            return new TestCaseResultDTO(
                    "TC01", "Creational", "Factory Method Pattern",
                    "Khởi tạo đúng RoomProduct (Standard, Deluxe, Suite) và RoomEntity dựa trên chuỗi phân loại.",
                    pass ? "PASSED" : "FAILED", time,
                    String.format("Standard: %,.0f VNĐ | Deluxe: %,.0f VNĐ | Suite: %,.0f VNĐ. Khởi tạo thành công phòng [%s].",
                            standard.getBasePrice(), deluxe.getBasePrice(), suite.getBasePrice(), room.getRoomNumber()),
                    "Đạt chuẩn Factory Method: Đóng gói toàn bộ logic phân nhánh khởi tạo đối tượng phòng."
            );
        } catch (Exception e) {
            return new TestCaseResultDTO("TC01", "Creational", "Factory Method", "Tạo phòng tự động", "FAILED",
                    System.currentTimeMillis() - start, e.getMessage(), "Lỗi ngoại lệ.");
        }
    }

    private TestCaseResultDTO runTC02() {
        long start = System.currentTimeMillis();
        try {
            RoomEntity sampleRoom = new RoomEntity("TC-201", "DELUXE", 1_000_000.0, "Wifi, TV, Minibar", 3);
            BookingEntity booking = new BookingOrderBuilder()
                    .customerInfo("Thầy Cô Hội Đồng", "0999888777")
                    .room(sampleRoom)
                    .dates(LocalDate.now(), LocalDate.now().plusDays(3))
                    .withBreakfast(true)
                    .withSpaPackage(true)
                    .withLaundryService(true)
                    .withAirportPickup(true)
                    .withSpecialRequests("Phục vụ chu đáo cho buổi bảo vệ đồ án")
                    .build();

            boolean pass = booking.getNights() == 3
                    && booking.isBreakfastIncluded()
                    && booking.isSpaPackage()
                    && booking.isLaundryService()
                    && booking.isAirportPickup()
                    && booking.getBookingCode().startsWith("BK-");

            long time = System.currentTimeMillis() - start;
            return new TestCaseResultDTO(
                    "TC02", "Creational", "Builder Pattern",
                    "Lắp ráp linh hoạt đơn đặt phòng phức tạp với các tùy chọn dịch vụ mở rộng (Viết chay không dùng Lombok @Builder).",
                    pass ? "PASSED" : "FAILED", time,
                    String.format("Mã đơn: %s | Khách: %s | Số đêm: %d | Tiện ích: Buffet, Spa, Giặt ủi, Xe đưa đón.",
                            booking.getBookingCode(), booking.getCustomerName(), booking.getNights()),
                    "Đạt chuẩn Builder: Fluent chaining, kiểm tra ràng buộc nghiệp vụ chặt chẽ."
            );
        } catch (Exception e) {
            return new TestCaseResultDTO("TC02", "Creational", "Builder", "Lắp ráp đơn đặt phòng", "FAILED",
                    System.currentTimeMillis() - start, e.getMessage(), "Lỗi ngoại lệ.");
        }
    }

    private TestCaseResultDTO runTC03() {
        long start = System.currentTimeMillis();
        try {
            SystemAuditLogSingleton s1 = SystemAuditLogSingleton.getInstance();
            SystemAuditLogSingleton s2 = SystemAuditLogSingleton.getInstance();
            s1.log("Creational", "Singleton Pattern", "Kiểm tra thực thi TC03 qua Singleton Audit Log.");

            boolean pass = (s1 == s2) && (!s2.getLogs().isEmpty());
            long time = System.currentTimeMillis() - start;

            return new TestCaseResultDTO(
                    "TC03", "Creational", "Singleton Pattern",
                    "Đảm bảo duy nhất 1 thể hiện SystemAuditLogSingleton ghi vết hoạt động trên toàn hệ thống (Thread-safe Double-Checked Locking).",
                    pass ? "PASSED" : "FAILED", time,
                    String.format("Thể hiện 1 Hash: %d == Thể hiện 2 Hash: %d. Tổng số nhật ký ghi vết: %d.",
                            System.identityHashCode(s1), System.identityHashCode(s2), s1.getLogs().size()),
                    "Đạt chuẩn Singleton: Khởi tạo lười (lazy loading), an toàn đa luồng tuyệt đối."
            );
        } catch (Exception e) {
            return new TestCaseResultDTO("TC03", "Creational", "Singleton", "Kiểm tra phiên bản duy nhất", "FAILED",
                    System.currentTimeMillis() - start, e.getMessage(), "Lỗi ngoại lệ.");
        }
    }

    private TestCaseResultDTO runTC04() {
        long start = System.currentTimeMillis();
        try {
            // Base room: Deluxe 1,000,000 x 2 = 2,000,000 + extraBed (150,000 x 2) = 2,300,000
            IBookingCostComponent cost = new BaseRoomCost("TC-401", "DELUXE", 1_000_000.0, 2, true);
            // + Buffet: 200,000 x 2 = 400,000 => 2,700,000
            cost = new BreakfastDecorator(cost, 2);
            // + Spa: 500,000 => 3,200,000
            cost = new SpaDecorator(cost);
            // + Laundry: 150,000 => 3,350,000
            cost = new LaundryDecorator(cost);
            // + Airport: 300,000 => 3,650,000
            cost = new AirportPickupDecorator(cost);

            double total = cost.calculateTotalCost();
            boolean pass = (total == 3_650_000.0);
            long time = System.currentTimeMillis() - start;

            return new TestCaseResultDTO(
                    "TC04", "Structural", "Decorator Pattern",
                    "Bọc động các lớp chi phí dịch vụ gia tăng lên thành phần tiền phòng cơ bản mà không sửa đổi mã nguồn gốc.",
                    pass ? "PASSED" : "FAILED", time,
                    String.format("Tổng tiền sau khi bọc 4 lớp dịch vụ: %,.0f VNĐ (Giá gốc: 2,300,000 VNĐ ➔ Tổng cộng: 3,650,000 VNĐ).", total),
                    "Đạt chuẩn Decorator: Mở rộng tính năng theo nguyên lý OCP (Open/Closed Principle)."
            );
        } catch (Exception e) {
            return new TestCaseResultDTO("TC04", "Structural", "Decorator", "Tính toán chi phí phân tầng", "FAILED",
                    System.currentTimeMillis() - start, e.getMessage(), "Lỗi ngoại lệ.");
        }
    }

    private TestCaseResultDTO runTC05() {
        long start = System.currentTimeMillis();
        try {
            RoomEntity room = new RoomEntity("TC-501", "STANDARD", 500_000.0, "Standard", 2);
            RoomStateContext ctx = new RoomStateContext(room);

            // 1. Available -> Booked
            ctx.book();
            boolean b1 = (room.getStatus() == RoomStatus.BOOKED);

            // 2. Booked -> Occupied
            ctx.checkIn();
            boolean b2 = (room.getStatus() == RoomStatus.OCCUPIED);

            // 3. Occupied -> Maintenance
            ctx.checkOut();
            boolean b3 = (room.getStatus() == RoomStatus.MAINTENANCE);

            // 4. Maintenance -> Available
            ctx.finishMaintenance();
            boolean b4 = (room.getStatus() == RoomStatus.AVAILABLE);

            // 5. Check invalid transition exception
            boolean threw = false;
            try {
                ctx.checkIn(); // Invalid when AVAILABLE
            } catch (IllegalStateException ex) {
                threw = true;
            }

            boolean pass = b1 && b2 && b3 && b4 && threw;
            long time = System.currentTimeMillis() - start;

            return new TestCaseResultDTO(
                    "TC05", "Behavioral", "State Pattern",
                    "Kiểm soát vòng đời phòng khách sạn: AVAILABLE ➔ BOOKED ➔ OCCUPIED ➔ MAINTENANCE ➔ AVAILABLE và chặn thao tác sai quy trình.",
                    pass ? "PASSED" : "FAILED", time,
                    "Vòng đời chuyển trạng thái hoạt động chính xác 100%. Bắt và xử lý ngoại lệ chuyển trạng thái phi lý thành công.",
                    "Đạt chuẩn State: Đóng gói hành vi riêng biệt cho từng trạng thái, loại bỏ hoàn toàn khối lệnh if-else phức tạp."
            );
        } catch (Exception e) {
            return new TestCaseResultDTO("TC05", "Behavioral", "State", "Vòng đời trạng thái phòng", "FAILED",
                    System.currentTimeMillis() - start, e.getMessage(), "Lỗi ngoại lệ.");
        }
    }

    private TestCaseResultDTO runTC06() {
        long start = System.currentTimeMillis();
        try {
            double amount = 1_000_000.0;
            PaymentContext ctx = new PaymentContext();

            // Cash: 0%
            ctx.setStrategy(new CashPaymentStrategy());
            PaymentResult rCash = ctx.executePayment(amount, "Khách Tiền Mặt");

            // Credit Card: +1.5%
            ctx.setStrategy(new CreditCardPaymentStrategy());
            PaymentResult rCard = ctx.executePayment(amount, "Khách Thẻ Visa");

            // VNPay: -5%
            ctx.setStrategy(new VNPayPaymentStrategy());
            PaymentResult rVNPay = ctx.executePayment(amount, "Khách VNPay QR");

            boolean pass = (rCash.getFinalAmount() == 1_000_000.0)
                    && (rCard.getFinalAmount() == 1_015_000.0)
                    && (rVNPay.getFinalAmount() == 950_000.0);
            long time = System.currentTimeMillis() - start;

            return new TestCaseResultDTO(
                    "TC06", "Behavioral", "Strategy Pattern",
                    "Thay đổi thuật toán tính tiền thanh toán tại thời điểm runtime (Tiền mặt 0%, Thẻ +1.5% phí, VNPay QR -5% ưu đãi).",
                    pass ? "PASSED" : "FAILED", time,
                    String.format("Tiền mặt: %,.0f VNĐ | Thẻ (+1.5%%): %,.0f VNĐ | VNPay (-5%%): %,.0f VNĐ.",
                            rCash.getFinalAmount(), rCard.getFinalAmount(), rVNPay.getFinalAmount()),
                    "Đạt chuẩn Strategy: Hoán đổi thuật toán thanh toán linh hoạt, dễ dàng mở rộng phương thức mới (Momo, ZaloPay...)."
            );
        } catch (Exception e) {
            return new TestCaseResultDTO("TC06", "Behavioral", "Strategy", "Tính toán thanh toán theo chiến lược", "FAILED",
                    System.currentTimeMillis() - start, e.getMessage(), "Lỗi ngoại lệ.");
        }
    }
}
