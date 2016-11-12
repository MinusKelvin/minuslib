package minusk.minuslib.immediate;

import minusk.minuslib.gl.ShaderProgram;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

/**
 * @author MinusKelvin
 */
public class ImmediateColor extends ImmediateBase {
	private int color;
	
	public ImmediateColor() {
		super(ImmediateColor.getShaderProgram(), ImmediateColor.getProjLoc(), 16, 1024, GL_TRIANGLES);
	}
	
	@Override
	protected void setupVertexPointers() {
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 16, 0);
		glVertexAttribPointer(1, 4, GL_UNSIGNED_BYTE, true, 16, 12);
	}
	
	public void color(int c) {
		color = c;
	}
	
	public void vertex(float x, float y, float z) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			ByteBuffer buf = stack.malloc(16);
			buf.putFloat(x);
			buf.putFloat(y);
			buf.putFloat(z);
			buf.putInt(color);
			buf.position(0);
			putData(buf);
		}
	}
	
	private static ShaderProgram shader;
	private static int proj;
	
	private static ShaderProgram getShaderProgram() {
		if (shader == null) {
			try {
				shader = new ShaderProgram(new InputStreamReader(ImmediateColor.class.getResourceAsStream("/minusk/minuslib/immediate/shaders/color.vs.glsl")),
						new InputStreamReader(ImmediateColor.class.getResourceAsStream("/minusk/minuslib/immediate/shaders/color.fs.glsl")));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return shader;
	}
	
	private static int getProjLoc() {
		return glGetUniformLocation(getShaderProgram().id, "proj");
	}
}
