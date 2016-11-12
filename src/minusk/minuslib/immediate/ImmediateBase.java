package minusk.minuslib.immediate;

import minusk.minuslib.gl.ShaderProgram;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.system.jemalloc.JEmalloc.je_malloc;
import static org.lwjgl.system.jemalloc.JEmalloc.je_realloc;

/**
 * Abstract immediate mode emulation class.
 * 
 * @author MinusKelvin
 */
public abstract class ImmediateBase {
	public final ShaderProgram program;
	public final int vbo, bytesPerVertex, drawMode, projLoc;
	public Matrix4f projection, transformation;
	private final Matrix4f combined = new Matrix4f();
	private int maxVerts, verts;
	private ByteBuffer data;
	private boolean drawing = false;
	
	public ImmediateBase(ShaderProgram program, int projLoc, int bytesPerVertex, int maxVerts, int drawMode) {
		this.program = program;
		this.bytesPerVertex = bytesPerVertex;
		this.maxVerts = maxVerts;
		this.drawMode = drawMode;
		this.projLoc = projLoc;
		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		data = je_malloc(maxVerts * bytesPerVertex);
	}
	
	public void draw() {
		if (!drawing)
			throw new IllegalStateException("Can't end drawing when not drawing (call begin())");
		draw(true, true);
		drawing = false;
	}
	
	public void drawLast() {
		if (drawing)
			throw new IllegalStateException("Can't draw the last thing drawn while trying to draw (call draw() or record())");
		draw(false, true);
	}
	
	public void record() {
		if (!drawing)
			throw new IllegalStateException("Can't record something to draw when not drawing (call begin())");
		draw(true, false);
		drawing = false;
	}
	
	private void draw(boolean updateVBO, boolean draw) {
		if (verts == 0)
			return;
		
		if (draw) {
			if (projection == null)
				combined.identity();
			else
				combined.set(projection);
			if (transformation != null)
				combined.mul(transformation);
			
			program.bind();
			try (MemoryStack stack = MemoryStack.stackPush()) {
				FloatBuffer mat = stack.callocFloat(16);
				combined.get(mat);
				glUniformMatrix4fv(projLoc, false, mat);
			}
		}
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		
		if (updateVBO) {
			data.position(0);
			glBufferData(GL_ARRAY_BUFFER, data, GL_STREAM_DRAW);
		}
		
		if (draw) {
			setupVertexPointers();
			
			glDrawArrays(drawMode, 0, verts);
		}
	}
	
	protected abstract void setupVertexPointers();
	
	public void begin() {
		if (drawing)
			throw new IllegalStateException("Can't begin drawing while drawing (call draw() or record())");
		
		drawing = true;
		verts = 0;
	}
	
	/**
	 * Please stack allocate <code>stuff</code>.
	 * <code>stuff</code> can contain several vertices.
	 * 
	 * @param stuff
	 */
	protected void putData(ByteBuffer stuff) {
		if (!drawing)
			throw new IllegalStateException("Can't draw when not drawing (call begin())");
		
		int v = verts;
		verts += stuff.remaining() / bytesPerVertex;
		
		boolean realloc = false;
		while (verts > maxVerts) {
			realloc = true;
			maxVerts += 1024;
		}
		if (realloc)
			data = je_realloc(data, maxVerts*bytesPerVertex);
		
		data.position(v*bytesPerVertex);
		data.put(stuff);
	}
}
