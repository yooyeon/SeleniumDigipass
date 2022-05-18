package Dashboard;
import java.time.Duration;
import java.net.SocketException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

public class DragAndDrop {

	public static void main(String[] args) throws InterruptedException {
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
		
		//Navigate to a AMS line dashboard.
		String dept="AVR01";
		driver.findElement(By.cssSelector("i.fas.fa-tachometer-alt")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//h4[contains(text(),'Manufacturing')]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//h4[contains(text(),'Assembly')]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//h4[contains(text(),'"+dept+"')]")).click();
		Thread.sleep(3000);
		int i=driver.findElements(By.xpath("//div[@id='departmentDash']")).size();
		Assert.assertTrue(i>0);
		
		
		
		
		System.out.println("Test pass!");		
		driver.quit();
		

	}

}
