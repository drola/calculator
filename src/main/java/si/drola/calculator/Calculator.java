package si.drola.calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Calculator {
	public static void main(String[] args) {
		try {
			System.out.print(" > ");
			BufferedReader bufferRead = new BufferedReader(
					new InputStreamReader(System.in));
			String s = bufferRead.readLine();
			
			Lexer l = new Lexer(s);
			Parser p = new Parser(l);
			p.parse();
			
			System.out.println(" >> " + p.getResult().toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
