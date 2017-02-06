package cn.edu.usst.SafeGuard.domain;

/**
 * Created by Wan on 2016/3/3 0003.
 * url信息的封装
 */
public class UrlBean  {
    private String url;
    private int VersionCode;
    private String desc;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVersionCode() {
        return VersionCode;
    }

    public void setVersionCode(int versionCode) {
        VersionCode = versionCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
