package com.zhaoyunhe.triple;

import info.u250.c2d.engine.Engine;
import info.u250.c2d.engine.EngineDrive;
import info.u250.c2d.engine.resources.AliasResourceManager;

import com.zhaoyunhe.triple.scene.ScenePlay;

public class TripleDrive implements EngineDrive {

	private ScenePlay mScenePlay;
	
	@Override
	public EngineOptions onSetupEngine() {
		final EngineOptions opt = new EngineOptions(new String[] { "data" },480, 800);
		opt.useGL20 = false;
		opt.autoResume = true;
		return opt;
	}

	@Override
	public void onLoadedResourcesCompleted() {
		mScenePlay=new ScenePlay();
		Engine.setMainScene(mScenePlay);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResourcesRegister(AliasResourceManager<String> reg) {
		reg.textureAtlas("atlas", "data/t.atlas");
		reg.font("gameFont", "data/kingthings.fnt");
		
		reg.sound("cleanup0", "data/sound/cleanup0.mp3");
		reg.sound("cleanup1", "data/sound/cleanup1.mp3");
		reg.sound("cleanup2", "data/sound/cleanup2.mp3");
		reg.sound("cleanup3", "data/sound/cleanup3.mp3");
		reg.sound("cleanup4", "data/sound/cleanup4.mp3");
		reg.sound("cleanup5", "data/sound/cleanup5.mp3");
		reg.sound("cleanup6", "data/sound/cleanup6.mp3");
		reg.sound("cleanup7", "data/sound/cleanup7.mp3");
		reg.sound("cleanup8", "data/sound/cleanup8.mp3");
		reg.sound("cleanup9", "data/sound/cleanup9.mp3");
		reg.sound("cleanup10", "data/sound/cleanup10.mp3");
		reg.sound("cleanup11", "data/sound/cleanup11.mp3");
		reg.sound("drop", "data/sound/drop.mp3");
		reg.sound("speedMode", "data/sound/speedMode.mp3");
		reg.sound("boom", "data/sound/missile-explosion.wav");
		
		reg.music("countdown", "data/music/countdown.mp3");
	}

}
