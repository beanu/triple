package com.zhaoyunhe.triple;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import info.u250.c2d.engine.Engine;
import info.u250.c2d.engine.EngineDrive;

public class Triple extends Engine {

	@Override
	protected EngineDrive onSetupEngineDrive() {
		return new TripleDrive();
	}

	public static TextureAtlas getAtals(){
		return Engine.resource("atlas");
	}
}
