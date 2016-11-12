import minusk.minuslib.core.Game;
import minusk.minuslib.core.GameController;
import minusk.minuslib.core.Window;
import minusk.minuslib.immediate.ImmediateColor;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by MinusKelvin on 3/17/16.
 */
public class Test implements GameController {
	private final Window window;
	private ImmediateColor immediate;
	
	private Test() {
		glfwInit();
		
		glfwDefaultWindowHints();
		Window.setOpenglHints(3,3,true);
		
		window = new Window(1280, 720, "Test", 0, null);
		glfwSwapInterval(0);
		window.setCallback(GLFWFramebufferSizeCallback.create((win,w,h) -> glViewport(0, 0, w, h)));
		
		glBindVertexArray(glGenVertexArrays());
		
		immediate = new ImmediateColor();
		immediate.transformation = new Matrix4f();
	}
	
	@Override
	public void update() {
		if (window.closeRequested())
			Game.stop();
		window.setTitle("Test - FPS: "+Game.getFPS());
	}
	
	@Override
	public void render(float alpha) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			glClearBufferfv(GL_COLOR, 0, stack.floats(0,0,0,0));
		}
		
		immediate.transformation.identity();
		immediate.begin();
		immediate.color(0xff0000);
		immediate.vertex(0, 0, 0);
		immediate.color(0xff00);
		immediate.vertex(0, 1, 0);
		immediate.color(0xff);
		immediate.vertex(1, 0, 0);
		immediate.draw();
		
		immediate.transformation.translate(-0.5f,-0.5f,0).rotate(1, 0, 0, 1);
		immediate.drawLast();
		
		window.swapBuffers();
	}
	
	@Override
	public void cleanUp() {
		
	}
	
	public static void main(String[] args) {
		Game.start(new Test());
	}
}
