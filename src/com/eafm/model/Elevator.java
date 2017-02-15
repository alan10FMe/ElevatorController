package com.eafm.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * Created by alan on 2/13/17.
 */
public class Elevator {

    private enum ElevatorStatus {
        MOVING_UP, MOVING_DOWN, IDLE
    }

    private int totalFloors;
    private ArrayList<Floor> floors;
    private Floor actualFloor;
    private TreeSet<Floor> floorsUp;
    private TreeSet<Floor> floorsDown;
    private ElevatorStatus elevatorStatus;

    public Elevator(int totalFloors) {
        this.totalFloors = totalFloors;
        initializeFloors();
        initializeQueues();
        actualFloor = floors.get(0);
        elevatorStatus = ElevatorStatus.IDLE;
    }

    private void initializeFloors() {
        floors = new ArrayList<>();
        for (int i = 0; i < totalFloors; i++) {
            floors.add(new Floor(i));
        }
    }

    private void initializeQueues() {
        floorsUp = new TreeSet<>(upComparator);
        floorsDown = new TreeSet<>(downComparator);
    }

    public void requestStop(Floor stop) {
        ElevatorStatus nextDirection = null;
        if (stop.getLevel() == actualFloor.getLevel()) {
            return;
        } else if (stop.getLevel() > actualFloor.getLevel()) {
            nextDirection = ElevatorStatus.MOVING_UP;
            requestStopUp(stop);
        } else {
            nextDirection = ElevatorStatus.MOVING_DOWN;
            requestStopDown(stop);
        }
        if (elevatorStatus == ElevatorStatus.IDLE) {
            elevatorStatus = nextDirection;
        }
    }

    public Floor getNextStop() {
        switch (elevatorStatus) {
            case MOVING_UP:
                if (hasNextStopUp()) {
                    return getNextStopUp();
                } else if (hasNextStopDown()) {
                    elevatorStatus = ElevatorStatus.MOVING_DOWN;
                    return getNextStopDown();
                } else {
                    elevatorStatus = ElevatorStatus.IDLE;
                    return null;
                }
            case MOVING_DOWN:
                if (hasNextStopDown()) {
                    return getNextStopDown();
                } else if (hasNextStopUp()) {
                    elevatorStatus = ElevatorStatus.MOVING_UP;
                    return getNextStopUp();
                } else {
                    elevatorStatus = ElevatorStatus.IDLE;
                    return null;
                }
            default:
                return null;
        }
    }

    public boolean hasNextStop() {
        return hasNextStopDown() || hasNextStopUp();
    }

    public void requestStopUp(Floor floor) {
        floorsUp.add(floor);
    }

    public Floor getNextStopUp() {
        Floor nextStop = floorsUp.iterator().next();
        floorsUp.remove(nextStop);
        return nextStop;
    }

    public boolean hasNextStopUp() {
        return floorsUp.iterator().hasNext();
    }

    public void requestStopDown(Floor floor) {
        floorsDown.add(floor);
    }

    public Floor getNextStopDown() {
        Floor nextStop = floorsDown.iterator().next();
        floorsDown.remove(nextStop);
        return nextStop;
    }

    public boolean hasNextStopDown() {
        return floorsDown.iterator().hasNext();
    }

    public Floor getActualFloor() {
        return actualFloor;
    }

    public void setActualFloor(Floor actualFloor) {
        this.actualFloor = actualFloor;
    }

    public int getTotalFloors() {
        return floors.size();
    }

    private static Comparator upComparator = new Comparator<Floor>() {
        @Override
        public int compare(Floor floor1, Floor floor2) {
            return (int) (floor1.getLevel() - floor2.getLevel());
        }
    };

    private static Comparator downComparator = new Comparator<Floor>() {
        @Override
        public int compare(Floor floor1, Floor floor2) {
            return (int) (floor2.getLevel() - floor1.getLevel());
        }
    };
}
