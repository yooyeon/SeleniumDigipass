package Dash;

import static org.testng.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ReserveOperatorAndRemoveRes {
	@Test
	public void mainReserveOperatorAndRemoveRes() throws InterruptedException, SQLException {
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
		
		String operator1="49654";
		String operator2="57957";
		String operator3="59836";		
				
		// Connect to DB and confirm operators are inserted in dashboard_data or unassigned table
		String UserName="sa";
		String Password="ChangeIt17";
		String serverName="10.172.86.53";
		String dbName="passport_sandbox";
		String  DB_URL = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true";
		Connection con= DriverManager.getConnection( DB_URL,UserName, Password);		
		Statement s=con.createStatement();
		
	
		Thread.sleep(10000);
		
		// click on unassigned panel icon on top in case no unassigned panel displayed in the dashboard.
		
		try {
			driver.findElement(By.cssSelector("i.SETicon-unassign.SIcon ")).click();	
			}
			catch(Exception e) {
			  now = LocalDateTime.now(); 
			  //System.out.println("@"+dt.format(now)+" "+"Clicked unassigned icon on top.");
			}
		Thread.sleep(3000);	
		
		
		ResultSet rs= s.executeQuery("SELECT  * FROM [passport_sandbox].[dbo].[dashboard_data] where badge in ('"+operator1+"','"+operator2+"','"+operator3+"')");
		
		while(rs.next()) {
			String o=rs.getNString("badge");
			// call method to move operator to unassigned
			 dragFromStationToUnassigned(driver,o);
		} 
		driver.navigate().refresh();
		Thread.sleep(20000);
		// Now all the operators are in unassgined. 
		// Try to reserve operator 2 in station "ASSY SUP 3".
			
		String sta3="ASSY SUP 3";	
		dragFromUnassignedToStation(driver,operator2, sta3);
		Thread.sleep(10000);	
	
		Actions actions = new Actions(driver);
		WebElement w= driver.findElement(By.xpath("//*[contains(text(),'"+operator2+"')]"));
		actions.contextClick(w).perform();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//div[@id='reserveStationBtn']")).click();
		Thread.sleep(20000);
		driver.findElement(By.xpath("//span[contains(text(),'Yes')]")).click();
		Thread.sleep(5000);
		
		//Confirm operator is reserved in station "ASSY SUP 3".
		String staID=driver.findElement(By.cssSelector("i.fad.fa-bookmark.reservedStationIcon")).getAttribute("data-stationid");
		
		rs= s.executeQuery("SELECT *FROM [passport_sandbox].[dbo].[station] where name ='"+sta3+"'");
		
		rs.next();
			int id=rs.getInt("id");
			
		Assert.assertEquals(id,Integer.parseInt(staID));
		
		
		driver.findElement(By.cssSelector("i.fad.fa-bookmark.reservedStationIcon")).click();
		Thread.sleep(5000);
		int si=driver.findElements(By.xpath("//div[contains(@ondblclick,'showReserveStationPopUp')]/div[contains(text(),'57957')]")).size();
		Assert.assertTrue(si>0);
		now = LocalDateTime.now();  
		System.out.println("@"+dt.format(now)+" "+"operator "+operator2+ " is reserved in statation "+sta3+"." );
		
		//Remove reserved operator.
		driver.findElement(By.xpath("//div[@data-stationid='"+id+"']")).click();
		Thread.sleep(5000);	
		
		//confirm reservation icon is removed.
		si=driver.findElements(By.cssSelector("i.fad.fa-bookmark.reservedStationIcon")).size();
		Assert.assertTrue(si==0);
		
		
		now = LocalDateTime.now();  
		System.out.println("@"+dt.format(now)+" "+"Removed operator "+operator2+ " Reservation successfully." );
		
		now = LocalDateTime.now();  
		System.out.println("@"+dt.format(now)+" "+"Test pass!");		
		driver.quit();
 
			

	}
	public static void dragFromStationToUnassigned(WebDriver driver,String operator) throws InterruptedException {
		DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  

		LocalDateTime now = LocalDateTime.now();
		
		WebElement target = driver.findElement(By.xpath("//div[@id='unassignedSec']"));
		
        // Assign(drag and drop) operator to that station.
		Actions a = new Actions(driver);
		WebElement source = driver.findElement(By.xpath("//*[contains(text(),'"+operator+"')]"));
		a.dragAndDrop(source, target).build().perform();
		Thread.sleep(20000);
		
		
		//Confirm previous occupied station moved to unassigned.
		String c=driver.findElement(By.xpath("//div[@id='"+operator+"']")).getAttribute("class");	
		Assert.assertTrue(c.contains("unassignedPanelHeader"));
		now = LocalDateTime.now();  
		System.out.println("@"+dt.format(now)+" "+"operator "+operator+ " is moved from station to  unassigned panel.");
				

	}
	public static void dragFromUnassignedToStation(WebDriver driver,String operator, String sta) throws InterruptedException{
		DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  

		LocalDateTime now = LocalDateTime.now();  

		String dropDes=sta;
		//System.out.println("Will drop operator to "+dropDes+" station.");
		
		WebElement ele1 = driver.findElement(By.xpath("//*[text()='"+dropDes+"']")); 
		WebElement parent = ele1.findElement(By.xpath("./../.."));// find station tile
		WebElement target = parent.findElement(By.xpath("./div[2]"));
		
        // Assign(drag and drop) operator to that station.
		Actions a = new Actions(driver);
		WebElement source = driver.findElement(By.xpath("//*[contains(text(),'"+operator+"')]"));
		a.dragAndDrop(source, target).build().perform();
		Thread.sleep(20000);
		
		//Confirm operator dropped to proper station.
		ele1 = driver.findElement(By.xpath("//div[contains(@style,'"+operator+".jpg')]")); 
		parent = ele1.findElement(By.xpath("./.."));// find ele1 parent element
		Boolean t=parent.findElement(By.xpath("./div[1]")).getText().contains(dropDes);// find ele1 parent element's third td child element.	
		Assert.assertTrue(t);	
		now = LocalDateTime.now();  
		System.out.println("@"+dt.format(now)+" "+"dragged and dropped operator "+operator+" to "+dropDes+" station.");	
}
	
}
