package ercnt.game;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import ercnt.SharedLoader;
import ercnt.loadable.AsyncLoadableTexture;
import ercnt.loadable.ILoadableTexture;

public class Game {
	
	private final String title;
	private final int width, height;
	
	private SharedLoader loader;
	private ILoadableTexture texture;
	
	public Game(String title, int width, int height) throws LWJGLException {
		this.title = title;
		this.width = width;
		this.height = height;
	}
	
	private void initDisplay() throws LWJGLException {
		Display.setTitle(this.title);
		Display.setDisplayMode(new DisplayMode(this.width, this.height));
		Display.create();
	}
	
	public void initLoader() throws LWJGLException {
		this.loader = new SharedLoader(this);
	}
	
	public void loadTextures() {
		this.texture = new AsyncLoadableTexture("texture.jpg");
		this.texture.loadAsync(this.loader);
	}
	
	public void start() {
		try {
			
			this.initDisplay();
			this.initLoader();
			this.loadTextures();
			
			GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
			
			while (!Display.isCloseRequested()) {
				GL11.glClear(256);
				GL11.glMatrixMode(5889);
				GL11.glLoadIdentity();
				GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1000D, 3000D);
				GL11.glMatrixMode(5888);
				GL11.glLoadIdentity();
				GL11.glTranslatef(0, 0, -2000.0F);
				
				this.draw();
			}
			
			Display.destroy();
			
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public void draw() {
		if (this.texture.isLoaded()) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.texture.getTextureId());
			
			GL11.glBegin(GL11.GL_QUADS);
			
            GL11.glVertex2f(0, 0);
            GL11.glTexCoord2f(0, 0);
            
            GL11.glVertex2f(0, 500);
            GL11.glTexCoord2f(0, 1);
            
            GL11.glVertex2f(500, 500);
            GL11.glTexCoord2f(1, 1);

            GL11.glVertex2f(500, 0);
            GL11.glTexCoord2f(1, 0);
            
            GL11.glEnd();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
		
		Display.update();
	}

}
