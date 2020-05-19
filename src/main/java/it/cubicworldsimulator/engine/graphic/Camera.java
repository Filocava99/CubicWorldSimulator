package it.cubicworldsimulator.engine.graphic;

import it.cubicworldsimulator.engine.Transformation;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
	public static final float DISTANCE_FROM_PLAYER = 50;
    private final float cameraStep = 1;
    private final Vector3f cameraMovement = new Vector3f();
    private final Vector3f position;
    private final Vector3f rotation;
    private final Transformation transformation;
    private Matrix4f viewMatrix;
    private View view;
    
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
        this.view = View.FIRSTPERSON;
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
        if ( offsetZ != 0 ) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            position.z += (float)Math.cos(Math.toRadians(rotation.y)) * offsetZ;
        }
        if ( offsetX != 0) {
            position.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }
        if(this.view.equals(View.FIRSTPERSON)) {
        	position.y += offsetY;
        }else {
        	position.y += (offsetY + DISTANCE_FROM_PLAYER);
        }
    }
    
    public void changeView() {
    	if(this.view.equals(View.FIRSTPERSON)) {
    		this.view = View.THIRDPERSON;
    	}else {
    		this.view = View.FIRSTPERSON;
    	}
    }
    
    public View getView() {
    	return this.view;
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