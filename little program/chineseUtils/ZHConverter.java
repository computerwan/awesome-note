package com.tracker.common.utils.chinese;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


/**
 * 中文简体繁体转换工具
 */
public enum ZHConverter {

    /**
     * TRADITIONAL表示将简体字转为繁体字
     * SIMPLIFIED表示将繁体字转为简体字
     * 使用方法：ZHConverter.TRADITIONAL.convert(str)
     */
    TRADITIONAL(0), SIMPLIFIED(1);
    private static final String DICT_TRADITIONAL = "trad.txt";
    private static final String DICT_SIMPLIFIED = "simp.txt";
    private static final String DICT_PHRASE_TRADITIONAL = "traditional.txt";
    private static final String DICT_PHRASE_SIMPLIFIED = "simplified.txt";
    public static final char CJK_UNIFIED_IDEOGRAPHS_START = '\u4E00';
    public static final char CJK_UNIFIED_IDEOGRAPHS_END = '\u9FA5';

    private static final Logger log = LoggerFactory.getLogger(ZHConverter.class);
    private Trie dict = new Trie();
    private char[] chars = null;
    private int maxLen = 2;

    ZHConverter(int i) {
        if (i == 0) {
            loadCharMapping(DICT_TRADITIONAL);
            loadPhraseToMap(DICT_PHRASE_TRADITIONAL);
        } else if (i == 1) {
            loadCharMapping(DICT_SIMPLIFIED);
            loadPhraseToMap(DICT_PHRASE_SIMPLIFIED);
        }
    }

    /**
     * 将字载入到char数组中(请勿修改trad.txt和simp.txt中的内容)
     */
    public void loadCharMapping(String dictFile) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(dictFile);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(is))) {
            CharArrayWriter out = new CharArrayWriter();
            String line = null;
            while (null != (line = in.readLine())) {
                out.write(line);
            }
            chars = out.toCharArray();
            in.close();

        } catch (IOException e) {
            log.error("fail to load into char arrays" + e);
        }
    }


    /**
     * 将词组载入到字典树中（文本内容中，注解必须以#开头，key和value中以=分割）
     */
    public void loadPhraseToMap(String dictFileWithPhrase) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(dictFileWithPhrase);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line = null;
            while (null != (line = reader.readLine())) {
                if (line.length() == 0 || line.startsWith("#")) {
                    continue;
                }
                String[] pair = line.split("=");

                if (pair.length < 2) {
                    continue;
                }
                maxLen = maxLen < pair[0].length() ? pair[0].length() : maxLen;
                dict.add(pair[0], pair[1]);
            }
        } catch (IOException e) {
            log.error("fail to load into map" + e);
        }
    }

    /**
     * 调用的主方法
     */
    public String convert(String str) {
        Reader in = new StringReader(str);
        Writer out = new StringWriter();
        try {
            convert(in, out);
        } catch (IOException e) {
            log.error("fail to convert{},because of{}", str, e);
        }
        return out.toString();
    }

    /**
     * 对单独字进行处理
     */
    public char convert(char ch) {
        if (ch >= CJK_UNIFIED_IDEOGRAPHS_START
                && ch <= CJK_UNIFIED_IDEOGRAPHS_END) {
            return chars[ch - CJK_UNIFIED_IDEOGRAPHS_START];
        } else {
            return ch;
        }
    }


    /**
     * 对词组做处理
     * 通过dict.match获取的是叶节点
     * PushbackReader：推回输出流
     * --> 如果存在，将匹配完成字段结尾后面的内容存入缓存
     * --> 如果不存在，将所有内容存入缓存
     */
    private void convert(Reader reader, Writer writer) throws IOException {
        PushbackReader in = new PushbackReader(new BufferedReader(reader), maxLen);
        char[] buf = new char[maxLen];

        int len = -1;
        while ((len = in.read(buf)) != -1) {
            TrieNode<String> node = dict.match(buf, len);

            if (node != null) {
                int offset = node.getLevel();
                writer.write(node.getValue());
                in.unread(buf, offset, len - offset);
            } else {
                in.unread(buf, 0, len);
                char ch = (char) in.read();
                writer.write(convert(ch));
            }
        }

    }


}
