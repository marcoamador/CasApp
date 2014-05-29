package com.casapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;

import data.objects.AppException;
import android.app.Application;
import android.content.Context;
import android.util.Log;

public class CasApp extends Application{
	public static final String PREFS_NAME = "MyPrefsFile";    //unique identifier for our Preferences
	public static final String PREF_USERNAME = "username"; //fields to be saved
	public static final String PREF_PASSWORD = "password";
	public static final String PREF_DATELOGIN = "dateLogin";	
	public static final String PREF_CHECKIN = "checkin";
	public static final String PREF_AUTHTOKEN = "authToken";
	public static final String PREF_LATITUDE = "latitude";
	public static final String PREF_LONGITUDE = "longitude";
	public static final String PREF_POINTS = "points";
	public static final String PREF_FEEDBACK_POINTS = "feedbackPoints";
	public static final int CONNECTION_TIMEOUT = 20000;
	public static final int SO_TIMEOUT = 20000;
	public static final int TIMEOUT = 20000;
	private static Charset charset = Charset.forName("UTF-8");
	
	
	static Gson gson = new Gson();
	
	//Removes undesirable characters from the Web Service answer
	public static String cleanResponseString(String j) {
		String s = j.replaceAll("\\\\\"", "\"").replaceAll("\\\\/", "/");
		//To remove the first and last "
		return s.substring(1,s.length()-1);
	}
		
	//HTTP GET request
		@SuppressWarnings("rawtypes")
		public static <T> T doGet(Context cont, String uriLeft, HashMap<String,Object> headers, Type T) throws Exception {
			//configs
			HttpParams httpParameters = new BasicHttpParams();
			//the time to wait for a connection to establish
			HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
			//the time to wait for data to be received
			HttpConnectionParams.setSoTimeout(httpParameters, SO_TIMEOUT);
			HttpConnectionParams.setTcpNoDelay(httpParameters, true);

			httpParameters.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
			httpParameters.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
			httpParameters.setLongParameter(ConnManagerPNames.TIMEOUT, TIMEOUT);
			//-----------
			
			
			
			HttpGet httpget = new HttpGet(cont.getString(R.string.urixml) + uriLeft);
			
			
			//Adds headers
			Iterator<Entry<String, Object>> itr = headers.entrySet().iterator();
			while(itr.hasNext()) {
				Map.Entry pairs = (Map.Entry) itr.next();
				httpget.setHeader(pairs.getKey().toString(), pairs.getValue().toString());
				Log.d("headers", pairs.getKey().toString() + pairs.getValue().toString());
			}
			
			
			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			//garbage collector pode ser activo se android precisar de memoria
			/*if (httpclient == null) {
				httpclient = new DefaultHttpClient(httpParameters);
			}*/
			
			Log.d("DOGET", "Before execute= " + cont.getString(R.string.urixml) + uriLeft);
			HttpResponse response;
			httpget.setHeader("Content-Type", "application/json");
			HttpEntity resEntityGet = null;
			String output = "";
			try {
				//faz pedido e obtem resposta
				response = httpclient.execute(httpget);
				resEntityGet = response.getEntity();
			}
			catch(SocketException e) {
				Log.d("DOGET", "Exception SocketException= " + e.getMessage());
				Log.d("DOGET", "Exception Class= " + e.getClass().toString());
				throw new Exception("Request Timeout");
			}
			catch(ConnectTimeoutException e) {
				Log.d("DOGET", "Exception ConnectTimeoutException= " + e.getMessage());
				Log.d("DOGET", "Exception Class= " + e.getClass().toString());
				throw new Exception("Request Timeout");
			}
			catch(Exception e) {
				Log.d("DOGET", "Exception Anormal= " + e.getMessage());
				Log.d("DOGET", "Exception Class= " + e.getClass().toString());
				throw new Exception("Request Timeout");
			}		
			
			Log.d("DOGET", "After execute");
			
			//Treat the webservice exceptions
			if(response.getStatusLine().getStatusCode() != 201 && response.getStatusLine().getStatusCode() != 200) {
				Exception exc = null;
				
				switch(response.getStatusLine().getStatusCode()) {
					case 500://InternalServerError 
					{
						AppException appExc = new AppException();
						if (resEntityGet != null) {  
							/*InputStreamReader isr = new InputStreamReader(response.getEntity().getContent(), charset);
							
							BufferedReader res = new BufferedReader(isr, 8192);
							String resposta = res.readLine();*/
							output = EntityUtils.toString(resEntityGet);
							Log.d("DOGET", "Exception resposta= " + output);
							appExc = (AppException)gson.fromJson(output, AppException.class);
						}
						exc = new Exception(appExc.getDescription());
						break;
					}
					default:
					{
						Log.d("DOGET", "Exception default" + response.getStatusLine().getStatusCode());
						Log.d("DOGET", "cenas" + response.getStatusLine().getReasonPhrase());
						exc = new Exception("Error");
						break;
					}				
				}
				throw exc;
			}		
			
			//se obtiver resposta
			if (resEntityGet != null) {  
				
				if(response.getEntity().getContentLength() != 0){
					InputStreamReader isr = new InputStreamReader(response.getEntity().getContent(), charset);
					BufferedReader res = new BufferedReader(isr, 8192);
					String resposta = res.readLine();
					
					
					/*ByteArrayOutputStream out = new ByteArrayOutputStream();
		            response.getEntity().writeTo(out);
		            out.close();
		            String responseString = out.toString();*/
					
					Log.d("DOGET", "Resposta= " +  resposta);
					return gson.fromJson(cleanResponseString(resposta), T);
					
				}
				else{
					Log.d("DOGET", "Resposta vazia= " + response.getEntity().getContentLength());
					return null;
				}
			}
			
			//no caso de nao obter resposta do WS
			return null;
		}
		
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public static <T> T doPut(Context cont, String uriLeft, HashMap<String,Object> headers, Type T, Object params) throws Exception{
			//configs
			HttpParams httpParameters = new BasicHttpParams();
			//the time to wait for a connection to establish
			HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
			//the time to wait for data to be received
			HttpConnectionParams.setSoTimeout(httpParameters, SO_TIMEOUT);
			HttpConnectionParams.setTcpNoDelay(httpParameters, true);
			
			httpParameters.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
			httpParameters.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
			httpParameters.setLongParameter(ConnManagerPNames.TIMEOUT, TIMEOUT);		
			//-----------

			HttpPut httpput = new HttpPut(cont.getString(R.string.urixml) + uriLeft);
			
			Log.d("DOPUT", "Uri= " + cont.getString(R.string.urixml) + uriLeft);
			
			//Request parameters
			String payload = gson.toJson(((T)params));

			//Properties of the http request
			StringEntity se = new StringEntity(payload, "UTF-8");
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			httpput.setEntity(se);
			httpput.setHeader("Accept", "application/json");
			httpput.setHeader("Content-Type", "application/json");
			httpput.setHeader("Content-Language", "en-US");
			//Adds headers
			Iterator<Entry<String, Object>> itr = headers.entrySet().iterator();
			while(itr.hasNext()) {
				Map.Entry pairs = (Map.Entry) itr.next();
				httpput.setHeader(pairs.getKey().toString(), pairs.getValue().toString());
			}
			
			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			
			//Garbage collector
			/*if (httpclient == null) {
				httpclient = new DefaultHttpClient(httpParameters);
			}*/
			
			HttpResponse response = null;
			HttpEntity resEntityGet = null;
			try {
				//Executes request
				response = httpclient.execute(httpput);
				resEntityGet = response.getEntity();
			}
			catch(SocketException e) {
				Log.d("DOPUT", "Exception SocketException= " + e.getMessage());
				Log.d("DOPUT", "Exception Class= " + e.getClass().toString());
				throw new Exception("Request Timeout");
			}
			catch(ConnectTimeoutException e) {
				Log.d("DOPUT", "Exception ConnectTimeoutException= " + e.getMessage());
				Log.d("DOPUT", "Exception Class= " + e.getClass().toString());
				throw new Exception("Request Timeout");
			}
			catch(Exception e) {
				Log.d("DOPUT", "Exception Anormal= " + e.getMessage());
				Log.d("DOPUT", "Exception Class= " + e.getClass().toString());
				throw new Exception("Request Timeout");
			}				
			
			Log.d("DOPUT", "Status code= " + response.getStatusLine().getStatusCode());
			if(response.getStatusLine().getStatusCode() != 201 && response.getStatusLine().getStatusCode() != 200) {
				Exception exc = null;
				
				switch(response.getStatusLine().getStatusCode()) {
				case 500://InternalServerError 
				{
					AppException appExc = new AppException();
					if (resEntityGet != null) {  
						InputStreamReader isr = new InputStreamReader(response.getEntity().getContent(), charset);
						BufferedReader res = new BufferedReader(isr, 8192);
						String resposta = res.readLine();
						Log.d("DOPUT", "Exception resposta= " + resposta);
						appExc = (AppException)gson.fromJson(resposta, AppException.class);
					}
					exc = new Exception(appExc.getDescription());
					break;
				}
				default:
				{
					Log.d("DOPUT", "Exception default");
					exc = new Exception("Error");
					break;
				}				
			}
				throw exc;
			}			
			
			//If there is an answer
			if (resEntityGet != null) {			
				InputStreamReader isr = new InputStreamReader(response.getEntity().getContent(), charset);
				BufferedReader res = new BufferedReader(isr, 8192);
				String resposta = res.readLine();
				Log.d("DOPUT", "Resposta= " + resposta);
				return gson.fromJson(cleanResponseString(resposta), T);			
			} 
			
			return null;
		}
		
		
		
		//HTTP POST request
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public static <T> T doPost(Context cont, String uriLeft, HashMap<String,Object> headers, Type T, Object params) throws Exception {
			//configs
			HttpParams httpParameters = new BasicHttpParams();
			//the time to wait for a connection to establish
			HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
			//the time to wait for data to be received
			HttpConnectionParams.setSoTimeout(httpParameters, SO_TIMEOUT);
			HttpConnectionParams.setTcpNoDelay(httpParameters, true);
			
			httpParameters.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
			httpParameters.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
			httpParameters.setLongParameter(ConnManagerPNames.TIMEOUT, TIMEOUT);		
			//-----------
			
			//post
			//HttpPost httppost = new HttpPost(uri + uriLeft);
			HttpPost httppost = new HttpPost(cont.getString(R.string.urixml) + uriLeft);


			//Request parameters
			String payload = gson.toJson(((T)params));

			//propriedades do pedido http
			StringEntity se = new StringEntity(payload, "UTF-8");
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			httppost.setEntity(se);
			httppost.setHeader("Accept", "application/json");
			httppost.setHeader("Content-Type", "application/json");
			httppost.setHeader("Content-Language", "en-US");
			//Adds headers
			Iterator<Entry<String, Object>> itr = headers.entrySet().iterator();
			while(itr.hasNext()) {
				Map.Entry pairs = (Map.Entry) itr.next();
				httppost.setHeader(pairs.getKey().toString(), pairs.getValue().toString());
			}
			
			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			//garbage collector pode ser activo se android precisar de memoria
			/*if (httpclient == null) {
				httpclient = new DefaultHttpClient(httpParameters);
			}*/
			
			HttpResponse response = null;
			HttpEntity resEntityGet = null;		
			
			try {
				//faz pedido e obtem resposta
				response = httpclient.execute(httppost);
				resEntityGet = response.getEntity();
			}
			catch(SocketException e) {
				Log.d("DOPOST", "Exception SocketException= " + e.getMessage());
				Log.d("DOPOST", "Exception Class= " + e.getClass().toString());
				throw new Exception("Request Timeout");
			}
			catch(ConnectTimeoutException e) {
				Log.d("DOPOST", "Exception ConnectTimeoutException= " + e.getMessage());
				Log.d("DOPOST", "Exception Class= " + e.getClass().toString());
				throw new Exception("Request Timeout");
			}
			catch(Exception e) {
				Log.d("DOPOST", "Exception Anormal= " + e.getMessage());
				Log.d("DOPOST", "Exception Class= " + e.getClass().toString());
				throw new Exception("Request Timeout");
			}			
			
			if(response.getStatusLine().getStatusCode() != 201 && response.getStatusLine().getStatusCode() != 200) {
				Exception exc = null;

				switch(response.getStatusLine().getStatusCode()) {
				case 500://InternalServerError 
				{
					AppException appExc = new AppException();
					if (resEntityGet != null) {  
						InputStreamReader isr = new InputStreamReader(response.getEntity().getContent(), charset);
						BufferedReader res = new BufferedReader(isr, 8192);
						String resposta = res.readLine();
						Log.d("DOPOST", "Exception resposta= " + resposta);
						appExc = (AppException)gson.fromJson(resposta, AppException.class);
					}
					exc = new Exception(appExc.getDescription());
					break;
				}
				default:
				{
					Log.d("DOPOST", "Exception default");
					exc = new Exception("Error");
					break;
				}				
			}
				throw exc;
			}
			
			//se obtiver resposta
			if (resEntityGet != null) {
				InputStreamReader isr = new InputStreamReader(response.getEntity().getContent(), charset);
				BufferedReader res = new BufferedReader(isr, 8192);
				String resposta = res.readLine();
				Log.d("DOPOST", "Resposta= " + resposta);
				return gson.fromJson(cleanResponseString(resposta), T);	
			}
			
			throw new Exception();
		}		
		
		public static String calculateCheckSum(final HashMap<String, Object> params, final String secretKey){
			String checkSum = "";
			final TreeSet<String> sortedKeys = new TreeSet <String>(String.CASE_INSENSITIVE_ORDER);
			sortedKeys.addAll(params.keySet());

			final StringBuilder paramValueStr = new StringBuilder();
			//final ArrayList < NameValuePair > queryParams = new ArrayList < NameValuePair >();

			for (final String key : sortedKeys) {
				paramValueStr.append(key);
				paramValueStr.append(params.get(key));
				//queryParams.add(new NameValuePair(key, params.get(key).toString()));
			}
			
			
			try {
				checkSum = calculateRFC2104HMAC(paramValueStr.toString().replace(" ", ""), secretKey); 
			} catch (SignatureException e) {
				e.printStackTrace();
			}
			return checkSum;
		}
		
		public static String calculateRFC2104HMAC(String data, String secretKey) throws java.security.SignatureException {
			String result = "";
			try {
				String original = data+"_"+secretKey;
				Log.d("calculateRFC2104HMAC", "original= " + original);
				MessageDigest md5 = MessageDigest.getInstance("MD5");

		        byte[] resultB = new byte[md5.getDigestLength()];
		        md5.reset();
		        md5.update(original.getBytes());
		        resultB = md5.digest();

		        StringBuffer buf = new StringBuffer(resultB.length * 2);

		        for (int i = 0; i < resultB.length; i++) {
		            int intVal = resultB[i] & 0xff;
		            if (intVal < 0x10) {
		                buf.append("0");
		            }
		            buf.append(Integer.toHexString(intVal));
		        }
		        result = buf.toString();		
		        Log.d("calculateRFC2104HMAC", "Hash= " + result);
				
				/*String original = data+"_"+secretKey;
				Log.d("calculateRFC2104HMAC", "original= " + original);
				byte[] bytes = original.getBytes();
		        MessageDigest m = MessageDigest.getInstance("MD5");
		        byte[] digest = m.digest(bytes);
		        result = new BigInteger(1, digest).toString(16);			
		        Log.d("calculateRFC2104HMAC", "Hash= " + result);*/
				/*Mac mac = Mac.getInstance("HmacSHA1");
				SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA1");
				mac.init(key);
				byte[] authentication = mac.doFinal(data.getBytes("ASCII"));
				result = new String(Hex.encodeHex(authentication));*/
				//result = new String(Base64.encodeBase64(authentication));
				//result = toHex(authentication);

			} catch (Exception e) {
				throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
			}
			return result;
		}
}
