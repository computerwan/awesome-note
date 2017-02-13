package JavaBasic.io;

import JavaBasic.io.BufferedInputFile;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by Wan on 2016/8/14 0014.
 */
public class MemoryInput {
    public static void main(String[] args) throws IOException {
        StringReader in = new StringReader(BufferedInputFile.read("D:\\test.txt"));
        int c;
        while((c=in.read())!=-1){
            System.out.println((char)c);
        }
    }
}
