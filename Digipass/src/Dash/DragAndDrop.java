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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DragAndDrop {
	
	@Test
	public void mainDragAndDrop( ) throws InterruptedException, SQLException {
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
		
		// Connect to DB and confirm operators are inserted in dashboard_data or unassigned table
				String UserName="sa";
				String Password="ChangeIt17";
				String serverName="10.172.86.53";
				String dbName="passport_sandbox";
				String  DB_URL = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true";
				Connection con= DriverManager.getConnection( DB_URL,UserName, Password);		
				Statement s=con.createStatement();
				
		
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
		Thread.sleep(30000);
		
		String operator1="49654";
		String operator2="57957";
		String operator3="59836";		
		//String operator4="57901";
		//String operator5="64134";
		

		// open WFD page in new tab 
		driver.switchTo().newWindow(WindowType.TAB);
		driver.get("https://goodmanglobalhold-uat.npr.mykronos.com/timekeeping#/timecard");
		driver.manage().window().maximize();
		driver.findElement(By.id("idToken1")).sendKeys("SETJIRA");
		Thread.sleep(1000);
		driver.findElement(By.id("idToken2")).sendKeys("goodman3");
		Thread.sleep(1000);
		driver.findElement(By.id("loginButton_0")).click();
		Thread.sleep(3000);
		
		// Call clock in method three times to clock in three operators.		
		ResultSet rs= s.executeQuery("SELECT  * FROM [passport_sandbox].[dbo].[dashboard_data] where badge="+operator1);
		Boolean d=rs.next();
		rs= s.executeQuery("SELECT  * FROM [passport_sandbox].[dbo].[unassigned_operators] where badge="+operator1);
		Boolean u=rs.next();
		if(!(d || u)) {
			clockIn(driver, operator1);
		} 	
		
		rs= s.executeQuery("SELECT  * FROM [passport_sandbox].[dbo].[dashboard_data] where badge="+operator2);
		 d=rs.next();
		rs= s.executeQuery("SELECT  * FROM [passport_sandbox].[dbo].[unassigned_operators] where badge="+operator2);
		 u=rs.next();
		
		if(!(d || u)) {
			clockIn(driver, operator2);
		} 
		
		rs= s.executeQuery("SELECT  * FROM [passport_sandbox].[dbo].[dashboard_data] where badge="+operator3);
		d=rs.next();
		rs= s.executeQuery("SELECT  * FROM [passport_sandbox].[dbo].[unassigned_operators] where badge="+operator3);
		u=rs.next();
		
		if(!(d || u)) {
			clockIn(driver, operator3);
		} 
		
		
		//switch tab ,and Wait 3min to get clocked in entry .
		Set<String> windows = driver.getWindowHandles();//[parentid, childid]
		Iterator<String> it = windows.iterator();
		String parentId= it.next();//will to go it 0 index
		String childId = it.next();
		driver.switchTo().window(parentId);
		driver.navigate().refresh();
		now = LocalDateTime.now();		

		System.out.println("@"+dt.format(now)+" "+"Wait for 3min...");
		Thread.sleep(180000);	

						
		driver.navigate().refresh();
		Thread.sleep(20000);
		
		// click on unassigned panel icon on top in case no unassigned panel displayed in the dashboard.
		try {
			driver.findElement(By.cssSelector("i.SETicon-unassign.SIcon ")).click();	
			}
			catch(Exception e) {
			  now = LocalDateTime.now(); 
			  System.out.println("@"+dt.format(now)+" "+"Clicked unassigned icon on top.");
			}			
		Thread.sleep(3000);		
		
		rs= s.executeQuery("SELECT  * FROM [passport_sandbox].[dbo].[dashboard_data] where badge in ('"+operator1+"','"+operator2+"','"+operator3+"')");
		
		while(rs.next()) {
			String o=rs.getNString("badge");
			// call method to move operator to unassigned
			 dragFromStationToUnassigned(driver,o);
		} 
		driver.navigate().refresh();
		Thread.sleep(20000);
		// Now all the operators are in unassgined. 
		// Try to drag operator 1 to Assy sup1 station. and operator 2 to Assy sup 3 station.
		String sta1="ASSY SUP 1";
		String sta3="ASSY SUP 3";
		dragFromUnassignedToStation(driver,operator1, sta1);
		dragFromUnassignedToStation(driver,operator2, sta3);
		
		
		// then drag operator 2 from Assy sup3 station to Assy sup2 station which is empty.	
		String sta2="ASSY SUP 2";
		dragFromStationToEmptyStation(driver,operator2,sta2 );
		
		//Drag and drop operator 3 from unassigned panel to occupied station Assy sup2 
		 rs= s.executeQuery("SELECT  * FROM [passport_sandbox].[dbo].[dashboard_data] where  station_name ='"+sta2+"'");			
		 rs.next();
		 String oco=rs.getNString("badge");
		 dragFromUnassignedToOccupiedStation(driver,operator3,sta2, oco);
		
		// then drag operator 1 from Assy sup1 station to Assy sup2 station which is occupied by operator 3
		 rs= s.executeQuery("SELECT  * FROM [passport_sandbox].[dbo].[dashboard_data] where  station_name ='"+sta2+"'");			
		 rs.next();
		 oco=rs.getNString("badge");
		 dragFromStationToOccupiedStation(driver,operator1, sta2, oco);
		 
		
		// then call method to drag operator 1 to unassigned . 
		
		 dragFromStationToUnassigned(driver,operator1);
		 
		 now = LocalDateTime.now(); 
		System.out.println("@"+dt.format(now)+" "+"Test pass!");		
		driver.quit();
		

	}
	
	public static void clockIn(WebDriver driver,String operator) throws InterruptedException {
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
				//driver.findElement(By.xpath("//button[@title='Timecard']")).click();
				driver.findElement(By.xpath("//ul[@class='go-to-items-list']/li[1]/button")).click();
				Thread.sleep(3000);
				
				//select today in timecard page
				driver.findElement(By.xpath("//button[@title='Select Timeframe']")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//ul[@class='pay-periods-list large']/li[9]/span[text()='Today']")).click();
				Thread.sleep(1000);
				
				// get current time in AM/PM format
				DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
				Date  time = new Date();
				String time1 = dateFormat.format( time);
				//System.out.println("current time is: "+time1);
				
				DateFormat dateFormat2 = new SimpleDateFormat("MM/dd");
				Date date = new Date();
				String date1 = dateFormat2.format(date);
				String strPattern = "^0+(?!$)";
			    date1 = date1.replaceAll(strPattern, "");
				//System.out.println("today is: "+date1);
				
							
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
				System.out.println("Clocked in "+operator+" @"+time1+" "+date1);
		
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
				Thread.sleep(30000);
				
				//Confirm operator dropped to proper station.
				ele1 = driver.findElement(By.xpath("//div[contains(@style,'"+operator+".jpg')]")); 
				parent = ele1.findElement(By.xpath("./.."));// find ele1 parent element
				Boolean t=parent.findElement(By.xpath("./div[1]")).getText().contains(dropDes);// find ele1 parent element's third td child element.	
				Assert.assertTrue(t);	
				now = LocalDateTime.now();  
				System.out.println("@"+dt.format(now)+" "+"dragged and dropped operator "+operator+" to "+dropDes+" station.");	
	}
	public static void dragFromUnassignedToOccupiedStation(WebDriver driver,String operator, String sta, String occupiedOperator) throws InterruptedException{
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
		Thread.sleep(30000);
		
		//Confirm operator dropped to proper station.
		ele1 = driver.findElement(By.xpath("//div[contains(@style,'"+operator+".jpg')]")); 
		parent = ele1.findElement(By.xpath("./.."));// find ele1 parent element
		Boolean t=parent.findElement(By.xpath("./div[1]")).getText().contains(dropDes);// find ele1 parent element's third td child element.	
		Assert.assertTrue(t);	
		now = LocalDateTime.now();  
		System.out.println("@"+dt.format(now)+" "+"dragged and dropped operator "+operator+" from unassigned panel to an occupied station "+dropDes+".");	
		
		//Confirm previous occupied station moved to unassigned.
		String c=driver.findElement(By.xpath("//div[@id='"+occupiedOperator+"']")).getAttribute("class");	
		Assert.assertTrue(c.contains("unassignedPanelHeader"));
		now = LocalDateTime.now(); 
		System.out.println("@"+dt.format(now)+" "+"Previous occupied operator "+occupiedOperator+ " is moved to unassigned panel.");
		
}
	public static void dragFromStationToEmptyStation(WebDriver driver,String operator, String sta) throws InterruptedException{
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
		Thread.sleep(30000);
		
		//Confirm operator dropped to proper station.
		ele1 = driver.findElement(By.xpath("//div[contains(@style,'"+operator+".jpg')]")); 
		parent = ele1.findElement(By.xpath("./.."));// find ele1 parent element
		Boolean t=parent.findElement(By.xpath("./div[1]")).getText().contains(dropDes);// find ele1 parent element's third td child element.	
		Assert.assertTrue(t);	
		now = LocalDateTime.now(); 
		System.out.println("@"+dt.format(now)+" "+"dragged and dropped operator "+operator+" from station to another empty station "+dropDes);	
}
	public static void dragFromStationToOccupiedStation(WebDriver driver,String operator, String sta, String occupiedOperator) throws InterruptedException{
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
		Thread.sleep(30000);
		
		//Confirm operator dropped to proper station.
		ele1 = driver.findElement(By.xpath("//div[contains(@style,'"+operator+".jpg')]")); 
		parent = ele1.findElement(By.xpath("./.."));// find ele1 parent element
		Boolean t=parent.findElement(By.xpath("./div[1]")).getText().contains(dropDes);// find ele1 parent element's third td child element.	
		Assert.assertTrue(t);	
		now = LocalDateTime.now(); 
		System.out.println("@"+dt.format(now)+" "+"dragged and dropped operator "+operator+" from station to another Occupied "+dropDes+" station.");	
		
		//Confirm previous occupied station moved to unassigned.
		String c=driver.findElement(By.xpath("//div[@id='"+occupiedOperator+"']")).getAttribute("class");	
		Assert.assertTrue(c.contains("unassignedPanelHeader"));
		now = LocalDateTime.now(); 
		System.out.println("@"+dt.format(now)+" "+"Previous occupied operator "+occupiedOperator+ " is moved to unassigned panel.");
				

}
	
	public static void dragFromStationToUnassigned(WebDriver driver,String operator) throws InterruptedException {
		DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  

		LocalDateTime now = LocalDateTime.now();
		
		WebElement target = driver.findElement(By.xpath("//div[@id='unassignedSec']"));
		
        // Assign(drag and drop) operator to that station.
		Actions a = new Actions(driver);
		WebElement source = driver.findElement(By.xpath("//*[contains(text(),'"+operator+"')]"));
		a.dragAndDrop(source, target).build().perform();
		Thread.sleep(30000);
		
		
		//Confirm previous occupied station moved to unassigned.
		String c=driver.findElement(By.xpath("//div[@id='"+operator+"']")).getAttribute("class");	
		Assert.assertTrue(c.contains("unassignedPanelHeader"));
		now = LocalDateTime.now();  
		System.out.println("@"+dt.format(now)+" "+"operator "+operator+ " is moved from station to  unassigned panel.");
				

	}
	

}
