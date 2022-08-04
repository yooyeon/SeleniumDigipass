package DPManage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ShiftCatalog {
	@Test

	public void mainShiftCatalog() throws InterruptedException, SQLException {
		//Delete TestYoo shift catalog in the db
		String UserName="sa";
		String Password="ChangeIt17";
		String serverName="10.172.86.53";
		String dbName="psi_management";
		String  DB_URL = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true";
		Connection con= DriverManager.getConnection( DB_URL,UserName, Password);		
		Statement s=con.createStatement();
		
		s.execute("delete from [psi_management].[dbo].[shift_breaks] where shift_catalog_id in (SELECT id FROM [psi_management].[dbo].[shift_catalog] where name like '%TestYoo%')");
		s.execute("delete from [psi_management].[dbo].[shift_catalog] where name like '%TestYoo%'");
		
		
		// Login to digipass management page
				System.setProperty("webdriver.chrome.driver", "C:\\yooyeon\\chromedriver.exe");
				WebDriver driver = new ChromeDriver();
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
				driver.get("http://setdev.ad.goodmanmfg.com/psimanagement/login");
				driver.manage().window().maximize();
				Thread.sleep(3000);
				driver.findElement(By.name("email")).sendKeys("yuyan.cui@goodmanmfg.com");
				driver.findElement(By.name("password")).sendKeys("111111");
				driver.findElement(By.cssSelector("button.form-submit")).click();
				Thread.sleep(5000);
				
				DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
				LocalDateTime now = LocalDateTime.now(); 
				
		//Navigate to shift catalog page.
				driver.findElement(By.cssSelector("span.MuiIconButton-label")).click();
				Thread.sleep(5000);
				driver.findElement(By.xpath("//*[text()='Shift Catalog']")).click();
				Thread.sleep(5000);
				
		//create new shift catalog
				driver.findElement(By.xpath("//span[text()='Add']")).click();
				Thread.sleep(3000);
				WebElement ele1 = driver.findElement(By.xpath("//div[@data-dx_placeholder='Enter Shift Name']"));
				WebElement parent = ele1.findElement(By.xpath("./.."));
				WebElement child = parent.findElement(By.xpath("./input[1]"));
				child.sendKeys("TestYoo");
				
				//Uncheck Active
				ele1 = driver.findElement(By.xpath("//*[text()='Active?']"));
				parent = ele1.findElement(By.xpath("./.."));
				child = parent.findElement(By.xpath("./div[2]/div[1]/div[1]/span[1]"));
				child.click();
				
				//shift duration set to 7 hour
				ele1 = driver.findElement(By.xpath("//*[text()='Shift Duration:']"));
				parent = ele1.findElement(By.xpath("./../../.."));
				child = parent.findElement(By.xpath("./div[4]/div[2]/i[1]"));
				child.click();
				Thread.sleep(2000);
				child.click();
				Thread.sleep(2000);
				
				//set number of breaks to 5
				ele1 = driver.findElement(By.xpath("//*[text()='Number of Breaks']"));
				parent = ele1.findElement(By.xpath("./../.."));
				child = parent.findElement(By.xpath("./div[2]/div[1]/i[1]"));
				child.click();
				Thread.sleep(2000);
				child.click();
				Thread.sleep(2000);
				child.click();
				Thread.sleep(2000);
				child.click();
				Thread.sleep(2000);
				child.click();
				Thread.sleep(2000);
				
				//set Example start Time to 2:00
				ele1 = driver.findElement(By.xpath("//div[text()='Example Start Time:']"));
				parent = ele1.findElement(By.xpath("./.."));
				child = parent.findElement(By.xpath("./div[2]/div[1]/div[1]/div[1]/div[1]/input[1]"));
				child.sendKeys(Keys.chord(Keys.CONTROL, "a"), "02:00");
				Thread.sleep(2000);
				
				//Delete 5th break
				ele1 = driver.findElement(By.xpath("//td[text()='5']"));
				parent = ele1.findElement(By.xpath("./.."));
				child = parent.findElement(By.xpath("./td[6]/div[1]/div[1]/i[1]"));
				child.click();
				Thread.sleep(2000);
				
				
//				//set 1st break offSset to 3:00
//				ele1 = driver.findElement(By.xpath("//td[text()='1']"));
//				parent = ele1.findElement(By.xpath("./.."));
//				child= parent.findElement(By.xpath("./td[4]/div[1]/div[1]/div[1]/div[1]/input[1]"));
//				child.sendKeys(Keys.chord(Keys.CONTROL, "a"), "03:00");
//				Thread.sleep(2000);
				
				
				
				//set 2nd break to cleanup & 6min & offSset to 1:00
				ele1 = driver.findElement(By.xpath("//td[text()='2']"));
				parent = ele1.findElement(By.xpath("./.."));
				child = parent.findElement(By.xpath("./td[2]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]"));
				child.click();
				driver.findElement(By.xpath("/html[1]/body[1]/div[3]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]")).click();
				child= parent.findElement(By.xpath("./td[3]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]"));
				child.click();
				Thread.sleep(2000);
				child= parent.findElement(By.xpath("./td[4]/div[1]/div[1]/div[1]/div[1]/input[1]"));
				child.sendKeys(Keys.chord(Keys.CONTROL, "a"), "01:00");
				Thread.sleep(2000);
				
				driver.findElement(By.xpath("//*[text()='Break Details']")).click();
				Thread.sleep(2000);
				
				child = parent.findElement(By.xpath("./td[5]/div[1]/div[1]/input[1]"));
				String st=child.getAttribute("value");
				Assert.assertEquals(st, "03:00");
				
				
				//3rd break to exercise & 4min& offSset to 2:00
				ele1 = driver.findElement(By.xpath("//td[text()='3']"));
				parent = ele1.findElement(By.xpath("./.."));
				child = parent.findElement(By.xpath("./td[2]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]"));
				child.click();
				driver.findElement(By.xpath("/html[1]/body[1]/div[3]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[3]/div[1]")).click();
				child= parent.findElement(By.xpath("./td[3]/div[1]/div[1]/div[2]/div[2]/div[2]/div[1]"));
				child.click();
				Thread.sleep(2000);
				child= parent.findElement(By.xpath("./td[4]/div[1]/div[1]/div[1]/div[1]/input[1]"));
				child.sendKeys(Keys.chord(Keys.CONTROL, "a"), "02:00");
				Thread.sleep(2000);
				
				driver.findElement(By.xpath("//*[text()='Break Details']")).click();
				Thread.sleep(2000);
				
				child = parent.findElement(By.xpath("./td[5]/div[1]/div[1]/input[1]"));
				st=child.getAttribute("value");
				Assert.assertEquals(st, "04:00");
				
				//4th break to lunch & 15min& offSset to 3:00
				ele1 = driver.findElement(By.xpath("//td[text()='4']"));
				parent = ele1.findElement(By.xpath("./.."));
				child = parent.findElement(By.xpath("./td[2]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]"));
				child.click();
				driver.findElement(By.xpath("/html[1]/body[1]/div[3]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[4]/div[1]")).click();
				child= parent.findElement(By.xpath("./td[3]/div[1]/div[1]/div[1]/input[1]"));
				child.click();
				Thread.sleep(2000);
				child.clear();
				Thread.sleep(2000);
				child.sendKeys("15");
				Thread.sleep(2000);
				child= parent.findElement(By.xpath("./td[4]/div[1]/div[1]/div[1]/div[1]/input[1]"));
				child.sendKeys(Keys.chord(Keys.CONTROL, "a"), "03:00");
				Thread.sleep(2000);
				
				driver.findElement(By.xpath("//*[text()='Break Details']")).click();
				Thread.sleep(2000);
				
				child = parent.findElement(By.xpath("./td[5]/div[1]/div[1]/input[1]"));
				st=child.getAttribute("value");
				Assert.assertEquals(st, "05:00");
				
				driver.findElement(By.xpath("//*[text()='Break Details']")).click();
				Thread.sleep(2000);
				
				driver.findElement(By.xpath("//span[text()='Save']")).click();
				Thread.sleep(5000);
				
		//Confirm TestYoo catalog is created successfully.
				driver.navigate().refresh();
				Thread.sleep(3000);
				ele1 = driver.findElement(By.xpath("//*[text()='In Use?']"));
				parent = ele1.findElement(By.xpath("./.."));
				child = parent.findElement(By.xpath("./div[2]/div[1]/div[1]/span[1]"));
				child.click();
				Thread.sleep(2000);
				
				ele1 = driver.findElement(By.xpath("//div[@data-dx_placeholder='Select...']"));
				parent = ele1.findElement(By.xpath("./.."));
				child = parent.findElement(By.xpath("./input[1]"));
				child.sendKeys("TestY");
				Thread.sleep(2000);
				int a=driver.findElements(By.xpath("//*[text()='TestYoo']")).size();
				Assert.assertEquals(1, a);
				
				
				now = LocalDateTime.now(); 
				System.out.println("@"+dt.format(now)+" "+"TestYoo shift catalog is created");	
				
				
				
				// check Active, and Discard changes.
				
				driver.findElement(By.xpath("//*[text()='TestYoo']")).click();
				Thread.sleep(2000);
				ele1 = driver.findElement(By.xpath("//*[text()='Active?']"));
				parent = ele1.findElement(By.xpath("./.."));
				child = parent.findElement(By.xpath("./div[2]/div[1]/div[1]/span[1]"));
				child.click();
				Thread.sleep(2000);
				child = parent.findElement(By.xpath("./div[2]/div[1]"));
				String v=child.getAttribute("aria-checked");
				Assert.assertEquals(v, "true");
				
				driver.findElement(By.xpath("//span[contains(text(),'Discard Changes')]")).click();
				Thread.sleep(2000);
				
				ResultSet rs= s.executeQuery("select * from [psi_management].[dbo].[shift_catalog] where name ='TestYoo'");
				rs.next(); 
				v=rs.getString("enabled");
				Assert.assertEquals(v, "0");
				
				now = LocalDateTime.now(); 
				System.out.println("@"+dt.format(now)+" "+"Discard button is working fine.");	
				
				
				// check Active, and save changes.
				driver.navigate().refresh();
				Thread.sleep(3000);
				ele1 = driver.findElement(By.xpath("//*[text()='In Use?']"));
				parent = ele1.findElement(By.xpath("./.."));
				child = parent.findElement(By.xpath("./div[2]/div[1]/div[1]/span[1]"));
				child.click();
				Thread.sleep(2000);
				
				ele1 = driver.findElement(By.xpath("//div[@data-dx_placeholder='Select...']"));
				parent = ele1.findElement(By.xpath("./.."));
				child = parent.findElement(By.xpath("./input[1]"));
				child.sendKeys("TestY");
				Thread.sleep(2000);
				driver.findElement(By.xpath("//*[text()='TestYoo']")).click();
				Thread.sleep(2000);
				
				ele1 = driver.findElement(By.xpath("//*[text()='Active?']"));
				parent = ele1.findElement(By.xpath("./.."));
				child = parent.findElement(By.xpath("./div[2]/div[1]/div[1]/span[1]"));
				child.click();
				Thread.sleep(2000);
				child = parent.findElement(By.xpath("./div[2]/div[1]"));
				v=child.getAttribute("aria-checked");
				Assert.assertEquals(v, "true");
				
				driver.findElement(By.xpath("//span[contains(text(),'Save')]")).click();
				Thread.sleep(5000);
				
				rs= s.executeQuery("select * from [psi_management].[dbo].[shift_catalog] where name ='TestYoo'");
				rs.next(); 
				v=rs.getString("enabled");
				Assert.assertEquals(v, "1");
				
				now = LocalDateTime.now(); 
				System.out.println("@"+dt.format(now)+" "+"Save button is working fine. updated TestYoo to Active.");	
				Thread.sleep(5000);
				
				// copy
				driver.findElement(By.xpath("//div[@aria-label='Copy']")).click();
				Thread.sleep(3000);
	
				parent = driver.findElement(By.cssSelector("div.copyPopupContainer"));
				child = parent.findElement(By.xpath("./div[2]/div[1]/div[1]"));
				child.click();
				Thread.sleep(3000);
				
				//Confirm TestYoo-Copy is created.
				driver.navigate().refresh();
				Thread.sleep(3000);
				ele1 = driver.findElement(By.xpath("//*[text()='In Use?']"));
				parent = ele1.findElement(By.xpath("./.."));
				child = parent.findElement(By.xpath("./div[2]/div[1]/div[1]/span[1]"));
				child.click();
				Thread.sleep(2000);
				
				ele1 = driver.findElement(By.xpath("//div[@data-dx_placeholder='Select...']"));
				parent = ele1.findElement(By.xpath("./.."));
				child = parent.findElement(By.xpath("./input[1]"));
				child.sendKeys("TestYoo - C");
				Thread.sleep(2000);
				a=driver.findElements(By.xpath("//*[text()='TestYoo - Copy']")).size();
				Assert.assertEquals(1, a);
				
				driver.findElement(By.xpath("//*[text()='TestYoo - Copy']")).click();
				Thread.sleep(2000);
				
				//confirm shift duration is 7:00.
				String sd=driver.findElement(By.cssSelector("span.squareButtonText")).getText();
				boolean t=sd.contains("07:00");
				Assert.assertEquals(t, true);
				
				ele1 = driver.findElement(By.xpath("//td[text()='4']"));
				parent = ele1.findElement(By.xpath("./.."));
				
				child = parent.findElement(By.xpath("./td[3]/div[1]/div[1]/div[1]/input[1]"));
				st=child.getAttribute("aria-valuenow");
				Assert.assertEquals(st, "15");
				
				child = parent.findElement(By.xpath("./td[4]/div[1]/div[1]/input[1]"));
				st=child.getAttribute("value");
				Assert.assertEquals(st, "03:00");
								
				
				now = LocalDateTime.now(); 
				System.out.println("@"+dt.format(now)+" "+"Copy is working fine.");	
				
				// delete
				driver.findElement(By.xpath("//span[contains(text(),'Delete')]")).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath("//*[text()='Yes']")).click();
				Thread.sleep(2000);
				
				//confirm can't find TestYoo-Copy
				driver.navigate().refresh();
				Thread.sleep(3000);
				ele1 = driver.findElement(By.xpath("//*[text()='In Use?']"));
				parent = ele1.findElement(By.xpath("./.."));
				child = parent.findElement(By.xpath("./div[2]/div[1]/div[1]/span[1]"));
				child.click();
				Thread.sleep(2000);
				
				ele1 = driver.findElement(By.xpath("//div[@data-dx_placeholder='Select...']"));
				parent = ele1.findElement(By.xpath("./.."));
				child = parent.findElement(By.xpath("./input[1]"));
				child.sendKeys("TestYoo - C");
				Thread.sleep(2000);
				a=driver.findElements(By.xpath("//*[text()='TestYoo - Copy']")).size();
				Assert.assertEquals(0, a);
				
				now = LocalDateTime.now(); 
				System.out.println("@"+dt.format(now)+" "+"Delete button is working fine.");	
				
				now = LocalDateTime.now(); 
				System.out.println("@"+dt.format(now)+" "+"Test pass!");				
				driver.quit();
				
				

	}

}
