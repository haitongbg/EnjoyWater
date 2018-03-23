package com.enjoywater.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static boolean isAppRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
        ActivityManager.RunningAppProcessInfo topProcessInfo = runningProcesses.get(0);
        if (topProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
            if (topProcessInfo.processName.equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static String getTime() {
        long lTime = 0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        String time = dateFormat.format(date);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        try {
            Date dateNow = simpleDateFormat.parse(time);
            lTime = dateNow.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String dateT = String.valueOf(lTime);

        return dateT;

    }

    public static boolean isInternetOn(Context context) {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            // if connected with internet
            return true;
        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    public static void saveString(Context context, String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, null);
    }

    public static void removeString(Context context, String key) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove(key).commit();
    }

    public static String getStringNotNull(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, "");
    }

    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    public static int getRelativeTop(View myView) {
//	    if (myView.getParent() == myView.getRootView())
        if (myView.getId() == android.R.id.content)
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent());
    }

    public static int getRelativeLeft(View myView) {
//	    if (myView.getParent() == myView.getRootView())
        if (myView.getId() == android.R.id.content)
            return myView.getLeft();
        else
            return myView.getLeft() + getRelativeLeft((View) myView.getParent());
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {

            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                // Read byte from input stream

                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;

                // Write byte from output stream
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    public static String priceParse(float price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.##");
        return formatter.format(price);
    }

    public static String formatNumber(float price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.##");
        return formatter.format(price);
    }

    public static String formatNumber(int number) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.##");
        return formatter.format(number);
    }


    public static void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showSoftKeyboard(final View view) {
        final InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                imm.showSoftInput(view, 0);
            }
        }, 500);

    }

    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public static String getMD5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Jump to phone call
     *
     * @param context
     * @param phoneNumber
     */
    public static void call(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }

    public static void sendEmail(String to, String title, Context context) {

        String[] TO = {to, ""};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        // emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Nhập nội dung");
        emailIntent.setType("message/rfc822");
        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context,
                    "There is no email client installed.", Toast.LENGTH_SHORT)
                    .show();
        }
    }


    public static void shareLink(Context context, String link) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, link);
        context.startActivity(Intent.createChooser(intent, "Share link"));
    }

    /**
     * Generate facebook keyhash
     *
     * @param context
     */
    public static void createKeyhashForFacebook(Context context) {
        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("KEYHASH=", something);
            }
        } catch (NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop()
                + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    public static Bitmap getCircledBitmap(Bitmap originalBitmap) {
        int originalWidth = originalBitmap.getWidth();
        int originalHeight = originalBitmap.getHeight();

        int size = originalWidth;
        if (originalHeight < originalWidth) {
            size = originalHeight;
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GRAY);
        paint.setStyle(Style.FILL);

        // draw circle
        canvas.drawCircle(size / 2, size / 2, size / 2, paint);

        // set paint mode
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

        // draw original bitmap
        if (size == originalHeight) {
            canvas.drawBitmap(originalBitmap, -(originalWidth / 2 - size / 2), 0, paint);
        } else {
            canvas.drawBitmap(originalBitmap, 0, -(originalHeight / 2 - size / 2), paint);
        }

        // return result
        return bitmap;
    }

    public static Bitmap getCroppedBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);

            canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;
        } else {
            return null;
        }
    }

    public static final int RECORD_PERMISSION_REQUEST_CODE = 1;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;

    public boolean checkPermissionForRecord(Activity activity) {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkPermissionForExternalStorage(Context context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkPermissionReadExternalStorage(Context context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkPermissionForCamera(Context context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public void requestPermissionForRecord(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)) {
            Toast.makeText(activity, "Microphone permission needed for recording. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_PERMISSION_REQUEST_CODE);
        }
    }

    public static void requestPermissionForExternalStorage(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(activity, "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

    public static void requestPermissionForCamera(Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            Toast.makeText(activity, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }


    public static final int ACTION_CONFIRM = 0x555;

    public static String stringForLowercase(String inputVal) {
        // Empty strings should be returned as-is.

        if (inputVal.length() == 0) return "";

        // Strings with only one character uppercased.

        if (inputVal.length() == 1) return inputVal.toUpperCase();

        // Otherwise uppercase first letter, lowercase the rest.

        return inputVal.substring(0, 1).toUpperCase()
                + inputVal.substring(1).toLowerCase();
    }

    public static boolean verifySearchString(String search, String compare) {

        // remove all text accents
        String strSearch = Utils.removeStringAccents(search);
        String strCompare = Utils.removeStringAccents(compare);

        // Split search text
        String[] arrSearch = strSearch.split(" ");

        int totleCompareTimes = 0;

        for (int i = 0; i < arrSearch.length; i++) {
            if (strCompare.indexOf(arrSearch[i]) != -1) {
                totleCompareTimes++;
            }
        }


        if (totleCompareTimes == arrSearch.length) {
            return true;
        } else
            return false;
    }


    static HashMap<Character, Character> MAP_NORM = new HashMap<>();

    public static String removeStringAccents(String value) {

        if (MAP_NORM.size() == 0) {
            MAP_NORM.put('à', 'a');
            MAP_NORM.put('á', 'a');
            MAP_NORM.put('ạ', 'a');
            MAP_NORM.put('ả', 'a');
            MAP_NORM.put('ã', 'a');
            MAP_NORM.put('â', 'a');
            MAP_NORM.put('ầ', 'a');
            MAP_NORM.put('ấ', 'a');
            MAP_NORM.put('ậ', 'a');
            MAP_NORM.put('ẩ', 'a');
            MAP_NORM.put('ẫ', 'a');
            MAP_NORM.put('ă', 'a');
            MAP_NORM.put('ằ', 'a');
            MAP_NORM.put('ắ', 'a');
            MAP_NORM.put('ặ', 'a');
            MAP_NORM.put('ẳ', 'a');
            MAP_NORM.put('ẵ', 'a');
            MAP_NORM.put('ê', 'e');
            MAP_NORM.put('ề', 'e');
            MAP_NORM.put('ế', 'e');
            MAP_NORM.put('ệ', 'e');
            MAP_NORM.put('ể', 'e');
            MAP_NORM.put('ễ', 'e');
            MAP_NORM.put('è', 'e');
            MAP_NORM.put('é', 'e');
            MAP_NORM.put('ẹ', 'e');
            MAP_NORM.put('ẻ', 'e');
            MAP_NORM.put('ẽ', 'e');
            MAP_NORM.put('ì', 'i');
            MAP_NORM.put('í', 'i');
            MAP_NORM.put('ị', 'i');
            MAP_NORM.put('ỉ', 'i');
            MAP_NORM.put('ĩ', 'i');
            MAP_NORM.put('ò', 'o');
            MAP_NORM.put('ó', 'o');
            MAP_NORM.put('ọ', 'o');
            MAP_NORM.put('ỏ', 'o');
            MAP_NORM.put('õ', 'o');
            MAP_NORM.put('ô', 'o');
            MAP_NORM.put('ồ', 'o');
            MAP_NORM.put('ố', 'o');
            MAP_NORM.put('ộ', 'o');
            MAP_NORM.put('ổ', 'o');
            MAP_NORM.put('ỗ', 'o');
            MAP_NORM.put('ơ', 'o');
            MAP_NORM.put('ờ', 'o');
            MAP_NORM.put('ớ', 'o');
            MAP_NORM.put('ợ', 'o');
            MAP_NORM.put('ở', 'o');
            MAP_NORM.put('ỡ', 'o');
            MAP_NORM.put('ù', 'u');
            MAP_NORM.put('ú', 'u');
            MAP_NORM.put('ụ', 'u');
            MAP_NORM.put('ũ', 'u');
            MAP_NORM.put('ủ', 'u');
            MAP_NORM.put('ư', 'u');
            MAP_NORM.put('ừ', 'u');
            MAP_NORM.put('ứ', 'u');
            MAP_NORM.put('ự', 'u');
            MAP_NORM.put('ử', 'u');
            MAP_NORM.put('ữ', 'u');
            MAP_NORM.put('ỳ', 'y');
            MAP_NORM.put('ý', 'y');
            MAP_NORM.put('ỵ', 'y');
            MAP_NORM.put('ỷ', 'y');
            MAP_NORM.put('ỹ', 'y');
            MAP_NORM.put('đ', 'd');

        }

        if (value == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder(value);

        for (int i = 0; i < value.length(); i++) {
            Character c = MAP_NORM.get(sb.charAt(i));
            if (c != null) {
                sb.setCharAt(i, c.charValue());
            }
        }

        return sb.toString();
    }

    // Validating phone
    public static boolean isValidPhone(String phone) {
        String Name_PATTERN = "(\\+84|0)\\d{9,10}";
        Pattern pattern = Pattern.compile(Name_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    // Validating email
    public static boolean isValidEmail(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    // Validating email
    public static boolean isValidPassword(String password) {
        String ePattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(password);
        return m.matches();
    }

    public static boolean isValidPasswordSimple(String password) {
        String ePattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(password);
        return m.matches();
    }

    public static String getFacebookPageURL(Context context, String fanpage_id) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + "https://www.facebook.com/" + fanpage_id;
            } else { //older versions of fb app
                return "fb://page/" + fanpage_id;
            }
        } catch (NameNotFoundException e) {
            return "https://www.facebook.com/" + fanpage_id; //normal web url
        }
    }

    public static String getFacebookPostURL(Context context, String fanpage_id, String post_id) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + "https://www.facebook.com/" + post_id;
            } else { //older versions of fb app
                return "fb://post/" + post_id;
            }
        } catch (NameNotFoundException e) {
            return "https://www.facebook.com/" + post_id; //normal web url
        }
    }
}
