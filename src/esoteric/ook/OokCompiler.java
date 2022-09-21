package esoteric.ook;

import java.nio.charset.StandardCharsets;

import model.StringFormatBuilder;

/* Based on the BFCompiler class (same code) */
public class OokCompiler {
	public static final String[] OPCODES = {
			"Ook! Ook! Ook! Ook! Ook! Ook! ",
			"Ook! Ook! Ook! Ook! ", 
			"Ook! Ook! ", 
			"", 
			"Ook. Ook. ", 
			"Ook. Ook. Ook. Ook. ", 
			"Ook. Ook. Ook. Ook. Ook. Ook. "
	};
	
	public static final String INIT = "Ook. Ook? Ook! Ook! Ook! Ook! Ook! Ook! Ook. Ook? Ook! Ook! "
			+ "Ook! Ook! Ook! Ook! Ook. Ook? Ook! Ook! Ook! Ook! Ook! Ook! Ook. Ook? ",
			FINAL = "Ook. Ook. Ook! Ook? Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook? "
					+ "Ook! Ook! Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. "
					+ "Ook. Ook. Ook. Ook. Ook. Ook? Ook? Ook! Ook? Ook. Ook. Ook. Ook. Ook. "
					+ "Ook. Ook. Ook! Ook? Ook! Ook! Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. "
					+ "Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook? Ook! Ook? Ook. "
					+ "Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook? Ook! Ook. Ook. Ook? Ook? Ook! "
					+ "Ook? Ook. Ook? Ook! ";
	
	public static String encode(String string) {
		byte[] data = string.getBytes(StandardCharsets.UTF_8);
		StringFormatBuilder sb = new StringFormatBuilder(INIT);
		byte b;
		for (int i=string.length()-1; i>=0; i--) {
			b = data[i];
			for (int j=0; j<3; j++) {
				sb.append(OPCODES[b % OPCODES.length]).append("Ook. Ook? ");
				b = (byte) Math.floor(b / OPCODES.length);
			}
		}
		sb.append(FINAL);
		return sb.toString();
	}
}
