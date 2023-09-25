package ercnt;

import org.lwjgl.LWJGLException;

import ercnt.game.Game;

public class Main {
	
	private static Game game;
	
	public static void main(String[] args) {
		try {
			
			game = new Game("Shared Test", 1000, 1000);
			game.start();
			
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

}
