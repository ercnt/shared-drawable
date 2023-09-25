package ercnt.loadable;

import ercnt.SharedLoader;

public class AsyncLoadableTexture extends LoadableTexture {

	public AsyncLoadableTexture(String fileName) {
		super(fileName);
	}
	
	@Override
	public AsyncLoadableTexture loadAsync(SharedLoader loader) {
		super.load(loader);
		return this;
	}

}
