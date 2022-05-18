package Digi;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RegisterTrainer {

	@Test
	public void mainRegisterTrainer() throws InterruptedException, SQLException {
	
		// need to run in db:   delete[trainer] where badge_num=1111
		String UserName="sa";
		String Password="ChangeIt17";
		String serverName="10.172.86.53";
		String dbName="passport_sandbox";
        String  DB_URL = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true";
		Connection con= DriverManager.getConnection( DB_URL,UserName, Password);

		Statement s=con.createStatement();
		s.execute("delete[trainer] where badge_num=1111");
		
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
				
				// navigate to register new trainer page 
				driver.findElement(By.cssSelector("i.fas.fa-book-open")).click();
				Thread.sleep(2000);
				driver.findElement(By.cssSelector("label.menu-open-button")).click();
				Thread.sleep(1000);
				driver.findElement(By.cssSelector("i.fas.fa-address-card")).click();
				Thread.sleep(3000);
				
				//add new trainer
				driver.findElement(By.cssSelector("i.dx-icon.dx-icon-edit-button-addrow")).click();
				Thread.sleep(2000);
				driver.findElement(By.cssSelector("input[id*='_badge_num']")).sendKeys("1111");
				driver.findElement(By.cssSelector("input[id*='_first_name']")).sendKeys("test");
				driver.findElement(By.cssSelector("input[id*='_last_name']")).sendKeys("selenium");
				driver.findElement(By.cssSelector("input[id*='_email']")).sendKeys("testSelenium@goodmanmfg.com");
				driver.findElement(By.xpath("//*[contains(text(),'Save')]")).click();
				Thread.sleep(2000);
				
				//search new created trainer, to make sure new trainer created successfully.
				driver.findElement(By.cssSelector("input[aria-label='Search in data grid']")).sendKeys("1111");
				Thread.sleep(1000);
				String trainer = driver.findElement(By.xpath("//*[contains(text(),'1111')]")).getText();
				Assert.assertEquals(trainer, "1111");
				System.out.println("New trainer created successfully!");
				
				//Edit new created trainer to make it active
				driver.findElement(By.cssSelector("a.dx-link.dx-link-edit.dx-icon-edit")).click();
				Thread.sleep(1000);
				WebElement ele1 = driver.findElement(By.xpath("//span[contains(text(),'Active?:')]"));	
				WebElement parent = ele1.findElement(By.xpath("./../../.."));
				WebElement child = parent.findElement(By.xpath("./div[1]/div[1]"));	
				child.click();				
				driver.findElement(By.xpath("//*[contains(text(),'Save')]")).click();
				Thread.sleep(2000);
				
				//verify new trainer is displayed as active.
				String result= driver.findElement(By.cssSelector("div[role='checkbox']")).getAttribute("aria-checked");
				Assert.assertEquals(result, "1");
				System.out.println("new trainer updated to active, so test pass!");
				
				driver.quit();
				
				
				

	}

}
