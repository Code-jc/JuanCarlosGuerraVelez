package com.LimelightHealtQA.LimelightProjectCarlos;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;

public class DriverFactory {

	private DriverFactory() {
		// Do-nothing..Do not allow to initialize this class from outside
	}

	private static DriverFactory instance = new DriverFactory();

	public static DriverFactory getInstance() {
		return instance;
	}

	ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>() // thread local
																	// driver
																	// object
																	// for
																	// webdriver
	{
		@Override
		protected WebDriver initialValue() {
			return null; // can be replaced with other browser drivers
		}
	};

	public WebDriver getDriver() // call this method to get the driver object
									// and launch the browser
	{
		return driver.get();
	}

	public WebDriver setDriver(String browserType) // call this method to get
													// the driver object and
													// launch the browser
	{
		String osName = (System.getProperty("os.name").toLowerCase().contains("mac") ? "mac" : "windows");
		if (browserType.equals("chrome")) {
			Path currentRelativePath = Paths.get("");
			String pathToDriver = currentRelativePath.toAbsolutePath().toString() +
					File.separator + "DriverEXE" + File.separator;
			if (osName.equals("windows")) {
				System.setProperty("webdriver.chrome.driver", pathToDriver + "chromedriver.exe");
			} else {
				System.setProperty("webdriver.chrome.driver", pathToDriver + "chromedriver");
			}
			driver.set(new ChromeDriver());
			int n = 10;
			// Seems to ocassionaly not be able to maximize window
			for (int i = 1; i <= n; i++) {
				try {
					driver.get().manage().window().maximize();
					break;
				} catch (WebDriverException e) {
					driver.set(new ChromeDriver());
					driver.get().manage().window().maximize();
				}
				if (i == n) {
					Assert.fail("Failed to Maximize window " + n + " times \n");
				}
			}
			driver.get().manage().timeouts()
					.implicitlyWait(15, TimeUnit.SECONDS);
		}

		return driver.get();
	}

	public void removeDriver() // Quits the driver and closes the browser
	{
		driver.get().quit();
		driver.remove();

	}
}
