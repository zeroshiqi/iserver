package cn.ichazuo.commons.component;

import java.io.File;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import cn.ichazuo.commons.util.DateUtils;

/**
 * @ClassName: UploadFile
 * @Description: (文件上传类)
 * @author ZhaoXu
 * @date 2015年6月28日 下午4:20:19
 * @version V1.0
 */
@Component("uploadFile")
public class UploadFile {
	private static Logger logger = LoggerFactory.getLogger(UploadFile.class);

	@Autowired
	private ConfigInfo configInfo;

	/**
	 * @Title: upload
	 * @Description: (文件上传)
	 * @param file
	 *            上传的文件
	 * @param folder
	 *            保存的文件夹
	 * @return
	 */
	public String upload(MultipartFile file, String folder) {
		String resultPath = "";
		// 获得文件后缀
		String suffix = getSuffix(file);
		// 创建随机文件名
		String uuidFileName = UUID.randomUUID().toString().replaceAll("-", "");
		// 获得当前时间作为文件夹
		String nowDate = DateUtils.getCurrentDate(DateUtils.DATE_FORMAT_NORMAL);
		// 保存路径
		String path = configInfo.getUploadPath() + folder + File.separator + nowDate + File.separator;

		// 原文件
		File targetFile = new File(path, uuidFileName + suffix);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		try {
			// 将文件复制到目标文件
			file.transferTo(targetFile);
			// 保存在数据库的文件+路径
			// 如:/file/member/2015-5-10/uuid.jpg
			resultPath = File.separator + folder + File.separator + nowDate + File.separator + uuidFileName + suffix;
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return resultPath;
	}

	/**
	 * @Title: getSuffix
	 * @Description: (获得后缀名)
	 * @param file
	 * @return
	 */
	public String getSuffix(MultipartFile file) {
		// 原文件名
		String oldFileName = file.getOriginalFilename();
		// 获得文件后缀
		String suffix = oldFileName.substring(oldFileName.lastIndexOf("."));
		return suffix;
	}
}
