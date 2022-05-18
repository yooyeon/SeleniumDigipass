import java.net.SocketException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

public class ManageStation {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
				// Login to home page , and expand menu icons
				/*   run in db:
				     delete [station] where  station_category_id in (select id from [station_category] where name ='testCategory')
				     delete [station_category_color_definition] where station_category_id in
  						(select id from [station_category] where name ='testCategory')
  					 delete[station_category] where name ='testCategory'  					 
  				*/
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
				
				
				//Open aac05 manage station details window.
				ele1 = driver.findElement(By.xpath("//div[contains(text(),'AAC05')]"));	
				parent = ele1.findElement(By.xpath("./../.."));
				child = parent.findElement(By.xpath("./td[3]/div[3]/div[1]"));	
				child.click();
				Thread.sleep(2000);
				driver.switchTo().frame("mapStationIframe");
				Thread.sleep(1000);

				
				//open manage station category popup window.
				driver.findElement(By.cssSelector("i.dx-icon.far.fa-plus-square")).click();
				Thread.sleep(1000);
				
				//Add new station category
				driver.findElement(By.xpath("//div[@id='categoryAddGrid']/div[1]/div[4]/div[1]/div[1]/div[3]/div[1]/div[1]/div[1]/div[1]")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//td[@aria-colindex='1']/div[1]/div[1]/div[1]/input[1]")).sendKeys("testCategory");
				Thread.sleep(1000);
				driver.findElement(By.xpath("//div[@id='categoryAddGrid']/div[1]/div[6]/div[1]/table[1]/tbody[1]/tr[1]/td[2]")).click();
				driver.findElement(By.xpath("//td[@aria-colindex='2']/div[1]/div[1]/div[1]/input[1]")).sendKeys("testCategoryDes");
				Thread.sleep(1000);
				driver.findElement(By.xpath("//div[@id='categoryAddGrid']/div[1]/div[6]/div[1]/table[1]/tbody[1]/tr[1]/td[3]")).click();
				driver.findElement(By.xpath("//td[@aria-colindex='3']/div[1]/div[1]/div[1]/div[1]/div[1]/input[1]")).sendKeys("#C43031");
				Thread.sleep(1000);
				driver.findElement(By.xpath("//div[@id='categoryAddGrid']/div[1]/div[6]/div[1]/table[1]/tbody[1]/tr[1]/td[4]")).click();
				driver.findElement(By.xpath("//td[@aria-colindex='4']/div[1]/div[1]/div[1]/div[1]/div[1]/input[1]")).sendKeys("#FFFFFF");
				Thread.sleep(1000);
				driver.findElement(By.xpath("//div[@id='categoryAddGrid']/div[1]/div[4]/div[1]/div[1]/div[3]/div[2]/div[1]/div[1]/div[1]/i[1]")).click();
				Thread.sleep(1000);
				String message=driver.findElement(By.cssSelector("div.dx-toast-message")).getText();
				Assert.assertEquals(message, "Record has been updated");
				System.out.println("This message appeared : " + message);
				Thread.sleep(3000);
				
				//close station mapping window and reopen
				driver.switchTo().defaultContent();
				driver.findElement(By.cssSelector("i.dx-icon.dx-icon-close")).click();
				Thread.sleep(1000);
				ele1 = driver.findElement(By.xpath("//div[contains(text(),'AAC05')]"));	
				parent = ele1.findElement(By.xpath("./../.."));
				child = parent.findElement(By.xpath("./td[3]/div[3]/div[1]"));	
				child.click();
				Thread.sleep(2000);				
				
				
			
				//Add new station with new added station category
				driver.switchTo().frame("mapStationIframe");
				driver.findElement(By.cssSelector("i.dx-icon.dx-icon-edit-button-addrow")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//td[@aria-colindex='2']/div[1]/div[1]/div[1]/div[1]/input[@class='dx-texteditor-input']")).sendKeys("testCategory");
				driver.findElement(By.xpath("//body/div[6]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='3']")).click();
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='3']/div[1]/div[1]/div[1]/input")).sendKeys("testStation1");
				Thread.sleep(1000);
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='5']")).click();
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='5']/div[1]/div[1]/div[1]/div[1]/input[1]")).sendKeys("VRV");
				driver.findElement(By.xpath("//div[contains(text(),'VRV  He Plate Brazer')]")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='6']")).click();
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='6']/div[1]/div[1]/div[1]/div[1]/input[1]")).sendKeys("Fan Top SA");
				driver.findElement(By.xpath("//div[contains(text(),'Fan Top SA')]")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='7']")).click();
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='7']/div[1]/div[1]/div[1]/div[1]/input[1]")).sendKeys("Kaishi");
				driver.findElement(By.xpath("//div[contains(text(),'Kaishi')]")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='8']")).click();
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='8']/div[1]/div[1]/div[1]/div[1]/input[1]")).sendKeys("IDL");
				driver.findElement(By.xpath("//div[contains(text(),'IDL')]")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//body/div[@id='pageContent']/div[@id='gridStationMapping']/div[1]/div[4]/div[1]/div[1]/div[3]/div[3]/div[1]/div[1]/div[1]/i[1]")).click();
				Thread.sleep(5000);

				
				//Add another station with new added station category
				//driver.switchTo().frame("mapStationIframe");
				driver.findElement(By.cssSelector("i.dx-icon.dx-icon-edit-button-addrow")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//td[@aria-colindex='2']/div[1]/div[1]/div[1]/div[1]/input[@class='dx-texteditor-input']")).sendKeys("testCategory");
				driver.findElement(By.xpath("//body/div[6]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='3']")).click();
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='3']/div[1]/div[1]/div[1]/input")).sendKeys("testStation2");
				Thread.sleep(1000);
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='5']")).click();
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='5']/div[1]/div[1]/div[1]/div[1]/input[1]")).sendKeys("VRV");
				driver.findElement(By.xpath("//div[contains(text(),'VRV  He Plate Brazer')]")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='6']")).click();
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='6']/div[1]/div[1]/div[1]/div[1]/input[1]")).sendKeys("Fan Top SA");
				driver.findElement(By.xpath("//div[contains(text(),'Fan Top SA')]")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='7']")).click();
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='7']/div[1]/div[1]/div[1]/div[1]/input[1]")).sendKeys("Kaishi");
				driver.findElement(By.xpath("//div[contains(text(),'Kaishi')]")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='8']")).click();
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='8']/div[1]/div[1]/div[1]/div[1]/input[1]")).sendKeys("IDL");
				driver.findElement(By.xpath("//div[contains(text(),'IDL')]")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//body/div[@id='pageContent']/div[@id='gridStationMapping']/div[1]/div[4]/div[1]/div[1]/div[3]/div[3]/div[1]/div[1]/div[1]/i[1]")).click();
				Thread.sleep(3000);
				
				//Add 3rd station 
				//driver.switchTo().frame("mapStationIframe");
				driver.findElement(By.cssSelector("i.dx-icon.dx-icon-edit-button-addrow")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//td[@aria-colindex='2']/div[1]/div[1]/div[1]/div[1]/input[@class='dx-texteditor-input']")).sendKeys("testCategory");
				driver.findElement(By.xpath("//body/div[6]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='3']")).click();
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='3']/div[1]/div[1]/div[1]/input")).sendKeys("testStation3");
				Thread.sleep(1000);
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='5']")).click();
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='5']/div[1]/div[1]/div[1]/div[1]/input[1]")).sendKeys("VRV");
				driver.findElement(By.xpath("//div[contains(text(),'VRV  He Plate Brazer')]")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='6']")).click();
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='6']/div[1]/div[1]/div[1]/div[1]/input[1]")).sendKeys("Fan Top SA");
				driver.findElement(By.xpath("//div[contains(text(),'Fan Top SA')]")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='7']")).click();
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='7']/div[1]/div[1]/div[1]/div[1]/input[1]")).sendKeys("Kaishi");
				driver.findElement(By.xpath("//div[contains(text(),'Kaishi')]")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='8']")).click();
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='8']/div[1]/div[1]/div[1]/div[1]/input[1]")).sendKeys("IDL");
				driver.findElement(By.xpath("//div[contains(text(),'IDL')]")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//body/div[@id='pageContent']/div[@id='gridStationMapping']/div[1]/div[4]/div[1]/div[1]/div[3]/div[3]/div[1]/div[1]/div[1]/i[1]")).click();
				Thread.sleep(3000);
				
				//search with new created station name. make sure listed two stations
				driver.findElement(By.xpath("//input[@aria-label='Search in data grid']")).sendKeys("testStation");
				int s= driver.findElements(By.xpath("//span[contains(text(),'testStation')]")).size();
				Assert.assertEquals(s, 3);
				System.out.println("Created two stations successfully!");
				
				//click select all to , select two filtered stations to do bulk change
				driver.findElement(By.xpath("//td[@aria-label='Select all']/div[1]/div[1]")).click();
				Thread.sleep(1000);
				
				//Do bulk change 
				driver.findElement(By.cssSelector("i.dx-icon.dx-icon-edit")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//div[@id='multiSelectOpType']/div[1]/div[1]/input[1]")).sendKeys("VRV Receiver Brazer");
				driver.findElement(By.xpath("//div[contains(text(),'VRV Receiver Brazer')]")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//span[contains(text(),'Confirm')]")).click();
				Thread.sleep(1000);
				driver.findElement(By.cssSelector("i.dx-icon.dx-icon-edit-button-save")).click();
				s= driver.findElements(By.xpath("//td[contains(text(),'VRV Receiver Brazer')]")).size();
				Assert.assertEquals(s, 3);
				System.out.println("bulk change succeed!");
				Thread.sleep(3000);
				
				//Edit one station name
				driver.findElement(By.xpath("//input[@aria-label='Search in data grid']")).sendKeys("1");
				Thread.sleep(5000);
				driver.findElement(By.xpath("//span[contains(text(),'testStation1')]")).click();
				driver.findElement(By.xpath("//tbody[@role='presentation']/tr[contains(@data-intro,'station to be shown')]/td[@aria-colindex='3']/div[1]/div[1]/div[1]/input")).sendKeys("0");
				Thread.sleep(1000);
				driver.findElement(By.xpath("//div[contains(text(),'Drag a column header here to group by that column')]")).click();
				Thread.sleep(1000);
				driver.findElement(By.cssSelector("i.dx-icon.dx-icon-edit-button-save")).click();
				Thread.sleep(5000);
				driver.findElement(By.xpath("//input[@aria-label='Search in data grid']")).sendKeys("0");
				Thread.sleep(1000);
				s= driver.findElements(By.xpath("//span[contains(text(),'testStation10')]")).size();
				Assert.assertEquals(s, 1);
				System.out.println("Edited station successfully!");
				
				//Delete one station
				driver.findElement(By.xpath("//a[@title='Delete']")).click();
				driver.findElement(By.cssSelector("i.dx-icon.dx-icon-edit-button-save")).click();
				Thread.sleep(5000);
				s= driver.findElements(By.xpath("//span[contains(text(),'testStation10')]")).size();
				Assert.assertEquals(s, 0);
				System.out.println("Deleted station successfully!");
				
				
				driver.quit();
				
				
				}
						

}
