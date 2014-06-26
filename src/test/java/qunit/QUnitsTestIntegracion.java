package qunit;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.junit.Assert.*;


@RunWith(Parameterized.class)
public class QUnitsTestIntegracion {
	private static WebDriver driver;
	private static String baseUrl;
	private static StringBuffer verificationErrors = new StringBuffer();
	private String testN;
	
	private static Logger logger = Logger.getLogger(QUnitsTestIntegracion.class.getName());
	
	private static Collection tests;
	static {
		
		int puerto = 8080;
		driver = new FirefoxDriver();
		baseUrl = "http://localhost:"+puerto;
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get(baseUrl + "/qunit-selenium/testQUnit.html");
		String running = driver.findElement(By.id("qunit-testresult")).getText();
		while(running == null || running.equalsIgnoreCase("Running:")){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				logger.log(Level.WARNING, "No pudo esperar");
			}
			running = driver.findElement(By.id("qunit-testresult")).getText();
		}
		
		List<WebElement> nombres = driver.findElements(By.className("test-name"));
		
		tests = new ArrayList<>();
		for(int i = 0; i < nombres.size(); i++) {
			String[] aux = new String[2];
			aux[0] = nombres.get(i).getText();
			aux[1] = ""+i;
			tests.add(aux);
		}
		
	}
	
	public QUnitsTestIntegracion(String nombre, String testN) {
		super();
		this.testN = testN;
	}

	@Parameters(name="{0} id:{1}")
	public static Collection testValues() {
		return tests;
	}
	

	@Test
	public void test1() throws Exception {
		String errores = driver.findElement(By.xpath("//li[@id='qunit-test-output"+testN+"']/strong/b/b")).getText();
				
		assertEquals("0", errores);
	}
	
	@AfterClass
	public static void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

}
