package com.cc.lc.interpreter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


public class Main {
	public static void main(String[] args) {
		Random generator = new Random(178341921);
		StringBuilder builder = new StringBuilder("(thread 'com.cc.lc.interpreter.test '"),
				builder2 = new StringBuilder("(+ ");
		boolean plus = false;
		int sum = 0;
		for(int i = 0; i < 1000; i++) {
			int a = Math.abs(generator.nextInt()) % 100,
				b = Math.abs(generator.nextInt()),
				term = 0;
			builder2.append('(');
			switch(generator.nextInt() % 4) {
				case 0:
					builder2.append('+'); term = a + b; break;
				case 1:
					builder2.append('-'); term = a - b; break;
				case 2:
					builder2.append('*'); term = a * b; break;
				default:
					builder2.append("/"); term = a / b; break;
			}
			builder2.append(' ');
			builder2.append(a);
			builder2.append(' ');
			builder2.append(b);
			builder2.append(") ");
			sum += term;
		}
		builder2.append(")");
		builder.append(builder2);
		builder.append(" 10)");
		System.out.println(builder.toString());
		System.out.println(builder2.toString());
		try {
			FileWriter fw = new FileWriter("t13.correct");
			fw.write(builder.toString());
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sum);
	}
}
