package esoteric.jsfuck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Adapted from official JSFuck creation code
 * Refs: 
 * JSFuck generator code : https://github.com/aemkei/jsfuck/blob/master/jsfuck.js
 * JS regex to Java converter : https://regex101.com/
 */
public class JSFuckCompiler {
	public static final int MIN = 32, MAX = 126;
	public static final Map<String, String> SIMPLE, CONSTRUCTORS, MAPPING;
	public static final String GLOBAL = "Function(\"return this\")()";
	
	static {
		SIMPLE = new HashMap<>();
		SIMPLE.put("false", "![]");
	    SIMPLE.put("true", "!![]");
	    SIMPLE.put("undefined", "[][[]]");
	    SIMPLE.put("NaN", "+[![]]");
	    SIMPLE.put("Infinity", "+(+!+[]+(!+[]+[])[!+[]+!+[]+!+[]]+[+!+[]]+[+[]]+[+[]]+[+[]])"); // +"1e1000"
	    
	    CONSTRUCTORS = new HashMap<>();
	    CONSTRUCTORS.put("Array", "[]");
	    CONSTRUCTORS.put("Number", "(+[])");
	    CONSTRUCTORS.put("String", "([]+[])");
	    CONSTRUCTORS.put("Boolean", "(![])");
	    CONSTRUCTORS.put("Function", "[][\"flat\"]");
	    CONSTRUCTORS.put("RegExp", "Function(\"return/\"+false+\"/\")()");
	    CONSTRUCTORS.put("Object", "[][\"entries\"]()");
	    
	    MAPPING = new HashMap<>();
	    MAPPING.put("a", "(false+\"\")[1]");
	    MAPPING.put("b", "([][\"entries\"]()+\"\")[2]");
	    MAPPING.put("c", "([][\"flat\"]+\"\")[3]");
	    MAPPING.put("d", "(undefined+\"\")[2]");
	    MAPPING.put("e", "(true+\"\")[3]");
	    MAPPING.put("f", "(false+\"\")[0]");
	    MAPPING.put("g", "(false+[0]+String)[20]");
	    MAPPING.put("h", "(+(101))[\"to\"+String[\"name\"]](21)[1]");
	    MAPPING.put("i", "([false]+undefined)[10]");
	    MAPPING.put("j", "([][\"entries\"]()+\"\")[3]");
	    MAPPING.put("k", "(+(20))[\"to\"+String[\"name\"]](21)");
	    MAPPING.put("l", "(false+\"\")[2]");
	    MAPPING.put("m", "(Number+\"\")[11]");
	    MAPPING.put("n", "(undefined+\"\")[1]");
	    MAPPING.put("o", "(true+[][\"flat\"])[10]");
	    MAPPING.put("p", "(+(211))[\"to\"+String[\"name\"]](31)[1]");
	    MAPPING.put("q", "(\"\")[\"fontcolor\"]([0]+false+\")[20]");
	    MAPPING.put("r", "(true+\"\")[1]");
	    MAPPING.put("s", "(false+\"\")[3]");
	    MAPPING.put("t", "(true+\"\")[0]");
	    MAPPING.put("u", "(undefined+\"\")[0]");
	    MAPPING.put("v", "(+(31))[\"to\"+String[\"name\"]](32)");
	    MAPPING.put("w", "(+(32))[\"to\"+String[\"name\"]](33)");
	    MAPPING.put("x", "(+(101))[\"to\"+String[\"name\"]](34)[1]");
	    MAPPING.put("y", "(NaN+[Infinity])[10]");
	    MAPPING.put("z", "(+(35))[\"to\"+String[\"name\"]](36)");

	    MAPPING.put("A", "(NaN+[][\"entries\"]())[11]");
	    MAPPING.put("B", "(+[]+Boolean)[10]");
	    MAPPING.put("C", "Function(\"return escape\")()((\"\")[\"italics\"]())[2]");
	    MAPPING.put("D", "Function(\"return escape\")()([][\"flat\"])[\"slice\"](\"-1\")");
	    MAPPING.put("E", "(RegExp+\"\")[12]");
	    MAPPING.put("F", "(+[]+Function)[10]");
	    MAPPING.put("G", "(false+Function(\"return Date\")()())[30]");
	    MAPPING.put("H", null);
	    MAPPING.put("I", "(Infinity+\"\")[0]");
	    MAPPING.put("J", null);
	    MAPPING.put("K", null);
	    MAPPING.put("L", null);
	    MAPPING.put("M", "(true+Function(\"return Date\")()())[30]");
	    MAPPING.put("N", "(NaN+\"\")[0]");
	    MAPPING.put("O", "(+[]+Object)[10]");
	    MAPPING.put("P", null);
	    MAPPING.put("Q", null);
	    MAPPING.put("R", "(+[]+RegExp)[10]");
	    MAPPING.put("S", "(+[]+String)[10]");
	    MAPPING.put("T", "(NaN+Function(\"return Date\")()())[30]");
	    MAPPING.put("U", "(NaN+Object()[\"to\"+String[\"name\"]][\"call\"]())[11]");
	    MAPPING.put("V", null);
	    MAPPING.put("W", null);
	    MAPPING.put("X", null);
	    MAPPING.put("Y", null);
	    MAPPING.put("Z", null);

	    MAPPING.put(" ", "(NaN+[][\"flat\"])[11]");
	    MAPPING.put("!", null);
	    MAPPING.put("\"", "(\"\")[\"fontcolor\"]()[12]");
	    MAPPING.put("#", null);
	    MAPPING.put("$", null);
	    MAPPING.put("%", "Function(\"return escape\")()([][\"flat\"])[21]");
	    MAPPING.put("&", "(\"\")[\"fontcolor\"](\")[13]");
	    MAPPING.put("\'", null);
	    MAPPING.put("(", "([][\"flat\"]+\"\")[13]");
	    MAPPING.put(")", "([0]+false+[][\"flat\"])[20]");
	    MAPPING.put("*", null);
	    MAPPING.put("+", "(+(+!+[]+(!+[]+[])[!+[]+!+[]+!+[]]+[+!+[]]+[+[]]+[+[]])+[])[2]");
	    MAPPING.put(",", "[[]][\"concat\"]([[]])+\"\"");
	    MAPPING.put("-", "(+(.+[0000001])+\"\")[2]");
	    MAPPING.put(".", "(+(+!+[]+[+!+[]]+(!![]+[])[!+[]+!+[]+!+[]]+[!+[]+!+[]]+[+[]])+[])[+!+[]]");
	    MAPPING.put("/", "(false+[0])[\"italics\"]()[10]");
	    MAPPING.put(":", "(RegExp()+\"\")[3]");
	    MAPPING.put(";", "(\"\")[\"fontcolor\"](NaN+\")[21]");
	    MAPPING.put("<", "(\"\")[\"italics\"]()[0]");
	    MAPPING.put("=", "(\"\")[\"fontcolor\"]()[11]");
	    MAPPING.put(">", "(\"\")[\"italics\"]()[2]");
	    MAPPING.put("?", "(RegExp()+\"\")[2]");
	    MAPPING.put("@", null);
	    MAPPING.put("[", "([][\"entries\"]()+\"\")[0]");
	    MAPPING.put("\\", "(RegExp(\"/\")+\"\")[1]");
	    MAPPING.put("]", "([][\"entries\"]()+\"\")[22]");
	    MAPPING.put("^", null);
	    MAPPING.put("_", null);
	    MAPPING.put("`", null);
	    MAPPING.put("{", "(true+[][\"flat\"])[20]");
	    MAPPING.put("|", null);
	    MAPPING.put("}", "([][\"flat\"]+\"\")[\"slice\"](\"-1\")");
	    MAPPING.put("~", null);
	    
	    fillMissingDigits();
	    replaceMap();
	    replaceStrings();
	}

	private static void fillMissingDigits() {
		int number, i;
		String output;
		
		for (number = 0; number < 10; number++) {
			output = "+[]";
			if (number > 0) output = "+!" + output;
			for (i = 1; i<number; i++) output = "+!+[]" + output;
			if (number > 1) output = output.substring(1);
			
			MAPPING.put(String.valueOf(number), "[" + output + "]");
		}
	}
	
	private static void replaceMap() {
		new ReplaceMap();
	}
	
	private static void replaceStrings() {
		new ReplaceStrings();
	}
	
	private static String escapeSequence(String c) {
		int cc = c.charAt(0);
		if (cc < 256) 
			return "\\" + Integer.toString(cc, 8);
		else {
			String cc16 = Integer.toString(cc, 16);
			return "\\u" + ("0000" + cc16).substring(cc16.length());
		}
	}
	
	private static String escapeSequenceForReplace(String c) {
		return escapeSequence(c).replace("\\", "t");
	}
	
	public static String encode(String input) {
		return encode(input, false, false);
	}
	
	public static String encode(String input, boolean wrapWithEval, boolean runInParentScope) {
		List<String> output = new ArrayList<>();
		
		if (input == null)
			return "";
		
		String unmapped = "";
		for (Entry<String, String> entry : MAPPING.entrySet()) {
			if (MAPPING.get(entry.getKey()) != null) {
				if (entry.getKey().equals("["))
					unmapped += "\\";
				unmapped += entry.getKey();
			}
		}
		
		String chars = ".*+?^${}()|[]\\";
		StringBuilder temp = new StringBuilder();
		char character;
		for (int i=0; i<unmapped.length(); i++) {
			character = unmapped.charAt(i);
			if (chars.contains(String.valueOf(character)))
					temp.append("\\");
			temp.append(character);
		}
		unmapped = temp.toString();
		unmapped = "[^" + unmapped + "]";
		Matcher matcher = Pattern.compile(unmapped).matcher(input);
		int unmappedCharactersCount = 0;
		while (matcher.find())
			unmappedCharactersCount++;
		if (unmappedCharactersCount > 1) {
			input = Pattern.compile("[^0123456789.adefilnrsuN]")
					.matcher(input)
					.replaceAll(mr -> escapeSequenceForReplace(mr.group()));
		} else if (unmappedCharactersCount > 0) {
			input = Pattern.compile("[\"\\\\]")
					.matcher(input)
					.replaceAll(mr -> escapeSequence(mr.group()));
			input = Pattern.compile(unmapped)
					.matcher(input)
					.replaceAll(mr -> escapeSequence(mr.group()));
		}
		
		String r = "";
		for (Entry<String, String> entry : SIMPLE.entrySet())
			r += entry.getKey() + "|";
		r += ".";
		
		Pattern.compile(r)
			.matcher(input)
			.replaceAll(mr -> {
				String c = mr.group(),
					replacement = SIMPLE.get(c);
				if (replacement != null)
					output.add("(" + replacement + "+[])");
				else {
					replacement = MAPPING.get(c);
					if (replacement != null)
						output.add(replacement);
					else
						throw new IllegalArgumentException("Found unmapped character: "+c);
				}
				return "";	// we don't care about actual string
			});
		
		String result = String.join("+", output);
		
		if (Pattern.compile("^\\d$").matcher(input).find())
			result += "+[]";
		
		if (unmappedCharactersCount > 1)
			result = "(" + result + ")[" + encode("split") + "](" + encode ("t") + ")[" 
						+ encode("join") +"](" + encode("\\") + ")";
		
		if (unmappedCharactersCount > 0)
			result = "[][" + encode("flat") + "]"+
				      "[" + encode("constructor") + "]" +
				      "(" + encode("return\"") + "+" + result + "+" + encode("\"") + ")()";
		
		if (wrapWithEval) {
			if (runInParentScope)
				result = "[][" + encode("flat") + "]" +
				        "[" + encode("constructor") + "]" +
				        "(" + encode("return eval") + ")()" +
				        "(" + result + ")";
			else
				result = "[][" + encode("flat") + "]" +
				        "[" + encode("constructor") + "]" +
				        "(" + result + ")()";
		}
		
		return result;
	}
	
	/* Prints using JS lexicographic order to simplify diffs */
	public static Map<String, String> print(Map<String, String> map) {
		Map<String, String> sorted = new TreeMap<String, String>(new JSComparator());
		map.forEach(sorted::put);
		for (Entry<String, String> entry : sorted.entrySet())
			System.out.printf("%s: %s%n", entry.getKey(), entry.getValue() == null ? entry.getValue() : '"' + entry.getValue() + '"');
		return sorted;
	}
	
	private static final class ReplaceMap {
		private String character, value;
		private int i;
		
		public ReplaceMap() {
			for (i = MIN; i <= MAX; i++) {
				character = Character.toString((char) i);	// equivalent of String.fromCharCode(i)
				value = MAPPING.get(character);
				if (value == null) continue;
				
				for (Entry<String, String> entry : CONSTRUCTORS.entrySet())
					value = value.replaceAll("\\b" + entry.getKey(), CONSTRUCTORS.get(entry.getKey()) + "[\"constructor\"]");
				
				for (Entry<String, String> entry : SIMPLE.entrySet())
					value = value.replaceAll(entry.getKey(), SIMPLE.get(entry.getKey()));
				
				replace("(\\d\\d+)", this::numberReplacer);
				replace("\\((\\d)\\)", str -> digitReplacer(str.substring(1, str.length()-1)));	// remove matched '(' and ')'
				replace("\\[(\\d)\\]", str -> digitReplacer(str.substring(1, str.length()-1)));
				
				value = value.replaceAll("GLOBAL", GLOBAL);
				value = value.replaceAll("\\+\"\"", "+[]"); // this doesn't work
				value = value.replaceAll("\"\"", "[]+[]");
				
				MAPPING.put(character, value);
			}
		}
		
		private void replace(String pattern, Function<String, String> replacement) {
			value = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE)	// case insensitive mimics JS /regexp/i
					.matcher(value)
					// replace all mimics JS /regexp/g
					.replaceAll(mr -> {
						String match = mr.group(), result = replacement.apply(match);
						return result == null ? match : result;
					});
		}
		
		private String digitReplacer(String x) {
			return MAPPING.get(x);
		}
		
		private String numberReplacer(String y) {
			return new NumberReplacer(y).returned;
		}
		
		private final class NumberReplacer {
			private String[] values;
			private int head;
			private String output, returned;
			
			public NumberReplacer(String y) {
				values = y.split("");
				head = Integer.parseInt(shift());
				output = "+[]";
				
				if (head > 0) output = "+!" + output;
				for (i = 1; i<head; i++) output = "+!+[]" + output;
				if (head > 1) output = output.substring(1);
				
				StringBuilder sb = new StringBuilder(output + "+");
				for (String str : values)
					sb.append(str + "+");
				sb.deleteCharAt(sb.length() - 1);
				
				returned = Pattern.compile("(\\d)")
					.matcher(sb.toString())
					.replaceAll(mr -> digitReplacer(mr.group()));
			}
			
			private String shift() {
				String[] shifted = new String[values.length-1];
				String result = values[0];
				for (int i=0; i<shifted.length; i++)
					shifted[i] = values[i+1];
				values = shifted;
				return result;
			}
		}
	}
	
	private static final class ReplaceStrings {
		private String regex, all, value;
		private Map<String, String> missing;
		private int count;
		
		public ReplaceStrings() {
			regex = "[^\\[\\]\\(\\)\\!\\+]{1}";
			count = MAX - MIN;
			
			for (Entry<String, String> entry : MAPPING.entrySet()) {
				all = entry.getKey();
				if (MAPPING.get(all) != null) {
					MAPPING.put(all, Pattern.compile("(\\\"[^\\\"]+\\\")", Pattern.CASE_INSENSITIVE)
							.matcher(MAPPING.get(all))
							.replaceAll(mr -> {
								String match = mr.group();
								return mappingReplacer(match.substring(1, match.length() - 1));
							}));
				}
			}
			
			while (findMissing()) {
				for (Entry<String, String> entry : missing.entrySet()) {
					all = entry.getKey();
					value = MAPPING.get(all);
					value = Pattern.compile(regex)
							.matcher(value)
							.replaceAll(mr -> {
								String result = valueReplacer(mr.group());
								return result == null ? "" : result;
							});
					
					MAPPING.put(all, value);
					missing.put(all, value);
				}
				
				if (count-- == 0)
					System.err.println("Could not compile the following chars: " + Arrays.deepToString(missing.entrySet().toArray()));
			}
		}
		
		private boolean findMissing() {
			String all, value;
			boolean done = false;
			
			missing = new HashMap<>();
			
			for (Entry<String, String> entry : MAPPING.entrySet()) {
				all = entry.getKey();
				
				value = MAPPING.get(all);
				
				if (value != null && Pattern.compile(regex).matcher(value).find()) {
					missing.put(all, value);
					done = true;
				}
			}
			
			return done;
		}
		
		private String mappingReplacer(String b) {
			return String.join("+", b.split(""));
		}
		
		private String valueReplacer(String c) {
			return missing.get(c) != null ? c : MAPPING.get(c);
		}
	}
	
	private static final class JSComparator implements Comparator<String> {
		private static final String ORDER = " !\"#$%&'()*+,-./0123456789:;<=>?@[\\]^_`AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz{|}~";
		
		@Override
		public int compare(String o1, String o2) {
			char c1 = o1.charAt(0), c2 = o2.charAt(0);
			return Integer.compare(ORDER.indexOf(c1), ORDER.indexOf(c2));
		}
	}
}