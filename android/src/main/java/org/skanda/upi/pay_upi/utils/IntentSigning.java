package org.skanda.upi.pay_upi.utils;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Base64;

public class IntentSigning {

    protected KeyPair generateKeyPair() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(new RSAKeyGenParameterSpec(512, RSAKeyGenParameterSpec.F4));
        return keyPairGenerator.generateKeyPair();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected String generateSignature(String url, PrivateKey key) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(key);

        String data = url.split("&sign=")[0];
        signature.update(data.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(signature.sign());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String singIntent(String url) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        KeyPair signingKeyPair = this.generateKeyPair();
        String orgId = "000000";

        url += "&orgId=" + orgId;

        String signature = this.generateSignature(url, signingKeyPair.getPrivate());

        url += "&sign=" + signature;
        return url;
    }


}
