import java.sql.*;

public class MySQLDatabaseHelper {
    // MySQL database URL, username, and password
    private static final String DB_URL = "http://localhost/phpmyadmin/index.php?route=/database/structure&db=memory_management";
    private static final String DB_USER = "root"; // Replace with your MySQL username
    private static final String DB_PASSWORD = "password"; // Replace with your MySQL password

    // Method to establish a connection to the MySQL database
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Add Memory Block
    public static void addMemoryBlock(String id, int size) {
        String sql = "INSERT INTO memory_blocks(id, size, is_allocated) VALUES(?, ?, ?)";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setInt(2, size);
            pstmt.setBoolean(3, false);  // Initially free
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Allocate Memory Block
    public static boolean allocateMemoryBlock(String id) {
        String sql = "UPDATE memory_blocks SET is_allocated = ? WHERE id = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, true);
            pstmt.setString(2, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Free Memory Block
    public static boolean freeMemoryBlock(String id) {
        String sql = "UPDATE memory_blocks SET is_allocated = ? WHERE id = ?";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, false);
            pstmt.setString(2, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Retrieve Memory Blocks
    public static ResultSet getMemoryBlocks() {
        String sql = "SELECT * FROM memory_blocks";
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
