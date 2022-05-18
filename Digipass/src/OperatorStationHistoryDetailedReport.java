import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

public class OperatorStationHistoryDetailedReport {

	public static void main(String[] args) throws InterruptedException, SQLException {
				// only need to specify two variables: department dept and operator type ot
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

					// Navigate to Reports - Operator Station History page
					driver.findElement(By.cssSelector("i.fas.fa-file-alt")).click();
					Thread.sleep(5000);
					driver.findElement(By.cssSelector("label.menu-open-button")).click();
					Thread.sleep(2000);
					driver.findElement(By.cssSelector("i.fas.fa-history")).click();
					Thread.sleep(3000);
					driver.findElement(By.cssSelector("div.dx-switch-off")).click();
					Thread.sleep(3000);
										
					
					//select AAC05 in departments
					String dept="AAC05";
					
					WebElement ele1 = driver.findElement(By.xpath("//div[@data-dx_placeholder='Select Department(s)']")); 
					WebElement parent = ele1.findElement(By.xpath("./.."));
					WebElement child = parent.findElement(By.xpath("./input[1]"));					
					child.click();
					Thread.sleep(1000);
				    ele1 = driver.findElement(By.xpath("//*[contains(text(),'"+dept+"')]")); 
					parent = ele1.findElement(By.xpath("./../.."));
					child = parent.findElement(By.xpath("./div[1]/div[1]/div[1]"));
					child.click();
					Thread.sleep(1000);
					driver.findElement(By.xpath("//span[contains(text(),'OK')]")).click();
					Thread.sleep(3000);
					
					//select Leak Operator from  operator type drop down list
					String ot="Leak Operator";
									
				    ele1 = driver.findElement(By.xpath("//div[@data-dx_placeholder='Select Operator Type(s)']")); 
					parent = ele1.findElement(By.xpath("./.."));
					child = parent.findElement(By.xpath("./input[1]"));		
					child.click();
					Thread.sleep(1000);
					child.sendKeys(ot);
					Thread.sleep(2000);
					ele1 = driver.findElement(By.xpath("//*[contains(text(),'"+ot+"')]")); 
					parent = ele1.findElement(By.xpath("./../.."));
					child = parent.findElement(By.xpath("./div[1]/div[1]/div[1]"));
					child.click();
					Thread.sleep(2000);
					driver.findElement(By.xpath("//body[1]/div[16]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/span[1]")).click();
					Thread.sleep(3000);
					
					//go to db, find stations names which in selected dept and with selected operator type
					List<String> sn = new ArrayList<String>();
					ResultSet rs= s.executeQuery("select * from  station where id in ( SELECT station_id  FROM [passport_sandbox].[dbo].[station_operator_type]  where station_id in (select id from station where department_id in (select id from department where name ='"+dept+"') )  and operator_type_id in (select id from operator_type where name ='"+ot+"' ) ) and is_active=1");
					while(rs.next()) {
						String a=rs.getNString("name");
						sn.add(a);
					} 
					System.out.println(sn);					
					
					
					// Verify select station drop down list , make sure it listed all the stations which is with selected operator type.
					ele1 = driver.findElement(By.xpath("//div[@data-dx_placeholder='Select Station(s)']")); 
					parent = ele1.findElement(By.xpath("./.."));
					child = parent.findElement(By.xpath("./input[1]"));		
					child.click();
					Thread.sleep(1000);
					int si;
					for (int i = 0; i < sn.size(); i++) {				         
				           si= driver.findElements(By.xpath("//*[contains(text(),'"+sn.get(i)+"')]")).size();
				           Assert.assertTrue(si>0);
				           System.out.println(sn.get(i)+" is listed in the drop down list.");
				        }
					Thread.sleep(2000);
					
					// select the first station from the station drop down 
					child.sendKeys(sn.get(0));
					Thread.sleep(1000);
					ele1 = driver.findElement(By.xpath("//*[contains(text(),'"+sn.get(0)+" - " +dept+"')]")); 
					parent = ele1.findElement(By.xpath("./../.."));
					child = parent.findElement(By.xpath("./div[1]/div[1]/div[1]"));
					child.click();
					Thread.sleep(1000);
					driver.findElement(By.xpath("//body/div[16]/div[1]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/span[1]")).click();
					Thread.sleep(2000);
					
					// Go to db,  get latest 2 entries from operator_station_history table where is with selected dept, and selected station.
					rs= s.executeQuery("SELECT TOP (1) * FROM [operator_station_history] where department_id in (select id from department where name ='"+dept+"') and station_name ='"+sn.get(0)+"' ORDER BY user_modified_date desc");
					rs.next(); 
					String badge= rs.getString("badge");
					System.out.println("latest record badge is: "+badge);
					String station= rs.getString("station_name");
					System.out.println("latest record station is: "+station);
					String shift= rs.getString("shift");
					System.out.println("latest record shift is: "+shift);
					String sequence= rs.getString("sequence");
					System.out.println("latest record sequence is: "+sequence);
					String date= rs.getString("user_modified_date");
					System.out.println("latest record date is: "+date);
					String month= (String) date.subSequence(6, 7);
					String year= (String) date.subSequence(0, 4);
					String day= (String) date.subSequence(8, 10);
					System.out.println("latest record month is: "+month);
					System.out.println("latest record year is: "+year);
					System.out.println("latest record day is: "+day);
					String fromdate = month+"/"+day+"/"+year+", 12:00 AM";
					System.out.println("So will enter from date as: "+fromdate);
					
					//enter badge as queried badge #
					ele1 = driver.findElement(By.xpath("//div[@data-dx_placeholder='Enter Badge']")); 
					parent = ele1.findElement(By.xpath("./.."));
					child = parent.findElement(By.xpath(".//input[1]"));			
					child.sendKeys(badge);
					
					//select queried date's midnight as from date in the page
					driver.findElement(By.xpath("//body/div[10]/div[1]/div[1]/div[1]/input[1]")).clear();
					driver.findElement(By.xpath("//body/div[10]/div[1]/div[1]/div[1]/input[1]")).sendKeys(fromdate);
										
					
					//select tomorrow as to date in the page
					driver.findElement(By.xpath("//body/div[11]/div[1]/div[1]/div[1]/div[2]/div[1]/div[1]/div[1]")).click();
					Thread.sleep(2000);	
					driver.findElement(By.xpath("//td[contains(@class,'dx-calendar-selected-date')]/following-sibling::td[1]")).click(); 
					Thread.sleep(1000);
					driver.findElement(By.xpath("//span[contains(text(),'Apply')]")).click();
					Thread.sleep(1000);
					
					//click on search to get report
					driver.findElement(By.xpath("//span[contains(text(),'Submit')]")).click();
					Thread.sleep(5000);
					
					
					//verify the first record displayed in the page is queried latest entry .
					String b=driver.findElement(By.xpath("//td[@aria-describedby='dx-col-1']")).getText();
					Assert.assertEquals(b, badge);
					
					String dep=driver.findElement(By.xpath("//td[@aria-describedby='dx-col-2']")).getText();
					Assert.assertEquals(dep, dept);
					
					
					String st=driver.findElement(By.xpath("//td[@aria-describedby='dx-col-3']")).getText();
					Assert.assertEquals(st, station);
					
					
					String stse=driver.findElement(By.xpath("//td[@aria-describedby='dx-col-6']")).getText();
					Assert.assertEquals(stse, sequence);

					
					String sh=driver.findElement(By.xpath("//td[@aria-describedby='dx-col-13']")).getText();
					Assert.assertEquals(sh, shift);
					
					
					String d=driver.findElement(By.xpath("//td[@aria-describedby='dx-col-14']")).getText();
					Assert.assertEquals(d, month+"/"+day+"/"+year);					
					
				
					
					System.out.println("Test pass!");
					

					driver.quit();
					
					
					
					
					
				
					
					
					
					

	}

}
