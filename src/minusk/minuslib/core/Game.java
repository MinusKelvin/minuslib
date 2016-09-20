package minusk.minuslib.core;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;

/**
 * Created by MinusKelvin on 3/17/16.
 */
public class Game {
	private static GameController game;
	
	/** true if game loop is running */
	private static boolean looping;
	/** maximum number of updates per frame before slowing game logic down */
	private static int maxSkips = 10;
	/** number of updates per second */
	private static double ups = 60;
	
	/**
	 * Starts the game loop. This is a fixed time step 
	 * 
	 * @param controller The <code>GameController</code> the gameloop should call
	 */
	public static void start(GameController controller) {
		if (game != null)
			throw new MinusLibException("Cannot start the gameloop while the gameloop is running");
		
		game = controller;
		
		looping = true;
		double currentTime = glfwGetTime();
		do {
			// Update until we've caught up or have reached the maximum skip count
			double frametime = 1 / ups;
			for (int i = 0; i < maxSkips && currentTime < glfwGetTime(); i++) {
				currentTime += frametime;
				game.update();
				frametime = 1 / ups;
				glfwPollEvents();
			}
			// If we're lagging too much (haven't caught up within the maximum skip count) slow game logic down
			if (currentTime < glfwGetTime())
				currentTime = glfwGetTime();
			
			game.render((float) ((glfwGetTime()-currentTime+frametime) / frametime));
			
			glfwPollEvents();
		} while (looping);
		
		game.cleanUp();
		
		game = null;
	}
	
	/**
	 * Stops the game loop if it is running.
	 */
	public static void stop() {
		looping = false;
	}
	
	/**
	 * @param ups The number of times per second the game's update method should be called
	 */
	public static void setTargetUPS(double ups) {
		Game.ups = ups;
	}
	
	/**
	 * @param maxSkips The number of times the game's update method can be called in a single frame before slowing game updates
	 */
	public static void setMaxSkips(int maxSkips) {
		Game.maxSkips = maxSkips;
	}
}
