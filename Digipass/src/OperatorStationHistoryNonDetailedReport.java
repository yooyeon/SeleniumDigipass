import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class OperatorStationHistoryNonDetailedReport {

	@Test
	public  void mainOperatorStationHistoryNonDetailedReport() throws InterruptedException, SQLException {
			// Connect to DB
			String UserName="sa";
			String Password="ChangeIt17";
			String serverName="10.172.86.53";
			String dbName="passport_sandbox";
			String  DB_URL = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true";
			Connection con= DriverManager.getConnection( DB_URL,UserName, Password);

			Statement s=con.createStatement();
		
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

			// Navigate to Reports - Operator Station History page
			driver.findElement(By.cssSelector("i.fas.fa-file-alt")).click();
			Thread.sleep(5000);
			driver.findElement(By.cssSelector("label.menu-open-button")).click();
			Thread.sleep(2000);
			driver.findElement(By.cssSelector("i.fas.fa-history")).click();
			Thread.sleep(3000);
				
			// Go to db,  get last entry from operator_station_history table.
			ResultSet rs= s.executeQuery("SELECT TOP (10) * FROM [passport_sandbox].[dbo].[operator_station_history] order by user_modified_date desc");
			rs.next(); 
			String badge= rs.getString("badge");
			System.out.println("latest record badge is: "+badge);
			String station= rs.getString("station_name");
			System.out.println("latest record station is: "+station);
			String shift= rs.getString("shift");
			System.out.println("latest record shift is: "+shift);
			String date= rs.getString("user_modified_date");
			System.out.println("latest record date is: "+date);
			String month= (String) date.subSequence(6, 7);
			String year= (String) date.subSequence(0, 4);
			String day= (String) date.subSequence(8, 10);
			System.out.println("latest record month is: "+month);
			System.out.println("latest record year is: "+year);
			System.out.println("latest record day is: "+day);
			String fromdate = month+"/"+day+"/"+year+", 12:00 AM";
			System.out.println("So will enter from date as: "+fromdate);	
			
			//enter badge as queried badge #
			WebElement  ele1 = driver.findElement(By.xpath("//div[@data-dx_placeholder='Enter Badge']")); 
			WebElement  parent = ele1.findElement(By.xpath("./.."));
			WebElement  child = parent.findElement(By.xpath(".//input[1]"));			
			child.sendKeys(badge);
			
			//select queried date's midnight as from date in the page
			driver.findElement(By.xpath("//body/div[10]/div[1]/div[1]/div[1]/input[1]")).clear();
			driver.findElement(By.xpath("//body/div[10]/div[1]/div[1]/div[1]/input[1]")).sendKeys(fromdate);
			
			
			
			
			//select tomorrow as to date in the page
			driver.findElement(By.xpath("//body/div[11]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]")).click();
			Thread.sleep(2000);	
			driver.findElement(By.xpath("//td[contains(@class,'dx-calendar-selected-date')]/following-sibling::td[1]")).click(); 
			Thread.sleep(1000);
			driver.findElement(By.xpath("//span[contains(text(),'Apply')]")).click();
			Thread.sleep(1000);
			
			//click on search to get report
			driver.findElement(By.xpath("//span[contains(text(),'Submit')]")).click();
			Thread.sleep(5000);
			
			//verify first record is queried latest entry .
			String d=driver.findElement(By.xpath("//td[@aria-describedby='dx-col-1']")).getText();
			Assert.assertEquals(d, month+"/"+day+"/"+year);
			
			String b=driver.findElement(By.xpath("//td[@aria-describedby='dx-col-2']")).getText();
			Assert.assertEquals(b, badge);
			
			String st=driver.findElement(By.xpath("//td[@aria-describedby='dx-col-3']")).getText();
			Assert.assertEquals(st, station);
			
			String sh=driver.findElement(By.xpath("//td[@aria-describedby='dx-col-4']")).getText();
			Assert.assertEquals(sh, shift);
			
			System.out.println("Test pass!");
			

			driver.quit();
				
				
				
			
	}

}
