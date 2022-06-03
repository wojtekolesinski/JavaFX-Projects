package com.example.tpo5.servlets;

import com.example.tpo5.models.Car;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "db", value = "/db")
public class DbConnectionServlet extends HttpServlet {
    private static final String url = "jdbc:sqlserver://localhost:1433;database=tpo;encrypt=true;trustServerCertificate=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30";
    private Connection connection;

    @Override
    public void init() throws ServletException {
        super.init();
        connection = null;
        try {
            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
            connection = DriverManager.getConnection(url, "SA", "MyPass@word");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("cars", getCars(req.getParameter("type")));
        req.getRequestDispatcher("/table").forward(req, resp);
    }

    @Override
    public void destroy() {
        super.destroy();
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Car> getCars(String type) {
        List<Car> cars = new ArrayList<>();
        try {
            String sql = """
                    SELECT [id], [make], [model], [type], [production_year], [fuel_consumption]
                    FROM [cars]
                    """;

            PreparedStatement statement = connection.prepareStatement(sql);

            if (type != null && !type.isEmpty()) {
                sql += "WHERE [type] = ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, type);
            }
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                cars.add(new Car(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5),
                        rs.getInt(6)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

}
