package com.hotel.booking.repository;

import com.hotel.booking.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {

    Optional<InvoiceEntity> findByInvoiceCode(String invoiceCode);

    Optional<InvoiceEntity> findByBookingId(Long bookingId);

    List<InvoiceEntity> findAllByOrderByPaymentDateDesc();
}
