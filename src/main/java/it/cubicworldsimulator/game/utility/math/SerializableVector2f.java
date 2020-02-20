package it.cubicworldsimulator.game.utility.math;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector2ic;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class SerializableVector2f extends Vector2f implements Serializable {
    public SerializableVector2f() {
    }

    public SerializableVector2f(float d) {
        super(d);
    }

    public SerializableVector2f(float x, float y) {
        super(x, y);
    }

    public SerializableVector2f(Vector2fc v) {
        super(v);
    }

    public SerializableVector2f(Vector2ic v) {
        super(v);
    }

    public SerializableVector2f(ByteBuffer buffer) {
        super(buffer);
    }

    public SerializableVector2f(int index, ByteBuffer buffer) {
        super(index, buffer);
    }

    public SerializableVector2f(FloatBuffer buffer) {
        super(buffer);
    }

    public SerializableVector2f(int index, FloatBuffer buffer) {
        super(index, buffer);
    }
}
