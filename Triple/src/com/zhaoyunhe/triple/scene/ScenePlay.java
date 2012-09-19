package com.zhaoyunhe.triple.scene;

import info.u250.c2d.engine.Engine;
import info.u250.c2d.engine.Scene;
import info.u250.c2d.graphic.C2dStage;

import java.util.ArrayList;

import com.badlogic.gdx.InputProcessor;
import com.zhaoyunhe.triple.scene.jewel.Jewel;
import com.zhaoyunhe.triple.scene.jewel.JewelType;

public class ScenePlay extends C2dStage implements Scene,IElimination {

	//UI
	private ScoreBoard scoreBoard;
	private EliminationGroup puzzle;
	private BottomBoard bottomBoard;
	
	//Data
	private int eCount=-1;
	private boolean speedMode=false;
	
	public ScenePlay(){
		bottomBoard=new BottomBoard();
		puzzle=new EliminationGroup(this);
		puzzle.setPosition(2, bottomBoard.getHeight());
		scoreBoard=new ScoreBoard();
		scoreBoard.setPosition(0, puzzle.getY()+puzzle.getHeight());
		
		this.addActor(bottomBoard);
		this.addActor(puzzle);
		this.addActor(scoreBoard);
	}
	
	@Override
	public void update(float delta) {
		this.act();
	}

	@Override
	public void render(float delta) {
		this.draw();
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
	
}
