package com.haoyigou.hyg.common.http;

import android.content.Context;

import com.haoyigou.hyg.application.MApplication;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;

/**
 * Created by witness on 2018/11/13.
 *
 */

public class SSLSocketClient {

    //获取这个SSLSocketFactory
    public static SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, getTrustManager(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //获取TrustManager
    private static TrustManager[] getTrustManager() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        if (chain == null) {
                            try {
                                throw new CertificateException("checkServerTrusted: X509Certificate array is null");
                            } catch (CertificateException e) {
                                e.printStackTrace();
                            }
                        }
                        if (chain.length < 1) {
                            try {
                                throw new CertificateException("checkServerTrusted: X509Certificate is empty");
                            } catch (CertificateException e) {
                                e.printStackTrace();
                            }
                        }
                        if (!(null != authType && authType.equals("ECDHE_RSA"))) {
                            try {
                                throw new CertificateException("checkServerTrusted: AuthType is not ECDHE_RSA");
                            } catch (CertificateException e) {
                                e.printStackTrace();
                            }
                        }

                        //检查所有证书
                        try {
                            TrustManagerFactory factory = TrustManagerFactory.getInstance("X509");
                            factory.init((KeyStore) null);
                            for (TrustManager trustManager : factory.getTrustManagers()) {
                                ((X509TrustManager) trustManager).checkServerTrusted(chain, authType);
                            }
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (java.security.cert.CertificateException e) {
                            e.printStackTrace();
                        } catch (KeyStoreException e) {
                            e.printStackTrace();
                        }

                        //获取本地证书中的信息
                        String clientEncoded = "";
                        String clientSubject = "";
                        String clientIssUser = "";
                        try {
                            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                            InputStream inputStream = MApplication.gainContext().getAssets().open("hyg1.cer");
                            X509Certificate clientCertificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
                            clientEncoded = new BigInteger(1, clientCertificate.getPublicKey().getEncoded()).toString(16);
                            clientSubject = clientCertificate.getSubjectDN().getName();
                            clientIssUser = clientCertificate.getIssuerDN().getName();
                        } catch (IOException | java.security.cert.CertificateException e) {
                            e.printStackTrace();
                        }

                        //获取网络中的证书信息
                        X509Certificate certificate = chain[0];
                        PublicKey publicKey = certificate.getPublicKey();
                        String serverEncoded = new BigInteger(1, publicKey.getEncoded()).toString(16);

                        if (!clientEncoded.equals(serverEncoded)) {
                            try {
                                throw new CertificateException("server's PublicKey is not equals to client's PublicKey");
                            } catch (CertificateException e) {
                                e.printStackTrace();
                            }
                        }
                        String subject = certificate.getSubjectDN().getName();
                        if (!clientSubject.equals(subject)) {
                            try {
                                throw new CertificateException("server's subject is not equals to client's subject");
                            } catch (CertificateException e) {
                                e.printStackTrace();
                            }
                        }
                        String issuser = certificate.getIssuerDN().getName();
                        if (!clientIssUser.equals(issuser)) {
                            try {
                                throw new CertificateException("server's issuser is not equals to client's issuser");
                            } catch (CertificateException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };
        return trustAllCerts;
    }

    //获取HostnameVerifier
    public static HostnameVerifier getHostnameVerifier() {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
        return hostnameVerifier;
    }
}
