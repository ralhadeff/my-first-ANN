package com.raphael.game;

enum Type {CLEAR, X, O}

public class Tile {

	int x,y;
	Type type;
	
	public Tile(Type type, int x, int y){
		this.type = type;
		this.x = x;
		this.y = y;
	}
	
}
