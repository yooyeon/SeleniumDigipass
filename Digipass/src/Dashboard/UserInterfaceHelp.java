package Dashboard;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

public class UserInterfaceHelp {

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
		DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  

		LocalDateTime now = LocalDateTime.now(); 
		
		//Navigate to a line dashboard.
		String dept="AAC07";
		driver.findElement(By.cssSelector("i.fas.fa-tachometer-alt")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//h4[contains(text(),'Manufacturing')]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//h4[contains(text(),'Assembly')]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//h4[contains(text(),'"+dept+"')]")).click();
		Thread.sleep(10000);
		int i=driver.findElements(By.xpath("//div[@id='departmentDash']")).size();
		Assert.assertTrue(i>0);
		
		//Click on Help icon on top .
		driver.findElement(By.cssSelector("i.fas.fa-info")).click();
		Thread.sleep(3000);
		
		//Confirm pop up window appeared.
		int s=driver.findElements(By.id("helpHolder")).size();
		Assert.assertEquals(1, s);
		now = LocalDateTime.now(); 
		System.out.println("@"+dt.format(now)+" "+"Help popup window appeared.");				

		
		//Click on Main button and confirm first row.
		driver.findElement(By.xpath("//div[@id='helpMenu']/div[text()='Main']")).click();
		Thread.sleep(2000);
		s=driver.findElements(By.xpath("//span[contains(text(),'not currently scheduled to run')]")).size();
		Assert.assertEquals(1, s);
		now = LocalDateTime.now(); 
		System.out.println("@"+dt.format(now)+" "+"Main tab is tested");		
		
		//click on Stations button and confirm first row.
		driver.findElement(By.xpath("//div[@id='helpMenu']/div[text()='Stations']")).click();
		Thread.sleep(2000);
		s=driver.findElements(By.xpath("//span[contains(text(),'Freshman person (less than 2 weeks)')]")).size();
		Assert.assertEquals(1, s);
		now = LocalDateTime.now(); 
		System.out.println("@"+dt.format(now)+" "+"Stations tab is tested");		
		
		//click on people status button and confirm first row.
		driver.findElement(By.xpath("//div[contains(text(),'People Status')]")).click();
		Thread.sleep(2000);
		s=driver.findElements(By.xpath("//span[contains(text(),'last station is filled')]")).size();
		Assert.assertEquals(1, s);
		now = LocalDateTime.now(); 
		System.out.println("@"+dt.format(now)+" "+"People Status tab is tested");		
		
		//click on Attendance button and confirm first row.
		driver.findElement(By.xpath("//div[@id='helpMenu']/div[text()='Attendance']")).click();
		Thread.sleep(2000);
		s=driver.findElements(By.xpath("//span[contains(text(),'Person shows clocked-out')]")).size();
		Assert.assertEquals(1, s);
		now = LocalDateTime.now(); 
		System.out.println("@"+dt.format(now)+" "+"Attendance tab is tested");
		
		//click on People Actions button and one of the row.
		driver.findElement(By.xpath("//div[contains(text(),'People Actions')]")).click();
		Thread.sleep(2000);
		s=driver.findElements(By.xpath("//span[contains(text(),'Release person/people to resource pool')]")).size();
		Assert.assertEquals(1, s);
		now = LocalDateTime.now(); 
		System.out.println("@"+dt.format(now)+" "+"People Actions tab is tested");
		
		//click on Errors button and confirm first row.
		driver.findElement(By.xpath("//div[@id='helpMenu']/div[text()='Errors']")).click();
		Thread.sleep(2000);
		s=driver.findElements(By.xpath("//span[contains(text(),'There is a network issue (if using WiFi, move to a')]")).size();
		Assert.assertEquals(1, s);
		now = LocalDateTime.now(); 
		System.out.println("@"+dt.format(now)+" "+"Errors tab is tested");
		
		//click on close button.and confirm help popup window closed. 
		driver.findElement(By.xpath("//div[@id='helpMenu']/div[text()='Close']")).click();
		Thread.sleep(3000);
		now = LocalDateTime.now(); 
		System.out.println("@"+dt.format(now)+" "+"Help popup window is closed");
		
		now = LocalDateTime.now(); 
		System.out.println("@"+dt.format(now)+" "+"Test pass!");				
		driver.quit();

	}

}
