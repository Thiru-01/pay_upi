package org.skanda.upi.pay_upi.model;

public class UpiAppMeta {
    final String appName;
    final String packageName;
    final byte[] icon;

    public UpiAppMeta(String appName, String packageName, byte[] icon) {
        this.appName = appName;
        this.packageName = packageName;
        this.icon = icon;
    }
}
