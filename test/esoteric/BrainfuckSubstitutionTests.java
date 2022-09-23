package esoteric;

import static esoteric.brainfuck.substition.BFSubstitutions.AAA;
import static esoteric.brainfuck.substition.BFSubstitutions.ALPHUCK;
import static esoteric.brainfuck.substition.BFSubstitutions.BINARYFUCK;
import static esoteric.brainfuck.substition.BFSubstitutions.BLUB;
import static esoteric.brainfuck.substition.BFSubstitutions.BRAINFNORD;
import static esoteric.brainfuck.substition.BFSubstitutions.BRAINFUCK;
import static esoteric.brainfuck.substition.BFSubstitutions.BRAINFUCK_4_HUMANS;
import static esoteric.brainfuck.substition.BFSubstitutions.BRAINFUCK_SQUARED;
import static esoteric.brainfuck.substition.BFSubstitutions.BRAINSYMBOL;
import static esoteric.brainfuck.substition.BFSubstitutions.BTJZXGQUARTFRQiFJLV;
import static esoteric.brainfuck.substition.BFSubstitutions.COLONOSCOPY;
import static esoteric.brainfuck.substition.BFSubstitutions.DETAILEDFUCK;
import static esoteric.brainfuck.substition.BFSubstitutions.EXCL_EXCL_FUCK;
import static esoteric.brainfuck.substition.BFSubstitutions.FLUFFLE_PUFF;
import static esoteric.brainfuck.substition.BFSubstitutions.FUCKBEES;
import static esoteric.brainfuck.substition.BFSubstitutions.GERMAN;
import static esoteric.brainfuck.substition.BFSubstitutions.GIBMEROL;
import static esoteric.brainfuck.substition.BFSubstitutions.HTPF;
import static esoteric.brainfuck.substition.BFSubstitutions.HUMANS_MIND_HAVE_SEX_WITH_SOMEONE;
import static esoteric.brainfuck.substition.BFSubstitutions.K_ON_FUCK;
import static esoteric.brainfuck.substition.BFSubstitutions.MIERDA;
import static esoteric.brainfuck.substition.BFSubstitutions.MORSEFUCK;
import static esoteric.brainfuck.substition.BFSubstitutions.NAGAWOOSKI;
import static esoteric.brainfuck.substition.BFSubstitutions.OMAM;
import static esoteric.brainfuck.substition.BFSubstitutions.OOK;
import static esoteric.brainfuck.substition.BFSubstitutions.PENISSCRIPT;
import static esoteric.brainfuck.substition.BFSubstitutions.PEWLANG;
import static esoteric.brainfuck.substition.BFSubstitutions.PIKALANG;
import static esoteric.brainfuck.substition.BFSubstitutions.REVERSEFUCK;
import static esoteric.brainfuck.substition.BFSubstitutions.REVOLUTION_9;
import static esoteric.brainfuck.substition.BFSubstitutions.ROADRUNNER;
import static esoteric.brainfuck.substition.BFSubstitutions.SCREAMCODE;
import static esoteric.brainfuck.substition.BFSubstitutions.SPIDER_GIANT;
import static esoteric.brainfuck.substition.BFSubstitutions.TERNARY;
import static esoteric.brainfuck.substition.BFSubstitutions.THIS_IS_NOT_A_BRAINFUCK_DERIVATIVE;
import static esoteric.brainfuck.substition.BFSubstitutions.TRIPLET;
import static esoteric.brainfuck.substition.BFSubstitutions.UWU;
import static esoteric.brainfuck.substition.BFSubstitutions.WPMLRIO;
import static esoteric.brainfuck.substition.BFSubstitutions.ZZZ;
import static esoteric.brainfuck.substition.BFSubstitutions.decode;
import static esoteric.brainfuck.substition.BFSubstitutions.encode;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import esoteric.brainfuck.BFCompiler;
import esoteric.brainfuck.substition.BFSubstitution;

class BrainfuckSubstitutionTests {

	@Test
	void trivial_substitutions_encode_and_decode_correctly() {
		String code = BFCompiler.encode("Hello, World!");
		BFSubstitution[] substitutions = {
				AAA, ALPHUCK, BINARYFUCK, BLUB, BRAINFNORD, BRAINFUCK, BRAINFUCK_4_HUMANS, 
				BRAINFUCK_SQUARED, BRAINSYMBOL, BTJZXGQUARTFRQiFJLV, COLONOSCOPY, DETAILEDFUCK,
				EXCL_EXCL_FUCK, FLUFFLE_PUFF, FUCKBEES, GERMAN, GIBMEROL, HTPF, HUMANS_MIND_HAVE_SEX_WITH_SOMEONE,
				K_ON_FUCK, MIERDA, MORSEFUCK, NAGAWOOSKI, OMAM, OOK, PENISSCRIPT, PEWLANG, PIKALANG, 
				REVERSEFUCK, REVOLUTION_9, ROADRUNNER, SCREAMCODE, SPIDER_GIANT, TERNARY, 
				THIS_IS_NOT_A_BRAINFUCK_DERIVATIVE, TRIPLET, UWU, WPMLRIO, ZZZ
		};
		for (int i=0; i<substitutions.length; i++) {
			final BFSubstitution substitution = substitutions[i];
			String encoded = encode(code, substitution);
			String decoded = assertDoesNotThrow(() -> decode(encoded, substitution), "Throws error at substitution #"+i);
			assertEquals(code, decoded, "Error for substitution #"+i);
		}
	}

}
