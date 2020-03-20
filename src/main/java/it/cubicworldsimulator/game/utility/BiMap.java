package it.cubicworldsimulator.game.utility;

public interface BiMap<X extends Object,T extends Object> {

    void put(X key, T value);

    T getByKey(X key);

    X getByValue(T value);

    T removeByKey(X key);

    X removeByValue(T value);

    boolean containsKey(X key);

    boolean containsValue(T value);

}
