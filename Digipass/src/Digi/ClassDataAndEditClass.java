package Digi;
import java.net.SocketException;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ClassDataAndEditClass  {

		@Test
	public void mainClassDataAndEditClass () throws InterruptedException {
		// TODO Auto-generated method stub
		// Need to run CreateClassAndEditGrade test first.
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
		
		// navigate to class data page 
		driver.findElement(By.cssSelector("i.fas.fa-book-open")).click();
		Thread.sleep(3000);
		
		// select core skill in skill type dropdown list.
		driver.findElement(By.xpath("//body/div[5]/div[1]/div[1]/div[1]")).click();
		Thread.sleep(1000);		
		WebElement ele1 = driver.findElement(By.xpath("//*[contains(text(),'Core Skill')]"));	
		WebElement parent = ele1.findElement(By.xpath("./../.."));// find ele1 parent element
		WebElement child = parent.findElement(By.xpath("./div[1]/div[1]/div[1]/span[1]"));// find ele1 parent element's third td child element.	
		child.click();
		driver.findElement(By.xpath("//body[1]/div[12]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]")).click();
		Thread.sleep(1000);	
		
		//select yuyan cui from instructor dropdown list
		driver.findElement(By.xpath("//body/div[7]/div[1]/div[1]/div[1]/input[1]")).sendKeys("yuyan");
		Thread.sleep(3000);	
		ele1 = driver.findElement(By.xpath("//*[contains(text(),'Yuyan Cui')]"));	
		parent = ele1.findElement(By.xpath("./../.."));// find ele1 parent element
		child = parent.findElement(By.xpath("./div[1]/div[1]/div[1]/span[1]"));// find ele1 parent element's third td child element.	
		child.click();
		driver.findElement(By.xpath("//body/div[12]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]")).click();
		Thread.sleep(1000);	
		
		// select 6S Training  in skill  dropdown list.
		driver.findElement(By.xpath("//body/div[6]/div[1]/div[1]/div[1]/input[1]")).click();
		Thread.sleep(1000);		
		ele1 = driver.findElement(By.xpath("//*[contains(text(),'6S Training')]"));	
		parent = ele1.findElement(By.xpath("./../.."));// find ele1 parent element
		child = parent.findElement(By.xpath("./div[1]/div[1]/div[1]/span[1]"));// find ele1 parent element's third td child element.	
		child.click();
		driver.findElement(By.xpath("//body/div[12]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]")).click();
		Thread.sleep(1000);	
		
		
	   // change to date time to today 11:59pm and submit	
		ele1 = driver.findElement(By.xpath("//div[@id='toClassSearch']"));	
		child = ele1.findElement(By.xpath("./div[1]/div[1]/div[2]/div[1]/div[1]/div[1]"));// find ele1 parent element's third td child element.	
		child.click();
		Thread.sleep(2000);	
		String h= driver.findElement(By.cssSelector("input[aria-label='hours']")).getAttribute("aria-valuenow");
		int hi =Integer.parseInt(h);
			while (hi!=11) {
			ele1 = driver.findElement(By.cssSelector("input[aria-label='hours']"));	
			parent = ele1.findElement(By.xpath("./.."));// find ele1 parent element
			child = parent.findElement(By.xpath("./div[2]/div[1]/div[1]/div[1]"));// find ele1 parent element's third td child element.				
			child.click();
			h= driver.findElement(By.cssSelector("input[aria-label='hours']")).getAttribute("aria-valuenow");
			hi =Integer.parseInt(h);
			}
		
		String m= driver.findElement(By.cssSelector("input[aria-label='minutes']")).getAttribute("aria-valuenow");
		int mi =Integer.parseInt(m);
			while (mi!=59) {
			ele1 = driver.findElement(By.cssSelector("input[aria-label='minutes']"));	
			parent = ele1.findElement(By.xpath("./.."));// find ele1 parent element
			child = parent.findElement(By.xpath("./div[2]/div[1]/div[1]/div[1]"));// find ele1 parent element's third td child element.				
			child.click();
			m= driver.findElement(By.cssSelector("input[aria-label='minutes']")).getAttribute("aria-valuenow");
			mi =Integer.parseInt(m);
			}
			
		ele1 = driver.findElement(By.cssSelector("input[aria-label='type']"));	
		parent = ele1.findElement(By.xpath("./.."));// find ele1 parent element	
		parent.click();
		driver.findElement(By.xpath("//div[contains(text(),'PM')]")).click();
		driver.findElement(By.xpath("//span[contains(text(),'Apply')]")).click();
		driver.findElement(By.xpath("//span[contains(text(),'Submit')]")).click();
		Thread.sleep(5000);	
		
		// make sure at lease one 6s training class is listed. and trainer is yuyan cui. total enrolled is 2 , student with grade is 2.
		ele1 = driver.findElement(By.cssSelector("tr[aria-rowindex='1']"));	
		String trainer = ele1.findElement(By.xpath("./td[1]")).getText();
		Assert.assertEquals(trainer, "Yuyan Cui");
		String skill = ele1.findElement(By.xpath("./td[2]")).getText();
		Assert.assertEquals(skill, "6S Training");
		String enrolled = ele1.findElement(By.xpath("./td[5]")).getText();
		Assert.assertEquals(enrolled, "2");
		String grade = ele1.findElement(By.xpath("./td[6]")).getText();
		Assert.assertEquals(grade, "2");
		System.out.println("class data page test Pass!");
		Thread.sleep(2000);
		
	   //navigate to edit class
		ele1.findElement(By.xpath("./td[8]/img")).click();
		Set<String> windows = driver.getWindowHandles();//[parentid, childid]
		Iterator<String> it = windows.iterator();
		String parentId= it.next();//will to go it 0 index
		String childId = it.next();
		driver.switchTo().window(childId);
		
		//Enroll two new operators in new created class
		Thread.sleep(2000);
		String Badge1 = "44122";
		String Badge2 = "54197";
		driver.findElement(By.xpath("//tbody/tr[1]/td[1]/div[1]/div[1]/div[1]/input[1]")).sendKeys(Badge1 + "\n");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//tbody/tr[1]/td[1]/div[1]/div[1]/div[1]/input[1]")).sendKeys(Badge2 + "\n");
		Thread.sleep(2000);
		List<WebElement> badge = driver.findElements(By.xpath("//tr/td[1]"));
		Boolean result = badge.stream().anyMatch(s -> s.getText().contains(Badge2));

		if (result == true) {
			System.out.println("Enroll operator function Pass!");
		} else {
			System.out.println("Enroll operator function failed.");
		}
		
		// Try delete operator badge1 44122 .
		ele1 = driver.findElement(By.xpath("//td[contains(text(),'" + Badge1 + "')]")); // find element contains text
																							// "44122"
		parent = ele1.findElement(By.xpath("./.."));// find ele1 parent element
		child = parent.findElement(By.xpath("./td[3]"));// find ele1 parent element's third td child element.
		child.click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//span[contains(text(),'Yes')]")).click();
		Thread.sleep(2000);
		String message = driver.findElement(By.cssSelector("div.dx-toast-message")).getText();
		System.out.println("This message appeared : " + message);
		Assert.assertEquals(message, "Employee has been successfully removed from class!");
		badge = driver.findElements(By.xpath("//tr/td[1]"));
		result = badge.stream().anyMatch(s -> s.getText().contains(Badge1));
		if (result == false) {
			System.out.println("Delete function Pass in edit class page!");
		} else {
			System.out.println("Delete function failed in edit class page.");
		}
		
		//navigate back to class data page. 
		driver.switchTo().window(parentId);
		
		//From class data page,  click on assign grade icon will open edit grade page. 
		ele1 = driver.findElement(By.cssSelector("tr[aria-rowindex='1']"));	
		ele1.findElement(By.xpath("./td[7]/img")).click();
		windows = driver.getWindowHandles();//[parentid, childid]
		it = windows.iterator();
		parentId= it.next();//will to go it 0 index
		String childId1 = it.next();
		String childId2 = it.next();
		driver.switchTo().window(childId2);
		String title=driver.findElement(By.id("pageTitle")).getText();
		Assert.assertEquals(title, "Trainer - Edit Grades");
		System.out.println("Could navigate to edit grade page from class data page.");
		driver.switchTo().window(parentId);
		
		driver.quit();
		
		
		
	}

}
