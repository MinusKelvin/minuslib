import minusk.minuslib.core.Game;
import minusk.minuslib.core.GameController;
import minusk.minuslib.core.Window;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by MinusKelvin on 3/17/16.
 */
public class Test implements GameController {
	private final Window window;
	private float green;
	
	private Test() {
		glfwInit();
		
		glfwDefaultWindowHints();
		Window.setOpenglHints(3,3,true);
		
		window = new Window(1280, 720, "Test", 0, null);
		glfwSwapInterval(0);
		
		glBindVertexArray(glGenVertexArrays());
	}
	
	@Override
	public void update() {
		if (window.closeRequested())
			Game.stop();
		window.setTitle("Test - FPS: "+Game.getFPS());
		green += 0.01f;
	}
	
	@Override
	public void render(float alpha) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			glClearBufferfv(GL_COLOR, 0, stack.floats(0,(float)Math.pow(green%1,2.2),0,0));
		}
		window.swapBuffers();
	}
	
	@Override
	public void cleanUp() {
		
	}
	
	public static void main(String[] args) {
		Game.start(new Test());
	}
}
