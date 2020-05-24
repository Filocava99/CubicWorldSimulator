package it.cubicworldsimulator.engine.graphic;

import it.cubicworldsimulator.engine.Transformation;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private final float cameraStep = 1;
    private final Vector3f cameraMovement = new Vector3f();

    private final Vector3f position;

    private final Vector3f rotation;

    private final Transformation transformation;

    private Matrix4f viewMatrix;

    public Camera() {
        this(new Vector3f(0, 35, 0), new Vector3f(0, 0, 0));
    }

    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
        transformation = new Transformation();
        viewMatrix = new Matrix4f();
    }

    protected Vector3f calculateNewPosition(Vector3f offset) {
    	Vector3f newPosition = new Vector3f(this.position);
    	if ( offset.z != 0 ) {
    		newPosition.x += (float)Math.sin(Math.toRadians(rotation.y)) * -1.0f * offset.z;
    		newPosition.z += (float)Math.cos(Math.toRadians(rotation.y)) * offset.z;
        }
        if ( offset.x != 0) {
        	newPosition.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offset.x;
        	newPosition.z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * offset.x;
        }
        newPosition.y += offset.y;
    	return newPosition;
    }
    
    public Matrix4f updateViewMatrix(){
        return transformation.updateGenericViewMatrix(position, rotation, viewMatrix);
    }

    public Matrix4f getViewMatrix(){
        return viewMatrix;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void movePosition(float offsetX, float offsetY, float offsetZ) {
    	Vector3f newPosition = calculateNewPosition(new Vector3f(offsetX,offsetY,offsetZ));
    	this.position.x = newPosition.x;
    	this.position.y = newPosition.y;
    	this.position.z = newPosition.z;
    	/*  if ( offsetZ != 0 ) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            position.z += (float)Math.cos(Math.toRadians(rotation.y)) * offsetZ;
        }
        if ( offsetX != 0) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }
        position.y += offsetY; */
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    public void moveRotation(float offsetX, float offsetY, float offsetZ) {
        rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.z += offsetZ;
    }

    public float getCameraStep() {
        return cameraStep;
    }

    public Vector3f getCameraMovement() {
        return cameraMovement;
    }
}