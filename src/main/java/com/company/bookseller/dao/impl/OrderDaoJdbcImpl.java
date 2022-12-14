package com.company.bookseller.dao.impl;

import com.company.bookseller.dao.OrderDao;
import com.company.bookseller.dao.connection.ConnectionManager;
import com.company.bookseller.dao.entity.Order;
import com.company.bookseller.dao.entity.Order.Status;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;

import static com.company.bookseller.service.util.paging.PagingUtil.totalCounter;

@Log4j2
public class OrderDaoJdbcImpl implements OrderDao {
    private static final String CREATE_ORDER =
            "INSERT INTO orders  (date, status_id, user_id, total_price)"
                    + "VALUES (?, (SELECT id FROM statuses WHERE status = ?), ?, ?)";
    private static final String UPDATE_ORDER =
            "UPDATE orders SET date = ?, status_id = (SELECT id FROM statuses WHERE status = ?), user_id = ?, "
                    + "total_price = ? WHERE id = ?";
    private static final String ORDERS_ALL = "SELECT o.id, o.date, s.status, o.user_id, o.total_price "
            + "FROM orders o JOIN statuses s ON o.status_id = s.id";
    private static final String GET_ALL_PAGED = ORDERS_ALL + " ORDER BY o.id  LIMIT ? OFFSET ?";
    private static final String GET_BY_ID = ORDERS_ALL + " WHERE o.id = ? ORDER BY o.id";
    private static final String GET_BY_USER_ID = ORDERS_ALL + " WHERE user_id = ?";
    private static final String DELETE_ORDER = "UPDATE orders SET status_id = (SELECT id FROM statuses WHERE status = 'CANCELLED')"
            + " WHERE id = ?";
    private static final String COUNT = "SELECT count(*) AS total FROM orders";
    private final ConnectionManager connectionManager;

    public OrderDaoJdbcImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    private Order processOrder(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setId(resultSet.getLong("id"));
        order.setOrderDateTime(resultSet.getTimestamp("date").toLocalDateTime());
        order.setStatus(Status.valueOf(resultSet.getString("status")));
        order.setUserId(resultSet.getLong("user_id"));
        order.setTotalPrice(resultSet.getBigDecimal("total_price"));
        return order;
    }

    @Override
    public List<Order> getAll(int limit, int offset) {
        List<Order> orders = new ArrayList<>();
        try {
            Connection connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_PAGED);
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orders.add(processOrder(resultSet));
            }
        } catch (SQLException e) {
            log.error(e);
        }
        return orders;
    }

    @Override
    public Order get(Long id) {
        Order order = null;
        try {
            Connection connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_BY_ID);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                order = processOrder(resultSet);
            }
        } catch (SQLException e) {
            log.error(e);
        }
        return order;
    }

    @Override
    public Order create(Order order) {
        try {
            Connection connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_ORDER, Statement.RETURN_GENERATED_KEYS);
            statement.setTimestamp(1, Timestamp.valueOf(order.getOrderDateTime()));
            statement.setString(2, String.valueOf(order.getStatus()));
            statement.setLong(3, order.getUserId());
            statement.setBigDecimal(4, order.getTotalPrice());
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                order.setId(keys.getLong(1));
                return order;
            }
        } catch (SQLException e) {
            log.error(e);
        }
        throw new RuntimeException("Couldn't create object: " + order);
    }

    @Override
    public Order update(Order order) {
        try {
            Connection connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_ORDER);
            statement.setTimestamp(1, Timestamp.valueOf(order.getOrderDateTime()));
            statement.setString(2, String.valueOf(order.getStatus()));
            statement.setLong(3, order.getUserId());
            statement.setBigDecimal(4, order.getTotalPrice());
            statement.setLong(5, order.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e);
        }
        return order;
    }

    @Override
    public boolean delete(Long id) {
        try {
            Connection connection = connectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_ORDER);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            log.error(e);
        }
        return true;
    }

    public List<Order> getByUserId(Long userId) {
        return getOrderByParam(userId, GET_BY_USER_ID);
    }

    @Override
    public long count() {
        return totalCounter(connectionManager, COUNT, log);
    }

    private List<Order> getOrderByParam(Long paramId, String sql) {
        List<Order> orders = new ArrayList<>();
        try {
            Connection connection = ConnectionManager.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, paramId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orders.add(processOrder(resultSet));
            }
        } catch (SQLException e) {
            log.error(e);
        }
        return orders;
    }
}
