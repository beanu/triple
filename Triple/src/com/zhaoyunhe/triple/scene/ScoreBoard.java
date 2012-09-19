package com.zhaoyunhe.triple.scene;

import info.u250.c2d.engine.Engine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.FloatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.zhaoyunhe.triple.Triple;
import com.zhaoyunhe.triple.scene.jewel.JewelType;

public class ScoreBoard extends Group {
	private Image bg;
	private JumpLabel[] score;
	
	public ScoreBoard(){
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
		
		this.setSize(Engine.getEngineConfig().width, 100);
	}
	
	public void addScore(JewelType type,int count){
		int value=0;
		JumpLabel label=null;
		switch (type) {
		case shui:
			label=score[0];
			break;
		case huo:
			label=score[1];
			break;
		case mu:
			label=score[2];
			break;
		case guang:
			label=score[3];
			break;
		case an:
			label=score[4];
			break;
		default:
			break;
		}
		value=Integer.valueOf(label.getText().toString());
		label.jump(value, value+count, 0.4f);
	}
	
	class JumpLabel extends Label{
		
		private FloatAction floatAction;
		
		public JumpLabel(CharSequence text,LabelStyle style){
			super(text, style);
			floatAction=new FloatAction();
		}
		
		public void jump(float start,float end,float duration){
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
		
	}
}
