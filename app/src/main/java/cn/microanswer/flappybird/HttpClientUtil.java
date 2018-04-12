package cn.microanswer.flappybird;

import android.text.TextUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class HttpClientUtil {

    /**
     *
     */

    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final String METHOD_POST = "POST";

    public static final int DEFAULT_CONNECTION_TIMEOUT = 120 * 1000;

    public static final int DEFAULT_SOCKET_READ_TIMEOUT = 120 * 1000;

    public static String doPost(String url, Map<String, Object> params) throws Exception {
        return doPost(url, concatParameters(params));
    }

    /**
     * post JSON add by 张彬 2017年3月1日10:51:01
     *
     * @param url
     * @param content
     * @return
     * @throws Exception
     */
    public static String postJson(String url, String content, Map<String, String> headParams) throws Exception {
        if (headParams == null) {
            headParams = new HashMap<>();
        }
        if (!headParams.containsKey("Content-Type")) {
            headParams.put("Content-Type", "application/json;charset=UTF-8");
        }
        return doPost(url, content, headParams);
    }

    public static String post(String url, String method, JSONObject data) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("method", method);
        jsonObject.put("data", data);


        TreeMap<String, String> headers = new TreeMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");

        return doPost(url, jsonObject.toString(), headers);
    }

    public static String doPost(String url, String params) throws Exception {
        return doPost(url, params, null);
    }

    public static String concatParameters(Map<String, Object> params) throws Exception {
        return concatParameters(params, true);
    }

    public static String concatParameters(Map<String, Object> params, Boolean isEncode) throws Exception {
        StringBuilder sb = new StringBuilder();
        int p = 0;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() != null) {
                if (p++ > 0) {
                    sb.append('&');
                }

                if (isEncode) {
                    sb.append(entry.getKey()).append('=').append(encodeURI(entry.getValue().toString()));
                } else {
                    sb.append(entry.getKey()).append('=').append(entry.getValue().toString());
                }

            }
        }
        return sb.toString();
    }

    public static String encodeURI(String param) {
        if (param == null) {
            return "";
        }
        try {
            return URLEncoder.encode(param, DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String doPost(String url, String params, Map<String, String> headParams) throws Exception {
        System.out.println("请求网络：" + params);
        byte[] content = {};
        if (params != null) {
            content = params.getBytes(DEFAULT_CHARSET);
        }
        return doPost(url, content, headParams);
    }

    public static String doPost(String url, byte[] content, Map<String, String> headParams) throws Exception {
        HttpURLConnection conn = null;
        OutputStream out = null;
        String rsp = null;

        try {
            try {
                conn = getConnection(url, METHOD_POST);
                if (headParams == null) {
                    headParams = new HashMap<>();
                }
                if (!headParams.containsKey("Content-Type")) {
                    headParams.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                }
                for (String key : headParams.keySet()) {
                    conn.setRequestProperty(key, headParams.get(key));
                }
                conn.setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT);
                conn.setReadTimeout(DEFAULT_SOCKET_READ_TIMEOUT);

            } catch (Exception e) {
                throw e;
            }
            try {
                out = conn.getOutputStream();
                out.write(content);
                rsp = getResponseAsString(conn);
            } catch (IOException e) {
                throw e;
            }

        } finally {
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rsp;
    }

    private static class DefaultTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

    }

    private static HttpURLConnection getConnection(String url, String method) throws Exception {
        if (url.toLowerCase().startsWith("https")) {
            // SSLContext ctx = SSLContext.getInstance("TLS");
            // ctx.init(new KeyManager[0], new TrustManager[] { new
            // DefaultTrustManager() }, new SecureRandom());
            // SSLContext.setDefault(ctx);
            // ((HttpsURLConnection) conn).setHostnameVerifier(new
            // HostnameVerifier() {
            // @Override
            // public boolean verify(String hostname, SSLSession session) {
            // return true;
            // }
            // });
            trustAllHttpsCertificates();
            HttpsURLConnection.setDefaultHostnameVerifier(hv);

        }
        HttpURLConnection conn = (HttpURLConnection) (new URL(url).openConnection());
        conn.setRequestMethod(method);
        conn.setInstanceFollowRedirects(true);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        // conn.setRequestProperty("Accept",
        // "text/xml,text/javascript,text/html");
        // conn.setRequestProperty("User-Agent", "stargate");
        return conn;
    }

    static HostnameVerifier hv = new HostnameVerifier() {
        public boolean verify(String urlHostName, SSLSession session) {
            System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
            return true;
        }
    };

    static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }

    private static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    protected static String getResponseAsString(HttpURLConnection conn) throws IOException {
        String charset = getResponseCharset(conn.getContentType());
        InputStream es = conn.getErrorStream();
        if (es == null) {
            return getStreamAsString(conn.getInputStream(), charset);
        } else {
            String msg = getStreamAsString(es, charset);
            if (TextUtils.isEmpty(msg)) {
                throw new IOException(conn.getResponseCode() + ":" + conn.getResponseMessage());
            } else {
                throw new IOException(msg);
            }
        }
    }

    private static String getStreamAsString(InputStream stream, String charset) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
            StringWriter writer = new StringWriter();

            char[] chars = new char[256];
            int count = 0;
            while ((count = reader.read(chars)) > 0) {
                writer.write(chars, 0, count);
            }

            return writer.toString();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    private static String getResponseCharset(String ctype) {
        String charset = DEFAULT_CHARSET;

        if (!TextUtils.isEmpty(ctype)) {
            String[] params = ctype.split(";");
            for (String param : params) {
                param = param.trim();
                if (param.startsWith("charset")) {
                    String[] pair = param.split("=", 2);
                    if (pair.length == 2) {
                        if (!TextUtils.isEmpty(pair[1])) {
                            charset = pair[1].trim();
                        }
                    }
                    break;
                }
            }
        }

        return charset;
    }
}