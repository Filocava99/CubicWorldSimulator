package it.cubicworldsimulator.game.utility;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class BiConcurrentHashMap<X, T> implements BiMap<X,T>{

    private final ConcurrentHashMap<X, T> keyMap = new ConcurrentHashMap<>( );
    private final ConcurrentHashMap<T, X> valueMap = new ConcurrentHashMap<>();

    @Override
    public void put(Object key, Object value) {
        keyMap.put((X)key, (T)value);
        valueMap.put((T)value, (X)key);
    }

    public T getByKey(Object key) {
        return keyMap.get(key);
    }

    @Override
    public X getByValue(Object value) {
        return valueMap.get(value);
    }

    @Override
    public T removeByKey(Object key) {
        T value = keyMap.remove(key);
        valueMap.remove(value);
        return value;
    }

    @Override
    public X removeByValue(Object value) {
        X key = valueMap.remove(value);
        keyMap.remove(key);
        return key;
    }

    @Override
    public boolean containsKey(Object key) {
        return keyMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValue(value);
    }
}
