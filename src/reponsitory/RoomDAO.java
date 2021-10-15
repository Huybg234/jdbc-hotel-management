package reponsitory;

import constant.DatabaseConstant;
import entity.Room;
import util.CollectionUtil;
import util.DatabaseConnection;
import util.ObjectUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    public static final String ROOM_TABLE_NAME = "room";

    public static final String ID = "id";
    public static final String ROOM_TYPE = "room_type";
    public static final String RENT_RATE = "rent_rate";
    public static final String ROOM_NUMBER = "room_number";

    private static final Connection connection;

    static {
        connection = DatabaseConnection.openConnection(DatabaseConstant.DRIVER_STRING, DatabaseConstant.URL, DatabaseConstant.USERNAME, DatabaseConstant.PASSWORD);
    }

    public List<Room> getRooms() {
        List<Room> rooms = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM " + ROOM_TABLE_NAME + " order by id";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            rooms = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt(ID);
                String room_type = resultSet.getString(ROOM_TYPE);
                float rent_rate = resultSet.getFloat(RENT_RATE);
                int room_number = resultSet.getInt(ROOM_NUMBER);
                Room room = new Room(id, room_type, rent_rate, room_number);
                rooms.add(room);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(resultSet, preparedStatement, null);
        }
        return rooms;
    }

    public void insertNewRoom(Room room) {
        if (ObjectUtil.isEmpty(room)) {
            return;
        }
        PreparedStatement preparedStatement = null;
        try {
            String query = "INSERT INTO " + ROOM_TABLE_NAME + " VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, room.getId());
            preparedStatement.setString(2, room.getRoomType());
            preparedStatement.setFloat(3, room.getRentRate());
            preparedStatement.setInt(4, room.getRoomNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(null, preparedStatement, null);
        }
    }

    public void insertNewRoom(List<Room> rooms) {
        if (CollectionUtil.isEmpty(rooms)) {
            return;
        }
        rooms.forEach(this::insertNewRoom);
    }
}
