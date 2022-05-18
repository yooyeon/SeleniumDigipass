import java.net.SocketException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

public class OrganizationDashboardIcon {

	public static void main(String[] args) throws InterruptedException {
		// Need to run  ManageStation.java and stationSequencing.java first
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
		
		//expand manufacturing 
		WebElement	ele1 = driver.findElement(By.xpath("//div[contains(text(),'Manufacturing')]"));	
		WebElement	parent = ele1.findElement(By.xpath("./.."));
		WebElement	child = parent.findElement(By.xpath("./div[1]/div[1]"));	
		child.click();
		Thread.sleep(2000);
				
		//expand assembly
		ele1 = driver.findElement(By.xpath("//div[contains(text(),'Assembly')]"));	
		parent = ele1.findElement(By.xpath("./.."));
		child = parent.findElement(By.xpath("./div[1]/div[2]"));	
		child.click();
		Thread.sleep(2000);	
		
		//Open aac05 station sequencing window.
		ele1 = driver.findElement(By.xpath("//div[contains(text(),'AAC05')]"));	
		parent = ele1.findElement(By.xpath("./../.."));
		child = parent.findElement(By.xpath(".//td[3]/div[4]/div[1]"));	
		child.click();
		Thread.sleep(2000);
		
		//click on Fan Top SA tab from station sequencing popup window
		driver.switchTo().frame("sequenceIframe");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//h4[contains(text(),'Fan Top SA')]")).click();
		Thread.sleep(2000);
		
		//get sequence number for testStation2 & testStation3
		ele1 = driver.findElement(By.xpath("//td[contains(text(),'testStation2')]"));	
		parent = ele1.findElement(By.xpath("./.."));
		child = parent.findElement(By.xpath(".//td[6]"));	
		int s2 = Integer.parseInt(child.getText());
		ele1 = driver.findElement(By.xpath("//td[contains(text(),'testStation3')]"));	
		parent = ele1.findElement(By.xpath("./.."));
		child = parent.findElement(By.xpath(".//td[6]"));	
		int s3 = Integer.parseInt(child.getText());
		System.out.println("testStation2 got sequence " + s2 +" and testStation3 got sequence " + s3);
		driver.switchTo().defaultContent();
		driver.findElement(By.xpath("//div[contains(text(),'Station Sequencing')]")).click();
		driver.findElement(By.cssSelector("i.dx-icon.dx-icon-close")).click();
		Thread.sleep(2000);
		
		//Click on AAC05 dashboard icon
		ele1 = driver.findElement(By.xpath("//div[contains(text(),'AAC05')]"));	
		parent = ele1.findElement(By.xpath("./../.."));
		child = parent.findElement(By.xpath(".//td[3]/div[5]/div[1]"));	
		child.click();
		Thread.sleep(3000);
		
		// Confirm testStation2 & testStation3 are displayed with correct sequence.
		ele1 = driver.findElement(By.xpath("//div[contains(text(),'testStation2')]"));	
		parent = ele1.findElement(By.xpath("./.."));
		child = parent.findElement(By.xpath(".//div[2]"));	
		int ss2 = Integer.parseInt(child.getText());
		Assert.assertEquals(ss2, s2);
		ele1 = driver.findElement(By.xpath("//div[contains(text(),'testStation3')]"));	
		parent = ele1.findElement(By.xpath("./.."));
		child = parent.findElement(By.xpath(".//div[2]"));	
		int ss3 = Integer.parseInt(child.getText());
		Assert.assertEquals(ss3, s3);
		System.out.println("In dashboard testStation2 sequence is " + ss2 +" and testStation3 sequence is " + ss3);
		System.out.println("Test pass! ");
		
		
		driver.quit();
		

	}

}
