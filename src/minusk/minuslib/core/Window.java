package minusk.minuslib.core;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2d;
import org.joml.Vector2i;
import org.joml.Vector4i;
import org.lwjgl.glfw.*;
import org.lwjgl.system.MemoryStack;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;

/**
 * @author MinusKelvin
 */
public final class Window {
	public final long pointer;
	
	private GLFWWindowCloseCallback windowClose;
	private GLFWWindowFocusCallback windowFocus;
	private GLFWWindowIconifyCallback windowIconify;
	private GLFWWindowPosCallback windowPos;
	private GLFWWindowRefreshCallback windowRefresh;
	private GLFWWindowSizeCallback windowSize;
	private GLFWCharCallback character;
	private GLFWCharModsCallback characterMods;
	private GLFWCursorEnterCallback mouseEnter;
	private GLFWCursorPosCallback mousePos;
	private GLFWDropCallback fileDrop;
	private GLFWFramebufferSizeCallback fboSize;
	private GLFWScrollCallback scroll;
	private GLFWMouseButtonCallback mouseButton;
	private GLFWKeyCallback key;
	
	public Window(int width, int height, String title, long monitor, @Nullable  Window shared) {
		pointer = glfwCreateWindow(width, height, title, monitor, shared == null ? 0 : shared.pointer);
	}
	
	public GLFWWindowCloseCallback setCallback(GLFWWindowCloseCallback callback) {
		return glfwSetWindowCloseCallback(pointer, windowClose = callback);
	}
	
	public GLFWWindowFocusCallback setCallback(GLFWWindowFocusCallback callback) {
		return glfwSetWindowFocusCallback(pointer, windowFocus = callback);
	}
	
	public GLFWWindowIconifyCallback setCallback(GLFWWindowIconifyCallback callback) {
		return glfwSetWindowIconifyCallback(pointer, windowIconify = callback);
	}
	
	public GLFWWindowPosCallback setCallback(GLFWWindowPosCallback callback) {
		return glfwSetWindowPosCallback(pointer, windowPos = callback);
	}
	
	public GLFWWindowRefreshCallback setCallback(GLFWWindowRefreshCallback callback) {
		return glfwSetWindowRefreshCallback(pointer, windowRefresh = callback);
	}
	
	public GLFWWindowSizeCallback setCallback(GLFWWindowSizeCallback callback) {
		return glfwSetWindowSizeCallback(pointer, windowSize = callback);
	}
	
	public GLFWCharCallback setCallback(GLFWCharCallback callback) {
		return glfwSetCharCallback(pointer, character = callback);
	}
	
	public GLFWCharModsCallback setCallback(GLFWCharModsCallback callback) {
		return glfwSetCharModsCallback(pointer, characterMods = callback);
	}
	
	public GLFWCursorPosCallback setCallback(GLFWCursorPosCallback callback) {
		return glfwSetCursorPosCallback(pointer, mousePos = callback);
	}
	
	public GLFWMouseButtonCallback setCallback(GLFWMouseButtonCallback callback) {
		return glfwSetMouseButtonCallback(pointer, mouseButton = callback);
	}
	
	public GLFWDropCallback setCallback(GLFWDropCallback callback) {
		return glfwSetDropCallback(pointer, fileDrop = callback);
	}
	
	public GLFWFramebufferSizeCallback setCallback(GLFWFramebufferSizeCallback callback) {
		return glfwSetFramebufferSizeCallback(pointer, fboSize = callback);
	}
	
	public GLFWCursorEnterCallback setCallback(GLFWCursorEnterCallback callback) {
		return glfwSetCursorEnterCallback(pointer, mouseEnter = callback);
	}
	
	public GLFWScrollCallback setCallback(GLFWScrollCallback callback) {
		return glfwSetScrollCallback(pointer, scroll = callback);
	}
	
	public GLFWKeyCallback setCallback(GLFWKeyCallback callback) {
		return glfwSetKeyCallback(pointer, key = callback);
	}
	
	public void makeCurrent() {
		glfwMakeContextCurrent(pointer);
	}
	
	public void swapBuffers() {
		glfwSwapBuffers(pointer);
	}
	
	public String getClipboardString() {
		return glfwGetClipboardString(pointer);
	}
	
	public void setClipboardString(String str) {
		glfwSetClipboardString(pointer, str);
	}
	
	public void destroy() {
		glfwDestroyWindow(pointer);
	}
	
	public void setInputMode(int mode, int value) {
		glfwSetInputMode(pointer, mode, value);
	}
	
	public int getInputMode(int mode) {
		return glfwGetInputMode(pointer, mode);
	}
	
	@Contract("null -> fail")
	public Vector2d getCursorPos(Vector2d dest) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			DoubleBuffer db = stack.mallocDouble(2);
			glfwGetCursorPos(pointer, db, (DoubleBuffer) db.duplicate().position(1));
			return dest.set(db);
		}
	}
	
	public void setCursorPos(double x, double y) {
		glfwSetCursorPos(pointer, x, y);
	}
	
	@Contract("null -> fail")
	public Vector2i getFramebufferSize(Vector2i dest) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer db = stack.mallocInt(2);
			glfwGetFramebufferSize(pointer, db, (IntBuffer) db.duplicate().position(1));
			return dest.set(db);
		}
	}
	
	public void setAspectRatio(int numer, int denom) {
		glfwSetWindowAspectRatio(pointer, numer, denom);
	}
	
	@Contract("null -> fail")
	public Vector2i getSize(Vector2i dest) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer db = stack.mallocInt(2);
			glfwGetWindowSize(pointer, db, (IntBuffer) db.duplicate().position(1));
			return dest.set(db);
		}
	}
	
	public void setSize(int width, int height) {
		glfwSetWindowSize(pointer, width, height);
	}
	
	public void setSizeLimits(int minWidth, int maxWidth, int minHeight, int maxHeight) {
		glfwSetWindowSizeLimits(pointer, minWidth, minHeight, maxWidth, maxHeight);
	}
	
	public void setTitle(String title) {
		glfwSetWindowTitle(pointer, title);
	}
	
	public boolean getKey(int key) {
		return glfwGetKey(pointer, key) == GLFW_PRESS;
	}
	
	public boolean getButton(int button) {
		return glfwGetMouseButton(pointer, button) == GLFW_PRESS;
	}
	
	@Contract("null -> fail")
	public Vector2i getPos(Vector2i dest) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer db = stack.mallocInt(2);
			glfwGetWindowPos(pointer, db, (IntBuffer) db.duplicate().position(1));
			return dest.set(db);
		}
	}
	
	@Contract("null -> fail")
	public Vector4i getFrameSize(Vector4i dest) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer db = stack.mallocInt(4);
			glfwGetWindowFrameSize(pointer, db, (IntBuffer) db.duplicate().position(1),
					(IntBuffer) db.duplicate().position(2), (IntBuffer) db.duplicate().position(3));
			return dest.set(db);
		}
	}
	
	public boolean closeRequested() {
		return glfwWindowShouldClose(pointer);
	}
	
	public void setCloseRequested(boolean closeRequested) {
		glfwSetWindowShouldClose(pointer, closeRequested);
	}
	
	public void iconify() {
		glfwIconifyWindow(pointer);
	}
	
	public void restore() {
		glfwRestoreWindow(pointer);
	}
	
	public void focus() {
		glfwFocusWindow(pointer);
	}
	
	public void show() {
		glfwShowWindow(pointer);
	}
	
	public void hide() {
		glfwHideWindow(pointer);
	}
	
	public void maximize() {
		glfwMaximizeWindow(pointer);
	}
	
	public int getAttribute(int attrib) {
		return glfwGetWindowAttrib(pointer, attrib);
	}
	
	public static void setOpenglHints(int major, int minor, boolean core) {
		glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_API);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, major);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, minor);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, core ? 1 : 0);
		glfwWindowHint(GLFW_OPENGL_PROFILE, core ? GLFW_OPENGL_CORE_PROFILE : GLFW_OPENGL_COMPAT_PROFILE);
	}
}
