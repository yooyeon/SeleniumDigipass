import java.net.SocketException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
public class OperatorReport {

	public static void main(String[] args) throws InterruptedException {
				// need to run CreateClassAndEditGrade.java first.
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

				// Navigate to Skills And Curriculum Reports page first then go to operator report page
				driver.findElement(By.cssSelector("i.fas.fa-file-alt")).click();
				Thread.sleep(3000);
				driver.findElement(By.cssSelector("label.menu-open-button")).click();
				Thread.sleep(2000);
				driver.findElement(By.cssSelector("i.fas.fa-table")).click();
				Thread.sleep(2000);
				
				//Enter badge 55143. then submit.
				driver.findElement(By.cssSelector("input.dx-texteditor-input")).sendKeys("55143");
				Thread.sleep(1000);
				driver.findElement(By.xpath("//span[contains(text(),'Submit')]")).click();
				Thread.sleep(2000);
				
				//In search area enter "Yuyan Cui", to filter classes created by test.
				driver.findElement(By.xpath("//input[@aria-label='Search in data grid']")).sendKeys("Yuyan Cui");
				Thread.sleep(3000);
				
				//verify in the report listed skill '6S training'
				int s= driver.findElements(By.xpath("//td[contains(text(),'6S Training ')]")).size();
				Assert.assertTrue(s>0);
				
				//verify operator name, result for one of the 6s training class
				WebElement ele1 = driver.findElement(By.xpath("//td[contains(text(),'6S Training ')]")); 
				WebElement parent = ele1.findElement(By.xpath("./.."));
				WebElement child = parent.findElement(By.xpath("./td[1]"));
				String name = child.getText();
				System.out.println("operator name is: "+name);
				Assert.assertEquals(name, "Shahid Zahoor");
				ele1 = driver.findElement(By.xpath("//td[contains(text(),'6S Training ')]")); 
				parent = ele1.findElement(By.xpath("./.."));
				child = parent.findElement(By.xpath("./td[15]"));
				String result = child.getText();
				System.out.println("operator result for 6s training is: "+result);
				Assert.assertEquals(result, "Passed");
				System.out.println("So Test Pass.");
				
				
				driver.quit();


	}

}
