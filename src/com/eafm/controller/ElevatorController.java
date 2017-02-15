package com.eafm.controller;

import com.eafm.model.Elevator;
import com.eafm.model.Floor;

/**
 * Created by alan on 2/14/17.
 */
public class ElevatorController extends Thread {

    private Elevator elevator;
    private boolean turnOffElevator;

    public ElevatorController(int totalFloors) {
        elevator = new Elevator(totalFloors);
    }

    public void turnOnElevator() {
        turnOffElevator = false;
        start();
    }

    public void run() {
        while (true) {
            moveToNextStop(elevator.getNextStop());
            if (!elevator.hasNextStop() && turnOffElevator) {
                break;
            }
        }
    }

    private void moveToNextStop(Floor nextStop) {
        if (nextStop != null) {
            System.out.println("Going to floor: " + nextStop.getLevel());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            elevator.setActualFloor(nextStop);
            System.out.println("Arrived to floor: " + elevator.getActualFloor().getLevel());
        }

    }

    public void addStop(int floor) {
        if (floor > elevator.getTotalFloors()) {
            throw new IllegalArgumentException("Floor does not exists");
        }
        elevator.requestStop(new Floor(floor));
    }

    public void turnOffElevator() {
        turnOffElevator = true;
    }

    public int getActualFloor() {
        return elevator.getActualFloor().getLevel();
    }

}
