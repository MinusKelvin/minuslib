package minusk.minuslib.core;

/**
 * Created by MinusKelvin on 3/17/16.
 */
public interface GameController {
	/**
	 * Updates this game at a fixed timestep.
	 * Called after the current <code>GameState</code>'s update method.
	 */
	void update();
	
	/**
	 * Renders this game somewhere between the current and previous frames.
	 * This method is called after the current <code>GameState</code>'s render method.
	 * This method must swap buffers.
	 * 
	 * @param alpha A value between 0 and 1, representing the fraction from the current frame to the next frame.
	 */
	void render(double alpha);
	
	/**
	 * Clean up phase.
	 */
	void cleanUp();
}
