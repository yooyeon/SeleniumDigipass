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

public class OperatorTypes {
	@Test
	public void mainOperatorTypes() throws InterruptedException, SQLException {

		// run in db:   delete [operator_type] where name ='TestOperatorType'
		String UserName="sa";
		String Password="ChangeIt17";
		String serverName="10.172.86.53";
		String dbName="passport_sandbox";
        String  DB_URL = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true";
		Connection con= DriverManager.getConnection( DB_URL,UserName, Password);

		Statement s=con.createStatement();
		s.execute(" delete [operator_type] where name ='TestOperatorType'");
		
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
				
				//navigate to Operator Types page
				driver.findElement(By.cssSelector("a.menu-item7.bgViolet.tooltip")).click();
				Thread.sleep(5000);
				driver.findElement(By.className("menu-open-button")).click();
				Thread.sleep(1000);
				driver.findElement(By.cssSelector("a.menu-item1.bgRedOrange.tooltip")).click();
				Thread.sleep(3000);
				driver.findElement(By.cssSelector("div[aria-label='Add a Row']")).click();
				driver.findElement(By.xpath("//input[contains(@id,'name')]")).sendKeys("TestOperatorType");
				driver.findElement(By.xpath("//input[contains(@id,'description')]")).sendKeys("TestOTdesc");
				driver.findElement(By.xpath("//span[contains(text(),'Save')]")).click();
				Thread.sleep(5000);
				
				//search TestOperatorType and confirm it is created successfully.
				driver.findElement(By.cssSelector("input[aria-label='Search in data grid']")).sendKeys("TestOperatorType");
				String operatorType= driver.findElement(By.xpath("//span[contains(text(),'TestOperatorType')]")).getText();
				Assert.assertEquals(operatorType, "TestOperatorType");
				System.out.println("Operator Type is created successfully.");
				Thread.sleep(3000);
				String i;
				//i= driver.findElement(By.cssSelector("div.dx-datagrid-checkbox-size.dx-checkbox.dx-state-readonly.dx-widget")).getAttribute("aria-checked");
				//System.out.println(i);
				
				//Update new created operator type
				driver.findElement(By.cssSelector("a.dx-link.dx-link-edit.dx-icon-edit")).click();
				Thread.sleep(3000);
				driver.findElement(By.cssSelector("[class='dx-datagrid-edit-form-item dx-first-col dx-last-col dx-field-item dx-col-0 dx-field-item-optional dx-flex-layout dx-label-h-align'] [class='dx-field-item-content dx-field-item-content-location-right'] [class='dx-checkbox dx-checkbox-checked dx-widget dx-datagrid-validator dx-validator dx-visibility-change-handler']")).click();
				driver.findElement(By.xpath("//span[contains(text(),'Save')]")).click();
				Thread.sleep(3000);
				i= driver.findElement(By.cssSelector("div.dx-datagrid-checkbox-size.dx-checkbox.dx-state-readonly.dx-widget")).getAttribute("aria-checked");
				//System.out.println(i);
				Assert.assertEquals(i, "false");			
				System.out.println("Operator type updated to inactive successfully");
				
				driver.quit();
				
				
				
	}

}
