package com.example.egzamin.dto;

import com.example.egzamin.entity.Reservation;

import java.time.LocalDateTime;

/*
    ============================================================
    DTO: OperationResponse
    ============================================================

    To jest prosta odpowiedź JSON po utworzeniu operacji.

    Nie zwracamy całej encji Reservation bezpośrednio, bo encje mają relacje
    do User i MainObject. W REST API prościej i bezpieczniej zwrócić tylko
    te pola, które naprawdę są potrzebne do pokazania wyniku.
*/
public class OperationResponse {

    private Long reservationId;
    private String username;
    private String mainObjectName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String status;
    private String note;
    private String extraData;
    private Double totalPrice;

    public static OperationResponse fromReservation(Reservation reservation) {
        OperationResponse response = new OperationResponse();

        response.setReservationId(reservation.getId());
        response.setStartDateTime(reservation.getStartDateTime());
        response.setEndDateTime(reservation.getEndDateTime());
        response.setStatus(reservation.getStatus());
        response.setNote(reservation.getNote());
        response.setExtraData(reservation.getExtraData());
        response.setTotalPrice(reservation.getTotalPrice());

        if (reservation.getUser() != null) {
            response.setUsername(reservation.getUser().getUsername());
        }

        if (reservation.getMainObject() != null) {
            response.setMainObjectName(reservation.getMainObject().getName());
        }

        return response;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMainObjectName() {
        return mainObjectName;
    }

    public void setMainObjectName(String mainObjectName) {
        this.mainObjectName = mainObjectName;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
