package com.zhaoyunhe.triple.scene;

import info.u250.c2d.engine.Engine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.FloatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.zhaoyunhe.triple.Triple;
import com.zhaoyunhe.triple.scene.jewel.JewelType;

public class ScoreBoard extends Stage {
	private Image bg;
	private JumpLabel[] score;
	private MenuGroup menu;
	private IGameControl igameControl;
	private IElimination ielimination;
	
	//
	private static final int[] SKILLCOUNT=new int[]{12,9,12,15,15};
	
	public ScoreBoard(ScenePlay scene){
		this.igameControl=scene;
		this.ielimination=scene;
		
		TextureAtlas atlas=Triple.getAtals();
		bg=new Image(atlas.findRegion("screen"));
		this.addActor(bg);
		
		LabelStyle _style=new LabelStyle(Engine.getDefaultFont(), new Color(1, 1, 0, 1));
		score=new JumpLabel[JewelType.values().length];
		for(int i=0;i<JewelType.values().length;i++){
			score[i]=new JumpLabel("0", _style);
			score[i].setPosition(i*60+25, 20);
			this.addActor(score[i]);
		}
		
		menu=new MenuGroup();
		menu.setPosition((this.getWidth()-menu.getWidth())/2, 100);
		
		this.addActor(menu);
		
		this.getRoot().setY(710);
	}
	
	public void addScore(JewelType type,int count){		
		
		for(int i=0;i<JewelType.values().length;i++){
			if(JewelType.values()[i]==type){
				int value=Integer.valueOf(score[i].getText().toString());
				score[i].jump(value, value+count, 0.4f);
				break;
			}
		}
		
		for(int i=0;i<JewelType.values().length;i++){
			if(score[i].compareTo(SKILLCOUNT[i])){
				ielimination.showSkillName(JewelType.values()[i]);
				break;
			}
		}
	}
	
	public JewelType checkScore(){
		//check
		for(int i=0;i<JewelType.values().length;i++){
			if(score[i].compareTo(SKILLCOUNT[i])){
				score[i].clearActions();
				score[i].floatAction.setValue(score[i].floatAction.getEnd()-SKILLCOUNT[i]);
				score[i].floatAction.setEnd(score[i].floatAction.getEnd()-SKILLCOUNT[i]);
				score[i].setText((int)score[i].floatAction.getEnd()+"");
				
				return JewelType.values()[i];
			};
		}
		return null;
	} 
	
	public void reset(){
		for(int i=0;i<score.length;i++){
			score[i].reset();
		}
	}
	
	public void pauseMode(){
		menu.menu_resume.setVisible(true);
		menu.menu_score.setVisible(false);
	}
	
	public void gameOverMode(){
		menu.menu_resume.setVisible(false);
		menu.menu_score.setVisible(true);
		menu.menu_score.setPosition(0, 170);
	}
	
	//---------------------inner class-------------------------
	class JumpLabel extends Label{
		
		private FloatAction floatAction;
		
		public JumpLabel(CharSequence text,LabelStyle style){
			super(text, style);
			floatAction=new FloatAction();
		}
		
		protected void jump(float start,float end,float duration){
			floatAction.setStart(start);
			floatAction.setEnd(end);
			floatAction.setDuration(duration);
			
			this.addAction(floatAction);
			this.addAction(Actions.sequence(Actions.moveBy(0, 10, duration/2),Actions.moveBy(0, -10, duration/2)));
		}

		@Override
		public void act(float delta) {
			this.setText((int)Math.floor(floatAction.getValue())+"");
			super.act(delta);
		}
		
		protected void reset(){
			floatAction.setValue(0);
			floatAction.setEnd(0);
		}
		
		protected boolean compareTo(int value){
			if(floatAction.getEnd()>=value)return true;
			else return false;
		}
	}
	
	class MenuGroup extends Group{
		private Image menu_exit;
		private Image menu_restart;
		private Image menu_resume;
		private Image menu_score;
		
		public MenuGroup(){
			TextureAtlas atlas=Triple.getAtals();
			menu_exit=new Image(atlas.findRegion("menu-exit"));
			menu_restart=new Image(atlas.findRegion("menu-restart"));
			menu_resume=new Image(atlas.findRegion("menu-resume"));
			menu_score=new Image(atlas.findRegion("menu-score"));
			
			menu_exit.setPosition(0, 10);
			menu_restart.setPosition(0, 90);
			menu_resume.setPosition(0, 170);
			menu_score.setPosition(0, 250);
			
			this.addActor(menu_exit);
			this.addActor(menu_restart);
			this.addActor(menu_resume);
			this.addActor(menu_score);
			
			this.setSize(menu_exit.getWidth(), menu_score.getY()+menu_score.getHeight());
			
			//addListener
			menu_resume.addListener(new ClickListener(){

				@Override
				public void clicked(InputEvent event, float x, float y) {
					igameControl.resumeGame();
				}
				
			});
			
			menu_restart.addListener(new ClickListener(){

				@Override
				public void clicked(InputEvent event, float x, float y) {
					igameControl.restartGame();
				}
				
			});
			
			menu_exit.addListener(new ClickListener(){

				@Override
				public void clicked(InputEvent event, float x, float y) {
					igameControl.exitGame();
				}
				
			});
			
		}
	}
}
