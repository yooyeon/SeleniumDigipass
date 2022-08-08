package DPManage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PSIWeight {
	@Test
	public  void mainPSIWeight() throws InterruptedException {
		// Login to digipass management page
		System.setProperty("webdriver.chrome.driver", "C:\\yooyeon\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get("http://setdev.ad.goodmanmfg.com/psimanagement/login");
		driver.manage().window().maximize();
		Thread.sleep(3000);
		driver.findElement(By.name("email")).sendKeys("yuyan.cui@goodmanmfg.com");
		driver.findElement(By.name("password")).sendKeys("111111");
		driver.findElement(By.cssSelector("button.form-submit")).click();
		Thread.sleep(8000);
		
		DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now(); 
		
		//Navigate to shift catalog page.
		driver.findElement(By.cssSelector("span.MuiIconButton-label")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//*[text()='PSI Family Configuration']")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//div[text()='PSI Weight %']")).click();
		Thread.sleep(5000);
		
	
		
		//Confirm DTTP/CN & DTTP/HP is listed .
		int si=driver.findElements(By.xpath("//td[text()='DTTP/CN']")).size();
		Assert.assertEquals(si, 1);
		si=driver.findElements(By.xpath("//td[text()='DTTP/HP']")).size();
		Assert.assertEquals(si, 1);
		
		now = LocalDateTime.now(); 
		System.out.println("@"+dt.format(now)+" "+"DTTP/CN & DTTP/HP is listed .");	
		
		//click on collapse all families button.
		driver.findElement(By.xpath("//div[@aria-label='Collapse all families']")).click();
		Thread.sleep(5000);
		si=driver.findElements(By.xpath("//td[text()='DTTP/CN']")).size();
		Assert.assertEquals(si, 0);
		now = LocalDateTime.now(); 
		System.out.println("@"+dt.format(now)+" "+"collapse all families button is working fine.");
		
		//click on expand all families button
		driver.findElement(By.xpath("//span[contains(text(),'Expand all families')]")).click();		
		Thread.sleep(5000);
		si=driver.findElements(By.xpath("//td[text()='DTTP/CN']")).size();
		Assert.assertEquals(si, 1);
		now = LocalDateTime.now(); 
		System.out.println("@"+dt.format(now)+" "+"expand all families  button is working fine.");
		
		
		
		//Test child psi sum can't be larger than 100.
		WebElement ele1 = driver.findElement(By.xpath("//*[text()='DTTP/CN']"));
		WebElement parent = ele1.findElement(By.xpath("./.."));
		WebElement child = parent.findElement(By.xpath("./td[3]"));
		int v1= Integer.parseInt(child.getText());
		
		int v=100-v1;
		int v2=v+1;
		
		ele1 = driver.findElement(By.xpath("//*[text()='DTTP/HP']"));
		parent = ele1.findElement(By.xpath("./.."));
		child = parent.findElement(By.xpath("./td[3]"));
		child.click();
		Thread.sleep(2000);
		
		ele1 = driver.findElement(By.xpath("//*[text()='DTTP/HP']"));
		parent = ele1.findElement(By.xpath("./.."));
		child = parent.findElement(By.xpath("./td[3]/div[1]/div[1]/div[1]/input[1]"));
		child.sendKeys(Keys.chord(Keys.CONTROL, "a"), String.valueOf(v2));
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[text()='Product']")).click();
		
		si=driver.findElements(By.xpath("//*[text()='Total sum of PSI avgs. exceed 100% . You must fix the PSI % in order to proceed.']")).size();
		Assert.assertEquals(si, 1);
		
		now = LocalDateTime.now(); 
		System.out.println("@"+dt.format(now)+" "+"If total sum of psi value larger than 100% will show error. pass!");
		
		Thread.sleep(3000);
		
		child.sendKeys(Keys.chord(Keys.CONTROL, "a"), String. valueOf(v));
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[text()='Product']")).click();
		
		si=driver.findElements(By.xpath("//*[text()='Data has been successfully updated']")).size();
		Assert.assertEquals(si, 1);
		
		now = LocalDateTime.now(); 
		System.out.println("@"+dt.format(now)+" "+"As long as user enter valid psi value. could be saved. pass!");
		
		//Test search .
				driver.findElement(By.xpath("//div[@class='dx-texteditor-input-container']/input[1]")).sendKeys("DTTP/LC-M");
				Thread.sleep(5000);
				si=driver.findElements(By.xpath("//span[text()='DTTP/LC-M']")).size();				
				Assert.assertEquals(si, 1);
				
				si=driver.findElements(By.xpath("//span[text()='DTTP/LC-S']")).size();
				Assert.assertEquals(si, 0);
				
				now = LocalDateTime.now(); 
				System.out.println("@"+dt.format(now)+" "+"search is working fine.");
				
				driver.findElement(By.cssSelector("span.dx-icon.dx-icon-clear")).click();
				Thread.sleep(5000);
				
		
		now = LocalDateTime.now(); 
		System.out.println("@"+dt.format(now)+" "+"Test pass!");				
		driver.quit();

	}

}
