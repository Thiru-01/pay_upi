package org.skanda.upi.pay_upi.core;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import org.skanda.upi.pay_upi.utils.IntentSigning;

import java.io.ByteArrayOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Upi {

    public static final int THIS_APP_REQUEST_CODE = 920;

    public List<Map<String, Object>> getInstalledUpiApps(Context context, List<String> listOfUpiApps) {
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> upiApps = new ArrayList<>();

        for (String upiApp : listOfUpiApps) {
            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(upiApp, 0);
                upiApps.add(applicationInfo);
            } catch (PackageManager.NameNotFoundException e) {
            }
        }

        List<Map<String, Object>> listOfUpi = new ArrayList<>();

        for (ApplicationInfo packageInfo : upiApps) {
            Drawable appIcon = null;
            try {
                appIcon = packageManager.getApplicationIcon(packageInfo.packageName);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("UPI", "Error getting icon for " + packageInfo.packageName);
            }

            HashMap<String, Object> upiData = new HashMap<>();
            upiData.put("appName", packageManager.getApplicationLabel(packageInfo).toString());
            upiData.put("packageName", packageInfo.packageName);
            upiData.put("icon", this.iconToByteArray(appIcon));
            listOfUpi.add(upiData);
        }

        return listOfUpi;
    }

    public List<Map<String, Object>> fetchUpiApps(Activity currentActivity) {
        List<Map<String, Object>> result = new ArrayList<>();

        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean initiateTransaction(Map<String, String> request, Activity activity) {

        String appName = request.get("app");
        String pa = request.get("pa");
        String pn = request.get("pn");
        String tid = request.get("tid");
        String tr = request.get("tr");
        String am = request.get("am");
        String cu = "INR";

        Uri.Builder upiUri = new Uri.Builder();
        upiUri.scheme("paytmmp").authority("pay");
        upiUri.appendQueryParameter("pa", pa);
        upiUri.appendQueryParameter("pn", pn);
        //upiUri.appendQueryParameter("aid", "uGICAgiC94_q4Wg");
        upiUri.appendQueryParameter("mc", "");
//        upiUri.appendQueryParameter("tr", tr);
        upiUri.appendQueryParameter("am", String.valueOf(am));
        upiUri.appendQueryParameter("cu", cu);
//        upiUri.appendQueryParameter("tn", "");
//        upiUri.appendQueryParameter("tid", tid);
        upiUri.appendQueryParameter("mode", "00"); // QR code

        Intent intent = new Intent(Intent.ACTION_VIEW, upiUri.build());
        intent.setPackage(appName);

        if(intent.resolveActivity(activity.getPackageManager()) == null) {
            return false;
        }

        activity.startActivityForResult(intent, THIS_APP_REQUEST_CODE);

        return true;
    }

    private byte[] iconToByteArray(Drawable drawable) {
        if (drawable == null) {
            return new byte[0];
        }
        Bitmap iconMap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(iconMap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        iconMap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }
}
