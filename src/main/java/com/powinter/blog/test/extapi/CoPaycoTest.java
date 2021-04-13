package com.powinter.blog.test.extapi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.security.Security;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import com.powinter.blog.test.RequestBody;

public class CoPaycoTest {

	// Client Secret
	final static String key = "czSuk0ad+N7BSvvWxhqRpvp6TVs7240dKAlBB3ZBtR4=";
	final static String iv = "1WbjDp0lxh9puA+aN2MNvg==";
	final static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd" + "'T'" + "HH:mm:ss.SSSXXX");
	final static DateTimeFormatter datTimeFormatters = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").withZone(ZoneId.of("Asia/Seoul"));

	public static void main(String[] args) throws Exception {

		Date date = new Date();
		String dateToStr = dateFormat.format(date);
		final ZonedDateTime signedYmdt = ZonedDateTime.parse("2021-01-01T09:00:00.000+09:00", datTimeFormatters);
		String expire_time = afterTime(30000);

		String enc_composite = encrypt("COMPOSITE1");
		System.out.println(enc_composite);

		RequestBody payco_body = new RequestBody();

		payco_body.put("requestChannelCode", "PC_WEB");
//		payco_body.put("clientTransactionId", "TXID0001");
		payco_body.put("expirationYmdt", expire_time);
//		payco_body.put("userIp", "10.0.16.165");
		payco_body.put("authentication.title", "test"); // 필수. 인증 제목
		payco_body.put("authentication.contents", "");
		payco_body.put("targetQuery.typeCode", enc_composite); // 필수. 유저 식별 유형 코드?
//		payco_body.put("targetQuery.ci", "");
		payco_body.put("targetQuery.name", encrypt("김재민"));
		payco_body.put("targetQuery.birthday", encrypt("19850522"));
		payco_body.put("targetQuery.cellphoneNumber", encrypt("01099461217"));

//		System.out.println("payco_bod-----------------");
//		System.out.println(payco_body.get("requestChanndelCode"));
//		System.out.println(payco_body.get("expirationYmdt"));
//		System.out.println(payco_body.get("authntication.title"));
//		System.out.println(payco_body.get("targetQuery.name"));
//		System.out.println(payco_body.get("targetQuery.birthday"));
//		System.out.println(payco_body.get("targetQuery.cellphoneNumber"));
//		System.out.println("-------------");

		String jsonStr = 
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
					+ "\"typeCode\":\""+enc_composite+"\","
					+ "\"name\":\""+encrypt("김득한")+"\","
					+ "\"birthday\":\""+ encrypt("19780313") +"\","
					+ "\"cellphoneNumber\":\""+encrypt("01099461217")+"\","
					+ "\"ci\":\"\""
					+ "}"
				+ "}";
//		String jsonStr = "{ ";
////	    Map<String, Object> mapobject=new HashMap<>();
//	    //MAP의 KEY값을 이용하여 VALUE값 가져오기
//	    for (String mapkey : payco_body.keySet()){
//	    	jsonStr = jsonStr+"\""+mapkey+"\" : \""+payco_body.get(mapkey)+"\""+" , ";
//	    }
//	    jsonStr = jsonStr.substring(0, jsonStr.length()-2) + " }";
	    System.out.println("[### req body : " + jsonStr);
	    
//	    System.out.println(new String(Base64.encode(sha256ToByte(jsonStr))));
//	    System.out.println(Base64.encode(sha256ToByte(jsonStr)));
	    String message = "POST\n/client/v1.1/signs/authentication.request\n" + signedYmdt + "\n" + new String(Base64.encode(sha256ToByte(jsonStr)));
	    System.out.println("[### message : "+message);
		String enc_sig = hmacForSig(message, key);
//		System.out.println("[### enc_sig : "+enc_sig);
		String auth = "PAYCO-CA1 clientId=ALPstoQqUDuzGN8Zy4Ji,signedDate=" + signedYmdt + ",signature=" + enc_sig;
		System.out.println("[### auth1 : "+auth);

//	    auth += sha256(encrypt(jsonStr));
//	    System.out.println("[### auth2 : "+auth);
	    
		try {
			URL url = new URL("https://demo-ca.payco.com/client/v1.1/signs/authentication.request");
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
//			URL url = new URL("http://localhost:8000/test/v1.1");
//			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("POST"); // HTTP POST 메소드 설정
//			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("Authorization", auth);
			con.setDoOutput(true); // POST 파라미터 전달을 위한 설정

			// Send post request
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			System.out.println(jsonStr.length());
			wr.writeBytes(jsonStr);
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

	public static String hmacForSig(String value, String key) throws Exception {
		final String SIGNATURE_ALGORITHM = "HmacSHA256";
		String hmac = null;
		try {
			Mac mac = Mac.getInstance(SIGNATURE_ALGORITHM);
			mac.init(new SecretKeySpec(key.getBytes(), SIGNATURE_ALGORITHM));
			byte[] hamc_data = mac.doFinal(value.getBytes());

			System.out.println("[### value :" + value);
//			System.out.println("[### hamc_data size :" + hamc_data.length);

			hmac = new String(Base64.encode(hamc_data));

//			System.out.println("[### hmac :" + hmac);
		} catch (Exception e) {
			System.out.println("[### error : " + e.getMessage());
		} finally {
		}
		return hmac;
	}
	
	public static String sha256(String data) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(data.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}

			// 출력
			return hexString.toString();

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public static byte[] sha256ToByte(String msg) {
		try {
	    MessageDigest md = MessageDigest.getInstance("SHA-256");
	    md.update(msg.getBytes());
	    
	    return md.digest();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
//	public static String bytesToHex1(byte[] bytes) {
//	    StringBuilder builder = new StringBuilder();
//	    for (byte b: bytes) {
//	      builder.append(String.format("%02x", b));
//	    }
//	    return builder.toString();
//	}




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
			encrypter.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(Base64.decode(key.getBytes()), "AES"),
					new IvParameterSpec(Base64.decode(iv.getBytes())));
			encryptedData = encrypter.doFinal(pTxt.getBytes());

			eTxt = new String(Base64.encode(encryptedData));

		} catch (Exception e) {
			System.out.println("[### error : " + e.getMessage());
		} finally {
		}

		return eTxt;
	}

	public static String afterTime(int mil_sec) throws Exception {
		Calendar cal = Calendar.getInstance();
		try {
			cal.add(Calendar.MILLISECOND, mil_sec);
		} catch (Exception e) {
			System.out.println("[### error : " + e.getMessage());
		}
		return dateFormat.format(cal.getTime());
	}

//	public static byte[] convert(Object obj) throws IOException {
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		ObjectOutputStream oos = new ObjectOutputStream(baos);
//		oos.writeObject(obj);
//		oos.flush();
//		oos.close();
//		return baos.toByteArray();
//	}
//
//	public static Object convert(byte[] bytes) throws Exception {
//		Object object = null;
//		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
//
//		object = ois.readObject();
//
//		ois.close();
//		return object;
//	}
}
