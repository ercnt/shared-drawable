package ercnt.loadable;

import ercnt.SharedLoader;

public interface ILoadableTexture {

	public String getFileName();
	
	public int getTextureId();
	
	public void load(SharedLoader loader);
	
	public ILoadableTexture loadAsync(SharedLoader loader);
	
	public boolean isLoaded();

}
