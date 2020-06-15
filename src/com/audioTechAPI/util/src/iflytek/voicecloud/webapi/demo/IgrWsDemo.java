package com.audioTechAPI.util.src.com.iflytek.voicecloud.webapi.demo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;
import okio.ByteString;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

public class IgrWsDemo {
    private static final String hostUrl = "https://ws-api.xfyun.cn/v2/igr"; //http url 不支持解析 ws/wss schema
    private static final String apiKey = "33c46db15027a061312048cfe0ba1654"; //正确的
    private static final String apiSecret = "2c051f1d4d288621eb4c1c638b9b63fe"; //正确的
    //private static final String apiKey = "5572154970a592937d18e8e0a"; //错误的
    //private static final String apiSecret = "FZHbAyhkJ6qsA7tUgMkhIlzpkBC"; //错误的
    private static final String appid = "5d10c291";
    //private static final String appid = "5c7737de";//错误的
    public static final int StatusFirstFrame = 0;
    public static final int StatusContinueFrame = 1;
    public static final int StatusLastFrame = 2;
    public static final Gson json = new Gson();
    public static void main(String[] args) throws Exception {
        // 构建鉴权url
        String authUrl = getAuthUrl(hostUrl, apiKey, apiSecret);
        OkHttpClient client = new OkHttpClient.Builder().build();
        //将url中的 schema http://和https://分别替换为ws:// 和 wss://
        String url = authUrl.toString().replace("http://", "ws://").replace("https://", "wss://");
        Request request = new Request.Builder().url(url).build();
        System.out.println("authUrl===>" + authUrl);
        WebSocket webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                try {
					System.out.println("onOpen code:" + response.code());
					System.out.println("onOpen body:" + response.body().string());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                System.out.println("receive=>" + text);
                ResponseData resp = json.fromJson(text, ResponseData.class);
                if (resp != null) {
                    if (resp.getCode() != 0) {
                        System.out.println("error=>" + resp.getMessage() + " sid=" + resp.getSid());
                        System.exit(0);
                    }
                    if (resp.getData() != null) {
                        if (resp.getData().getStatus() == 2) {
                            // todo  resp.data.status ==2 说明数据全部返回完毕，可以关闭连接，释放资源
                            System.out.println("session end ");
                            //System.exit(0);
                            webSocket.close(1005,"");
                        } else {
                            // todo 根据返回的数据处理
                        }
                    }
                }
            }
            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
            }
            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                System.out.println("socket closing");
            }
            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                System.out.println("socket closed");
            }
            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                try {
                	if (null != response) {
                        int code = response.code();
    					System.out.println("onFailure code:" + code);
    					System.out.println("onFailure body:" + response.body().string());
    					if (101 != code) {
    		                System.out.println("connection failed");
    		                System.exit(0);
    					}
                	}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        int frameSize = 157;//195; //每一帧音频的大小
        int intervel = 10;
        int status = 0;  // 音频的状态
        //FileInputStream fs = new FileInputStream("2.pcm");
        try (FileInputStream fs = new FileInputStream("resource\\igr\\igr_pcm_16k.pcm")) {
            byte[] buffer = new byte[frameSize];
            int test = 0;
            // 发送音频
            end:
            while (true) {
                int len = fs.read(buffer);
                if (len == -1) {
                    status = StatusLastFrame;  //文件读完，改变status 为 2
                }
                switch (status) {
                    case StatusFirstFrame:   // 第一帧音频status = 0
                        JsonObject frame = new JsonObject();
                        JsonObject business = new JsonObject();  //第一帧必须发送
                        JsonObject common = new JsonObject();  //第一帧必须发送
                        JsonObject data = new JsonObject();  //每一帧都要发送
                        // 填充common
                        common.addProperty("app_id", appid);
                        //填充business
                        business.addProperty("aue", "raw");
                        business.addProperty("rate", "16000");
                        business.addProperty("ent", "igr");
                        //填充data
                        data.addProperty("status", StatusFirstFrame);
                        data.addProperty("audio", Base64.getEncoder().encodeToString(Arrays.copyOf(buffer,len)));
                        //填充frame
                        frame.add("common", common);
                        frame.add("business", business);
                        frame.add("data", data);
                        webSocket.send(frame.toString());
                        status = StatusContinueFrame;  // 发送完第一帧改变status 为 1
                        test +=1;
                        break;
                    case StatusContinueFrame:  //中间帧status = 1
                        JsonObject frame1 = new JsonObject();
                        JsonObject data1 = new JsonObject();
                        data1.addProperty("status", StatusContinueFrame);
                        data1.addProperty("audio", Base64.getEncoder().encodeToString(Arrays.copyOf(buffer,len)));
                        frame1.add("data", data1);
                        webSocket.send(frame1.toString());
                        test +=1;
                        break;
                    case StatusLastFrame:    // 最后一帧音频status = 2 ，标志音频发送结束
                        //Thread.sleep(intervel + 1000); //模拟音频采样延时
                        System.out.println("last status:" + status);
                       // Thread.sleep(9000); //测试超时10s
                        JsonObject frame2 = new JsonObject();
                        JsonObject data2 = new JsonObject();
                        data2.addProperty("status", StatusLastFrame);
                        data2.addProperty("audio", "");
                        frame2.add("data", data2);
                        webSocket.send(frame2.toString());
                        test +=1;
                       /* if (test >= 166) {

                            break end;
                        }*/

                        break end;
                        
                }
                Thread.sleep(intervel); //模拟音频采样延时
            }
            System.out.println("all data is send:" + test);
        }
    }
    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        
        Date dateD = new Date();
        Date dateD1 = new Date();
        
        //测试用，调整时间戳（<=5,>-5)
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateD);
		calendar.add(Calendar.MINUTE, 15);
		dateD1 = calendar.getTime();
		System.out.println(sdf.format(dateD1));

        String date1s = format.format(dateD1);
        String date = format.format(dateD);
		System.out.println("date:" + date);
        
        StringBuilder builder = new StringBuilder("host: ").append("nothing").append("\n").//
        		 //new StringBuilder("date: ").append(date).append("\n").//
        		append("date: ").append(date).append("\n").//
                append("GET ").append(url.getPath()).append(" HTTP/1.1");//;
        Charset charset = Charset.forName("UTF-8");
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(charset), "hmacsha256");
        mac.init(spec);
        byte[] hexDigits = mac.doFinal(builder.toString().getBytes(charset));
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        String authorization = String.format("hmac username=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        HttpUrl httpUrl = HttpUrl.parse("https://" + url.getHost() + url.getPath()).newBuilder().//
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(charset))).//
                addQueryParameter("date", date).//
                addQueryParameter("host", "nothing").//
                build();
        System.out.println("auth  : " + Base64.getEncoder().encodeToString(authorization.getBytes(charset)));
		System.out.println("【ITR WebAPI hexDigits】\n" + hexDigits);
		System.out.println();
		System.out.println("【ITR WebAPI sha】\n" + sha);
		System.out.println();

		System.out.println("IGR WebAPI authorization：" + authorization);
		System.out.println(Base64.getEncoder().encodeToString(authorization.getBytes(charset)));
		return httpUrl.toString();
    }
    public static class ResponseData {
        private int code;
        private String message;
        private String sid;
        private Data data;
        public int getCode() {
            return code;
        }
        public String getMessage() {
            return message;
        }
        public String getSid() {
            return sid;
        }
        public Data getData() {
            return data;
        }
    }
    public static class Data {
        private int status;
        private Object result;
        public int getStatus() {
            return status;
        }
        public Object getResult() {
            return result;
        }
    }
}
