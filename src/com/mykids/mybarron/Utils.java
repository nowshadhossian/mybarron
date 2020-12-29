package com.mykids.mybarron;

import java.util.Random;

/**
 * Created by Nowshad on 7/30/14.
 */
public class Utils {
    public static <T> String getCommaSeperatedString(T[] array){
        String commaSeperated = "";
        for(T a : array){
            commaSeperated += a + ",";
        }
        return removeLastCommaIfExist(commaSeperated);
    }

    public static String removeLastCommaIfExist(String str){
        if(str.endsWith(",")){
            return str.substring(0, str.length()-1);
        }
        return str;
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
    
    public static Integer[] getArrayFromCommaSeperatedString(String str) 
    		throws UnsupportedParameterException{
    	String numbers[];
    	if(str == null){
    		throw new UnsupportedParameterException();
    	}
    	
    	str = str.trim();
    	
    	if(str.contains(",")){
    		numbers = str.split(",");	
    	} else if(str.contains(" ")){
    		numbers = str.split(" ");
    	} else if(str.matches("[0-9]+")){
    		try{
    			return new Integer[]{ Integer.parseInt(str) };	
    		}catch(Exception e){
    			throw new UnsupportedParameterException();
    		}
    	} else {
    		throw new UnsupportedParameterException();
    	}
    	
    	Integer[] numberInt = new Integer[numbers.length];
    	for(int i = 0; i< numbers.length; i++){
    		try{
    			numberInt[i] = Integer.parseInt(numbers[i]);
    		} catch(Exception e){
    			throw new UnsupportedParameterException();
    		}
    		
    	}
    	return numberInt;
    }
    
    
}
