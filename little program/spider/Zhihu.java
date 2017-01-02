import java.util.ArrayList;


public class Zhihu {
    public String question;
    public String zhihuUrl;


    public Zhihu(String question, String zhihuUrl, ArrayList<String> answers) {
        this.question = question;
        this.zhihuUrl = zhihuUrl;

    }

    public Zhihu() {
        this.question = "";
        this.zhihuUrl = "";

    }

    @Override
    public String toString() {
        return "Zhihu{" +
                "问题是:" + question + '\'' +
                ", 地址是=http://www.zhihu.com" + zhihuUrl + '\'' +
                '}';
    }
     public String writeString(){
         String result="";
         result+="问题是："+question+" "+"链接是http://www.zhihu.com"+zhihuUrl;
         return result;
     }

}
