package designPatterns.singleton;

/**
 * Created by Wan on 2016/8/14 0014.
 */
public class Singleton {
    //˽�л����췽��
    private Singleton(){}
    //ʹ��һ����̬�ڲ�����ά������
    private static class SingletonFactory{
        private static Singleton instance =new Singleton();
    }
    public static Singleton getInstance(){
        return SingletonFactory.instance;
    }
}


