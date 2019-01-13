package com.raphael.framework.impl;

import com.raphael.framework.Game;
import com.raphael.framework.Screen;

public abstract class GLScreen extends Screen {
    protected final GLGame glGame;
    
    public GLScreen(Game game) {
        super(game);
        glGame = (GLGame)game;
    }

}
