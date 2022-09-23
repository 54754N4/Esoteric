package esoteric.brainfuck.substition;

import model.StringFormatBuilder;

/**
 * Declares usual family of trivial brainfuck substitutions.
 * Refs: https://esolangs.org/wiki/Trivial_brainfuck_substitution
 */
public interface BFSubstitutions {
	
	/**
	 * Encodes BF code into a trivial substitution
	 * @param bf - code
	 * @param substitution - trivial substitution mapping
	 * @return encoded bf code
	 */
	static String encode(String bf, BFSubstitution substitution) {
		StringBuilder builder = new StringBuilder();
		boolean spaced = substitution.isSpaceSeparated();
		for (int i=0; i<bf.length(); i++) {
			builder.append(substitution.get(bf.charAt(i)));
			if (spaced)
				builder.append(" ");
		}
		return builder.toString().trim();
	}
	
	/**
	 * Decodes a trivial substitution back to BF code (uses a 
	 * StringFormatBuilder to simulate a mini lookahead lexer).
	 * @param encoded - substituted code
	 * @param substitution - trivial substitution mapping
	 * @return bf code
	 */
	static String decode(String encoded, BFSubstitution substitution) {
		StringFormatBuilder builder = new StringFormatBuilder(encoded);
		StringBuilder result = new StringBuilder();
		String[] mappings = substitution.getOrderedMappings();
		String opcodes = substitution.getOrderedOpcodes();
		String token;
		boolean spaced = substitution.isSpaceSeparated(), match;
		while (builder.length() != 0) {
			match = false;
			while (spaced && builder.peek(" "))
				builder.deleteFirstChar();
			for (int i=0; i<mappings.length; i++) {
				token = mappings[i];
				if (builder.peek(token)) {
					match = true;
					builder.deleteFirstChars(token.length());
					result.append(opcodes.charAt(i));
					break;
				}
			}
			if (!match)
				throw new IllegalArgumentException("No match found: "+builder);
		}
		return result.toString();
	}
	
	/* Family of trivial substitutions */
	
	// !!Fuck 
	public static final BFSubstitution EXCL_EXCL_FUCK = new BFSubstitution.Builder()
		.set("!!!!!#", "!!!!!!#", "!!!!!!!#", "!!!!!!!!#", "!!!!!!!!!!#", "!!!!!!!!!#", "!!!!!!!!!!!#", "!!!!!!!!!!!!#")
		.build();
	// AAA 
	public static final BFSubstitution AAA = new BFSubstitution.Builder()
		.set("aAaA", "AAaa", "AAAA", "AaAa", "aaaa", "aAaa", "aaAA", "aaaA")
		.build();
	// Alphuck 
	public static final BFSubstitution ALPHUCK = new BFSubstitution.Builder()
		.set("a", "c", "e", "i", "j", "o", "p", "s")
		.build();
	// BrainFNORD 
	public static final BFSubstitution BRAINFNORD = new BFSubstitution.Builder()
		.set("pineal", "fnord", "hail", "eris", "kallisti", "chaos", "23", "5")
		.build();
	// brainfuck 
	public static final BFSubstitution BRAINFUCK = new BFSubstitution.Builder()
		.set(">", "<", "+", "-", ".", ",", "[", "]")
		.build();
	// brainfuck 4 humans 
	public static final BFSubstitution BRAINFUCK_4_HUMANS = new BFSubstitution.Builder()
		.set("right", "left", "incr", "decr", "out", "inp", "loop(", ")")
		.spaced()
		.build();
	// brainsymbol 
	public static final BFSubstitution BRAINSYMBOL = new BFSubstitution.Builder()
		.set("!", "@", "#", "$", "%", "^", "&", "*")
		.build();
	// Btjzxgquartfrqifjlv 
	public static final BFSubstitution BTJZXGQUARTFRQiFJLV = new BFSubstitution.Builder()
		.set("f", "rqi", "qua", "rtf", "lv", "j", "btj", "zxg")
		.build();
	// Binaryfuck 
	public static final BFSubstitution BINARYFUCK = new BFSubstitution.Builder()
		.set("010", "011", "000", "001", "100", "101", "110", "111")
		.build();
	// Blub 
	public static final BFSubstitution BLUB = new BFSubstitution.Builder()
		.set("Blub. Blub?", "Blub? Blub.", "Blub. Blub.", "Blub! Blub!", "Blub! Blub.", "Blub. Blub!", "Blub! Blub?", "Blub? Blub!")
		.spaced()
		.build();
	// Brainfuck² 
	public static final BFSubstitution BRAINFUCK_SQUARED = new BFSubstitution.Builder()
		.set("Ook!", "Alphuck", "Fuckfuck", "POGAACK", "Unibrain", "Wordfuck", "Brainfuck²", "ZZZ")
		.build();
	// Colonoscopy 
	public static final BFSubstitution COLONOSCOPY = new BFSubstitution.Builder()
		.set(";};", ";{;", ";;};", ";;{;", ";;;};", ";;;{;", "{{;", "}};")
		.build();
	// DetailedFuck 
	public static final BFSubstitution DETAILEDFUCK = new BFSubstitution.Builder()
		.set("MOVE THE MEMORY POINTER ONE CELL TO THE RIGHT", "MOVE THE MEMORY POINTER ONE CELL TO THE LEFT", "INCREMENT THE CELL UNDER THE MEMORY POINTER BY ONE", "DECREMENT THE CELL UNDER THE MEMORY POINTER BY ONE", "REPLACE THE CELL UNDER THE MEMORY POINTER'S VALUE WITH THE ASCII CHARACTER CODE OF USER INPUT", "PRINT THE CELL UNDER THE MEMORY POINTER'S VALUE AS AN ASCII CHARACTER", "IF THE CELL UNDER THE MEMORY POINTER'S VALUE IS ZERO INSTEAD OF READING THE NEXT COMMAND IN THE PROGRAM JUMP TO THE CORRESPONDING COMMAND EQUIVALENT TO THE ] COMMAND IN BRAINFUCK", "IF THE CELL UNDER THE MEMORY POINTER'S VALUE IS NOT ZERO INSTEAD OF READING THE NEXT COMMAND IN THE PROGRAM JUMP TO THE CORRESPONDING COMMAND EQUIVALENT TO THE [ COMMAND IN BRAINFUCK")
		.spaced()
		.build();
	// Fluffle Puff 
	public static final BFSubstitution FLUFFLE_PUFF = new BFSubstitution.Builder()
		.set("b", "t", "bf", "pl", "!", "?", "*gasp*", "*pomf*")
		.build();
	// fuckbeEs 
	public static final BFSubstitution FUCKBEES = new BFSubstitution.Builder()
		.set("f", "u", "c", "k", "b", "e", "E", "s")
		.build();
	// GERMAN 
	public static final BFSubstitution GERMAN = new BFSubstitution.Builder()
		.set("LINKS", "RECHTS", "ADDITION", "SUBTRAKTION", "EINGABE", "AUSGABE", "SCHLEIFENANFANG", "SCHLEIFENENDE")
		.build();
	// GibMeRol 
	public static final BFSubstitution GIBMEROL = new BFSubstitution.Builder()
		.set("G", "i", "b", "M", "e", "R", "o", "l")
		.build();
	// HTPF 
	public static final BFSubstitution HTPF = new BFSubstitution.Builder()
		.set(">", "<", "=", "/", "\"", "#", "&", ";")
		.build();
	// Human's mind have sex with someone 
	public static final BFSubstitution HUMANS_MIND_HAVE_SEX_WITH_SOMEONE = new BFSubstitution.Builder()
		.set("Move the pointer to the right", "Move the pointer to the left", "Increment the memory cell under the pointer", "Decrement the memory cell under the pointer", "Output the character signified by the cell at the pointer", "Input the character signified by the cell at the pointer", "Jump past the matching right bracket if the cell under the pointer is 0", "Jump back to the matching left bracket if the cell under the pointer is 0")
		.spaced()
		.build();
	// K-on Fuck 
	public static final BFSubstitution K_ON_FUCK = new BFSubstitution.Builder()
		.set("うんうんうん", "うんうんたん", "うんたんうん", "うんたんたん", "たんうんうん", "たんうんたん", "たんたんうん", "たんたんたん")
		.build();
	// Mierda 
	public static final BFSubstitution MIERDA = new BFSubstitution.Builder()
		.set("Derecha", "Izquierda", "Mas", "Menos", "Decir", "Leer", "Iniciar Bucle", "Terminar Bucle")
		.spaced()
		.build();
	// Morsefuck 
	public static final BFSubstitution MORSEFUCK = new BFSubstitution.Builder()
		.set(".--", "--.", "..-", "-..", "-.-", ".-.", "---", "...")
		.build();
	// Nagawooski 
	public static final BFSubstitution NAGAWOOSKI = new BFSubstitution.Builder()
		.set("na", "ga", "woo", "ski", "an", "ag", "oow", "iks")
		.build();
	// Omam 
	public static final BFSubstitution OMAM = new BFSubstitution.Builder()
		.set("hold your horses now", "sleep until the sun goes down", "through the woods we ran", "deep into the mountain sound", "don't listen to a word i say", "the screams all sound the same", "though the truth may vary", "this ship will carry")
		.spaced()
		.build();
	// Ook! 
	public static final BFSubstitution OOK = new BFSubstitution.Builder()
		.set("Ook. Ook?", "Ook? Ook.", "Ook. Ook.", "Ook! Ook!", "Ook! Ook.", "Ook. Ook!", "Ook! Ook?", "Ook? Ook!")
		.spaced()
		.build();
	// PenisScript 
	public static final BFSubstitution PENISSCRIPT = new BFSubstitution.Builder()
		.set("8=D", "8==D", "8===D", "8====D", "8=====D", "8======D", "8=======D", "8========D")
		.build();
	// Pewlang 
	public static final BFSubstitution PEWLANG = new BFSubstitution.Builder()
		.set("pew", "Pew", "pEw", "peW", "PEw", "pEW", "PeW", "PEW")
		.spaced()
		.build();
	// Pikalang 
	public static final BFSubstitution PIKALANG = new BFSubstitution.Builder()
		.set("pipi", "pichu", "pi", "ka", "pikachu", "pikapi", "pika", "chu")
		.spaced()
		.build();
	// ReverseFuck 
	public static final BFSubstitution REVERSEFUCK = new BFSubstitution.Builder()
		.set("<", ">", "-", "+", ",", ".", "]", "[")
		.build();
	// Revolution 9 
	public static final BFSubstitution REVOLUTION_9 = new BFSubstitution.Builder()
		.set("It's alright", "turn me on, dead man", "Number 9", "if you become naked", "The Beatles", "Paul is dead", "Revolution 1", "Revolution 9")
		.spaced()
		.build();
	// Roadrunner 
	public static final BFSubstitution ROADRUNNER = new BFSubstitution.Builder()
		.set("meeP", "Meep", "mEEp", "MeeP", "MEEP", "meep", "mEEP", "MEEp")
		.spaced()
		.build();
	// SCREAMCODE 
	public static final BFSubstitution SCREAMCODE = new BFSubstitution.Builder()
		.set("AAAH", "AAAAGH", "FUCK", "SHIT", "!!!!!!", "WHAT?!", "OW", "OWIE")
		.spaced()
		.build();
	// Spider Giant 
	public static final BFSubstitution SPIDER_GIANT = new BFSubstitution.Builder()
		.set("Spider", "He is our hero!", "We love you spider!", "Get rid of", "Must stop!", "Step on Spider!", "I promise not to kill you.", "Oh!")
		.spaced()
		.build();
	// Ternary 
	public static final BFSubstitution TERNARY = new BFSubstitution.Builder()
		.set("01", "00", "11", "10", "21", "20", "02", "12")
		.build();
	// THIS IS NOT A BRAINFUCK DERIVATIVE 
	public static final BFSubstitution THIS_IS_NOT_A_BRAINFUCK_DERIVATIVE  = new BFSubstitution.Builder()
		.set("IT NEVER WAS", "IT NEVER WILL BE", "THIS IS NOT A BRAINFUCK DERIVATIVE", "IT HAS NOTHING TO DO WITH BRAINFUCK", "TO ANYBODY WHO SAYS THIS IS A BRAINFUCK DERIVATIVE:", "FUCK YOU", "SHUT UP YOU LITTLE BITCH", "SHUT THE FUCK UP")
		.spaced()
		.build();
	// Triplet 
	public static final BFSubstitution TRIPLET = new BFSubstitution.Builder()
		.set("001", "100", "111", "000", "010", "101", "110", "011")
		.build();
	// UwU 
	public static final BFSubstitution UWU = new BFSubstitution.Builder()
		.set("OwO", "°w°", "UwU", "QwQ", "@w@", ">w<", "~w~", "¯w¯")
		.spaced()
		.build();
	// wepmlrio 
	public static final BFSubstitution WPMLRIO = new BFSubstitution.Builder()
		.set("r", "l", "p", "m", "o", "I", "w", "e")
		.build();
	// ZZZ 
	public static final BFSubstitution ZZZ = new BFSubstitution.Builder()
		.set("zz", "-zz", "z", "-z", "zzz", "-zzz", "z+z", "z-z")
		.spaced()
		.build();
}
