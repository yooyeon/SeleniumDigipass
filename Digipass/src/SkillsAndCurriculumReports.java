import java.net.SocketException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.testng.Assert;

public class SkillsAndCurriculumReports {

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

		// Navigate to Skills And Curriculum Reports page.
		driver.findElement(By.cssSelector("i.fas.fa-file-alt")).click();
		Thread.sleep(5000);
		
		//only select 6S training skill 
		driver.findElement(By.xpath("//div[@id='SkillSelect']/div[1]/div[1]//input[1]")).click();
		WebElement ele1 = driver.findElement(By.xpath("//div[contains(text(),'Select All')]")); 
		WebElement parent = ele1.findElement(By.xpath("./.."));
		WebElement child = parent.findElement(By.xpath("./div[1]/div[1]"));
		child.click();// uncheck select all
		Thread.sleep(1000);
		ele1 = driver.findElement(By.xpath("//div[contains(text(),'6S Training')]")); 
		parent = ele1.findElement(By.xpath("./.."));
		child = parent.findElement(By.xpath("./div[1]/div[1]")); 
		child.click();
		//select To date as tomorrow then submit
		driver.findElement(By.xpath("//div[@id='To']/div[1]/div[1]/div[2]")).click();
		driver.findElement(By.xpath("//td[contains(@class,'dx-calendar-selected-date')]/following-sibling::td[1]")).click(); 
		driver.findElement(By.xpath("//span[contains(text(),'Apply')]")).click();
		driver.findElement(By.xpath("//span[contains(text(),'Submit')]")).click();
		Thread.sleep(5000);
		
		//verify operator 55368 & 55143 presented in the report.
		int s=driver.findElements(By.xpath("//span[contains(text(),'Zahoor - 55143')]")).size();
		Assert.assertEquals(s, 1);
	    s=driver.findElements(By.xpath("//span[contains(text(),'Lastice - 55368')]")).size();
		Assert.assertEquals(s, 1);
		
		//verify report . confirm that operator 55368 grade is 0 , and 55143 grade is 1.		
		String t;
		int c=driver.findElements(By.xpath("/html[1]/body[1]/div[9]/div[1]/div[1]/div[1]/table[1]/tr[4]/td[1]/div[1]/div[1]/div[1]/div[1]/table[1]/tbody[1]/tr")).size();
	    for (int i=1;i<=c;i++)
	    {
	    	t= driver.findElement(By.xpath("/html[1]/body[1]/div[9]/div[1]/div[1]/div[1]/table[1]/tr[4]/td[1]/div[1]/div[1]/div[1]/div[1]/table[1]/tbody[1]/tr["+i+"]/td[1]/span[1]")).getText();

	    	String s1="Brittani a Lastice - 55368";
	    	String s2="Shahid Zahoor - 55143";
	    	
	    	if(t.equals(s1)) {
	    		String grade =driver.findElement(By.xpath("/html[1]/body[1]/div[9]/div[1]/div[1]/div[1]/table[1]/tr[4]/td[2]/div[1]/div[1]/div[1]/div[1]/table[1]/tbody[1]/tr["+i+"]/td[1]/span[1]")).getText();
	    		Assert.assertEquals(grade, "0.00");
	    		System.out.println("Operator 55368 latest grade is 0.00.");
	    	}
	    	else if(t.equals(s2)){
	    		String grade =driver.findElement(By.xpath("/html[1]/body[1]/div[9]/div[1]/div[1]/div[1]/table[1]/tr[4]/td[2]/div[1]/div[1]/div[1]/div[1]/table[1]/tbody[1]/tr["+i+"]/td[1]/span[1]")).getText();
	    		Assert.assertEquals(grade, "1.00");
	    		System.out.println("Operator 55143 latest grade is 1.00.");
	    		
	    	} 
	    	
	    }
	    			
		System.out.println("So test pass!");

		driver.quit();
	}

}
