package com.smsalert;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.*;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SendSms {
	public void addSslCertificate() throws NoSuchAlgorithmException, KeyManagementException {

		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

			}

		} };

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {

				return false;
			}
		};
		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		/*
		 * end of the fix
		 */

	}

	// declaring class variable
	static String api_key;
	static String username;
	static String password;

	static String sender;
	static String api_url;
	static String start;

	String time;
	String mob_no;
	String message;
	String unicode;
	String dlr_url;
	String type;
	String route;
	String authkey;

	// function to set sender id
	public static String setsender_id(String sid) {
		sender = sid;
		return sender;
	}

	// function to set api_key key
	public static String setapi_key(String apk) {
		// checking for valid working key
		api_key = apk;
		return api_key;
	}

	// function to set username 
	public static String setusername(String user) {
		// checking for valid working key
		username = user;
		return username;
	}

	// function to set password 
	public static String setpassword(String pwd) {
		// checking for valid working key
		password = pwd;
		return password;
	}

	// function to set Api url
	public static String setapi_url(String ap) {
		// checking for valid url format
		String check = ap;
		String str = check.substring(0, 7);
		String t = "http://";
		String s = "https:/";
		String st = "https://";
		if (str.equals(t)) {
			start = t;
			api_url = check.substring(7);
		} else if (check.substring(0, 8).equals(st)) {
			start = st;
			api_url = check.substring(8);
		} else if (str.equals(s)) {
			start = st;
			api_url = check.substring(7);
		} else {
			start = t;
			api_url = ap;
		}
		return api_url;
	}

	// function to set parameter import java.net.URLEncoder;
	public void setparams(String ap, String apk, String sd) {
		setapi_key(apk);
		setsender_id(sd);
		setapi_url(ap);
	}

	// function to set parameter import java.net.URLEncoder;
	public void authparams(String url,String user, String pwd) {
		setusername(user);
		setpassword(pwd);
		setapi_url(url);
	}

	/*
	 * function to send sms
	 * 
	 * @ Simple message : last two field are set to null
	 * 
	 * @ Unicode message :set unicode parameter to one
	 * 
	 * @ Scheduled message : give time in 'ddmmyyyyhhmm' format
	 */
	public String process_sms(String sender, String mob_no, String message, String route, String unicode, String time)
			throws IOException, MalformedURLException, KeyManagementException, NoSuchAlgorithmException, UnsupportedEncodingException {
		
		if (api_key != null)
			authkey ="&apikey=" + api_key;
		else
		    authkey ="&user=" + username + "&pwd=" + password;
		
		if(message != null && mob_no != null && sender != null){
			message = URLEncoder.encode(message, "UTF-8");
		if (unicode == null)
			unicode = "0";
		unicode = "&unicode=" + unicode;
		if (time == null)
			time = "";
		else
			time = "&time=" + URLEncoder.encode(time, "UTF-8");
		URL url = new URL("" + start + api_url+"/api/push.json?"+ authkey + "&sender=" + sender
				+ "&mobileno=" + mob_no + "&text=" + message + "&route=" + route + unicode + time);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		con.getOutputStream();
		con.getInputStream();
		BufferedReader rd;
		String line;
		String result = "";
		rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
		while ((line = rd.readLine()) != null) {
			result += line;
		}
		rd.close();
		return result;
	    }
		else
		return null;
		
	}

	public void send_sms(String sender,String mob_no, String message, String route)
			throws IOException, MalformedURLException, KeyManagementException, NoSuchAlgorithmException {
		process_sms(sender, mob_no, message, route,unicode = null, time = null);

	}

}
