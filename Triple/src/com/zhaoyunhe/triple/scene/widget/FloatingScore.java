package com.zhaoyunhe.triple.scene.widget;

import info.u250.c2d.engine.Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class FloatingScore {
	
	private static final float duration = 1f; 
	
	private BitmapFont _font;
	private String _text;
	private Vector2 _pos;
	private float _time;
	private boolean play;
	
	public FloatingScore( BitmapFont font) {
		_font = font;
		_text = new String("0");
		_pos = new Vector2();
		_time = 0.0f;
		play=false;
	}
	
	public void setPosition(float x,float y){
		_pos.set(x, y);
	}
	
	public void setValue(int value){
		_text=value+"";
	}
	
	public void start(){
		_time=0.0f;
		play=true;
	}
	
	public void draw() {
		if(play){
			SpriteBatch batch = Engine.getSpriteBatch();
			
			_time += Gdx.graphics.getDeltaTime();
			
			float p = 1.0f - _time/duration;
			Vector2 currentPos = new Vector2(_pos.x - 12, _pos.y + (1.0f - p) * 30);
			Color oldFontColor = _font.getColor();
			
			_font.setColor(new Color(0.0f, 0.0f, 0.0f, p));
			_font.draw(batch, _text, (int)currentPos.x - 2, (int)currentPos.y - 2);
			_font.draw(batch, _text, (int)currentPos.x + 2, (int)currentPos.y + 2);
			_font.setColor(new Color(1.0f, 1.0f, 1.0f, p));
			_font.draw(batch, _text, (int)currentPos.x, (int)currentPos.y);
			_font.setColor(oldFontColor);
			
			if(_time >= duration){
				play=false;
			}
		}

	}
}
