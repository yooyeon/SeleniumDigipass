package Dashboard;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

public class search {

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
				
				// Navigate to a line dashboard.
				String dept = "SE Support Team";
				driver.findElement(By.cssSelector("i.fas.fa-tachometer-alt")).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath("//h4[contains(text(),'Manufacturing')]")).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath("//h4[contains(text(),'Systems Engineering')]")).click();
				Thread.sleep(2000);
				driver.findElement(By.xpath("//h4[contains(text(),'" + dept + "')]")).click();
				Thread.sleep(5000);
				int i = driver.findElements(By.xpath("//div[@id='departmentDash']")).size();
				Assert.assertTrue(i > 0);
				Thread.sleep(10000);
				
				// click on search icon on top
				driver.findElement(By.cssSelector("i.fa.fa-search")).click();
				Thread.sleep(5000);
				driver.findElement(By.xpath("//div[@id='searchByBadge']/div[1]/div[1]/input[1]")).sendKeys("57957"+ "\n");				
				Thread.sleep(8000);
				
				//confirm operator picture displayed
				int s =driver.findElements(By.xpath("//img[@src='pictures/57957.jpg']")).size();
				Assert.assertEquals(s, 1);
				now = LocalDateTime.now();
				System.out.println("@" + dt.format(now) + " " +"57957.jpg picture is displayed.");
				
				//Navigate to Skill data tab
				driver.findElement(By.xpath("//*[text()='Skill Data']")).click();
				Thread.sleep(2000);
				
				//Confirm AMS Basic skill is listed.
				s=driver.findElements(By.xpath("//*[text()='AMS Basic']")).size();
				Assert.assertEquals(s, 1);
				now = LocalDateTime.now();
				System.out.println("@" + dt.format(now) + " " +"AMS Basic picture is in Skill Data tab.");
				
				now = LocalDateTime.now();
				System.out.println("@" + dt.format(now) + " " + "Test Pass.");
				driver.quit();
				
				
				
				
				


	}

}
