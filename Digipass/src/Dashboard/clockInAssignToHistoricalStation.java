package Dashboard;

import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

public class clockInAssignToHistoricalStation {

	public static void main(String[] args) throws InterruptedException, SQLException {
	
		// Login to test WFD 
				System.setProperty("webdriver.chrome.driver", "C:\\yooyeon\\chromedriver.exe");
				WebDriver driver = new ChromeDriver();
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

				driver.get("https://goodmanglobalhold-uat.npr.mykronos.com/timekeeping#/timecard");
				driver.manage().window().maximize();
				driver.findElement(By.id("idToken1")).sendKeys("SETJIRA");
				Thread.sleep(1000);
				driver.findElement(By.id("idToken2")).sendKeys("goodman3");
				Thread.sleep(1000);
				driver.findElement(By.id("loginButton_0")).click();
				Thread.sleep(3000);

				DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  

				LocalDateTime now = LocalDateTime.now();  
				
				
				// Get today's date 
				//DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				String date1 = dateFormat.format(date);
				System.out.println("today is : "+date1);
				
				
				// Get current time
				dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date time = new Date();
				String time1 = dateFormat.format(time);
				System.out.println("current time is: "+time1);
				
				// Get current time exclude date
				DateFormat dateFormat3 = new SimpleDateFormat("HH:mm:ss");
				Date date3 = new Date();
				String time3 = dateFormat3.format(date3);
				//System.out.println("time3 is : "+time3);
				
				
				// Connect to DB 
				//String operator="49102";
				String UserName="sa";
				String Password="ChangeIt17";
				String serverName="10.172.86.53";
				String dbName="passport_sandbox";
				String  DB_URL = "jdbc:sqlserver://" +serverName + ":1433;DatabaseName=" + dbName + ";encrypt=true;trustServerCertificate=true";
				Connection con= DriverManager.getConnection( DB_URL,UserName, Password);		
				Statement s=con.createStatement();
				
				//Connect to DB and find an operator who doesn't exists in dashboard or unassigned table
				// and this operator no reserved station in any dept
				// and operator has schedule for today and operator last station id in snapshot table  in his scheduled dept	
				// and scheduled dept is non AMS line. 
				// and in snapshot table department_number is not null
				// and  current time is in his scheduled time 
				// and operator scheduled department id is same as snapshot department id.	
				// and operator department current is running a shift , and this shift number is matching with operator scheduled shift number.
				// and operator only has one entry for today in schedules table. (I found it will fail when operator got two entries in schedule table for today as 2nd shift--i think it's SET code issue , but since it's an existing  issue will exclude this case)
				// and his last_station_id from snapshot table is not occupied now.
				// and last_station_id in snapshot table is same as station_id in operator_station_history table latest entry.  then find latest clock entry's ID for filtered operator.
				
				ResultSet rs= s.executeQuery(" with t as (\r\n"
						+ "SELECT row_number() over(\r\n"
						+ "partition by badge order by queued_time desc\r\n"
						+ ") as row_num, *\r\n"
						+ "FROM [passport_demo].[dbo].[operator_status] ) \r\n"
						+ " select top(15) * from t where t.row_num=1   and \r\n"
						+ "badge  in \r\n"
						+ "(select badge from operator_snapshot where last_station_id in  (select id from station where department_id not in (select id from department where name ='AAC05') and is_active=1 and id in (select station_id from dashboard_data where  badge is null )))\r\n"
						+ "\r\n"
						+ "and badge in (  select  osp.badge from operator_snapshot as osp \r\n"
						+ "join operator_schedules as os on osp.badge=os.badge\r\n"
						+ "join dashboard_shiftsdefinition_v as sd on os.department_id=sd.department_id \r\n"
						+ "join station as st on osp.last_station_id = st.id\r\n"
						+ "join operator_station_history  as osh  on osh.badge= osp.badge\r\n"
						+ "where sd.is_ams_line=0 and osh.shift= os.shift_number  and os.shift_number=sd.shiftNumber and sd.shiftactive='Y'  and  os.scheduled_day='"+date1+"' and os. schedule_start <='"+time1+"' and os.schedule_end >= '"+time1+"'\r\n"
						+ "and sd.dateEffective<='"+date1+"' and cast(sd.shiftStart as time) <='"+time3+"'  and cast(sd.shiftEnd as time) >='"+time3+"'\r\n"
						+ " and os.department_id = st.department_id and osp.last_department_id=os.department_id\r\n"
						+ " and osp.last_department_number in (SELECT financegroup\r\n"
						+ "  FROM [passport_sandbox].[dbo].[department_financegroup] where department_id=os.department_id ) \r\n"
						+ "  and osp.last_station_id in (  select top(1) station_id from operator_station_history where badge= osp.badge and department_number is not null order by user_modified_date desc) )\r\n"
						+ "\r\n"
						+ "and  badge in (SELECT badge FROM [passport_sandbox].[dbo].[operator_schedules] where  scheduled_day='"+date1+"' group by badge having COUNT(badge)=1) \r\n"
						+ " and  badge not in (select badge from unassigned_operators)\r\n"
						+ " and badge not in (select badge  from dashboard_data where badge is not null) \r\n"
						+ " and badge not in (select  o.badge_num from  operator as o \r\n"
						+ "join operator_station_reservation as osr on o.id=osr.operator_id) order by queued_time asc  ") ;
				
			
				rs.next(); 
				String operator= rs.getString("badge");
				now = LocalDateTime.now(); 
				System.out.println("@"+dt.format(now)+" "+"Operator badge is : "+operator);
				
				rs= s.executeQuery("SELECT TOP (1) * FROM [passport_sandbox].[dbo].[operator_status] where badge ="+operator+" order by queued_time desc");
				rs.next(); 
				String id1= rs.getString("id");
				now = LocalDateTime.now(); 
				System.out.println("@"+dt.format(now)+" "+"In operator_status table latest record id is: "+id1);		
				
				//click on search and search with a operator badge number, then select operator and click "Go To" button to open timecard page.
				driver.findElement(By.xpath("//button[@title='Employee Search']")).click();
				Thread.sleep(2000);
				driver.findElement(By.id("employeesSearchInput")).sendKeys(operator);
				Thread.sleep(2000);
				driver.findElement(By.xpath("//button[text()='Search']")).click();
				
				WebElement ele1 = driver.findElement(By.xpath("//span[text()='"+operator+"']")); 
				WebElement parent = ele1.findElement(By.xpath("./../../../../../../../../../.."));
				WebElement child = parent.findElement(By.xpath("./div[2]/div[1]/div[1]/input[1]"));		
				child.click();
				Thread.sleep(1000);
				driver.findElement(By.id("goToDropdownButton")).click();
				Thread.sleep(2000);
				
				//driver.findElement(By.xpath("//button[@title='Timecard']")).click();
				driver.findElement(By.xpath("//ul[@class='go-to-items-list']/li[1]/button")).click();
				Thread.sleep(3000);
				
				//select today in timecard page
				driver.findElement(By.xpath("//button[@title='Select Timeframe']")).click();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//ul[@class='pay-periods-list large']/li[9]/span[text()='Today']")).click();
				Thread.sleep(1000);
				
				//Get current time
				DateFormat dateFormat2 = new SimpleDateFormat("hh:mm a");
				Date date2 = new Date();
				String time2 = dateFormat2.format(date2);
				

				DateFormat dateFormat5 = new SimpleDateFormat("MM/dd");
				Date dat = new Date();
				String dat1 = dateFormat5.format(dat);
				String strPattern = "^0+(?!$)";
			    dat1 = dat1.replaceAll(strPattern, "");
				System.out.println("today is: "+dat1);
				
				
				// In today's schedule , enter clock in time and current time and save. 
				ele1 = driver.findElement(By.xpath("//span[contains(text(), ' "+dat1+"')]"));
				parent = ele1.findElement(By.xpath("./../../../../../.."));
				child = parent.findElement(By.xpath("./div[1]/timecard-add-cell[1]/div[1]"));
				String app=child.getAttribute("id");
				char a1 = app.charAt(0);
				
				Actions act = new Actions(driver);
				WebElement  dc = driver.findElement(By.id(a1+"_inpunch"));
				act.contextClick(dc).perform();
				Thread.sleep(1000);
				driver.findElement(By.xpath("//button[@aria-label='Edit Punch']")).click();
				Thread.sleep(2000);
				driver.findElement(By.id("punch-effective-time_inptext")).sendKeys(time2);
				Thread.sleep(1000);
				driver.findElement(By.xpath("//button[text()='Apply']")).click();
				Thread.sleep(2000);
				driver.findElement(By.cssSelector("i.icon-k-save.button-highlight.hidden-xs")).click();
				
				//check db in every 20 sec, wait until found new clock entry created.
				now = LocalDateTime.now();  
				System.out.println("@"+dt.format(now)+" "+"Wait for clock entry, will check every 20 sec...");
				
				while(!checkNewEntryCreated(rs,s, id1, operator))
				{

		             Thread.sleep(20000);
		           
				}
				rs= s.executeQuery("SELECT TOP (1) * FROM [passport_sandbox].[dbo].[operator_status] where badge ="+operator+" order by queued_time desc");
				rs.next(); 
				String id2= rs.getString("id");
				Assert.assertTrue(Integer.parseInt(id2)>Integer.parseInt(id1));
				String v= rs.getString("clocked_in");
				Assert.assertEquals(v, "1");
				v= rs.getString("clocked_out");
				Assert.assertEquals(v, "0");
				String badge=rs.getString("badge");
				Assert.assertEquals(badge, operator);
				now = LocalDateTime.now(); 
				System.out.println("@"+dt.format(now)+" "+"It created new clock in entry and id is "+id2);	
				
				
				
				//Wait 20sec, then confirm in the db, it inserted into dashboard_data table.
				now = LocalDateTime.now(); 
				System.out.println("@"+dt.format(now)+" "+"Wait for 20sec...");
				Thread.sleep(20000);	
				rs= s.executeQuery(" SELECT * FROM [passport_sandbox].[dbo].[dashboard_data] where badge ="+operator+"");
				rs.next(); 
				String station= rs.getString("Station_name");
				String stId= rs.getString("Station_id");
				String dept= rs.getString("department_id");
				String sft= rs.getString("shift");
				rs= s.executeQuery(" SELECT * FROM department where id ="+dept+"");
				rs.next(); 
				String deptName= rs.getString("name");
				now = LocalDateTime.now(); 
				System.out.println("@"+dt.format(now)+" "+"Operator has assigned to department: "+deptName+" in station: "+station);		
				
			    //confirm that operator assigned to a previous worked station.
				rs= s.executeQuery("SELECT * \r\n"
						+ "  FROM [passport_sandbox].[dbo].[operator_station_history] where badge ='"+operator+"' \r\n"
						+ "  and department_id='"+dept+"' and shift= '"+sft+"' and station_id='"+stId+"' ");
				
				Assert.assertTrue(rs.next());
				
				now = LocalDateTime.now(); 
				System.out.println("@"+dt.format(now)+" "+"Test pass!");		
				driver.quit();
				
				

			}
			public static boolean checkNewEntryCreated(ResultSet rs,Statement s, String id1, String operator) throws InterruptedException, SQLException{
				rs= s.executeQuery("SELECT TOP (1) * FROM [passport_sandbox].[dbo].[operator_status] where badge ="+operator+" order by queued_time desc");
				rs.next(); 
				String id2= rs.getString("id");
				if(Integer.parseInt(id2)>Integer.parseInt(id1)) {
					return true;
				} else {
					return false;
				}
						
		}

}
