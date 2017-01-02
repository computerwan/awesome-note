import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
 * 通过正则表达式，爬取了百度的图标
 */

public class crawBaidu {
    public static void main(String[] args) {
        String url = "http://www.baidu.com";
        String result = sendGet(url);
        String imgSrc = RegexString(result, "src=\"(.+?)\"");
        System.out.println(imgSrc);
    }

    private static String RegexString(String targetStr, String patternStr) {
        //将模式与正则表达式绑定
        Pattern pattern = Pattern.compile(patternStr);
        //做匹配
        Matcher matcher = pattern.matcher(targetStr);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "Nothing";
    }

    private static String sendGet(String url) {
        //定义一个字符串来存储网页内容；
        String result = "";
        //定义一个字符缓冲输入流
        BufferedReader in = null;
        try {
            //定义一个URL对象
            URL realUrl = new URL(url);
            //获取连接
            URLConnection conn = realUrl.openConnection();
            //开启连接
            conn.connect();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }


}
