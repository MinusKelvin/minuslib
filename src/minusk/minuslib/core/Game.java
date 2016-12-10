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
	/** true if the game is puased (no ticking, same alpha parameter to render()) */
	private static boolean paused;
	/** maximum number of updates per frame before slowing game logic down */
	private static int maxTicks = 10;
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
		double timeOfLastFrame = glfwGetTime();
		double timeSinceTick = 1 / ups;
		do {
			double t = glfwGetTime();
			if (!paused)
				timeSinceTick += (t - timeOfLastFrame);
			timeOfLastFrame = t;
			double frametime = 1 / ups;
			
			// Update until we've caught up or have reached the maximum tick count
			int ticks = 0;
			while (timeSinceTick > frametime) {
				if (ticks++ > maxTicks) {
					// Slows the game logic down if we're lagging too much
					timeSinceTick = frametime;
					break;
				}
				timeSinceTick -= frametime;
				game.update();
			}
			
			game.render(ups * timeSinceTick);
			
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
	 * Pauses the game loop. <code>GameController.update()</code> will not be called,
	 * and <code>GameController.render(double)</code> will be called with the same alpha value.
	 */
	public static void pause() {
		paused = true;
	}
	
	/** Unpauses the game loop. See Game.pause() */
	public static void unpause() {
		paused = false;
	}
	
	public static boolean isPaused() {
		return paused;
	}
	
	/**
	 * @param ups The number of times per second the game's update method should be called
	 */
	public static void setTargetUPS(double ups) {
		Game.ups = ups;
	}
	
	/**
	 * @param maxTicks The number of times the game's update method can be called in a single frame before slowing game updates
	 */
	public static void setMaxTicksPerFrame(int maxTicks) {
		Game.maxTicks = maxTicks;
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
