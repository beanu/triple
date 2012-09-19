package com.zhaoyunhe.triple.tools;

import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

public class Packer {
	public static void main(String args[]) throws Exception {
		Settings settings = new Settings();
		settings.alias = true;
		settings.stripWhitespaceX = settings.stripWhitespaceY = false;
		settings.rotation = false;
		settings.edgePadding=false;
		settings.maxWidth = 1024;
		settings.maxHeight = 1024;

		TexturePacker2.process(settings,
				"raw/",
				"../Triple-android/assets/data","t");

	}
}
