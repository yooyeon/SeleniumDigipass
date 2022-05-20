package Dashboard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import org.apache.commons.exec.util.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;



public class test {

	public static void main(String[] args) throws InterruptedException {
		DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
		Date time = new Date();
		String time1 = dateFormat.format(time);
		System.out.println("current time is: "+time1);
		
		DateFormat dateFormat2 = new SimpleDateFormat("MM/dd");
		Date date = new Date();
		String date1 = dateFormat2.format(date);
		String strPattern = "^0+(?!$)";
	    date1 = date1.replaceAll(strPattern, "");
		System.out.println(date1);
	}

}
