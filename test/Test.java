import minusk.minuslib.core.Game;
import minusk.minuslib.core.GameController;
import minusk.minuslib.core.Window;
import minusk.minuslib.gl.GBuffer;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by MinusKelvin on 3/17/16.
 */
public class Test implements GameController {
	private final Window window;
	private GBuffer framebuffer;
	private float green;
	
	private Test() {
		glfwInit();
		
		glfwDefaultWindowHints();
		Window.setOpenglHints(3,3,true);
		
		window = new Window(1280, 720, "Test", 0, null);
		glfwSwapInterval(0);
		
		glBindVertexArray(glGenVertexArrays());
		
		framebuffer = new GBuffer.GBufferBuilder(GL_DEPTH_COMPONENT32F, 0).addTexture(GL_RGBA8, 0).build();
		framebuffer.resize(500, 500);
	}
	
	@Override
	public void update() {
		if (window.closeRequested())
			Game.stop();
		window.setTitle("Test - FPS: "+Game.getFPS());
		green += 0.01f;
		framebuffer.resize(500 + (int) (Math.sin(green)*100), 500 + (int) (Math.cos(green)*100));
	}
	
	@Override
	public void render(float alpha) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			framebuffer.bind();
			glClearBufferfv(GL_COLOR, 0, stack.floats(0,(float)Math.pow(green%1,2.2),0,0));
			glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
			glBindFramebuffer(GL_READ_FRAMEBUFFER, framebuffer.framebufferID);
			glViewport(0, 0, 1280, 720);
			glBlitFramebuffer(0, 0, framebuffer.getWidth(), framebuffer.getHeight(), 300, 100,
					300+framebuffer.getWidth(), 100+framebuffer.getHeight(), GL_COLOR_BUFFER_BIT, GL_NEAREST);
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
