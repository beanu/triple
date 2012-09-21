package com.zhaoyunhe.triple.scene;

import java.util.ArrayList;

import com.zhaoyunhe.triple.scene.jewel.Jewel;
import com.zhaoyunhe.triple.scene.jewel.JewelType;

public interface IElimination {

	/**
	 * 消除成功
	 * @param list
	 * @param jTouch
	 */
	public void eliminationOk(ArrayList<Jewel> list);

	/**
	 * 消除失败
	 */
	public void eliminationFail();
	
	
	public void eliminationOkForSkill(ArrayList<Jewel> list);
	
	
	/**
	 * 检查每个属性的分数是否达到特定的值，并触发技能
	 * @return
	 */
	public JewelType checkScoreIfFireSkill();
	
	public void showSkillName(JewelType type);
	
	/**
	 *  小炸弹技能的动画
	 * @param jTouch
	 */
	public void effect_boom(Jewel jTouch);
	
	/**
	 * 整加时间的技能动画
	 */
	public void effect_addTime();
	
	/**
	 * 消除一行
	 * @param jTouch
	 */
	public void effect_eliminate_row(Jewel jTouch);
	
	/**
	 * 消除一列
	 * @param jTouch
	 */
	public void effect_eliminate_col(Jewel jTouch);
}
