package Digi;
import java.net.SocketException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SkillOperatorType {

	@Test
	public void mainSkillOperatorType() throws InterruptedException {

		// Login to home page , and expand menu icons
		System.setProperty("webdriver.chrome.driver", "C:\\yooyeon\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get("http://setdev.ad.goodmanmfg.com/digipass/login.php");
		driver.manage().window().maximize();
		driver.findElement(By.id("emailNew")).sendKeys("yuyan.cui@goodmanmfg.com");
		driver.findElement(By.id("passwordNew")).sendKeys("111111");
		driver.findElement(By.id("submitBtn")).click();
		// Thread.sleep(3000);
		driver.findElement(By.className("menu-open-button")).click();
		Thread.sleep(1000);

		// navigate to skills to operator type page
		driver.findElement(By.cssSelector("a.menu-item7.bgViolet.tooltip")).click();
		Thread.sleep(5000);
		driver.findElement(By.className("menu-open-button")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("i.fas.fa-user-tag")).click();
		Thread.sleep(3000);
		
		//select operator type 'Al Brazer 1'
		driver.findElement(By.xpath("//body[1]/div[4]/div[1]/div[1]/div[1]/div[1]/input[1]")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[contains(text(),'Al Brazer 1')]")).click();
		driver.findElement(By.cssSelector("div.w3-padding.w3-mobile")).click();
		
		//click to add new skill to operator type 'Al Brazer 1'
		driver.findElement(By.cssSelector("i.dx-icon.dx-icon-edit-button-addrow")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("input[id*='_Skill ID']")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[contains(text(),'AMS Basic')]")).click();
		driver.findElement(By.cssSelector("input[id*='_Days to Complete Skill']")).sendKeys("5");
		driver.findElement(By.cssSelector("input[id*='_Skill Renewal Time']")).sendKeys("4");
		driver.findElement(By.cssSelector("input[id*='_Skill Refresh Time']")).sendKeys("3");
		driver.findElement(By.xpath("//span[contains(text(),'Save')]")).click();
		Thread.sleep(3000);
		
		//Search new added skill and Edit new added skill
		driver.findElement(By.cssSelector("input[aria-label='Search in data grid']")).sendKeys("AMS Basic");
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("a.dx-link.dx-link-edit.dx-icon-edit")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("input[id*='_Days to Complete Skill']")).sendKeys("5");
		driver.findElement(By.xpath("//span[contains(text(),'Save')]")).click();
		Thread.sleep(3000);
		
		//Search new added skill make sure updated new change 
		driver.findElement(By.cssSelector("input[aria-label='Search in data grid']")).sendKeys("AMS Basic");
		Thread.sleep(1000);
		String v= driver.findElement(By.xpath("//td[contains(text(),'55')]")).getText();
		Assert.assertEquals(v, "55");
		System.out.println("New added skill got updated successfully!");
		
		//Delete new added skill from operator type 'Al Brazer 1'
		driver.findElement(By.cssSelector("a[title='Delete']")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//span[contains(text(),'Yes')]")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("input[aria-label='Search in data grid']")).sendKeys("a");
		driver.findElement(By.cssSelector("span.dx-icon.dx-icon-clear")).click();
		Thread.sleep(1000);
		
		//make sure no deleted skill listed
		
		List<WebElement> skill = driver.findElements(By.xpath("//tr/td[1]"));
		
		Boolean result= skill.stream().anyMatch(s->s.getText().contains("AMS Basic"));
		
		if (result==false)
		{
		System.out.println("skill deleted successfully!");
		}
		else 
		{
		System.out.println("Test failed.");
		}
		
		driver.quit();
	}

}
