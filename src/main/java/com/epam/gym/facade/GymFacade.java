package com.epam.gym.facade;

import com.epam.gym.model.Trainee;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.Training;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.TrainingService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GymFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public GymFacade(final TraineeService traineeService,
                     final TrainerService trainerService,
                     final TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    public Trainee createTrainee(final Trainee trainee) {
        return traineeService.create(trainee);
    }

    public Trainee updateTrainee(final Trainee trainee) {
        return traineeService.update(trainee);
    }

    public void deleteTrainee(final Long id) {
        traineeService.delete(id);
    }

    public Optional<Trainee> getTrainee(final Long id) {
        return traineeService.findById(id);
    }

    public List<Trainee> getAllTrainees() {
        return traineeService.findAll();
    }

    public Trainer createTrainer(final Trainer trainer) {
        return trainerService.create(trainer);
    }

    public Trainer updateTrainer(final Trainer trainer) {
        return trainerService.update(trainer);
    }

    public Optional<Trainer> getTrainer(final Long id) {
        return trainerService.findById(id);
    }

    public List<Trainer> getAllTrainers() {
        return trainerService.findAll();
    }

    public Training createTraining(final Training training) {
        return trainingService.create(training);
    }

    public Optional<Training> getTraining(final Long id) {
        return trainingService.findById(id);
    }

    public List<Training> getAllTrainings() {
        return trainingService.findAll();
    }
}
