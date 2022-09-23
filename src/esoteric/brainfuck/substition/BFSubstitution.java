package esoteric.brainfuck.substition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/* Represents any trivial BF substitutions */
public class BFSubstitution {
	public static final String OPCODES = "><+-.,[]",
			DEFAULT_PTR_RIGHT = ">",
			DEFAULT_PTR_LEFT = "<",
			DEFAULT_INC = "+",
			DEFAULT_DEC = "-",
			DEFAULT_OUTPUT = ".",
			DEFAULT_INPUT = ",",
			DEFAULT_LOOP_START = "[",
			DEFAULT_LOOP_END = "]";
	
	private final Map<String, String> substitution;
	private final boolean spaceSeparated;
	
	public BFSubstitution(Map<String, String> substitution, boolean spaceSeparated) {
		this.spaceSeparated = spaceSeparated;
		boolean[] contained = new boolean[OPCODES.length()];
		String[] keys = substitution.keySet().toArray(String[]::new);
		for (int i=0; i<keys.length; i++)
			if (OPCODES.contains(keys[i]))
				contained[i] = true;
		for (int i=0; i<contained.length; i++)
			if (!contained[i])
				throw new IllegalArgumentException(String.format("Opcode %s has not been mapped", keys[i]));
		this.substitution = new HashMap<>(substitution);
	}
	
	public boolean isSpaceSeparated() {
		return spaceSeparated;
	}
	
	public String[] getOrderedMappings() {
		List<String> values = new ArrayList<>(substitution.values());
		// Sort mappings from biggest to smallest
		Collections.sort(values, (v1, v2) -> {
			if (v1.length() == v2.length())
				return v1.compareTo(v2);
			return v1.length() < v2.length() ? 1 : -1;
		});
		return values.toArray(String[]::new);
	}
	
	public String getOrderedOpcodes() {
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, String>> entries = substitution.entrySet();
		String[] mappings = getOrderedMappings();
		for (String mapping : mappings)
			for (Entry<String, String> entry : entries)
				if (entry.getValue().equals(mapping))
					sb.append(entry.getKey());
		return sb.toString();
	}
	
	public String get(String key) {
		return substitution.get(key);
	}
	
	public String get(char key) {
		return get(String.valueOf(key));
	}
	
	public String getPointerRight() {
		return get(DEFAULT_PTR_RIGHT);
	}
	
	public String getPointerLeft() {
		return get(DEFAULT_PTR_LEFT);
	}
	
	public String getIncrement() {
		return get(DEFAULT_INC);
	}
	
	public String getDecrement() {
		return get(DEFAULT_DEC);
	}
	
	public String getOutput() {
		return get(DEFAULT_OUTPUT);
	}
	
	public String getInput() {
		return get(DEFAULT_INPUT);
	}
	
	public String getLoopStart() {
		return get(DEFAULT_LOOP_START);
	}
	
	public String getLoopEnd() {
		return get(DEFAULT_LOOP_END);
	}
	
	public static final class Builder {
		private Map<String, String> substitution;
		private boolean spaceSeparated;
		
		public Builder() {
			substitution = new HashMap<>();
			spaceSeparated = false;
		}
		
		// Convenience method for trivial substitutions
		public Builder set(
				String ptrRight, String ptrLeft, String inc, 
				String dec, String out, String in, 
				String loopStart, String loopEnd
		) {
			return setPointerRight(ptrRight)
					.setPointerLeft(ptrLeft)
					.setIncrement(inc)
					.setDecrement(dec)
					.setOutput(out)
					.setInput(in)
					.setLoopStart(loopStart)
					.setLoopEnd(loopEnd);
		}
		
		public Builder spaced() {
			spaceSeparated = true;
			return this;
		}
		
		public Builder setPointerRight(String s) {
			substitution.put(DEFAULT_PTR_RIGHT, s);
			return this;
		}
		
		public Builder setPointerLeft(String s) {
			substitution.put(DEFAULT_PTR_LEFT, s);
			return this;
		}
		
		public Builder setIncrement(String s) {
			substitution.put(DEFAULT_INC, s);
			return this;
		}
		
		public Builder setDecrement(String s) {
			substitution.put(DEFAULT_DEC, s);
			return this;
		}
		
		public Builder setOutput(String s) {
			substitution.put(DEFAULT_OUTPUT, s);
			return this;
		}
		
		public Builder setInput(String s) {
			substitution.put(DEFAULT_INPUT, s);
			return this;
		}
		
		public Builder setLoopStart(String s) {
			substitution.put(DEFAULT_LOOP_START, s);
			return this;
		}
		
		public Builder setLoopEnd(String s) {
			substitution.put(DEFAULT_LOOP_END, s);
			return this;
		}
		
		public BFSubstitution build() {
			return new BFSubstitution(substitution, spaceSeparated);
		}
	}
}