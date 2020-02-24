package it.cubicworldsimulator.engine.graphic;

import org.joml.Vector2d;

import it.cubicworldsimulator.engine.Window;
import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {
	private final Vector2d previousPosition;
	private final Vector2d currentPosition;
	private final Vector2d displacementVector;
	private boolean pointerInWindow;
	private boolean leftButtonPressed;
	private boolean rightButtonPressed;
	
	public MouseInput() {
		this.previousPosition = new Vector2d(-1, -1);
		this.currentPosition = new Vector2d(0, 0);
		this.displacementVector = new Vector2d();
		this.pointerInWindow = false;
		this.leftButtonPressed = false;
		this.rightButtonPressed = false;
	}
	
	public void init(Window window) {
		glfwSetCursorPosCallback(window.getWindowHandle(), (windowHandle, xpos, ypos) ->{
			this.currentPosition.x = xpos;
			this.currentPosition.y = ypos;
		});
		glfwSetCursorEnterCallback(window.getWindowHandle(), (windowHandle, entered)->{
			this.pointerInWindow = entered;
		});
		glfwSetMouseButtonCallback(window.getWindowHandle(), (windowHandle, button, action, mode)->{
			this.leftButtonPressed = (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS);
			this.rightButtonPressed = (button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS);
		});
	}
	
	public Vector2d getDisplacementVector() {
		return this.displacementVector;
	}
	
	public boolean isLeftButtonPressed() {
		return this.leftButtonPressed;
	}
	
	public boolean isRightButtonPressed() {
		return this.rightButtonPressed;
	}
	
}
