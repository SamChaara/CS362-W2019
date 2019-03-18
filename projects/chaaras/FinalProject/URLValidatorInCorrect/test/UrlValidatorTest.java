

import junit.framework.TestCase;

//You can use this as a skeleton for your 3 different test approach
//It is an optional to use this file, you can generate your own test file(s) to test the target function!
// Again, it is up to you to use this file or not!





public class UrlValidatorTest extends TestCase {


   public UrlValidatorTest(String testName) {
      super(testName);
   }

   
   
   public void testManualTest()
   {
//You can use this function to implement your manual testing
	  boolean expected;
      boolean result;
	   
	  // Upper case IPV6
      UrlValidator urlVal = new UrlValidator();
       
      String ipv6_url_upper ="http://[FEDC:BA98:7654:3210:FEDC:BA98:7654:3210]";
      expected = true;
      result = urlVal.isValid(ipv6_url_upper);
      assertEquals(ipv6_url_upper, expected, result);
      
      // Lower case IPV6
      String ipv6_url_lower ="http://[fedc:ba98:7654:3210:fedc:ba98:7654:3210]";
      expected = true;
      result = urlVal.isValid(ipv6_url_lower);
      assertEquals(ipv6_url_lower, expected, result);
   
      // Schemes
      String[] schemes = {"ftp", "https", "http"};
      UrlValidator urlVal2 = new UrlValidator(schemes);
      
      // Valid scheme
      String goodScheme = "https://www.google.com:80/test1?action=view";
      expected = true;
      result = urlVal2.isValid(goodScheme);
      assertEquals(goodScheme, expected, result);

      // Invalid scheme
      String badScheme = "h3t://www.google.com:80/test1?action=view";
      expected = false;
      result = urlVal2.isValid(badScheme);
      assertEquals(badScheme, expected, result);
      
   }
   
   
   public void testYourFirstPartition()
   {
	 //You can use this function to implement your First Partition testing	   

   }
   
   public void testYourSecondPartition(){
		 //You can use this function to implement your Second Partition testing	   

   }
   //You need to create more test cases for your Partitions if you need to 
   
   public void testIsValid()
   {
	   //You can use this function for programming based testing

   }
   


}
