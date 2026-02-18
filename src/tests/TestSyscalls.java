package tests;

import java.io.IOException;

import stuffs.OSValidator;

public class TestSyscalls {

	public static void main(String[] args) {
		Runtime r;
		r = Runtime.getRuntime();
		System.out.println(Runtime.version());
		try {
			if (OSValidator.isWindows()) {
				r.exec("cmd /c start .");
				r.exec("cmd /c mkdir AAAAA");
			} else {
				r.exec("xdg-open .");
				r.exec("mkdir AAAAA");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}