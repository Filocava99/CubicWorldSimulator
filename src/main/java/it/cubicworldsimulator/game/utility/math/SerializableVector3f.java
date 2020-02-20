package it.cubicworldsimulator.game.utility.math;

import org.joml.*;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class SerializableVector3f extends Vector3f implements Serializable {
    public SerializableVector3f() {
    }

    public SerializableVector3f(float d) {
        super(d);
    }

    public SerializableVector3f(float x, float y, float z) {
        super(x, y, z);
    }

    public SerializableVector3f(Vector3fc v) {
        super(v);
    }

    public SerializableVector3f(Vector3ic v) {
        super(v);
    }

    public SerializableVector3f(Vector2fc v, float z) {
        super(v, z);
    }

    public SerializableVector3f(Vector2ic v, float z) {
        super(v, z);
    }

    public SerializableVector3f(ByteBuffer buffer) {
        super(buffer);
    }

    public SerializableVector3f(int index, ByteBuffer buffer) {
        super(index, buffer);
    }

    public SerializableVector3f(FloatBuffer buffer) {
        super(buffer);
    }

    public SerializableVector3f(int index, FloatBuffer buffer) {
        super(index, buffer);
    }
}
