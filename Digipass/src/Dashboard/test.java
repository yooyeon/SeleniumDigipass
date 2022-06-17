package Dashboard;
import java.time.Duration;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;



public class test {

	public static void main(String[] args) throws InterruptedException, SQLException {
		
		int tr=0;
		while(true && tr!=9)
		{
			
             Thread.sleep(2000);
             tr++;
             System.out.println(tr);
           
		}
}
}