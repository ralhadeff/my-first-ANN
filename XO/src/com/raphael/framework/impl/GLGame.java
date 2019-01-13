package com.raphael.framework.impl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.raphael.framework.Audio;
import com.raphael.framework.FileIO;
import com.raphael.framework.Game;
import com.raphael.framework.Input;
import com.raphael.framework.Screen;

public abstract class GLGame extends Activity implements Game,GLSurfaceView.Renderer {
	enum GLGameState {
		Initialized,
		Running, 
		Paused, 
		Finished, 
		Idle
	}

	private GLSurfaceView glView;
	GLGraphics glGraphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	GLGameState state = GLGameState.Initialized;
	Object stateChanged = new Object();
	long startTime = System.nanoTime();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		glView = new GLSurfaceView(this);
		// Create an OpenGL ES 2.0 context
		glView.setEGLContextClientVersion(2);

		glView.setRenderer(this);

		setContentView(glView);

		glGraphics = new GLGraphics(glView);
		fileIO = new AndroidFileIO(this);
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, glView, 1, 1);
		getCurrentFocus().setKeepScreenOn(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		glView.onResume();
		getCurrentFocus().setKeepScreenOn(true);
	}

	public void onSurfaceCreated(GL10 unused, EGLConfig config) {

		synchronized (stateChanged) {
			if (state == GLGameState.Initialized)
				screen = getStartScreen();
			state = GLGameState.Running;
			screen.resume();
			startTime = System.nanoTime();
		}
	}

	public void onSurfaceChanged(GL10 unused, int width, int height) {
	}

	public void onDrawFrame(GL10 unused) {

		GLGameState state = null;

		synchronized (stateChanged) {
			state = this.state;
		}

		if (state == GLGameState.Running) {
			float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
			startTime = System.nanoTime();

			screen.update(deltaTime);
			screen.present(deltaTime);
		}

		if (state == GLGameState.Paused) {
			screen.pause();
			synchronized (stateChanged) {
				this.state = GLGameState.Idle;
				stateChanged.notifyAll();
			}
		}

		if (state == GLGameState.Finished) {
			screen.pause();
			screen.dispose();
			synchronized (stateChanged) {
				this.state = GLGameState.Idle;
				stateChanged.notifyAll();
			}
		}
	}

	@Override
	public void onPause() {
		synchronized (stateChanged) {
			if (isFinishing())
				state = GLGameState.Finished;
			else
				state = GLGameState.Paused;
			while (true) {
				try {
					stateChanged.wait();
					break;
				} catch (InterruptedException e) {
				}
			}
		}
		glView.onPause();
		getCurrentFocus().setKeepScreenOn(false);
		super.onPause();
	}

	public GLGraphics getGLGraphics() {
		return glGraphics;
	}

	public Input getInput() {
		return input;
	}

	public FileIO getFileIO() {
		return fileIO;
	}

	public Audio getAudio() {
		return audio;
	}

	public void setScreen(Screen newScreen) {
		if (newScreen == null)
			throw new IllegalArgumentException("Screen must not be null");

		this.screen.pause();
		this.screen.dispose();
		newScreen.resume();
		newScreen.update(0);
		this.screen = newScreen;
	}

	public Screen getCurrentScreen() {
		return screen;
	}

	public void onBackPressed() {
		screen.onBackPressed();
	}

	public void defaultBack() {
		super.onBackPressed();
	}

}
