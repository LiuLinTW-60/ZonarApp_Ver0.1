
package com.james.utils;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrayConverter<T> {

	public ArrayList<T> toList(T[] objects) {
		ArrayList<T> list = new ArrayList<T>();
		if (objects != null) {
			list.addAll(new ArrayList<T>(Arrays.asList(objects)));
		}
		return list;
	}
}
