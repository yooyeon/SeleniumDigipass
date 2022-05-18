package Digi;
import java.net.SocketException;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CreateClassAndEditGrade {
	
	@Test
	public void  mainCreateClassAndEditGrade () throws InterruptedException {
		// TODO Auto-generated method stub
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

				// Navigate to create class page and create 6s training class.
				driver.findElement(By.cssSelector("a.menu-item2.bgBlueGreen.tooltip")).click();
				Thread.sleep(5000);
				driver.findElement(By.xpath("//input[contains(@id,'_skillType')]")).click();
				driver.findElement(By.xpath("//*[contains(text(),'Core Skill')]")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//input[contains(@id,'_Skills')]")).click();
				driver.findElement(By.xpath("//*[contains(text(),'6S Training ')]")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//*[contains(text(),'Register Employees')]")).click();
				Thread.sleep(3000);

				// Enroll operators in new created class
				String Badge1="55368";
				String Badge2 ="44122";
				String Badge3 ="55143";
				driver.findElement(By.xpath("//tbody/tr[1]/td[1]/div[1]/div[1]/div[1]/input[1]")).sendKeys(Badge1 + "\n");
				Thread.sleep(3000);
				driver.findElement(By.xpath("//tbody/tr[1]/td[1]/div[1]/div[1]/div[1]/input[1]")).sendKeys(Badge2 + "\n");
				Thread.sleep(3000);
				driver.findElement(By.xpath("//tbody/tr[1]/td[1]/div[1]/div[1]/div[1]/input[1]")).sendKeys(Badge3 + "\n");
				Thread.sleep(3000);
				List<WebElement> badge = driver.findElements(By.xpath("//tr/td[1]"));
				Boolean result = badge.stream().anyMatch(s -> s.getText().contains(Badge2));

				if (result == true) {
					System.out.println("Enroll operator function Pass!");
				} else {
					System.out.println("Enroll operator function failed.");
				}

			

				// Try delete operator 44122 .
				WebElement ele1 = driver.findElement(By.xpath("//td[contains(text(),'44122')]")); // find element contains text
																									// "44122"
				WebElement parent = ele1.findElement(By.xpath("./.."));// find ele1 parent element
				WebElement child = parent.findElement(By.xpath("./td[3]"));// find ele1 parent element's third td child element.
				child.click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//span[contains(text(),'Yes')]")).click();
				Thread.sleep(2000);
				String message = driver.findElement(By.cssSelector("div.dx-toast-message")).getText();
				System.out.println("This message appeared : " + message);
				Assert.assertEquals(message, "Employee has been successfully removed from class!");
				badge = driver.findElements(By.xpath("//tr/td[1]"));
				result = badge.stream().anyMatch(s -> s.getText().contains(Badge2));
				if (result == false) {
					System.out.println("Delete function Pass!");
				} else {
					System.out.println("Delete function failed.");
				}
				
				//Make sure could navigate to Assign grade page
				driver.findElement(By.cssSelector("i.dx-icon.fas.fa-book-open")).click();
				Set<String> windows = driver.getWindowHandles();//[parentid, childid]
				Iterator<String> it = windows.iterator();
				String parentId= it.next();//will to go it 0 index
				String childId = it.next();
				driver.switchTo().window(childId);
				String title = driver.findElement(By.xpath("//h2[contains(text(),'Trainer - Edit Grades')]")).getText();
				Assert.assertEquals(title, "Trainer - Edit Grades");
				System.out.println("navigate to Assign grade function Pass!");
				Thread.sleep(3000);
				
				// Assign grade : grade 0 to badge Badge1, grade 1 to badge Badge3
				WebElement ele2 = driver.findElement(By.xpath("//td[contains(text(),'" + Badge1 + "')]")); 
				WebElement parent2 = ele2.findElement(By.xpath("./.."));
				WebElement child2 = parent2.findElement(By.xpath("./td[12]"));
				child2.click();
				Thread.sleep(1000);
				ele2 = driver.findElement(By.xpath("//td[contains(text(),'" + Badge1 + "')]")); 
				parent2 = ele2.findElement(By.xpath("./.."));
				child2 = parent2.findElement(By.xpath("./td[12]/div[1]/div[1]/div[1]/input[1]"));
				child2.sendKeys("0"+ "\n");
				Thread.sleep(1000);
				
				WebElement ele3 = driver.findElement(By.xpath("//td[contains(text(),'" + Badge3 + "')]")); 
				WebElement parent3 = ele3.findElement(By.xpath("./.."));
				WebElement child3 = parent3.findElement(By.xpath("./td[12]"));
				child3.click();
				Thread.sleep(1000);
				ele3 = driver.findElement(By.xpath("//td[contains(text(),'" + Badge3 + "')]")); 
				parent3 = ele3.findElement(By.xpath("./.."));
				child3 = parent3.findElement(By.xpath("./td[12]/div[1]/div[1]/div[1]/input[1]"));
				child3.sendKeys("1"+ "\n");
				Thread.sleep(1000);				

				//Make sure after grade assigned, result fail displayed to badge Badge1, pass displayed for badge Badge3
				ele2 = driver.findElement(By.xpath("//td[contains(text(),'" + Badge1 + "')]")); 
				parent2 = ele2.findElement(By.xpath("./.."));
				child2 = parent2.findElement(By.xpath("./td[14]"));
				String r= child2.getText();
				Assert.assertEquals(r, "Failed");
				
				ele3 = driver.findElement(By.xpath("//td[contains(text(),'" + Badge3 + "')]")); 
				parent3 = ele3.findElement(By.xpath("./.."));
				child3 = parent3.findElement(By.xpath("./td[14]"));
				r= child3.getText();
				Assert.assertEquals(r, "Passed");
				
				System.out.println("Badge1 is fail, badge 2 is pass. so test pass!");
				
				
				driver.quit();
																							
	}

}
