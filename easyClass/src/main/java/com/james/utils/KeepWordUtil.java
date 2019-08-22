/*
 * Copyright 2012 Thinkermobile, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.james.utils;

/**
 * 
 * @author JamesX
 * @since 2012/10/15
 */
public class KeepWordUtil {
	/**
	 * represent for &
	 */
	public static String AND = "&amp;";
	
	/**
	 * represent for '
	 */
	public static String APOSTROPHE = "&apos;";
	
	/**
	 * represent for >
	 */
	public static String GREATER = "&gt;";
	
	/**
	 * represent for <
	 */
	public static String LESS = "&lt;";
	
	/**
	 * represent for "
	 */
	public static String DOUBLE_QUOTATION = "&quot;";
	
	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String decode(String string){
		String decode = string.replace(AND, "&");
		decode = decode.replace(APOSTROPHE, "'");
		decode = decode.replace(GREATER, ">");
		decode = decode.replace(LESS, "<");
		decode = decode.replace(DOUBLE_QUOTATION, "\"");
		
		return decode;
	}
	
	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String encode(String string){
		String decode = string.replace("&", AND);
		decode = decode.replace("'", APOSTROPHE);
		decode = decode.replace(">", GREATER);
		decode = decode.replace("<", LESS);
		decode = decode.replace("\"", DOUBLE_QUOTATION);
		
		return decode;
	}
}
