import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.testng.Assert;

public class DbTesting {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		String UserName="sa";
		String Password="ChangeIt17";
		String serverName="10.172.86.53";
		String dbName="passport_sandbox";
        String  DB_URL = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true";
		Connection con= DriverManager.getConnection( DB_URL,UserName, Password);

		Statement s=con.createStatement();
		
		s.execute("UPDATE [status_codes] SET oneButtonAssign=1 WHERE [key]='READY'"); //update table
		
		ResultSet rs= s.executeQuery("SELECT  *  FROM [status_codes] WHERE [key]='READY'");//select from table
		
		while (rs.next()) //rs.next() will move to 1st index, since by default will point to base index.
		{
			System.out.println( rs.getString("oneButtonAssign"));
		}
	
		
		
		
		
	}

}
