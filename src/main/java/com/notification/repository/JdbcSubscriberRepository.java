package com.notification.repository;

import com.notification.model.Subscriber;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class JdbcSubscriberRepository implements SubscriberRepository {

    private final DataSource dataSource;

    public JdbcSubscriberRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Subscriber> findSubscribers(String medicineId, String ubsId) throws Exception {
        String baseSql = "select u.name, u.phone " +
                "from user_medication_subscription ums " +
                "join users u on u.id = ums.user_id " +
                "where ums.medicine_id = ?";

        boolean filterByUbs = ubsId != null && !ubsId.isBlank();
        String sql = filterByUbs ? baseSql + " and ums.ubs_id = ?" : baseSql;

        List<Subscriber> subscribers = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, medicineId);
            if (filterByUbs) {
                stmt.setString(2, ubsId);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString(1);
                    String phone = rs.getString(2);
                    subscribers.add(new Subscriber(name, phone));
                }
            }
        }

        return subscribers;
    }
}
