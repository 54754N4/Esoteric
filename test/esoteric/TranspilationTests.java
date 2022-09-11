package esoteric;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import model.CharStream;
import model.StringFormatBuilder;
import run.Esoteric;
import run.Esoteric.Input;
import run.Esoteric.Output;

class TranspilationTests {

	@Test
	void code_transpiles_correctly() {
		String[] inputCode = {
			JSFUCK_INPUT,
			BRAINFUCK_INPUT, BRAINFUCK_INPUT, BRAINFUCK_INPUT, BRAINFUCK_INPUT,
			OOK_INPUT, OOK_INPUT, OOK_INPUT, OOK_INPUT
		};
		Input[] inputLang = {
			Input.JSFuck,
			Input.Brainfuck, Input.Brainfuck, Input.Brainfuck, Input.Brainfuck,
			Input.Ook, Input.Ook, Input.Ook, Input.Ook
		};
		Output[] outputLang = {
			Output.JS,
			Output.Ook, Output.C, Output.Java, Output.Python,
			Output.Brainfuck, Output.C, Output.Java, Output.Python
		};
		String[] expected = {
			JSFUCK_TO_JS,
			BRAINFUCK_TO_OOK, BRAINFUCK_TO_C, BRAINFUCK_TO_JAVA, BRAINFUCK_TO_PYTHON,
			OOK_TO_BRAINFUCK, OOK_TO_C, OOK_TO_JAVA, OOK_TO_PYTHON
		};
		CharStream stream;
		StringFormatBuilder result;
		for (int i=0; i<inputCode.length; i++) {
			stream = CharStream.of(inputCode[i]);
			result = Esoteric.transpile(stream, inputLang[i], outputLang[i]);
			assertEquals(expected[i], result.toString());
		}
	}
	
	@Test
	void transpilation_should_throw_on_invalid_cases() {
		final String[] inputCode = {
			JSFUCK_INPUT, JSFUCK_INPUT, JSFUCK_INPUT, JSFUCK_INPUT, JSFUCK_INPUT,
			BRAINFUCK_INPUT, 
			OOK_INPUT
		};
		final Input[] inputLang = {
			Input.JSFuck, Input.JSFuck, Input.JSFuck, Input.JSFuck, Input.JSFuck,
			Input.Brainfuck,
			Input.Ook,
		};
		final Output[] outputLang = {
			Output.Brainfuck, Output.Ook, Output.C, Output.Java, Output.Python,
			Output.JS,
			Output.JS
		};
		Executable executable;
		for (int i=0; i<inputCode.length; i++) {
			final int constant = i;
			final CharStream stream = CharStream.of(inputCode[i]);
			executable = () -> Esoteric.transpile(stream, inputLang[constant], outputLang[constant]);
			assertThrows(IllegalArgumentException.class, executable);
		}
	}

	/* Inputs */
	
	private static final String
		JSFUCK_INPUT = "[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]][([][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]]+[])[!+[]+!+[]+!+[]]+(!![]+[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]])[+!+[]+[+[]]]+([][[]]+[])[+!+[]]+(![]+[])[!+[]+!+[]+!+[]]+(!![]+[])[+[]]+(!![]+[])[+!+[]]+([][[]]+[])[+[]]+([][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]]+[])[!+[]+!+[]+!+[]]+(!![]+[])[+[]]+(!![]+[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]])[+!+[]+[+[]]]+(!![]+[])[+!+[]]]((!![]+[])[+!+[]]+(!![]+[])[!+[]+!+[]+!+[]]+(!![]+[])[+[]]+([][[]]+[])[+[]]+(!![]+[])[+!+[]]+([][[]]+[])[+!+[]]+(+[![]]+[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]])[+!+[]+[+!+[]]]+(!![]+[])[!+[]+!+[]+!+[]]+(+(!+[]+!+[]+!+[]+[+!+[]]))[(!![]+[])[+[]]+(!![]+[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]])[+!+[]+[+[]]]+([]+[])[([][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]]+[])[!+[]+!+[]+!+[]]+(!![]+[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]])[+!+[]+[+[]]]+([][[]]+[])[+!+[]]+(![]+[])[!+[]+!+[]+!+[]]+(!![]+[])[+[]]+(!![]+[])[+!+[]]+([][[]]+[])[+[]]+([][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]]+[])[!+[]+!+[]+!+[]]+(!![]+[])[+[]]+(!![]+[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]])[+!+[]+[+[]]]+(!![]+[])[+!+[]]][([][[]]+[])[+!+[]]+(![]+[])[+!+[]]+((+[])[([][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]]+[])[!+[]+!+[]+!+[]]+(!![]+[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]])[+!+[]+[+[]]]+([][[]]+[])[+!+[]]+(![]+[])[!+[]+!+[]+!+[]]+(!![]+[])[+[]]+(!![]+[])[+!+[]]+([][[]]+[])[+[]]+([][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]]+[])[!+[]+!+[]+!+[]]+(!![]+[])[+[]]+(!![]+[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]])[+!+[]+[+[]]]+(!![]+[])[+!+[]]]+[])[+!+[]+[+!+[]]]+(!![]+[])[!+[]+!+[]+!+[]]]](!+[]+!+[]+!+[]+[!+[]+!+[]])+(![]+[])[+!+[]]+(![]+[])[!+[]+!+[]])()([][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]][([][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]]+[])[!+[]+!+[]+!+[]]+(!![]+[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]])[+!+[]+[+[]]]+([][[]]+[])[+!+[]]+(![]+[])[!+[]+!+[]+!+[]]+(!![]+[])[+[]]+(!![]+[])[+!+[]]+([][[]]+[])[+[]]+([][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]]+[])[!+[]+!+[]+!+[]]+(!![]+[])[+[]]+(!![]+[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]])[+!+[]+[+[]]]+(!![]+[])[+!+[]]]((!![]+[])[+!+[]]+(!![]+[])[!+[]+!+[]+!+[]]+(!![]+[])[+[]]+([][[]]+[])[+[]]+(!![]+[])[+!+[]]+([][[]]+[])[+!+[]]+([]+[])[(![]+[])[+[]]+(!![]+[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]])[+!+[]+[+[]]]+([][[]]+[])[+!+[]]+(!![]+[])[+[]]+([][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]]+[])[!+[]+!+[]+!+[]]+(!![]+[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]])[+!+[]+[+[]]]+(![]+[])[!+[]+!+[]]+(!![]+[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]])[+!+[]+[+[]]]+(!![]+[])[+!+[]]]()[+!+[]+[!+[]+!+[]]]+((![]+[])[+!+[]]+(![]+[])[!+[]+!+[]]+(!![]+[])[!+[]+!+[]+!+[]]+(!![]+[])[+!+[]]+(!![]+[])[+[]]+[+!+[]]+[!+[]+!+[]+!+[]+!+[]+!+[]+!+[]]+[!+[]+!+[]+!+[]+!+[]]+(!![]+[])[+[]]+[!+[]+!+[]+!+[]+!+[]+!+[]]+[+[]]+(!![]+[])[+[]]+[!+[]+!+[]+!+[]+!+[]]+[!+[]+!+[]]+(!![]+[])[+[]]+[+!+[]]+[+!+[]]+[+[]]+(!![]+[])[!+[]+!+[]+!+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[!+[]+!+[]]+(!![]+[])[+[]]+[+!+[]]+[!+[]+!+[]+!+[]+!+[]+!+[]]+[!+[]+!+[]+!+[]+!+[]+!+[]+!+[]+!+[]]+(!![]+[])[+[]]+[!+[]+!+[]+!+[]+!+[]+!+[]]+[!+[]+!+[]+!+[]+!+[]]+(!![]+[])[+[]]+[!+[]+!+[]+!+[]+!+[]]+[+[]]+(!![]+[])[+[]]+[+!+[]]+[!+[]+!+[]+!+[]+!+[]+!+[]+!+[]]+[!+[]+!+[]+!+[]+!+[]+!+[]+!+[]+!+[]]+(!![]+[])[+[]]+[+!+[]]+[!+[]+!+[]+!+[]+!+[]+!+[]]+[!+[]+!+[]+!+[]+!+[]+!+[]+!+[]+!+[]]+(!![]+[])[+!+[]]+(![]+[])[!+[]+!+[]]+([][[]]+[])[!+[]+!+[]]+(!![]+[])[+[]]+[!+[]+!+[]+!+[]+!+[]]+[+!+[]]+(!![]+[])[+[]]+[!+[]+!+[]+!+[]+!+[]]+[!+[]+!+[]]+(!![]+[])[+[]]+[!+[]+!+[]+!+[]+!+[]+!+[]]+[+!+[]])[(![]+[])[!+[]+!+[]+!+[]]+(+(!+[]+!+[]+[+!+[]]+[+!+[]]))[(!![]+[])[+[]]+(!![]+[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]])[+!+[]+[+[]]]+([]+[])[([][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]]+[])[!+[]+!+[]+!+[]]+(!![]+[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]])[+!+[]+[+[]]]+([][[]]+[])[+!+[]]+(![]+[])[!+[]+!+[]+!+[]]+(!![]+[])[+[]]+(!![]+[])[+!+[]]+([][[]]+[])[+[]]+([][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]]+[])[!+[]+!+[]+!+[]]+(!![]+[])[+[]]+(!![]+[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]])[+!+[]+[+[]]]+(!![]+[])[+!+[]]][([][[]]+[])[+!+[]]+(![]+[])[+!+[]]+((+[])[([][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]]+[])[!+[]+!+[]+!+[]]+(!![]+[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]])[+!+[]+[+[]]]+([][[]]+[])[+!+[]]+(![]+[])[!+[]+!+[]+!+[]]+(!![]+[])[+[]]+(!![]+[])[+!+[]]+([][[]]+[])[+[]]+([][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]]+[])[!+[]+!+[]+!+[]]+(!![]+[])[+[]]+(!![]+[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]])[+!+[]+[+[]]]+(!![]+[])[+!+[]]]+[])[+!+[]+[+!+[]]]+(!![]+[])[!+[]+!+[]+!+[]]]](!+[]+!+[]+!+[]+[+!+[]])[+!+[]]+(![]+[])[!+[]+!+[]]+([![]]+[][[]])[+!+[]+[+[]]]+(!![]+[])[+[]]]((!![]+[])[+[]])[([][(!![]+[])[!+[]+!+[]+!+[]]+([][[]]+[])[+!+[]]+(!![]+[])[+[]]+(!![]+[])[+!+[]]+([![]]+[][[]])[+!+[]+[+[]]]+(!![]+[])[!+[]+!+[]+!+[]]+(![]+[])[!+[]+!+[]+!+[]]]()+[])[!+[]+!+[]+!+[]]+(!![]+[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]])[+!+[]+[+[]]]+([![]]+[][[]])[+!+[]+[+[]]]+([][[]]+[])[+!+[]]](([][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]][([][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]]+[])[!+[]+!+[]+!+[]]+(!![]+[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]])[+!+[]+[+[]]]+([][[]]+[])[+!+[]]+(![]+[])[!+[]+!+[]+!+[]]+(!![]+[])[+[]]+(!![]+[])[+!+[]]+([][[]]+[])[+[]]+([][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]]+[])[!+[]+!+[]+!+[]]+(!![]+[])[+[]]+(!![]+[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]])[+!+[]+[+[]]]+(!![]+[])[+!+[]]]((!![]+[])[+!+[]]+(!![]+[])[!+[]+!+[]+!+[]]+(!![]+[])[+[]]+([][[]]+[])[+[]]+(!![]+[])[+!+[]]+([][[]]+[])[+!+[]]+(![]+[+[]])[([![]]+[][[]])[+!+[]+[+[]]]+(!![]+[])[+[]]+(![]+[])[+!+[]]+(![]+[])[!+[]+!+[]]+([![]]+[][[]])[+!+[]+[+[]]]+([][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]]+[])[!+[]+!+[]+!+[]]+(![]+[])[!+[]+!+[]+!+[]]]()[+!+[]+[+[]]]+![]+(![]+[+[]])[([![]]+[][[]])[+!+[]+[+[]]]+(!![]+[])[+[]]+(![]+[])[+!+[]]+(![]+[])[!+[]+!+[]]+([![]]+[][[]])[+!+[]+[+[]]]+([][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]]+[])[!+[]+!+[]+!+[]]+(![]+[])[!+[]+!+[]+!+[]]]()[+!+[]+[+[]]])()[([][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]]+[])[!+[]+!+[]+!+[]]+(!![]+[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]])[+!+[]+[+[]]]+([][[]]+[])[+!+[]]+(![]+[])[!+[]+!+[]+!+[]]+(!![]+[])[+[]]+(!![]+[])[+!+[]]+([][[]]+[])[+[]]+([][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]]+[])[!+[]+!+[]+!+[]]+(!![]+[])[+[]]+(!![]+[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]])[+!+[]+[+[]]]+(!![]+[])[+!+[]]]((![]+[+[]])[([![]]+[][[]])[+!+[]+[+[]]]+(!![]+[])[+[]]+(![]+[])[+!+[]]+(![]+[])[!+[]+!+[]]+([![]]+[][[]])[+!+[]+[+[]]]+([][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]]+[])[!+[]+!+[]+!+[]]+(![]+[])[!+[]+!+[]+!+[]]]()[+!+[]+[+[]]])+[])[+!+[]])+([]+[])[(![]+[])[+[]]+(!![]+[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]])[+!+[]+[+[]]]+([][[]]+[])[+!+[]]+(!![]+[])[+[]]+([][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]]+[])[!+[]+!+[]+!+[]]+(!![]+[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]])[+!+[]+[+[]]]+(![]+[])[!+[]+!+[]]+(!![]+[][(![]+[])[+[]]+(![]+[])[!+[]+!+[]]+(![]+[])[+!+[]]+(!![]+[])[+[]]])[+!+[]+[+[]]]+(!![]+[])[+!+[]]]()[+!+[]+[!+[]+!+[]]])())",
		BRAINFUCK_INPUT = ">++++++++[<+++++++++>-]<.>++++[<+++++++>-]<+.+++++++..+++.>>++++++[<+++++++>-]<++.------------.>++++++[<+++++++++>-]<+.<.+++.------.--------.>>>++++[<++++++++>-]<+.",
		OOK_INPUT = "Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook? Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook! Ook! Ook? Ook! Ook? Ook. Ook! Ook. Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook? Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook! Ook! Ook? Ook! Ook? Ook. Ook. Ook. Ook! Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook. Ook! Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook. Ook. Ook? Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook? Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook! Ook! Ook? Ook! Ook? Ook. Ook. Ook. Ook. Ook. Ook! Ook. Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook. Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook? Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook! Ook! Ook? Ook! Ook? Ook. Ook. Ook. Ook! Ook. Ook? Ook. Ook! Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook. Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook. Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook. Ook. Ook? Ook. Ook? Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook? Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook! Ook! Ook? Ook! Ook? Ook. Ook. Ook. Ook! Ook. ";
	
	/* Expected Outputs */
	private static final String
		JSFUCK_TO_JS = "[].flat.constructor(\"return eval\")()(\"alert(\\\"Hello, world!\\\")\")",
		BRAINFUCK_TO_JAVA = "public static int[] mem = new int[2048];\r\n"
				+ "public static int ptr = 0;\r\n"
				+ "\r\n"
				+ "public static void func0() {\r\n"
				+ "	while (mem[ptr] != 0) {\r\n"
				+ "		ptr--;\r\n"
				+ "		mem[ptr] += 9;\r\n"
				+ "		ptr++;\r\n"
				+ "		mem[ptr]--;\r\n"
				+ "	}\r\n"
				+ "}\r\n"
				+ "public static void func1() {\r\n"
				+ "	while (mem[ptr] != 0) {\r\n"
				+ "		ptr--;\r\n"
				+ "		mem[ptr] += 7;\r\n"
				+ "		ptr++;\r\n"
				+ "		mem[ptr]--;\r\n"
				+ "	}\r\n"
				+ "}\r\n"
				+ "public static void func2() {\r\n"
				+ "	while (mem[ptr] != 0) {\r\n"
				+ "		ptr--;\r\n"
				+ "		mem[ptr] += 8;\r\n"
				+ "		ptr++;\r\n"
				+ "		mem[ptr]--;\r\n"
				+ "	}\r\n"
				+ "}\r\n"
				+ "public static void main(String[] args) {\r\n"
				+ "	ptr++;\r\n"
				+ "	mem[ptr] += 8;\r\n"
				+ "	func0();\r\n"
				+ "	ptr--;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "	ptr++;\r\n"
				+ "	mem[ptr] += 4;\r\n"
				+ "	func1();\r\n"
				+ "	ptr--;\r\n"
				+ "	mem[ptr]++;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "	mem[ptr] += 7;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "	mem[ptr] += 3;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "	ptr += 2;\r\n"
				+ "	mem[ptr] += 6;\r\n"
				+ "	func1();\r\n"
				+ "	ptr--;\r\n"
				+ "	mem[ptr] += 2;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "	mem[ptr] += -12;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "	ptr++;\r\n"
				+ "	mem[ptr] += 6;\r\n"
				+ "	func0();\r\n"
				+ "	ptr--;\r\n"
				+ "	mem[ptr]++;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "	ptr--;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "	mem[ptr] += 3;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "	mem[ptr] += -6;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "	mem[ptr] += -8;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "	ptr += 3;\r\n"
				+ "	mem[ptr] += 4;\r\n"
				+ "	func2();\r\n"
				+ "	ptr--;\r\n"
				+ "	mem[ptr]++;\r\n"
				+ "	System.out.print((char) mem[ptr]);\r\n"
				+ "}\r\n",
		BRAINFUCK_TO_OOK = "Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook? Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook! Ook! Ook? Ook! Ook? Ook. Ook! Ook. Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook? Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook! Ook! Ook? Ook! Ook? Ook. Ook. Ook. Ook! Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook. Ook! Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook. Ook. Ook? Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook? Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook! Ook! Ook? Ook! Ook? Ook. Ook. Ook. Ook. Ook. Ook! Ook. Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook. Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook? Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook! Ook! Ook? Ook! Ook? Ook. Ook. Ook. Ook! Ook. Ook? Ook. Ook! Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook. Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook. Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook! Ook. Ook. Ook? Ook. Ook? Ook. Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook! Ook? Ook? Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook. Ook? Ook! Ook! Ook? Ook! Ook? Ook. Ook. Ook. Ook! Ook. ",
		BRAINFUCK_TO_C = "int mem[2048];\r\n"
				+ "int ptr = 0;\r\n"
				+ "\r\n"
				+ "void func0() {\r\n"
				+ "	while (mem[ptr]) {\r\n"
				+ "		ptr--;\r\n"
				+ "		mem[ptr] += 9;\r\n"
				+ "		ptr++;\r\n"
				+ "		mem[ptr]--;\r\n"
				+ "	}\r\n"
				+ "}\r\n"
				+ "void func1() {\r\n"
				+ "	while (mem[ptr]) {\r\n"
				+ "		ptr--;\r\n"
				+ "		mem[ptr] += 7;\r\n"
				+ "		ptr++;\r\n"
				+ "		mem[ptr]--;\r\n"
				+ "	}\r\n"
				+ "}\r\n"
				+ "void func2() {\r\n"
				+ "	while (mem[ptr]) {\r\n"
				+ "		ptr--;\r\n"
				+ "		mem[ptr] += 8;\r\n"
				+ "		ptr++;\r\n"
				+ "		mem[ptr]--;\r\n"
				+ "	}\r\n"
				+ "}\r\n"
				+ "int main(int argc, char** argv) {\r\n"
				+ "	// Initialise memory cells\r\n"
				+ "	for (int i=0; i<2048; i++)\r\n"
				+ "		mem[i] = 0;\r\n"
				+ "\r\n"
				+ "	// Driver code\r\n"
				+ "	ptr++;\r\n"
				+ "	mem[ptr] += 8;\r\n"
				+ "	func0();\r\n"
				+ "	ptr--;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	ptr++;\r\n"
				+ "	mem[ptr] += 4;\r\n"
				+ "	func1();\r\n"
				+ "	ptr--;\r\n"
				+ "	mem[ptr]++;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	mem[ptr] += 7;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	mem[ptr] += 3;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	ptr += 2;\r\n"
				+ "	mem[ptr] += 6;\r\n"
				+ "	func1();\r\n"
				+ "	ptr--;\r\n"
				+ "	mem[ptr] += 2;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	mem[ptr] += -12;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	ptr++;\r\n"
				+ "	mem[ptr] += 6;\r\n"
				+ "	func0();\r\n"
				+ "	ptr--;\r\n"
				+ "	mem[ptr]++;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	ptr--;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	mem[ptr] += 3;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	mem[ptr] += -6;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	mem[ptr] += -8;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	ptr += 3;\r\n"
				+ "	mem[ptr] += 4;\r\n"
				+ "	func2();\r\n"
				+ "	ptr--;\r\n"
				+ "	mem[ptr]++;\r\n"
				+ "	putchar(mem[ptr]);\r\n"
				+ "	return 0;\r\n"
				+ "}\r\n",
		BRAINFUCK_TO_PYTHON = "mem = [0] * 2048\r\n"
				+ "ptr = 0\r\n"
				+ "\r\n"
				+ "def func0():\r\n"
				+ "	global ptr, mem\r\n"
				+ "	while mem[ptr] != 0:\r\n"
				+ "		ptr += -1\r\n"
				+ "		mem[ptr] += 9\r\n"
				+ "		ptr += 1\r\n"
				+ "		mem[ptr] += -1\r\n"
				+ "def func1():\r\n"
				+ "	global ptr, mem\r\n"
				+ "	while mem[ptr] != 0:\r\n"
				+ "		ptr += -1\r\n"
				+ "		mem[ptr] += 7\r\n"
				+ "		ptr += 1\r\n"
				+ "		mem[ptr] += -1\r\n"
				+ "def func2():\r\n"
				+ "	global ptr, mem\r\n"
				+ "	while mem[ptr] != 0:\r\n"
				+ "		ptr += -1\r\n"
				+ "		mem[ptr] += 8\r\n"
				+ "		ptr += 1\r\n"
				+ "		mem[ptr] += -1\r\n"
				+ "\r\n"
				+ "ptr += 1\r\n"
				+ "mem[ptr] += 8\r\n"
				+ "func0()\r\n"
				+ "ptr += -1\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n"
				+ "ptr += 1\r\n"
				+ "mem[ptr] += 4\r\n"
				+ "func1()\r\n"
				+ "ptr += -1\r\n"
				+ "mem[ptr] += 1\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n"
				+ "mem[ptr] += 7\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n"
				+ "mem[ptr] += 3\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n"
				+ "ptr += 2\r\n"
				+ "mem[ptr] += 6\r\n"
				+ "func1()\r\n"
				+ "ptr += -1\r\n"
				+ "mem[ptr] += 2\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n"
				+ "mem[ptr] += -12\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n"
				+ "ptr += 1\r\n"
				+ "mem[ptr] += 6\r\n"
				+ "func0()\r\n"
				+ "ptr += -1\r\n"
				+ "mem[ptr] += 1\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n"
				+ "ptr += -1\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n"
				+ "mem[ptr] += 3\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n"
				+ "mem[ptr] += -6\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n"
				+ "mem[ptr] += -8\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n"
				+ "ptr += 3\r\n"
				+ "mem[ptr] += 4\r\n"
				+ "func2()\r\n"
				+ "ptr += -1\r\n"
				+ "mem[ptr] += 1\r\n"
				+ "print(chr(mem[ptr]), end='')\r\n",
		OOK_TO_BRAINFUCK = ">++++++++[<+++++++++>-]<.>++++[<+++++++>-]<+.+++++++..+++.>>++++++[<+++++++>-]<++.------------.>++++++[<+++++++++>-]<+.<.+++.------.--------.>>>++++[<++++++++>-]<+.",
		OOK_TO_C = BRAINFUCK_TO_C,
		OOK_TO_JAVA = BRAINFUCK_TO_JAVA,
		OOK_TO_PYTHON = BRAINFUCK_TO_PYTHON;
}
