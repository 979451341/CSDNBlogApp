package com.example.csdn.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class SPUtils
{
	public SPUtils()
	{
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 保存在手机里面的文件名
	 */
	public static final String FILE_NAME = "share_data";

	/**
	 * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
	 *  @param context
	 * @param key
	 * @param object
	 */
	public static String put(Context context, String key, Object object)
	{

		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();

		if (object instanceof String)
		{
			editor.putString(key, (String) object);
		} else if (object instanceof Integer)
		{
			editor.putInt(key, (Integer) object);
		} else if (object instanceof Boolean)
		{
			editor.putBoolean(key, (Boolean) object);
		} else if (object instanceof Float)
		{
			editor.putFloat(key, (Float) object);
		} else if (object instanceof Long)
		{
			editor.putLong(key, (Long) object);
		} else
		{
			editor.putString(key, object.toString());
		}

		SharedPreferencesCompat.apply(editor);
		return key;
	}

	/**
	 * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
	 * 
	 * @param context
	 * @param key
	 * @param defaultObject
	 * @return
	 */
	public static Object get(Context context, String key, Object defaultObject)
	{
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);

		if (defaultObject instanceof String)
		{
			return sp.getString(key, (String) defaultObject);
		} else if (defaultObject instanceof Integer)
		{
			return sp.getInt(key, (Integer) defaultObject);
		} else if (defaultObject instanceof Boolean)
		{
			return sp.getBoolean(key, (Boolean) defaultObject);
		} else if (defaultObject instanceof Float)
		{
			return sp.getFloat(key, (Float) defaultObject);
		} else if (defaultObject instanceof Long)
		{
			return sp.getLong(key, (Long) defaultObject);
		}

		return null;
	}

	/**
	 * 移除某个key值已经对应的值
	 * 
	 * @param context
	 * @param key
	 */
	public static void remove(Context context, String key)
	{
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		SharedPreferencesCompat.apply(editor);
	}


	/**
	 * 清除所有数据
	 * 
	 * @param context
	 */
	public static void clear(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 查询某个key是否已经存在
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean contains(Context context, String key)
	{
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		return sp.contains(key);
	}

	/**
	 * 返回所有的键值对
	 * 
	 * @param context
	 * @return
	 */
	public static Map<String, ?> getAll(Context context)
	{
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		return sp.getAll();
	}

	/**
	 * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
	 * 
	 * @author zhy
	 * 
	 */
	private static class SharedPreferencesCompat
	{
		private static final Method sApplyMethod = findApplyMethod();

		/**
		 * 反射查找apply的方法
		 * 
		 * @return
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private static Method findApplyMethod()
		{
			try
			{
				Class clz = SharedPreferences.Editor.class;
				return clz.getMethod("apply");
			} catch (NoSuchMethodException ignored)
			{
			}

			return null;
		}

		/**
		 * 如果找到则使用apply执行，否则使用commit
		 * 
		 * @param editor
		 */
		public static void apply(SharedPreferences.Editor editor)
		{
			try
			{
				if (sApplyMethod != null)
				{
					sApplyMethod.invoke(editor);
					return;
				}
			} catch (IllegalArgumentException ignored)
			{
			} catch (IllegalAccessException ignored)
			{
			} catch (InvocationTargetException ignored)
			{
			}
			editor.commit();
		}
	}


	/**
	 * 保存List
	 * @param key
	 * @param datalist
	 */
	public static <T> void setDataList(Context context,String key, List<T> datalist) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		if (null == datalist || datalist.size() <= 0)
			return;

		Gson gson = new Gson();
		//转换成json数据，再保存
		String strJson = gson.toJson(datalist);
//		editor.clear();
		editor.putString(key, strJson);
		editor.commit();

	}

	/**
	 * 获取List
	 * @param key
	 * @return
	 */
	public static <T> List<T> getDataList(Context context, String key, Object defaultObject) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
				Context.MODE_PRIVATE);
		List<T> datalist=new ArrayList<T>();
		String strJson = sp.getString(key, null);
		if (null == strJson) {
			return datalist;
		}
		Gson gson = new Gson();
		datalist = gson.fromJson(strJson, new TypeToken<List<T>>(){}.getType());
		Log.e(TAG, "getDataList: "+gson.fromJson(strJson, new TypeToken<List<T>>(){}.getType()));
		return datalist;

	}


}