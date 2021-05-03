/*
 * Copyright (C) 1993-2020 ID Business Solutions Limited
 * All rights reserved
 */
package com.idbs.devassessment.solution;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import com.idbs.devassessment.core.DifficultyLevel;
import com.idbs.devassessment.core.IDBSSolutionException;

/**
 * Example solution for the example question
 */

public class CandidateSolution extends CandidateSolutionBase {
	@Override
	public DifficultyLevel getDifficultyLevel() {
		return DifficultyLevel.LEVEL_2;
	}

	@Override
	public String getAnswer() throws IDBSSolutionException {
		long result = 0;
		try { 
			String json = getDataForQuestion();
			if (json.contains(";")) {
				String[] str = json.split(";");
				ArrayList<Long> arrLong = new ArrayList<>();
				int valueOfX = Integer.valueOf(str[0].substring(str[0].indexOf("=") + 2));
				String yFinal = str[1].substring(str[1].indexOf("=") + 2);
				String[] ySplit = yFinal.replace("+", "p").replace("-", "p").split("p");
				ArrayList<String> operationList = new ArrayList<String>();				
				 				
				for (int i = 0; i < ySplit.length; i++) {
					if (ySplit[i].length() > 1) {
						arrLong.add(Integer.valueOf(ySplit[i].substring(0, ySplit[i].indexOf(".")))
								* getPower(valueOfX, Integer.valueOf(ySplit[i].substring(ySplit[i].indexOf(".") + 3))));
					}
				}
				String[] charSplit = yFinal.split("(?!^)");
				for (int i = 0; i < charSplit.length; i++) {
					if (charSplit[i].equals("+") || charSplit[i].equals("-")) {
						operationList.add(charSplit[i]);
					}
				}

				for (int j = 0; j < operationList.size(); j++) {
					if (operationList.get(j).equals("+")) {
						result = Math.addExact(result, arrLong.get(j));
					}
					if (operationList.get(j).equals("-")) {
						result = Math.subtractExact(result, arrLong.get(j));
					}
				}

			} else {
				JsonReader reader = Json.createReader(new StringReader(json));
				JsonObject jsonObject = reader.readObject();
				reader.close();

				int xValue = jsonObject.getInt("xValue");

				List<JsonValue> jsonArray = jsonObject.getJsonArray("terms");

				for (int i = 0; i < jsonArray.size(); i++) {
					JsonReader readerSingle = Json.createReader(new StringReader(jsonArray.get(i).toString()));
					JsonObject jsonObjectSingle = readerSingle.readObject();
					reader.close();
					int power = jsonObjectSingle.getInt("power");
					int multiplier = jsonObjectSingle.getInt("multiplier");
					String action = jsonObjectSingle.getString("action");
					long valueOld = getPower(xValue, power);
					
					long value = multiplier == 0 ? 0 : multiplication(valueOld, multiplier);					
					if (action.equals("add")) {
						result = (result + value);
					}

					if (action.equals("subtract")) {
						result = (result - value);
					}
				}
			}
		} catch (Exception e) {
			throw new IDBSSolutionException();
		}

		return String.valueOf(result);
	}

	public static long getPower(int startValue, int power) {
		long result = startValue;
		if (power == 0)
			return 1;
		for (int i = 1; i < power; i++) {
			result = multiplication(result, startValue);
		}
		return result;
	}

	public static long multiplication(long startValue, long power) {
		long result = 0;
		for (int i = 0; i < power; i++) {
			result = result + startValue;
		}
		return result;
	}

}
