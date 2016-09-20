import minusk.minuslib.core.Game;
import minusk.minuslib.core.GameController;
import minusk.minuslib.core.Window;

import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwInit;

/**
 * Created by MinusKelvin on 3/17/16.
 */
public class Test implements GameController {
	private final Window window;
	
	public Test() {
		glfwInit();
		
		glfwDefaultWindowHints();
		Window.setOpenglHints(3,3,true);
		
		window = new Window(1280, 720, "Test", 0, null);
	}
	
	@Override
	public void update() {
		if (window.closeRequested())
			Game.stop();
	}
	
	@Override
	public void render(float alpha) {
		window.swapBuffers();
	}
	
	@Override
	public void cleanUp() {
		
	}
	
	public static void main(String[] args) {
		Game.start(new Test());
	}
}
