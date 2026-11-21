package com.epam.gym.facade;

import com.epam.gym.model.Trainee;
import com.epam.gym.model.Trainer;
import com.epam.gym.model.Training;
import com.epam.gym.service.TraineeService;
import com.epam.gym.service.TrainerService;
import com.epam.gym.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GymFacadeTest {
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainerService trainerService;
    @Mock
    private TrainingService trainingService;

    private GymFacade facade;

    @BeforeEach
    void setUp() {
        facade = new GymFacade(traineeService, trainerService, trainingService);
    }

    @Test
    void delegatesTraineeOperations() {
        final var trainee = new Trainee();
        when(traineeService.create(trainee)).thenReturn(trainee);
        when(traineeService.update(trainee)).thenReturn(trainee);
        when(traineeService.findById(1L)).thenReturn(Optional.of(trainee));
        when(traineeService.findAll()).thenReturn(List.of(trainee));

        assertEquals(trainee, facade.createTrainee(trainee));
        assertEquals(trainee, facade.updateTrainee(trainee));
        facade.deleteTrainee(1L);
        assertEquals(Optional.of(trainee), facade.getTrainee(1L));
        assertEquals(List.of(trainee), facade.getAllTrainees());
        verify(traineeService).delete(1L);
    }

    @Test
    void delegatesTrainerOperations() {
        final var trainer = new Trainer();
        when(trainerService.create(trainer)).thenReturn(trainer);
        when(trainerService.update(trainer)).thenReturn(trainer);
        when(trainerService.findById(2L)).thenReturn(Optional.of(trainer));
        when(trainerService.findAll()).thenReturn(List.of(trainer));

        assertEquals(trainer, facade.createTrainer(trainer));
        assertEquals(trainer, facade.updateTrainer(trainer));
        assertEquals(Optional.of(trainer), facade.getTrainer(2L));
        assertEquals(List.of(trainer), facade.getAllTrainers());
    }

    @Test
    void delegatesTrainingOperations() {
        final var training = new Training();
        when(trainingService.create(training)).thenReturn(training);
        when(trainingService.findById(3L)).thenReturn(Optional.of(training));
        when(trainingService.findAll()).thenReturn(List.of(training));

        assertEquals(training, facade.createTraining(training));
        assertEquals(Optional.of(training), facade.getTraining(3L));
        assertEquals(List.of(training), facade.getAllTrainings());
    }
}
