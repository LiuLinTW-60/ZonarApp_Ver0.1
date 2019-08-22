/*
 * Copyright 2014 Thinkermobile, Inc.
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

package com.james.easyinternet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * <br>
 * Chinese Explanation: <br>
 * EasyResponseObjectParser可以用來處理一個正規命名的JSONObject字串，轉成一個object <br>
 * 需注意的是object本身的public變數必須完全對應到JSONObject的各層位置 <br>
 * <br>
 * English Explanation: <br>
 *
 * @author JamesX
 * @since 2014_02_20
 */
public class EasyResponseObjectParser {

    public static final String TAG = EasyResponseObjectParser.class.getSimpleName();

    static String NAME_PREFIX = "class ";

    private EasyResponseObjectParser() {

    }

    /**
     * @deprecated <br>
     * Instead, please use <br>
     * <code> startParsing(String jsonString, Object target)
     */
    public static void simpleParseObject(String jsonString, Object target) throws Exception {
        startParsing(jsonString, target);
    }

    /**
     * <br>
     * Chinese Explanation: <br>
     * 用來處理JSONObject字串，object的public變數設計必須對應JSONObject的內容階層 <br>
     * <br>
     * English Explanation: <br>
     * Parse a Json String into a target object. All public variables of target object must match the structure of Json String. <br>
     * <br>
     * Take a Json String for example: <br>
     * <p/>
     * <pre>
     * "car":{"color":"red","type":"coupe"}
     * </pre>
     * <p/>
     * <br>
     * <br>
     * developer must define a class like following to match the structure. <br>
     * <p/>
     * <pre>
     * public class Car {
     * 	public String color;
     * 	public String type;
     * }
     * </pre>
     *
     * @param jsonString (String)
     * @param target     (Object)
     * @throws Exception
     */
    public static void startParsing(String jsonString, Object target) throws Exception {
        if (jsonString != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                parseJson(target, jsonObject);
            } catch (JSONException e) {
                JSONArray jsonArray = new JSONArray(jsonString);
                parseJson(target, jsonArray);
            }
        }
    }

    /**
     * @deprecated <br>
     * Instead, please use <br>
     * <code> startParsing(EasyResponseObject easyResponseObject, Object target)
     */
    public static void simpleParseObject(EasyResponseObject easyResponseObject, Object target) throws Exception {
        startParsing(easyResponseObject, target);
    }

    /**
     * <br>
     * Chinese Explanation: <br>
     * 用來處理JSONObject字串，object的public變數設計必須對應JSONObject的內容階層 <br>
     * <br>
     * English Explanation: <br>
     * Parse a Json String into a target object. All public variables of target object must match the structure of Json String. <br>
     * Json String here denotes the body of easyResponseObject. <br>
     * <br>
     * Take a Json String for example: <br>
     * <p/>
     * <pre>
     * "car":{"color":"red","type":"coupe"}
     * </pre>
     * <p/>
     * <br>
     * <br>
     * developer must define a class like following to match the structure. <br>
     * <p/>
     * <pre>
     * public class Car {
     * 	public String color;
     * 	public String type;
     * }
     * </pre>
     *
     * @param easyResponseObject (EasyResponseObject)
     * @param target             (Object)
     * @throws Exception
     */
    public static void startParsing(EasyResponseObject easyResponseObject, Object target) throws Exception {
        if (easyResponseObject != null && easyResponseObject.getBody() != null) {
            try {
                JSONObject jsonObject = new JSONObject(easyResponseObject.getBody());
                parseJson(target, jsonObject);
            } catch (JSONException e) {
                JSONArray jsonArray = new JSONArray(easyResponseObject.getBody());
                parseJson(target, jsonArray);
            }
        }
    }

    private static void parseJson(Object target, Object jsonObjectOrArray) throws JSONException, IllegalArgumentException, IllegalAccessException, ArrayIndexOutOfBoundsException, InstantiationException {
        Field[] fields = target.getClass().getFields();
        for (int i = 0; i < fields.length; i++) {
            //
            String name = fields[i].getName();

            if (jsonObjectOrArray instanceof JSONObject &&
                    !((JSONObject) jsonObjectOrArray).has(name) &&
                    !fields[i].getType().getSimpleName().equalsIgnoreCase(HashMap.class.getSimpleName())) {
                continue;
            }

            final Object object;
            if (jsonObjectOrArray instanceof JSONObject &&
                    !fields[i].getType().getSimpleName().equalsIgnoreCase(HashMap.class.getSimpleName()) &&
                    !((JSONObject) jsonObjectOrArray).isNull(name)) {
                object = ((JSONObject) jsonObjectOrArray).get(name);
            } else {
                object = jsonObjectOrArray;
            }

            if (object instanceof JSONArray) {
                Class<?> componentType = fields[i].getType().getComponentType();

                JSONArray jsonArray = (JSONArray) object;
                if (fields[i].getType().getSimpleName().contains("[]")) {
                    fields[i].set(target, Array.newInstance(componentType, jsonArray.length()));
                    for (int index = 0; index < jsonArray.length(); index++) {
                        Object object2 = jsonArray.get(index);
                        if (object2 instanceof JSONObject) {
                            Array.set(fields[i].get(target), index, componentType.newInstance());
                            parseJson(Array.get(fields[i].get(target), index), (JSONObject) object2);
                        } else if (object2 instanceof String) {
                            Array.set(fields[i].get(target), index, (String) object2);
                        } else if (object2 instanceof Integer || object2 instanceof Long ||
                                object2 instanceof Float || object2 instanceof Double) {
                            if (fields[i].get(target).getClass().getFields()[0].getType().getSimpleName().toLowerCase().contains("int")) {
                                Array.set(fields[i].get(target), index, Double.valueOf(object2.toString()).intValue());
                            } else if (fields[i].get(target).getClass().getFields()[0].getType().getSimpleName().toLowerCase().contains("long")) {
                                Array.set(fields[i].get(target), index, Double.valueOf(object2.toString()).longValue());
                            } else if (fields[i].get(target).getClass().getFields()[0].getType().getSimpleName().toLowerCase().contains("float")) {
                                Array.set(fields[i].get(target), index, Float.valueOf(object2.toString()));
                            } else if (fields[i].get(target).getClass().getFields()[0].getType().getSimpleName().toLowerCase().contains("double")) {
                                Array.set(fields[i].get(target), index, Double.valueOf(object2.toString()));
                            }
                        } else if (object2 instanceof Boolean) {
                            try {
                                Array.set(fields[i].get(target), index, (Boolean) object2);
                            } catch (Exception e) {
                                if ((Boolean) object) {
                                    Array.set(fields[i].get(target), index, 1);
                                } else {
                                    Array.set(fields[i].get(target), index, 0);
                                }
                            }
                        }
                    }
                } else if (fields[i].getType().getSimpleName().equalsIgnoreCase(ArrayList.class.getSimpleName())) {
                    ParameterizedType genericType = (ParameterizedType) fields[i].getGenericType();
                    Type[] actualTypeArguments = genericType.getActualTypeArguments();
                    ArrayList list = new ArrayList();
                    fields[i].set(target, list);
                    for (int index = 0; index < jsonArray.length(); index++) {
                        Object object2 = jsonArray.get(index);
                        if (actualTypeArguments[0].toString().contains("java.lang.String")) {
                            list.add(object2.toString());
                        } else if (object2 instanceof JSONObject) {
                            list.add(getClassNewInstance(actualTypeArguments[0]));
                            parseJson(list.get(index), (JSONObject) object2);
                        } else if (object2 instanceof String) {
                            list.add(object2.toString());
                        } else if (object2 instanceof Integer || object2 instanceof Long ||
                                object2 instanceof Float || object2 instanceof Double) {
                            if (fields[i].get(target).getClass().getFields()[0].getType().getSimpleName().toLowerCase().contains("int")) {
                                list.add(Double.valueOf(object2.toString()).intValue());
                            } else if (fields[i].get(target).getClass().getFields()[0].getType().getSimpleName().toLowerCase().contains("long")) {
                                list.add(Double.valueOf(object2.toString()).longValue());
                            } else if (fields[i].get(target).getClass().getFields()[0].getType().getSimpleName().toLowerCase().contains("float")) {
                                list.add(Float.valueOf(object2.toString()));
                            } else if (fields[i].get(target).getClass().getFields()[0].getType().getSimpleName().toLowerCase().contains("double")) {
                                list.add(Double.valueOf(object2.toString()));
                            }
                        } else if (object2 instanceof Boolean) {
                            try {
                                list.add((Boolean) object2);
                            } catch (Exception e) {
                                if ((Boolean) object) {
                                    list.add(1);
                                } else {
                                    list.add(0);
                                }
                            }
                        }
                    }
                }
            } else if (object instanceof JSONObject) {
                if (fields[i].getType().getSimpleName().equalsIgnoreCase(HashMap.class.getSimpleName())) {
                    ParameterizedType genericType = (ParameterizedType) fields[i].getGenericType();
                    Type[] actualTypeArguments = genericType.getActualTypeArguments();

                    HashMap hashMap = new HashMap();
                    fields[i].set(target, hashMap);

                    Iterator iterator = ((JSONObject) object).keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next().toString();
                        Object newInstance = getClassNewInstance(actualTypeArguments[1]);
                        hashMap.put(key, newInstance);
                        parseJson(hashMap.get(key), newInstance);
                    }
                } else {
                    try{
                        fields[i].set(target, fields[i].getType().newInstance());
                    }
                    catch (Exception e) {

                    }
                    parseJson(fields[i].get(target), (JSONObject) object);
                }
            } else if (object instanceof String) {
                fields[i].set(target, (String) object);
            } else if (object instanceof Integer || object instanceof Long ||
                    object instanceof Float || object instanceof Double) {
                if (fields[i].getType().getSimpleName().toLowerCase().contains("int")) {
                    fields[i].set(target, Double.valueOf(object.toString()).intValue());
                } else if (fields[i].getType().getSimpleName().toLowerCase().contains("long")) {
                    fields[i].set(target, Double.valueOf(object.toString()).longValue());
                } else if (fields[i].getType().getSimpleName().toLowerCase().contains("float")) {
                    fields[i].set(target, Float.valueOf(object.toString()));
                } else if (fields[i].getType().getSimpleName().toLowerCase().contains("double")) {
                    fields[i].set(target, Double.valueOf(object.toString()));
                }
            } else if (object instanceof Boolean) {
                try {
                    fields[i].set(target, (Boolean) object);
                } catch (Exception e) {
                    if ((Boolean) object) {
                        fields[i].set(target, 1);
                    } else {
                        fields[i].set(target, 0);
                    }
                }
            }
        }
    }

    private static Object getClassNewInstance(Type type) {
        String fullName = type.toString();
        if (fullName.startsWith(NAME_PREFIX)) {

            fullName = fullName.substring(NAME_PREFIX.length());
        }

        try {
            Class<?> genericsType = Class.forName(fullName);
            // now, i have a instance of generics type
            Object object = genericsType.newInstance();
            return object;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
