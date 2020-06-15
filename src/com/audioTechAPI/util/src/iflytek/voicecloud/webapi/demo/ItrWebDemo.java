package com.audioTechAPI.util.src.com.iflytek.voicecloud.webapi.demo;

import com.google.gson.JsonObject;
import com.audioTechAPI.util.src.com.iflytek.voicecloud.webapi.demo.util.FileUtil;
import com.audioTechAPI.util.src.com.iflytek.voicecloud.webapi.demo.util.HttpUtil;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 拍照速算识别 WebAPI 接口调用示例
 * 
 * 运行方法：直接运行 com.iflytek.voicecloud.webapi.demo.main() 即可
 * 
 * 结果： 控制台输出识别结果信息
 * 
 * @author iflytek
 * 
 */
public class ItrWebDemo {
	// ITR webapi 接口地址
    private static final String WebITR_URL = "https://rest-api.xfyun.cn/v2/itr"; //http url 不支持解析 ws/wss schema
	/*// 应用ID
	private static final String APPID = "5c874c2e";
	// 接口密钥
	private static final String API_KEY = "a68af9b44ceaa8e1c1f53313dc6bd766";
	// 接口密钥
	private static final String API_SECRET = "ZYzEL8zzlc4N1Jf9juI7BXNYlUQfK2EM";*/
 // 应用ID
 	private static final String APPID = "5ca1b5e2";
 	// 接口密钥
 	private static final String API_KEY = "7785c1e87d5db7d7f18f25df7ff77808";
 	// 接口密钥
 	private static final String API_SECRET = "Nzc4NWMxZTg3ZDVkYjdkN2YxOGYyNWRmN2ZmNzc4MDh4Znl1bmNvbnNvbGU=";
    
	// 图片地址
	private static final String AUDIO_PATH = "resource\\itr\\testitr.jpg";

	/**
	 * OCR WebAPI 调用示例程序
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String body = buildHttpBody();
		Map<String, String> header = buildHttpHeader(body);
		Map<String, Object> resultMap = HttpUtil.doPost2(WebITR_URL, header, body);
		System.out.println("【ITR WebAPI 接口调用结果】" + resultMap);
		System.out.println();
	}
	

	/**
	 * 组装http请求头
	 */	
   public static Map<String, String> buildHttpHeader(String body) throws Exception {
		Map<String, String> header = new HashMap<String, String>();
        Charset charset = Charset.forName("UTF-8");
        Mac mac = Mac.getInstance("hmacsha256");
		
        URL url = new URL(WebITR_URL);
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        
        Date dateD = new Date();
        Date dateD1 = new Date();
        
        //测试用，调整时间戳（<=5,>-5)
       /* SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateD);
		calendar.add(Calendar.SECOND, -300);
		dateD1 = calendar.getTime();
		System.out.println(sdf.format(dateD1));

        String date1s = format.format(dateD1);*/
        String date = format.format(dateD);
		System.out.println("【ITR WebAPI date】\n" + date);
		System.out.println();

		//对body进行sha256签名,生成digest头部，POST请求必须对body验证
        MessageDigest messageDigest;
        String digest = "";
        messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hash = messageDigest.digest(body.getBytes());
        digest = Hex.encodeHexString(hash);
        String digestBase64 = "SHA-256=" + Base64.getEncoder().encodeToString(digest.getBytes(charset));
        
		//sha256加密
/*		SecretKeySpec spec = new SecretKeySpec(body.getBytes(charset), "hmacsha256");
		mac.init(spec);
		String digestBase64 = "SHA-256=" + Base64.getEncoder().encodeToString(mac.doFinal());*/

		System.out.println("【ITR WebAPI digestBase64】\n" + digestBase64);
		System.out.println();
        
		//hmacsha256加密原始字符串
        StringBuilder builder = new StringBuilder("host: ").append(url.getHost()).append("\n").//
        		append("date: ").append(date).append("\n").//
                append("POST ").append(url.getPath()).append(" HTTP/1.1").append("\n").//
                append("digest: ").append(digestBase64);
		System.out.println("【ITR WebAPI builder】\n" + builder);
		System.out.println();
		//hmacsha256加密
        //Mac mac = Mac.getInstance("hmacsha256");
		SecretKeySpec spec = new SecretKeySpec(API_SECRET.getBytes(charset), "hmacsha256");
        mac.init(spec);
        byte[] hexDigits = mac.doFinal(builder.toString().getBytes(charset));
        String sha = Base64.getEncoder().encodeToString(hexDigits);
		System.out.println("【ITR WebAPI hexDigits】\n" + hexDigits);
		System.out.println();
		System.out.println("【ITR WebAPI sha】\n" + sha);
		System.out.println();
		
		//组装authorization
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", API_KEY, "hmac-sha256", "host date request-line digest", sha);
        //String authorizationBase64 = Base64.getEncoder().encodeToString(authorization.getBytes(charset));
		System.out.println("【ITR WebAPI authorization】\n" + authorization);
		System.out.println();
		//System.out.println("【ITR WebAPI authorizationBase64】\n" + authorizationBase64);
		//System.out.println();
		
        header.put("Authorization", authorization);
		header.put("Content-Type", "application/json");
		//header.put("Accept", "application/json,version=1.0");
		header.put("Host", url.getHost());
		header.put("Date", date);
		header.put("Digest", digestBase64);
		System.out.println("【ITR WebAPI header】\n" + header);
		System.out.println();
		return header;
    }
   

	/**
	 * 组装http请求体
	 */	
   public static String buildHttpBody() throws Exception {
       JsonObject body = new JsonObject();
       JsonObject business = new JsonObject();
       JsonObject common = new JsonObject();
       JsonObject data = new JsonObject();
       //填充common
       common.addProperty("app_id", APPID);
       //填充business
       business.addProperty("ent", "math-arith");//phfw-chapter
       business.addProperty("aue", "raw");
       //填充data
       byte[] imageByteArray = FileUtil.read(AUDIO_PATH);
       String imageBase64 = new String(Base64.getEncoder().encodeToString(imageByteArray));
       data.addProperty("image", imageBase64);
       //填充body
       body.add("common", common);
       body.add("business", business);
       body.add("data", data);
       System.out.println("【ITR WebAPI body】\n" + body);
		System.out.println();
       
       return body.toString();
       //return "123";
   }
}
