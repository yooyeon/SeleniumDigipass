package Digi;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ManageOperators {

	@Test
	public void mainManageOperators() throws InterruptedException, SQLException {
	
		// Connect to DB and run:  update [operator] set badge_num='41278',alt_badge=null where badge_num='3333'
		String UserName="sa";
		String Password="ChangeIt17";
		String serverName="10.172.86.53";
		String dbName="passport_sandbox";
        String  DB_URL = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true";
		Connection con= DriverManager.getConnection( DB_URL,UserName, Password);

		Statement s=con.createStatement();
		s.execute("update [operator] set badge_num='41278',alt_badge=null where badge_num='3333'");
		
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
		
		//Navigate to manage operators page and update new badge for a operator
		driver.findElement(By.cssSelector("i.fas.fa-id-card-alt")).click();
		Thread.sleep(5000);
		driver.findElement(By.cssSelector("input[aria-label='Search in data grid']")).sendKeys("41278");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("a[title='Edit']")).click();
		Thread.sleep(1000);
		String newBadge= "3333";
		driver.findElement(By.xpath("//input[contains(@id,'_transfer_badge')]")).sendKeys(newBadge);
		
		//make sure go to skill button will open reports in new tab
		driver.findElement(By.xpath("//span[contains(text(),'Go to Skills Report')]")).click();
		Set<String> windows = driver.getWindowHandles();//[parentid, childid]
		Iterator<String> it = windows.iterator();
		String parentId= it.next();//will to go it 0 index
		String childId = it.next();
		driver.switchTo().window(childId);
		String opreator = driver.findElement(By.cssSelector("h2.count")).getText();
		Assert.assertEquals(opreator, "41278");
		System.out.println("Go to skills report button is working fine.");
		
		//go back to manage operator popup window save changes. 
		driver.switchTo().window(parentId);
		driver.findElement(By.xpath("//span[contains(text(),'Save')]")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//span[contains(text(),'Yes')]")).click();
		Thread.sleep(2000);
		String message =driver.findElement(By.cssSelector("div.dx-toast-message")).getText();
		//Assert.assertEquals(message, "Operator updated successfully!");
		System.out.println("This message appeared : " + message);
		

		//make sure new badge number listed
		
		List<WebElement> badge = driver.findElements(By.xpath("//tr/td[1]"));
		
		Boolean result= badge.stream().anyMatch( f->f.getText().contains(newBadge));
		
		if (result==true)
		{
		System.out.println("Test Pass!");
		}
		else 
		{
		System.out.println("Test failed.");
		}
		
		driver.quit();
		
	}

}
