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

public class SetdevRun {

	public static void main(String[] args) throws InterruptedException, SQLException {
		// Login to test WFD 
				System.setProperty("webdriver.chrome.driver", "C:\\yooyeon\\chromedriver.exe");
				WebDriver driver = new ChromeDriver();
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

				driver.get("https://goodmanglobalhold-uat.npr.mykronos.com/timekeeping#/timecard");
				
				driver.findElement(By.id("idToken1")).sendKeys("SETJIRA");
				Thread.sleep(1000);
				driver.findElement(By.id("idToken2")).sendKeys("goodman3");
				Thread.sleep(1000);
				driver.findElement(By.id("loginButton_0")).click();
				Thread.sleep(3000);
				
		String operator1 = "16440";		
		
		for(int j=1;j<=60;j++) {		
			search (driver, operator1);
			Thread.sleep(250000);
		
		}
		
		System.out.println("Test pass!");		
		//driver.quit();
		

	}
	
	public static void search (WebDriver driver,String operator) throws InterruptedException {
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
	}

}
