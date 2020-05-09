package it.cubicworldsimulator.game.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class BiConcurrentHashMap<X, T> implements BiMap<X, T> {

    private final static Logger logger = LogManager.getLogger(BiConcurrentHashMap.class);

    private final ConcurrentHashMap<X, T> keyMap = new ConcurrentHashMap<X,T>();
    private final ConcurrentHashMap<T, X> valueMap = new ConcurrentHashMap<T, X>();

    @Override
    public synchronized void put(X key, T value) {
        keyMap.put(key, value);
        valueMap.put(value, key);
    }

    public synchronized T getByKey(X key) {
        return keyMap.get(key);
    }

    @Override
    public synchronized X getByValue(T value) {
        return valueMap.get(value);
    }

    @Override
    public synchronized Optional<T> removeByKey(X key) {
        Optional<T> optional = Optional.ofNullable(keyMap.remove(key));
        optional.ifPresent(valueMap::remove);
        return optional;
    }

    @Override
    public synchronized Optional<X> removeByValue(T value) {
        Optional<X> key = Optional.ofNullable(valueMap.remove(value));
        key.ifPresent(keyMap::remove);
        return key;
    }

    @Override
    public synchronized boolean containsKey(X key) {
        return keyMap.containsKey(key);
    }

    @Override
    public synchronized boolean containsValue(T value) {
        return valueMap.containsKey(value);
    }
}
