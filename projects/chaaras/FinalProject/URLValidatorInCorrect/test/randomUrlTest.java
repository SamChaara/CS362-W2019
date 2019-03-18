import java.lang.StringBuilder;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

//had to make a class because you cant pass by reference in Java
class reference {
	public int badUrl = 0;
}
public class randomUrlTest {
	/* This will randomly generate a url by generating several parts of a url. Each part
	 * will be individually checked to see if it is correct. If it is not, a flag (iHateJava test) 
	 * will be set to indicate to main whether or not it should test assertTrue or assertFalse.
	 */
	private static final String IPV6_PSUEDO_REGEX = "([0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F]:){7}[0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F]";
    private static final Pattern IPV6_PSUEDO_PATTERN = Pattern.compile(IPV6_PSUEDO_REGEX);
    private static final String IPV6_REGEX = "[0-9a-fA-F:]+";
    private static final String AUTHORITY_CHARS_REGEX = "\\p{Alnum}\\-\\.";
    private static final String USERINFO_CHARS_REGEX = "[a-zA-Z0-9%-._~!$&'()*+,;=]";
    private static final String PATH_REGEX = "^(/[-\\w:@&?=+,.!/~*'%$_;\\(\\)]*)?$";
    private static final Pattern PATH_PATTERN = Pattern.compile(PATH_REGEX);
    private static final String QUERY_REGEX = "^(\\S*)$";
    private static final Pattern QUERY_PATTERN = Pattern.compile(QUERY_REGEX);
    private static final String USERINFO_FIELD_REGEX = USERINFO_CHARS_REGEX + "+" + "(?::" + USERINFO_CHARS_REGEX + "*)?@"; 
    private static final String AUTHORITY_REGEX = "(?:\\[("+IPV6_REGEX+")\\]|(?:(?:"+USERINFO_FIELD_REGEX+")?([" + AUTHORITY_CHARS_REGEX + "]*)))(?::(\\d*))?(.*)?";
    private static final Pattern AUTHORITY_PATTERN = Pattern.compile(AUTHORITY_REGEX);
    private static final int PARSE_AUTHORITY_IPV6 = 1;
    private static final int MAX_UNSIGNED_16_BIT_INT = 0xFFFF;
    private static final int PARSE_AUTHORITY_PORT = 3; 
    private static final int PARSE_AUTHORITY_EXTRA = 4;
    private static final int PARSE_AUTHORITY_HOST_IP = 2;
    
	static String generateUrl(reference test) {
	    String ipv6Let = "0123456789ABCDEFabcdef::::"; //maybe higher chance of : make it correct every so often
	    String ipv6Control = "0123456789ABCDEFabcdef";
		String pathLet = "abcdefghijklmonpqrstuvwxyz/.";
	    String schemeStr = "fhtps";
		String letPeriod = "abcdefghijklmnopqrstuvwxyz";
		String queryLet[] = {"action", "view", "&", "="};
		
		double ipv6Chance = Math.random() * 100;
		//ipv6 url
		if (ipv6Chance < 5) {
			int len = (int)(Math.random() * 38 + 3); //min 3, max 40 length
			StringBuilder ipv6Build = new StringBuilder(40);
			for (int i = 0; i < len; i++) {
				int index = (int)(Math.random() * ipv6Let.length());
				ipv6Build.append(ipv6Let.charAt(index));
			}
			String authority = ipv6Build.toString();

			
			authority = "[" + authority + "]";

			final String authorityASCII = DomainValidator.unicodeToASCII(authority);

	        Matcher authorityMatcher = AUTHORITY_PATTERN.matcher(authorityASCII);
	        if (!authorityMatcher.matches()) {
	            test.badUrl += 1;
	        }

	        // We have to process IPV6 separately because that is parsed in a different group
	        String ipv6 = authorityMatcher.group(PARSE_AUTHORITY_IPV6);
	        if (ipv6 != null) {
	            InetAddressValidator inetAddressValidator = InetAddressValidator.getInstance();
	                if (!inetAddressValidator.isValidInet6Address(ipv6)) {
	                    test.badUrl += 1;
	                }
	        } else {
	            String port = authorityMatcher.group(PARSE_AUTHORITY_PORT);
	            if (port != null && port.length() > 0) {
	                try {
	                    int iPort = Integer.parseInt(port);
	                    if (iPort < 0 || iPort > MAX_UNSIGNED_16_BIT_INT) {
	                        test.badUrl += 1;
	                    }
	                } catch (NumberFormatException nfe) {
	                    test.badUrl += 1; // this can happen for big numbers
	                }
	            }
	        }

	        String extra = authorityMatcher.group(PARSE_AUTHORITY_EXTRA);
	        if (extra != null && extra.trim().length() > 0){
	            test.badUrl += 1;
	        }
	        authority = "http://" + authority;
			return authority;
		}
		else if (ipv6Chance < 10) {
			StringBuilder ipv6Build = new StringBuilder(39);
			int count = 1;
			for (int i = 0; i < 39; i++)
			{
				int index = (int)(Math.random() * ipv6Control.length());
				if (count % 5 == 0) {
					ipv6Build.append(":");
				}
				else {
					ipv6Build.append(ipv6Control.charAt(index));
				}
				count += 1;
			}
			String ipv6 = ipv6Build.toString();
			if (!IPV6_PSUEDO_PATTERN.matcher(ipv6).matches()) {
				test.badUrl += 1;
			}
			ipv6 = "http://[" + ipv6 + "]:80/index.html";
			return ipv6;
		}
		//normal url
		else {
		//randomly do 3-5 characters (ftp or http or https)
		double n = Math.floor(Math.random() * 3) + 3;
		int len = (int) n;
		StringBuilder schemeBuild = new StringBuilder(len);
		//generate random scheme (~ one correct per 1000 iterations)
		for (int i = 0; i < len; i++)
		{
			int index = (int) (Math.random() * schemeStr.length());
			schemeBuild.append(schemeStr.charAt(index));
		}
		String scheme = schemeBuild.toString();
		
		if (!scheme.equals("http") && !scheme.equals("ftp") && !scheme.equals("https")) {
			test.badUrl += 1;
		}


		scheme = scheme + "://";
		len = (int)(Math.random()*20); //can be null
		StringBuilder authorityBuild = new StringBuilder(len);
		for (int i = 0; i < len; i++)
		{
			int index = (int)(Math.random()* letPeriod.length());
			int rando = (int)(Math.random()*10);
			if (rando == 9) {
				authorityBuild.append(".");
			}
			else {
				authorityBuild.append(letPeriod.charAt(index));
			}
		}
		
		double addW = Math.random()*10;
		String authority;
		//randomly add www. to beginning of string
		if (addW > 4.5) {
			authority = "www." + authorityBuild;
		}
		else {
			authority = authorityBuild.toString();
		}
		len = (int)(Math.random() *2 + 2);
		StringBuilder domainBuild = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			int index = (int)(Math.random() * letPeriod.length());
			domainBuild.append(letPeriod.charAt(index));
		}
		String domain = domainBuild.toString();
		authority = authority + "." + domain;
		if (!isValidAuthority(authority)) {
			test.badUrl += 1;
		}
		//do random port, most of time will be a good port
		int randoPort = (int)(Math.random() * 90000 - 15000); //also checks negative ports
		if (randoPort > 65535 || randoPort < 0) {
			test.badUrl += 1;
		}
		String port = ":" + Integer.toString(randoPort);
		
		//build a random path
		len = (int)(Math.random() * 32);
		StringBuilder pathBuild = new StringBuilder(len);
		for (int i=0; i< len; i++) {
			int index = (int)(Math.random()* pathLet.length());
			pathBuild.append(pathLet.charAt(index));
		}
		String path = "/" + pathBuild;
		if (!PATH_PATTERN.matcher(path).matches()) {
			test.badUrl += 1;
		}
		//make a random query
		len = (int)(Math.random() * 10);
		String query = "";
		for (int i =0; i < len; i++) {
			int index = (int)(Math.random() * queryLet.length);
			query += queryLet[index];
		}
		query = "?" + query;
		if (!QUERY_PATTERN.matcher(query).matches()) {
			test.badUrl += 1;
		}
		String url = "";
			url = scheme + authority+ port + path + query;
		return url;
		}
	}
    protected static boolean isValidAuthority(String authority) {
        if (authority == null) {
            return false;
        }


        // convert to ASCII if possible
        final String authorityASCII = DomainValidator.unicodeToASCII(authority);

        Matcher authorityMatcher = AUTHORITY_PATTERN.matcher(authorityASCII);
        if (!authorityMatcher.matches()) {
            return false;
        }

        // We have to process IPV6 separately because that is parsed in a different group
        String ipv6 = authorityMatcher.group(PARSE_AUTHORITY_IPV6);
        if (ipv6 != null) {
            InetAddressValidator inetAddressValidator = InetAddressValidator.getInstance();
                if (!inetAddressValidator.isValidInet6Address(ipv6)) {
                    return false;
                }
        } else {
            String hostLocation = authorityMatcher.group(PARSE_AUTHORITY_HOST_IP);
            // check if authority is hostname or IP address:
            // try a hostname first since that's much more likely
            DomainValidator domainValidator = DomainValidator.getInstance();
            if (!domainValidator.isValid(hostLocation)) {
                // try an IPv4 address
                InetAddressValidator inetAddressValidator = InetAddressValidator.getInstance();
                if (!inetAddressValidator.isValidInet4Address(hostLocation)) {
                    // isn't IPv4, so the URL is invalid
                    return false;
                }
            }
        }

        String extra = authorityMatcher.group(PARSE_AUTHORITY_EXTRA);
        if (extra != null && extra.trim().length() > 0){
            return false;
        }

        return true;
    }

	public static void main(String[] args) {
		int i = 0;
		int goodCount = 0;
		int badCount = 0;
		int count = 1000000;
		while (i < count) {
			reference test = new reference();
			test.badUrl = 0;
			String url = randomUrlTest.generateUrl(test);
			UrlValidator urlVal = new UrlValidator(UrlValidator.ALLOW_2_SLASHES);
			boolean isVal = urlVal.isValid(url);
			if (test.badUrl > 0) {
				if(isVal != false) {
					System.out.println("incorrect true returned from isValid for " + url);
				}
				else {
					badCount += 1;
				}
			}
			else {
				if (isVal != true) {
					System.out.println("incorrect false from isValid for " + url);
				}
				else {
					goodCount += 1;
				}
			}
			i += 1;
		}
		System.out.println("Total good URLs generated and isValid matched: " + goodCount);
		System.out.println("Total bad URLs isValid matched: " + badCount);
		System.out.println("Total bugs: " + (count - badCount - goodCount));
	}
	
}