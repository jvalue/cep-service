package org.jvalue.ceps.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public final class BiMapTest {

	@Test
	public void testGetRemove() {
		BiMap<String, String> map = new BiMap<String, String>();

		assertEquals(0, map.size());
		assertTrue(map.getFirstKeySet().isEmpty());
		assertTrue(map.getSecondKeySet().isEmpty());

		map.put("hello", "world");
		assertEquals(1, map.size());
		assertTrue(map.getFirstKeySet().contains("hello"));
		assertEquals(1, map.getFirstKeySet().size());
		assertTrue(map.getSecondKeySet().contains("world"));
		assertEquals(1, map.getSecondKeySet().size());
		assertEquals("hello", map.getFirst("world"));
		assertEquals("world", map.getSecond("hello"));
		assertTrue(map.containsFirst("hello"));
		assertTrue(map.containsSecond("world"));

		map.put("foo", "bar");
		assertEquals(2, map.size());
		assertTrue(map.getFirstKeySet().contains("foo"));
		assertEquals(2, map.getFirstKeySet().size());
		assertTrue(map.getSecondKeySet().contains("bar"));
		assertEquals(2, map.getSecondKeySet().size());
		assertEquals("foo", map.getFirst("bar"));
		assertEquals("bar", map.getSecond("foo"));
		assertTrue(map.containsFirst("foo"));
		assertTrue(map.containsSecond("bar"));

		map.removeFirst("hello");
		assertEquals(1, map.size());
		assertNull(map.getFirst("world"));
		assertNull(map.getSecond("hello"));
		assertFalse(map.containsFirst("hello"));
		assertFalse(map.containsSecond("world"));

		map.removeSecond("bar");
		assertEquals(0, map.size());
		assertNull(map.getFirst("bar"));
		assertNull(map.getSecond("foo"));
		assertFalse(map.containsFirst("foo"));
		assertFalse(map.containsSecond("bar"));

		map.put("dummy", "dummy");
	}

}
