package it.cubicworldsimulator.engine.graphic;

import it.cubicworldsimulator.engine.Transformation;

import java.util.Observer;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera implements Observer{
    private final float cameraStep = 1;
    private final Vector3f cameraMovement = new Vector3f();
    private Vector3f position;
    private Vector3f rotation;
    private final Transformation transformation;
    private Matrix4f viewMatrix;
    private VisualizationStrategy visualizationStrategy;

    public Camera() {
        this(new Vector3f(0, 35, 0), new Vector3f(0, 0, 0));
    }
    
    public Camera(Vector3f position) {
    	this(position, new Vector3f(0, 0, 0));
    }

    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
        transformation = new Transformation();
        viewMatrix = new Matrix4f();
        this.visualizationStrategy = (p, r) -> {
    		Vector3f newPosition = new Vector3f(p);
    		return newPosition;
    	};
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

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }
    
    public void setStrategy(VisualizationStrategy visualizationStrategy) {
    	this.visualizationStrategy = visualizationStrategy;
    }
    
    public float getCameraStep() {
        return cameraStep;
    }

    public Vector3f getCameraMovement() {
        return cameraMovement;
    }

	@Override
	public void update(Vector3f position, Vector3f rotation) {
		// TODO Auto-generated method stub
		this.rotation = new Vector3f(rotation);
		this.position = this.visualizationStrategy.calculatePosition(position, this.rotation);
	}

}