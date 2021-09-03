package com.haoyigou.hyg.base;

import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.security.cert.CertificateParsingException;
import javax.security.cert.X509Certificate;

/**
 * Created by Witness on 2020-04-28
 * Describe:
 */
public class GeneralHostnameVerifier implements HostnameVerifier {

    private static final int ALT_DNS_NAME = 2;
    private static final int ALT_IPA_NAME = 7;

    public static GeneralHostnameVerifier getInstance(String baseHostname, InputStream... iss) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            List<Collection<? extends Certificate>> list = new ArrayList<>();
            for (InputStream is : iss) {
                list.add(certificateFactory.generateCertificates(is));
            }
            if (list.isEmpty()) {
                throw new IllegalArgumentException("expected non-empty set of trusted certificates");
            }
            return new GeneralHostnameVerifier(baseHostname, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Put the certificates a key store.
        return null;
    }

    /**
     * 公钥列表
     */
    private List<String> clientPublicKey = new ArrayList<>();

    private String baseHostname;

    public GeneralHostnameVerifier(String baseHostname, List<Collection<? extends Certificate>> list) {
        this.baseHostname = baseHostname;
        for (Collection<? extends Certificate> certificates : list) {
            for (Certificate certificate : certificates) {
                clientPublicKey.add(certificate.getPublicKey().toString());
            }
        }
    }

    @Override
    public boolean verify(String hostname, SSLSession sslSession) {
        // 如果不是我们自己的域名就走用默认的验证方式，不验证公钥
        if (!hostname.contains(baseHostname)) {
            return defaultVerify(hostname, sslSession);
        }
        try {
            // 从 SSLSession 获取请求的公钥信息
            List<String> pubKeys = new ArrayList<>();
            for (X509Certificate certificate : sslSession.getPeerCertificateChain()) {
                pubKeys.add(certificate.getPublicKey().toString());
            }
            // 验证请求的 SSL 证书是否合法（我们认可的证书），才继续验证，否则返回失败
            if (comparePubKey(pubKeys)) {
                return defaultVerify(hostname, sslSession);
            }
            return false;
        } catch (SSLPeerUnverifiedException e) {
            e.printStackTrace();
        }
        return defaultVerify(hostname, sslSession);
    }

    private boolean comparePubKey(List<String> servicePubKeys) throws SSLPeerUnverifiedException {
        if (servicePubKeys == null || servicePubKeys.size() <= 0) {
            return false;
        }
        if (clientPublicKey == null || clientPublicKey.size() <= 0) {
            throw new SSLPeerUnverifiedException("clientPublicKey null");
        }
        for (String key : servicePubKeys) {
            if (clientPublicKey.contains(key)) {
                return true;
            }
        }
        return false;

    }


    public boolean defaultVerify(String hostname, SSLSession session) {
        try {
            Certificate[] certificates = session.getPeerCertificates();
            return verify(hostname, (java.security.cert.X509Certificate) certificates[0]);
        } catch (SSLException e) {
            return false;
        }
    }

    public boolean verify(String hostname, java.security.cert.X509Certificate certificate) {
        return verifyAsIpAddress(hostname) ? verifyIpAddress(hostname, certificate) : verifyHostname(hostname, certificate);
    }


    /**
     * Returns true if {@code certificate} matches {@code ipAddress}.
     */
    private boolean verifyIpAddress(String ipAddress, java.security.cert.X509Certificate certificate) {
        List<String> altNames = getSubjectAltNames(certificate, ALT_IPA_NAME);
        for (int i = 0, size = altNames.size(); i < size; i++) {
            if (ipAddress.equalsIgnoreCase(altNames.get(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if {@code certificate} matches {@code hostname}.
     */
    private boolean verifyHostname(String hostname, java.security.cert.X509Certificate certificate) {
        hostname = hostname.toLowerCase(Locale.US);
        List<String> altNames = getSubjectAltNames(certificate, ALT_DNS_NAME);
        for (String altName : altNames) {
            if (verifyHostname(hostname, altName)) {
                return true;
            }
        }
        return false;
    }

    public static List<String> allSubjectAltNames(java.security.cert.X509Certificate certificate) {
        List<String> altIpaNames = getSubjectAltNames(certificate, ALT_IPA_NAME);
        List<String> altDnsNames = getSubjectAltNames(certificate, ALT_DNS_NAME);
        List<String> result = new ArrayList<>(altIpaNames.size() + altDnsNames.size());
        result.addAll(altIpaNames);
        result.addAll(altDnsNames);
        return result;
    }

    private static List<String> getSubjectAltNames(java.security.cert.X509Certificate certificate, int type) {
        List<String> result = new ArrayList<>();
        try {
            Collection<?> subjectAltNames = certificate.getSubjectAlternativeNames();
            if (subjectAltNames == null) {
                return Collections.emptyList();
            }
            for (Object subjectAltName : subjectAltNames) {
                List<?> entry = (List<?>) subjectAltName;
                if (entry == null || entry.size() < 2) {
                    continue;
                }
                Integer altNameType = (Integer) entry.get(0);
                if (altNameType == null) {
                    continue;
                }
                if (altNameType == type) {
                    String altName = (String) entry.get(1);
                    if (altName != null) {
                        result.add(altName);
                    }
                }
            }
            return result;
        } catch (java.security.cert.CertificateParsingException e) {
            return Collections.emptyList();
        }
    }

    /**
     * Returns {@code true} iff {@code hostname} matches the domain name {@code pattern}.
     *
     * @param hostname lower-case host name.
     * @param pattern  domain name pattern from certificate. May be a wildcard pattern such as {@code
     *                 *.android.com}.
     */
    public boolean verifyHostname(String hostname, String pattern) {
        // Basic sanity checks
        // Check length == 0 instead of .isEmpty() to support Java 5.
        if ((hostname == null) || (hostname.length() == 0) || (hostname.startsWith("."))
                || (hostname.endsWith(".."))) {
            // Invalid domain name
            return false;
        }
        if ((pattern == null) || (pattern.length() == 0) || (pattern.startsWith("."))
                || (pattern.endsWith(".."))) {
            // Invalid pattern/domain name
            return false;
        }

        // Normalize hostname and pattern by turning them into absolute domain names if they are not
        // yet absolute. This is needed because server certificates do not normally contain absolute
        // names or patterns, but they should be treated as absolute. At the same time, any hostname
        // presented to this method should also be treated as absolute for the purposes of matching
        // to the server certificate.
        //   www.android.com  matches www.android.com
        //   www.android.com  matches www.android.com.
        //   www.android.com. matches www.android.com.
        //   www.android.com. matches www.android.com
        if (!hostname.endsWith(".")) {
            hostname += '.';
        }
        if (!pattern.endsWith(".")) {
            pattern += '.';
        }
        // hostname and pattern are now absolute domain names.

        pattern = pattern.toLowerCase(Locale.US);
        // hostname and pattern are now in lower case -- domain names are case-insensitive.

        if (!pattern.contains("*")) {
            // Not a wildcard pattern -- hostname and pattern must match exactly.
            return hostname.equals(pattern);
        }
        // Wildcard pattern

        // WILDCARD PATTERN RULES:
        // 1. Asterisk (*) is only permitted in the left-most domain name label and must be the
        //    only character in that label (i.e., must match the whole left-most label).
        //    For example, *.example.com is permitted, while *a.example.com, a*.example.com,
        //    a*b.example.com, a.*.example.com are not permitted.
        // 2. Asterisk (*) cannot match across domain name labels.
        //    For example, *.example.com matches test.example.com but does not match
        //    sub.test.example.com.
        // 3. Wildcard patterns for single-label domain names are not permitted.

        if ((!pattern.startsWith("*.")) || (pattern.indexOf('*', 1) != -1)) {
            // Asterisk (*) is only permitted in the left-most domain name label and must be the only
            // character in that label
            return false;
        }

        // Optimization: check whether hostname is too short to match the pattern. hostName must be at
        // least as long as the pattern because asterisk must match the whole left-most label and
        // hostname starts with a non-empty label. Thus, asterisk has to match one or more characters.
        if (hostname.length() < pattern.length()) {
            // hostname too short to match the pattern.
            return false;
        }

        if ("*.".equals(pattern)) {
            // Wildcard pattern for single-label domain name -- not permitted.
            return false;
        }

        // hostname must end with the region of pattern following the asterisk.
        String suffix = pattern.substring(1);
        if (!hostname.endsWith(suffix)) {
            // hostname does not end with the suffix
            return false;
        }

        // Check that asterisk did not match across domain name labels.
        int suffixStartIndexInHostname = hostname.length() - suffix.length();
        if ((suffixStartIndexInHostname > 0)
                && (hostname.lastIndexOf('.', suffixStartIndexInHostname - 1) != -1)) {
            // Asterisk is matching across domain name labels -- not permitted.
            return false;
        }

        // hostname matches pattern
        return true;
    }
    private static final Pattern VERIFY_AS_IP_ADDRESS = Pattern.compile(
            "([0-9a-fA-F]*:[0-9a-fA-F:.]*)|([\\d.]+)");
    /** Returns true if {@code host} is not a host name and might be an IP address. */
    public static boolean verifyAsIpAddress(String host) {
        return VERIFY_AS_IP_ADDRESS.matcher(host).matches();
    }

}
