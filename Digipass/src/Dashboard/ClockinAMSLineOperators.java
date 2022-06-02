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

public class ClockinAMSLineOperators {

	public static void main(String[] args) throws InterruptedException, SQLException {
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
		
		//Navigate to a line dashboard.
		String dept="AAC07";
		driver.findElement(By.cssSelector("i.fas.fa-tachometer-alt")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//h4[contains(text(),'Manufacturing')]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//h4[contains(text(),'Assembly')]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//h4[contains(text(),'"+dept+"')]")).click();
		Thread.sleep(5000);
		int i=driver.findElements(By.xpath("//div[@id='departmentDash']")).size();
		Assert.assertTrue(i>0);
		
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
		
		// Get today's date 
		//DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String date3 = dateFormat.format(date);
		System.out.println("today is : "+date3);
		
		
		// Connect to DB and confirm operators are inserted in dashboard_data or unassigned table
				String UserName="sa";
				String Password="ChangeIt17";
				String serverName="10.172.86.53";
				String dbName="passport_sandbox";
				String  DB_URL = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true";
				Connection con= DriverManager.getConnection( DB_URL,UserName, Password);		
				Statement s=con.createStatement();
						
				ResultSet rs= s.executeQuery("with t as (\r\n"
						+ "SELECT row_number() over(\r\n"
						+ "partition by badge order by queued_time \r\n"
						+ "	) as row_num, *\r\n"
						+ "FROM [passport_demo].[dbo].[operator_status] ) \r\n"
						+ "						\r\n"
						+ " select top(15) * from t where t.row_num=1   and \r\n"
						+ "badge  in (\r\n"
						+ "\r\n"
						+ "SELECT badge\r\n"
						+ "  FROM [passport_sandbox].[dbo].[operator_schedules] WHERE scheduled_day='"+date3+"'\r\n"
						+ "  and department_id in (select id from department where name ='"+dept+"')\r\n"
						+ " and  badge not in (select badge from unassigned_operators)\r\n"
						+ " and badge not in (select badge  from dashboard_data where badge is not null) \r\n"
						+ ")\r\n"
						+ " ") ;
				
			
	
		
		// Call clock in method three times to clock in two operators.
		String[] operator = new String[6];
		for (int j = 0; j < 6; j++) {	
			rs.next();		
			operator[j]=rs.getNString("badge");	
			
		}
		
		clockIn(driver, operator[0]);
		clockIn(driver, operator[1]);
		clockIn(driver, operator[2]);
		clockIn(driver, operator[3]);
		clockIn(driver, operator[4]);
		
		//switch tab ,and Wait 3min to get clocked in entry .
				Set<String> windows = driver.getWindowHandles();//[parentid, childid]
				Iterator<String> it = windows.iterator();
				String parentId= it.next();//will to go it 0 index
				String childId = it.next();
				driver.switchTo().window(parentId);
				driver.navigate().refresh();
				System.out.println("Wait for 3min...");
				//Thread.sleep(20000);	
				Thread.sleep(200000);	
			
		// move all operator to unassigned panel		
		rs= s.executeQuery("SELECT  * FROM [passport_sandbox].[dbo].[dashboard_data] where badge in ('"+operator[0]+"','"+operator[1]+"','"+operator[2]+"','"+operator[3]+"','"+operator[3]+"') and department_id in  (select id from department where name ='"+dept+"') ");
		
		while(rs.next()) {
			String o=rs.getNString("badge");
			// call method to move operator to unassigned
			 dragFromStationToUnassigned(driver,o);
		} 
			
		Thread.sleep(20000);	
		// Now all the operators are in unassgined. 
		// Try to drag  one operator to a station.
		rs= s.executeQuery("SELECT  * FROM [passport_sandbox].[dbo].[unassigned_operators] where department_id in  (select id from department where name ='"+dept+"') and can_work_stationIds !='' ");
		rs.next();
		String op=rs.getNString("badge");
		String sta=rs.getNString("can_work_stationIds");
		String[] station = sta.split("\\|");// split by '|' and after split it will return array.
		String des = station[0].trim();// will remove space
		rs= s.executeQuery("select * from station where id ='"+des+"'");
		rs.next();
		String sta1=rs.getNString("name");

		dragFromUnassignedToStation(driver,op, sta1);
		
		
		
		System.out.println("Test pass!");		
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
				driver.findElement(By.xpath("//button[@title='Timecard']")).click();
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
				System.out.println("dragged and dropped operator "+operator+" to "+dropDes+" station.");	
	}
	public static void dragFromUnassignedToOccupiedStation(WebDriver driver,String operator, String sta, String occupiedOperator) throws InterruptedException{
		
		
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
		System.out.println("dragged and dropped operator "+operator+" from unassigned panel to an occupied station "+dropDes+".");	
		
		//Confirm previous occupied station moved to unassigned.
		String c=driver.findElement(By.xpath("//div[@id='"+occupiedOperator+"']")).getAttribute("class");	
		Assert.assertTrue(c.contains("unassignedPanelHeader"));
		System.out.println("Previous occupied operator "+occupiedOperator+ " is moved to unassigned panel.");
		
}
	public static void dragFromStationToEmptyStation(WebDriver driver,String operator, String sta) throws InterruptedException{
		
		
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
		System.out.println("dragged and dropped operator "+operator+" from station to another empty station "+dropDes);	
}
	public static void dragFromStationToOccupiedStation(WebDriver driver,String operator, String sta, String occupiedOperator) throws InterruptedException{
		
		
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
		System.out.println("dragged and dropped operator "+operator+" from station to another Occupied "+dropDes+" station.");	
		
		//Confirm previous occupied station moved to unassigned.
		String c=driver.findElement(By.xpath("//div[@id='"+occupiedOperator+"']")).getAttribute("class");	
		Assert.assertTrue(c.contains("unassignedPanelHeader"));
		System.out.println("Previous occupied operator "+occupiedOperator+ " is moved to unassigned panel.");
				

}
	
	public static void dragFromStationToUnassigned(WebDriver driver,String operator) throws InterruptedException {
	
		WebElement target = driver.findElement(By.xpath("//div[@id='unassignedSec']"));
		
        // Assign(drag and drop) operator to that station.
		Actions a = new Actions(driver);
		WebElement source = driver.findElement(By.xpath("//*[contains(text(),'"+operator+"')]"));
		a.dragAndDrop(source, target).build().perform();
		Thread.sleep(20000);
		
		
		//Confirm previous occupied station moved to unassigned.
		String c=driver.findElement(By.xpath("//div[@id='"+operator+"']")).getAttribute("class");	
		Assert.assertTrue(c.contains("unassignedPanelHeader"));
		System.out.println("operator "+operator+ " is moved  from station to  unassigned panel.");
				

	}
	

}
