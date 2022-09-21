package esoteric.brainfuck;

import java.nio.charset.StandardCharsets;

import model.StringFormatBuilder;

/* Very simple BF encoder based on: https://github.com/Kleshni/Brainfuck-converter/blob/master/index.htm */
public class BFCompiler {
	public static final String[] OPCODES = {"---", "--", "-", "", "+", "++", "+++"};
	public static final String INIT = ">--->--->--->",
			FINAL = "+[<+++[-<+++++++>]<+++[-<+++++++>]<+++[.>]<]";
	
	public static String encode(String string) {
		byte[] data = string.getBytes(StandardCharsets.UTF_8);
		StringFormatBuilder sb = new StringFormatBuilder(INIT);
		byte b;
		for (int i=string.length()-1; i>=0; i--) {
			b = data[i];
			for (int j=0; j<3; j++) {
				sb.append(OPCODES[b % OPCODES.length]).append(">");
				b = (byte) Math.floor(b / OPCODES.length);
			}
		}
		sb.append(FINAL);
		return sb.toString();
	}
}
