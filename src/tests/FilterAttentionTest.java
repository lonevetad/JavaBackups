package tests;

import java.awt.Color;

import tools.StreamPixel;
import tools.impl.MonoColoredRectangle;

public class FilterAttentionTest {
	StreamPixel sp;

	public void build() {
		sp = new MonoColoredRectangle(200, 150, Color.BLUE);

	}

	public static void main(String[] args) {
		FilterAttentionTest t;
		t = new FilterAttentionTest();
		t.build();
	}
}