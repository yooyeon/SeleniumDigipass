package Dashboard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

public class test {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		// Create object of SimpleDateFormat class and decide the format
		//DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		DateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
		Date date2 = new Date();
		String time2 = dateFormat2.format(date2);
		System.out.println(time2);
	}

}
