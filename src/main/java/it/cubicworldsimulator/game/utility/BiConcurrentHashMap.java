package it.cubicworldsimulator.game.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

public class BiConcurrentHashMap<X, T> implements BiMap<X,T>{

    private final static Logger logger = LogManager.getLogger(BiConcurrentHashMap.class);

    private final ConcurrentHashMap<X, T> keyMap = new ConcurrentHashMap<>( );
    private final ConcurrentHashMap<T, X> valueMap = new ConcurrentHashMap<>();

    @Override
    public void put(X key, T value) {
        keyMap.put(key, value);
        valueMap.put(value, key);
    }

    public T getByKey(X key) {
        return keyMap.get(key);
    }

    @Override
    public X getByValue(T value) {
        return valueMap.get(value);
    }

    @Override
    public T removeByKey(X key) {
        T value = keyMap.remove(key);
        valueMap.remove(value);
        return value;
    }

    @Override
    public X removeByValue(T value) {
        X key = valueMap.remove(value);
        keyMap.remove(key);
        return key;
    }

    @Override
    public boolean containsKey(X key) {
        return keyMap.containsKey(key);
    }

    @Override
    public boolean containsValue(T value) {
        return valueMap.containsKey(value);
    }
}
