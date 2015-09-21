import java.sql.*;
import simpledb.remote.SimpleDriver;

public class CreateStudentDB {
    public static void main(String[] args) {
		Connection conn = null;
		try {
			Driver d = new SimpleDriver();
			conn = d.connect("jdbc:simpledb://localhost", null);
			Statement stmt = conn.createStatement();

			//Creates a Player Table in the database
			String s = "create table PLAYER(PId int, PName varchar(10), Salary int, TeamID int)";
			stmt.executeUpdate(s);
			System.out.println("Table PLAYER created.");
			
			//Creates a Team Table in the database
			s = "create table Team(TeamID int, TName varchar(50), TWins int, TLosses int)";
			stmt.executeUpdate(s);
			
			//Inserts some example players into the Player Table
			s = "insert into PLAYER(PId, PName, Salary, TeamID) values ";
			String[] playvals = {"(1, 'joe', 10000, 4)",
								 "(2, 'amy', 20000, 4)",
								 "(3, 'max', 30000, 5)",
								 "(4, 'sue', 40000, 5)",
								 "(5, 'bob', 50000, 3)",
								 "(6, 'kim', 60000, 1)",
								 "(7, 'art', 70000, 4)",
								 "(8, 'pat', 80000, 1)",
								 "(9, 'lee', 90000, 2)",
								 "(10, 'mike', 30000, 4)"};
			for (int i=0; i<playvals.length; i++) {
				stmt.executeUpdate(s + playvals[i]);
				System.out.println("Player records inserted.");
			}

			System.out.println("After player insertion!!");
			
			//Insert some example Teams into the Team Table
			s = "insert into Team(TeamID, TName, TWins, TLosses) values ";
			String TeamVals[] = {"(1, 'Rangers', 3, 0)",
								 "(2, 'Islanders', 4, 1)",
								 "(3, 'Jets', 1, 1)",
								 "(4, 'Mets', 5, 7)",
								 "(5, 'Yankees', 1, 3)",
								 "(5, 'Giants', 3, 5)"};
			for(int i=0; i<TeamVals.length; i++) {
				stmt.executeUpdate(s + TeamVals[i]);
				System.out.println("Team " + i + " inserted");
			}
			
			
			//Query the Player Table and print results
			String PlayerQuery = "select PName from Player where PID=1";
			ResultSet rs = stmt.executeQuery(PlayerQuery);
			while(rs.next()) {
				String PNameQuery = (rs.getString("PName"));
				System.out.println(PNameQuery);
			}
			
			//Query the Team Table and print results
			String TeamQuery = "select TeamID, TName, TLosses from Team where TWins = 3";
			ResultSet teamResult = stmt.executeQuery(TeamQuery);
			while(teamResult.next()) {
				String TNameQuery = (teamResult.getString("TName"));
				Integer TeamIDQuery = (teamResult.getInt("TeamID"));
				Integer TeamLossesQuery = (teamResult.getInt("TLosses"));
				System.out.println(TNameQuery + " " + TeamIDQuery + " " + TeamLossesQuery);
			}
			System.out.println("Done with queries");
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
