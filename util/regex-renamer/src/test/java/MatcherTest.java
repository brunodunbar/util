import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MatcherTest {

	public static void main(String[] args) {
		
		Pattern pattern = Pattern.compile("Test (\\d+).mkv");
		Matcher matcher = pattern.matcher("Test 01.mkv"); 
		
		
	
		System.out.println(matcher.matches());
		System.out.println(matcher.groupCount());
		System.out.println(matcher.group(1));
	}
	
}
