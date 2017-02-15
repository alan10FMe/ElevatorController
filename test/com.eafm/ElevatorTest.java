package com.eafm;

import com.eafm.controller.ElevatorController;
import com.eafm.model.Elevator;
import com.eafm.model.Floor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

/**
 * Created by alan on 2/14/17.
 */
public class ElevatorTest {

    private final int TOTAL_FLOORS = 100;
    private Elevator elevator;
    private ElevatorController elevatorController;

    //Elevator should be able to work for any number of floors (specified when the elevator is first installed)
    @Before
    public void initializeElevator() {
        elevator = new Elevator(TOTAL_FLOORS);
        elevatorController = new ElevatorController(TOTAL_FLOORS);
    }

    //Elevator should be able to work for any number of floors (specified when the elevator is first installed)
    //Elevator should start on the 1st floor
    @Test
    public void generateElevator() {
        System.out.println("Elevator, number of floors: " + elevator.getTotalFloors());
        Assert.assertEquals(elevator.getTotalFloors(), TOTAL_FLOORS);
        System.out.println("Actual floor: " + elevator.getActualFloor().getLevel());
        Assert.assertEquals(elevator.getActualFloor().getLevel(), 0);
    }


    //Test to validate queue to put all request in asc order
    @Test
    public void testQueueUp() {
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            elevator.requestStopUp(new Floor(random.nextInt(TOTAL_FLOORS)));
        }
        Floor tempFloor = null;
        while (elevator.hasNextStopUp()) {
            Floor nextStopUp = elevator.getNextStopUp();
            System.out.println(nextStopUp.getLevel());
            if (tempFloor != null) {
                Assert.assertTrue(tempFloor.getLevel() < nextStopUp.getLevel());
            }
            tempFloor = nextStopUp;
        }
    }


    //Test to validate queue to put all request in desc order
    @Test
    public void testQueueDown() {
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            elevator.requestStopDown(new Floor(random.nextInt(TOTAL_FLOORS)));
        }
        Floor tempFloor = null;
        while (elevator.hasNextStopDown()) {
            Floor nextStop = elevator.getNextStopDown();
            System.out.println(nextStop.getLevel());
            if (tempFloor != null) {
                Assert.assertTrue(tempFloor.getLevel() > nextStop.getLevel());
            }
            tempFloor = nextStop;
        }
    }

    //Test when somebody request a floor that does not exists
    @Test(expected = IllegalArgumentException.class)
    public void testFloorNotValid() {
        elevatorController.addStop(150);
    }


    //Test elevator going up
    //having multiple requests
    @Test
    public void testElevatorGoingUp() throws InterruptedException {
        elevatorController.turnOnElevator();

        elevatorController.addStop(4);
        elevatorController.addStop(1);
        elevatorController.addStop(5);
        elevatorController.addStop(3);

        //Enough time to get the last floor
        Thread.sleep(6000);

        //The last floor should be 5
        Assert.assertEquals(5, elevatorController.getActualFloor());

        elevatorController.turnOffElevator();
        try {
            elevatorController.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Test elevator going down
    //having multiple requests
    @Test
    public void testElevatorGoingDown() throws InterruptedException {
        elevatorController.turnOnElevator();

        elevatorController.addStop(50);
        //Enough time to get the 50th floor
        Thread.sleep(3000);

        elevatorController.addStop(4);
        elevatorController.addStop(1);
        elevatorController.addStop(5);
        elevatorController.addStop(3);

        //Enough time to get the last floor
        Thread.sleep(6000);

        //The last floor should be 1
        Assert.assertEquals(1, elevatorController.getActualFloor());

        elevatorController.turnOffElevator();
        try {
            elevatorController.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Test elevator going up and down
    //having multiple requests
    @Test
    public void testElevatorGoingUpDown() throws InterruptedException {
        elevatorController.turnOnElevator();

        elevatorController.addStop(8);
        elevatorController.addStop(3);
        Thread.sleep(3000);

        Assert.assertEquals(8, elevatorController.getActualFloor());

        elevatorController.addStop(5);
        elevatorController.addStop(6);
        elevatorController.addStop(3);
        Thread.sleep(4000);

        Assert.assertEquals(3, elevatorController.getActualFloor());

        elevatorController.addStop(10);
        Thread.sleep(2000);
        elevatorController.addStop(8);
        Thread.sleep(2000);
        elevatorController.addStop(6);
        Thread.sleep(2000);
        elevatorController.addStop(1);
        Thread.sleep(2000);
        elevatorController.addStop(7);
        Thread.sleep(2000);

        Assert.assertEquals(7, elevatorController.getActualFloor());

        elevatorController.turnOffElevator();
        try {
            elevatorController.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Test for method to validate what floor the elevator is currently on
    @Test
    public void validateActualFloor() throws InterruptedException {
        elevatorController.turnOnElevator();

        //The initial floor have to be 0
        Assert.assertEquals(0, elevatorController.getActualFloor());

        elevatorController.addStop(4);
        //Enough time to get the floor
        Thread.sleep(6000);

        //The actual floor have to be 4
        Assert.assertEquals(4, elevatorController.getActualFloor());

        elevatorController.turnOffElevator();
        try {
            elevatorController.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
