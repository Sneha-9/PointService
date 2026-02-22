package com.sneha;

import com.sneha.pointservice.Point;
import com.sneha.pointservice.UserPointData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PointDatabase {
    private final Connection connection;

    PointDatabase() {
        String jdbcURL = "jdbc:postgresql://localhost:5432/pointService";
        String username = "sneha";
        String password = "password";

        // Establish the connection
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(jdbcURL, username, password);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    Point aggregate(String id, int point, long createdat, long updatedat) {
        try {
            String insertStatement = String.format(
                    "Insert into users (id, point, createdat, updatedat) values ('%s', '%s','%s','%d','%d')",
                    id,
                    point,
                    createdat,
                    updatedat
            );

            Statement statement = connection.createStatement();
            statement.execute(insertStatement);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    List<UserPointData> getUserPoints(int minPoint) {
        List<UserPointData> result = new ArrayList<>();
        try {
            String selectStatement = String.format("Select * from pointservice where point >= '%s'", minPoint);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectStatement);

            while (resultSet.next()) {
                result.add(
                        UserPointData.newBuilder()
                                .setId(resultSet.getString("id"))
                                .setPoint(Point.newBuilder().setValue(resultSet.getInt("point")).build())
                                .build()
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
