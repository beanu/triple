package com.zhaoyunhe.triple.scene.widget;

import info.u250.c2d.engine.Engine;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.FloatAction;

public class TimeProgressBar extends Group {
	private FloatAction valueAction;
	private TextureRegion bg;
	private TextureRegion fg;
	private BitmapFont font;
	
	public TimeProgressBar(TextureRegion fg,TextureRegion bg){
		this.fg=fg;
		this.bg=bg;
		this.valueAction=new FloatAction();
		this.setSize(this.bg.getRegionWidth(), this.bg.getRegionHeight());
		
		font=Engine.getDefaultFont();
	}
	
	public void setAction(float begin,float end,float duration){
		this.valueAction.setStart(begin);
		this.valueAction.setEnd(end);
		this.valueAction.setDuration(duration);
		this.addAction(valueAction);
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {		
		float x = this.getX();
		float y = this.getY();
		float width = this.getWidth() * ((valueAction.getValue()) / valueAction.getStart());
		float height = this.getHeight() ;
		batch.draw(bg, x, y, this.getWidth(), height);
		batch.draw(fg, width-this.getWidth(), y, this.getWidth(), height);
		font.draw(batch, (int)Math.ceil(valueAction.getValue())+"", this.getWidth()/2, 30);
	}
	
	
}
