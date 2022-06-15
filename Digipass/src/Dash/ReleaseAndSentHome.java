package Dash;

import java.time.Duration;
import java.util.Iterator;
import java.util.Set;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ReleaseAndSentHome {

	@Test
	public void mainReleaseAndSentHome( ) throws InterruptedException, SQLException {
		// Before testing , open an AMS line dashboard , find an operator in dashboard
		// station or unassigned panel.and specify dept & operator in the script.

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

		// Navigate to a AMS line dashboard.
		String dept = "AAC07";


		driver.findElement(By.cssSelector("i.fas.fa-tachometer-alt")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//h4[contains(text(),'Manufacturing')]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//h4[contains(text(),'Assembly')]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//h4[contains(text(),'" + dept + "')]")).click();
		Thread.sleep(10000);
		int i = driver.findElements(By.xpath("//div[@id='departmentDash']")).size();
		Assert.assertTrue(i > 0);

		// Connect to DB and find latest clock in entry's ID for selected operator
		String UserName = "sa";
		String Password = "ChangeIt17";
		String serverName = "10.172.86.53";
		String dbName = "passport_sandbox";
		String DB_URL = "jdbc:sqlserver://" + serverName + ":1433;DatabaseName=" + dbName
				+ ";encrypt=true;trustServerCertificate=true";
		Connection con = DriverManager.getConnection(DB_URL, UserName, Password);
		Statement s = con.createStatement();
		ResultSet rs= s.executeQuery("SELECT TOP (1) * FROM [passport_sandbox].[dbo].[unassigned_operators] where department_id in  (select id from department where name ='"+dept+"') and status not in ('RTP','RTPPEND') ");
		rs.next(); 
		String operator=rs.getString("badge");;

		rs = s.executeQuery("SELECT TOP (1) * FROM [passport_sandbox].[dbo].[operator_status] where badge ="
				+ operator + " order by queued_time desc");
		rs.next();
		String id1 = rs.getString("id");
		System.out.println("latest record id is: " + id1);
		
		
		// Find operator from dashboard, then right click , and release him
		Actions actions = new Actions(driver);
		WebElement w = driver.findElement(By.xpath("//*[contains(text(),'" + operator + "')]"));
		actions.contextClick(w).perform();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@id='sendToPool']/div[1]/i[1]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@aria-label='close']")).click();
		Thread.sleep(7000);

		// confirm operator moved to unassigned operator panel with red color release-to-pool icon
		String status = driver.findElement(By.xpath("//div[@id='" + operator + "']")).getAttribute("alt");
		Assert.assertTrue(driver.findElement(By.xpath("//div[@id='" + operator + "']")).getAttribute("class")
				.contains("unassignedPanelHeader"));
		System.out.println("status is " + status);

		Assert.assertTrue(driver.findElement(By.xpath("//div[contains(@style,'" + operator + ".jpg')]/div[1]/i[1]"))
				.getAttribute("class").contains("SIcon SETicon-sendToPool SETiconsRedContrasted"));
		System.out.println("operator displayed in unassigned panel with RTPPEND Red icon.");

		rs = s.executeQuery("Select * from unassigned_operators where badge=" + operator);
		rs.next();
		String st = rs.getString("status");
		Assert.assertEquals(st, "RTPPEND");
		System.out.println("operator status is RTPPEND in unassigned operator table.");

		// open resource pool page in new tab. and confirm displayed released operator.
		driver.switchTo().newWindow(WindowType.TAB);
		driver.get("http://setdev.ad.goodmanmfg.com/digipassmanagement/resourcepool");
		driver.manage().window().maximize();
		driver.findElement(By.xpath("//input[@name='email']")).sendKeys("yuyan.cui@goodmanmfg.com");
		driver.findElement(By.xpath("//input[@name='password']")).sendKeys("111111");
		driver.findElement(By.xpath("//button[contains(text(),'Login')]")).click();
		Thread.sleep(15000);

		Set<String> windows = driver.getWindowHandles();// [parentid, childid]
		Iterator<String> it = windows.iterator();
		String parentId = it.next();// will to go it 0 index
		String childId = it.next();
		driver.switchTo().window(childId);

		int si = driver.findElements(By.xpath("//*[contains(text(),'" + operator + "')]")).size();
		Assert.assertEquals(si, 1);

		Assert.assertTrue(
				driver.findElement(By.xpath("//div[contains(@style,'" + operator + ".jpg')]/div[4]/div[1]/div[1]/*[1]"))
						.getAttribute("class").contains("MuiCircularProgress-svg"));

		System.out.println("operator appeared in resource pool page with yellow loading icon.");
		System.out.println("Wait for 3min...");

		// Wait for 3min. then confirm in the db, clock in entry inserted with wc= 00004 and isReleasedPool is 1.
		Thread.sleep(180000);
		rs = s.executeQuery("SELECT TOP (1) * FROM [passport_sandbox].[dbo].[operator_status] where badge =" + operator
				+ " order by queued_time desc");
		rs.next();
		String id2 = rs.getString("id");
		Assert.assertTrue(Integer.parseInt(id2) > Integer.parseInt(id1));
		String wc = rs.getString("org_path");
		Assert.assertTrue(wc.contains("00004"));

		String clock = rs.getString("clock_in_clock_name");
		Assert.assertEquals(clock, "Manager External API");

		String f = rs.getString("isInReleasedPool");
		Assert.assertEquals(f, "1");
		System.out.println("new clock in entry is inserted with wc 00004 and isInReleasedPool is 1.");

		// In unassigned_operators table, operator status updated to RTP
		rs = s.executeQuery("Select * from unassigned_operators where badge=" + operator);
		rs.next();
		st = rs.getString("status");
		Assert.assertEquals(st, "RTP");
		System.out.println("operator status is RTP in unassigned operator table.");

		// In dashboard operator changed to RTP status with yellow icon.
		driver.switchTo().window(parentId);
		Assert.assertTrue(driver.findElement(By.xpath("//div[contains(@style,'" + operator + ".jpg')]/div[1]/i[1]"))
				.getAttribute("class").contains("SIcon SETicon-sendToPool SETiconsYellowContrasted"));
		System.out.println("operator displayed in unassigned panel with RTP yellow icon.");
		Thread.sleep(2000);
		
		// Switch to resource pool page and confirm operator showing with green bar.

		driver.switchTo().window(childId);
		driver.navigate().refresh();
		Thread.sleep(20000);
		Assert.assertTrue(driver.findElement(By.xpath("//*[text()='"+operator+"']/../../div[1]")).getAttribute("style").contains("green")) ;
		System.out.println("Operator department is in green bar");
		
		// And in pull operator popup window , send to home button is unavailable.
		WebElement ele1 = driver.findElement(By.xpath("//*[text()='" + operator + "']"));
		WebElement parent = ele1.findElement(By.xpath("./.."));
		parent.click();

		Thread.sleep(2000);
		i=driver.findElements(By.cssSelector("svg.svg-inline--fa.fa-house-user.fa-w-18 ")).size();
		Assert.assertEquals(i, 0);
		System.out.println("In pull operator popup window, send to home button is unavailable");
		driver.navigate().refresh();

		//wait for 20min so operator showing with red bar
		System.out.println("Send to home button is unavailable , need to Wait for 20min...");
		Thread.sleep(1200000);
		
		driver.navigate().refresh();
		Thread.sleep(20000);
		Assert.assertTrue(driver.findElement(By.xpath("//*[text()='"+operator+"']/../../div[1]")).getAttribute("style").contains("red")) ;
		System.out.println("Operator department is in red bar");
		
		// Send operator to home.
		driver.navigate().refresh();
		Thread.sleep(20000);
		ele1 = driver.findElement(By.xpath("//*[text()='" + operator + "']"));
		parent = ele1.findElement(By.xpath("./.."));
		parent.click();

		Thread.sleep(2000);
        driver.findElement(By.cssSelector("svg.svg-inline--fa.fa-house-user.fa-w-18 ")).click();
		Thread.sleep(7000);
		driver.navigate().refresh();

		// right after send to home, confirm operator disappeared from resource pool page.
		i = driver.findElements(By.xpath("//*[contains(text(),'" + operator + "')]")).size();
		Assert.assertTrue(i == 0);
		System.out.println("After sent operator to home, Operator is removed from resource pool page.");
		
		//In unassigned_operators table, operator got removed from unassigned table..
		rs = s.executeQuery("Select * from unassigned_operators where badge=" + operator);
		Assert.assertFalse(rs.next()) ;
		System.out.println("operator got removed from unassigned operator table.");
		
		// In dashboard can't find operator.
		driver.switchTo().window(parentId);
		driver.navigate().refresh();
		Thread.sleep(10000);
		i = driver.findElements(By.xpath("//*[contains(text(),'" + operator + "')]")).size();
		Assert.assertEquals(i, 0);
		System.out.println("operator is removed from dashboad unassigned panel");

		// In db. previous rtp inserted entry , isInReleasedPool set to 0 and isSentHome column updated to 1.
		rs= s.executeQuery("SELECT TOP (1) * FROM [passport_sandbox].[dbo].[operator_status] where badge ="+operator+" order by queued_time desc");
		rs.next(); 
		String id3= rs.getString("id");
		Assert.assertTrue(id3.equals(id2));
		f=rs.getString("isInReleasedPool");
		Assert.assertEquals(f, "0");
		String h=rs.getString("isSentHome");
		Assert.assertEquals(h, "1");
		System.out.println("previous RPT inserted clock in entry isInReleasedPool set to 0 and isSentHome column updated to 1. ");
		
		System.out.println("Test pass!");		
		driver.quit();
	}

}
