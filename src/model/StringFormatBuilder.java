package model;

import java.io.Serializable;

public class StringFormatBuilder implements Serializable, Comparable<StringBuilder>, Appendable, CharSequence {
	private static final long serialVersionUID = 4559974153365969616L;
	
	private StringBuilder sb;	// Backed by real StringBuilder
	
	/* Constructors */
	
	public StringFormatBuilder() {
        sb = new StringBuilder();
    }
	
	public StringFormatBuilder(CharSequence seq) {
		sb = new StringBuilder(seq);
	}
	
	public StringFormatBuilder(int capacity) {
		sb = new StringBuilder(capacity);
	}
	
	public StringFormatBuilder(String str) {
		sb = new StringBuilder(str);
	}
	
	public StringFormatBuilder(String format, Object...args) {
		this(String.format(format, args));
	} 
	
	/* New format methods */
	
	public StringFormatBuilder append(String format, Object...args) {
		sb.append(String.format(format, args));
		return this;
	}
	
	public StringFormatBuilder appendln(String format, Object...args) {
		return append(format+"%n", args);
	}
	
	public StringFormatBuilder prepend(String format, Object...args) {
		return insert(0, String.format(format, args));
	}
	
	public StringFormatBuilder prependln(String format, Object...args) {
		return prepend(format+"%n", args);
	}
	
	public StringFormatBuilder prepend(Object obj) {
        return prepend(String.valueOf(obj));
    }

    public StringFormatBuilder prepend(String str) {
        sb.insert(0, str);
        return this;
    }

    public StringFormatBuilder prepend(StringBuffer sb) {
        this.sb.insert(0, sb);
        return this;
    }
    
    public StringFormatBuilder prepend(CharSequence s) {
        sb.insert(0, s);
        return this;
    }

    public StringFormatBuilder prepend(CharSequence s, int start, int end) {
        sb.insert(0, s, start, end);
        return this;
    }

    public StringFormatBuilder prepend(char[] str) {
        sb.insert(0, str);
        return this;
    }
    
    public StringFormatBuilder prepend(char[] str, int offset, int len) {
        sb.insert(0, str, offset, len);
        return this;
    }
    
    public StringFormatBuilder prepend(boolean b) {
        sb.insert(0, b);
        return this;
    }
        
    public StringFormatBuilder prepend(char c) {
        sb.insert(0, c);
        return this;
    }

    public StringFormatBuilder prepend(int i) {
        sb.insert(0, i);
        return this;
    }
    
    public StringFormatBuilder prepend(long lng) {
        sb.insert(0, lng);
        return this;
    }
    
    public StringFormatBuilder prepend(float f) {
        sb.insert(0, f);
        return this;
    }
    
    public StringFormatBuilder prepend(double d) {
        sb.insert(0, d);
        return this;
    }

    public StringFormatBuilder prependCodePoint(int codePoint) {
    	if (Character.isBmpCodePoint(codePoint)) {
            return prepend((char)codePoint);
        }
        return prepend(Character.toChars(codePoint));
    }
	
    /* Extra convenience methods */
    
    public boolean peek(String str) {
		return str.length() <= length() 
				&& subSequence(0, str.length()).equals(str);
	}
    
    public char lastChar() {
    	int length = length();
    	if (length == 0)
    		return '\0';
    	return charAt(length - 1);
    }
    
    public StringFormatBuilder deleteFirstChars(int count) {
    	return delete(0, count);
    }
    
    public StringFormatBuilder deleteFirstChar() {
    	return deleteFirstChars(1);
    }
    
    public StringFormatBuilder deleteFirstChar(char thisChar) {
    	if (charAt(0) == thisChar)
    		deleteFirstChar();
    	return this;
    }
    
    public StringFormatBuilder deleteLastChars(int count) {
    	int start = length() - count;
    	if (start < 0)
    		start = 0;
    	return delete(start, length());
    }
  
    public StringFormatBuilder deleteLastChar() {
    	return deleteLastChars(1);
    }
    
    public StringFormatBuilder deleteLastChar(char thisChar) {
    	if (charAt(length() - 1) == thisChar)
    		deleteLastChar();
    	return this;
    }
    
	/* StringBuilder methods */
	
    public StringFormatBuilder append(Object obj) {
        return append(String.valueOf(obj));
    }

    public StringFormatBuilder append(String str) {
        sb.append(str);
        return this;
    }

    public StringFormatBuilder append(StringBuffer sb) {
        sb.append(sb);
        return this;
    }
    
    public StringFormatBuilder append(CharSequence s) {
        sb.append(s);
        return this;
    }

    public StringFormatBuilder append(CharSequence s, int start, int end) {
        sb.append(s, start, end);
        return this;
    }

    public StringFormatBuilder append(char[] str) {
        sb.append(str);
        return this;
    }
    
    public StringFormatBuilder append(char[] str, int offset, int len) {
        sb.append(str, offset, len);
        return this;
    }
    
    public StringFormatBuilder append(boolean b) {
        sb.append(b);
        return this;
    }
        
    public StringFormatBuilder append(char c) {
        sb.append(c);
        return this;
    }

    public StringFormatBuilder append(int i) {
        sb.append(i);
        return this;
    }
    
    public StringFormatBuilder append(long lng) {
        sb.append(lng);
        return this;
    }
    
    public StringFormatBuilder append(float f) {
        sb.append(f);
        return this;
    }
    
    public StringFormatBuilder append(double d) {
        sb.append(d);
        return this;
    }

    public StringFormatBuilder appendCodePoint(int codePoint) {
        sb.appendCodePoint(codePoint);
        return this;
    }

    public StringFormatBuilder delete(int start, int end) {
        sb.delete(start, end);
        return this;
    }

    public StringFormatBuilder deleteCharAt(int index) {
        sb.deleteCharAt(index);
        return this;
    }

    public StringFormatBuilder replace(int start, int end, String str) {
        sb.replace(start, end, str);
        return this;
    }
    
    public StringFormatBuilder insert(int index, char[] str, int offset, int len) {
        sb.insert(index, str, offset, len);
        return this;
    }

    public StringFormatBuilder insert(int offset, Object obj) {
        sb.insert(offset, obj);
        return this;
    }

    public StringFormatBuilder insert(int offset, String str) {
        sb.insert(offset, str);
        return this;
    }

    public StringFormatBuilder insert(int offset, char[] str) {
        sb.insert(offset, str);
        return this;
    }

    public StringFormatBuilder insert(int dstOffset, CharSequence s) {
        sb.insert(dstOffset, s);
        return this;
    }

    public StringFormatBuilder insert(int dstOffset, CharSequence s, int start, int end) {
        sb.insert(dstOffset, s, start, end);
        return this;
    }

    public StringFormatBuilder insert(int offset, boolean b) {
        sb.insert(offset, b);
        return this;
    }

    public StringFormatBuilder insert(int offset, char c) {
        sb.insert(offset, c);
        return this;
    }

    public StringFormatBuilder insert(int offset, int i) {
        sb.insert(offset, i);
        return this;
    }

    public StringFormatBuilder insert(int offset, long l) {
        sb.insert(offset, l);
        return this;
    }

    public StringFormatBuilder insert(int offset, float f) {
        sb.insert(offset, f);
        return this;
    }

    public StringFormatBuilder insert(int offset, double d) {
        sb.insert(offset, d);
        return this;
    }

    public int indexOf(String str) {
        return sb.indexOf(str);
    }
    
    public int indexOf(String str, int fromIndex) {
        return sb.indexOf(str, fromIndex);
    }
    
    public int lastIndexOf(String str) {
        return sb.lastIndexOf(str);
    }
    
    public int lastIndexOf(String str, int fromIndex) {
        return sb.lastIndexOf(str, fromIndex);
    }
    
    public StringFormatBuilder reverse() {
        sb.reverse();
        return this;
    }
  
    /* Overriden methods */

	@Override
	public int length() {
		return sb.length();
	}

	@Override
	public char charAt(int index) {
		return sb.charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return sb.subSequence(start, end);
	}

	@Override
	public int compareTo(StringBuilder o) {
		return sb.compareTo(o);
	}
	
	@Override
	public String toString() {
    	return sb.toString();
    }
	
	public static void main(String[] args) {
		StringFormatBuilder sb = new StringFormatBuilder("Hello, World!");
		System.out.println(sb.deleteFirstChars(1));
		
	}
}