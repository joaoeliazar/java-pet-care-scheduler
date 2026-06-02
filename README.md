# Java Pet Care Scheduler

A console-based Java application for managing pet profiles and appointments, with file persistence and report generation.

---

## Features

- Register pets with unique IDs and full profile details
- Schedule appointments with type validation and future date enforcement
- Display all pets, upcoming appointments, and past history
- Generate reports for appointments in the next 7 days and overdue vet visits
- Persist all data to files so nothing is lost between sessions

---

## How to Run

### Compile

```bash
javac -d out src/Main.java src/app/PetCareScheduler.java src/model/Pet.java src/model/Appointment.java
```

### Run

```bash
java -cp out Main
```

---

## Menu Options

| Option | Description |
|--------|-------------|
| 1 | Register a new pet |
| 2 | Schedule an appointment |
| 3 | Save data to files |
| 4 | Display records |
| 5 | Generate reports |
| 6 | Exit |

---

## Project Structure

```
PetCareScheduler/
├── data/
│   ├── pets.txt
│   └── appointments.txt
├── src/
│   ├── app/
│   │   └── PetCareScheduler.java
│   ├── model/
│   │   ├── Appointment.java
│   │   └── Pet.java
│   └── Main.java
```

---

## Technologies

- Java 17+
- Java Date & Time API (`LocalDate`, `LocalDateTime`, `DateTimeFormatter`)
- Java I/O (`BufferedReader`, `BufferedWriter`, `FileReader`, `FileWriter`)
- Java Collections (`HashMap`, `ArrayList`, `List`)

---

## Concepts Practiced

- Object-Oriented Programming (encapsulation, class design)
- Collections and generics
- Exception handling (`NumberFormatException`, `DateTimeParseException`, `IOException`)
- File persistence with delimited text files
- Stream API with filtering, sorting, and mapping
- Menu-driven console application design
