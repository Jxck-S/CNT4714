import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class Stored {
    public Connection dbConnection = null;
    public Connection opp_log_dbConnection = null;
    public String user_logged_in = null;
    public Integer user_query_count = null;
    public Integer user_update_count = null;
    public void update_logged_in_user(String user) {
        // This is only for use on localhost, this doesn't accurately reflect the user logged in if remote
        this.user_logged_in = user + "@localhost";
        System.out.println("Set Stored User logged in: " + this.user_logged_in);
    }
    public void get_inital_user_counts_from_db() {
        try {
            String sql = "SELECT num_queries, num_updates  FROM operationscount where login_username = ?";
            PreparedStatement statement = this.opp_log_dbConnection.prepareStatement(sql);
            statement.setString(1, this.user_logged_in);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Log Record found for that username");
                    this.user_query_count = resultSet.getInt("num_queries");
                    this.user_update_count = resultSet.getInt("num_updates");
                    System.out.println("User query count: " + this.user_query_count);
                    System.out.println("User update count: " + this.user_update_count);
                } else {
                    System.out.println("No record found for that username, init to 0s");
                    this.user_query_count = 0;
                    this.user_update_count = 0;
                    String insert_sql = "INSERT INTO operationscount (login_username, num_queries, num_updates) VALUES (?, 0, 0)";
                    PreparedStatement insert_Statement = this.opp_log_dbConnection.prepareStatement(insert_sql);
                    insert_Statement.setString(1, this.user_logged_in);
                    int rows_affected = insert_Statement.executeUpdate();
                    if (rows_affected > 0) {
                        System.out.println("User counts inserted");
                    } else {
                        System.out.println("User counts not inserted");
                    }

                }
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
        }
    }

    public void update_user_counts_in_db() {
        try {
            String sql = "UPDATE operationscount SET num_queries = ?, num_updates = ? WHERE login_username = ?";
            PreparedStatement statement = this.opp_log_dbConnection.prepareStatement(sql);
            statement.setInt(1, this.user_query_count);
            statement.setInt(2, this.user_update_count);
            statement.setString(3, this.user_logged_in);
            int rows_affected = statement.executeUpdate();
            if (rows_affected > 0) {
                System.out.println("User counts updated");
            } else {
                System.out.println("User counts not updated");
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
        }
    }
    public void increment_query_count() {
        this.user_query_count++;
        this.update_user_counts_in_db();
    }
    public void increment_update_count() {
        this.user_update_count++;
        this.update_user_counts_in_db();
    }
}

