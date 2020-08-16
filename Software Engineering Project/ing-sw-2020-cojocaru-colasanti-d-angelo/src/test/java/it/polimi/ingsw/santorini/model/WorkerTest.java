package it.polimi.ingsw.santorini.model;

import it.polimi.ingsw.santorini.view.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WorkerTest {
    Worker worker;
    PlayersHandler playersHandler;
    ArrayList<Worker> workers;
    static List<Color> workerTypes;

    @BeforeAll
    void init() {
        workerTypes = Arrays.asList(Color.values());
        playersHandler = new PlayersHandler();
        for (int i = 1; i <= 4; i++)
            playersHandler.addPlayer(new Player (i, "Pippo" + i));
    }

    @RepeatedTest(3)
    void getTypeTest(RepetitionInfo repetitionInfo) {
        Color testedWorkerType = workerTypes.get(repetitionInfo.getCurrentRepetition() - 1);
        worker = new Worker(testedWorkerType.getName(), Gender.MALE);
        assertEquals(testedWorkerType.getName(), worker.getColor(), "Worker getType() returned a different intValueOf than the initialized one");
    }

    @RepeatedTest(4)
    void OccupantIdTest(RepetitionInfo repetitionInfo){
        Player currentPlayer = PlayersHandler.getPlayerById(repetitionInfo.getCurrentRepetition());
        workers = new ArrayList<>(2);
        for(int i = 0; i < 2; i++)
            workers.add(new Worker(Color.YELLOW.getName(), Gender.FEMALE));

        currentPlayer.setWorkers(workers);
        for(Worker worker : workers)
            worker.setPlayerId(currentPlayer.getId());
        assertAll (
            () -> assertEquals(repetitionInfo.getCurrentRepetition(), workers.get(0).occupantId(), "Worker should return the correct occupantId"),
            () -> assertEquals(repetitionInfo.getCurrentRepetition(), workers.get(1).occupantId(), "Worker should return the correct occupantId")
        );
    }
}