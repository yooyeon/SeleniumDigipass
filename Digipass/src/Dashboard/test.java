package Dashboard;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.testng.annotations.Test;



public class test {

	public static void main(String[] args) throws InterruptedException, SQLException {
		  
		String operator="50781";
		DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  

		LocalDateTime now = LocalDateTime.now(); 
		
		
		//open resource pool page in new tab. and confirm displayed released operator.
		System.setProperty("webdriver.chrome.driver", "C:\\yooyeon\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));		
		driver.get("http://setdev.ad.goodmanmfg.com/digipassmanagement/resourcepool");
		driver.manage().window().maximize();
		driver.findElement(By.xpath("//input[@name='email']")).sendKeys("yuyan.cui@goodmanmfg.com");
		driver.findElement(By.xpath("//input[@name='password']")).sendKeys("111111");
		driver.findElement(By.xpath("//button[contains(text(),'Login')]")).click();
		Thread.sleep(30000);
				
	
		
		//Switch to resource pool page and find that Released operator and move to Kitting Outdoor.
		WebElement ele1 = driver.findElement(By.xpath("//*[text()='"+operator+"']")); 
		WebElement parent = ele1.findElement(By.xpath("./.."));
		parent.click();
	
	
		Thread.sleep(2000);
		
		 ele1 = driver.findElement(By.xpath("//div[contains(text(),'Receiving Department:')]")); // find element contains text "44122"
		 parent = ele1.findElement(By.xpath("./.."));// find ele1 parent element
		WebElement child = parent.findElement(By.xpath("./div[2]/div[1]/div[1]/div[1]/input[1]"));// find ele1 parent element's third td child element.	
		child.click();
		child.sendKeys("West Paint");
		Thread.sleep(2000);
		child.sendKeys("\n");
	
		driver.findElement(By.xpath("//button[contains(@class,'TransferPopup_save')]")).click();
		Thread.sleep(20000);
		driver.navigate().refresh();
	}
}