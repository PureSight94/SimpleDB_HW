import java.sql.*;
import org.apache.derby.jdbc.ClientDriver;

public class CreateStudentDB {
    public static void main(String[] args) {
		Connection conn = null;
		try {

		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (conn != null)
					conn.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
