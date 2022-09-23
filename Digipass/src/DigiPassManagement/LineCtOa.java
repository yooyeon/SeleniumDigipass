package DigiPassManagement;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class LineCtOa {

	public static void main(String[] args) throws InterruptedException {
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
				driver.findElement(By.xpath("//*[text()='Line CT, OA, & RTR']")).click();
				Thread.sleep(5000);
				
				
				
				
				now = LocalDateTime.now(); 
				System.out.println("@"+dt.format(now)+" "+"Test pass!");				
				driver.quit();
	}

}
