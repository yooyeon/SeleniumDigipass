import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

public class ManagementNonDetailedReports {

	public static void main(String[] args) throws SQLException, InterruptedException, ParseException {
		// Connect to DB
		String UserName="sa";
		String Password="ChangeIt17";
		String serverName="10.172.86.53";
		String dbName="passport_sandbox";
        String  DB_URL = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true";
		Connection con= DriverManager.getConnection( DB_URL,UserName, Password);

		Statement s=con.createStatement();
		
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
		
		//Navigate to management non report-detailed report page.
		driver.findElement(By.cssSelector("i.fas.fa-chart-bar")).click();
		Thread.sleep(3000);
		driver.findElement(By.cssSelector("div.dx-switch-on")).click();
		Thread.sleep(3000);
		
		// Go to db,  get last entry from special_unassigned_status_history table.
		ResultSet rs= s.executeQuery("SELECT top (1) * FROM [special_unassigned_status_history] order by modified_date desc");
		rs.next(); 
		String deptID= rs.getString("department_id");
		System.out.println("latest record department id is: "+deptID);
		String badge=rs.getString("badge");
		System.out.println("latest record badge is: "+badge);
		String date=rs.getString("modified_date");
		System.out.println("latest record date is: "+date);
		String shift= rs.getString("shift");
		System.out.println("latest record department shift is: "+shift);
		String name= rs.getString("operator_name");
		System.out.println("latest record department name is: "+name);
		String status= rs.getString("status");
		System.out.println("latest record department status is: "+status);
		String month= (String) date.subSequence(6, 7);
		String year= (String) date.subSequence(0, 4);
		String day= (String) date.subSequence(8, 10);
		System.out.println("latest record month is: "+month);
		System.out.println("latest record year is: "+year);
		System.out.println("latest record day is: "+day);
		rs= s.executeQuery("select * from department where id ="+deptID);
		rs.next(); 
		String dept=rs.getString("name");
		System.out.println("latest record department name is: "+ dept);
		String fromdate = month+"/"+day+"/"+year+", 12:00 AM";
		System.out.println("So will enter from date as: "+fromdate);	
	
		

		//select queried date's midnight as from date in the page
		driver.findElement(By.xpath("//body/div[5]/div[1]/div[1]/div[1]/div[1]/input[1]")).clear();
		driver.findElement(By.xpath("//body/div[5]/div[1]/div[1]/div[1]/div[1]/input[1]")).sendKeys(fromdate);
		

		//select tomorrow as to date in the page
		driver.findElement(By.xpath("//body/div[6]/div[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]")).click();
		Thread.sleep(2000);				
		driver.findElement(By.xpath("//td[contains(@class,'dx-calendar-selected-date')]/following-sibling::td[1]")).click(); 
		Thread.sleep(1000);
		driver.findElement(By.xpath("//span[contains(text(),'Apply')]")).click();
		Thread.sleep(1000);
	
		
		// select queried dept  in the page
		driver.findElement(By.cssSelector("div.dx-texteditor-container.dx-native-click")).click();
		WebElement  ele1 = driver.findElement(By.xpath("//div[@data-dx_placeholder='Select Department(s)']")); 
		WebElement  parent = ele1.findElement(By.xpath("./.."));
		WebElement  child = parent.findElement(By.xpath(".//input[1]"));
	    child.sendKeys(dept);//enter queried dept
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@class='dx-list-select-all']/div[1]")).click();
		driver.findElement(By.xpath("//span[contains(text(),'OK')]")).click();
		Thread.sleep(2000);
		
		//enter queried badge in the page
	    ele1 = driver.findElement(By.xpath("//div[@data-dx_placeholder='Please scan or enter Badge/Clock #']")); 
		parent = ele1.findElement(By.xpath("./.."));
		child = parent.findElement(By.xpath(".//input[1]"));
		child.sendKeys(badge);
		
		//click on search to get report
		driver.findElement(By.xpath("//span[contains(text(),'Search')]")).click();
		Thread.sleep(5000);


		//verify first record is queried latest entry .
		String b=driver.findElement(By.xpath("//body[1]/div[11]/div[2]/div[1]/div[6]/div[1]/div[1]/div[1]/div[1]/table[1]/tbody[1]/tr[1]/td[1]")).getText();
		Assert.assertEquals(b, badge);
		
		String d=driver.findElement(By.xpath("//body[1]/div[11]/div[2]/div[1]/div[6]/div[1]/div[1]/div[1]/div[1]/table[1]/tbody[1]/tr[1]/td[2]")).getText();
		Assert.assertEquals(d, dept);
		
		String sh=driver.findElement(By.xpath("//body[1]/div[11]/div[2]/div[1]/div[6]/div[1]/div[1]/div[1]/div[1]/table[1]/tbody[1]/tr[1]/td[3]")).getText();
		Assert.assertEquals(sh, shift);
		
		String o=driver.findElement(By.xpath("//body[1]/div[11]/div[2]/div[1]/div[6]/div[1]/div[1]/div[1]/div[1]/table[1]/tbody[1]/tr[1]/td[4]")).getText();
		Assert.assertEquals(o, name);
		
		String st=driver.findElement(By.xpath("//body[1]/div[11]/div[2]/div[1]/div[6]/div[1]/div[1]/div[1]/div[1]/table[1]/tbody[1]/tr[1]/td[5]")).getText();
		Assert.assertEquals(st, status);
		
		
		String md=driver.findElement(By.xpath("//body[1]/div[11]/div[2]/div[1]/div[6]/div[1]/div[1]/div[1]/div[1]/table[1]/tbody[1]/tr[1]/td[6]")).getText();
		String md1= md.substring(0, md.length() - 1); // to remove last character.
		Assert.assertEquals(md1, date);
		
		System.out.println("Test pass!");
		

		driver.quit();
}

}
