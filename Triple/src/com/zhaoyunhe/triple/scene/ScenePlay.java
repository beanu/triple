package com.zhaoyunhe.triple.scene;

import info.u250.c2d.accessors.SpriteAccessor;
import info.u250.c2d.engine.Engine;
import info.u250.c2d.engine.Scene;
import info.u250.c2d.graphic.AnimationSprite;
import info.u250.c2d.graphic.AnimationSprite.AnimationSpriteListener;
import info.u250.c2d.graphic.C2dStage;

import java.util.ArrayList;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.zhaoyunhe.triple.Triple;
import com.zhaoyunhe.triple.scene.jewel.Jewel;
import com.zhaoyunhe.triple.scene.jewel.JewelType;
import com.zhaoyunhe.triple.scene.widget.FloatingScore;

public class ScenePlay extends C2dStage implements Scene,IElimination,IGameControl {

	//UI
	private ScoreBoard scoreBoard;//stage
	private EliminationGroup puzzle;//group
	private BottomBoard bottomBoard;//group
	private FloatingScore addTime;
	
	//Data
	private int eCount=-1;
	private boolean speedMode=false;
	private int score=0;
	
	//Static Data
	private static final float SCOREBOARD_ANIMATION_TIME=0.5f;
	
	//Particle
	private ParticleEmitter particleEmitter;
	
	//Sprite
	private AnimationSprite spriteBoom;
	private AnimationSprite spriteMouse;
	
	public ScenePlay(){
		TextureAtlas atlas=Triple.getAtals();
		
		bottomBoard=new BottomBoard(this);
		puzzle=new EliminationGroup(this);
		puzzle.setPosition(2, bottomBoard.getHeight());
		this.addActor(bottomBoard);
		this.addActor(puzzle);
		
		scoreBoard=new ScoreBoard(this);
		
		ParticleEffect particleEffect= new ParticleEffect();
		particleEffect.load(Gdx.files.internal("data/a.p"), Gdx.files.internal("data/"));
		particleEmitter = particleEffect.findEmitter("huo");
		particleEmitter.setPosition(0, puzzle.getY());
		
		spriteBoom = new AnimationSprite(0.08f, atlas,"booming");
		spriteBoom.setVisible(false);
		spriteMouse=new AnimationSprite(0.08f, atlas, "mouse");
		spriteMouse.setVisible(false);
		
		addTime=new FloatingScore(Engine.resource("gameFont",BitmapFont.class));
	}
	
	@Override
	public void update(float delta) {
		this.act();
		scoreBoard.act();
	}

	@Override
	public void render(float delta) {
		this.draw();
		scoreBoard.draw();
		
		Engine.getSpriteBatch().begin();
		particleEmitter.draw(Engine.getSpriteBatch(), delta);
		spriteBoom.render(delta);
		spriteMouse.render(delta);
		addTime.draw();
		Engine.getSpriteBatch().end();
	}

	@Override
	public void show() {
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
	}

	@Override
	public InputProcessor getInputProcessor() {
		return this;
	}

	@Override
	public void eliminationOk(ArrayList<Jewel> list) {
		
		JewelType type=list.get(0).type;
		int count=list.size();
		scoreBoard.addScore(type,count);
		
		score+=list.size()*10;
		bottomBoard.updateScore(score);
		
		if(speedMode){
			System.out.println("speed");
			if(particleEmitter.isComplete()){
				System.out.println("particle start");
				particleEmitter.start();
			}
			Engine.getSoundManager().playSound("speedMode");
			eCount++;
			if(eCount>=4){
				speedMode=false;
			}
		}else{
			eCount++;
			int soundId=eCount%12;
			if(soundId==0 && eCount>=12){
				speedMode=true;
				eCount=-1;
			}
			Engine.getSoundManager().playSound("cleanup"+soundId);
		}
		Engine.getSoundManager().playSound("drop");
	}

	@Override
	public void eliminationFail() {
		eCount=-1;
		speedMode=false;
	}
	
	@Override
	public void eliminationOkForSkill(ArrayList<Jewel> list) {
		for(int i=0;i<JewelType.values().length;i++){
			JewelType type=JewelType.values()[i];
			ArrayList<Jewel> _ls=new ArrayList<Jewel>();
			for(Jewel jewel:list){
				if(jewel.type==type){
					_ls.add(jewel);
				}
			}
			if(_ls.size()>0){
				scoreBoard.addScore(type,_ls.size());
				score+=list.size()*10;
				bottomBoard.updateScore(score);
			}
		}
		
		//music
		if(speedMode){
			System.out.println("speed");
			if(particleEmitter.isComplete()){
				System.out.println("particle start");
				particleEmitter.start();
			}
			Engine.getSoundManager().playSound("speedMode");
			eCount++;
			if(eCount>=4){
				speedMode=false;
			}
		}else{
			eCount++;
			int soundId=eCount%12;
			if(soundId==0 && eCount>=12){
				speedMode=true;
				eCount=-1;
			}
			Engine.getSoundManager().playSound("cleanup"+soundId);
		}
		Engine.getSoundManager().playSound("drop");
		
	}
	
	@Override
	public JewelType checkScoreIfFireSkill() {
		JewelType type=scoreBoard.checkScore();
		return type;
	}
	@Override
	public void showSkillName(JewelType type) {
		bottomBoard.showEffectName(type);
	}

	@Override
	public void effect_boom(Jewel jTouch) {
		Engine.getSoundManager().playSound("boom");
		Vector2 _v=new Vector2();
		jTouch.localToStageCoordinates(_v);
		spriteBoom.setPosition(_v.x, _v.y);
		spriteBoom.setVisible(true);
		spriteBoom.replay();
		spriteBoom.setAnimationSpriteListener(new AnimationSpriteListener() {
			
			@Override
			public void onLastFrame() {
				spriteBoom.stop();
				spriteBoom.setVisible(false);
			}
		});
		bottomBoard.showEffectName(null);
	}

	@Override
	public void effect_addTime() {
		bottomBoard.addTime(10);
		addTime.setPosition(400, 50);
		addTime.setValue(10);
		addTime.start();
		bottomBoard.showEffectName(null);
	}
	
	@Override
	public void effect_eliminate_row(Jewel jTouch) {
		Vector2 _v=new Vector2();
		jTouch.localToStageCoordinates(_v);
		spriteMouse.setPosition(0, _v.y);
		spriteMouse.setVisible(true);
		spriteMouse.replay();
		
		Tween.to(spriteMouse, SpriteAccessor.POSITION_XY, 1000f)
				.target(Engine.getEngineConfig().width, _v.y)
				.setCallback(new TweenCallback() {
					
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						if(type==COMPLETE){
							spriteMouse.stop();
							spriteMouse.setVisible(false);
						}
					}
				})
				.start(Engine.getTweenManager());
		bottomBoard.showEffectName(null);
	}

	@Override
	public void effect_eliminate_col(Jewel jTouch) {
		Vector2 _v=new Vector2();
		jTouch.localToStageCoordinates(_v);
		spriteMouse.setPosition(_v.x, puzzle.getY());
		spriteMouse.setVisible(true);
		spriteMouse.replay();
		
		Tween.to(spriteMouse, SpriteAccessor.POSITION_XY, 1000f)
				.target(_v.x, puzzle.getY()+puzzle.getHeight())
				.setCallback(new TweenCallback() {
					
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						if(type==COMPLETE){
							spriteMouse.stop();
							spriteMouse.setVisible(false);
						}
					}
				})
				.start(Engine.getTweenManager());
		bottomBoard.showEffectName(null);
	}

	@Override
	public void pauseGame() {
		Gdx.input.setInputProcessor(null);
		puzzle.getColor().a=0.5f;
		bottomBoard.getColor().a=0.5f;
		
		scoreBoard.pauseMode();
		scoreBoard.addAction(Actions.sequence(Actions.moveBy(0, -480, SCOREBOARD_ANIMATION_TIME),Actions.run(new Runnable() {
			
			@Override
			public void run() {
				Engine.get().pause();
				Gdx.input.setInputProcessor(scoreBoard);
			}
		})));

	}

	@Override
	public void resumeGame() {
		Engine.get().resume();
		bottomBoard.resume();
		scoreBoard.addAction(Actions.sequence(Actions.moveBy(0, 480, SCOREBOARD_ANIMATION_TIME),Actions.run(new Runnable() {
			
			@Override
			public void run() {
				puzzle.getColor().a=1f;
				bottomBoard.getColor().a=1f;
				Gdx.input.setInputProcessor(ScenePlay.this.getInputProcessor());
			}
		})));
		
	}

	@Override
	public void endGame() {
		Gdx.input.setInputProcessor(null);
		puzzle.getColor().a=0.5f;
		bottomBoard.getColor().a=0.5f;
		scoreBoard.gameOverMode();
		scoreBoard.addAction(Actions.sequence(Actions.moveBy(0, -480, SCOREBOARD_ANIMATION_TIME),Actions.run(new Runnable() {
			
			@Override
			public void run() {
//				Engine.get().pause();
				Gdx.input.setInputProcessor(scoreBoard);
			}
		})));
	}

	@Override
	public void restartGame() {
		Engine.get().resume();
		scoreBoard.addAction(Actions.sequence(Actions.moveBy(0, 480, SCOREBOARD_ANIMATION_TIME),Actions.run(new Runnable() {
			
			@Override
			public void run() {
				puzzle.getColor().a=1f;
				bottomBoard.getColor().a=1f;
				reset();
				scoreBoard.reset();
				puzzle.reset();
				bottomBoard.reset();
				Gdx.input.setInputProcessor(ScenePlay.this.getInputProcessor());
			}
		})));
	}

	@Override
	public void exitGame() {
		// TODO Auto-generated method stub
		
	}
	
	private void reset(){
		eCount=-1;
		speedMode=false;
		score=0;
	}

}
