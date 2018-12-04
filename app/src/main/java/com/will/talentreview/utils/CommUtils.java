package com.will.talentreview.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;

import com.will.talentreview.constant.Config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.List;

public class CommUtils {
	private final static String TAG = "CommUtils";

	// read & write authority
	private final static int MODE = Context.MODE_PRIVATE;

	/**
	 * dp2px
	 */
	public static int dp2px(Context context, int dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static Bitmap B64ToBm(String str) {
		byte[] bytes = Base64.decode(str.trim().getBytes(), Base64.NO_PADDING);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	/**
	 * px2dp
	 */
	public static int px2dp(Context context, int pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 根据设备信息获取当前分辨率下指定单位对应的像素大小； px,dip,sp -> px
	 */
	public float getRawSize(Context c, int unit, float size) {
		Resources r;
		if (c == null) {
			r = Resources.getSystem();
		} else {
			r = c.getResources();
		}
		return TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
	}

	/**
	 * 播放器
	 */
	private static MediaPlayer mediaPlayer;
	private static AssetManager assetManager;

	public static boolean isEmpty(String s) {
		if (null == s)
			return true;
		if (s.length() == 0)
			return true;
		if (s.trim().length() == 0)
			return true;
		return false;
	}

	public static boolean isForeground(Context context, String className) {
		if (context == null || TextUtils.isEmpty(className)) {
			return false;
		}

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(1);
		if (list != null && list.size() > 0) {
			ComponentName cpn = list.get(0).topActivity;
			if (className.equals(cpn.getClassName())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * inner class describing object type
	 *
	 * @version 1.0
	 * @createDate 2013-8-7
	 */
	private enum SimpleObjectTypeEnum {
		Object,
		Integer,
		Long,
		Float,
		Boolean,
		String,
		UNKNOWN
	}

	/**
	 * Save specific key & value to given named shared preferences
	 *
	 * @param context
	 *            context to get sharedPreferences
	 * @param preferenceName
	 *            preferenceName name of sharedpreferences
	 * @param key
	 *            specific key
	 * @param value
	 *            specific value
	 * @return whether saved successfully ,return true or false
	 */
	public static boolean saveData(Context context, String preferenceName, String key, Object value) {

		boolean bCommitted = false;

		Editor oEditor = null;

		while (true) {

			if (context == null || preferenceName == null || key == null) {
				break; // while (true)
			}

			SimpleObjectTypeEnum objType = validateObjectType(value);

			oEditor = context.getSharedPreferences(preferenceName, MODE).edit();

			switch (objType) {

			case Integer:

				oEditor.putInt(key, (Integer) value);

				break;

			case Long:

				oEditor.putLong(key, (Long) value);

				break;

			case Float:

				oEditor.putFloat(key, (Float) value);

				break;

			case Boolean:

				oEditor.putBoolean(key, (Boolean) value);

				break;

			case String:

				oEditor.putString(key, (String) value);

				break;

			case Object:

				oEditor.putString(key, saveObjectToString(value));

				break;

			default:

				oEditor.putString(key, value.toString());

				break;

			} // switch (objType)

			bCommitted = oEditor.commit();

			break; // while (true)

		} // while (true)

		return (bCommitted);
	}

	/**
	 * judge an object type
	 *
	 * @param obj
	 *            object to be judged
	 * @return an object of <class>SimpleObjectTypeEnum</class> class
	 */
	private static SimpleObjectTypeEnum validateObjectType(Object obj) {

		SimpleObjectTypeEnum flag = SimpleObjectTypeEnum.UNKNOWN;

		if (obj instanceof Integer) {

			flag = SimpleObjectTypeEnum.Integer;

		} else if (obj instanceof Long) {

			flag = SimpleObjectTypeEnum.Long;

		} else if (obj instanceof Float) {

			flag = SimpleObjectTypeEnum.Float;

		} else if (obj instanceof Boolean) {

			flag = SimpleObjectTypeEnum.Boolean;

		} else if (obj instanceof String) {

			flag = SimpleObjectTypeEnum.String;

		} else if (obj instanceof Object) {

			flag = SimpleObjectTypeEnum.Object;
		}

		return (flag);
	}

	private static String saveObjectToString(Object object) {

		String zPersonBase64 = null;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		ObjectOutputStream oos;

		// //////////////////////////////////////////////////////////////////////
		// try
		// //////////////////////////////////////////////////////////////////////

		try {

			oos = new ObjectOutputStream(baos);

			oos.writeObject(object);

			zPersonBase64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

			// //////////////////////////////////////////////////////////////////////
			// IOException
			// //////////////////////////////////////////////////////////////////////
			baos.close();
			oos.close();
		} catch (IOException exIO) {

			exIO.printStackTrace();
		}

		return (zPersonBase64);
	}

	public static boolean getDataOfBoolean(Context context, String key) {
		SharedPreferences preferences = context.getSharedPreferences(Config.SP_NAME, MODE);
		return preferences.getBoolean(key, false);

	}

	/**
	 * get boolean value by given key from named shared preferences
	 *
	 * @param context
	 * @param preferenceName
	 * @param key
	 *            key
	 * @param defValue
	 *            default value
	 * @return value found
	 */
	public static boolean getDataBoolean(Context context, String preferenceName, String key, boolean defValue) {

		boolean bRetVal = defValue;

		if (context != null && preferenceName != null) {

			bRetVal = context.getSharedPreferences(preferenceName, MODE).getBoolean(key, defValue);
		}

		return (bRetVal);
	}

	/**
	 * get long value by given key from named shared preferences
	 *
	 * @param context
	 * @param preferenceName
	 * @param key
	 *            key
	 * @param defValue
	 *            default value
	 * @return value found
	 */
	public static long getDataLong(Context context, String preferenceName, String key, long defValue) {

		long lRetVal = defValue;

		if (context != null && preferenceName != null) {

			lRetVal = context.getSharedPreferences(preferenceName, MODE).getLong(key, defValue);
		}

		return (lRetVal);
	}

	/**
	 * get long value by given key from named shared preferences
	 *
	 * @param context
	 * @param preferenceName
	 * @param key
	 *            key
	 * @param defValue
	 *            default value
	 * @return value found
	 */
	public static float getFloat(Context context, String preferenceName, String key, float defValue) {

		float lRetVal = defValue;

		if (context != null && preferenceName != null) {

			lRetVal = context.getSharedPreferences(preferenceName, MODE).getFloat(key, defValue);
		}

		return (lRetVal);
	}

	/**
	 * get int value by given key from named shared preferences
	 *
	 * @param context
	 * @param preferenceName
	 * @param key
	 *            key
	 * @param defValue
	 *            default value
	 * @return value found
	 */
	public static int getDataInt(Context context, String preferenceName, String key, int defValue) {

		int nRetVal = defValue;

		if (context != null && preferenceName != null) {

			nRetVal = context.getSharedPreferences(preferenceName, MODE).getInt(key, defValue);
		}

		return (nRetVal);
	}

	/**
	 * get string value by given key from named shared preferences
	 *
	 * @param context
	 * @param preferenceName
	 * @param key
	 *            given key
	 * @param defValue
	 *            default value
	 * @return value found
	 */
	public static String getDataString(Context context, String preferenceName, String key, String defValue) {

		String zRetVal = defValue;

		if (context != null && preferenceName != null) {

			zRetVal = context.getSharedPreferences(preferenceName, MODE).getString(key, defValue);
		}

		return (zRetVal);
	}

	/**
	 * get Object value by given key from named shared preferences
	 *
	 * @param context
	 * @param preferenceName
	 * @param key
	 *            given key
	 * @param defValue
	 *            default value
	 * @return value found
	 */
	public static Object getDataObject(Context context, String preferenceName, String key, Object defValue) {

		Object oRetVal = defValue;

		if (context != null && preferenceName != null) {

			oRetVal = getObjectFromString(context.getSharedPreferences(preferenceName, MODE).getString(key, ""));
		}

		return (oRetVal);
	}

	private static Object getObjectFromString(String objString) {

		Object oRetVal = null;

		while (true) {

			if ("".equalsIgnoreCase(objString)) {
				break; // while (true)
			}

			byte[] base64Bytes = Base64.decode(objString.getBytes(), Base64.DEFAULT);

			ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);

			ObjectInputStream ois = null;

			// //////////////////////////////////////////////////////////////////
			// try
			// //////////////////////////////////////////////////////////////////

			try {

				ois = new ObjectInputStream(bais);

				oRetVal = ois.readObject();

				bais.close();
				ois.close();
				// //////////////////////////////////////////////////////////////////
				// StreamCorruptedException
				// //////////////////////////////////////////////////////////////////

			} catch (StreamCorruptedException exSC) {
				exSC.printStackTrace();

				// //////////////////////////////////////////////////////////////////
				// IOException
				// //////////////////////////////////////////////////////////////////

			} catch (IOException exIO) {
				exIO.printStackTrace();

				// //////////////////////////////////////////////////////////////////
				// ClassNotFoundException
				// //////////////////////////////////////////////////////////////////

			} catch (ClassNotFoundException exCNF) {
				exCNF.printStackTrace();
			}

			break; // while (true)

		} // while (true)

		return (oRetVal);
	}

	/**
	 * 获取设备唯一标识
	 *
	 * @param context
	 * @return
	 */
	@SuppressLint("MissingPermission")
	public static String getDeviceId(Context context) {
		String device_id = "";
		try {
			org.json.JSONObject json = new org.json.JSONObject();
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			device_id = tm.getDeviceId();

			android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);

			String mac = wifi.getConnectionInfo().getMacAddress();
			json.put("mac", mac);

			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}

			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}

		} catch (Exception e) {
		}
		return device_id;
	}

	/**
	 * 判断某个服务是否正在运行的方法
	 *
	 * @param mContext
	 * @param serviceName
	 *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
	 * @return true代表正在运行，false代表服务没有正在运行
	 */
	public boolean isServiceWork(Context mContext, String serviceName) {
		boolean isWork = false;
		ActivityManager myAM = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> myList = myAM.getRunningServices(40);
		if (myList.size() <= 0) {
			return false;
		}
		for (int i = 0; i < myList.size(); i++) {
			String mName = myList.get(i).service.getClassName().toString();
			if (mName.equals(serviceName)) {
				isWork = true;
				break;
			}
		}
		return isWork;
	}

	/**
	 * 判断app是否在手机当前显示
	 *
	 * @param context
	 * @param packname
	 * @return
	 */
	public static boolean isTopActivity(Context context, String packname) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
		if (tasksInfo.size() > 0) {
			String topPackageName = tasksInfo.get(0).topActivity.getPackageName();
			// 应用程序位于堆栈的顶层
			if (packname.equals(topPackageName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据包名检测某个APP是否安装
	 * <h3>Version</h3> 1.0
	 * <h3>CreateTime</h3> 2016/6/27,13:02
	 * <h3>UpdateTime</h3> 2016/6/27,13:02
	 * <h3>CreateAuthor</h3>
	 * <h3>UpdateAuthor</h3>
	 * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
	 *
	 * @param packageName
	 *            包名
	 * @return true 安装 false 没有安装
	 */
	public static boolean isInstallByRead(String packageName) {
		return new File("/data/data/" + packageName).exists();
	}

	/**
	 * 打开第三方app
	 *
	 * @param context
	 * @param packname
	 */
	public static void openApp(Context context, String packname) {
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(packname);
		if (intent != null) {
			context.startActivity(intent);
		}
	}

	/**
	 * play:(播放声音).
	 */
	public static void playSound(Context context, String soundName) {
		if (null == assetManager)
			assetManager = context.getAssets();

		AssetFileDescriptor fileDescriptor;
		if (null == mediaPlayer)
			mediaPlayer = new MediaPlayer();
		try {
			if (null != soundName && !"".equals(soundName)) {
				fileDescriptor = assetManager.openFd(soundName);
				mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(),
						fileDescriptor.getLength());
				mediaPlayer.prepare();
				mediaPlayer.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取app版本号
	 * */
	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
		}
		return versionName;
	}

	/**
	 * clear data of sp
	 *
	 * @param context
	 */
	public static void clearSPData(Context context) {

	}

//	public static LoginVO getLoginVoData(Context context) {
//		return (LoginVO) CommUtils.getDataObject(context, preferenceName, LOGINVO_DATA, null);
//	}
}
