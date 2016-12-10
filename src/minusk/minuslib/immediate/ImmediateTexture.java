package minusk.minuslib.immediate;

import minusk.minuslib.gl.ShaderProgram;
import minusk.minuslib.gl.Texture2D;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * @author MinusKelvin
 */
public class ImmediateTexture  extends ImmediateBase {
	private int color, tex;
	private float texX, texY;
	
	public ImmediateTexture() {
		super(ImmediateTexture.getShaderProgram(), ImmediateTexture.getProjLoc(), 24, 1024, GL_TRIANGLES);
	}
	
	@Override
	protected void setupVertexPointers() {
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 24, 0);
		glVertexAttribPointer(1, 4, GL_UNSIGNED_BYTE, true, 24, 12);
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 24, 16);
		
		glBindTexture(GL_TEXTURE_2D, tex);
	}
	
	public void color(int c) {
		color = c;
	}
	
	public void texture(float u, float v) {
		texX = u;
		texY = v;
	}
	
	public void vertex(float x, float y, float z) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			ByteBuffer buf = stack.malloc(24);
			buf.putFloat(x);
			buf.putFloat(y);
			buf.putFloat(z);
			buf.putInt(color);
			buf.putFloat(texX);
			buf.putFloat(texY);
			buf.position(0);
			putData(buf);
		}
	}
	
	public void bindTexture(int id) {
		tex = id;
	}
	
	public void bindTexture(Texture2D texture) {
		tex = texture.id;
	}
	
	private static ShaderProgram shader;
	private static int proj;
	
	private static ShaderProgram getShaderProgram() {
		if (shader == null) {
			try {
				shader = new ShaderProgram(new InputStreamReader(ImmediateColor.class.getResourceAsStream("/minusk/minuslib/immediate/shaders/texture.vs.glsl")),
						new InputStreamReader(ImmediateColor.class.getResourceAsStream("/minusk/minuslib/immediate/shaders/texture.fs.glsl")));
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
