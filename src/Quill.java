import io.ctc.utils.FileUtil;

import java.io.File;
import java.util.regex.MatchResult;

import junit.framework.TestCase;

import com.google.re2j.Matcher;
import com.google.re2j.Pattern;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.AutomatonMatcher;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.RunAutomaton;


public class Quill extends TestCase {
	
	
	public interface IMatcher {
		public String replaceAll(CharSequence txt);
	}
	public interface IPattern {
		public IMatcher matcher(String regex);
	}

	public static class Replacer {
		String m_regex;
		String m_replace;
		java.util.regex.Pattern m_javaPattern;
		com.google.re2j.Pattern m_re2jPattern;
		
		public Replacer(String regex, String replace) {
			m_regex = regex;
			m_replace = replace;
			m_javaPattern = java.util.regex.Pattern.compile(m_regex);
			m_re2jPattern = com.google.re2j.Pattern.compile(m_regex);
		}
		
		public String applyJava(String text) {
			return m_javaPattern.matcher(text).replaceAll(m_replace);
		}
		
		public String applyRe2j(String text) {
			return m_re2jPattern.matcher(text).replaceAll(m_replace);
		}
		
		public String apply(String text, String type) {
			if ("re2j".equals(type)) {
				return applyRe2j(text);
			}
			
			return applyJava(text);
		}
	}
	
	public static class REPLACERS {
		public static Replacer singleOpeningSmartQuotes = new Replacer("(\\s|“|\")'", "$1‘");
		public static Replacer singleClosingSmartQuotes = new Replacer("'", "’");
		public static Replacer doubleOpeningSmartQuotes = new Replacer("(\\s|‘)\"", "$1“");
		public static Replacer doubleClosingSmartQuotes = new Replacer("\"", "”");
		public static Replacer removeCarriageReturns = new Replacer("\r", "");
		public static Replacer removeComments = new Replacer("\\/\\*.*?\\*\\/", "");
		public static Replacer removeSpacesFromBlankLines = new Replacer("\\n[ \\t]+\\n", "\n\n");
		public static Replacer actHeaders = new Replacer("\n#(.+?)\n", "\n<h2 class=\"act\">$1</h2>\n");
		public static Replacer sceneHeaders = new Replacer("\\n\\.(.+?)\\n", "\n<h2 class=\"scene\">$1</h2>\n");
		public static Replacer characters = new Replacer("\\n([A-Z0-9]+)(.*?):\\s*?\\n", "\n<div class=\"character\">$1$2</div>\n");
		//public static Replacer blockAction = new Replacer("\\n\\n[\\s]*?\\[(.*?)\\][\\s]*?(?=\\n\\n|\\n\\<)", "\n\n<div class=\"action\">$1</div>\n\n");
		public static Replacer dialog = new Replacer("\\n\\n*([^\\<\\>]+)\\n*\\n", "<div class=\"dialogue\">" + "$1" + "</div>\n");
		public static Replacer fixDialog = new Replacer("\\<div class=\"dialogue\">\\s*\\<\\/div>", "");
		public static Replacer parenthetical = new Replacer("\\[(.*?)\\]", "<span class=\"action\">$1</span>");
		public static Replacer bold = new Replacer("\\*\\*([^\n]+?)\\*\\*", "<strong>$1</strong>");
		public static Replacer italic = new Replacer("\\*([^\\n]+?)\\*", "<em>$1</em>");
		public static Replacer underline = new Replacer("\\_([^\\n]+?)_", "<u>$1</u>");

//		
		/* Characters */
//		.replaceAll("\\n([A-Z0-9]+)(.*?):\\s*?\\n", "\n<div class=\"character\">$1$2</div>\n")
					
		/* Forced Characters */
//		//.replaceAll(/\n@(.*?):\s*?\n/g, '\n<div class="character">$1</div>\n')
//		.replaceAll("\\n\\n[\\s]*?\\[(.*?)\\][\\s]*?(?=\\n\\n|\\n\\<)", "\n\n<div class=\"action\">$1</div>\n\n") /* Block action action */
//
//		.replaceAll("\\n\\n*([^\\<\\>]+)\\n*\\n", "<div class=\"dialogue\">" + "$1" + "</div>\n") /* All dialogue */
//		.replaceAll("\\<div class=\"dialogue\">\\s*\\<\\/div>", "") /* Get rid of empty dialoAll dialogue */
//		.replaceAll("\\[(.*?)\\]", "<span class=\"action\">$1</span>") /*  parentheticals */
//		.replaceAll("\\*\\*([^\n]+?)\\*\\*", "<strong>$1</strong>") /* bold emphasis */
//		.replaceAll("\\*([^\\n]+?)\\*", "<em>$1</em>") /* italic emphasis */
//		.replaceAll("\\_([^\\n]+?)_", "<u>$1</u>") /* underline  */
	};
	
	public String processQuill(String text, String type) {
		text = REPLACERS.singleOpeningSmartQuotes.apply(text, type);
		text = REPLACERS.singleClosingSmartQuotes.apply(text, type);
		text = REPLACERS.doubleOpeningSmartQuotes.apply(text, type);
		text = REPLACERS.doubleClosingSmartQuotes.apply(text, type);
		text = REPLACERS.removeCarriageReturns.apply(text, type);
		text = REPLACERS.removeComments.apply(text, type);
		text = REPLACERS.removeSpacesFromBlankLines.apply(text, type);
		text = REPLACERS.actHeaders.apply(text, type);
		text = REPLACERS.sceneHeaders.apply(text, type);
		
		
		return text;
	}
	
	public void testSimpleRegex() {
		String orig = FileUtil.contentsAsString(new File("src/ed.txt"));
		String text = orig;
		text = "\n\n" + text + "\n\n";
		
		long start = System.currentTimeMillis();
		text = processQuill(text, "java");
		long end = System.currentTimeMillis();
		System.out.println("java: " + (end - start));

		// re2j
		text = orig;
		start = System.currentTimeMillis();
		text = processQuill(text, "re2j");
		end = System.currentTimeMillis();
		System.out.println("re2: " + (end - start));
	}
	
	public static String script = FileUtil.contentsAsString(new File("src/ed.txt"));
	
	public void testQuillRegex() {
		String orig = FileUtil.contentsAsString(new File("src/ed.txt"));
		String text = orig;
		text = "\n\n" + text + "\n\n";
		
		long start = System.currentTimeMillis();

		//String output = "<div class=\"script\">" + "\n" + 
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
	

	
	public String simpleReplace(final String text, final String regex, final String rep) {
		RegExp r = new RegExp(regex);
		Automaton a = r.toAutomaton();
		RunAutomaton ra = new RunAutomaton(a);
		return simpleReplace(text, ra, rep);
	}
	
	public String simpleReplace(final String text, final RunAutomaton ra, final String rep) {
		AutomatonMatcher m = ra.newMatcher(text);
		StringBuilder sb = new StringBuilder();
		int cursor = 0;
		
		while (m.find()) {
			sb.append(text.substring(cursor, m.start()));
			sb.append(rep);
			cursor = m.end();
		}
		
		sb.append(text.substring(cursor));
		
		return sb.toString();
	}
	
	public void testAutomaton() {
		String s = "abccccdefbzxcvb";
		RegExp r = new RegExp("b(c)*");
		Automaton a = r.toAutomaton();
		RunAutomaton ra = new RunAutomaton(a);
		AutomatonMatcher m = ra.newMatcher(s);
		
		int i = 0;
		while (m.find()) {
			i++;
			System.out.println("MATCH #" + i);
			//System.out.println("Group count:  " + m.groupCount());
			System.out.println("Start    ...  " + m.start());
			System.out.println("End  ... ...  " + m.end());

			MatchResult mr = m.toMatchResult();
			//System.out.println("Count:   ...  " + mr.groupCount());
			System.out.println("Group 0  ...  " + mr.group());
			System.out.println("");
		}
	}
	
	public void testAutomatonReplace() {
		String res = simpleReplace("Hello", "e.*o", "4");
		System.out.println(res);
	}
	
	// consider smart quotes
	// replace (\\s|“|\")' with $1‘.
	// The pattern there is to find one expression, and do a simple replace with another
	// eg find (\\s|“|\")', then replace the matching text ' with ‘
	// find regex1, 
	
	// a+(.+)a+    replace with   [$2]
	// replace *...* with [...]
	public static class Timer {
		static long m_start;
		public static void start() {
			m_start = System.currentTimeMillis();
		}
		
		public static void print(String str) {
			long end = System.currentTimeMillis();
			System.out.println(str + ": " + (end - m_start) + "ms");
		}
	}
	
	public class CTCTimer {
		long m_start;
		
		public CTCTimer() {
			reset();
		}
		
		public long end() {
			long t = get();
			reset();
			return t;
		}
		
		public long get() {
			return System.currentTimeMillis() - m_start;
		}
		
		public long print(String prefix) {
			long t = get();
			System.out.println(prefix + ": " + t + "ms");
			reset();
			return t;
		}
		
		public void reset() {
			m_start = System.currentTimeMillis();
		}
	}
	public void testReplacingOpeningQuotes() {
		RegExp r = new RegExp("(\\s|“|\\\")'");
		Automaton a = r.toAutomaton();
		RunAutomaton ra = new RunAutomaton(a);
		java.util.regex.Pattern p = java.util.regex.Pattern.compile("(\\s|“|\")'");
		String text = script;
		
		CTCTimer timer = new CTCTimer();
		int warm = 20;
		int max = 200;
		
		// Automaton test with compiling
		for (int i = 0; i < warm; i++) {
			replaceAll(text, "(\\s|“|\\\")'", "(\\s|“|\")'", "$1‘");
		}
		timer.reset();
		for (int i = 0; i < max; i++) {
			replaceAll(text, "(\\s|“|\\\")'", "(\\s|“|\")'", "$1‘");
		}
		timer.print("Automaton with compiling");
		
		
		// Automaton test without compiling
		text = script;
		for (int i = 0; i < warm; i++) {
			replaceAll(text, ra, p, "$1‘");
		}
		timer.reset();
		for (int i = 0; i < max; i++) {
			replaceAll(text, ra, p, "$1‘");
		}
		timer.print("Automaton");

		// java regex test
		text = script;
		for (int i = 0; i < warm; i++) {
			p.matcher(text).replaceAll("$1‘");
		}
		timer.reset();
		for (int i = 0; i < max; i++) {
			p.matcher(text).replaceAll("$1‘");
		}
		timer.print("Java");
		
		text = script;
		timer.reset();
		text = REPLACERS.singleOpeningSmartQuotes.apply(text, "re2j");
		Timer.print("Re2j");
	}
	
	public RunAutomaton makeRunAutomaton(String regex) {
		RegExp r = new RegExp(regex);
		Automaton a = r.toAutomaton();
		RunAutomaton ra = new RunAutomaton(a);
		
		return ra;
	}
	
	/**
	 * Could do with a replacer pattern that uses Automaton for the initial
	 * match and then standard Java regex replacement within. This could offer a
	 * significant performance win without much complexity.
	 * @return 
	 * 
	 * @Rollo
	 */
	public String replaceAll(String text, String autoRegex, String javaRegex, String rep) {
		RunAutomaton ra = makeRunAutomaton(autoRegex);
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(javaRegex);
		
		return replaceAll(text, ra, p, rep);
	}
	
	public String replaceAll(String text, RunAutomaton ra, java.util.regex.Pattern p, String rep) {
		StringBuilder out = new StringBuilder();
		
		AutomatonMatcher m = ra.newMatcher(text);
		int cursor = 0;
		
		while (m.find()) {
			out.append(text.substring(cursor, m.start()));
			String subseq = text.substring(m.start(), m.end());
			out.append(p.matcher(subseq).replaceAll(rep));
			cursor = m.end();
		}
		
		out.append(text.substring(cursor));
		return out.toString();
	}
}