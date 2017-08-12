package com.LimelightHealtQA.LimelightProjectCarlos;

import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.google.common.collect.Ordering;

public class AmazonTest{
	
	  private static final By SEARCH_BOX           		= By.xpath("//input[@type='text'][@class='nav-input']");
	  private static final By SEARCH_BUTTON  			= By.xpath("//input[@type='submit'][@class='nav-input'][@value='Go']");	
	  private static final By CASE_MATERIAL_PLASTIC 	= By.xpath("//Span[@class='a-size-small a-color-base s-ref-text-link s-ref-link-cursor'][text()='Plastic']");
	  private static final By LOW_PRICE_BOX				= By.xpath("//input[@type='text'][@name='low-price']");
	  private static final By HIGH_PRICE_BOX			= By.xpath("//input[@type='text'][@name='high-price']");
	  private static final By GO_BUTTON 				= By.xpath("//input[@class='a-button-input'][@type='submit'][@value='Go']");
	
	
	  
	@Test 
	public void TestAmazon()
	{
		WebDriver driver = DriverFactory.getInstance().setDriver("chrome");
		
		
		driver.get("http://www.amazon.com");
		
		
		
		WebDriverWait wait = new WebDriverWait(driver,150);
        wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_BOX));
        
        WebElement searchBox = driver.findElement(SEARCH_BOX);
        Assert.assertTrue(searchBox.isDisplayed());
        WebElement searchBtn = driver.findElement(SEARCH_BUTTON);
        Assert.assertTrue(searchBtn.isDisplayed());
        
        searchBox.sendKeys("ipad air 2 case");
        searchBtn.click();
		
        
        //Refine search
       
        /**  Select Case Material  */
        wait.until(ExpectedConditions.visibilityOfElementLocated(CASE_MATERIAL_PLASTIC));
        WebElement casePastic = driver.findElement(CASE_MATERIAL_PLASTIC);
        Assert.assertTrue(casePastic.isDisplayed(),"Case Material: Plastic, is not displayed");
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", casePastic);
        casePastic.click();
        
        /**  Select Price - Min=25 Max=100  */
        WebElement minPrice = driver.findElement(LOW_PRICE_BOX);
        Assert.assertTrue(minPrice.isDisplayed(),"Min Price box is not displayed");
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", minPrice);
        minPrice.sendKeys("20");
        
        WebElement maxPrice = driver.findElement(HIGH_PRICE_BOX);
        Assert.assertTrue(maxPrice.isDisplayed(),"Max Price box is not displayed");
        maxPrice.sendKeys("100");
        
        WebElement goBtn = driver.findElement(GO_BUTTON);
        Assert.assertTrue(goBtn.isDisplayed(),"Go button is displayed");
        goBtn.click();
        
        
        /** Output the Name, Price and Score/Rating (Stars) of the first 5 results **/
        System.out.println("\n\n***   Name, Price and Score/Rating (Stars) of the first 5 results: \n");
        String [] name 				 = new String [5];
        String [] priceWhole		 = new String [5];
        String [] priceFractional	 = new String [5];
        String [] ratingStars		 = new String [5];
        float  [] realPrice          = new float  [5];
        float  [] rating			 = new float  [5];
        String [] Reccomendation	 = new String [5];
        int i 	= 0;
        float aux = 0;
        float fRating;
        String rec;
        String reccom;
     
        
        for (int prodNumber = 1; prodNumber <=5; prodNumber ++)
        {
        	//Get Product Name
        	WebElement productName = driver.findElement(
        	        By.xpath("(//ul[@id='s-results-list-atf']"
        	        	   + "//a[@class='a-link-normal s-access-detail-page  s-color-twister-title-link a-text-normal'])["
        	        	   + prodNumber + "]" ));
        	name [i] = productName.getText();
        	System.out.println(name[i]);
        	
        	//Get Price
        	boolean sxTag = true;
        	try
        	{
	        	WebElement productPriceWhole = driver.findElement(
	        	        By.xpath("(//span[@class='a-price-whole'])[" + prodNumber + "]"));
	        	priceWhole [i] = productPriceWhole.getText();
	        	sxTag = false;
	        	
        	}
        	catch (Exception e){}
	        
        	if (sxTag == true)
        	{
        		WebElement productPriceWhole = driver.findElement(
	        	        By.xpath("(//span[@class='sx-price-whole'])[" + prodNumber + "]"));
	        	priceWhole [i] = productPriceWhole.getText();
        		
        	}
        	
        	boolean tagSup = false;
        	try
        	{
	        	WebElement productPriceFractional = driver.findElement(
	        	        By.xpath("(//sup[@class='sx-price-fractional'])[" + prodNumber + "]"));
	        	priceFractional [i] = productPriceFractional.getText();
	        	tagSup = true;
        	}
        	catch (Exception e){}
        	
        	if (tagSup != true)
        	{
        		WebElement productPriceFractional = driver.findElement(
	        	        By.xpath("(//span[@class='a-price-fraction'])[" + prodNumber + "]"));
	        	priceFractional [i] = productPriceFractional.getText();
        		
        	}
        	
        	String price =  priceWhole[i] + "." +priceFractional [i];
        	realPrice [i] = Float.parseFloat(price);
        	
        	System.out.println("Price: $" + realPrice [i]);
        	
        	//Get Rating (stars)
           	WebElement stars = driver.findElement(
        	        By.xpath("((//a[@class='a-popover-trigger a-declarative'])//i//span)[" + prodNumber + "]"));
        	ratingStars [i] = stars.getAttribute("innerHTML");
        	System.out.println("Stars: " + ratingStars[i] + "\n");
        	
        	String StarsFloat = stars.getAttribute("innerHTML");
        	
        	String StarsToFloat = StarsFloat.substring(0,3);
        	
        	if (StarsFloat.contains(" o"))
	        	{
	        		StarsToFloat.substring(0, 2);
	        		fRating = Float.parseFloat(StarsToFloat);
	                rating[i] = fRating;
	        	}	
	        	else{
	        		fRating = Float.parseFloat(StarsToFloat);
	        		rating[i] = fRating;
	        	}
	        	reccom = price +" "+ StarsToFloat +" " + name [i] ;
	        	
	        	
	        	Reccomendation [i] = price +" "+ StarsToFloat +" " + name[i];
	        	
        	// Product is between $20 - $100?
        	
        	 Assert.assertTrue((realPrice [i] >= 20)|| (realPrice[i] <= 100),"Price is not between the range:  $20 - $100");
        	 StarsToFloat = "";
        	 StarsFloat = "";
        	 i++;
        	 
        	 
        	 
        	
        }
       
        String reccomend = "";
        
        for (int n = 0; n <=4; n++ )
        {
        	for (int m = n +1; m <= 4; m++)
        	{
        	 if (rating[m] >= rating[n] && realPrice[m] <= realPrice[n] )
        	 {
        		 reccomend = Reccomendation[m];
        		 reccomend.substring(0, 10);
        	 }	
        		
        	}
        }
        System.out.println("Reccomendation :" + reccomend);
        
        
        
        // Sort Products by Price 
        System.out.println("\n *** First 5 results ordered by price - High to Low:  ");
       boolean isSortedByPrice = false;
        try{
	        for (int p = 0; p < 5; p ++){
			    	for(int j = p + 1; j < 5; j ++){
				 		if (realPrice[p] < realPrice[j])
				 		{
				 			aux = realPrice[p];
				 			realPrice[p] = realPrice[j];
				 			realPrice[j] = aux;
				 		}
			 	}
		    	System.out.println(realPrice[p]);
	        }	
	        isSortedByPrice = true;
        }  
          catch (Exception e){
        	  isSortedByPrice = false;
        }
        
        
        
        
        
        
       
        		
        	
        
        
       
        
        //Assert Products ordered by Rating - High to Low  
        assertTrue(isSortedByPrice == true,"Products by price are not sorted.");
        
        
        // Sort Products by Rating Stars 
        System.out.println("\n *** First 5 results ordered by Rating Stars - High to Low: ");
        
        boolean isOrderedDescending = false;
        try {
        	 for (int p = 0; p < 5; p ++){
		    	for(int j = p + 1; j < 5; j ++){
			 		if (rating[p] < rating[j])
			 		{
			 			aux = rating[p];
			 			rating[p] = rating[j];
			 			rating[j] = aux;
			 			
			 		}
		    	}
		    	System.out.println(rating[p]);
        	}
        	 isOrderedDescending = true;
        }
        catch(Exception e){
        	isOrderedDescending = false;
        	}
        
       //Assert Products ordered by Rating - High to Low  
        assertTrue(isOrderedDescending == true,"Products by Rating are not sorted.");
        
        
        //Based on Score and Cost recommend the item a user should purchase
        
        System.out.println("\n *** Based on the Score and Cost, the item recommended is: \n");
        rec = String.valueOf(rating[0]);
        
        for (int p = 0; p <=Reccomendation.length - 1; p++)
        {
        	if (Reccomendation[p].contains(reccomend)  )
        	{
        		System.out.println(Reccomendation[p]);
        		String pLinkText = Reccomendation[p];
        		String link = pLinkText.substring(10, 40);
        		WebElement prodRec = driver.findElement(By.partialLinkText(link));
        		prodRec.click();
        	}
        }
        
       
	
		
	}

	 @AfterClass
 	public void TearDown()
 	{
 		
 	}

	
	
	

}
