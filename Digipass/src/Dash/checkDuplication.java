package Dash;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class checkDuplication {
	
	@Test
	public void mainCheckDuplication( ) throws InterruptedException, SQLException {
		// Login to home page , and expand menu icons
				System.setProperty("webdriver.chrome.driver", "C:\\yooyeon\\chromedriver.exe");
				WebDriver driver = new ChromeDriver();
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
				driver.get("http://setdev.ad.goodmanmfg.com/digipass/login.php");
				driver.manage().window().maximize();
				driver.findElement(By.id("emailNew")).sendKeys("yuyan.cui@goodmanmfg.com");
				driver.findElement(By.id("passwordNew")).sendKeys("111111");
				driver.findElement(By.id("submitBtn")).click();
				Thread.sleep(3000);
				driver.findElement(By.className("menu-open-button")).click();
				Thread.sleep(1000);
				
				DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  

				LocalDateTime now = LocalDateTime.now();  
				//Navigate to a line dashboard.
				String dept="SE Support Team";
				driver.findElement(By.cssSelector("i.fas.fa-tachometer-alt")).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath("//h4[contains(text(),'Manufacturing')]")).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath("//h4[contains(text(),'Systems Engineering')]")).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath("//h4[contains(text(),'"+dept+"')]")).click();
				Thread.sleep(5000);
				int i=driver.findElements(By.xpath("//div[@id='departmentDash']")).size();
				Assert.assertTrue(i>0);
				Thread.sleep(10000);
				
				// Connect to DB 
				String UserName="sa";
				String Password="ChangeIt17";
				String serverName="10.172.86.53";
				String dbName="passport_sandbox";
				String  DB_URL = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true";
				Connection con= DriverManager.getConnection( DB_URL,UserName, Password);		
				Statement s=con.createStatement();
				
				// check duplication for dashboard station operators
				ResultSet rs= s.executeQuery("SELECT  * FROM [passport_sandbox].[dbo].[dashboard_data] where badge is not null and department_id in \r\n"
						+ "(select id from department where name ='"+dept+"' )");
				
				while(rs.next()) {
					String badge=rs.getNString("badge");
					// call method to move operator to unassigned
					DuplicationChek(driver, badge);
				} 
				
				// check duplication for unassigned panel operators
				 rs= s.executeQuery("select * from unassigned_operators where department_id in \r\n"
				 		+ "(select id from department where name ='"+dept+"' )");
				
				while(rs.next()) {
					String badge=rs.getNString("badge");
					// call method to move operator to unassigned
					DuplicationChek(driver, badge);
				} 
				now = LocalDateTime.now(); 
				System.out.println("@"+dt.format(now)+" "+"Test Done.");
				driver.quit();

	}

	 
	public static void DuplicationChek(WebDriver driver,String operator) throws InterruptedException {
		DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  

		LocalDateTime now = LocalDateTime.now();  
		
		int k= driver.findElements(By.xpath("//*[text()='"+operator+"']")).size();
		if(k>1) {
			System.out.println("@"+dt.format(now)+" "+"Found duplication for operator: "+operator);
		} 

		Thread.sleep(2000);	
		
	}
	
	
}
