package com.sniper.springmvc.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sniper.springmvc.hibernate.service.impl.FilesService;
import com.sniper.springmvc.hibernate.service.impl.PostService;
import com.sniper.springmvc.model.Files;
import com.sniper.springmvc.model.Post;
import com.sniper.springmvc.utils.ValidateUtil;
import com.sniper.springmvc.utils.ZipUtil;

/**
 * 文件信息获取
 * 
 * @author sniper
 * 
 */
@Controller
@RequestMapping("/file-info")
public class FileInfoAction extends RootAction {

	@Resource
	private FilesService filesService;

	@Resource
	private PostService postService;

	@ResponseBody
	@RequestMapping("deleteFileByID")
	public Map<String, Object> deleteFileByID(@RequestParam("id") Integer id) {
		Map<String, Object> ajaxResult = new HashMap<>();
		ajaxResult.put("code", 500);
		if (ValidateUtil.isValid(id)) {
			try {
				Files files = filesService.getEntity(id);
				filesService.deleteEntiry(files);
				ajaxResult.put("code", 200);
				ajaxResult.put("file", null);
			} catch (Exception e) {
				ajaxResult.put("error", 500);
				ajaxResult.put("file", null);
				ajaxResult.put("msg", e.getMessage());
			}

		}
		return ajaxResult;
	}

	/**
	 * 根据文章id获取附件列表
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getFilesByPostID")
	public Map<String, Object> getFilesByPostID(@RequestParam("id") Integer id) {
		Map<String, Object> ajaxResult = new HashMap<>();
		ajaxResult.put("msg", "数据不符");
		ajaxResult.put("code", 500);
		if (ValidateUtil.isValid(id)) {
			Post post = postService.getPostFiles(id);
			if (post.getFiles().size() > 0) {
				Set<Files> files = post.getFiles();
				ajaxResult.put("file", files);
				ajaxResult.put("code", 200);
			}
		}
		return ajaxResult;
	}

	/**
	 * 根据附件id获取附件
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getFileByID")
	public Map<String, Object> getFileByID(@RequestParam("id") Integer id) {
		Map<String, Object> ajaxResult = new HashMap<>();
		ajaxResult.put("msg", "数据不符");
		ajaxResult.put("code", 500);
		if (ValidateUtil.isValid(id)) {
			Files files = filesService.getEntity(id);
			ajaxResult.put("code", 200);
			ajaxResult.put("file", files);
		}
		return ajaxResult;
	}

	/**
	 * 根据文件地址获取附件
	 * 
	 * @param url
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getFileByUrl")
	public Map<String, Object> getFileByUrl(@RequestParam("url") String url) {
		Map<String, Object> ajaxResult = new HashMap<>();
		if (ValidateUtil.isValid(url)) {
			Files files = filesService.getFileByUrl(url);
			ajaxResult.put("error", 0);
			ajaxResult.put("file", files);
		}
		return ajaxResult;
	}

	@ResponseBody
	@RequestMapping("setFileVar")
	public Map<String, Object> setFileVar(@RequestParam("name") String name,
			@RequestParam("value") String value, @RequestParam("id") Integer id) {
		Map<String, Object> ajaxResult = new HashMap<>();
		ajaxResult.put("error", 0);
		ajaxResult.put("msg", "无操作");
		if (ValidateUtil.isValid(name) && ValidateUtil.isValid(value)) {
			try {
				int result = filesService.batchFiledChange(name, value,
						new Integer[] { id });

				if (result == 1) {
					ajaxResult.put("error", 0);
					ajaxResult.put("msg", "操作成功");
				} else {
					ajaxResult.put("error", 1);
					ajaxResult.put("msg", "操作失败");
				}
			} catch (Exception e) {
				ajaxResult.put("error", 0);
				ajaxResult.put("msg", e.getMessage());
			}

		}

		return ajaxResult;
	}

	private String getFileName(String filePath)
			throws UnsupportedEncodingException {

		if (filePath.indexOf("/") != -1) {
			filePath = filePath.substring(filePath.lastIndexOf("/") + 1);
		}

		String Agent = request.getHeader("User-Agent");
		if (null != Agent) {
			Agent = Agent.toLowerCase();
			if (Agent.indexOf("firefox") != -1) {
				filePath = new String(filePath.getBytes(), "ISO8859-1");
			} else if (Agent.indexOf("msie") != -1) {
				filePath = URLEncoder.encode(filePath, "UTF-8");
			} else {
				filePath = URLEncoder.encode(filePath, "UTF-8");
			}
		}
		return filePath;
	}

	/**
	 * 需要通过 redirect:download 传递文件路径或者文件类型
	 * 
	 * @param path
	 * @param type
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "download")
	public ResponseEntity<byte[]> download(
			@RequestParam(value = "path", required = false) String path,
			@RequestParam(value = "id", required = false) Integer id,
			@RequestParam(value = "fileName", required = false) String fileName,
			@RequestParam(value = "type", required = false, defaultValue = "application/vnd.ms-excel") String type)
			throws IOException {
		// 确定各个成员变量的值

		if (ValidateUtil.isValid(id)) {
			Files files = filesService.getEntity(id);
			path = files.getNewPath();
			fileName = files.getOldName();
			if (fileName.indexOf(files.getSuffix()) == -1) {
				fileName = fileName + "." + files.getSuffix();
			}
			type = files.getFileType();
		}
		
		String rootDir = "";
		if (ValidateUtil.isValid(getSystemConfig().get("imagePath"))) {
			rootDir = (String) getSystemConfig().get("imagePath");
		} else {
			rootDir = getRequest().getServletContext().getRealPath("/");
		}

		path = rootDir + path;
		// 如何文件名称不存在
		if (!ValidateUtil.isValid(fileName)) {
			fileName = path.substring(path.lastIndexOf("."));
		}
		
		fileName = getFileName(fileName);

		HttpHeaders headers = new HttpHeaders();
		byte[] body = "文件不存在".getBytes();
		HttpStatus httpState = HttpStatus.NOT_FOUND;
		File file = new File(path);
		if (file.exists() && file.isFile()) {

			InputStream is = new FileInputStream(file);
			body = new byte[is.available()];
			is.read(body);
			is.close();
			headers.add("Content-Type", type);
			headers.add("Content-Length", "" + body.length);
			// 加双引号解决火狐空格断点的问题
			headers.add("Content-Disposition", "attachment;filename=\""
					+ fileName + "\"");
			httpState = HttpStatus.OK;

		}

		ResponseEntity<byte[]> entity = new ResponseEntity<>(body, headers,
				httpState);

		return entity;
	}

	/**
	 * type 在这时没有使用,设置只是为了和前台url项符合
	 * 
	 * @param id
	 * @param type
	 * @param filetype
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "downloads")
	public ResponseEntity<byte[]> downloads(
			@RequestParam(value = "delid", required = false) Integer[] id,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "filetype", required = false, defaultValue = "application/vnd.ms-excel") String filetype)
			throws IOException {
		// 确定各个成员变量的值
		String rootDir = "";

		if (ValidateUtil.isValid(getSystemConfig().get("imagePath"))) {
			rootDir = (String) getSystemConfig().get("imagePath");
		} else {
			rootDir = getRequest().getServletContext().getRealPath("/");
		}

		ZipUtil util = new ZipUtil();
		util.setRootDir(rootDir);
		util.setRequest(getRequest());
		for (int i = 0; i < id.length; i++) {
			Files files = filesService.getEntity(id[i]);
			util.addFile(files);
		}
		String fileName = "批量下载" + ".zip";
		String zipFilePath = util.zipData(fileName);

		HttpHeaders headers = new HttpHeaders();
		byte[] body = "文件不存在".getBytes();
		HttpStatus httpState = HttpStatus.NOT_FOUND;
		File file = new File(zipFilePath);
		if (file.exists() && file.isFile()) {

			InputStream is = new FileInputStream(file);
			body = new byte[is.available()];
			is.read(body);
			is.close();
			headers.add("Content-Type", filetype);
			headers.add("Content-Length", "" + body.length);
			fileName = getFileName(fileName);
			headers.add("Content-Disposition", "attachment;filename=\""
					+ fileName + "\"");
			httpState = HttpStatus.OK;
			// 用我那删除
			// file.delete();

		}

		ResponseEntity<byte[]> entity = new ResponseEntity<>(body, headers,
				httpState);

		return entity;
	}

}
