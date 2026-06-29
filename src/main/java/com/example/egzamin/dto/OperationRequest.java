package com.example.egzamin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/*
    ============================================================
    DTO: OperationRequest
    ============================================================

    To jest uniwersalny JSON do REST API.

    Użycie przykładowe:

    POST /api/operations

    {
      "mainObjectId": 1,
      "startDateTime": "2026-07-01T10:00",
      "endDateTime": "2026-07-03T10:00",
      "note": "Odbiór rano",
      "extraData": "A1,A2,A3 albo gabaryt M",
      "discount": true
    }

    Dzięki temu jeden endpoint REST można szybko przerobić na:
    - rezerwację auta,
    - wizytę u weterynarza,
    - nadanie paczki,
    - rezerwację biletu.
*/
public class OperationRequest {

    private Long mainObjectId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDateTime;

    private String note;

    private String extraData;

    private boolean discount;

    public Long getMainObjectId() {
        return mainObjectId;
    }

    public void setMainObjectId(Long mainObjectId) {
        this.mainObjectId = mainObjectId;
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

    public boolean isDiscount() {
        return discount;
    }

    public void setDiscount(boolean discount) {
        this.discount = discount;
    }

    public ReservationForm toReservationForm() {
        ReservationForm form = new ReservationForm();

        form.setMainObjectId(mainObjectId);
        form.setStartDateTime(startDateTime);
        form.setEndDateTime(endDateTime);
        form.setNote(note);
        form.setExtraData(extraData);
        form.setDiscount(discount);

        return form;
    }
}
