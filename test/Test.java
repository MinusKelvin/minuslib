import minusk.minuslib.core.Game;
import minusk.minuslib.core.GameController;
import minusk.minuslib.core.Window;
import minusk.minuslib.gl.Texture2D;
import minusk.minuslib.immediate.ImmediateTexture;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_MAX_LEVEL;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by MinusKelvin on 3/17/16.
 */
public class Test implements GameController {
	private final Window window;
	private ImmediateTexture immediate;
	private Texture2D texture;
	
	private Test() {
		glfwInit();
		
		glfwDefaultWindowHints();
		Window.setOpenglHints(3,3,true);
		
		window = new Window(1280, 720, "Test", 0, null);
		glfwSwapInterval(0);
		window.setCallback(GLFWFramebufferSizeCallback.create((win,w,h) -> glViewport(0, 0, w, h)));
		
		glBindVertexArray(glGenVertexArrays());
		
		immediate = new ImmediateTexture();
		immediate.transformation = new Matrix4f();
		try {
			texture = new Texture2D().load(0, GL_RGBA8, getClass().getResourceAsStream("/test.png"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		texture.parameteri(GL_TEXTURE_MAX_LEVEL, 0);
		texture.parameteri(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		texture.parameteri(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		immediate.bindTexture(texture);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	@Override
	public void update() {
		if (window.closeRequested())
			Game.stop();
		window.setTitle("Test - FPS: "+Game.getFPS());
	}
	
	@Override
	public void render(double alpha) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			glClearBufferfv(GL_COLOR, 0, stack.floats(1,1,1,1));
		}
		
		immediate.transformation.identity();
		immediate.begin();
		immediate.color(0xffffffff);
		immediate.texture(0, 0);
		immediate.vertex(0, 0, 0);
		immediate.texture(0, 1);
		immediate.vertex(0, 1, 0);
		immediate.texture(1, 0);
		immediate.vertex(1, 0, 0);
		immediate.draw();
		
		immediate.transformation.translate(-0.25f,-0.5f,0).rotate(0.5f, 0, 0, 1);
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
