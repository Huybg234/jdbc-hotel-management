package entity;

import java.io.Serializable;
import java.util.Scanner;

public class Room implements Serializable {
    private int id;
    private String roomType;
    private float rentRate;
    private int roomNumber;

    public final static String VIP_ROOM = "Phòng vip";
    public final static String SINGLE_ROOM = "Phòng đơn";
    public final static String DOUBLE_ROOM = "Phòng đôi";

    private static int AUTO_ID = 100;

    public Room() {
    }

    public Room(int id, String roomType, float rentRate, int roomNumber) {
        this.id = id;
        this.roomType = roomType;
        this.rentRate = rentRate;
        this.roomNumber = roomNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public float getRentRate() {
        return rentRate;
    }

    public void setRentRate(float rentRate) {
        this.rentRate = rentRate;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public static String getVipRoom() {
        return VIP_ROOM;
    }

    public static String getSingleRoom() {
        return SINGLE_ROOM;
    }

    public static String getDoubleRoom() {
        return DOUBLE_ROOM;
    }

    public void inputRoomInfo(){
        this.setId(Room.AUTO_ID);
        System.out.println("Nhập loại phòng: ");
        System.out.println("1.Phòng đơn");
        System.out.println("2.Phòng đôi");
        System.out.println("3.Phòng Vip");
        boolean check = true;
        do {
            int choice = new Scanner(System.in).nextInt();
            if (choice <= 0 || choice > 3) {
                System.out.print("Nhập số từ 1 đến 3! Nhập lại: ");
                check = false;
                continue;
            }
            switch (choice) {
                case 1:
                    this.setRoomType(Room.SINGLE_ROOM);
                    System.out.println("Phòng đơn");
                    check = true;
                    break;
                case 2:
                    this.setRoomType(Room.DOUBLE_ROOM);
                    System.out.println("Phòng đôi");
                    check = true;
                    break;
                case 3:
                    this.setRoomType(Room.VIP_ROOM);
                    System.out.println("Phòng Vip");
                    check = true;
                    break;
                default:
                    System.out.println("Nhập sai! Hãy nhập từ 1 đến 3!");
                    check = false;
                    break;
            }
        } while (!check);
        System.out.println("Nhập mức tiền thuê: ");
        this.rentRate = new Scanner(System.in).nextInt();
        System.out.println("Nhập số phòng: ");
        this.roomNumber = new Scanner(System.in).nextInt();
        Room.AUTO_ID++;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomType='" + roomType + '\'' +
                ", rentRate=" + rentRate +
                ", roomNumber=" + roomNumber +
                '}';
    }
}
