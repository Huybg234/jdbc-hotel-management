import entity.Customer;
import entity.Room;
import reponsitory.CustomerDAO;
import reponsitory.RoomChoiceDAO;
import reponsitory.RoomDAO;
import service.RoomChoice;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainRun {
    private static List<Customer> customers = new ArrayList<>();
    private static List<Room> rooms = new ArrayList<>();
    private static List<RoomChoice> roomChoices = new ArrayList<>();

    public static final CustomerDAO customerDAO = new CustomerDAO();
    public static final RoomDAO roomDAO = new RoomDAO();
    public static final RoomChoiceDAO roomChoiceDAO = new RoomChoiceDAO();
    public static void main(String[] args) {
        menu();
    }

    private static void menu() {
        do {
            int functionChoice = functionChoice();
            switch (functionChoice) {
                case 1:
                    createNewRoom();
                    break;
                case 2:
                    createNewCustomer();
                    break;
                case 3:
                    roomArrange();
                    break;
                case 4:
                case 5:
                case 6:
                    System.exit(0);
            }

        } while (true);
    }

    public static boolean isValidCustomerAndRoom() {
        return customers != null && rooms != null && customers.size() != 0 && rooms.size() != 0;
    }

    private static boolean roomArrange() {
        if (!isValidCustomerAndRoom()) {
            System.out.println("Cần nhập thông tin phòng và khách hàng trước khi sắp xếp phòng: ");
            return false;
        }
        boolean check = true;
        int roomRest;
        int day = 0;
        System.out.println("Nhập id khách hàng muốn sắp xếp: ");
        int customerId;
        Room room;
        Customer customer;
        do {
            try {
                customerId = new Scanner(System.in).nextInt();
                check = true;
            } catch (Exception e) {
                System.out.println("Không được nhập ký tự khác ngoài số! Nhập lại: ");
                check = false;
                continue;
            }
            customer = searchCustomer(customerId);
            int retry = 1;
            if (customer != null && customer.getId() == customerId) {
                System.out.println("Loại phòng khách hàng " + customer.getName() + " thuê là: ");
                System.out.println(customer.getRoomTypeRent());
                System.out.println("Số phòng loại " + customer.getRoomTypeRent() + " khách hàng muốn thuê là: ");
                System.out.println(customer.getRoomNumberRent());

                room = searchRoomType(customer.getRoomTypeRent());
                assert room != null;
                System.out.println("Số phòng " + room.getRoomType() + " hiện có:");
                System.out.println(room.getRoomNumber());
                if (room.getRoomNumber() >= customer.getRoomNumberRent()) {
                    roomRest = room.getRoomNumber() - customer.getRoomNumberRent();
                    room.setRoomNumber(roomRest);
                } else {
                    return suggestOtherRoom(customer, room, retry);
                }
                break;
            }
            System.out.print("Không có khách hàng nào có ID vừa nhập, vui lòng nhập lại: ");
        } while (true);
        System.out.println("Nhập số ngày thuê");
        do {
            try {
                day = new Scanner(System.in).nextInt();
                check = true;
            } catch (Exception e) {
                System.out.print("Không được nhập ký tự khác ngoài số! Nhập lại: ");
                check = false;
                continue;
            }
            if (day <= 0 ) {
                System.out.print("Số ngày thuê phải lớn hơn 0! Nhập lại: ");
                check = false;
            }
        } while (!check);
        System.out.println(customer);
        System.out.println(room);
        RoomChoice roomChoice = new RoomChoice(customer, room, customer.getRoomNumberRent(), day);
        roomChoices.add(roomChoice);
        roomChoiceDAO.insertNewRoomChoice(roomChoices);
        return true;
    }

    public static boolean suggestOtherRoom(Customer customer, Room room, int retry) {
        if (retry == 2) {
            System.out.println("Thue phong that bai");
            return false;
        }
        retry++;
        boolean check = true;
        System.out.println("Không đủ phòng khách hàng muốn thuê, khách hàng có lựa chọn: ");
        System.out.println("1. Giảm số phòng muốn thuê");
        System.out.println("2. Thuê loại phòng khác");
        do {
            int choice = 0;
            try {
                choice = new Scanner(System.in).nextInt();
            } catch (Exception e) {
                System.out.print("Không được nhập ký tự khác ngoài số! Nhập lại: ");
                check = false;
                continue;
            }
            if (choice <= 0 || choice > 2) {
                System.out.print("Chọn 1 hoặc 2! Nhập lại: ");
                check = false;
                continue;
            }
            switch (choice) {
                case 1:
                    if (room.getRoomNumber() == 0) {
                        System.out.println("Loại phòng này đã hết, chọn loại khác");
                        return reduceRoomType(customer, room, retry);
                    } else {
                        reduceRoomNumber(customer, room);
                        return true;
                    }
                case 2:
                    return reduceRoomType(customer, room, retry);
                default:
                    System.out.println("Nhập sai! Hãy chọn 1 hoặc 2!");
                    check = false;
                    break;
            }
        } while (!check);
        return false;
    }

    public static void reduceRoomNumber(Customer customer, Room room){
        int reselect = 0;
        boolean check = true;
        do {
            try {
                System.out.println("Nhập số lượng phòng chọn lại: ");
                reselect = new Scanner(System.in).nextInt();
            } catch (Exception e) {
                System.out.print("Không được nhập ký tự khác ngoài số! Nhập lại: ");
                check = false;
                continue;
            }
            if (reselect <= 0 || reselect > room.getRoomNumber()) {
                System.out.println("Số phòng lớn hơn 0 va nhỏ hơn tổng số phòng hiện có! Nhập lại");
                check = false;
            } else {
                int roomRest = room.getRoomNumber() - reselect;
                room.setRoomNumber(roomRest);
                customer.setRoomNumberRent(reselect);
            }
        } while (!check);
    }

    private static boolean reduceRoomType(Customer customer, Room room, int retry){
        System.out.println("Chọn loại phòng: ");
        System.out.println(room.getRoomType().equals(Room.SINGLE_ROOM) ? "" : "1.Phòng đơn");
        System.out.println(room.getRoomType().equals(Room.DOUBLE_ROOM) ? "" : "2.Phòng đôi");
        System.out.println(room.getRoomType().equals(Room.VIP_ROOM) ? "" : "3.Phòng Vip");
        boolean check = true;
        do {
            int choiceRoomType = new Scanner(System.in).nextInt();
            if (choiceRoomType <= 0 || choiceRoomType > 3) {
                System.out.print("Nhập số từ 1 đến 3! Nhập lại: ");
                check = false;
                continue;
            }
            switch (choiceRoomType) {
                case 1:
                    customer.setRoomTypeRent(Room.SINGLE_ROOM);
                    check = true;
                    break;
                case 2:
                    customer.setRoomTypeRent(Room.DOUBLE_ROOM);
                    check = true;
                    break;
                case 3:
                    customer.setRoomTypeRent(Room.VIP_ROOM);
                    check = true;
                    break;
                default:
                    System.out.println("Nhập sai! Hãy nhập từ 1 đến 3!");
                    check = false;
                    break;
            }
        } while (!check);
        System.out.println("Nhập số phòng khách muốn thuê lại: ");
        do {
            int reChoiceRoomNumber;
            try {
                reChoiceRoomNumber = new Scanner(System.in).nextInt();
            } catch (Exception e) {
                System.out.print("Không được nhập ký tự khác ngoài số! Nhập lại: ");
                check = false;
                continue;
            }
            if (reChoiceRoomNumber <= 0) {
                System.out.println("Số lượng phòng phải lớn hơn 0! Nhập lại:");
                check = false;
            }
        } while (!check);
        room = searchRoomType(customer.getRoomTypeRent());
        int roomRest = 0;
        assert room != null;
        System.out.println("Số phòng " + room.getRoomType() + " hiện có:");
        System.out.println(room.getRoomNumber());
        if (room.getRoomNumber() >= customer.getRoomNumberRent()) {
            roomRest = room.getRoomNumber() - customer.getRoomNumberRent();
            room.setRoomNumber(roomRest);
            return true;
        } else {
            return suggestOtherRoom(customer, room, retry);
        }
    }

    private static Room searchRoomType(String roomType) {
        for (Room room : rooms) {
            if (room.getRoomType().equals(roomType)) {
                return room;
            }
        }
        return null;
    }

    private static Customer searchCustomer(int customerId) {
        for (Customer customer : customers) {
            if (customer.getId() == customerId) {
                return customer;
            }
        }
        return null;
    }

    private static void createNewRoom() {
        System.out.println("Nhập số lượng phòng muốn thêm: ");
        int roomCount = 0;
        boolean check = true;
        do {
            try {
                roomCount = new Scanner(System.in).nextInt();
                check = true;
            } catch (Exception e) {
                System.out.println("Không được nhập ký tự khác ngoài chữ:");
                check = false;
                continue;
            }
            if (roomCount <= 0) {
                System.out.println("Số lượng phòng phải lớn hơn 0! Nhập lại: ");
                check = false;
            }
        } while (!check);
        for (int i = 0; i < roomCount; i++) {
            Room room = new Room();
            room.inputRoomInfo();
            rooms.add(room);
        }
        roomDAO.insertNewRoom(rooms);
    }

    private static void createNewCustomer() {
        System.out.println("Nhập số lượng khách hàng muốn thêm: ");
        int customerCount = 0;
        boolean check = true;
        do {
            try {
                customerCount = new Scanner(System.in).nextInt();
                check = true;
            } catch (Exception e) {
                System.out.println("Không được nhập ký tự khác ngoài chữ:");
                check = false;
                continue;
            }
            if (customerCount <= 0) {
                System.out.println("Số lượng khách hàng phải lớn hơn 0! Nhập lại: ");
                check = false;
            }
        } while (!check);
        for (int i = 0; i < customerCount; i++) {
            Customer customer = new Customer();
            customer.inputInfo();
            customers.add(customer);
        }
        customerDAO.insertNewCustomer(customers);
    }

    private static int functionChoice() {
        System.out.println("--------Quản lý phòng khách sạn--------");
        System.out.println("1.Nhập danh sách phòng");
        System.out.println("2.Nhập danh sách khách sạn");
        System.out.println("3.Sắp xếp phòng cho mỗi khách hàng");
        System.out.println("4.Sắp xếp danh sách xếp phòng");
        System.out.println("5.Tính toán hóa đơn cho mỗi khách hàng");
        System.out.println("6.Thoát");
        int functionChoice;
        do {
            functionChoice = new Scanner(System.in).nextInt();
            if (functionChoice >= 1 && functionChoice <= 6) {
                break;
            }
            System.out.print("Chức năng chọn không hợp lệ, vui lòng chọn lại: ");
        } while (true);
        return functionChoice;
    }
}
