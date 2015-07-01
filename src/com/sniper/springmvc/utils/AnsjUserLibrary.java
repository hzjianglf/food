package com.sniper.springmvc.utils;

import org.ansj.util.MyStaticValue;

public class AnsjUserLibrary {

	private String userLibrary;

	public void init() {
		// 默认辞典路径
		MyStaticValue.userLibrary = this.userLibrary;

		// 歧异词典的路径
		MyStaticValue.ambiguityLibrary = "";

	}
}
