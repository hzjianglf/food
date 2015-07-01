package com.sniper.springmvc.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sniper.springmvc.action.home.HomeBaseAction;
import com.sniper.springmvc.hibernate.service.impl.FilesService;
import com.sniper.springmvc.model.Files;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
public class FileUploadAction extends HomeBaseAction {

	@Resource
	private FilesService filesService;

	/**
	 * 返回域名部分加目录
	 * 
	 * @return
	 */
	private String getWebUrl() {
		String webUrl = null;
		if (ValidateUtil.isValid(getSystemConfig().get("imagePathPrefx"))) {
			return (String) getSystemConfig().get("imagePathPrefx");
		}

		if (null == webUrl) {
			// 去除末尾的斜杠
			webUrl = getBasePath().substring(0, getBasePath().length() - 1);
		}
		return webUrl;
		// return "http://www.yummyshandong.com/";
	}

	/**
	 * 淡灰json数据
	 * 
	 * @param servletRequest
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "upload")
	public Map<String, Object> upload(
			@RequestParam("imgFile") MultipartFile imgFile,
			Locale locale,
			@RequestParam(value = "dir", required = false, defaultValue = "image") String dir,
			@RequestParam(value = "album", required = false, defaultValue = "false") Boolean album,
			@RequestParam(value = "tags", required = false, defaultValue = "") String tags,
			@RequestParam(value = "eventdate", required = false, defaultValue = "") String datetime)
			throws IOException {
		Map<String, Object> ajaxResult = new HashMap<>();

		if (imgFile != null && imgFile.getSize() > 0) {
			// 上传文件,获取上传后的地址
			String savePath = uploadFile(imgFile, dir);
			ajaxResult.put("error", 0);
			// 返回编译器使用
			ajaxResult.put("url", getWebUrl() + savePath);
			// 返回个人使用
			ajaxResult.put("filePath", getWebUrl() + savePath);
			ajaxResult.put("fileShotPath", savePath);
			ajaxResult.put("fileType", imgFile.getContentType());
			ajaxResult.put("id",
					getSaveFilesID(imgFile, savePath, album, tags, datetime));
			ajaxResult.put("oldName", imgFile.getOriginalFilename());
		} else {
			return alert(messageSource.getMessage("fileupload.nofile", null,
					locale));

		}

		return ajaxResult;
	}

	@ResponseBody
	@RequestMapping(value = "uploads")
	public List<Map<String, Object>> uploads(
			@RequestParam("imgFile") MultipartFile[] imgFile,
			Locale locale,
			@RequestParam(value = "dir", required = false, defaultValue = "image") String dir,
			@RequestParam(value = "album", required = false, defaultValue = "false") Boolean album,
			@RequestParam(value = "tags", required = false, defaultValue = "") String tags,
			@RequestParam(value = "datetime", required = false, defaultValue = "") String datetime)
			throws IOException {
		List<Map<String, Object>> ajaxResult = new ArrayList<>();
		Map<String, Object> result = new HashMap<>();
		// 设置request

		for (int i = 0; i < imgFile.length; i++) {
			MultipartFile file = imgFile[i];

			if (imgFile != null || file.getSize() > 0) {
				// 上传文件,获取上传后的地址
				String savePath = uploadFile(file, dir);
				result.put("error", 0);
				result.put("url", getWebUrl() + savePath);
				result.put("filePath", getWebUrl() + savePath);
				result.put("fileType", file.getContentType());
				result.put("id",
						getSaveFilesID(file, savePath, album, tags, datetime));
				result.put("oldName", file.getOriginalFilename());
				ajaxResult.add(result);
			}
		}

		return ajaxResult;
	}

	/**
	 * 负责保存附件
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private String uploadFile(MultipartFile imgFile, String dir)
			throws FileNotFoundException, IOException {

		String rootDir = getRootDir();

		// 保存的目录，是固定目录，只能更改rootpath目录移动文件
		String saveFileName = getSaveFilename(imgFile);
		String savePath = getSaveDir(dir);
		// 用户前台显示
		String saveUrl = savePath;

		// 组装真的url
		savePath = rootDir + savePath;
		// 检查文件夹是否存在
		File fileDir = new File(savePath);
		if (!fileDir.isDirectory()) {
			fileDir.mkdirs();
		}
		savePath += saveFileName;
		saveUrl += saveFileName;

		FileOutputStream out = new FileOutputStream(savePath);
		InputStream in = imgFile.getInputStream();

		byte[] buffer = new byte[1024];
		int len = 0;

		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
		out.close();
		in.close();

		return saveUrl;
	}

	/**
	 * 获取保存的地址
	 * 
	 * @return
	 */
	private String getSaveDir(String dir) {

		String saveDir = "/attachments/" + dir + "/";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		saveDir += ymd + "/";
		return saveDir;
	}

	private String getRootDir() {
		String rootDir = "";
		// 项目根目录
		if (ValidateUtil.isValid(getSystemConfig().get("imagePath"))) {
			rootDir = (String) getSystemConfig().get("imagePath");
		} else {
			rootDir = getRequest().getServletContext().getRealPath("/");
		}

		return rootDir;
	}

	/**
	 * 生成文件问名称
	 * 
	 * @return
	 */
	private String getSaveFilename(MultipartFile imgFile) {
		String fileExt = imgFile.getOriginalFilename()
				.substring(imgFile.getOriginalFilename().lastIndexOf(".") + 1)
				.toLowerCase();

		// if(!Arrays.<String>asList(extMap.get(dirName).split(",")).contains(fileExt)){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String newFileName = df.format(new Date())
				+ new Random().nextInt(100000) + "." + fileExt;
		return newFileName;
	}

	/**
	 * 吧上传的文件保存在数据库
	 * 
	 * @return
	 */
	private int getSaveFilesID(MultipartFile imgFile, String savePath,
			boolean album, String tags, String date) {
		Files files = new Files();
		files.setOldName(imgFile.getOriginalFilename());
		files.setNewPath(savePath);
		files.setSize(imgFile.getSize());
		files.setAlbum(album);
		files.setTags(tags);
		if (ValidateUtil.isValid(date)) {

			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date2 = dateFormat.parse(date);
				files.setEventTimeDate(date2);
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
		files.setFileType(imgFile.getContentType());
		files.setAdminUser(getAdminUser());
		// 获取后缀
		String suffix = savePath.substring(savePath.lastIndexOf(".") + 1);
		files.setSuffix(suffix);

		try {
			files.setHash(DigestUtils.md5Hex(imgFile.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		filesService.saveEntiry(files);
		return files.getId();
	}

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "htmlmanager")
	public void htmlmanager(
			@RequestParam(value = "dir", required = false, defaultValue = "image") String dir,
			@RequestParam(value = "order", required = true) String orderSource,
			@RequestParam(value = "path", required = false) String pathSource,
			PrintWriter writer) throws IOException {

		String rootDir = getRootDir();

		// 根目录路径，可以指定绝对路径，比如 /var/www/attached/
		String rootPath = rootDir + "/attachments/";
		// 根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/

		String rootUrl = getWebUrl() + "/attachments/";

		// 图片扩展名
		String[] fileTypes = new String[] { "gif", "jpg", "jpeg", "png", "bmp" };

		String dirName = dir;
		if (dirName != null) {
			if (!Arrays.<String> asList(
					new String[] { "image", "flash", "media", "file" })
					.contains(dirName)) {

				writer.print("Invalid Directory name.");
			}

			rootPath += dirName + "/";
			rootUrl += dirName + "/";
			File saveDirFile = new File(rootPath);
			if (!saveDirFile.exists()) {
				saveDirFile.mkdirs();
			}
		}
		// 根据path参数，设置各路径和URL
		String path = pathSource != null ? pathSource : "";
		String currentPath = rootPath + path;
		String currentUrl = rootUrl + path;
		String currentDirPath = path;
		String moveupDirPath = "";
		// 请求的路径
		if (!"".equals(path)) {
			String str = currentDirPath.substring(0,
					currentDirPath.length() - 1);
			moveupDirPath = str.lastIndexOf("/") >= 0 ? str.substring(0,
					str.lastIndexOf("/") + 1) : "";
		}

		// 排序形式，name or size or type
		String order = orderSource != null ? orderSource.toLowerCase() : "name";

		// 不允许使用..移动到上一级目录
		if (path.indexOf("..") >= 0) {
			writer.print("Access is not allowed.");
			return;
		}

		// 最后一个字符不是/
		if (!"".equals(path) && !path.endsWith("/")) {
			writer.print("Parameter is not valid.");
			return;
		}
		// 目录不存在或不是目录
		File currentPathFile = new File(currentPath);
		if (!currentPathFile.isDirectory()) {
			writer.print("Directory does not exist.");
			return;
		}
		// 遍历目录取的文件信息
		List<Hashtable<String, Object>> fileList = new ArrayList<>();
		if (currentPathFile.listFiles() != null) {
			for (File file : currentPathFile.listFiles()) {
				Hashtable<String, Object> hash = new Hashtable<>();
				String fileName = file.getName();
				if (file.isDirectory()) {
					hash.put("is_dir", true);
					hash.put("has_file", (file.listFiles() != null));
					hash.put("filesize", 0L);
					hash.put("is_photo", false);
					hash.put("filetype", "");
				} else if (file.isFile()) {
					String fileExt = fileName.substring(
							fileName.lastIndexOf(".") + 1).toLowerCase();
					hash.put("is_dir", false);
					hash.put("has_file", false);
					hash.put("filesize", file.length());
					hash.put("is_photo", Arrays.<String> asList(fileTypes)
							.contains(fileExt));
					hash.put("filetype", fileExt);
				}
				hash.put("filename", fileName);
				hash.put("datetime",
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file
								.lastModified()));
				fileList.add(hash);
			}
		}

		if ("size".equals(order)) {
			Collections.sort(fileList, new SizeComparator());
		} else if ("type".equals(order)) {
			Collections.sort(fileList, new TypeComparator());
		} else {
			Collections.sort(fileList, new NameComparator());
		}

		Map<String, Object> ajaxResult = new HashMap<>();

		ajaxResult.put("moveup_dir_path", moveupDirPath);
		ajaxResult.put("current_dir_path", currentDirPath);
		ajaxResult.put("current_url", currentUrl);
		ajaxResult.put("total_count", fileList.size());
		ajaxResult.put("file_list", fileList);

		// 输出结果
		ObjectMapper json = new ObjectMapper();
		String tempfileValue = json.writeValueAsString(ajaxResult);
		JsonNode jsonNode = json.readTree(tempfileValue);
		writer.print(jsonNode);
		return;
	}

	/**
	 * 返回错误
	 * 
	 * @param msg
	 * @return
	 */
	private Map<String, Object> alert(String msg) {
		Map<String, Object> ajaxResult = new HashMap<>();

		ajaxResult.put("error", 1);
		ajaxResult.put("message", msg);
		return ajaxResult;
	}

	@SuppressWarnings("rawtypes")
	public class NameComparator implements Comparator {
		public int compare(Object a, Object b) {
			Hashtable hashA = (Hashtable) a;
			Hashtable hashB = (Hashtable) b;
			if (((Boolean) hashA.get("is_dir"))
					&& !((Boolean) hashB.get("is_dir"))) {
				return -1;
			} else if (!((Boolean) hashA.get("is_dir"))
					&& ((Boolean) hashB.get("is_dir"))) {
				return 1;
			} else {
				return ((String) hashA.get("filename"))
						.compareTo((String) hashB.get("filename"));
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public class SizeComparator implements Comparator {
		public int compare(Object a, Object b) {
			Hashtable hashA = (Hashtable) a;
			Hashtable hashB = (Hashtable) b;
			if (((Boolean) hashA.get("is_dir"))
					&& !((Boolean) hashB.get("is_dir"))) {
				return -1;
			} else if (!((Boolean) hashA.get("is_dir"))
					&& ((Boolean) hashB.get("is_dir"))) {
				return 1;
			} else {
				if (((Long) hashA.get("filesize")) > ((Long) hashB
						.get("filesize"))) {
					return 1;
				} else if (((Long) hashA.get("filesize")) < ((Long) hashB
						.get("filesize"))) {
					return -1;
				} else {
					return 0;
				}
			}
		}
	}

	/**
	 * 根据类型排序
	 * 
	 * @author sniper
	 * 
	 */
	public class TypeComparator implements Comparator<Object> {
		public int compare(Object a, Object b) {
			Hashtable<?, ?> hashA = (Hashtable<?, ?>) a;
			Hashtable<?, ?> hashB = (Hashtable<?, ?>) b;
			if (((Boolean) hashA.get("is_dir"))
					&& !((Boolean) hashB.get("is_dir"))) {
				return -1;
			} else if (!((Boolean) hashA.get("is_dir"))
					&& ((Boolean) hashB.get("is_dir"))) {
				return 1;
			} else {
				return ((String) hashA.get("filetype"))
						.compareTo((String) hashB.get("filetype"));
			}
		}
	}

}
