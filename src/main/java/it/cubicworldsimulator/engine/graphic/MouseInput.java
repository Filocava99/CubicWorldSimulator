package it.cubicworldsimulator.engine.graphic;

import org.joml.Vector2d;

import it.cubicworldsimulator.engine.Window;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {
	private final Vector2f previousPosition;
	private final Vector2f currentPosition;
	private final Vector2f displacementVector;
	private boolean pointerInWindow;
	private boolean leftButtonPressed;
	private boolean rightButtonPressed;
	
	public MouseInput() {
		this.previousPosition = new Vector2f(-1, -1);
		this.currentPosition = new Vector2f(0, 0);
		this.displacementVector = new Vector2f();
		this.pointerInWindow = false;
		this.leftButtonPressed = false;
		this.rightButtonPressed = false;
	}
	
	public void init(Window window) {
		glfwSetCursorPosCallback(window.getWindowHandle(), (windowHandle, xpos, ypos) ->{
			this.currentPosition.x = (float)xpos;
			this.currentPosition.y = (float)ypos;
		});
		glfwSetCursorEnterCallback(window.getWindowHandle(), (windowHandle, entered)->{
			this.pointerInWindow = entered;
		});
		glfwSetMouseButtonCallback(window.getWindowHandle(), (windowHandle, button, action, mode)->{
			this.leftButtonPressed = (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS);
			this.rightButtonPressed = (button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS);
		});
	}

	public void input(Window window) {
		displacementVector.x = 0;
		displacementVector.y = 0;
		if (previousPosition.x > 0 && previousPosition.y > 0 && pointerInWindow) {
			double deltax = currentPosition.x - previousPosition.x;
			double deltay = currentPosition.y - previousPosition.y;
			boolean rotateX = deltax != 0;
			boolean rotateY = deltay != 0;
			if (rotateX) {
				displacementVector.y = (float) deltax;
			}
			if (rotateY) {
				displacementVector.x = (float) deltay;
			}
		}
		previousPosition.x = currentPosition.x;
		previousPosition.y = currentPosition.y;
	}

	public Vector2f getDisplacementVector() {
		return this.displacementVector;
	}
	
	public boolean isLeftButtonPressed() {
		return this.leftButtonPressed;
	}
	
	public boolean isRightButtonPressed() {
		return this.rightButtonPressed;
	}
	
}
