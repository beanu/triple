package com.zhaoyunhe.triple.scene;

import info.u250.c2d.engine.Engine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.zhaoyunhe.triple.Triple;
import com.zhaoyunhe.triple.scene.jewel.JewelType;

public class BottomBoard extends Group {

	private Image bg;
	private Image btn_pause_bg;
	private Image btn_timer_bg;
	
	private Label label_timer;	
	private double _remainingTime;
	
	private Label label_score;
	private Label label_effect_name;
	
	private IGameControl igameControl;;
	private boolean gameOver;
	private boolean pauseMusic;
	
	public BottomBoard(IGameControl igameControl){
		this.igameControl=igameControl;
		
		TextureAtlas atlas=Triple.getAtals();
		bg=new Image(atlas.findRegion("bottom"));
		btn_pause_bg=new Image(atlas.findRegion("btn_pause_backgroud"));
		btn_timer_bg=new Image(atlas.findRegion("btn_timer_backgroud"));
		
		btn_pause_bg.setPosition(35, 15);
		btn_timer_bg.setPosition(370, 15);
		
		label_timer=new Label("60", new LabelStyle(Engine.resource("gameFont",BitmapFont.class), new Color(1, 1, 0, 1)));
		label_timer.setPosition(btn_timer_bg.getX()+20, btn_timer_bg.getY()+20);
		
		label_score=new Label("0", new LabelStyle(Engine.resource("gameFont",BitmapFont.class), new Color(0, 1, 0, 1)));
		label_score.setPosition(200, 20);
		label_effect_name=new Label("", new LabelStyle(Engine.resource("gameFont",BitmapFont.class), new Color(1, 1, 0, 1)));
		label_effect_name.setPosition(200, 70); 
		
		btn_pause_bg.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				pauseMusic=true;
				Engine.getMusicManager().pauseMusic("countdown");
				BottomBoard.this.igameControl.pauseGame();
			}
			
		});
		
		this.addActor(bg);
		this.addActor(btn_pause_bg);
		this.addActor(btn_timer_bg);
		this.addActor(label_timer);
		this.addActor(label_score);
		this.addActor(label_effect_name);
		
		this.setSize(Engine.getEngineConfig().width, bg.getHeight());
		reset();
	}
	
	@Override
	public void act(float delta) {
		// Game time
		if(!gameOver){
			if (_remainingTime > 0) {
				label_timer.setText(""+(int)_remainingTime);
				if((int)_remainingTime<=10 && !pauseMusic){
					Engine.getMusicManager().playMusic("countdown", false);
				}
				if((int)_remainingTime>10 && !pauseMusic){
					Engine.getMusicManager().stopMusic("countdown");
				}
			}else{
				gameOver=true;
				igameControl.endGame();
			}
			_remainingTime -= delta;
		}
	
		super.act(delta);
	}


	public void reset(){
		Engine.getMusicManager().stopMusic("countdown");
		_remainingTime=60;
		gameOver=false;
		pauseMusic=false;
		updateScore(0);
		showEffectName(null);
	}
	
	public void resume(){
		pauseMusic=false;
	}
	
	public void updateScore(int value){
		label_score.setText(value+"");
	}
	
	public void showEffectName(JewelType type){
		
		if(type!=null){
			label_effect_name.setText(type+"");
		}else{
			label_effect_name.setText("");
		}
	}
	
	public void addTime(int value){
		_remainingTime+=value;
	}
}