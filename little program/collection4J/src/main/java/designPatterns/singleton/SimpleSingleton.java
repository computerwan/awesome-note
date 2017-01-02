package designPatterns.singleton;

/**
 * Created by Wan on 2016/8/13 0013.
 */
public class SimpleSingleton {
    //1������˽�о�̬ʵ������ֹ���ⲿ���ã���ֵΪnull��Ŀ����Ϊ���ӳټ���
    private static SimpleSingleton instatnce =null;
    //2��˽�л����췽������ֹ��ʵ����
    private SimpleSingleton(){}
    //3����̬����������ʵ��
    public static SimpleSingleton getInstatnce(){
        if(instatnce == null){
            instatnce =new SimpleSingleton();
        }
        return instatnce;
    }
    //����ö����������л������Ա�֤���������л�ǰ�󱣳�һ��
    public Object readResolve(){
        return instatnce;
    }
}
