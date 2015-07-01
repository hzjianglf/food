package com.sniper.springmvc.action.home;

import java.io.ByteArrayInputStream;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.sniper.springmvc.utils.Verify;
import com.sniper.springmvc.utils.VerifyCode;

/**
 * 提供图片验证码
 * 
 * @author laolang
 * 
 */
@SessionAttributes("sessionVerifyName")
@Controller
public class VerifyAction {

	private static final String VALIDATECODE = "sessionVerifyName";

	public static String getValidatecode() {
		return VerifyAction.VALIDATECODE;
	}

	@ResponseBody
	@RequestMapping("verify")
	public ResponseEntity<byte[]> verify(Map<String, Object> map)
			throws Exception {
		// 如果开启Hard模式，可以不区分大小写
		String securityCode = VerifyCode.getSecurityCode(5,
				VerifyCode.SecurityCodeLevel.Hard, false).toLowerCase();
		// 获取默认难度和长度的验证码
		// String securityCode = VerifyCode.getSecurityCode();
		// 图片流
		ByteArrayInputStream imageStream = Verify
				.getImageAsInputStream(securityCode);
		// 放入session中
		map.put(VALIDATECODE, securityCode);

		byte[] body = new byte[imageStream.available()];
		imageStream.read(body);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "image/jpeg");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		HttpStatus httpState = HttpStatus.OK;

		ResponseEntity<byte[]> entity = new ResponseEntity<>(body, headers,
				httpState);

		return entity;

	}

}