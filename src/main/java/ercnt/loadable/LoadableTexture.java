package ercnt.loadable;

import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import ercnt.SharedLoader;

public class LoadableTexture implements ILoadableTexture {
	
	protected String  fileName;
	protected int     loadState;
	protected int     textureId;

	public LoadableTexture(String fileName) {
		this.fileName = fileName;
	}
	
	@Override
	public void load(SharedLoader loader) {
		this.textureId = GL11.glGenTextures();
		this.loadState = 1;
		
        try (InputStream is = ClassLoader.getSystemResourceAsStream(this.fileName)) {
        	
			try {
				loader.loadTexture(this.textureId, ImageIO.read(is));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
        } catch (IOException e1) {
			e1.printStackTrace();
		}

		this.loadState = 2;
	}
	
	@Override
	public boolean isLoaded() {
		return this.loadState == 2;
	}
	
	public int getLoadState() {
		return this.loadState;
	}
	
	@Override
	public int getTextureId() {
		return this.textureId;
	}

	@Override
	public String getFileName() {
		return this.fileName;
	}

	@Override
	public ILoadableTexture loadAsync(SharedLoader loader) {
		return null;
	}

}
