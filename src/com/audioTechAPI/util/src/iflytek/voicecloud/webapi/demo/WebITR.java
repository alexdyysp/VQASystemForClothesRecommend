package com.audioTechAPI.util.src.com.iflytek.voicecloud.webapi.demo;

import com.google.gson.JsonObject;
import com.audioTechAPI.util.src.com.iflytek.voicecloud.webapi.demo.util.FileUtil;
import com.audioTechAPI.util.src.com.iflytek.voicecloud.webapi.demo.util.HttpUtil;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.nio.charset.Charset;
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
public class WebITR {
	// ITR webapi 接口地址
    private static final String WebITR_URL = "https://rest-api.xfyun.cn/v2/itr"; //https url
/*	// 应用ID
	private static final String APPID = "5c874c2e";
	// 接口APIKey
	private static final String API_KEY = "a68af9b44ceaa8e1c1f53313dc6bd766";
	// 接口APISercet
	private static final String API_SECRET = "ZYzEL8zzlc4N1Jf9juI7BXNYlUQfK2EM";*/
	// 应用ID
	private static final String APPID = "5a30f23b";
	// 接口APIKey
	private static final String API_KEY = "d6e876c2a01fe296cb5d151182be212d";
	// 接口APISercet
	private static final String API_SECRET = "ZDZlODc2YzJhMDFmZTI5NmNiNWQxNTExODJiZTIxMmR4Znl1bmNvbnNvbGU=";

	// 图片地址
	private static final String AUDIO_PATH = "resource\\itr\\itr_bmp~1.bmp";

	/**
	 * ITR WebAPI 调用示例程序
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String body = buildHttpBody();
		Map<String, String> header = buildHttpHeader(body);
		Map<String, Object> resultMap = HttpUtil.doPost2(WebITR_URL, header, body);
		System.out.println("【ITR WebAPI 接口调用结果】\n" + resultMap.get("body"));
	}
	

	/**
	 * 组装http请求头
	 */	
   public static Map<String, String> buildHttpHeader(String body) throws Exception {
		Map<String, String> header = new HashMap<String, String>();
        Charset charset = Charset.forName("UTF-8");
        Mac mac = Mac.getInstance("hmacsha256");
        URL url = new URL(WebITR_URL);
        
        //时间戳
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date dateD = new Date();
        String date = format.format(dateD);
		//System.out.println("【ITR WebAPI date】\n" + date);

		//对body进行sha256签名,生成digest头部，POST请求必须对body验证
		SecretKeySpec spec = new SecretKeySpec(body.getBytes(charset), "hmacsha256");
		mac.init(spec);
		String digestBase64 = "SHA-256=" + Base64.getEncoder().encodeToString(mac.doFinal());
		//System.out.println("【ITR WebAPI digestBase64】\n" + digestBase64);
        
		//hmacsha256加密原始字符串
        StringBuilder builder = new StringBuilder("host: ").append(url.getHost()).append("\n").//
        		append("date: ").append(date).append("\n").//
                append("POST ").append(url.getPath()).append(" HTTP/1.1").append("\n").//
                append("digest: ").append(digestBase64);
		//System.out.println("【ITR WebAPI builder】\n" + builder);
		spec = new SecretKeySpec(API_SECRET.getBytes(charset), "hmacsha256");
        mac.init(spec);
        byte[] hexDigits = mac.doFinal(builder.toString().getBytes(charset));
        String sha = Base64.getEncoder().encodeToString(hexDigits);
		//System.out.println("【ITR WebAPI sha】\n" + sha);
		
		//组装authorization
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", API_KEY, "hmac-sha256", "host date request-line digest", sha);
        //System.out.println("【ITR WebAPI authorization】\n" + authorization);
		
        header.put("Authorization", authorization);
		header.put("Content-Type", "application/json");
		header.put("Accept", "application/json,version=1.0");
		header.put("Host", url.getHost());
		header.put("Date", date);
		header.put("Digest", digestBase64);
		//System.out.println("【ITR WebAPI header】\n" + header);
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
       business.addProperty("ent", "math-arith");
       business.addProperty("aue", "raw");
       //填充data
       byte[] imageByteArray = FileUtil.read(AUDIO_PATH);
       String imageBase64 = new String(Base64.getEncoder().encodeToString(imageByteArray));
       data.addProperty("image", imageBase64);
       //填充body
       body.add("common", common);
       body.add("business", business);
       body.add("data", data);
       
       return body.toString();
   }
}
