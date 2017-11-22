import io.ctc.utils.FileUtil;

import java.io.File;

import junit.framework.TestCase;

import com.google.re2j.Matcher;
import com.google.re2j.Pattern;


public class JustTesting extends TestCase {

	public void testQuillRegex() {

		String orig = FileUtil.contentsAsString(new File("src/ed.txt"));
		long start = System.currentTimeMillis();
		String text = orig;
		//System.out.println(text);
		text = "\n\n" + text + "\n\n"; /* add new lines at start and end*/
		//String output = "<div class=\"script\">" + "\n" + 
				// smart quotes
				text = java.util.regex.Pattern.compile("(\\s|“|\")'").matcher(text).replaceAll("$1‘");
				text = java.util.regex.Pattern.compile("'").matcher(text).replaceAll("’");
				text = java.util.regex.Pattern.compile("(\\s|‘)\"").matcher(text).replaceAll("$1“");
				// replace single quotes with opening smart quotes
//				.replaceAll("(\\s|“|\")'", "$1‘")
				
				// replace single quotes with closing smart quotes
//				.replaceAll("'", "’")
				
				// replaceAll double quotes with opening smart quotes
//				.replaceAll("(\\s|‘)\"", "$1“") 
				
				// replaceAll double quotes with closing smart quotes
//				.replaceAll("\"", "”")
//
				/* remove carriage returns */
//				.replaceAll("\r", "")
				
				/* remove comments */
//				.replaceAll("\\/\\*.*?\\*\\/", "")
				
				/* remove spaces from blank lines */
//				.replaceAll("\\n[ \\t]+\\n", "\n\n")
				
				/* Act header must start with a # */
//				.replaceAll(/\n#(.+?)\n/g, '\n<h2 class="act">$1</h2>\n')
				
				/* Sceneheaders must start with a . */
//				.replaceAll("\\n\\.(.+?)\\n", "\n<h2 class=\"scene\">$1</h2>\n")
//				
				/* Characters */
//				.replaceAll("\\n([A-Z0-9]+)(.*?):\\s*?\\n", "\n<div class=\"character\">$1$2</div>\n")
							
				/* Forced Characters */
//				//.replaceAll(/\n@(.*?):\s*?\n/g, '\n<div class="character">$1</div>\n')
//				.replaceAll("\\n\\n[\\s]*?\\[(.*?)\\][\\s]*?(?=\\n\\n|\\n\\<)", "\n\n<div class=\"action\">$1</div>\n\n") /* Block action action */
//
//				.replaceAll("\\n\\n*([^\\<\\>]+)\\n*\\n", "<div class=\"dialogue\">" + "$1" + "</div>\n") /* All dialogue */
//				.replaceAll("\\<div class=\"dialogue\">\\s*\\<\\/div>", "") /* Get rid of empty dialoAll dialogue */
//				.replaceAll("\\[(.*?)\\]", "<span class=\"action\">$1</span>") /*  parentheticals */
//				.replaceAll("\\*\\*([^\n]+?)\\*\\*", "<strong>$1</strong>") /* bold emphasis */
//				.replaceAll("\\*([^\\n]+?)\\*", "<em>$1</em>") /* italic emphasis */
//				.replaceAll("\\_([^\\n]+?)_", "<u>$1</u>") /* underline  */
//				+ "\n</div>";
		//output;
		long end = System.currentTimeMillis();
		System.out.println("java: " + (end - start));

		// re2j
		start = System.currentTimeMillis();
		text = orig;
		text = Pattern.compile("(\\s|“|\")'").matcher(text).replaceAll("$1‘");
		text = Pattern.compile("'").matcher(text).replaceAll("’");
		text = Pattern.compile("(\\s|‘)\"").matcher(text).replaceAll("$1“");

		end = System.currentTimeMillis();
		System.out.println("re2: " + (end - start));
		//System.out.print(text);

	}
}