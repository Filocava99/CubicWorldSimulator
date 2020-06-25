package it.cubicworldsimulator.game.utility;

import java.util.Optional;

public interface BiMap<X,T> {

    /**
     * Add a value to the bimap
     * @param key
     * @param value
     */
    void put(X key, T value);

    /**
     * Get a value using a key
     * @param key
     * @return value
     */
    T getByKey(X key);

    /**
     * Get a key using a value
     * @param value
     * @return key
     */
    X getByValue(T value);

    /**
     * Remove a value using a key
     * @param key
     * @return an optional value
     */
    Optional<T> removeByKey(X key);

    /**
     * Removes a key using a value
     * @param value
     * @return optional key
     */
    Optional<X> removeByValue(T value);

    /**
     * Checks if a value is present using a key
     * @param key
     * @return boolean
     */
    boolean containsKey(X key);

    /**
     * Checks if a key is present using a value
     * @param value
     * @return boolean
     */
    boolean containsValue(T value);

}
