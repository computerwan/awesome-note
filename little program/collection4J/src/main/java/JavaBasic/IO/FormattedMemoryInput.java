import JavaBasic.IO.BufferedInputFile;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by Wan on 2016/8/14 0014.
 */
public class FormattedMemoryInput {
    public static void main(String[] args) throws IOException {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(BufferedInputFile.read("D:\\test.txt").getBytes()));
        while(true){
            System.out.println((char)in.readByte());
        }
    }
}
