package Digi;
import java.net.SocketException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class stationSequencing {

	@Test
	public  void mainstationSequencing() throws InterruptedException {
		// Need to run  ManageStation.java first
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
		
		//confirm right after sequence window appeared, it assigned sequence to testStation2 & testStation3
		String message=driver.findElement(By.cssSelector("div.dx-toast-message")).getText();
		Assert.assertEquals(message, "SS-1 - Records updated successfully!");
		System.out.println("This message appeared : " + message);
		Thread.sleep(3000);

		
		//Confirm one of the new added station assigned sequence.
		ele1 = driver.findElement(By.xpath("//td[contains(text(),'testStation2')]"));	
		parent = ele1.findElement(By.xpath("./.."));
		child = parent.findElement(By.xpath(".//td[6]"));	
		int s2 = Integer.parseInt(child.getText());
		Assert.assertTrue(s2>0);
		System.out.println("testStation2 got sequence " + s2);
		
		//drag teststation3 to above teststation2
		Actions a = new Actions(driver);
		WebElement source = driver.findElement(By.xpath("//td[contains(text(),'testStation3')]"));
		WebElement target = driver.findElement(By.xpath("//td[contains(text(),'testStation2')]"));
		//drag and drop 
		a.dragAndDrop(source, target).build().perform();
		Thread.sleep(3000);
		//save change
		driver.findElement(By.cssSelector("i.dx-icon.dx-icon-save")).click();
		Thread.sleep(1000);
		message=driver.findElement(By.cssSelector("div.dx-toast-message")).getText();
		Assert.assertEquals(message, "SS-1 - Records updated successfully!");
		System.out.println("This message appeared : " + message);
		Thread.sleep(3000);
		
		
		//confirm now teststation3 sequence is Teststation2 previous sequence s.
		ele1 = driver.findElement(By.xpath("//td[contains(text(),'testStation3')]"));	
		parent = ele1.findElement(By.xpath("./.."));
		child = parent.findElement(By.xpath(".//td[6]"));
		int s3 = Integer.parseInt(child.getText());
		Assert.assertTrue(s3==s2);
		System.out.println("testStation3 sequence changed to " + s3 +".so drag and drop succeed.");
		
		//select teststation2 and teststation3 to do bulk change
		ele1 = driver.findElement(By.xpath("//td[contains(text(),'testStation3')]"));	
		parent = ele1.findElement(By.xpath("./.."));
		child = parent.findElement(By.xpath(".//td[1]/div[1]/div[1]"));	
		child.click();
		Thread.sleep(1000);
		ele1 = driver.findElement(By.xpath("//td[contains(text(),'testStation2')]"));	
		parent = ele1.findElement(By.xpath("./.."));
		child = parent.findElement(By.xpath(".//td[1]/div[1]/div[1]"));	
		child.click();
		Thread.sleep(1000);
		
		//bulk change
		driver.findElement(By.cssSelector("i.dx-icon.dx-icon-edit")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@class='dx-texteditor-container']/input[1]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[contains(text(),'Kansei')]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//span[contains(text(),'Confirm')]")).click();
		Thread.sleep(1000);
		message=driver.findElement(By.cssSelector("div.dx-toast-message")).getText();
		Assert.assertEquals(message, "SS-1 - Records updated successfully!");
		System.out.println("This message appeared : " + message);
		Thread.sleep(3000);
		
		//confirm teststation2 and teststation3 subgroup changed to Kansei
		ele1 = driver.findElement(By.xpath("//td[contains(text(),'testStation3')]"));	
		parent = ele1.findElement(By.xpath("./.."));
		child = parent.findElement(By.xpath(".//td[5]"));	
		String sg3=child.getText();
		Assert.assertEquals(sg3, "Kansei");
		ele1 = driver.findElement(By.xpath("//td[contains(text(),'testStation2')]"));	
		parent = ele1.findElement(By.xpath("./.."));
		child = parent.findElement(By.xpath(".//td[5]"));	
		String sg2=child.getText();
		Assert.assertEquals(sg2, "Kansei");
		System.out.println("bulk change succeed.");
				
		
		driver.quit();
				
		
		
	}

}
