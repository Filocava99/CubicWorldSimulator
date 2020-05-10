package it.cubicworldsimulator.game.utility;

import junit.framework.TestCase;

public class BiConcurrentHashMapTest extends TestCase {

    BiConcurrentHashMap<String, Integer> map = new BiConcurrentHashMap<>();

    public void testGetByKey() {
        map.put("test", 5);
        assertEquals(Integer.valueOf(5),map.getByKey("test"));
    }

    public void testGetByValue() {
        map.put("test", 5);
        assertEquals("test",map.getByValue(5));
    }

    public void testRemoveByKey() {
        map.put("test", 5);
        assertEquals(Integer.valueOf(5),map.removeByKey("test"));
    }

    public void testRemoveByValue() {
        map.put("test", 5);
        assertEquals("test",map.removeByValue(5));
    }

    public void testContainsKey() {
        map.put("test", 5);
        assertTrue(map.containsKey("test"));
    }

    public void testContainsValue() {
        map.put("test", 5);
        assertTrue(map.containsValue(5));
    }
}