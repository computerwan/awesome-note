import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class crawbyHttpClient {

    public static boolean downloadPage(String path) throws Exception {
        //httpClient是一个接口，要找实现该接口的类
        HttpClient client = new DefaultHttpClient();
        // 定义输入输出流
        InputStream in = null;
        //存放结果
        String result = "";
        //获取GET
        HttpGet get = new HttpGet(path);
        //获取响应
        HttpResponse response = client.execute(get);
        //如果返回值是200，则正常
        int code = response.getStatusLine().getStatusCode();
        if (code == 200) {
            in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
            System.out.println(result);
        } else {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        try {
            // 抓取百度首页，输出
            crawbyHttpClient.downloadPage("http://www.baidu.com");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}