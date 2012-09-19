package com.zhaoyunhe.triple.scene.jewel;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Pool;

public class Jewel extends Image {
	public int v;
	public int h;
	public JewelType type;
	public boolean eleminate;//新版本消除用到的变量，是否已经检测的标记
	
	private Jewel(){
		super();
	}

	//Pool
	private static Pool<Jewel> pool=new Pool<Jewel>(){

		@Override
		protected Jewel newObject() {
			return new Jewel();
		}
		
	};
	
	public void free(){
		this.remove();
		pool.free(this);
	}
	
	public static Jewel $(TextureRegionDrawable drawable,JewelType type){
		Jewel je=pool.obtain();
		je.setDrawable(drawable);
		je.setSize(je.getPrefWidth(), je.getPrefHeight());
		je.type=type;
		je.v=0;
		je.h=0;
		je.eleminate=false;
		je.setOrigin(je.getWidth()/2, je.getHeight()/2);
		return je;
	}
	//Pool end
	
	/**
	 * 根据下标设定图片的位置带有动画的过程
	 */
	public void refeshPosition(){
		this.addAction(Actions.moveTo(_getX(), _getY(), 0.2f));
	}

	/**
	 * 初始化位置
	 */
	public void initPosition(){
		this.setX(_getX());
		this.setY(_getY());
	}
	
//	/**
//	 * 交换时的动画
//	 */
//	
//	public void exchangePosition(){
//		this.addAction(Actions.moveTo(_getX(), _getY(), 0.06f));
//	}

	private float _getX(){
		return h*JewelConfig.SPACE_H;
	}
	
	private float _getY(){
//		return (JewelConfig.VERTICAL-v)*JewelConfig.SPACE_V;
		return (JewelConfig.VERTICAL-v-1)*JewelConfig.SPACE_V;
	}
}
