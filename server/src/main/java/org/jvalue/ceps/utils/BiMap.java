package org.jvalue.ceps.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public final class BiMap<T1, T2> {

	private final Map<T1, T2> map1 = new HashMap<T1, T2>();
	private final Map<T2, T1> map2 = new HashMap<T2, T1>();


	public void put(T1 key1, T2 key2) {
		Assert.assertFalse(map1.containsKey(key1), "already inserted key " + key1);
		Assert.assertFalse(map2.containsKey(key2), "already inserted key " + key2);
		map1.put(key1, key2);
		map2.put(key2, key1);
	}


	public T2 getSecond(T1 key) {
		return map1.get(key);
	}


	public T1 getFirst(T2 key) {
		return map2.get(key);
	}


	public boolean containsFirst(T1 key) {
		return map1.containsKey(key);
	}


	public boolean containsSecond(T2 key) {
		return map2.containsKey(key);
	}


	public void removeFirst(T1 key) {
		T2 key2 = map1.remove(key);
		map2.remove(key2);
	}


	public void removeSecond(T2 key) {
		T1 key2 = map2.remove(key);
		map1.remove(key2);
	}


	public Set<T1> getFirstKeySet() {
		return map1.keySet();
	}


	public Set<T2> getSecondKeySet() {
		return map2.keySet();
	}


	public int size() {
		return map1.size();
	}

}
