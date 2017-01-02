package JavaBasic.IO;

import java.io.*;

/**
 * Created by Wan on 2016/8/14 0014.
 */
public class BasicFileOutput {
    static String file ="D:\\result.txt";

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new StringReader(BufferedInputFile.read("D:\\test.txt")));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        int lineCount =1;
        String s;
        while((s=in.readLine())!=null){
            out.println(lineCount++ + " :" +s);
        }
        out.close();
        System.out.println(BufferedInputFile.read(file));
    }
}
