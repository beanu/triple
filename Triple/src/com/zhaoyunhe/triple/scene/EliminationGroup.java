package com.zhaoyunhe.triple.scene;

import info.u250.c2d.engine.Engine;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.zhaoyunhe.triple.Triple;
import com.zhaoyunhe.triple.scene.jewel.Jewel;
import com.zhaoyunhe.triple.scene.jewel.JewelConfig;
import com.zhaoyunhe.triple.scene.jewel.JewelType;

/**
 * 点击三个相连的即可消除
 * 
 * @author beanu
 */

public class EliminationGroup extends Group {
	private final TextureAtlas atlas;
	
	private Jewel[][] jewels;
	private ArrayList<Jewel> listTouch;//点击后，被消除的数组
	private Jewel jTouch;// 被选中的宝石
	private ArrayList<ArrayList<Jewel>> allTripleList;//所有可以消除的三联消数组，要保证数组个数不能为零
	private ArrayList<ArrayList<Jewel>> allDoubleList;
	
	private TextureRegionDrawable[] drawable;
	private IElimination iEliminate;
	
	private ClickListener clickListener;

	public EliminationGroup(IElimination ie) {
		jewels = new Jewel[JewelConfig.VERTICAL][JewelConfig.HORIZONTAL];
		atlas = Triple.getAtals();
		listTouch = new ArrayList<Jewel>();
		allTripleList=new ArrayList<ArrayList<Jewel>>();
		allDoubleList=new ArrayList<ArrayList<Jewel>>();
		iEliminate=ie;
		
		drawable=new TextureRegionDrawable[]{
				new TextureRegionDrawable(atlas.findRegion("1")),
				new TextureRegionDrawable(atlas.findRegion("2")),
				new TextureRegionDrawable(atlas.findRegion("3")),
				new TextureRegionDrawable(atlas.findRegion("4")),
				new TextureRegionDrawable(atlas.findRegion("5")),
		};
		
		this.init();
		checkAllTripleList();
		
		clickListener=new ClickListener(){

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if (pointer == 0) {
					// Vector2 co = new Vector2(x,y);
					// screenToStageCoordinates(co);
					System.out.println("touchDown" + x + ":" + y);

					if (y < 700) {
						jTouch = (Jewel) hit(x, y, false);
					}

					if (jTouch != null) {
						jTouch.remove();
						addActor(jTouch);
						jTouch.setScale(1.2f);
						// jewels[jTouch.v][jTouch.h]=null;
						eliminate(jTouch);
					}
				}

				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (pointer == 0) {
					System.out.println("touchUp" + x + ":" + y);
					if (jTouch != null) {
						jTouch.setScale(1f);
						jTouch.refeshPosition();
					}
				}
			}
			
		};
		this.addListener(clickListener);
		this.setSize(Engine.getEngineConfig().width, JewelConfig.SPACE_V*JewelConfig.VERTICAL);
	}

	private void init() {

		for (int i = 0; i < JewelConfig.VERTICAL; i++) {
			for (int j = 0; j < JewelConfig.HORIZONTAL; j++) {
				jewels[i][j] = productJewel();
				jewels[i][j].v = i;
				jewels[i][j].h = j;
				// set position
				jewels[i][j].refeshPosition();
				this.addActor(jewels[i][j]);
				System.out.print(jewels[i][j].type.toString() + " ");
			}
			System.out.println();
		}

	}

	// 随即产生 一个宝石
	private Jewel productJewel() {
		int i = MathUtils.random(1, 5);
		Jewel jewel = null;
		switch (i) {
		case 1:
			jewel = Jewel.$(drawable[0], JewelType.shui);
			break;
		case 2:
			jewel = Jewel.$(drawable[1], JewelType.huo);
			break;
		case 3:
			jewel = Jewel.$(drawable[2], JewelType.mu);
			break;
		case 4:
			jewel = Jewel.$(drawable[3], JewelType.guang);
			break;
		case 5:
			jewel = Jewel.$(drawable[4], JewelType.an);
			break;
		default:
			break;
		}
		return jewel;
	}

	private void jewelTransform(Jewel je,JewelType type){
		switch(type){
		case shui:je.setDrawable(drawable[0]);break;
		case huo:je.setDrawable(drawable[1]);break;
		case mu:je.setDrawable(drawable[2]);break;
		case guang:je.setDrawable(drawable[3]);break;
		case an:je.setDrawable(drawable[4]);break;
		}
		
		je.type=type;
	}

	/**
	 * 根据一个jTouch去判断周围同一类型的，并把结果放到list中
	 * @param jTouch
	 * @param list
	 */
	private void detect(Jewel jTouch,ArrayList<Jewel> list) {
		int v = jTouch.v;
		int h = jTouch.h;
		jTouch.eleminate = true;
		list.add(jTouch);

		// 向上检测
		if (v - 1 >= 0) {
			if (!jewels[v - 1][h].eleminate) {
				if (jewels[v - 1][h].type == jTouch.type) {
					detect(jewels[v - 1][h],list);
				}
			}
		}
		// 向下检测
		if (v + 1 < JewelConfig.VERTICAL) {
			if (!jewels[v + 1][h].eleminate) {
				if (jewels[v + 1][h].type == jTouch.type) {
					detect(jewels[v + 1][h],list);
				}
			}
		}

		// 向左检测
		if (h - 1 >= 0) {
			if (!jewels[v][h - 1].eleminate) {
				if (jewels[v][h - 1].type == jTouch.type) {
					detect(jewels[v][h - 1],list);
				}
			}
		}

		// 向右检测
		if (h + 1 < JewelConfig.HORIZONTAL) {
			if (!jewels[v][h + 1].eleminate) {
				if (jewels[v][h + 1].type == jTouch.type) {
					detect(jewels[v][h + 1],list);
				}
			}
		}

	}

	/**
	 * 执行消除行为，消除掉符合规则的砖石
	 */
	public void eliminate(Jewel jTouch) {
		
		for(ArrayList<Jewel> list:allTripleList){
			if(list.contains(jTouch)){
				listTouch.addAll(list);
			}
		}
		
		// 消除这些相连的
		if (listTouch.size() >= 3) {
			iEliminate.eliminationOk(listTouch);
			this.removeTriple(listTouch);
			// 当最后一组相连的消除后，开始刷新数据，获得新的排列。要确保上一步已经处理完
			this.refresh(listTouch);
			listTouch.clear();
			//重新检测可以消除的数组
			this.checkAllTripleList();

		} else {
			iEliminate.eliminationFail();
			for (Jewel je : listTouch) {
				System.out.println("检测了[" + je.v + "][" + je.h + "]");
			}
			listTouch.clear();
		}

	}

	/**
	 * 去掉三个相连及以上的
	 * 
	 * @param je
	 * @param duration
	 */
	private void removeTriple(ArrayList<Jewel> eliminate) {
		// 去掉每组相连的
		// 每一组消除之后，下一组才接着消除，间隔时间可以设定
		for (Jewel je : eliminate) {
			je.free();
			jewels[je.v][je.h] = null;
		}
	}

	/**
	 * 刷新jewels[][],并填上新的数据 思想是：每一列一列的检查，对待每一个列，去把空值置顶 从每一列的下面往上比较
	 */
	private void refresh(ArrayList<Jewel> eliminate) {
		// 数组下落
		for (int h = 0; h < JewelConfig.HORIZONTAL; h++) {
			for (int v = JewelConfig.VERTICAL - 1; v >= 0; v--) {
				if (jewels[v][h] == null) {
					int tmp = v - 1;
					while (tmp >= 0) {
						if (jewels[tmp][h] != null) {
							jewels[v][h] = jewels[tmp][h];
							jewels[v][h].v = v;
							jewels[v][h].h = h;
							jewels[v][h].refeshPosition();

							jewels[tmp][h] = null;
							break;
						}
						tmp--;
					}
				}
			}
		}

		// 检测位置空的地方，用来生成新的宝石
		// 方法：检测以前消除的数据,遍历一遍把列相同的放到一个list中，然后所有的list放到filling中
		ArrayList<ArrayList<Jewel>> filling = new ArrayList<ArrayList<Jewel>>();
		for (int m = 0; m < JewelConfig.HORIZONTAL; m++) {
			ArrayList<Jewel> list = new ArrayList<Jewel>();
			filling.add(list);
		}
		for (Jewel je : eliminate) {
			filling.get(je.h).add(je);
		}

		// 开始生成新的数据用于填充
		for (int i = 0; i < filling.size(); i++) {
			ArrayList<Jewel> list = filling.get(i);
			for (int j = 0; j < list.size(); j++) {
				Jewel a = productJewel();
				a.v = j - list.size();
				a.h = i;
				a.initPosition();

				a.v = j;
				a.h = i;
				if (jewels[j][i] == null) {
					jewels[j][i] = a;
					jewels[j][i].refeshPosition();
					this.addActor(jewels[j][i]);
				}
			}

		}

	}
	
	private void checkAllTripleList(){
		for(int i=0;i<JewelConfig.VERTICAL;i++){
			for(int j=0;j<JewelConfig.HORIZONTAL;j++){
				jewels[i][j].eleminate=false;
			}
		}
		allTripleList.clear();
		allDoubleList.clear();
		
		for(int i=JewelConfig.VERTICAL-1;i>=0;i--){
			for(int j=0;j<JewelConfig.HORIZONTAL;j++){
				if(!jewels[i][j].eleminate){
					ArrayList<Jewel> ls=new ArrayList<Jewel>();
					this.detect(jewels[i][j], ls);
					if(ls.size()==2){
						allDoubleList.add(ls);
					}
					if(ls.size()>=3){
						allTripleList.add(ls);
					}
				}
			}
		}
		System.out.println("可以消除的队列有"+allTripleList.size()+"个。");
		if(allTripleList.size()<=1){
			for(ArrayList<Jewel> list : allDoubleList){
				if(doubleToTriple(list)){
					System.out.println("改变了一个");
					checkAllTripleList();
					break;
				}
			}
		}
		
	}
	
	private boolean doubleToTriple(ArrayList<Jewel> list){
		for(Jewel je:list){
			int v = je.v;
			int h = je.h;
			
			// 向上检测
			if (v - 1 >= 0) {
				if (jewels[v - 1][h].type != je.type && !isInTriple(jewels[v - 1][h])) {
					jewelTransform(jewels[v - 1][h],je.type);
					return true;
				}
			}
			
			// 向下检测
			if (v + 1 < JewelConfig.VERTICAL) {
				if (jewels[v + 1][h].type != je.type && !isInTriple(jewels[v + 1][h])) {
					jewelTransform(jewels[v + 1][h],je.type);
					return true;
				}
			}

			// 向左检测
			if (h - 1 >= 0) {
				if (jewels[v][h - 1].type != je.type && !isInTriple(jewels[v][h - 1])) {
					jewelTransform(jewels[v][h - 1],je.type);
					return true;
				}
			}

			// 向右检测
			if (h + 1 < JewelConfig.HORIZONTAL) {
				if (jewels[v][h + 1].type != je.type && !isInTriple(jewels[v][h + 1])) {
					jewelTransform(jewels[v][h + 1],je.type);
					return true;
				}
			}
			
		}
		return false;
	}

	/**
	 * 判断当前Jewel是否已经在三联消的队列里，用于死锁的检测
	 * @param je
	 * @return
	 */
	private boolean isInTriple(Jewel je){
		for(ArrayList<Jewel> list:allTripleList){
			if(list.contains(je)){
				return true;
			}
		}
		return false;
	}

	public void toStrings() {
		System.out.println("|||||||||||||||||||||||||||||||");
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				if (jewels[i][j] == null) {
					System.out.print("*** ");
				} else {
					System.out.print(jewels[i][j].type.toString() + " ");
				}

			}
			System.out.println();
		}
	}

}
