package minusk.minuslib.gl;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;
import static org.lwjgl.opengl.GL32.glTexImage2DMultisample;

/**
 * @author MinusKelvin
 */
public class GBuffer {
	public final int framebufferID;
	private final int depthTarget, depthFormat, depthSamples;
	private final TextureDescriber[] tex;
	private int width, height;
	
	private GBuffer(int depthFormat, int depthSamples, ArrayList<TextureDescriber> textures) {
		framebufferID = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, framebufferID);
		this.depthFormat = depthFormat;
		this.depthSamples = depthSamples;
		
		tex = new TextureDescriber[textures.size()];
		int[] bufs = new int[textures.size()];
		int[] ids = new int[tex.length+1];
		glGenTextures(ids);
		depthTarget = ids[ids.length-1];
		if (depthSamples == 0) {
			glBindTexture(GL_TEXTURE_2D, depthTarget);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		} else {
			glBindTexture(GL_TEXTURE_2D_MULTISAMPLE, depthTarget);
		}
		glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, depthTarget, 0);
		
		for (int i = 0; i < textures.size(); i++) {
			bufs[i] = GL_COLOR_ATTACHMENT0 + i;
			tex[i] = textures.get(i);
			tex[i].id = ids[i];
			if (tex[i].samples == 0) {
				glBindTexture(GL_TEXTURE_2D, ids[i]);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			} else {
				glBindTexture(GL_TEXTURE_2D_MULTISAMPLE, ids[i]);
			}
			glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + i, ids[i], 0);
		}
		
		glDrawBuffers(bufs);
	}
	
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		
		if (depthSamples == 0) {
			glBindTexture(GL_TEXTURE_2D, depthTarget);
			glTexImage2D(GL_TEXTURE_2D, 0, depthFormat, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, 0);
		} else {
			glBindTexture(GL_TEXTURE_2D_MULTISAMPLE, depthTarget);
			glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, depthSamples, depthFormat, width, height, false);
		}
		
		for (int i = 0; i < tex.length; i++) {
			if (tex[i].samples == 0) {
				glBindTexture(GL_TEXTURE_2D, tex[i].id);
				glTexImage2D(GL_TEXTURE_2D, 0, tex[i].format, width, height, 0, GL_RGBA, GL_FLOAT, 0);
			} else {
				glBindTexture(GL_TEXTURE_2D_MULTISAMPLE, tex[i].id);
				glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE, depthSamples, depthFormat, width, height, false);
			}
		}
	}
	
	public void bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, framebufferID);
		glViewport(0, 0, width, height);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getColorBufferID(int texNum) {
		return tex[texNum].id;
	}
	
	public int getDepthBufferID() {
		return depthTarget;
	}
	
	public void dispose() {
		int[] textures = new int[tex.length+1];
		for (int i = 0; i < tex.length; i++)
			textures[i] = tex[i].id;
		textures[tex.length] = depthTarget;
		glDeleteTextures(textures);
		glDeleteFramebuffers(framebufferID);
	}
	
	private static class TextureDescriber {
		private int samples = 0;
		private int format;
		private int id;
	}
	
	public static class GBufferBuilder {
		private ArrayList<TextureDescriber> textures = new ArrayList<>();
		private int depth, samples;
		
		public GBufferBuilder(int depthFormat, int depthSamples) {
			depth = depthFormat;
			samples = depthSamples;
		}
		
		public GBufferBuilder addTexture(int format, int samples) {
			TextureDescriber tex = new TextureDescriber();
			tex.format = format;
			tex.samples = samples;
			textures.add(tex);
			return this;
		}
		
		public GBuffer build() {
			return new GBuffer(depth, samples, textures);
		}
	}
}
