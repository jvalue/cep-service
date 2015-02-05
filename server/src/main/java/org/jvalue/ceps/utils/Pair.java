package org.jvalue.ceps.utils;


public final class Pair<T1, T2> {

	public T1 first;
	public T2 second;

	public Pair(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}


	@Override
	@SuppressWarnings("rawtypes")
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Pair)) return false;
		if (other == this) return true;
		Pair pair = (Pair) other;
		return equals(first, pair.first) && equals(second, pair.second);
	}


	private boolean equals(Object o1, Object o2) {
		if (o1 == null && o2 == null) return true;
		if (o1 == null || o2 == null) return false;
		return o1.equals(o2);
	}

}
