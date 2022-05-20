package Dashboard;

import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

public class clockIn {

	public static void main(String[] args) throws InterruptedException, SQLException {
		//Before testing, need to specify clock in operator badge number. 
		
		// Login to test WFD 
		System.setProperty("webdriver.chrome.driver", "C:\\yooyeon\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

		driver.get("https://goodmanglobalhold-uat.npr.mykronos.com/timekeeping#/timecard");
		driver.manage().window().maximize();
		driver.findElement(By.id("idToken1")).sendKeys("SETJIRA");
		Thread.sleep(1000);
		driver.findElement(By.id("idToken2")).sendKeys("goodman3");
		Thread.sleep(1000);
		driver.findElement(By.id("loginButton_0")).click();
		Thread.sleep(3000);
		
		// Connect to DB and find latest clock in entry's ID for selected operator
		String operator="57957";
		String UserName="sa";
		String Password="ChangeIt17";
		String serverName="10.172.86.53";
		String dbName="passport_sandbox";
		String  DB_URL = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true";
		Connection con= DriverManager.getConnection( DB_URL,UserName, Password);		
		Statement s=con.createStatement();
				
		ResultSet rs= s.executeQuery("SELECT TOP (1) * FROM [passport_sandbox].[dbo].[operator_status] where badge ="+operator+" order by queued_time desc");
		rs.next(); 
		String id1= rs.getString("id");
		System.out.println("In operator_status table latest record id is: "+id1);		
		
		//click on search and search with a operator badge number, then select operator and click "Go To" button to open timecard page.
		driver.findElement(By.xpath("//button[@title='Employee Search']")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("employeesSearchInput")).sendKeys(operator);
		Thread.sleep(2000);
		driver.findElement(By.xpath("//button[text()='Search']")).click();
		
		WebElement ele1 = driver.findElement(By.xpath("//span[text()='"+operator+"']")); 
		WebElement parent = ele1.findElement(By.xpath("./../../../../../../../../../.."));
		WebElement child = parent.findElement(By.xpath("./div[2]/div[1]/div[1]/input[1]"));		
		child.click();
		Thread.sleep(1000);
		driver.findElement(By.id("goToDropdownButton")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//button[@title='Timecard']")).click();
		Thread.sleep(3000);
		
		//select today in timecard page
		driver.findElement(By.xpath("//button[@title='Select Timeframe']")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//ul[@class='pay-periods-list large']/li[9]/span[text()='Today']")).click();
		Thread.sleep(3000);
		
		// get current time in AM/PM format
		DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
		Date  time = new Date();
		String time1 = dateFormat.format( time);
		System.out.println("current time is: "+time1);
		
		DateFormat dateFormat2 = new SimpleDateFormat("MM/dd");
		Date date = new Date();
		String date1 = dateFormat2.format(date);
		String strPattern = "^0+(?!$)";
	    date1 = date1.replaceAll(strPattern, "");
		System.out.println("today is: "+date1);
		
		
		
		
		// In today's schedule , enter clock in time and current time and save. 
		ele1 = driver.findElement(By.xpath("//span[contains(text(), ' "+date1+"')]"));
		parent = ele1.findElement(By.xpath("./../../../../../.."));
		child = parent.findElement(By.xpath("./div[1]/timecard-add-cell[1]/div[1]"));
		String app=child.getAttribute("id");
		char a1 = app.charAt(0);
		//System.out.println(a1);
						
		Actions act = new Actions(driver);
		WebElement  dc = driver.findElement(By.id(a1+"_inpunch"));
		act.contextClick(dc).perform();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[@aria-label='Edit Punch']")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("punch-effective-time_inptext")).clear();
		driver.findElement(By.id("punch-effective-time_inptext")).sendKeys(time1);
		
	
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[text()='Apply']")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("i.icon-k-save.button-highlight.hidden-xs")).click();
		
		//Wait 3min, then confirm in the db, it created new clock in entry.
		System.out.println("Wait for 3min...");
		Thread.sleep(180000);	
		rs= s.executeQuery("SELECT TOP (1) * FROM [passport_sandbox].[dbo].[operator_status] where badge ="+operator+" order by queued_time desc");
		rs.next(); 
		String id2= rs.getString("id");
		Assert.assertTrue(Integer.parseInt(id2)>Integer.parseInt(id1));
		String v= rs.getString("clocked_in");
		Assert.assertEquals(v, "1");
		v= rs.getString("clocked_out");
		Assert.assertEquals(v, "0");
		String badge=rs.getString("badge");
		Assert.assertEquals(badge, operator);
		System.out.println("It created new clock in entry and id is "+id2);		
		
	
		System.out.println("Test pass!");		
		driver.quit();
		
		

	}

}
