package reponsitory;

import constant.DatabaseConstant;
import entity.Customer;
import util.CollectionUtil;
import util.DatabaseConnection;
import util.ObjectUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    public static final String CUSTOMER_TABLE_NAME = "customer";

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String ROOM_TYPE_RENT = "room_type_rent";
    public static final String ROOM_NUMBER_RENT = "room_number_rent";

    private static final Connection connection;

    static {
        connection = DatabaseConnection.openConnection(DatabaseConstant.DRIVER_STRING, DatabaseConstant.URL, DatabaseConstant.USERNAME, DatabaseConstant.PASSWORD);
    }

    public List<Customer> getCustomers() {
        List<Customer> customers = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String query = "SELECT * FROM " + CUSTOMER_TABLE_NAME + " order by id";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            customers = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt(ID);
                String name = resultSet.getString(NAME);
                String address = resultSet.getString(ADDRESS);
                String phone_number = resultSet.getString(PHONE_NUMBER);
                String room_type_rent = resultSet.getString(ROOM_TYPE_RENT);
                int room_number_rent = resultSet.getInt(ROOM_NUMBER_RENT);
                Customer customer = new Customer(id, name, address, phone_number, room_type_rent, room_number_rent);
                customers.add(customer);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(resultSet, preparedStatement, null);
        }
        return customers;
    }

    public void insertNewCustomer(Customer customer) {
        if (ObjectUtil.isEmpty(customer)) {
            return;
        }
        PreparedStatement preparedStatement = null;
        try {
            String query = "INSERT INTO " + CUSTOMER_TABLE_NAME + " VALUES (?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, customer.getId());
            preparedStatement.setString(2, customer.getName());
            preparedStatement.setString(3, customer.getAddress());
            preparedStatement.setString(4, customer.getPhoneNumber());
            preparedStatement.setString(5, customer.getRoomTypeRent());
            preparedStatement.setInt(6, customer.getRoomNumberRent());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(null, preparedStatement, null);
        }
    }

    public void insertNewCustomer(List<Customer> customers) {
        if (CollectionUtil.isEmpty(customers)) {
            return;
        }
        customers.forEach(this::insertNewCustomer);
    }
}
