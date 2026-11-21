# Gym CRM

A Spring Core module that manages gym trainees, trainers, and training sessions using a Map-based in-memory storage.

## Project Structure

- `src/main/java/com/epam/gym/model/` - Domain objects: `User`, `Trainee`, `Trainer`, `Training`, and `TrainingType`.
- `src/main/java/com/epam/gym/dao/` - DAO interfaces for entity CRUD and username lookup operations.
- `src/main/java/com/epam/gym/dao/memory/` - In-memory DAO implementations backed by Map storage.
- `src/main/java/com/epam/gym/storage/` - Shared `InMemoryStorage` implementation and separate data namespaces.
- `src/main/java/com/epam/gym/service/` - Business operations for trainees, trainers, trainings, and profile credentials.
- `src/main/java/com/epam/gym/facade/` - `GymFacade`, providing the main entry point for application operations.
- `src/main/java/com/epam/gym/config/` - Spring Java configuration and startup seed-data loading.
- `src/main/java/com/epam/gym/exception/` - Application-specific exceptions.
- `src/main/resources/` - Seed data, external properties, and Logback configuration.
- `src/test/java/com/epam/gym/` - Unit tests for services, DAOs, and Spring context startup.

## Models

### User

Base profile shared by trainees and trainers.

- `id` - Unique entity identifier
- `firstName` - User's first name
- `lastName` - User's last name
- `username` - Generated as `FirstName.LastName`, with a numeric suffix for duplicates
- `password` - Random 10-character password generated at profile creation
- `active` - Indicates whether the profile is active

### Trainee

Extends `User` and represents a gym member.

- `dateOfBirth` - Trainee's date of birth
- `address` - Contact or home address

Trainees can be created, updated, selected, and deleted.

### Trainer

Extends `User` and represents a gym instructor.

- `specialization` - The trainer's `TrainingType`

Trainers can be created, updated, and selected.

### Training

Represents a scheduled training session.

- `id` - Unique training identifier
- `traineeId` - Identifier of the participating trainee
- `trainerId` - Identifier of the assigned trainer
- `trainingName` - Name of the training session
- `trainingType` - Category of the training
- `trainingDate` - Date and time of the session
- `trainingDuration` - Duration in minutes

Trainings can be created and selected.

### TrainingType

Describes a category of training.

- `name` - Training category name, such as Fitness or Yoga

## Relationships

- A trainee and a trainer are both types of `User`.
- A training links one trainee and one trainer using `traineeId` and `trainerId`.
- A trainer has one specialization represented by `TrainingType`.
- Each domain entity is stored in its own namespace in the in-memory Map storage.

## Running

The application starts through `com.epam.gym.Main`:

```bash
mvn clean test
mvn exec:java -Dexec.mainClass="com.epam.gym.Main"
```
