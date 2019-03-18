

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
   
   public void combineAndValidate(UrlValidator validator, String[] scheme, String[] host, String[] port, String[] path, boolean expected)
   {
	   boolean result;
	   
	   for(int i = 0; i < scheme.length; i++)
	   {
		   for(int k = 0; k < host.length; k++)
		   {
			   for(int l = 0; l < port.length; l++)
			   {
				   for(int m = 0; m < path.length; m++)
				   {
						   String URL = scheme[i] + host[k] + port[l] + path[m];
						   result = validator.isValid(URL);
						   assertEquals(URL, expected, result);
				   }
			   }
		   }
	   }
   }
   
   public void testIsValid()
   {
	   boolean result;

	   //Start testing using the default options of the URL Validator
	   UrlValidator urlValDefault = new UrlValidator();

	   String[] validScheme = {"http://", "https://", "ftp://"};
	   String[] invalidScheme = {"", "file://", "mailto://", "irc://", "http://", "http:/", "http//", "http:", "http/"};
 
	   String[] validHost = {"www.google.com", "google.com", "WWW.GOOGLE.COM","test.google.com", "0.0.0.0", "255.255.255.255", "[0:0:0:0:0:0:0:1]", "[0:0:0:0:0:0:0:0]", "[2001:0db8:85a3:0000:0000:8a2e:0370:7334]", "[2001:db8:85a3:0:0:8a2e:370:7334]", "[2001:DB8:85A3:0:0:8A2E:370:7334]", "[2001:db8:85a3::8a2e:370:7334]", "[::1]", "[::]"};
	   String[] invalidHost = {".www.google.com", ".com.", "0.0.0", "256.255.255.255", "[0:0:0:0:0:0:0:Z]", "[0:0:0:0:0:0:0:z]", "[K001:0db8:85a3:0000:0000:8a2e:0370:7334]"};
	   
	   String[] validPort = {"", ":80", ":8080", ":1", ":65535"};
	   String[] invalidPort = {":99999999999", ":abc", ":-1", ":65536"};
	   
	   String[] validPath = {"/test/", "/test", "/1test/", "/test1/", "/hello/world/", "/hello/world", "/hello/world.html"};
	   String[] invalidPath = {"/../", "/../hello/world/", "/hello/..//world/", "/hello//world"};
	   
	   result = urlValDefault.isValid(null);
	   assertFalse("null shouldn't be a valid url", result);
	   
	   result = urlValDefault.isValid("http://www.google.com/");
	   assertTrue("http://www.google.com/ is a known good url", result);
	   
	   result = urlValDefault.isValid("http://www.google.com");
	   assertTrue("http://www.google.com is a known good url", result);
	   
	   result = urlValDefault.isValid("http://www.google.com/?task=view");
	   assertTrue("http://www.google.com/?task=view is a known good url", result);
	   
	   result = urlValDefault.isValid("http://username:password@www.google.com/");
	   assertTrue("http://username:password@www.google.com/ is a known good url", result);
	   
	   result = urlValDefault.isValid("http://username:@www.google.com/");
	   assertTrue("http://username:@www.google.com/ is a known good url", result);
	   
	   result = urlValDefault.isValid("http://:password@www.google.com/");
	   assertFalse("http://:password@www.google.com/ is a known bad url", result);
	   
	   result = urlValDefault.isValid("http://:@www.google.com/");
	   assertFalse("http://:@www.google.com/ is a known bad url", result);
	   
	   result = urlValDefault.isValid("HTTP://WWW.GOOGLE.COM");
	   assertTrue("URLs are not case sensitive", result);
	   
	   
	   combineAndValidate(urlValDefault, validScheme, validHost, validPort, validPath, true);
	   combineAndValidate(urlValDefault, validScheme, validHost, validPort, invalidPath, false);
	   combineAndValidate(urlValDefault, validScheme, validHost, invalidPort, validPath, false);
	   combineAndValidate(urlValDefault, validScheme, validHost, invalidPort, invalidPath, false);
	   
	   combineAndValidate(urlValDefault, validScheme, invalidHost, validPort, validPath, false);
	   combineAndValidate(urlValDefault, validScheme, invalidHost, validPort, invalidPath, false);
	   combineAndValidate(urlValDefault, validScheme, invalidHost, invalidPort, validPath, false);
	   combineAndValidate(urlValDefault, validScheme, invalidHost, invalidPort, invalidPath, false);
	   
	   combineAndValidate(urlValDefault, invalidScheme, validHost, validPort, validPath, false);
	   combineAndValidate(urlValDefault, invalidScheme, validHost, validPort, invalidPath, false);
	   combineAndValidate(urlValDefault, invalidScheme, validHost, invalidPort, validPath, false);
	   combineAndValidate(urlValDefault, invalidScheme, validHost, invalidPort, invalidPath, false);
	   
	   combineAndValidate(urlValDefault, invalidScheme, invalidHost, validPort, validPath, false);
	   combineAndValidate(urlValDefault, invalidScheme, invalidHost, validPort, invalidPath, false);
	   combineAndValidate(urlValDefault, invalidScheme, invalidHost, invalidPort, validPath, false);
	   combineAndValidate(urlValDefault, invalidScheme, invalidHost, invalidPort, invalidPath, false);
   }
}
