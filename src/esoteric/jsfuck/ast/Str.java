package esoteric.jsfuck.ast;

import java.util.Arrays;

import model.AST;

public class Str implements JSObject {
	private String value;
	public final Num length;
	
	public Str(String value) {
		this.value = value;
		length = new Num(value.length());
	}
	
	public String getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Str other = (Str) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	public Str anchor(AST name) {
		return new Str(String.format("<a name=\"%s\">%s</a>", toString(name).getValue(), value));
	}
	
	public AST at(AST index) {
		int i = toNumber(index).getIntValue();
		if (i >= value.length() || i <= -value.length())
			return new Undefined();
		if (i < 0)
			i = value.length() + i;
		return new Str("" + value.charAt(i));
	}
	
	public Str big() {
		return new Str(String.format("<big>%s</big>", value));
	}
	
	public Str blink() {
		return new Str(String.format("<blink>%s</blind>", value));
	}
	
	public Str bold() {
		return new Str(String.format("<b>%s</b>", value));
	}
		
	public Str charAt(AST index) {
		int i = toNumber(index).getIntValue();
		String s;
		if (i < 0 || i >= value.length())
			s = "";
		else
			s = "" + value.charAt(i);
		return new Str(s);
	}
	
	public Str charAt() {
		return charAt(new Num(0));
	}
	
	public Num charCodeAt(AST index) {
		int i = toNumber(index).getIntValue();
		if (i < 0 || i >= value.length())
			return new NaN();
		return new Num(value.charAt(i));
	}
	
	public AST codePointAt(AST pos) {
		if (pos == null)
			return new Undefined();
		int i = toNumber(pos).getIntValue();
		return new Num(value.codePointAt(i));
	}
	
	public Str concat(AST...strs) {
		StringBuilder sb = new StringBuilder();
		for (AST str : strs)
			sb.append(str.toString());
		return new Str(sb.toString());
	}
	
	public FunctionDef constructor() {
		return new FunctionDef("String", this, "constructor");
	}
	
	public Bool endsWith(AST str, AST length) {
		String s = value;
		int t = toNumber(length).getIntValue();
		s = s.substring(0, t);
		return new Bool(s.endsWith(toString(str).getValue()));
	}
	
	public Bool endsWith(AST str) {
		String s = value;
		int t = toNumber(length).getIntValue();
		s = s.substring(0, t);
		return new Bool(s.endsWith(toString(str).getValue()));
	}
	
	public Str fixed() {
		return new Str(String.format("<tt>%s</tt>", value));
	}
	
	public Str fontcolor(AST v) {
		return new Str(String.format("<font color=\"%s\">%s</font>", v == null ? "undefined" : v.toString(), value));
	}
	
	public Str fontcolor() {
		return fontcolor(null);
	}
	
	public Str fontsize(AST v) {
		return new Str(String.format("<font size=\"%s\">%s</font>", v == null ? "undefined" : v.toString(), value));
	}
	
	public Str fontsize() {
		return fontsize(null);
	}
	
	public Bool includes(AST str , AST pos) {
		String s = value.substring(toNumber(pos).getIntValue());
		return new Bool(s.contains(toString(str).getValue()));
	}
	
	public Bool includes(AST str) {
		return includes(str, new Num(0));
	}
	
	public Num indexOf(AST str, AST pos) {
		int i = toNumber(pos).getIntValue();
		if (i < 0)
			i = 0;
		else if (i > value.length())
			return new Num(-1);
		return new Num(value.indexOf(toString(str).getValue(), i));
	}
	
	public Num indexOf(AST str) {
		return indexOf(str, new Num(0));
	}
	
	public Str italics() {
		return new Str(String.format("<i>%s</i>", value));
	}
	
	public Num lastIndexOf(AST str, AST pos) {
		return new Num(value.lastIndexOf(toString(str).getValue(), toNumber(pos).getIntValue()));
	}
	
	public Num lastIndexOf(AST str) {
		return lastIndexOf(str, new Num(0));
	}
	
	public Str link(AST url) {
		return new Str(String.format("<a href=\"%s\">%s</a>", toString(url), value));
	}
	
	public Str padEnd(AST length, AST p) {
		int targetLength = toNumber(length).getIntValue();
		int padding = targetLength - value.length();
		Str pad = toString(p);
		String s = pad.getValue();
		while (value.length() + s.length() + pad.length.getIntValue() <= targetLength &&  padding-->0)
			s += s;
		int missing = targetLength - (value.length() + s.length());
		if (missing > 0)
			s += pad.getValue().substring(0, missing);
		return new Str(value + s);
	}
	
	public Str padEnd(AST length) {
		return padEnd(length, new Str(" "));
	}
	
	public Str padStart(AST length, AST p) {
		int targetLength = toNumber(length).getIntValue();
		int padding = targetLength - value.length();
		Str pad = toString(p);
		String s = pad.getValue();
		while (value.length() + s.length() + pad.length.getIntValue() <= targetLength &&  padding-->0)
			s += s;
		int missing = targetLength - (value.length() + s.length());
		if (missing > 0)
			s += pad.getValue().substring(0, missing);
		return new Str(s + value);
	}
	
	public Str padStart(AST length) {
		return padEnd(length, new Str(" "));
	}
	
	public Str repeat(AST count) {
		int c = toNumber(count).getIntValue();
		StringBuilder sb = new StringBuilder();
		while (c-->0)
			sb.append(value);
		return new Str(sb.toString());
	}
	
	public Str replace(AST pattern, AST replacement) {
		String p = toString(pattern).getValue(),
			r = toString(replacement).getValue();
		return new Str(value.replace(p, r));
	}
	
	public Str replaceAll(AST pattern, AST replacement) {
		String p = toString(pattern).getValue(),
			r = toString(replacement).getValue();
		return new Str(value.replaceAll(p, r));
	}
	
	public Num search(AST regexp) {
		String r = toString(regexp).getValue();
		return new Num(value.indexOf(r));
	}
	
	public Str slice(AST start, AST end) {
		int is, ie;
		try {
			is = toNumber(start).getIntValue();
		} catch (Exception e) {
			is = 0;
		}
		try {
			ie = toNumber(end).getIntValue();
		} catch (Exception e) {
			ie = value.length();
		}
		if (is > value.length())
			return new Str("");
		if (is < 0)
			is = Math.max(is + value.length(), 0);
		if (ie < 0)
			ie = Math.max(ie + value.length(), 0);
		if (ie <= is)
			return new Str("");
		return new Str(value.substring(is, ie));
	}
	
	public Str small() {
		return new Str(String.format("<small>%s</small>", value));
	}
	
	public Array split(AST sep, AST limit) {
		int l = toNumber(limit).getIntValue();
		Array array = new Array();
		if (l == 0)
			return array;
		String[] split = value.split(toString(sep).getValue());
		for (int i=0; i<l && i<split.length; i++)
			array.add(new Str(split[i]));
		return array;
	}
	
	public Array split(AST sep) {
		return split(sep, new Num(Integer.MAX_VALUE));
	}
	
	public static void main(String[] args) {
		Str s = new Str("alert164t50t42t110ellt157t54t40t167t157rldt41t42t51");
		System.out.println(Arrays.toString(s.split(new Str("t")).toArray()));
	}
	
	public Array split() {
		Array array = new Array();
		array.add(new Str(value));
		return array;
	}
	
	public Bool startsWith(AST str, AST pos) {
		return new Bool(value.startsWith(toString(str).getValue(), toNumber(pos).getIntValue()));
	}
	
	public Bool startsWith(AST str) {
		return startsWith(str, new Num(0));
	}
	
	public Str strike() {
		return new Str(String.format("<strike>%s</strike>", value));
	}
	
	public Str sub() {
		return new Str(String.format("<sub>%s</sub>", value));
	}
	
	public Str substr(AST start, AST length) {
		int s, l;
		try { s = toNumber(start).getIntValue(); }
		catch (Exception e) { s = 0; }
		try { l = toNumber(length).getIntValue(); }
		catch (Exception e) { l = value.length(); }
		if (s < 0)
			s = s + value.length();
		return new Str(value.substring(s, s+l));
	}
	
	public Str substr(AST start) {
		return substr(start, new Num(value.length()));
	}
	
	public Str substring(AST start, AST end) {
		int s, l;
		try { s = toNumber(start).getIntValue(); }
		catch (Exception e) { s = 0; }
		try { l = toNumber(length).getIntValue(); }
		catch (Exception e) { l = value.length(); }
		if (s < 0)
			s = s + value.length();
		return new Str(value.substring(s, l));
	}
	
	public Str substring(AST start) {
		return substring(start, new Num(value.length()));
	}
	
	public Str sup() {
		return new Str(String.format("<sup>%s</sup>", value));
	}
	
	public Str toLowerCase() {
		return new Str(value.toLowerCase());
	}
	
	public Str toStringJS() {
		return new Str(toString());
	}
	
	@Override
	public String toString() {
		return value;
	}
	
	public Str toUpperCase() {
		return new Str(value.toUpperCase());
	}
	
	public Str trim() {
		return new Str(value.trim());
	}
	
	public Str trimEnd() {
		int count = 0, index = value.length() - 1;
		while (Character.isWhitespace(value.charAt(index--)))
			count++;
		return new Str(value.substring(0, value.length() - count));
	}
	
	public Str trimRight() {
		return trimEnd();
	}
	
	public Str trimStart() {
		int count = 0, index = 0;
		while (Character.isWhitespace(value.charAt(index++)))
			count++;
		return new Str(value.substring(count, value.length()));
	}
	
	public Str trimLeft() {
		return trimStart();
	}
	
	public Str valueOf() {
		return this;
	}
}
