package esoteric.jsfuck.ast;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import model.AST;

public class Num implements JSObject {
	private String value;
	private boolean integer;
	
	public Num(String value, boolean integer) {
		if (value.contains("e")) {
			value = value.replace("e", "E");
			if (value.contains("E+"))
				value = value.replace("E+", "E");
		}
		this.value = value;
		this.integer = integer;
	}
	
	public Num(double num) {
		value = String.valueOf(num);
		integer = isInteger(num);
		if (integer && value.endsWith(".0"))
			value = value.substring(0, value.length() - 2);
	}
	
	public boolean isIndexable() {
		return integer;
	}
	
	public boolean isExponential() {
		return value.contains("E");
	}
	
	public int getIntValue() {
		if (value.contains("E"))
			return new BigDecimal(value).intValue();
		return Integer.valueOf(value);
	}
	
	public double getDoubleValue() {
		return Double.valueOf(value);
	}
	
	public String getValue() {
		return value;
	}
	
	public boolean isDouble() {
		return !integer;
	}
	
	public boolean isInteger() {
		return integer;
	}
	
	public static boolean isInteger(Double d) {
		return d % 1 == 0;
	}
	
	public static boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isInteger(String s) {
		try { 
			Integer.parseInt(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (integer ? 1231 : 1237);
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Num other = (Num) obj;
		if (integer != other.integer)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	public FunctionDef constructor() {
		return new FunctionDef("Number", this, "constructor");
	}
	
	private static String decimals(int decimals, String symbol) {
		StringBuilder sb = new StringBuilder();
		while (decimals-->0) sb.append(symbol);
		return sb.toString();
	}
	
	public Str toExponential(AST decimals) {
		int dec = toNumber(decimals).getIntValue();
		NumberFormat scientific = new DecimalFormat("0."+decimals(dec, "#")+"E0");
		String converted;
		if (NaN.class.isInstance(this))
			converted = "NaN";
		else if (Infinity.class.isInstance(this))
			converted = Infinity.class.cast(this).isNegative() ? "-Infinity" : "Infinity"; 
		else {
			converted = scientific.format(getDoubleValue());
			if (!converted.contains("E-"))
				converted = converted.replace("E", "e+");
			else
				converted = converted.replace("E-", "e-");
		}
		return new Str(converted);
	}
	
	public Str toExponential() {
		return toExponential(new Num(value.length()));
	}
	
	public Str toFixed(AST digits) {
		int dig = toNumber(digits).getIntValue();
		NumberFormat fixed = new DecimalFormat("0."+decimals(dig, "0"));
		String converted;
		if (NaN.class.isInstance(this))
			converted = "NaN";
		else if (Infinity.class.isInstance(this))
			converted = Infinity.class.cast(this).isNegative() ? "-Infinity" : "Infinity";
		else 
			converted = fixed.format(getDoubleValue());
		return new Str(converted);
	}
	
	public Str toFixed() {
		return toFixed(new Num(value.length()));
	}
	
	public Str toPrecision(AST precision) {
		return toFixed(precision);	// TODO check if it's important to actually implement and not piggy back
	}
	
	public Str toPrecision() {
		return toFixed();
	}
	
	public Str toStringJS(AST radix) {
		Str r = toString(radix);
		return new Str(new BigInteger(value).toString(Integer.parseInt(r.getValue())));
	}
	
	@Override
	public String toString() {
		return value;
	}
	
	public Num valueOf() {
		return this;
	}
}