package in.co.indusnet.soap.utility;

import java.util.Random;

public class Generator {
	
	public static String randomNumberGenerator(int length) {
		Random random = new Random();
		long number = random.nextLong();
		if(number<=0) {
			number = number * (-1);
		}
		String numberString = Long.toString(number);
		String value=numberString.substring(0,length);
		return value;
	}
	
}
