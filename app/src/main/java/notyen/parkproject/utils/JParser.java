package notyen.parkproject.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class JParser {

    /**
     * @param source JsonString
     * @param c      Class
     * @return try to put Json data into class and return object
     */
    public static <O> O toObject(String source, Class<O> c) {
        JSONObject jObject = null;
        try {
            jObject = new JSONObject(source);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return toObject(jObject, c);
    }

    /**
     * @param jObject JsonObject
     * @param c       Class
     * @return try to put Json data into class and return object
     */
    public static <O> O toObject(JSONObject jObject, Class<O> c) {
        O o = newInstance(c);
        if (o == null)
            return null;
        List<Field> fields = getAllField(c);
        for (Field field : fields) {
            Object value = findValueByKey(jObject, field.getName());
            if (value != null)
                setFieldData(o, field, value);
        }
        return o;
    }

    /**
     * @param jArray JsonArray
     * @param c      Class
     * @return try to put Json data into class and return list object
     */
    public static <O> List<O> toList(JSONArray jArray, Class<O> c) {
        List<O> list = new ArrayList<O>();
        int length = jArray.length();
        if (isBaseElement(c))
            for (int i = 0; i < length; i++) {
                O o = null;
                try {
                    if (c != jArray.get(i).getClass())
                        throw new ClassCastException();
                    else
                        o = (O) jArray.get(i);
                } catch (ClassCastException e) {
                    Develop.e(JParser.class, c.getName()
                            + " Not Match JSONObject Type");
                } catch (JSONException e) {
                    Develop.e(JParser.class, "JSONObject Not Found");
                }
                if (o != null)
                    list.add(o);
            }
        else
            for (int i = 0; i < length; i++) {
                JSONObject jObject = null;
                try {
                    jObject = jArray.getJSONObject(i);
                } catch (JSONException e) {
//                    Develop.e(JParser.class, "JSONObject Not Found");
                }
                if (jObject != null)
                    list.add(toObject(jObject, c));
            }
        return list;
    }

    /**
     * @param jArrayString JsonArray
     * @param c            Class
     * @return try to put Json data into class and return list object
     */
    public static <O> List<O> toList(String jArrayString, Class<O> c) {
        if (jArrayString == null) {
//            Develop.i(JParser.class, "JsonArray is NULL");
            return new ArrayList<O>();
        } else
            try {
                JSONArray jArray = new JSONArray(jArrayString);
                return toList(jArray, c);
            } catch (JSONException e) {
//                Develop.i(JParser.class, "JsonArray Convert Fail");
                return new ArrayList<O>();
            }
    }

    /**
     * @param o
     * @return try to convert object to jsonObject
     */
    public static JSONObject toJsonObject(Object o) {
        if (o == null)
            return new JSONObject();
        else {
            JSONObject jObject = new JSONObject();
            Class<?> c = o.getClass();
            List<Field> fields = getAllField(c);
            for (Field f : fields) {
                f.setAccessible(true);
                try {
                    if (isBaseElement(f.getType()))
                        jObject.put(f.getName(), f.get(o));
                    else if (f.getType() == List.class) {
                        jObject.put(f.getName(),
                                toJsonArray((List<? extends Object>) f.get(o)));
                    } else
                        jObject.put(f.getName(), toJsonObject(f.get(o)));
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return jObject;
        }
    }

    /**
     * @param objectArray
     * @return try to convert objectArray to jsonArray
     */
    public static JSONArray toJsonArray(List<? extends Object> objectArray) {
        if (objectArray == null || objectArray.size() == 0)
            return new JSONArray();
        else {
            JSONArray jArray = new JSONArray();
            for (Object o : objectArray) {
                if (isBaseElement(o.getClass()))
                    jArray.put(o);
                else
                    jArray.put(toJsonObject(o));
            }
            return jArray;
        }
    }

    /**
     * @param o     to put data
     * @param field field to put data
     * @param value value to put in field in object
     */
    private static void setFieldData(Object o, Field field, Object value) {
        if (!isBaseElement(field.getType()))
            if (field.getType() == List.class) {
                if (value.getClass() == JSONArray.class) {
                    JSONArray jArray = (JSONArray) value;
                    Class<?> gClass = getlListClass(field);
                    value = toList(jArray, gClass);
                } else {
                    Develop.e(JParser.class, "JSONArray Not Found");
                }
            } else {
                if (value.getClass() == JSONObject.class) {
                    JSONObject jObject = (JSONObject) value;
                    Class<?> fClass = field.getType();
                    value = toObject(jObject, fClass);
                } else {
//                    Develop.e(JParser.class, "JSONObject Not Found");
                }
            }

        field.setAccessible(true);
        try {
            field.set(o, value);
        } catch (IllegalArgumentException e) {
//            Develop.i(JParser.class, field.getName() + "("
//                    + field.getType().getSimpleName() + ")" + " not match "
//                    + value.getClass().getSimpleName());
            tryToParseType(o, field, value);
        } catch (IllegalAccessException e) {
//            Develop.i(JParser.class, field.getName() + " is not accessible ");
        }
    }

    private static void tryToParseType(Object o, Field field, Object value) {
        try {
            if (field.getType() == Integer.TYPE)
                field.set(o, (int) Double.parseDouble(value.toString()));
            if (field.getType() == Short.TYPE)
                field.set(o, Short.parseShort(value.toString()));
            if (field.getType() == Long.TYPE)
                field.set(o, Long.parseLong(value.toString()));
            if (field.getType() == Float.TYPE)
                field.set(o, Float.parseFloat(value.toString()));
            if (field.getType() == Double.TYPE)
                field.set(o, Double.parseDouble(value.toString()));
            if (field.getType() == Boolean.TYPE)
                field.set(o, Boolean.parseBoolean(value.toString()));
            if (field.getType() == String.class)
                field.set(o, String.valueOf(value));
//            Develop.i(JParser.class, "try to parse："
//                    + value.getClass().getSimpleName() + "(" + value.toString()
//                    + ")" + " > " + field.getType().getSimpleName()
//                    + "：Success");
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * @param c Class
     * @return new class object
     */
    private static <O> O newInstance(Class<O> c) {
        try {
            return c.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return List field class
     */
    private static Class<?> getlListClass(Field field) {
        ParameterizedType pt = (ParameterizedType) field.getGenericType();
        return extractClassFromType(pt.getActualTypeArguments()[0]);
    }

    /**
     * @param t
     * @return GenericType
     */
    private static Class<?> extractClassFromType(Type t) {
        if (t instanceof Class<?>)
            return (Class<?>) t;
        return (Class<?>) ((ParameterizedType) t).getRawType();
    }

    /**
     * @param jObject JsonObject
     * @return value
     */
    private static Object findValueByKey(JSONObject jObject, String key) {
        if (null == jObject)
            return null;
        Iterator<?> keys = jObject.keys();
        // --檢查相同key--//
//        if(keys==null)
//            return null;
        while (keys.hasNext()) {
            String jsonKey = keys.next().toString();
            if (toLowerCase(jsonKey).equals(toLowerCase(key)))
                try {
                    return jObject.get(jsonKey);
                } catch (JSONException e) {

                }
        }
        // --檢查相同key(名稱第一位省去)--//
        keys = jObject.keys();
        String key2 = key.substring(1);
        while (keys.hasNext()) {
            String jsonKey = keys.next().toString();
            if (toLowerCase(jsonKey).equals(toLowerCase(key2)))
                try {
                    return jObject.get(jsonKey);
                } catch (JSONException e) {

                }
        }
//        Develop.i(JParser.class, key + " and " + key2 + " not found");
        return null;
    }

    /**
     * @param c Class
     * @return isBaseElement
     */
    private static boolean isBaseElement(Class<?> c) {
        if (c == Integer.TYPE)
            return true;
        else if (c == Integer.class)
            return true;
        else if (c == Float.class)
            return true;
        else if (c == Double.class)
            return true;
        else if (c == Float.TYPE)
            return true;
        else if (c == String.class)
            return true;
        else if (c == Double.TYPE)
            return true;
        else if (c == Long.TYPE)
            return true;
        else if (c == Boolean.TYPE)
            return true;
        else if (c == Short.TYPE)
            return true;
        else if (c == Character.TYPE)
            return true;
        else
            return false;
    }

    /**
     * @param c Class
     * @return all class field
     */
    private static List<Field> getAllField(Class<?> c) {
        List<Field> fields = new ArrayList<Field>();
        fields.addAll(Arrays.asList(c.getDeclaredFields()));
        Class<?> superClass = c.getSuperclass();
        if (superClass != null && !superClass.equals(Object.class))
            fields.addAll(getAllField(superClass));
        return fields;
    }

    /**
     * @param string String
     * @return lower String
     */
    private static String toLowerCase(String string) {
        String result = "";
        for (char c : string.toCharArray())
            if (c >= 65 && c <= 90)
                result += c += 32;
            else
                result += c;
        return result;
    }
}
