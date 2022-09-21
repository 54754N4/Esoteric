package esoteric.jsfuck.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import model.AST;

public class Array extends ArrayList<AST> implements JSObject {
	private static final long serialVersionUID = 8889949572315829436L;

	public Array(AST...asts) {
		super(Arrays.asList(asts));
	} 
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}
	
	public AST at(AST index) {
		return get(toNumber(index).getIntValue());
	}
	
	public Array concat(Array...arrays) {
		Array result = new Array();
		for (Array array : arrays) 
			result.addAll(array);
		return result;
	}
	
	public FunctionDef constructor() {
		return new FunctionDef("Array", this, "constructor");
	}
	
	public Array copyWithin(AST target, AST start, AST end) {
		int t, s, e;
		t = toNumber(target).getIntValue();
		s = toNumber(start).getIntValue();
		e = toNumber(end).getIntValue();
		if (t < 0) 
			t = size() + t;
		if (s < 0)
			s = size() + s;
		if (e < 0)
			e = size() + e;
		for (int i=s; i<e && t<e; i++, t++)
			set(t, get(i));
		return this;
	}
	
	public Array copyWithin(AST target, AST start) {
		return copyWithin(target, start, new Num(size()));
	}
	
	public Array copyWithin(AST target) {
		return copyWithin(target, new Num(0));
	}
	
	public EntryIterator entries() {
		return new EntryIterator(this);
	}
	
	public Bool every(TriPredicate<AST, Num, Array> predicate) {
		for (int i=0; i<size(); i++)
			if (!predicate.test(get(i), new Num(i), this))
				return Bool.FALSE;
		return Bool.TRUE;
	}
	
	public Bool every(BiPredicate<AST, Num> predicate) {
		return every((ast, num, array) -> predicate.test(ast, num));
	}
	
	public Bool every(Predicate<AST> predicate) {
		return every((ast, num) -> predicate.test(ast));
	}
	
	public Array fill(AST value, AST start, AST end) {
		int s, e;
		s = toNumber(start).getIntValue();
		e = toNumber(end).getIntValue();
		if (s < 0)
			s = size() + s;
		if (e < 0)
			e = size() + e;
		for (int i=s; i<e; i++)
			set(i, value);
		return this;
	}
	
	public Array fill(AST value, AST start) {
		return fill(value, start, new Num(size()));
	}
	
	public Array fill(AST value) {
		return fill(value, new Num(0));
	}
	
	public Array filter(TriPredicate<AST, Num, Array> predicate) {
		Array array = new Array();
		for (int i=0; i<size(); i++)
			if (predicate.test(get(i), new Num(i), this))
				array.add(get(i));
		return array;
	}
	
	public Array filter(BiPredicate<AST, Num> predicate) {
		return filter((ast, num, array) -> predicate.test(ast, num));
	}
	
	public Array filter(Predicate<AST> predicate) {
		return filter((ast, num) -> predicate.test(ast));
	}
	
	public AST find(TriPredicate<AST, Num, Array> predicate) {
		for (int i=0; i<size(); i++)
			if (predicate.test(get(i), new Num(i), this))
				return get(i);
		return new Undefined();
	}
	
	public AST find(BiPredicate<AST, Num> predicate) {
		return find((ast, num, array) -> predicate.test(ast, num));
	}
	
	public AST find(Predicate<AST> predicate) {
		return find((ast, num) -> predicate.test(ast));
	}
	
	public Num findIndex(TriPredicate<AST, Num, Array> predicate) {
		for (int i=0; i<size(); i++)
			if (predicate.test(get(i), new Num(i), this))
				return new Num(i);
		return new Num(-1);
	}
	
	public Num findIndex(BiPredicate<AST, Num> predicate) {
		return findIndex((ast, num, array) -> predicate.test(ast, num));
	}
	
	public Num findIndex(Predicate<AST> predicate) {
		return findIndex((ast, num) -> predicate.test(ast));
	}
	
	public AST findLast(TriPredicate<AST, Num, Array> predicate) {
		for (int i=size()-1; i >= 0; i--)
			if (predicate.test(get(i), new Num(i), this))
				return get(i);
		return new Undefined();
	}
	
	public AST findLast(BiPredicate<AST, Num> predicate) {
		return findLast((ast, num, array) -> predicate.test(ast, num));
	}
	
	public AST findLast(Predicate<AST> predicate) {
		return findLast((ast, num) -> predicate.test(ast));
	}
	
	public Num findLastIndex(TriPredicate<AST, Num, Array> predicate) {
		for (int i=size()-1; i >= 0; i--)
			if (predicate.test(get(i), new Num(i), this))
				return new Num(i);
		return new Num(-1);
	}
	
	public Num findLastIndex(BiPredicate<AST, Num> predicate) {
		return findLastIndex((ast, num, array) -> predicate.test(ast, num));
	}
	
	public Num findLastIndex(Predicate<AST> predicate) {
		return findLastIndex((ast, num) -> predicate.test(ast));
	}
	
	public Array flat(AST depth) {
		int d = toNumber(depth).getIntValue();
		if (d < 1)
			d = 1;
		Array array = new Array();
		for (AST ast : this) {
			if (Array.class.isInstance(ast))
				ast = array.flat(new Num(d - 1));
			array.add(ast);
		}
		return array;
	}
	
	public Array flat() {
		return flat(new Num(1));
	}
	
	public Array flatMap(TriFunction<AST, Num, Array, AST> map) {
		return map(map).flat();
	}
	
	public Array flatMap(BiFunction<AST, Num, AST> map) {
		return flatMap((ast, num, array) -> map.apply(ast, num));
	}
	
	public Array flatMap(Function<AST, AST> map) {
		return flatMap((ast, num) -> map.apply(ast));
	}
	
	public void forEach(TriConsumer<AST, Num, Array> consumer) {
		for (int i=0; i<size(); i++)
			consumer.accept(get(i), new Num(i), this);
	}
	
	public void forEach(BiConsumer<AST, Num> consumer) {
		forEach((ast, num, array) -> consumer.accept(ast, num));
	}
	
	public Bool includes(AST ast, AST index) {
		int start = toNumber(index).getIntValue();
		for (int i=start; i<size(); i++)
			if (get(i).equals(ast))
				return Bool.TRUE;
		return Bool.FALSE;
	}
	
	public Bool includes(AST ast) {
		return includes(ast, new Num(0));
	}
	
	public Num indexOf(AST ast, AST index) {
		int start = toNumber(index).getIntValue();
		for (int i=start; i<size(); i++)
			if (get(i).equals(ast))
				return new Num(i);
		return new Num(-1);
	}
	
	public Num indexOf(AST ast) {
		return indexOf(ast, new Num(0));
	}
	
	public Str join(AST sep) {
		Str s = toString(sep);
		StringBuilder sb = new StringBuilder();
		for (AST ast : this)
			sb.append(toString(ast))
				.append(s.getValue());
		sb.delete(sb.length() - s.length.getIntValue(), sb.length());
		return new Str(sb.toString());
	}
	
	public Str join() {
		return join(new Str(","));
	}
	
	public KeysIterator keys() {
		return new KeysIterator(size());
	}
	
	public Num lastIndexOf(AST ast, AST index) {
		int end = toNumber(index).getIntValue();
		for (int i=end; i >= 0; i--)
			if (get(i).equals(ast))
				return new Num(i);
		return new Num(-1);
	}
	
	public Num lastIndexOf(AST ast) {
		return lastIndexOf(ast, new Num(size() - 1));
	}
	
	public Array map(TriFunction<AST, Num, Array, AST> map) {
		Array array = new Array();
		for (int i=0; i<size(); i++)
			array.add(map.apply(get(i), new Num(i), this));
		return array;
	}
	
	public Array map(BiFunction<AST, Num, AST> map) {
		return map((ast, num, array) -> map.apply(ast, num));
	}
	
	public Array map(Function<AST, AST> map) {
		return map((ast, num) -> map.apply(ast));
	}
	
	public AST pop() {
		if (size() - 1 < 0)
			return new Undefined();
		return remove(size() - 1);
	}
	
	public Num push(AST...elements) {
		for (AST element : elements) 
			add(element);
		return new Num(size());
	}
	
	public Array reverse() {
		Array array = new Array();
		for (int i=size()-1; i>=0; i++)
			array.add(get(i));
		return array;
	}
	
	public AST shift() {
		if (size() == 0)
			return new Undefined();
		return remove(0);
	}
	
	public Array slice(AST start, AST end) {
		int s, e;
		s = toNumber(start).getIntValue();
		e = toNumber(end).getIntValue();
		if (s < 0)
			s = size() + s;
		if (e < 0)
			e = size() + e;
		Array array = new Array();
		for (int i=s; i<e; i++)
			array.add(get(i));
		return array;
	}
	
	public Array slice(AST start) {
		return slice(start, new Num(size()-1));
	}
	
	public Array slice() {
		return slice(new Num(0));
	}
	
	public Bool some(TriPredicate<AST, Num, Array> predicate) {
		Bool bool = Bool.FALSE;
		for (int i=0; i<size(); i++)
			if (predicate.test(get(i), new Num(i), this))
				bool = Bool.TRUE;
		return bool;
	}
	
	public Bool some(BiPredicate<AST, Num> predicate) {
		return some((ast, num, array) -> predicate.test(ast, num));
	}
	
	public Bool some(Predicate<AST> predicate) {
		return some((ast, num) -> predicate.test(ast));
	}
	
	public Num unshift(AST...elements) {
		for (int i=elements.length-1; i>=0; i++)
			add(0, elements[i]);
		return new Num(size());
	}
	
	public ValuesIterator values() {
		return new ValuesIterator(this);
	}
	
	public Str toStringJS() {
		return new Str(Arrays.toString(toArray()));
	}
	
	@FunctionalInterface
	public static interface TriPredicate<A, B, C> {
		boolean test(A a, B b, C c);
	}
	
	@FunctionalInterface
	public static interface TriFunction<A, B, C, D> {
		D apply(A a, B b, C c);
	}
	
	@FunctionalInterface
	public static interface TriConsumer<A, B, C> {
		void accept(A a, B b, C c);
	}
	
	public static class Entry implements AST {
		public final Num index;
		public final AST value;		// public because JS allows Entry.value calls
		
		public Entry(Num index, AST value) {
			this.index = index;
			this.value = value;
		}
	}
	
	public static class KeysIterator implements Iterator<Num>, AST {
		private int index, length;
		
		public KeysIterator(int length) {
			this.length = length;
			index = 0;
		}
		
		@Override
		public boolean hasNext() {
			return index < length;
		}

		@Override
		public Num next() {
			return new Num(index++);
		}
	}
	
	public static class ValuesIterator implements Iterator<AST>, AST {
		private int index;
		private Array array;
		
		public ValuesIterator(Array array) {
			this.array = array;
			index = 0;
		}

		@Override
		public boolean hasNext() {
			return index < array.size();
		}

		@Override
		public AST next() {
			return array.get(index++);
		}
	}
	
	public static class EntryIterator implements Iterator<Entry>, AST {
		private Array array;
		private int index;
		
		public EntryIterator(Array array) {
			this.array = array;
			index = 0;
		}
		
		@Override
		public boolean hasNext() {
			return index < array.size();
		}

		@Override
		public Entry next() {
			Entry entry = new Entry(new Num(index), array.get(index));
			index++;
			return entry;
		}
	}
}
