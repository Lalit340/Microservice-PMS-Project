package in.co.indusnet.rekyc.utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.co.indusnet.rekyc.exception.SSODataStoreException;


@Component
public class CommonFunctions {

	@Autowired
	private ObjectMapper mapper;
	
	/**
	 * Applies the specified mask to the card number.
	 *
	 * @param cardNumber The card number in plain format
	 * @param mask       The number mask pattern. Use # to include a digit from the
	 *                   card number at that position, use x to skip the digit at
	 *                   that position
	 *
	 * @return The masked card number Example - maskCardNumber("1234123412341234",
	 *         "xxxx-xxxx-xxxx-####") > xxxx-xxxx-xxxx-1234
	 */
	public static String maskCardNumber(String cardNumber, String mask) {

		// format the number
		int index = 0;
		StringBuilder maskedNumber = new StringBuilder();
		for (int i = 0; i < mask.length(); i++) {
			char c = mask.charAt(i);
			if (c == '#') {
				maskedNumber.append(cardNumber.charAt(index));
				index++;
			} else if (c == 'x') {
				maskedNumber.append(c);
				index++;
			} else {
				maskedNumber.append(c);
			}
		}

		// return the masked number
		return maskedNumber.toString();
	}

	public static String randomNumber(int count) {
		// define the range
		int max = 9;
		int min = 1;
		int range = max - min + 1;

		String rNo = null;
		// generate random numbers within 1 to 10
		for (int i = 0; i < count; i++) {
			int rand = (int) (Math.random() * range) + min;
			rNo = rNo + rand;
		}
		return rNo;
	}

	// funstion to convert object to json string
	public String getJson(Object object) {
		try {
			String jsonData = mapper.writeValueAsString(object);
			return jsonData;
		} catch (JsonProcessingException e) {
			throw new SSODataStoreException("Unable to Process", HttpStatus.EXPECTATION_FAILED.value());
		}
	}

	/**
	 * Reads a "properties" file, and returns it as a Map (a collection of key/value
	 * pairs).
	 * 
	 * @param filename  The properties filename to read.
	 * @param delimiter The string (or character) that separates the key from the
	 *                  value in the properties file.
	 * @return The Map that contains the key/value pairs.
	 * @throws Exception assumption here is that proper lines are like "key = value"
	 *                   and the "=" is the delimiter
	 */
	public static Map<String, String> readPropertiesFileAsMap(String filename, String delimiter) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.trim().length() == 0)
				continue;
			if (line.charAt(0) == '#')
				continue;
			int delimPosition = line.indexOf(delimiter);
			String key = line.substring(0, delimPosition - 1).trim();
			String value = line.substring(delimPosition + 1).trim();
			map.put(key, value);
		}
		reader.close();
		return map;
	}

	public static String convertStringArrayToString(ArrayList<String> idProofArray, String delimiter) {
		StringBuilder sb = new StringBuilder();
		for (String str : idProofArray)
			sb.append(str).append(delimiter);
		return sb.substring(0, sb.length() - 1);
	}

	public static String getAccountTypeString(String type) {
		
		String returnType = null;
		switch (type) {
		case "SBA":
			returnType = "SA";
			break;
		case "SAA":
			returnType = "SA";
			break;
		case "CAA":
			returnType = "CA";
			break;
		case "FDA":
			returnType = "FD";
			break;
		case "ODA":
			returnType = "OD";
			break;
		case "TDA":
			returnType = "TD";
			break;
		default:
			returnType = "";
			break;
		}
		return returnType;
	}

	public static String getAccountTypeFullName(String type) {
		String returnType = null;
		switch (type) {
		case "SBA":
			returnType = "Savings Account";
			break;
		case "SAA":
			returnType = "Savings Account";
			break;
		case "CAA":
			returnType = "Current Account";
			break;
		case "FDA":
			returnType = "Fixed Deposit";
			break;
		case "ODA":
			returnType = "Overdraft Account";
			break;
		case "TDA":
			returnType = "Term Deposit";
			break;
		default:
			returnType = "";
			break;
		}
		return returnType;
	}

	public static String capitalizeString(String str) {
		String finalString = null;
		String[] strArr;
		if (str == null || (str.length()) == 0) {
			return str;
		} else {
			strArr = str.split(" ");
		}

		if (strArr.length > 0) {
			for (int i = 0; i < strArr.length; i++) {
				strArr[i] = strArr[i].toLowerCase();
				strArr[i] = strArr[i].substring(0, 1).toUpperCase() + strArr[i].substring(1);
			}
			finalString = String.join(" ", strArr);
		}

		return finalString;
	}

	public static String[] removeDuplicateVal(String[] args) {

		LinkedHashSet<String> lhSetColors = new LinkedHashSet<String>(Arrays.asList(args));
		String[] newArray = lhSetColors.toArray(new String[lhSetColors.size()]);
		return newArray;

	}
	public static String[] concatStringArry(String[]... jobs) {
        int len = 0;
        for (final String[] job : jobs) {
            len += job.length;
        }

        final String[] result = new String[len];

        int currentPos = 0;
        for (final String[] job : jobs) {
            System.arraycopy(job, 0, result, currentPos, job.length);
            currentPos += job.length;
        }

        return result;
    }
}
