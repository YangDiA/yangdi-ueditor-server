package hello;

import com.baidu.ueditor.ActionEnter;
import com.baidu.ueditor.UploadImageToZimgUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SpringBootApplication
@RestController
public class Application {
	//@Value("${website.ueditor.imageUrlPrefix:http://192.168.1.160:4869/}")
	String imageUrlPrefix="http://192.168.1.160:4869/";

	//@Value("${server.zimg.server:http://192.168.1.160:4869/upload}")
	private  String zimgServer="http://192.168.1.160:4869/upload";

	@RequestMapping("/")
	public String home() {
		return "Hello Docker World";
	}

	@RequestMapping( value = "/nonsecservice/ueditor/upload")
	@ResponseBody
	public void ueditor(HttpServletRequest request, HttpServletResponse response){
		try {

			String rootPath = request.getRealPath( "/" );
			Map<String ,Object> mapParam = new HashMap<>();
			mapParam.put("imageUrlPrefix",imageUrlPrefix);

			// 必须初始化图片服务器地址
			UploadImageToZimgUtil.getIstance(zimgServer);
			response.getWriter().write(new ActionEnter( request,rootPath,mapParam ).exec());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
