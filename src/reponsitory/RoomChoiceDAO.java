package reponsitory;

import constant.DatabaseConstant;
import entity.Customer;
import entity.Room;
import service.RoomChoice;
import util.CollectionUtil;
import util.DatabaseConnection;
import util.ObjectUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomChoiceDAO {
    private static final String ROOM_CHOICE_TABLE_NAME = "room_choice";

    private static final String CUSTOMER_ID = "customer_id";
    private static final String ROOM_ID = "room_id";
    private static final String ROOM_QUANTITY = "room_quantity";
    private static final String DAY_NUMBER = "day_number";

    private static final Connection connection;

    static {
        connection = DatabaseConnection.openConnection(DatabaseConstant.DRIVER_STRING, DatabaseConstant.URL, DatabaseConstant.USERNAME, DatabaseConstant.PASSWORD);
    }

    public List<RoomChoice> getRoomChoices() {
        List<RoomChoice> roomChoices = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT cm.id customer_id, cm.name customer_name, cm.address customer_address, cm.phone_number customer_phone_number, cm.room_type_rent customer_room_type_rent, cm.room_number_rent customer_room_number_rent, " +
                    "r.id room_id, r.room_type room_room_type, r.rent_rate room_rent_rate, r.room_number room_room_number, " +
                    "rc.room_quantity roomChoice_room_quantity, rc.day_number roomChoice_day_number" +
                    "FROM " + ROOM_CHOICE_TABLE_NAME + " rc join " + CustomerDAO.CUSTOMER_TABLE_NAME+ " cm on rc.customer_id = cm.id join "+ RoomDAO.ROOM_TABLE_NAME + "r on rc.room_id = r.id";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            roomChoices = new ArrayList<>();
            while (resultSet.next()) {
                int customer_id = resultSet.getInt(CUSTOMER_ID);
                String name = resultSet.getString(CustomerDAO.NAME);
                String address = resultSet.getString(CustomerDAO.ADDRESS);
                String phone_number = resultSet.getString(CustomerDAO.PHONE_NUMBER);
                String room_type_rent = resultSet.getString(CustomerDAO.ROOM_TYPE_RENT);
                int room_number_rent = resultSet.getInt(CustomerDAO.ROOM_NUMBER_RENT);
                Customer customer = new Customer(customer_id, name, address, phone_number, room_type_rent, room_number_rent);

                int room_id = resultSet.getInt(ROOM_ID);
                String room_type = resultSet.getString(RoomDAO.ROOM_TYPE);
                float rent_rate = resultSet.getFloat(RoomDAO.RENT_RATE);
                int room_number = resultSet.getInt(RoomDAO.ROOM_NUMBER);
                Room room = new Room(room_id, room_type, rent_rate, room_number);

                int room_quantity = resultSet.getInt(ROOM_QUANTITY);
                int day_number = resultSet.getInt(DAY_NUMBER);


                RoomChoice roomChoice = new RoomChoice(customer, room, room_quantity, day_number);
                roomChoices.add(roomChoice);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(resultSet, preparedStatement, null);
        }
        return roomChoices;
    }

    public void insertRoomChoices(RoomChoice roomChoice) {
        if (ObjectUtil.isEmpty(roomChoice)) {
            return;
        }
        PreparedStatement preparedStatement = null;
        try {
            String query = "INSERT INTO " + ROOM_CHOICE_TABLE_NAME + " VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, roomChoice.getCustomer().getId());
            preparedStatement.setInt(2, roomChoice.getRoom().getId());
            preparedStatement.setInt(3, roomChoice.getRoomQuantity());
            preparedStatement.setInt(4, roomChoice.getDayNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(null, preparedStatement, null);
        }
    }

    public void insertNewRoomChoice(List<RoomChoice> roomChoices) {
        if (CollectionUtil.isEmpty(roomChoices)) {
            return;
        }
        roomChoices.forEach(this::insertRoomChoices);
    }
}
