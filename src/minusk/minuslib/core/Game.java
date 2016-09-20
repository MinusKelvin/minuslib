package minusk.minuslib.core;

import java.util.Arrays;

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
	/** used to find the average FPS  */
	private static double[] frameHistory = new double[60];
	static {
		Arrays.fill(frameHistory, 1/60.0);
	}
	
	/**
	 * Starts the game loop. This is a fixed time step game loop.
	 * 
	 * @param controller The <code>GameController</code> the game loop should call
	 */
	public static void start(GameController controller) {
		if (game != null)
			throw new IllegalStateException("Cannot start the gameloop while the gameloop is running");
		
		game = controller;
		
		looping = true;
		double currentTime = glfwGetTime();
		double frametime = 1 / ups;
		do {
			double t = glfwGetTime();
			// Update until we've caught up or have reached the maximum skip count
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
			
			// Shift all the elements of this array down one
			System.arraycopy(frameHistory, 1, frameHistory, 0, frameHistory.length - 1);
			frameHistory[frameHistory.length-1] = glfwGetTime()-t;
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
	
	/**
	 * @return The game's FPS calculated through an average of frame times over the past 10 frames.
	 */
	public static int getFPS() {
		double sum = 0;
		for (double d : frameHistory)
			sum += d;
		return (int) Math.round(frameHistory.length/sum);
	}
}
