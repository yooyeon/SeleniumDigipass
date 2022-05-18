package Digi;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.testng.Assert.assertEquals;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class OrganizationManagement {

	@Test
	public  void mainOrganizationManagement() throws InterruptedException, SQLException {
		// Connect to DB and run:  delete [department] where name ='testDept'
		String UserName="sa";
		String Password="ChangeIt17";
		String serverName="10.172.86.53";
		String dbName="passport_sandbox";
        String  DB_URL = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true";
		Connection con= DriverManager.getConnection( DB_URL,UserName, Password);

		Statement s=con.createStatement();
		s.execute(" delete [department] where name ='testDept'");
		
		
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
		
		//Navigate to organization page
		driver.findElement(By.cssSelector("i.far.fa-sitemap")).click();
		Thread.sleep(3000);
		
		//Add a department
		driver.findElement(By.cssSelector("i.dx-icon.far.fa-plus-square")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//body/div[18]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[4]/div[1]/div[1]/div[3]/div[1]/div[1]/div[1]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//tbody/tr[1]/td[1]/div[1]/div[1]/div[1]/input[1]")).sendKeys("testDept"+ "\n");
		Thread.sleep(1000);
		String message=driver.findElement(By.cssSelector("div.dx-toast-message")).getText();
		Assert.assertEquals(message, "Record has been updated");
		System.out.println("This message appeared : " + message);
		Thread.sleep(3000);
		
		//search new created dept and delete it 
		driver.findElement(By.xpath("//body[1]/div[18]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]/div[4]/div[1]/div[1]/div[3]/div[3]/div[1]/div[1]/div[1]/input[1]")).sendKeys("testDept");
		Thread.sleep(3000);
		driver.findElement(By.xpath("//a[contains(text(),'Delete')]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//span[contains(text(),'Yes')]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@aria-label='Yes']")).click();
		Thread.sleep(2000);
		message=driver.findElement(By.cssSelector("div.dx-toast-message")).getText();
		Assert.assertEquals(message, "Record has been updated");
		System.out.println("This message appeared : " + message);
		System.out.println("Deleted new created dept successfully." );
		Thread.sleep(2000);
		driver.findElement(By.xpath("/html[1]/body[1]/div[18]/div[1]/div[1]/div[1]/div[3]/div[1]/div[1]/div[1]/div[1]")).click();
		Thread.sleep(2000);
		
		
		//create new organization
		driver.findElement(By.id("addnewOrgBtn")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//input[contains(@id,'_name')]")).sendKeys("testOrg");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[contains(@id,'org_layers_id')]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[contains(text(),'Business Unit')]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//span[contains(text(),'Save')]")).click();

		
		// check whether new created org presented in the page
		int i = driver.findElements(By.xpath("//div[contains(text(),'testOrg')]")).size();
		Assert.assertEquals(i, 1);
		System.out.println("New organization created successfully!");
		Thread.sleep(5000);
		
		//delete new created org
		WebElement ele1 = driver.findElement(By.xpath("//div[contains(text(),'testOrg')]"));	
		WebElement parent = ele1.findElement(By.xpath("./../.."));// find ele1 parent element
		WebElement child = parent.findElement(By.xpath("./td[3]/div[2]/div[1]"));	
		child.click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//span[contains(text(),'Yes')]")).click();
		Thread.sleep(2000);
		message=driver.findElement(By.cssSelector("div.dx-toast-message")).getText();
		Assert.assertEquals(message, "Record has been updated");
		System.out.println("This message appeared : " + message);
		
		// make sure new org is removed from page
		i = driver.findElements(By.xpath("//div[contains(text(),'testOrg')]")).size();
		Assert.assertEquals(i, 0);
		System.out.println("deleted new org successfully!");
		
		
		
		//expand manufacturing 
		ele1 = driver.findElement(By.xpath("//div[contains(text(),'Manufacturing')]"));	
		parent = ele1.findElement(By.xpath("./.."));
		child = parent.findElement(By.xpath("./div[1]/div[1]"));	
		child.click();
		Thread.sleep(2000);
		
		//make sure AAC05 is not presented
		int si=driver.findElements(By.xpath("//div[contains(text(),'AAC05')]")).size();
		Assert.assertEquals(si, 0);
		
		
		//expand assembly
		ele1 = driver.findElement(By.xpath("//div[contains(text(),'Assembly')]"));	
		parent = ele1.findElement(By.xpath("./.."));
		child = parent.findElement(By.xpath("./div[1]/div[2]"));	
		child.click();
		Thread.sleep(2000);
		
		//make sure AAC05 is presented 
		String dept=driver.findElement(By.xpath("//div[contains(text(),'AAC05')]")).getText();	
		Assert.assertEquals(dept, "AAC05");
		System.out.println("AAC05 is listed in organization page");
		
		driver.quit();
		
		
		
		
	}

}
