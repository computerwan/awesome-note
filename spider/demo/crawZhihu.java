import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class crawZhihu {
    public static void main(String[] args) {
        String url = "https://www.zhihu.com/explore/recommendations";
        String result = sendGet(url);
        ArrayList<Zhihu> results = crawZhihu.getZhihu(result);
        //ArrayList<String> titles = RegexString(result, "question_link.+?>(.+?)<");
        Iterator it = results.iterator();
        while (it.hasNext()) {
            System.out.println(it.next() + "");
        }
        //写入本地文件
        for(Zhihu i:results) {
            FileReaderWriter.writeIntoFile(i.writeString(),
                                "D:/知乎_编辑推荐.txt", true);
        }
    }

    //将标题，连接，回答结果放在这里
    private static ArrayList<Zhihu> getZhihu(String result) {
        //存放结果
        ArrayList<Zhihu> results = new ArrayList<Zhihu>();
        //标题
        Pattern titlepattern = Pattern.compile("question_link.+?>(.+?)<");
        Matcher titlematcher = titlepattern.matcher(result);
        //连接
        Pattern urlpattern = Pattern.compile("question_link.+?href=\"(.+?)\"");
        Matcher urlmatcher = urlpattern.matcher(result);
        boolean isFind = titlematcher.find() && urlmatcher.find();
//        System.out.println(titlematcher.find());
//        System.out.println(urlmatcher.find());
        while(isFind) {
            Zhihu zhihu = new Zhihu();
            zhihu.question=titlematcher.group(1);
            zhihu.zhihuUrl=urlmatcher.group(1);
            results.add(zhihu);
            isFind = titlematcher.find() && urlmatcher.find();
        }
      return results;
    }

/*    private static ArrayList<String> RegexString(String targetStr, String patternStr) {
        //定义一个ArrayList存放结果
        ArrayList<String> results = new ArrayList<String>();
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(targetStr);
        while (matcher.find()) {
            results.add(matcher.group(1));
        }
        return results;
    }*/

    private static String sendGet(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            URL realurl = new URL(url);
            URLConnection conn = realurl.openConnection();
            conn.connect();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("网络建立失败");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return result;
    }

}
