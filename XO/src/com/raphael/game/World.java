package com.raphael.game;

import java.util.Random;

import com.raphael.framework.math.OverlapTester;
import com.raphael.framework.math.Rectangle;
import com.raphael.framework.math.Vector2;

public class World {

	Rectangle startX, startO;
	Rectangle[] rectangles;
	Grid grid;
	Type player, computer;
	static Random random = new Random();
	boolean win;
	boolean lose;
	WeightsReader reader;
	
	ArtificialNetwork network;

	public World() {

		startX = new Rectangle(100, 280, 300, 200);
		startO = new Rectangle(1080 - 100 - 300, 280, 300, 200);
		rectangles = new Rectangle[2];

		rectangles[0] = startX;
		rectangles[1] = startO;

		grid = new Grid();

		// default starting is X
		player = Type.X;
		computer = Type.O;

		win = false;
		lose = false;
		
		network = new ArtificialNetwork(9,45,9);
		reader = new WeightsReader();
	}

	public void click(Vector2 worldPoint) {

		if (OverlapTester.pointInRectangle(rectangles[0], worldPoint))
			startNewGame(Type.X);
		else if (OverlapTester.pointInRectangle(rectangles[1], worldPoint))
			startNewGame(Type.O);
		else if (!win && !lose) {
			Vector2 destination = worldPoint.cpy().sub(540, 1270).mul(1 / 300f);
			int x = Math.round(destination.x);
			int y = Math.round(-destination.y);
			if (Math.abs(x) < 2 && Math.abs(y) < 2) {
				int num = 4 + x + y * 3;
				if (playPlayer(grid, num, player)) {
					grid.grid[num].type = player;
					if (!gameOver())
						playComputer(grid, computer);
					else
						win();
					if (gameOver())
						lose();
				}
			}
		}

	}

	private void win() {
		win = true;
	}

	private void lose() {
		lose = true;
	}

	private boolean gameOver() {
		for (int i = -1; i < 2; i++) {
			// rows
			if (grid.tile(i, -1).type == grid.tile(i, 0).type
					&& grid.tile(i, 0).type == grid.tile(i, 1).type
					&& grid.tile(i, -1).type != Type.CLEAR)
				return true;
			// lines
			if (grid.tile(-1, i).type == grid.tile(0, i).type
					&& grid.tile(0, i).type == grid.tile(1, i).type
					&& grid.tile(-1, i).type != Type.CLEAR)
				return true;

		}
		// diagonals:
		if (grid.tile(-1, -1).type == grid.tile(0, 0).type
				&& grid.tile(0, 0).type == grid.tile(1, 1).type
				&& grid.tile(-1, -1).type != Type.CLEAR)
			return true;
		if (grid.tile(1, -1).type == grid.tile(0, 0).type
				&& grid.tile(0, 0).type == grid.tile(-1, 1).type
				&& grid.tile(1, -1).type != Type.CLEAR)
			return true;
		return false;
	}

	private boolean playPlayer(Grid grid, int tileNumber, Type type) {
		// player attempts a play, return true if legal
		if (grid.grid[tileNumber].type == Type.CLEAR)
			return true;
		return false;
	}

	private void startNewGame(Type type) {
		grid.reset();
		win = false;
		lose = false;
		player = type;
		computer = type == Type.X ? Type.O : Type.X;
		if (player == Type.O)
			playComputerFirst(grid, Type.X);
	}

	private void playComputerFirst(Grid grid, Type type) {
		int index = random.nextInt(9);
		grid.grid[index].type = type;	
	}

	private void playComputer(Grid grid, Type type) {
		// computer inserts data into network and gets an output to play

		// to multiply input for network, +1 or -1
		int mul = (type == Type.X)? 1 : -1;
		
		// notice that input needs to have 0 as a leading dummy bias 
		// also notice that the i of the input is shifted +1 as a consequence of the bias
		float[] input = new float[10];
		input[0]=1;
		for (int i = 0; i<9; i++){
			switch(grid.grid[i].type){
			case X:
			input[i+1] = 1*mul;
			break;
			case O:
				input[i+1]=-1*mul;
				break;
			default:
				input[i+1]=0;
				break;
			}
		}
		float[] output = network.run(input);
		
		float max = 0;
		int index = -1;
		// start from 1 to skip the dummy bias in the output layer
		for (int i =1; i<output.length; i++){
			if (output[i]>max){
				max = output[i];
				index = i;
			}
		}
		// subtract 1 from index because of the bias shifting the numbers +1
		index--;
		grid.grid[index].type = type;
			
		/*
		 * random selector:
		boolean playing = false;
		// go over grid
		for (Tile tile : grid.grid) {
			// if there is any free tile, go to loop
			if (tile.type == Type.CLEAR) {
				playing = true;
				break;
			}
		}
		// randomly pick tile until a free one is selected
		while (playing) {
			int num = random.nextInt(9);
			if (grid.grid[num].type == Type.CLEAR) {
				playing = false;
				grid.grid[num].type = type;
			}
		}
		*/
	}

}
