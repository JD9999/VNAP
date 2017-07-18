package VNAP;

import java.sql.*;

public class Connect {
    private final String url = "jdbc:mariadb://localhost/" + MyPlugin.dbUrl;
    private final String dbUsrName = MyPlugin.dbUsrName;
    private final String dbPassword = MyPlugin.dbPassword;
    private final String tableName = MyPlugin.dbTable;
    private String sql = "";

    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public Connect() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection(this.url, this.dbUsrName, this.dbPassword);
            stmt = conn.createStatement();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int CheckIfExists(String username) {
        int count = 0;
        sql = "SELECT COUNT(*) FROM " + tableName + " WHERE username = ?";
        try {
            final PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (rs.next())
                count = rs.getInt(1);
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return count;
    }

    public void RegisterUser(String username, String password) {
        sql = "INSERT INTO " + tableName + " (username, password) VALUES ('" + username + "','" + password + "')";
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public boolean AuthUser(String username, String password) {
        sql = "SELECT COUNT(*) FROM " + tableName + " WHERE username='" + username + "' AND password='" + password + "'";
        boolean ok = false;

        try {
            rs = stmt.executeQuery(sql);
            if (rs.next())
                if (rs.getInt(1) > 0)
                    return true;
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return false;
    }

    public void Close() {
        try {
            if (conn != null)
                conn.close();
            if (stmt != null)
                stmt.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
