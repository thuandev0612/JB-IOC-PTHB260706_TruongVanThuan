package JB_IOC_PTHB260706_Truongvanthuan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =  "jdbc:postgresql://localhost:5432/db_cinema_booking";

    private static final String USER = "postgres";
    private static final String password = "@Thuan061201";

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL, USER, password);
    }


}

