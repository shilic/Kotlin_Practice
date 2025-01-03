package com.example.my;

import androidx.annotation.NonNull;

import org.junit.Test;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpUnitTest {
    static final String url1 = "https://jsonplaceholder.typicode.com/users";
    static final String url2 = "https://api.jisuapi.com/pet/query?appkey=yourappkey&name=拉布拉多";
    static String url = url1;

    @Test
    public void okGetEnqueue() throws InterruptedException {
        //Builder构建者模式 可参考https://www.jianshu.com/p/afe090b2e19c
        //创建 okHttpClient 实例
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        //创建 Request 实例
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        System.out.println("尝试发起 异步请求");
        //🌟异步请求发起
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("异步请求失败");
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                System.out.println("异步请求成功");
                if (response.body() != null) {
                    String result = response.body().string();
                    System.out.println("异步请求 ok = " + result);
                }
            }

        });
        // 因为单元测试的缘故，程序到这里就会终止，故增加休眠，等待 callback
        Thread.sleep(5000);
    }
    @Test
    public void okGetExecute(){
        //Builder构建者模式 可参考https://www.jianshu.com/p/afe090b2e19c
        //创建 okHttpClient 实例
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        //创建 Request 实例
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        //同步请求
        //发起同步请求的方式，返回一个 Response类的值
        //Response response = null;
        Call call = okHttpClient.newCall(request);
        try (Response response = call.execute()){
            //🌟发起同步请求
            if (response.body() != null) {
                String result = response.body().string();
                System.out.println("同步获取 ok = " + result);
            }
            //如果需要更新主线程的 UI 如果使用 rxjava 以及 retrofit 就不用这样处理了
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
