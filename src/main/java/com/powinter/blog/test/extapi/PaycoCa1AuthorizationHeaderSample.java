package com.powinter.blog.test.extapi;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;

public class PaycoCa1AuthorizationHeaderSample {
	final static String key = "czSuk0ad+N7BSvvWxhqRpvp6TVs7240dKAlBB3ZBtR4=";
	final static String iv = "1WbjDp0lxh9puA+aN2MNvg==";
	static final DateTimeFormatter datTimeFormatters = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").withZone(ZoneId.of("Asia/Seoul"));
	final static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd" + "'T'" + "HH:mm:ss.SSSXXX");

	public static void main(String[] args) {
		String dateToStr = dateFormat.format(new Date());
		
		System.out.println("===dateToStr:"+dateToStr);
		final String httpMethod = "POST";
		final String uri = "/client/v1.1/signs/authentication.request";
//		final String payload = "{\"key1\":\"value1\",\"key2\":\"value2\"}";
		final String payload = 
				"{"
				+ "\"requestChannelCode\":\"PC_WEB\","
				+ "\"clientTransactionId\":\"TXID0001\","
//				+ "\"expirationYmdt\":\""+expire_time+"\","
//				+ "\"userIp\":\"10.0.16.165\","
				+ "\"authentication\":{"
					+ "\"contents\":\"\","
					+ "\"title\":\"test\""
					+ "},"
				+ "\"targetQuery\":{"
					+ "\"typeCode\":\""+encrypt("COMPOSITE1")+"\","
					+ "\"name\":\""+encrypt("김득한")+"\","
					+ "\"birthday\":\""+ encrypt("19780313") +"\","
					+ "\"cellphoneNumber\":\""+encrypt("01099461217")+"\","
					+ "\"ci\":\"\""
					+ "}"
				+ "}";
		final ZonedDateTime signedYmdt = ZonedDateTime.parse("2021-01-01T09:00:00.000+09:00", datTimeFormatters);

		final String clientId= "ALPstoQqUDuzGN8Zy4Ji";
		final String clientSecret = "czSuk0ad+N7BSvvWxhqRpvp6TVs7240dKAlBB3ZBtR4=";
		final byte[] clientSecretBytes = Base64.getDecoder().decode(clientSecret);

		dateToStr = dateFormat.format(new Date());
		
		System.out.println("===dateToStr:"+dateToStr);
		
		final String authorizationHeader = generateHeader(
				httpMethod,
				uri,
				payload,
				dateToStr,
				clientId,
				clientSecretBytes);
		System.out.println("* " + authorizationHeader);
		
		
		try {
			URL url = new URL("https://demo-ca.payco.com/client/v1.1/signs/authentication.request");
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
//			URL url = new URL("http://localhost:8000/test/v1.1");
//			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("POST"); // HTTP POST 메소드 설정
//			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			con.setRequestProperty("Authorization", authorizationHeader);
			con.setDoOutput(true); // POST 파라미터 전달을 위한 설정

			// Send post request
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			System.out.println(payload.length());
			wr.writeBytes(payload);
//			wr.write(jsonStr.getBytes("utf-8"), 0, jsonStr.length()); // URL 파라미터 추가
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println("HTTP 응답 코드 : " + responseCode);
			System.out.println("HTTP body : " + response.toString());

		} catch (Exception e) {
			System.out.println("[### error : " + e.getMessage());
		}
	}


	static String generateHeader(final String httpMethod,
								 final String uri,
								 final String payload,
//								 final ZonedDateTime signedYmdt,
								 final String signedYmdt,
								 final String clientId,
								 final byte[] clientSecretBytes) {
//		final String signedDate = signedYmdt.format(datTimeFormatters);
		final String signedDate = signedYmdt;
//		final String canonicalRequest = toCanonicalRequest(httpMethod, uri, payload, signedDate);
		final String canonicalRequest = toCanonicalRequest(httpMethod, uri, payload, signedDate);

		final byte[] canonicalRequestBytes = canonicalRequest.getBytes(StandardCharsets.UTF_8);
		final byte[] macBytes = doHmacSha256(canonicalRequestBytes, clientSecretBytes);
		final String mac = encodeBase64(macBytes);

		return toAuthorizationHeader(clientId, signedDate, mac);
	}

	
	

	static String toCanonicalRequest(final String httpMethod,
									 final String uri,
									 final String payload,
									 final String signedDate) {
		return httpMethod + "\n" +
				uri + "\n" +
				signedDate + "\n" +
				encodeBase64(sha256(payload.getBytes(StandardCharsets.UTF_8)));
	}

	public static byte[] sha256(final byte[] messageBytes) {
		try {
			final MessageDigest digest = MessageDigest.getInstance("SHA-256");
			return digest.digest(messageBytes);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static String encodeBase64(final byte[] messageBytes) {
		return Base64.getEncoder().encodeToString(messageBytes);
	}

	static byte[] doHmacSha256(byte[] messageBytes,
							   byte[] keyBytes) {
		final SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA256");

		try {

			final Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(signingKey);
			return mac.doFinal(messageBytes);

		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			throw new RuntimeException(e);
		}
	}

	static String toAuthorizationHeader(final String clientId,
										final String signedDate,
										final String mac) {
		final String form = "PAYCO-CA1 clientId=%s,signedDate=%s,signature=%s";
		return String.format(form, clientId, signedDate, mac);
	}
	
	public static String encrypt(String pTxt) {
		String eTxt = "";
		final String PROVIDER = "BC";
		final String SIGNATURE_ALGORITHM = "AES/CBC/PKCS5Padding";
		try {

			for (java.security.Provider p : java.security.Security.getProviders()) {
//				System.out.println("[### exist provider : " + p.toString());
			}

			// 암호화 Provider 추가
			if (Security.getProvider("BC") == null) {
				Security.addProvider(new BouncyCastleProvider());
				System.out.println("addProvider : BC");
			}

			byte[] encryptedData = null;
//			System.out.println(iv.getBytes().length);
			Cipher encrypter = Cipher.getInstance(SIGNATURE_ALGORITHM, PROVIDER);
			encrypter.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(Base64.getDecoder().decode(key.getBytes()), "AES"),
					new IvParameterSpec(Base64.getDecoder().decode(iv.getBytes())));
			encryptedData = encrypter.doFinal(pTxt.getBytes());

			eTxt = new String(Base64.getEncoder().encode(encryptedData));

		} catch (Exception e) {
			System.out.println("[### error : " + e.getMessage());
		} finally {
		}

		return eTxt;
	}	
}
