package MiniProject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestAmazon
{

	public static WebDriver driver = null;

	public static void main(String[] args) throws InterruptedException 
	{

		System.setProperty("webdriver.chrome.driver","E:\\worksoft\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		Date date = new Date();
		PrintWriter writer=null;
		try
		{
			writer = new PrintWriter(new FileWriter(new File(sdf.format(date) + ".txt")));
			BufferedReader reader = new BufferedReader(new FileReader(new File("E:\\shivani.txt")));
			writer.println("Modele_name     "+"\t Test "+ "\t Comments ");
			 writer.println("Reading TextFile "+"\t Pass " + "\t Reaading Successfull");
			List<String> list = new ArrayList<>();
//			 	reading text files for url and credentials and adding it into list
			reader.lines().forEach(l -> list.add(l));
//				iterating over list to get data
			Iterator<String> itr = list.iterator();
			if (itr.hasNext())
			{
				String homepage = itr.next();
				driver.navigate().to(homepage);
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.findElement(By.id("nav-link-accountList-nav-line-1")).click();
				driver.findElement(By.id("ap_email")).sendKeys(itr.next());
				WebElement continueButton = driver.findElement(By.id("continue"));
				continueButton.click();
				driver.findElement(By.id("ap_password")).sendKeys(itr.next());
				String url = driver.getCurrentUrl();
				WebElement signInButton = driver.findElement(By.id("signInSubmit"));
				signInButton.click();
				Thread.sleep(2000);
				String url1 = driver.getCurrentUrl();
//						if two url'are same it means login failed
				if (url.equals(url1))
				{
					 writer.println("Login "+"\t\t\t Fail"+"\t Login Failed ");
					driver.navigate().to(homepage);
				}
				else 
				{
					writer.println("Login"+"\t\t\t\t Pass"+"\t Login Successfull \n");
				}
//						reading excel file for products	
				File src = new File("E:\\Book.xlsx");
				FileInputStream stream = new FileInputStream(src);
				XSSFWorkbook workbook = new XSSFWorkbook(stream);
				XSSFSheet sheet = workbook.getSheetAt(0);
				for (int i = 0; i < 3; i++) {
//						calling searchProduct method to search for top product
					searchProduct(sheet.getRow(i).getCell(0).getStringCellValue(), writer);
				}
				driver.navigate().to(homepage);
				workbook.close();
				writer.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace(writer);
			e.printStackTrace();
			writer.close();
		}
	}

	public static void searchProduct(String productName, PrintWriter writer) {
		WebElement searchBar = driver.findElement(By.id("twotabsearchtextbox"));
		searchBar.sendKeys(productName);
		driver.findElement(By.id("nav-search-submit-button")).click();
//	getting list of top products
		List<WebElement> list = driver
				.findElements(By.xpath("//span[@class='a-size-medium a-color-base a-text-normal']"));
		if (list.isEmpty()) 
		{

			list = driver.findElements(By.xpath("//span[@class='a-size-base-plus a-color-base a-text-normal']"));
		}
		Iterator<WebElement> itr = list.iterator();
		int i = 1;
		writer.println(productName.toUpperCase() + "-");
		System.out.println(productName+"->");
//	iterating over a list and printing top 5 products in text file
		while (itr.hasNext() && i <= 5)
		{
			String product = itr.next().getText();
			System.out.println("Result " + i + " -> " + product);
			writer.println("Result " + i + " -> " + product);
			i++;
		}
		searchBar = driver.findElement(By.id("twotabsearchtextbox"));
		searchBar.clear();
		System.out.println("-------------");
		writer.println("-------------");
		writer.println("");
	}
}