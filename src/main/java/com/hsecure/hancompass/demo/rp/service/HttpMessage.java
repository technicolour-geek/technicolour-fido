package com.hsecure.hancompass.demo.rp.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hsecure.hancompass.demo.rp.exception.RpErrorCodeInternal;
import com.hsecure.hancompass.demo.rp.exception.RpException;

/**
 * HTTP 요청 메시지를 웹서버에 전송한다.
 */
public class HttpMessage {

	private Logger logger = LoggerFactory.getLogger(HttpMessage.class);

	/**
	 * HTTP 프로토콜을 사용하여 연결할 URL
	 */
	private final URL targetURL;

	/**
	 * POST 방식으로 데이터를 전송할 때 사용되는 출력 스트림
	 */
	private DataOutputStream out;
	
	private int connectTimeout;
	private int readTimeout;

	public HttpMessage(URL targetURL, int connectTimeout, int readTimeout) {
		logger.debug("HttpMessage : " + targetURL);
		this.targetURL = targetURL;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}

	public InputStream sendGetMessage() throws RpException {
		return sendGetMessage(null);
	}

	public InputStream sendGetMessage(Properties params) throws RpException {

		String paramString = "";
		InputStream aInputStream = null;

		try {
			if (params != null) {
				logger.debug("sendGetMessage :" + params.toString());
				paramString = "?" + encodeString(params);
			}

			URL url = new URL(targetURL.toExternalForm() + paramString);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// TimeOut Set
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			conn.setRequestMethod("GET");
			conn.setUseCaches(false);

			if (conn.getResponseCode() != 200) {
				logger.debug("Response Code : " + conn.getResponseCode());
				throw new RpException(conn.getResponseCode());
			}

			aInputStream = conn.getInputStream();

		} catch (RpException pException) {
			throw pException;
		} catch (IOException pException) {
			/*-----------------------------------------------------------------------
			 * 서버 연결 실패.
			 *----------------------------------------------------------------------*/
			throw new RpException(RpErrorCodeInternal.RP_ERROR_CODE_4012_REQUEST_FAILED, pException);
		} catch (Exception pException) {
			/*-----------------------------------------------------------------------
			 * 서버 연결 실패.
			 *----------------------------------------------------------------------*/
			throw new RpException(RpErrorCodeInternal.RP_ERROR_CODE_4012_REQUEST_FAILED, pException);
		}

		return aInputStream;
	}

	public InputStream sendPostMessage() throws RpException {
		return sendPostMessage("");
	}

	public InputStream sendPostMessage(Properties params) throws RpException {
		logger.debug("sendPostMessage_parmas : " + params.toString());
		String paramString = "";
		InputStream aInputStream = null;

		try {
			if (params != null) {
				paramString = encodeString(params);
			}

			aInputStream = sendPostMessage(paramString);
		} catch (RpException pException) {
			throw pException;
		} catch (Exception pException) {
			/*-----------------------------------------------------------------------
			 * 서버 통신 실패.
			 *----------------------------------------------------------------------*/
			throw new RpException(RpErrorCodeInternal.RP_ERROR_CODE_4012_REQUEST_FAILED, pException);
		}

		return aInputStream;
	}

	private InputStream sendPostMessage(String encodedParamString) throws RpException {
		/* connection */
		URL url;
		HttpURLConnection conn = null;
		InputStream aInputStream = null;

		try {
			logger.debug("sendPostMessage_encodeParamString : " + encodedParamString);

			// URL tempUrl = targetURL;
			if (targetURL.getProtocol().toLowerCase().equals("https")) {
				TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return null;
					}

					public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
					}

					public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
					}
				} };

				// Install the all-trusting trust manager

				SSLContext sc = SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
					public boolean verify(String paramString, SSLSession paramSSLSession) {
						return true;
					}
				});

				url = new URL(targetURL.toString());
				HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
				https.setHostnameVerifier(new CustomizedHostnameVerifier());
				conn = https;
			} else {
				conn = (HttpURLConnection) targetURL.openConnection();
			}

			// Timeout Set
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);

			conn.setDoInput(true);
			conn.setDoOutput(true);

			conn.setUseCaches(false);

			conn.setRequestProperty("Content-Type", "application/json");

			out = null;

			out = new DataOutputStream(conn.getOutputStream());
			out.writeBytes(encodedParamString);
			out.flush();

			if (conn.getResponseCode() != 200) {
				logger.debug("Response Code : " + conn.getResponseCode());
				throw new RpException(conn.getResponseCode());
			}

			aInputStream = conn.getInputStream();
		} catch (RpException pException) {
			throw pException;
		} catch (IOException pException) {
			/*-----------------------------------------------------------------------
			 * 서버 통신 실패.
			 *----------------------------------------------------------------------*/
			throw new RpException(RpErrorCodeInternal.RP_ERROR_CODE_4012_REQUEST_FAILED, pException);
		} catch (Exception pException) {
			/*-----------------------------------------------------------------------
			 * 서버 통신 실패.
			 *----------------------------------------------------------------------*/
			throw new RpException(RpErrorCodeInternal.RP_ERROR_CODE_4012_REQUEST_FAILED, pException);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException pException) {
					/*-----------------------------------------------------------------------
					 * close 실패.
					 *----------------------------------------------------------------------*/
					throw new RpException(RpErrorCodeInternal.RP_ERROR_CODE_4013_RESOURCE_CLOSE_FAILED, pException);
				}
			}
		}

		return aInputStream;
	}

	public InputStream sendStreamMessage(Properties params, String encodedParamString) throws RpException {
		logger.debug("sendStreamMessage : " + encodedParamString);

		/* connection */
		URL url;
		HttpURLConnection conn = null;
		InputStream aInputStream = null;

		try {
			// URL tempUrl = targetURL;
			if (targetURL.getProtocol().toLowerCase().equals("https")) {
				TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return null;
					}

					public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
					}

					public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
					}
				} };

				SSLContext sc = SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
					public boolean verify(String paramString, SSLSession paramSSLSession) {
						return true;
					}
				});

				String paramString = null;
				HttpsURLConnection https = null;
				
				if (params != null)
				{
					paramString = "?" + encodeString(params);
					url = new URL(targetURL.toExternalForm() + paramString);
	                https = (HttpsURLConnection) url.openConnection();
				}
				else {
				    https = (HttpsURLConnection) targetURL.openConnection();
				}
				
				https.setHostnameVerifier(new CustomizedHostnameVerifier());
				conn = https;
			} else {
				String paramString = null;
				if (params != null) {
					paramString = "?" + encodeString(params);
					url = new URL(targetURL.toExternalForm() + paramString);
					conn = (HttpURLConnection) url.openConnection();
				} else {
					conn = (HttpURLConnection) targetURL.openConnection();
				}

			}

			// TimeOut Set
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);

			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			conn.setRequestProperty("Content-Type", "application/json");

			out = null;
			out = new DataOutputStream(conn.getOutputStream());
			out.writeBytes(encodedParamString);
			out.flush();

			if (conn.getResponseCode() != 200) {
				logger.debug("Response Code : " + conn.getResponseCode());
				throw new RpException(conn.getResponseCode());
			}

			aInputStream = conn.getInputStream();
		} catch (RpException pException) {
			throw pException;
		} catch (IOException pException) {
			/*-----------------------------------------------------------------------
			 * 서버 통신 실패.
			 *----------------------------------------------------------------------*/
			throw new RpException(RpErrorCodeInternal.RP_ERROR_CODE_4012_REQUEST_FAILED, pException);
		} catch (Exception pException) {
			/*-----------------------------------------------------------------------
			 * 서버 통신 실패.
			 *----------------------------------------------------------------------*/
			throw new RpException(RpErrorCodeInternal.RP_ERROR_CODE_4012_REQUEST_FAILED, pException);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException pException) {
					/*-----------------------------------------------------------------------
					 * close 실패.
					 *----------------------------------------------------------------------*/
					throw new RpException(RpErrorCodeInternal.RP_ERROR_CODE_4013_RESOURCE_CLOSE_FAILED, pException);
				}
			}
		}

		return aInputStream;
	}

	public String responseString(InputStream is) throws RpException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;
		String httpRespData = "";

		try {
			while ((line = br.readLine()) != null) {
				httpRespData += line;
			}
		} catch (IOException pException) {
			/*-----------------------------------------------------------------------
			 * Response Data 받기 에러.
			 *----------------------------------------------------------------------*/
			throw new RpException(RpErrorCodeInternal.RP_ERROR_CODE_4012_REQUEST_FAILED, pException);
		}

		logger.debug("responseString : " + httpRespData);

		return httpRespData;
	}

	@SuppressWarnings("deprecation")
	public static String encodeString(Properties params) {
//		logger.debug("== encodeString ==" + params.toString());
		StringBuffer sb = new StringBuffer(256);
		Enumeration<?> names = params.propertyNames();

		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String value = params.getProperty(name);
			sb.append(URLEncoder.encode(name) + "=" + URLEncoder.encode(value));

			if (names.hasMoreElements()) {
				sb.append("&");
			}
		}
		return sb.toString();
	}

	private static class CustomizedHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}
}
