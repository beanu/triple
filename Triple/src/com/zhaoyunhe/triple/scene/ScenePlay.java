package com.zhaoyunhe.triple.scene;

import info.u250.c2d.engine.Engine;
import info.u250.c2d.engine.Scene;
import info.u250.c2d.graphic.C2dStage;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.zhaoyunhe.triple.scene.jewel.Jewel;
import com.zhaoyunhe.triple.scene.jewel.JewelType;

public class ScenePlay extends C2dStage implements Scene,IElimination,IGameControl {

	//UI
	private ScoreBoard scoreBoard;//stage
	private EliminationGroup puzzle;//group
	private BottomBoard bottomBoard;//group
	
	//Data
	private int eCount=-1;
	private boolean speedMode=false;
	private int score=0;

	public ScenePlay(){
		bottomBoard=new BottomBoard(this);
		puzzle=new EliminationGroup(this);
		puzzle.setPosition(2, bottomBoard.getHeight());
		this.addActor(bottomBoard);
		this.addActor(puzzle);
		
		scoreBoard=new ScoreBoard(this);
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
		scoreBoard.addScore(type, list.size());
		score+=list.size()*10;
		bottomBoard.updateScore(score);
		
		if(speedMode){
			System.out.println("speed");
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
	public void pauseGame() {
		Gdx.input.setInputProcessor(null);
		puzzle.getColor().a=0.5f;
		bottomBoard.getColor().a=0.5f;
		
		scoreBoard.pauseMode();
		scoreBoard.addAction(Actions.sequence(Actions.moveBy(0, -480, 0.3f),Actions.run(new Runnable() {
			
			@Override
			public void run() {
				Engine.get().pause();
				Gdx.input.setInputProcessor(scoreBoard);
			}
		})));

	}

	@Override
	public void resumeGame() {
		Engine.doResume();
		Engine.get().resume();
		scoreBoard.addAction(Actions.sequence(Actions.moveBy(0, 480, 0.3f),Actions.run(new Runnable() {
			
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
		scoreBoard.addAction(Actions.sequence(Actions.moveBy(0, -480, 0.3f),Actions.run(new Runnable() {
			
			@Override
			public void run() {
				Engine.get().pause();
				Gdx.input.setInputProcessor(scoreBoard);
			}
		})));
	}

	@Override
	public void restartGame() {
		Engine.doResume();
		Engine.get().resume();
		scoreBoard.addAction(Actions.sequence(Actions.moveBy(0, 480, 0.3f),Actions.run(new Runnable() {
			
			@Override
			public void run() {
				puzzle.getColor().a=1f;
				bottomBoard.getColor().a=1f;
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
	
}
