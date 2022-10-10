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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;


public class ClockOutFromDashaboardStation {

	public static void main(String[] args) throws SQLException, InterruptedException {
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
		
		DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  

		LocalDateTime now = LocalDateTime.now();  
		
		// Connect to DB 
		//String operator="49102";
		String UserName="sa";
		String Password="ChangeIt17";
		String serverName="10.172.86.53";
		String dbName="passport_sandbox";
		String  DB_URL = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true";
		Connection con= DriverManager.getConnection( DB_URL,UserName, Password);		
		Statement s=con.createStatement();
		
		ResultSet rs= s.executeQuery("select top(2) * from dashboard_data where badge is not null order by user_modified_date asc");
		
		rs.next(); 
		String operator= rs.getString("badge");
		StringBuffer buf = new StringBuffer(operator);
		while (buf.length() < 6) {
			  buf.insert(0, '0');
			}

		 operator = buf.toString();
		String station= rs.getString("station_name");
		String dept= rs.getString("department_name");
		now = LocalDateTime.now();
		System.out.println("@"+dt.format(now)+" "+"Operator:"+operator+" is in department: "+dept+" and station: "+station);
		
		rs= s.executeQuery("SELECT TOP (1) * FROM [passport_sandbox].[dbo].[operator_status] where badge ="+operator+" order by queued_time desc");
		rs.next(); 
		String id1= rs.getString("id");
		now = LocalDateTime.now();  
		System.out.println("@"+dt.format(now)+" "+"In operator_status table latest record id is: "+id1);		
		
		
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
		Thread.sleep(1000);
		
		//Get current time
		DateFormat dateFormat2 = new SimpleDateFormat("hh:mm a");
		Date date2 = new Date();
		String time2 = dateFormat2.format(date2);
		
		DateFormat dateFormat3 = new SimpleDateFormat("MM/dd");
		Date date = new Date();
		String dat1 = dateFormat3.format(date);
		String strPattern = "^0+(?!$)";
	    dat1 = dat1.replaceAll(strPattern, "");
		System.out.println("today is: "+dat1);
	
	
		
		// In today's schedule , enter clock out time and current time and save. 
		ele1 = driver.findElement(By.xpath("//span[contains(text(), ' "+dat1+"')]"));
		parent = ele1.findElement(By.xpath("./../../../../../../.."));
		child = parent.findElement(By.xpath("./div[1]/timecard-add-cell[1]/div[1]"));
		String app=child.getAttribute("id");
		char a1 = app.charAt(0);
		
		Actions act = new Actions(driver);
		WebElement  dc = driver.findElement(By.id(a1+"_outpunch"));
		act.contextClick(dc).perform();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[@aria-label='Edit Punch']")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("punch-effective-time_inptext")).sendKeys(time2);
		Thread.sleep(1000);
		driver.findElement(By.xpath("//button[text()='Apply']")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("i.icon-k-save.button-highlight.hidden-xs")).click();
		
		//check in db every 20sec, wait until clock entry created...
		now = LocalDateTime.now();  
		System.out.println("@"+dt.format(now)+" "+"Wait for clock entry, will check every 20 sec...");
		
		while(!checkNewEntryCreated(rs,s, id1, operator))
		{

             Thread.sleep(20000);
           
		}
		rs= s.executeQuery("SELECT TOP (1) * FROM [passport_sandbox].[dbo].[operator_status] where badge ="+operator+" order by queued_time desc");
		rs.next(); 
		String id2= rs.getString("id");
		Assert.assertTrue(Integer.parseInt(id2)>Integer.parseInt(id1));
		String v= rs.getString("clocked_in");
		Assert.assertEquals(v, "0");
		v= rs.getString("clocked_out");
		Assert.assertEquals(v, "1");
		String badge=rs.getString("badge");
		Assert.assertEquals(badge, operator);
		now = LocalDateTime.now();  
		System.out.println("@"+dt.format(now)+" "+"It created new clock out entry and id is "+id2);	
		
		
		
		//Wait 1min, then confirm in the db, operator got removed from dashboard_data table.
		now = LocalDateTime.now();  
		System.out.println("@"+dt.format(now)+" "+"Wait for 20 sec...");
		Thread.sleep(20000);	
		rs= s.executeQuery(" SELECT * FROM [passport_sandbox].[dbo].[dashboard_data] where badge ="+operator+"");
		Assert.assertFalse(rs.next()) ; 
		now = LocalDateTime.now();  
		System.out.println("@"+dt.format(now)+" "+"Operator: "+operator+" is removed from department: "+dept+" station: "+station);		
		
	    
		
		now = LocalDateTime.now(); 
		System.out.println("@"+dt.format(now)+" "+"Test pass!");		
		driver.quit();
		
		
}

public static boolean checkNewEntryCreated(ResultSet rs,Statement s, String id1, String operator) throws InterruptedException, SQLException{
rs= s.executeQuery("SELECT TOP (1) * FROM [passport_sandbox].[dbo].[operator_status] where badge ="+operator+" order by queued_time desc");
rs.next(); 
String id2= rs.getString("id");
if(Integer.parseInt(id2)>Integer.parseInt(id1)) {
	return true;
} else {
	return false;
}
		
}

}
