package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pet {

    private String petId;
    private String name;
    private String species;
    private int age;
    private String ownerName;
    private String contactInfo;
    private LocalDate registrationDate;
    private List<Appointment> appointments;

    public Pet(String petId, String name, String species, int age, String ownerName, String contactInfo) {
        this.petId = petId;
        this.name = name;
        this.species = species;
        this.age = age;
        this.ownerName = ownerName;
        this.contactInfo = contactInfo;
        this.registrationDate = LocalDate.now();
        this.appointments = new ArrayList<>();
    }

    // Getters
    public String getPetId()           { return petId; }
    public String getName()            { return name; }
    public String getSpecies()         { return species; }
    public int getAge()                { return age; }
    public String getOwnerName()       { return ownerName; }
    public String getContactInfo()     { return contactInfo; }
    public LocalDate getRegistrationDate() { return registrationDate; }
    public List<Appointment> getAppointments() { return appointments; }

    // Setters
    public void setPetId(String petId)         { this.petId = petId; }
    public void setName(String name)           { this.name = name; }
    public void setSpecies(String species)     { this.species = species; }
    public void setAge(int age)                { this.age = age; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }

    // Add a single appointment
    public void addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
    }

    @Override
    public String toString() {
        return "Pet: ( " +
                "ID: " + petId +
                " | Name: " + name +
                " | Species/Breed: " + species +
                " | Age: " + age + " years" +
                " | Owner: " + ownerName +
                " | Contact: " + contactInfo +
                " | Registered on: " + registrationDate +
                " | Appointments: " + appointments.size() +
                " )";
    }
}