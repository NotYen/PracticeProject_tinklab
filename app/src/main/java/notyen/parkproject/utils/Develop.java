package notyen.parkproject.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Develop {
	final public static String divider = "====================";
	static boolean debug = false;
	static TextView textView;

	public static void setDebug(boolean debug) {
		Develop.debug = debug;
	}

	public static boolean isDebug() {
		return debug;
	}

	public static void v(Object c, Object log) {
		if (debug)
			Log.v(getClassName(c), getLog(log));
	}

	public static void e(Object c, Object log) {
		if (debug)
			Log.e(getClassName(c), getLog(log));
	}

	public static void d(Object c, Object log) {
		if (debug)
			Log.d(getClassName(c), getLog(log));
	}

	public static void i(Object c, Object log) {
		if (debug)
			Log.i(getClassName(c), getLog(log));
	}

	public static void w(Object c, Object log) {
		if (debug)
			Log.w(getClassName(c), getLog(log));
	}

	public static String getMemoryInfo() {
		float maxMemory = Runtime.getRuntime().maxMemory() / 1024f / 1024f;
		float totalMemory = Runtime.getRuntime().totalMemory() / 1024f / 1024f;
		float freeMemory = Runtime.getRuntime().freeMemory() / 1024f / 1024f;
		return String.format("%.1f", totalMemory) + "mb/"
				+ String.format("%.1f", maxMemory) + "mb/"
				+ String.format("%.1f", freeMemory) + "mb";
	}

	public static void toast(Context context, Object obj, int duration) {
		if (debug)
			Toast.makeText(context, obj.toString(), duration).show();
	}

	public static void toast(Context context, int resId, int duration) {
		if (debug)
			Toast.makeText(context, context.getString(resId), duration).show();
	}

	/**
	 * 解決連續顯示朝時間才消失的Toast
	 */
	public static Toast toast = null;
	public static void showToast(Context context, String msg) {
		if (toast == null) {
			toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}

	private static String getClassName(Object o) {
		if (o == null)
			return "isNull";
		else if (o.getClass() == String.class)
			return (String) o;
		else if (o.getClass() == Class.class)
			return ((Class<?>) o).getName();
		else
			return o.getClass().getName();
	}

	private static String getLog(Object log) {
		return log == null ? "isNull" : log.toString();
	}

	/**
	 * Set Developer Label at layout TOP_LEFT Load畫面上顯示"Developer"字樣
	 */

	public static void addDevText(Context context, final ViewGroup layout,
								  String prevText) {
		final TextView devText = new TextView(context);
		if (debug) {
			layout.addView(devText, LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);

			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd HH:mm");
			String s = dateFormat.format(getBuildDate(context));
			devText.setText(prevText + s);

			devText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
			devText.setBackgroundColor(Color.BLACK);
			devText.setTextColor(Color.WHITE);
			AnimationSet animSet = new AnimationSet(false);
			animSet.addAnimation(new TranslateAnimation(0, 0, -devText
					.getLineHeight(), 0));
			animSet.addAnimation(new AlphaAnimation(0, 1));
			animSet.setDuration(500);
			devText.startAnimation(animSet);
			new CountDownTimer(5000, 5000) {

				@Override
				public void onTick(long arg0) {

				}

				@Override
				public void onFinish() {
					layout.removeView(devText);
				}
			}.start();
		}
	}

	public static Date getBuildDate(Context context) {
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(), 0);
			ZipFile zf = new ZipFile(ai.sourceDir);
			ZipEntry ze = zf.getEntry("classes.dex");
			long time = ze.getTime();
			return new Date(time);
		} catch (Exception e) {
			return new Date();
		}
	}

	public static void addDevText(Context context, String text) {
		if (textView != null && textView.getParent() != null)
			return;
		WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_FULLSCREEN,
				PixelFormat.TRANSLUCENT);
		textView = new TextView(context);
		// textView.setBackgroundColor(Color.argb(255, 0, 0, 0));
		textView.setTextColor(Color.RED);
		textView.setText(text);
		textView.setGravity(Gravity.CENTER);

		final WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		manager.addView(textView, params);
		Animation in = new AlphaAnimation(0, 1);
		in.setDuration(1000);
		final Animation out = new AlphaAnimation(1, 0);
		out.setDuration(1000);
		textView.startAnimation(in);
	}

	public static void removeDevText(Context context) {
		final WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		if (textView != null && textView.getParent() != null)
			manager.removeView(textView);
	}

//	public static String UTF8Text(String str) {
//		String UTF8Speed = "";
//		try {
//			UTF8Speed = new String(str.getBytes("ISO-8859-1"), "utf-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		return UTF8Speed;
//	}
}
