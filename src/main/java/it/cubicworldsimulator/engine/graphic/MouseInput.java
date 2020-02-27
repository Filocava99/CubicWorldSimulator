package it.cubicworldsimulator.engine.graphic;

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
	private final Window window;
	
	public MouseInput(Window window) {
		this.previousPosition = new Vector2f(-1, -1);
		this.currentPosition = new Vector2f(0, 0);
		this.displacementVector = new Vector2f();
		this.pointerInWindow = false;
		this.leftButtonPressed = false;
		this.rightButtonPressed = false;
		this.window = window;
	}
	
	public void init() {
		setCursorPosition();
		checkCursorEnter();
		setMouseButtonPressed();
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
	
	public void input() {
		this.displacementVector.x = 0;
		this.displacementVector.y = 0;
		if(this.previousPosition.x > 0 && this.previousPosition.y > 0 && this.pointerInWindow) {
			double deltaX = this.currentPosition.x - this.previousPosition.x;
			double deltaY = this.currentPosition.y - this.previousPosition.y;
			boolean rotateX = (deltaX != 0);
			boolean rotateY = (deltaY != 0);
			if(rotateX) {
				this.displacementVector.y = (float) deltaX;
			}
			if(rotateY) {
				this.displacementVector.x = (float) deltaY;
			}
		}
		this.previousPosition.x = this.currentPosition.x;
		this.previousPosition.y = this.currentPosition.y;
	}

	private void setCursorPosition() {
		glfwSetCursorPosCallback(this.window.getWindowHandle(), (windowHandle, xpos, ypos) ->{
			this.currentPosition.x = (float)xpos;
			this.currentPosition.y = (float)ypos;
		});
	}

	private void checkCursorEnter() {
		glfwSetCursorEnterCallback(this.window.getWindowHandle(), (windowHandle, entered)->{
			this.pointerInWindow = entered;
		});
	}

	private void setMouseButtonPressed() {
		glfwSetMouseButtonCallback(this.window.getWindowHandle(), (windowHandle, button, action, mode)->{
			this.leftButtonPressed = (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS);
			this.rightButtonPressed = (button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS);
		});
	}

}
