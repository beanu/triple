package com.zhaoyunhe.triple.scene;

import info.u250.c2d.engine.Engine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.FloatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.zhaoyunhe.triple.Triple;

public class BottomBoard extends Group {

	private Image bg;
	private Image btn_pause_bg;
	private Image btn_timer_bg;
	
	private Label label_timer;
	private FloatAction floatAction;
	private SequenceAction timerAction;
	
	private Label label_score;
	
	public BottomBoard(final IGameControl igameControl){	
		TextureAtlas atlas=Triple.getAtals();
		bg=new Image(atlas.findRegion("bottom"));
		btn_pause_bg=new Image(atlas.findRegion("btn_pause_backgroud"));
		btn_timer_bg=new Image(atlas.findRegion("btn_timer_backgroud"));
		
		btn_pause_bg.setPosition(35, 15);
		btn_timer_bg.setPosition(370, 15);
		
		floatAction=new FloatAction(60, 0);
		floatAction.setDuration(60);
		label_timer=new Label("60", new LabelStyle(Engine.resource("gameFont",BitmapFont.class), new Color(1, 1, 0, 1))){

			@Override
			public void act(float delta) {
				this.setText((int)floatAction.getValue()+"");
				super.act(delta);
			}
			
		};
		label_timer.setPosition(btn_timer_bg.getX()+20, btn_timer_bg.getY()+20);
		timerAction=Actions.sequence(floatAction,Actions.run(new Runnable() {
			
			@Override
			public void run() {
				igameControl.endGame();
			}
		}));
		this.addAction(timerAction);
		
		label_score=new Label("0", new LabelStyle(Engine.resource("gameFont",BitmapFont.class), new Color(0, 1, 0, 1)));
		label_score.setPosition(200, 20);
		
		btn_pause_bg.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				igameControl.pauseGame();
			}
			
		});
		
		this.addActor(bg);
		this.addActor(btn_pause_bg);
		this.addActor(btn_timer_bg);
		this.addActor(label_timer);
		this.addActor(label_score);
		
		this.setSize(Engine.getEngineConfig().width, bg.getHeight());
	}

	public void reset(){
		//TODO this floatAction have error when reset
		floatAction.reset();
		floatAction.setValue(floatAction.getStart());
		floatAction.setDuration(60);
		this.addAction(timerAction);
	}
	
	public void updateScore(int value){
		label_score.setText(value+"");
	}
}