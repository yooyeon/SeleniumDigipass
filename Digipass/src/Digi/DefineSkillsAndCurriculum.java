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

public class DefineSkillsAndCurriculum {
	@Test
	public void mainDefineSkillsAndCurriculum() throws InterruptedException, SQLException {
 
		// Connect to DB and run:  delete skill where name ='TestSkill'
				String UserName="sa";
				String Password="ChangeIt17";
				String serverName="10.172.86.53";
				String dbName="passport_sandbox";
		        String  DB_URL = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true";
				Connection con= DriverManager.getConnection( DB_URL,UserName, Password);

				Statement s=con.createStatement();
				s.execute("delete skill where name ='TestSkill'");
				
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
		
		//navigate to skill page and create new skill
		driver.findElement(By.cssSelector("a.menu-item7.bgViolet.tooltip")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("i.dx-icon.dx-icon-edit-button-addrow")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("div.dx-popup-content")).click();
		driver.findElement(By.cssSelector("input[id*='name']")).sendKeys("TestSkill");
		driver.findElement(By.cssSelector("input[id*='description']")).sendKeys("TestDescription");
		driver.findElement(By.cssSelector("input[id*='skill_type_id']")).click();
		driver.findElement(By.xpath("//div[contains(text(),'Operations & Management Skill')]")).click();
		driver.findElement(By.cssSelector("input[id*='score_metric_id']")).click();
		driver.findElement(By.xpath("//div[contains(text(),'\"100 Point Scale\" : 0 to 100 points')]")).click();
		driver.findElement(By.cssSelector("input[id*='pass_min']")).sendKeys("60");
		driver.findElement(By.xpath("//span[contains(text(),'Save')]")).click();
		Thread.sleep(5000);
		//search skill and confirm skill is created successfully.
		driver.findElement(By.cssSelector("input[aria-label='Search in data grid']")).sendKeys("TestSkill");
		
		String skill= driver.findElement(By.className("dx-datagrid-search-text")).getText();
		Assert.assertEquals(skill, "TestSkill");
		System.out.println("skill is created successfully");
		driver.findElement(By.xpath("//tbody/tr[1]/td[13]/a[1]")).click();
		Thread.sleep(5000);
		//update skill pass_min to 70
		driver.findElement(By.cssSelector("input[id*='pass_min']")).clear();
		driver.findElement(By.cssSelector("input[id*='pass_min']")).sendKeys("70");	
		driver.findElement(By.cssSelector("label[for*='pass_min']")).click();
		driver.findElement(By.cssSelector("div[aria-label='Save']")).click();
		Thread.sleep(3000);
		//check whether updated successfully
		String min_pass= driver.findElement(By.xpath("//td[contains(text(),'70')]")).getText();
		Assert.assertEquals(min_pass, "70");
		System.out.println("Pass_Min updated successfully");
		System.out.println("Testing passed ");		
		
		driver.quit();
	}

}
