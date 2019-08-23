package CucumberReportDemo.CucumberReportDemo;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;

import CucumberReportDemo.CucumberReportDemo.ExtentReport;


public class Reports {
ExtentHtmlReporter reporter;
ExtentReports extent;
ExtentTest test;

WebDriver driver;
 
public static String timeStamp()
	{
		Timestamp time =new Timestamp(System.currentTimeMillis());
		String ts= time.toString().replaceAll(":", "_ ").replace(".", "_");
		  System.out.println(ts);
		return ts;
	}
	void reportGeneration(String reportpath, String Configfilepath)
	{
		 System.out.println("loging in to browser");
		 reporter= new ExtentHtmlReporter(reportpath+"ExtentReport"+timeStamp()+".html");
		 extent = new ExtentReports();
		 extent.attachReporter(reporter);
		 reporter.loadXMLConfig(new File(Configfilepath));
		 reporter.config().setTestViewChartLocation(ChartLocation.TOP);
		 reporter.config().setChartVisibilityOnOpen(false);
		}
	
	void createTestcase(String testcasename)
	{
		test=extent.createTest(testcasename);
		

	}
	
	void testStatus(String teststatus) throws InterruptedException, IOException 
	{
	switch( teststatus)
	{
	case "pass" :
		Assert.assertTrue(true);
		Thread.sleep(2000);
		test.log(Status.PASS,MarkupHelper.createLabel("pass the senario", ExtentColor.GREEN));
		break;
	case "fail" :
		Assert.assertTrue(true);
		Thread.sleep(2000);
		test.log(Status.FAIL, MarkupHelper.createLabel("fail the senario",ExtentColor.RED));
		attachFailScreenshot();
		break;
	case "skip" :
	   Assert.assertTrue(true);
		Thread.sleep(2000);
		test.log(Status.SKIP, MarkupHelper.createLabel("skip the senario",ExtentColor.ORANGE));
		break;
	}
	}
	void createLable(String text)
	{
		
		 String testResult = text ;
		   test.info(MarkupHelper.createLabel(testResult, ExtentColor.GREEN));
	}
	void embededTable(HashMap<String,String> s)
	    {
	        

	        StringBuilder stringMapTable = new StringBuilder();
	        stringMapTable.append("<html><body><h4>Basic HTML Table</h4><table style='width:70%' border=1><tr><th bgcolor=\"#0000FF\" width=\"200%\">Excepted</th><th bgcolor=\"#00FF00\">Actual</th><th bgcolor=\"#FF0000\">Status</th></tr>");

	       for(Map.Entry<String, String> entry:s.entrySet())
	       
	       {
	       String key =entry.getKey();
	       String value =entry.getValue();
	       String status="";
	       if(key.equalsIgnoreCase(value))
	       {
	       status= "Pass";
	       stringMapTable.append("<tr><td>" + key + "</td><td>" +value + "</td><td width=\"40%\">" +status + "</td></tr>");
	       }
	       else
	       {
	       status="Fail"; 
	       stringMapTable.append("<tr><td bgcolor=\"#FF0000\">" + key + "</td><td bgcolor=\"#FF0000\">" +value + "</td><td bgcolor=\"#FF0000\" width=\"40%\">" +status + "</td></tr>");
	       }
	              
	        }
	       stringMapTable.append("</table></body></html>");
	      String mapTable = stringMapTable.toString();

	    
	        test.log(Status.INFO,mapTable);
	    }
	void embededLink(String link)
	    {
	        test.log(Status.INFO, "<a href="+link+">Click Here</a>");
	    }
	void codeBlock(String content)
	{
		String code = "\n\t\n\t\t"+content+"\n\t\n";
		Markup m = MarkupHelper.createCodeBlock(code);

		test.pass(m);
		
	}
	void setSystemInfo( String os, String browser)
	{
		
		extent.setSystemInfo(os, browser);
	}
	void attachFailScreenshot() throws IOException
	{
		String screenShotPath =captureScreen(driver,ExtentReport.pro.getProperty("Screenshotpath"));
		test.log(Status.FAIL, MarkupHelper.createLabel(" Test case FAILED due to below issues:", ExtentColor.RED));
		 reporter.config().setCSS(".r-img { width: 50%; }");
       test.fail("Snapshot below: ",MediaEntityBuilder.createScreenCaptureFromPath(screenShotPath).build());
       
     }
	void assignAuthorName(String name)
	{
		test.assignAuthor(name);
	}
	void addCategories(String testing_type)
	{
		test.assignCategory(testing_type);	
	}
	public static String captureScreen(WebDriver driver, String screentshotpath) throws IOException
	    {
		 System.setProperty("webdriver.gecko.driver",ExtentReport.pro.getProperty("Driverpath") );
		 driver= new FirefoxDriver();
	            driver.get(ExtentReport.pro.getProperty("url"));
		        TakesScreenshot screen = (TakesScreenshot) driver;
				File src = screen.getScreenshotAs(OutputType.FILE);
				String dest =screentshotpath+System. currentTimeMillis()+".png";
				File target = new File(dest);
				FileUtils.copyFile(src, target);
				return dest;
	    }
	void endReport()
	{
		
		extent.flush();
		
	}
}
