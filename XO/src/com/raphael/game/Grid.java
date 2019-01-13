package com.raphael.game;

public class Grid {

	Tile[] grid;

	public Grid() {

		grid = new Tile[9];
		int counter = 0;
		for (int y = -1; y < 2; y++) {
			for (int x = -1; x < 2; x++) {
				grid[counter] = new Tile(Type.CLEAR, x, y);
				counter++;
			}
		}

	}

	public void reset() {
		for (Tile tile : grid)
			tile.type = Type.CLEAR;
	}
	
	public Tile tile(int x, int y){
		int index = 4+x-3*y;
		return grid[index];
	}

}
