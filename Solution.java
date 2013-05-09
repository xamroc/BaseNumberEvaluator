import java.util.Scanner;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

class Solution {

	public static void main(String[] args) {
		
		Evaluator base = new Evaluator();
		String[] arrInput = base.readInput();
		String[] decInput = base.toDecimalInput(arrInput);
		long ans = base.evaluate(decInput);
		
		System.out.println(ans);
	}
}

/**Evaluator class takes the input of the user and validates its format of two values surrounding an 
 * underscore, where the left uses representative values of range 0-9 and A-F, while the right value is the 
 * base system ranging from 2-16. Each expression and operator is stored in a string array which will be 
 * converted to decimal format and evaluated.
 * 
 * @author Marco
 * 
 */
class Evaluator {
	
	private String input;
	private String regexSplit = "(?<=[\\+\\-\\*])|(?=[\\+\\-\\*])";
	private String regexExp = "[0-9A-F]+_(?:2|3|4|5|6|7|8|9|10|11|12|13|14|15|16)";
	private String regexOp = "[\\+\\-\\*]";
	private final int A = 10, B = 11, C = 12, D = 13, E = 14, F = 15;
	
	/**Splits the number bases and operators of input*/	
	public String[] breakdown(String s) {
		String[] arrInput = s.split(regexSplit);
		return arrInput;
	}

	/**Converts A-F to respective values as represented in undenary to hexadecimal*/
	private int convert(char c) {
		int n = 0;
		
		switch (c) {
			case 'A': n = A;
				break;
			case 'B': n = B;
				break;
			case 'C': n = C;
				break;
			case 'D': n = D;
				break;
			case 'E': n = E;
				break;
			case 'F': n = F;
				break;
			default: n = Integer.parseInt(c + "");				
		}
		
		return n;
	}

	/**Evaluates the decimal expression*/
	public long evaluate(String[] arrInput) {
		/*ScriptEngineManager is used to evaluate the expression from String format*/
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine eng = mgr.getEngineByName("JavaScript");
		String input = "";
		long ans = 0;
		
		for(int i=0; i < arrInput.length; i++) {
			input = input + arrInput[i];
		}
		
		try {
			ans = ((Double) eng.eval(input)).intValue();
		} catch (ScriptException e) {
			System.out.println("Error evaluating input string.");
		}
		
		return ans;
	}
	
	/**Converts a number base into a decimal format and returns it back to its string array*/
	private String decimalPart(String s) {
		String[] expParts = s.split("_");
		String num = expParts[0];
		int base = Integer.parseInt(expParts[1]);
		int digits = num.length();
		int total = 0;
		
		for(int i=0; i < digits; i++) {
			int totalDigit = 0;
			int cur = convert(num.charAt(i));
			totalDigit = totalDigit + cur * pow(base, digits-(i+1));
			total = total + totalDigit;
		}
		
		return Integer.toString(total);
	}
	
	/**Exponentiation*/
	private int pow(int n, int power) {
		int r = 1;
		
		while(power!=0) {
			r = r * n;
			power--;
		}
		
		return r;
	}

	/**Reads and validates the input*/
	public String[] readInput() {
		String[] arrInput;
		boolean valid;
		
		do {
			Scanner in = new Scanner(System.in);
			input = in.nextLine();
			input = input.replaceAll("\\s+", "");
			
			arrInput = breakdown(input);
			valid = validParts(arrInput);
			
		} while(valid == false);
		
		return arrInput;
	}
	
	/**Converts the input to decimal form*/
	public String[] toDecimalInput(String[] arr) {
		for(int i=0; i < arr.length; i++) {
			if(i % 2 == 0) {
				arr[i] = decimalPart(arr[i]);
			}
		}
		
		return arr;
	}

	/**Validates the expression to make sure that every digit's value will be smaller than the base system*/
	private boolean validExp(String exp) {
		String[] expParts = exp.split("_");
		String num = expParts[0];
		int base = Integer.parseInt(expParts[1]);
		
		for(int i=0; i < num.length(); i++) {
			char charDigit = num.charAt(i);
			int intDigit = convert(charDigit);
			if(intDigit >= base) {
				return false;
			}
		}
		
		return true;
	}

	/**Checks if there are an odd number of expressions and operator pieces like it is supposed to and 
	 * validates the format of each number base and operator
	 */
	public boolean validParts(String[] arr) {
		if(arr.length % 2 == 0) {
			return false;
		}
		for(int i=0; i<arr.length; i++) {
			if(i % 2 == 0) {
				if(!arr[i].matches(regexExp) || !validExp(arr[i])) {
					return false;
				}
			} else if(i % 2 != 0) {
				if(!arr[i].matches(regexOp)) {
					return false;
				}
			}
		}
		
		return true;
	}
}
