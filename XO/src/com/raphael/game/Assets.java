package com.raphael.game;

import com.raphael.framework.gl.Texture;
import com.raphael.framework.gl.TextureRegion;
import com.raphael.framework.impl.GLGame;

public class Assets {

	public static Texture test;
	public static TextureRegion region;
	
	public static void load(GLGame game) {
		test = new Texture(game, "animals.png");
		region = new TextureRegion(test, 0, 0, 128, 90);
	}

	public static void reload() {
		test.reload();
	}

}
