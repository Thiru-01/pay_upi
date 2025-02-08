package org.skanda.upi.pay_upi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import org.jetbrains.annotations.NotNull;
import org.skanda.upi.pay_upi.core.Upi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * PayUpiPlugin
 */
public class PayUpiPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware, PluginRegistry.ActivityResultListener {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;
    private EventChannel.EventSink eventSink = null;
    private Result result = null;
    private Activity currentActivity;

    final private List<String> _queue = new ArrayList<>();

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "pay_upi");
        channel.setMethodCallHandler(this);

//        new EventChannel(Objects.requireNonNull(flutterPluginBinding.getBinaryMessenger()), "pay_upi_event").setStreamHandler(
//                new EventChannel.StreamHandler() {
//                    @Override
//                    public void onListen(Object arguments, EventChannel.EventSink events) {
//                        eventSink = events;
//                        if(!_queue.isEmpty()) {
//                            for (String response : _queue) {
//                                eventSink.success(response);
//                            }
//                            _queue.clear();
//                        }
//                    }
//
//                    @Override
//                    public void onCancel(Object arguments) {
//                        eventSink = null;
//                        _queue.clear();
//                    }
//                }
//        );
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (this.result == null) {
            this.result = result;
        }

        Upi upiService = new Upi();

        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals("getUpiApps")) {
            if (call.arguments instanceof List) {
                List upiApps = (List) call.arguments;
                if (upiApps.get(0) instanceof String) {
                    List<String> apps = (List<String>) call.arguments;
                    result.success(upiService.getInstalledUpiApps(currentActivity.getApplicationContext(), apps));
                    return;
                }
            }
            result.error("INVALID_ARGUMENTS", "UPI Apps package name required", null);
        } else if (call.method.equals("initiateTransaction")) {
            if (call.arguments instanceof Map) {
                Map<String, String> req = (Map<String, String>) call.arguments;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    boolean resultStatus = upiService.initiateTransaction(req, currentActivity);
                    if (!resultStatus) {
                        result.error("TRANSACTION_FAILED", "Transaction failed", null);
                    }
                    return;
                }

                result.error("UNSUPPORTED_VERSION", "Android version not supported", null);
            }
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    @Override
    public void onAttachedToActivity(@NonNull @NotNull ActivityPluginBinding binding) {
        this.currentActivity = binding.getActivity();
        Log.d("PayUpiPlugin", "Activity attached: " + this.currentActivity.getLocalClassName());
        binding.addActivityResultListener(this);

//        Intent upiIntent = this.currentActivity.getIntent();
//        Uri data = upiIntent.getData();
//        if (data != null) {
//            String response = data.toString();
//            Log.d("PayUpiPlugin", "UPI Response: " + response);
//            _queue.add("UPI Response: " + response);
//
//        }
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {

    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull @NotNull ActivityPluginBinding binding) {
        this.currentActivity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivity() {

    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (requestCode == Upi.THIS_APP_REQUEST_CODE && data != null) {
            String result = data.getStringExtra("response");
        }
        return true;
    }
}
