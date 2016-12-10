package minusk.minuslib.gl;

import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.system.jemalloc.JEmalloc.je_free;
import static org.lwjgl.system.jemalloc.JEmalloc.je_malloc;
import static org.lwjgl.system.jemalloc.JEmalloc.je_realloc;

/**
 * @author MinusKelvin
 */
public class Texture2D {
	public final int id;
	
	public Texture2D() {
		id = glGenTextures();
	}
	
	public Texture2D load(int mip, int texFormat, ByteBuffer fileData) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer x = stack.ints(0);
			IntBuffer y = stack.ints(0);
			IntBuffer comp = stack.ints(1);
			
			ByteBuffer img = stbi_load_from_memory(fileData, x, y, comp, 4);
			
			allocate(x.get(0), y.get(0), mip, texFormat, img);
			
			stbi_image_free(img);
		}
		return this;
	}
	
	public Texture2D load(int mip, int texFormat, InputStream file) throws IOException {
		byte[] bytes = new byte[1024];
		int count;
		ByteBuffer stuff = je_malloc(0);
		
		while ((count = file.read(bytes)) != -1) {
			int pos = stuff.remaining();
			stuff = je_realloc(stuff, pos+count);
			stuff.position(pos);
			stuff.put(bytes, 0, count);
			stuff.position(0);
		}
		
		load(mip, texFormat, stuff);
		
		je_free(stuff);
		
		return this;
	}
	
	public Texture2D allocate(int w, int h, int mip, int format, ByteBuffer data) {
		glBindTexture(GL_TEXTURE_2D, id);
		glTexImage2D(GL_TEXTURE_2D, mip, format, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
		return this;
	}
	
	public Texture2D allocate(int w, int h, int mip, int format) {
		glBindTexture(GL_TEXTURE_2D, id);
		glTexImage2D(GL_TEXTURE_2D, mip, format, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
		return this;
	}
	
	public Texture2D putData(int x, int y, int w, int h, int mip, ByteBuffer data) {
		glBindTexture(GL_TEXTURE_2D, id);
		glTexSubImage2D(GL_TEXTURE_2D, mip, x, y, w, h, GL_RGBA, GL_UNSIGNED_BYTE, data);
		return this;
	}
	
	public Texture2D parameteri(int param, int value) {
		glBindTexture(GL_TEXTURE_2D, id);
		glTexParameteri(GL_TEXTURE_2D, param, value);
		return this;
	}
	
	public void dispose() {
		glDeleteTextures(id);
	}
}
