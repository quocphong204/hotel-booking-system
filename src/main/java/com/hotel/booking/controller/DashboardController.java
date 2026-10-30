package com.hotel.booking.controller;

import com.hotel.booking.dto.BookingFormDTO;
import com.hotel.booking.dto.CostPreviewDTO;
import com.hotel.booking.entity.BookingEntity;
import com.hotel.booking.entity.InvoiceEntity;
import com.hotel.booking.entity.RoomEntity;
import com.hotel.booking.entity.RoomStatus;
import com.hotel.booking.patterns.creational.singleton.AuditLogEntry;
import com.hotel.booking.patterns.structural.facade.HotelManagementFacade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    private final HotelManagementFacade facade;

    public DashboardController(HotelManagementFacade facade) {
        this.facade = facade;
    }

    @GetMapping({"/", "/dashboard"})
    public String viewDashboard(Model model) {
        List<RoomEntity> rooms = facade.getAllRooms();

        // Count stats
        long totalRooms = rooms.size();
        long availableCount = rooms.stream().filter(r -> r.getStatus() == RoomStatus.AVAILABLE).count();
        long bookedCount = rooms.stream().filter(r -> r.getStatus() == RoomStatus.BOOKED).count();
        long occupiedCount = rooms.stream().filter(r -> r.getStatus() == RoomStatus.OCCUPIED).count();
        long maintenanceCount = rooms.stream().filter(r -> r.getStatus() == RoomStatus.MAINTENANCE).count();

        // Find active booking for each room
        Map<Long, BookingEntity> roomBookingMap = new HashMap<>();
        for (RoomEntity r : rooms) {
            facade.getActiveBookingForRoom(r.getId()).ifPresent(b -> roomBookingMap.put(r.getId(), b));
        }

        List<AuditLogEntry> auditLogs = facade.getAuditLogs();

        model.addAttribute("rooms", rooms);
        model.addAttribute("roomBookingMap", roomBookingMap);
        model.addAttribute("totalRooms", totalRooms);
        model.addAttribute("availableCount", availableCount);
        model.addAttribute("bookedCount", bookedCount);
        model.addAttribute("occupiedCount", occupiedCount);
        model.addAttribute("maintenanceCount", maintenanceCount);
        model.addAttribute("auditLogs", auditLogs);
        model.addAttribute("bookingForm", new BookingFormDTO());

        return "dashboard";
    }

    @PostMapping("/rooms/book")
    public String bookRoom(@ModelAttribute BookingFormDTO bookingForm, RedirectAttributes redirectAttributes) {
        try {
            BookingEntity booking = facade.bookRoomWithBuilder(bookingForm);
            redirectAttributes.addFlashAttribute("successMessage",
                    String.format("Đặt phòng thành công! [Mã đơn: %s] cho khách hàng %s (Phòng %s). Đã kích hoạt Builder Pattern & State Pattern.",
                            booking.getBookingCode(), booking.getCustomerName(), booking.getRoom().getRoomNumber()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/rooms/{id}/state")
    public String changeRoomState(@PathVariable Long id,
                                  @RequestParam String action,
                                  RedirectAttributes redirectAttributes) {
        try {
            RoomEntity room = facade.changeRoomState(id, action);
            redirectAttributes.addFlashAttribute("successMessage",
                    String.format("Thao tác '%s' thành công cho phòng %s. Trạng thái hiện tại: [%s] (State Pattern).",
                            action, room.getRoomNumber(), room.getStatusDisplayName()));
        } catch (IllegalStateException e) {
            // Invalid state transition alert
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/checkout/{bookingId}")
    public String viewCheckoutPage(@PathVariable Long bookingId, Model model, RedirectAttributes redirectAttributes) {
        try {
            CostPreviewDTO preview = facade.previewCheckoutCost(bookingId);
            model.addAttribute("preview", preview);
            model.addAttribute("auditLogs", facade.getAuditLogs());
            return "checkout";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/dashboard";
        }
    }

    @PostMapping("/checkout")
    public String processCheckout(@RequestParam Long bookingId,
                                  @RequestParam String paymentMethod,
                                  RedirectAttributes redirectAttributes) {
        try {
            InvoiceEntity invoice = facade.processCompleteCheckout(bookingId, paymentMethod);
            redirectAttributes.addFlashAttribute("successMessage",
                    String.format("Thanh toán & Check-out thành công! [Mã hóa đơn: %s] - Phương thức: %s - Tổng tiền: %,.0f VNĐ. (Decorator + Strategy + State Pattern). Phòng đã chuyển sang trạng thái Bảo trì / Dọn dẹp.",
                            invoice.getInvoiceCode(), invoice.getPaymentMethod(), invoice.getFinalAmount()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi thanh toán: " + e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/api/logs")
    @ResponseBody
    public List<AuditLogEntry> getLiveLogs() {
        return facade.getAuditLogs();
    }

    @PostMapping("/api/logs/clear")
    public String clearLogs(RedirectAttributes redirectAttributes) {
        facade.clearAuditLogs();
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa toàn bộ nhật ký giám sát Design Pattern!");
        return "redirect:/dashboard";
    }
}
