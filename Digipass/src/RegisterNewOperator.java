import java.net.SocketException;	
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;



public class RegisterNewOperator {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		// run in db:   delete[operator] where badge_num='44122'
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
		
		//navigate to operator register page
		driver.findElement(By.cssSelector("a.menu-item6.bgGreen.tooltip")).click();
		driver.findElement(By.id("badge")).sendKeys("44122");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("h4[data-i18n='general_SubmitButton']")).click();
		Thread.sleep(3000);
	 	String message=driver.findElement(By.cssSelector("div.dx-toast-message")).getText();
		Assert.assertEquals(message, "Operator 44122 not found");
		System.out.println("This message appeared : " + message);
		//Thread.sleep(10000);
		
		//from popup window enter first and last name
		driver.findElement(By.name("First Name")).sendKeys("yuyan");
		driver.findElement(By.name("Last Name")).sendKeys("cui");
		driver.findElement(By.cssSelector("div.dx-dropdowneditor-icon")).click();
		driver.findElement(By.cssSelector("td.dx-calendar-cell.dx-calendar-today.dx-calendar-contoured-date")).click();
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("div[aria-label='Register']")).click();
		Thread.sleep(3000);
		message=driver.findElement(By.cssSelector("div.dx-toast-message")).getText();
		Assert.assertEquals(message, "Operator created successfully!");
		System.out.println("This message appeared : " + message);
		
		//Reenter badge again.
		driver.findElement(By.id("badge")).sendKeys("44122");
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("h4[data-i18n='general_SubmitButton']")).click();
		Thread.sleep(3000);
		message =driver.findElement(By.cssSelector("div.dx-toast-message")).getText();
		Assert.assertEquals(message, "Operator 44122 found");
		System.out.println("This message appeared : " + message);
		System.out.println("Testing passed ");
		
		driver.quit();
		

	}

}
