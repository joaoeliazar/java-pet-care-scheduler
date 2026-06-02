package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Appointment {
    private String type;
    private LocalDateTime dateTime;
    private String notes;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

    public Appointment(String type, LocalDateTime dateTime, String notes) {
        this.type = type;
        this.dateTime = dateTime;
        this.notes = notes;
    }

    // Getters
    public String getType() { return type; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getNotes() { return notes; }

    // Setters
    public void setType(String type) { this.type = type; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return "Appointment: ( " +
                "Type: " + type +
                " | Date: " + dateTime.format(FORMATTER) +
                " | Notes: " + (notes == null || notes.isEmpty() ? "none" : notes) +
                " )";
    }



}
