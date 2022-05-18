import java.net.SocketException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

public class SetFinanceGroup {

	public static void main(String[] args) throws InterruptedException, SocketException {
		// TODO Auto-generated method stub
		// Run in db:   delete [station_groups] where name ='testGroup' 
		//              delete [station_subgroup] where name ='test'		  
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
		
		//Open aac05 Set Finance Group popup window.
		ele1 = driver.findElement(By.xpath("//div[contains(text(),'AAC05')]"));	
		parent = ele1.findElement(By.xpath("./../.."));
		child = parent.findElement(By.xpath("./td[3]/div[2]/div[1]"));	
		child.click();
		Thread.sleep(2000);
		
		//Confirm set finance group popup window presented 
		int s=driver.findElements(By.xpath("//div[contains(text(),'Set Finance Group')]")).size();	
		Assert.assertEquals(s, 1);
		
		
		//Add a station Group
		driver.findElement(By.xpath("//span[contains(text(),'Add Station Group')]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@id='txtGroupName']/div[1]/input[1]")).sendKeys("testGroup");
		driver.findElement(By.xpath("//div[@id='txtGroupDesc']/div[1]/input[1]")).sendKeys("testGroupDesc");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//span[contains(text(),'Confirm')]")).click();
		Thread.sleep(1000);
		String message=driver.findElement(By.cssSelector("div.dx-toast-message")).getText();
		Assert.assertEquals(message, "Station Group stored successfully!");
		System.out.println("This message appeared : " + message);
		Thread.sleep(5000);
		
		
		//Add a new finance group for aac05 with new created station group
		driver.findElement(By.xpath("//body[1]/div[9]/div[1]/div[1]/div[2]/div[1]/div[1]/div[4]/div[1]/div[1]/div[3]/div[1]/div[1]/div[1]/div[1]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//tbody/tr[1]/td[2]/div[1]/div[1]/div[1]/div[1]/input[1]")).sendKeys("testGroup");
		driver.findElement(By.xpath("//div[contains(text(),'testGroup')]")).click();
		driver.findElement(By.xpath("//tbody/tr[1]/td[4]/div[1]/div[1]/div[1]/div[1]/input[1]")).click();
		driver.findElement(By.xpath("//tbody/tr[1]/td[4]/div[1]/div[1]/div[1]/div[1]/input[1]")).sendKeys("1");
		driver.findElement(By.xpath("//div[@class='dx-item dx-list-item dx-state-focused']/div[1]")).click();//Enter shift 1
		Thread.sleep(2000);		
		driver.findElement(By.xpath("//tbody/tr[1]/td[5]/div[1]/div[1]/div[1]/div[1]/input[1]")).click();
		driver.findElement(By.xpath("//tbody/tr[1]/td[5]/div[1]/div[1]/div[1]/div[1]/input[1]")).sendKeys("DL");
		driver.findElement(By.xpath("//div[@class='dx-item dx-list-item dx-state-focused']/div[1]")).click();//enter labor type as DL
		Thread.sleep(2000);
		driver.findElement(By.xpath("//tbody/tr[1]/td[3]/div[1]/div[1]/div[1]/input[1]")).sendKeys("111A");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("a.dx-link.dx-link-save.dx-icon-save")).click();
		Thread.sleep(1000);
		message=driver.findElement(By.cssSelector("div.dx-toast-message")).getText();
		Assert.assertEquals(message, "Records have been successfully updated!");
		System.out.println("This message appeared : " + message);
		System.out.println("New finance group added successfully!");
		Thread.sleep(3000);
		
		//close finance group popup window and reopen aac05 finance group popup window
		driver.findElement(By.cssSelector("i.dx-icon.dx-icon-close")).click();
		Thread.sleep(1000);
		ele1 = driver.findElement(By.xpath("//div[contains(text(),'AAC05')]"));	
		parent = ele1.findElement(By.xpath("./../.."));
		child = parent.findElement(By.xpath("./td[3]/div[2]/div[1]"));	
		child.click();
		Thread.sleep(2000);	
		
		//edit new added finance group
		ele1 = driver.findElement(By.xpath("//td[contains(text(),'testGroup')]"));	
		parent = ele1.findElement(By.xpath("./.."));
		child = parent.findElement(By.xpath("./td[6]/a[1]"));	
		child.click();driver.findElement(By.xpath("//td[@aria-colindex='3']/div[1]/div[1]/div[@class='dx-texteditor-container']/input[1]")).clear();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//td[@aria-colindex='3']/div[1]/div[1]/div[@class='dx-texteditor-container']/input[1]")).sendKeys("112A");
		driver.findElement(By.xpath("//a[@title='Save']")).click();
		Thread.sleep(1000);
		message=driver.findElement(By.cssSelector("div.dx-toast-message")).getText();
		Assert.assertEquals(message, "Records have been successfully updated!");
		System.out.println("This message appeared : " + message);
		System.out.println("edited new added finance group." );
		
		
		//close finance group popup window and reopen aac05 finance group popup window
		driver.findElement(By.cssSelector("i.dx-icon.dx-icon-close")).click();
		Thread.sleep(1000);
		ele1 = driver.findElement(By.xpath("//div[contains(text(),'AAC05')]"));	
		parent = ele1.findElement(By.xpath("./../.."));
		child = parent.findElement(By.xpath("./td[3]/div[2]/div[1]"));	
		child.click();
		Thread.sleep(2000);	
		
		
		//delete new added finance group
		ele1 = driver.findElement(By.xpath("//td[contains(text(),'testGroup')]"));	
		parent = ele1.findElement(By.xpath("./.."));
		child = parent.findElement(By.xpath("./td[6]/a[2]"));	
		child.click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//span[contains(text(),'Yes')]")).click();
		Thread.sleep(1000);
		message=driver.findElement(By.cssSelector("div.dx-toast-message")).getText();
		Assert.assertEquals(message, "Records have been successfully updated!");
		System.out.println("This message appeared : " + message);
		System.out.println("Deleted new added finance group." );
		
		
		// Add station subgroup
		driver.findElement(By.xpath("//span[contains(text(),'Add Station Subgroup')]")).click();
		Thread.sleep(2000);	
		ele1 = driver.findElement(By.xpath("//div[contains(text(),'Drag a column header here to group by that column')] "));	
		parent = ele1.findElement(By.xpath("./../../../../.."));
		child = parent.findElement(By.xpath("./div[3]/div[1]/div[1]/div[1]/div[1]"));	
		child.click();
		driver.findElement(By.xpath("//td[contains(@class,'dx-focused')]/div[1]/div[1]/div[1]/input[1]")).sendKeys("test");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//a[@title='Save']")).click();
		Thread.sleep(2000);

		
		//search new added subgroup, and confirm subgroup added successfully
		ele1 = driver.findElement(By.xpath("//div[contains(text(),'Drag a column header here to group by that column')] "));	
		parent = ele1.findElement(By.xpath("./../../../../.."));
		child = parent.findElement(By.xpath("./div[3]/div[3]/div[1]/div[1]/div[1]/input[1]"));	
		child.sendKeys("test");
		Thread.sleep(1000);
		s=driver.findElements(By.xpath("//span[contains(text(),'test')]")).size();
		Assert.assertEquals(s, 1);
		System.out.println("Added new subgroup successfully!" );
		
		
		driver.quit();
		
		
		
	}

}
