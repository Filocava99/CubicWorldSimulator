package it.cubicworldsimulator.game.utility;

import java.util.Optional;

public interface BiMap<X,T> {

    void put(X key, T value);

    T getByKey(X key);

    X getByValue(T value);

    Optional<T> removeByKey(X key);

    Optional<X> removeByValue(T value);

    boolean containsKey(X key);

    boolean containsValue(T value);

}
