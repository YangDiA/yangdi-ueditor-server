package com.baidu.ueditor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by YangDi on 2017/7/25.
 */
public class UploadImageToZimgUtil {
   private  String zimgServer;

    public  String uploadFile(MultipartFile multipartFile){
        try {
            RestTemplate rest = new RestTemplate();
            MultiValueMap<String, Object> param = new LinkedMultiValueMap<String, Object>();
            param.add("userfile", multipartFile.getBytes());
            String url="http://192.168.1.160:4869/upload";
            String response = rest.postForObject(url, param, String.class);
            String str =response.substring(response.indexOf("<body>")+11,response.indexOf("</body>")-1);
            return str.split(",")[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  "";
    }

    // 定义一个私有构造方法
    private UploadImageToZimgUtil(String zimgServer) {
        this.zimgServer =  zimgServer;
    }
    //定义一个静态私有变量(不初始化，不使用final关键字，使用volatile保证了多线程访问时instance变量的可见性，避免了instance初始化时其他变量属性还没赋值完时，被另外线程调用)
    private static volatile UploadImageToZimgUtil instance;

    //定义一个共有的静态方法，返回该类型实例
    public static UploadImageToZimgUtil getIstance(String zimgServer) {
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (instance == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (UploadImageToZimgUtil.class) {
                //未初始化，则初始instance变量
                if (instance == null) {
                    instance = new UploadImageToZimgUtil(zimgServer);
                }
            }
        }
        return instance;
    }
    public static UploadImageToZimgUtil getIstance(){
        return instance;
    }
}
