package Digi;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ScoreMetrics {

	@Test
	public  void mainScoreMetrics() throws InterruptedException, SQLException {
		
		// run in db: delete [score_metric] where name ='testScore'
		String UserName="sa";
		String Password="ChangeIt17";
		String serverName="10.172.86.53";
		String dbName="passport_sandbox";
        String  DB_URL = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true";
		Connection con= DriverManager.getConnection( DB_URL,UserName, Password);

		Statement s=con.createStatement();
		s.execute("delete [score_metric] where name ='testScore'");
		
		//Login to home page , and expand menu icons
		System.setProperty("webdriver.chrome.driver", "C:\\yooyeon\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));		 
		driver.get("http://setdev.ad.goodmanmfg.com/digipass/login.php");
		driver.manage().window().maximize();
		driver.findElement(By.id("emailNew")).sendKeys("yuyan.cui@goodmanmfg.com");
		driver.findElement(By.id("passwordNew")).sendKeys("111111");
		driver.findElement(By.id("submitBtn")).click();		
		//Thread.sleep(3000);
		driver.findElement(By.className("menu-open-button")).click();
		Thread.sleep(1000);
		
		//navigate to ScoreMetrics page
		driver.findElement(By.cssSelector("a.menu-item7.bgViolet.tooltip")).click();
		Thread.sleep(8000);
		driver.findElement(By.className("menu-open-button")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.fas.fa-traffic-light")).click();
		Thread.sleep(3000);
		
		//create new score metrics
		driver.findElement(By.cssSelector("div[aria-label='Add a Row']")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("input[id*='_name']")).sendKeys("testScore");
		driver.findElement(By.cssSelector("input[id*='_description']")).sendKeys("testScoreDesc");
		driver.findElement(By.cssSelector("input[id*='_min_value']")).sendKeys("0");
		driver.findElement(By.cssSelector("input[id*='_max_value']")).sendKeys("9");
		driver.findElement(By.cssSelector("label[for*='active']")).click();
		driver.findElement(By.xpath("//span[contains(text(),'Save')]")).click();
		Thread.sleep(3000);
		String message=driver.findElement(By.cssSelector("div.dx-toast-message")).getText();
		Assert.assertEquals(message, "Score metric stored successfully!");
		System.out.println("This message appeared : " + message);	
		Thread.sleep(3000);		
		
		//update new created skill
		driver.navigate().refresh();
		driver.findElement(By.cssSelector("input[aria-label='Search in data grid']")).sendKeys("testScore");
		Thread.sleep(2000);	
		driver.findElement(By.cssSelector("a.dx-link.dx-link-edit.dx-icon-edit")).click();
		Thread.sleep(3000);	
		driver.findElement(By.cssSelector("input[aria-valuenow='9']")).sendKeys("0");
		driver.findElement(By.cssSelector("label[for*='active']")).click();
		driver.findElement(By.xpath("//span[contains(text(),'Save')]")).click();
		Thread.sleep(3000);
		message=driver.findElement(By.cssSelector("div.dx-toast-message")).getText();
		Assert.assertEquals(message, "Score metric stored successfully!");
		System.out.println("This message appeared : " + message);	
		Thread.sleep(3000);	
		driver.navigate().refresh();
		String MaxValue =driver.findElement(By.xpath("//td[contains(text(),'90')]")).getText();
		Assert.assertEquals(MaxValue, "90");
		System.out.println("MaxValue updated to 90 successfully");	
		
		
		driver.quit();
		
	}

}
