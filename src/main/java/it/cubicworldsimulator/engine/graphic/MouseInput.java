package it.cubicworldsimulator.engine.graphic;

import org.joml.Vector2d;
import org.joml.Vector2f;
import it.cubicworldsimulator.engine.Window;
import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {
	private final Vector2d previousPosition;
	private final Vector2d currentPosition;
	private final Vector2f displacementVector;
	private boolean pointerInWindow;
	private boolean leftButtonPressed;
	private boolean rightButtonPressed;
	
	public MouseInput() {
		this.previousPosition = new Vector2d(-1, -1);
		this.currentPosition = new Vector2d(0, 0);
		this.displacementVector = new Vector2f();
		this.pointerInWindow = false;
		this.leftButtonPressed = false;
		this.rightButtonPressed = false;
	}
	
	public void init(Window window) {
		setCursorPosition(window);
		checkCursorEnter(window);
		setMouseButtonPressed(window);
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
	
	private void setCursorPosition(Window window) {
		glfwSetCursorPosCallback(window.getWindowHandle(), (windowHandle, xpos, ypos) ->{
			this.currentPosition.x = xpos;
			this.currentPosition.y = ypos;
		});
	}
	
	private void checkCursorEnter(Window window) {
		glfwSetCursorEnterCallback(window.getWindowHandle(), (windowHandle, entered)->{
			this.pointerInWindow = entered;
		});
	}
	
	private void setMouseButtonPressed(Window window) {
		glfwSetMouseButtonCallback(window.getWindowHandle(), (windowHandle, button, action, mode)->{
			this.leftButtonPressed = (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS);
			this.rightButtonPressed = (button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS);
		});
	}
	
}
