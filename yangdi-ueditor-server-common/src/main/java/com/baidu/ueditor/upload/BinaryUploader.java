package com.baidu.ueditor.upload;

import com.baidu.ueditor.UploadImageToZimgUtil;
import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.State;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BinaryUploader {
	
	
	public static final State save(HttpServletRequest request,
			Map<String, Object> conf) {

		FileItemStream fileStream = null;
		boolean isAjaxUpload = request.getHeader( "X_Requested_With" ) != null;

		if (!ServletFileUpload.isMultipartContent(request)) {
			return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
		}

		ServletFileUpload upload = new ServletFileUpload(
				new DiskFileItemFactory());

        if ( isAjaxUpload ) {
            upload.setHeaderEncoding( "UTF-8" );
        }

		try {
			/*FileItemIterator iterator = upload.getItemIterator(request);

			while (iterator.hasNext()) {
				fileStream = iterator.next();

				if (!fileStream.isFormField())
					break;
				fileStream = null;
			}

			if (fileStream == null) {
				return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
			}

			
			String savePath = (String) conf.get("savePath");
		
			String originFileName = fileStream.getName();
			String suffix = FileType.getSuffixByFilename(originFileName);

			originFileName = originFileName.substring(0,
					originFileName.length() - suffix.length());
			savePath = savePath + suffix;

			long maxSize = ((Long) conf.get("maxSize")).longValue();

			if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
				return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
			}

			savePath = PathFormat.parse(savePath, originFileName);
			String physicalPath = (String) conf.get("rootPath") + savePath;
			
			
			InputStream is = fileStream.openStream();
			State storageState = StorageManager.saveFileByInputStream(is,
					physicalPath, maxSize);
			is.close();




			if (storageState.isSuccess()) {
				storageState.putInfo("url", PathFormat.format(savePath));
				storageState.putInfo("type", suffix);
				storageState.putInfo("original", originFileName + suffix);

			}*/
			MultiValueMap<String, MultipartFile> multipartFiles = ((StandardMultipartHttpServletRequest) request).getMultiFileMap();
			State storageState =new BaseState(true);
			storageState = new BaseState(true);
			if(multipartFiles!=null&&multipartFiles.size()>0){
				List<MultipartFile>	files = multipartFiles.get("files");
				//循环获取file数组中得文件
				for(MultipartFile file:files){

					//保存文件
					try {
						Map<String,String> temp=new HashMap<String,String>();
						String fileId= UploadImageToZimgUtil.getIstance().uploadFile(file);
						//String fileId = "group1/M00/00/01/wKgKG1l2uCmAGbt8AAzodQCbVVc725.jpg";
						temp.put("souceFileName",file.getOriginalFilename());
						temp.put("targetFileName",fileId);
						storageState.putInfo("url",fileId );
						storageState.putInfo("type", "");
						storageState.putInfo("original", file.getOriginalFilename());

					} catch (Exception e) {
						System.out.print(e.getMessage());
					}
				}
			}



			return storageState;
		} /*catch (FileUploadException e) {
			return new BaseState(false, AppInfo.PARSE_REQUEST_ERROR);
		}*/ catch (Exception e) {
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	private static boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);

		return list.contains(type);
	}
}
