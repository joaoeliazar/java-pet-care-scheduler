// app/PetCareScheduler.java
package app;

import model.Appointment;
import model.Pet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.io.*;
import java.util.*;

public class PetCareScheduler {

    private final Scanner scanner = new Scanner(System.in);
    private final HashMap<String, Pet> pets = new HashMap<>();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
    private static final List<String> VALID_TYPES = List.of("vet visit", "vaccination", "grooming");


    // ─────────────────────────────────────────
    //  ENTRY POINT
    // ─────────────────────────────────────────

    /**
     * Starts the application. Loads existing data from files, then
     * runs the main menu loop until the user chooses to exit.
     */
    public void run() {
        loadData();
        int choice = -1;

        System.out.println("Welcome to the PetCareScheduler application!");

        do {
            printMenu();
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number between 1 and 6.");
                continue;
            }

            switch (choice) {
                case 1 -> registerPet();
                case 2 -> scheduleAppointment();
                case 3 -> saveData();
                case 4 -> displayRecords();
                case 5 -> generateReports();
                case 6 -> System.out.println("Thank you for using the Pet Care Scheduler Application, goodbye!");
                default -> System.out.println("Invalid option. Try again.");
            }

        } while (choice != 6);

        scanner.close();
    }

    /**
     * Prints the main menu options to the console.
     */
    private void printMenu() {
        System.out.print("\n" +
                "1. Register a pet\n" +
                "2. Schedule an appointment\n" +
                "3. Save data\n" +
                "4. Display records\n" +
                "5. Generate reports\n" +
                "6. Exit\n" +
                "Choose: ");
    }


    // ─────────────────────────────────────────
    //  REGISTER PET
    // ─────────────────────────────────────────

    /**
     * Prompts the user to enter all details for a new pet and adds
     * it to the pets map. Validates that the ID is not already in use
     * and that age is a non-negative integer.
     */
    private void registerPet() {
        System.out.println("--- Register Pet ---\n");

        System.out.print("Pet ID: ");
        String petId = scanner.nextLine().trim();
        if (pets.containsKey(petId)) {
            System.out.println("Error: Pet ID is already in use!");
            return;
        }

        System.out.print("Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Species/Breed: ");
        String species = scanner.nextLine().trim();

        int age = -1;
        while (age < 0) {
            System.out.print("Age: ");
            try {
                age = Integer.parseInt(scanner.nextLine().trim());
                if (age < 0) System.out.println("Age cannot be negative.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid age. Enter a whole number.");
            }
        }

        System.out.print("Owner name: ");
        String ownerName = scanner.nextLine().trim();

        System.out.print("Contact info: ");
        String contactInfo = scanner.nextLine().trim();

        Pet pet = new Pet(petId, name, species, age, ownerName, contactInfo);
        pets.put(petId, pet);
        System.out.println("Pet registered successfully: " + pet);
    }


    // ─────────────────────────────────────────
    //  SCHEDULE APPOINTMENT
    // ─────────────────────────────────────────

    /**
     * Prompts the user to schedule an appointment for an existing pet.
     * Validates that the pet ID exists, the appointment type is valid,
     * and the date/time is in the future.
     */
    private void scheduleAppointment() {
        System.out.println("\n--- Schedule Appointment ---\n");

        System.out.print("Pet ID: ");
        String petId = scanner.nextLine().trim();
        if (!pets.containsKey(petId)) {
            System.out.println("Error: Pet ID not found!");
            return;
        }

        System.out.print("Appointment type - (vet visit, vaccination or grooming): ");
        String apptType = scanner.nextLine().trim().toLowerCase();
        if (!VALID_TYPES.contains(apptType)) {
            System.out.println("Invalid appointment type, choose one from " + VALID_TYPES);
            return;
        }

        LocalDateTime dateTime = null;
        while (dateTime == null) {
            System.out.print("Date and time (MM/dd/yyyy HH:mm): ");
            try {
                dateTime = LocalDateTime.parse(scanner.nextLine().trim(), FORMATTER);
                if (dateTime.isBefore(LocalDateTime.now())) {
                    System.out.println("Error: Date must be in the future.");
                    dateTime = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format. Use MM/dd/yyyy HH:mm");
            }
        }

        System.out.print("Notes (optional, press Enter to skip): ");
        String notes = scanner.nextLine().trim();

        Appointment appointment = new Appointment(apptType, dateTime, notes);
        pets.get(petId).addAppointment(appointment);
        System.out.println("Appointment scheduled successfully: " + appointment);
    }


    // ─────────────────────────────────────────
    //  DISPLAY RECORDS
    // ─────────────────────────────────────────

    /**
     * Shows a sub-menu and delegates to the appropriate display method
     * based on the user's choice.
     */
    private void displayRecords() {
        System.out.println("\n--- Display Records ---\n");
        System.out.println("1. All registered pets");
        System.out.println("2. All appointments for a specific pet");
        System.out.println("3. Upcoming appointments for all pets");
        System.out.println("4. Past appointment history for all pets");
        System.out.print("Choose: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            switch (choice) {
                case 1 -> displayAllPets();
                case 2 -> displayPetAppointments();
                case 3 -> displayUpcomingAppointments();
                case 4 -> displayPastAppointments();
                default -> System.out.println("Invalid option.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    /**
     * Prints all registered pets sorted alphabetically by pet ID.
     */
    private void displayAllPets() {
        if (pets.isEmpty()) {
            System.out.println("No pets registered.");
            return;
        }
        pets.values().stream()
                .sorted(Comparator.comparing(Pet::getPetId))
                .forEach(System.out::println);
    }

    /**
     * Prompts for a pet ID and prints all appointments for that pet,
     * sorted chronologically from nearest to furthest.
     */
    private void displayPetAppointments() {
        System.out.print("Enter Pet ID: ");
        String id = scanner.nextLine().trim();
        Pet pet = pets.get(id);
        if (pet == null) {
            System.out.println("Pet not found.");
            return;
        }
        if (pet.getAppointments().isEmpty()) {
            System.out.println("No appointments for " + pet.getName());
            return;
        }
        pet.getAppointments().stream()
                .sorted(Comparator.comparing(Appointment::getDateTime))
                .forEach(System.out::println);
    }

    /**
     * Prints all future appointments across all pets,
     * sorted chronologically from nearest to furthest.
     */
    private void displayUpcomingAppointments() {
        LocalDateTime now = LocalDateTime.now();
        boolean found = false;

        List<Map.Entry<LocalDateTime, String>> entries = new ArrayList<>();
        for (Pet pet : pets.values()) {
            for (Appointment a : pet.getAppointments()) {
                if (a.getDateTime().isAfter(now)) {
                    entries.add(Map.entry(a.getDateTime(), pet.getName() + ": " + a));
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No upcoming appointments.");
            return;
        }

        entries.stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .forEach(System.out::println);
    }

    /**
     * Prints all past appointments across all pets,
     * sorted chronologically from most recent to oldest.
     */
    private void displayPastAppointments() {
        LocalDateTime now = LocalDateTime.now();
        boolean found = false;

        List<Map.Entry<LocalDateTime, String>> entries = new ArrayList<>();
        for (Pet pet : pets.values()) {
            for (Appointment a : pet.getAppointments()) {
                if (a.getDateTime().isBefore(now)) {
                    entries.add(Map.entry(a.getDateTime(), pet.getName() + ": " + a));
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No past appointments.");
            return;
        }

        entries.stream()
                .sorted(Comparator.comparing(Map.Entry<LocalDateTime, String>::getKey).reversed())
                .map(Map.Entry::getValue)
                .forEach(System.out::println);
    }


    // ─────────────────────────────────────────
    //  GENERATE REPORTS
    // ─────────────────────────────────────────

    /**
     * Shows a sub-menu and delegates to the appropriate report method
     * based on the user's choice.
     */
    private void generateReports() {
        System.out.println("\n--- Reports ---\n");
        System.out.println("1. Pets with appointments in the next 7 days");
        System.out.println("2. Pets overdue for a vet visit (6+ months)");
        System.out.print("Choose: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            switch (choice) {
                case 1 -> reportUpcomingWeek();
                case 2 -> reportOverdueVetVisit();
                default -> System.out.println("Invalid option.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    /**
     * Prints all appointments scheduled within the next 7 days,
     * sorted chronologically from nearest to furthest.
     */
    private void reportUpcomingWeek() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekLater = now.plusDays(7);
        System.out.println("\nPets with appointments in the next 7 days:");

        List<Map.Entry<LocalDateTime, String>> entries = new ArrayList<>();
        for (Pet pet : pets.values()) {
            for (Appointment a : pet.getAppointments()) {
                if (a.getDateTime().isAfter(now) && a.getDateTime().isBefore(weekLater)) {
                    entries.add(Map.entry(a.getDateTime(), pet.getName() + ": " + a));
                }
            }
        }

        if (entries.isEmpty()) {
            System.out.println("None found.");
        } else {
            entries.stream()
                    .sorted(Comparator.comparing(Map.Entry::getKey))
                    .map(Map.Entry::getValue)
                    .forEach(System.out::println);
        }
    }

    /**
     * Prints all pets that have not had a vet visit in the last 6 months,
     * or have never had one. Sorted by pet ID.
     */
    private void reportOverdueVetVisit() {
        LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);
        System.out.println("\nPets overdue for a vet visit:");

        List<Pet> overdue = pets.values().stream()
                .filter(pet -> pet.getAppointments().stream()
                        .noneMatch(a -> a.getType().equals("vet visit")
                                && a.getDateTime().isAfter(sixMonthsAgo)))
                .sorted(Comparator.comparing(Pet::getPetId))
                .toList();

        if (overdue.isEmpty()) {
            System.out.println("All pets are up to date.");
        } else {
            overdue.forEach(pet ->
                    System.out.println(pet.getName() + " (ID: " + pet.getPetId() + ")"));
        }
    }


    // ─────────────────────────────────────────
    //  FILE I/O
    // ─────────────────────────────────────────

    /**
     * Saves all pets and their appointments to separate files in the data/ folder.
     * Creates the folder if it does not exist.
     * Each line uses "|" as a delimiter for easy parsing on load.
     */
    private void saveData() {
        try {
            new File("data").mkdirs();

            try (BufferedWriter pw = new BufferedWriter(new FileWriter("data/pets.txt"));
                 BufferedWriter aw = new BufferedWriter(new FileWriter("data/appointments.txt"))) {

                for (Pet pet : pets.values()) {
                    pw.write(pet.getPetId() + "|" + pet.getName() + "|" + pet.getSpecies()
                            + "|" + pet.getAge() + "|" + pet.getOwnerName()
                            + "|" + pet.getContactInfo() + "|" + pet.getRegistrationDate());
                    pw.newLine();

                    for (Appointment a : pet.getAppointments()) {
                        aw.write(pet.getPetId() + "|" + a.getType() + "|"
                                + a.getDateTime().format(FORMATTER) + "|"
                                + (a.getNotes() == null ? "" : a.getNotes()));
                        aw.newLine();
                    }
                }
                System.out.println("Data saved successfully.");
            }

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    /**
     * Loads pet and appointment data from files on startup.
     * If no files exist (first run), the method returns silently.
     * Pets are loaded first, then appointments are linked to their
     * respective pets using the petId stored at the start of each line.
     */
    private void loadData() {
        File petsFile = new File("data/pets.txt");
        File apptFile = new File("data/appointments.txt");

        if (!petsFile.exists()) return;

        try (BufferedReader pr = new BufferedReader(new FileReader(petsFile))) {
            String line;
            while ((line = pr.readLine()) != null) {
                String[] p = line.split("\\|", -1);
                Pet pet = new Pet(p[0], p[1], p[2], Integer.parseInt(p[3]), p[4], p[5]);
                pet.setRegistrationDate(LocalDate.parse(p[6]));
                pets.put(pet.getPetId(), pet);
            }
        } catch (IOException e) {
            System.out.println("Error loading pets: " + e.getMessage());
        }

        if (!apptFile.exists()) return;

        try (BufferedReader ar = new BufferedReader(new FileReader(apptFile))) {
            String line;
            while ((line = ar.readLine()) != null) {
                String[] a = line.split("\\|", -1);
                Pet pet = pets.get(a[0]);
                if (pet != null) {
                    Appointment appt = new Appointment(
                            a[1],
                            LocalDateTime.parse(a[2], FORMATTER),
                            a[3]
                    );
                    pet.addAppointment(appt);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading appointments: " + e.getMessage());
        }

        System.out.println("Data loaded: " + pets.size() + " pet(s) found.");
    }
}