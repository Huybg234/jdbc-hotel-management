package service;

import entity.Customer;
import entity.Room;

public class RoomChoice {
    private Customer customer;
    private Room room;
    private int roomQuantity;
    private int dayNumber;

    public RoomChoice() {
    }

    public RoomChoice(Customer customer, Room room, int roomQuantity, int dayNumber) {
        this.customer = customer;
        this.room = room;
        this.roomQuantity = roomQuantity;
        this.dayNumber = dayNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public int getRoomQuantity() {
        return roomQuantity;
    }

    public void setRoomQuantity(int roomQuantity) {
        this.roomQuantity = roomQuantity;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    @Override
    public String toString() {
        return "RoomChoice{" +
                "customer=" + customer +
                ", room=" + room +
                ", roomQuantity=" + roomQuantity +
                ", dayNumber=" + dayNumber +
                '}';
    }
}
