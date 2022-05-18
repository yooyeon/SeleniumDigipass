import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

public class ManagementDetailedReport {

	public static void main(String[] args) throws InterruptedException, SQLException {
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
		
		//Navigate to management report-detailed report page.
		driver.findElement(By.cssSelector("i.fas.fa-chart-bar")).click();
		Thread.sleep(5000);
		
		//expand assembly 
		WebElement ele1 = driver.findElement(By.xpath("//span[contains(text(),'Assembly')]")); 
		WebElement parent = ele1.findElement(By.xpath("./.."));
		WebElement child = parent.findElement(By.xpath("./div[1]"));
		child.click();
		
		//select a dept and find dept id from db
		String dept= "AAC01";
		ResultSet rs= s.executeQuery("SELECT * FROM [department] WHERE NAME ='"+dept+"'");//select from table
		
		rs.next(); //rs.next() will move to 1st index, since by default will point to base index.
		System.out.println("Dept id is "+ rs.getString("id"));
		String deid= rs.getString("id");
		
		/*		
		//Verify ATS value is correct for select dept
        rs= s.executeQuery
        ("SELECT count(id) FROM [special_unassigned_status_history] where department_id="+deid+" and status='ATS'");
		
		rs.next(); //rs.next() will move to 1st index, since by default will point to base index.
		int n=Integer.parseInt(rs.getString(1));
		if (n>0) 
		{
		int l= driver.findElements(By.xpath("//span[contains(text(),"+ n+")]")).size(); // presented in the page
		Assert.assertTrue(l>0);
		} 
		*/
		
		//Verify each status count  for select dept
		String[] status = { "ATS", "AWOL", "OSH", "PPE - OFF","PPE - ON","PPE - UNKNOWN","RTP","RTP-Pending","RTPFAIL","RTPPEND" };
		
		for (int i = 0; i < status.length; i++)
		{
		
			rs= s.executeQuery
				        ("SELECT count(id) FROM [special_unassigned_status_history] where department_id="+deid+" and status='"+status[i]+"'");
						
			rs.next(); //rs.next() will move to 1st index, since by default will point to base index.
			int n=Integer.parseInt(rs.getString(1));
			if (n>0) 
			{
			int l= driver.findElements(By.xpath("//span[contains(text(),"+ n+")]")).size(); // presented in the page
						Assert.assertTrue(l>0);
			System.out.println(status[i]+" count is "+n+".and presented.");
			} 
			
		}
		
		// confirm Aluminum coils total PPE - UNKNOWN count is presented.
		rs= s.executeQuery
		        ("SELECT count(id) FROM [special_unassigned_status_history] where department_id in (8,2152) and status='PPE - UNKNOWN' ");
				
		rs.next(); //rs.next() will move to 1st index, since by default will point to base index.
		int n=Integer.parseInt(rs.getString(1));
		if (n>0) 
		{
			int l= driver.findElements(By.xpath("//span[contains(text(),"+ n+")]")).size(); // presented in the page
				Assert.assertTrue(l>0);
				System.out.println("Aluminum coils total PPE - UNKNOWN count is "+n+ ".and presented.");
		} 
		
		System.out.println("test pass.");
	
		driver.quit();
				

	}

}
