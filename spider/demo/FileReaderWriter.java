import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileReaderWriter {
    private static File file;
    //根据所给的地址创建文件
    public static boolean createNewFile(String filePath) {
        boolean isSuccess = true;
        //如果有"\\"则转换为"/"
        String filePathTurn = filePath.replaceAll("\\\\", "/");
        //过滤掉文件名（去掉最后一个"/"之后的内容）
        int index = filePathTurn.lastIndexOf("/");
        String dir = filePathTurn.substring(0, index);
        //创建文件夹
        File fileDir = new File(dir);
        isSuccess = fileDir.mkdirs();
        //创建文件
        file = new File(filePathTurn);
        try {
            isSuccess = file.createNewFile(); //为什么要和上面重名
        } catch (IOException e) {
            isSuccess = false;
            e.printStackTrace();
        }

        return isSuccess;
    }

    public static boolean writeIntoFile(String content, String filePath,
                                        boolean isAppend) {
        //创建文件
        boolean isSuccess = createNewFile(filePath);

        // 写入文件
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file, isAppend);
            fileWriter.write(content);
            fileWriter.flush();
        } catch (IOException e) {
            isSuccess = false;
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null)
                    fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return isSuccess;
    }
}
