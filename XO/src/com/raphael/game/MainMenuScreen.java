package com.raphael.game;

import java.util.List;

import android.opengl.GLES20;

import com.raphael.framework.FileIO;
import com.raphael.framework.Game;
import com.raphael.framework.Input.TouchEvent;
import com.raphael.framework.gl.Camera2D;
import com.raphael.framework.gl.ShapeBatcher;
import com.raphael.framework.gl.SpriteBatcher;
import com.raphael.framework.impl.GLGame;
import com.raphael.framework.impl.GLScreen;
import com.raphael.framework.math.OverlapTester;
import com.raphael.framework.math.Rectangle;
import com.raphael.framework.math.Vector2;

public class MainMenuScreen extends GLScreen {

	static final float WIDTH = 1080;
	static final float HEIGHT = 1920;

	static final float LONG_PRESS_TIME = 0.45f;

	// compare to squared to save on calculations (see code below)
	static final float SCROLL_CUTOFF = 1000;

	// LONG is when a long press finger is still down
	enum FingerMode {
		NONE, ONE, LONG
	}

	Game game;

	FingerMode mode;
	// index of fingers on screen
	int firstPointer;
	// for long press
	float longPressTimer;
	boolean dragged;

	Camera2D guiCam;
	// touchPoing and worldPoint are grabbers of coordinates
	// first is the current position of the first finger
	// firstDown is the original position of the first finger
	// second is the current position of the second finger
	// currentPosition and centerOfGui are camera focus points
	// middle is a helper vector
	// centerOfGui is
	Vector2 touchPoint, worldPoint, first, firstDown, firstDownWorld;
	SpriteBatcher batcher;
	ShapeBatcher shapeBatcher;
	public World world;
	WorldRenderer renderer;
	Rectangle worldBound, startX, startO;
	
    private Triangle mTriangle;
    private CopyOfTriangle mCopy;
	
	public MainMenuScreen(GLGame game) {
		super(game);
		this.game = game;
		guiCam = new Camera2D(game.getGLGraphics(), WIDTH, HEIGHT);
		touchPoint = new Vector2();
		// translated touchPoint in terms of GUI coordinates
		worldPoint = new Vector2();
		// start with 0 fingers on screen
		mode = FingerMode.NONE;
		// position of the first finger and initial position of the finger
		first = new Vector2();
		firstDown = new Vector2();
		firstDownWorld = new Vector2();

		// objects to create the graphics
		batcher = new SpriteBatcher(game.getGLGraphics(), 20000);
		shapeBatcher = new ShapeBatcher(game.getGLGraphics(), 20000);

		world = new World();
		FileIO fileIO = glGame.getFileIO();
		// read assets
		world.reader.readAssetWeights(fileIO, "Theta1.txt", world.network.layers.get(0).weights);
		world.reader.readAssetWeights(fileIO, "Theta2.txt", world.network.outputLayer.weights);	
		// try to read local files, and overwrite weights with local files if possible
		world.reader.readFileToWeights("XO/Theta1.txt", world.network.layers.get(0).weights);
		world.reader.readFileToWeights("XO/Theta2.txt", world.network.outputLayer.weights);
		
		renderer = new WorldRenderer(game.getGLGraphics(), world, batcher, shapeBatcher);

		// bound boxes for the different game elements
		worldBound = new Rectangle(0, 0, WIDTH, HEIGHT);
		
        // initialize a triangle
        mTriangle = new Triangle();
        // initialize a square
        // transfer shader to shapeBatcher
        shapeBatcher.setShaderProgram(mTriangle.mProgram);
        mCopy = new CopyOfTriangle();
        batcher.setShaderProgram(mCopy.mProgram);

	}

	@Override
	public void update(float deltaTime) {

		// delta time is supposed to normalized the updateWorld method to run
		// smoothly in real time
		// if delta time is too large due to slow performance, it might cause
		// glitches
		if (deltaTime > 0.1f)
			deltaTime = 0.1f;

		// get touch events from the touch events pool
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		// if keyboard is use, activate:
		game.getInput().getKeyEvents();
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			touchPoint.set(event.x, event.y);
			// translate touch event to GUI coordinates
			// note that the word "World" here does not reflect the class World
			guiCam.touchToWorld(touchPoint);
			// is the touch within the game bounds (World class)
			if (OverlapTester.pointInRectangle(worldBound, touchPoint)) {
				worldPoint.set(event.x, event.y);
				// update the camera of the world renderer and translate
				// coordinates to world coordinates
				renderer.cam.touchToWorld(worldPoint);
				// handle the touch
				handleWorldTouch(event);
			}
			// handle the touch in the GUI, if needed
			// this is on top of the world handling
			switch (event.type) {
			case TouchEvent.TOUCH_DOWN:
				break;
			case TouchEvent.TOUCH_DRAGGED:
				break;
			// update modes when fingers come up
			// this duplication is very important in case the finger was placed
			// down in the world bound but then dragged out of it and picked up
			// outside
			case TouchEvent.TOUCH_UP:
				if (mode == FingerMode.ONE) {
					if (event.pointer == firstPointer)
						mode = FingerMode.NONE;
				} else if (mode == FingerMode.LONG) {
					if (event.pointer == firstPointer)
						mode = FingerMode.NONE;
				}
			}
		}

		// update timer for long press and check if enough time has elapsed
		longPressTimer += deltaTime;
		// apply if the one finger hasn't moved too far away
		if (mode == FingerMode.ONE && (longPressTimer > LONG_PRESS_TIME)
				&& !dragged) {
			mode = FingerMode.LONG;
		}

	}

	private void handleWorldTouch(TouchEvent event) {

		switch (event.type) {
		// first finger becomes drag (pane) second becomes zoom
		case TouchEvent.TOUCH_DOWN:
			if (mode == FingerMode.NONE) {
				firstPointer = event.pointer;
				first.set(touchPoint);
				mode = FingerMode.ONE;
				// remember where finger was initially placed
				firstDown.set(first);
				firstDownWorld.set(event.x, event.y);
				renderer.cam.touchToWorld(firstDownWorld);
				// stuff for long press
				longPressTimer = 0;
				dragged = false;
			} else if (mode == FingerMode.LONG) {
				// during a long press resolution, other fingers are ignored
			}
			break;
		case TouchEvent.TOUCH_DRAGGED:
			if (mode == FingerMode.ONE && event.pointer == firstPointer) {
				first.set(touchPoint);
				// if dragged too far away, cancel potential long press or order
				if (firstDown.distSquared(first) > SCROLL_CUTOFF) {
					dragged = true;
				}
			} else if (mode == FingerMode.LONG) {
				if (event.pointer == firstPointer) {
				}
			}
			break;
		// when fingers go up, update the situation.
		// See notes in the update method for this part
		case TouchEvent.TOUCH_UP:
			if (mode == FingerMode.ONE) {
				if (event.pointer == firstPointer) {
					mode = FingerMode.NONE;
					// move player on finger up and only if not scrolling
					// and the timer is short enough
					if (!dragged) {
						if (longPressTimer < LONG_PRESS_TIME) {
							world.click(worldPoint);
						}
					}
				}
			} else if (mode == FingerMode.LONG) {
				if (event.pointer == firstPointer) {
					mode = FingerMode.NONE;
				}
			}
			break;
		}
	}

	@Override
	public void present(float deltaTime) {
		
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		// draw the world with the world renderer
		renderer.render();

		// draw GUI stuff
		guiCam.setViewportAndMatrices();
        
        // Draw triangle
        //mTriangle.draw(guiCam.getProjection());
        
        //mSquare.draw(guiCam.getProjection());
	}

	public void setup() {
	}

	@Override
	public void onBackPressed() {
		game.defaultBack();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
        // Set the background frame color
        GLES20.glClearColor(1, 1, 1, 1.0f);
        mTriangle.recompile();
        // initialize a square
        // transfer shader to shapeBatcher
        shapeBatcher.setShaderProgram(mTriangle.mProgram);
        mCopy = new CopyOfTriangle();
        batcher.setShaderProgram(mCopy.mProgram);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void reload() {
	}

}
