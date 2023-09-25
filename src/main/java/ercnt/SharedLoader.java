package ercnt;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.Drawable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.SharedDrawable;

import ercnt.game.Game;
import ercnt.loadable.ILoadableTexture;

public class SharedLoader {

	private final IntBuffer dataBuffer = ByteBuffer.allocateDirect(4194304).order(ByteOrder.nativeOrder()).asIntBuffer();
	
    private final ConcurrentLinkedQueue<ILoadableTexture> queue;
    
    private final Drawable 			drawable;
    private final Game 				game;
    
	public SharedLoader(Game game) throws LWJGLException {
		this.game = game;
		this.queue = new ConcurrentLinkedQueue<>();
		this.drawable = new SharedDrawable(Display.getDrawable());

	    Executors.newSingleThreadExecutor().execute(() -> {
			try {
				
				if (!this.drawable.isCurrent()) this.drawable.makeCurrent();

				while (true) {
					Thread.sleep(50L);
					
					ILoadableTexture loadable = null;
					
					while ((loadable = this.queue.poll()) != null) {
						try {
							this.loadAsync(loadable);
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}
				
			} catch (Throwable ex) {
				ex.printStackTrace();
			}
	    });
	}
	
	public Game getGame() {
		return game;
	}

	public void addQueue(ILoadableTexture loadable) {
		this.queue.add(loadable);
	}
	
	public void loadAsync(ILoadableTexture loadable) {
		this.queue.remove(loadable.loadAsync(this));
	}

	public void clear() {
		this.queue.clear();
	}
	
	public void loadTexture(int texture, BufferedImage image) {
		synchronized (this) {
			final int width  = image.getWidth();
			final int height = image.getHeight();
			
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, 0);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MIN_LOD, 0.0F);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LOD, (float) 0);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0.0F);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, ((IntBuffer) null));
			
			int k = this.dataBuffer.capacity() / width;
	        int[] pixels = new int[k * width];

	        image.getRGB(0, 0, width, height, pixels, 0, width);

			this.dataBuffer.clear();
			this.dataBuffer.put(pixels, 0, width * height);
			this.dataBuffer.position(0).limit(width * height);
			
			GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, width, height, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, this.dataBuffer);
		}
	}

}