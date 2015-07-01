package com.sniper.springmvc.action.home;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sniper.springmvc.action.RootAction;
import com.sniper.springmvc.coder.RSACoder;
import com.sniper.springmvc.hibernate.service.impl.AdminUserService;
import com.sniper.springmvc.hibernate.service.impl.MailService;
import com.sniper.springmvc.hibernate.service.impl.SetPasswordService;
import com.sniper.springmvc.model.AdminUser;
import com.sniper.springmvc.model.Mail;
import com.sniper.springmvc.model.SetPassword;
import com.sniper.springmvc.utils.DateUtil;
import com.sniper.springmvc.utils.MailUtil;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
@RequestMapping("/password")
public class ForgetPasswordAction extends RootAction {

	@Resource
	private JavaMailSenderImpl sender;

	@Resource
	private AdminUserService adminUserService;

	@Resource
	private SetPasswordService passwordService;

	@Resource
	private MailService mailService;

	@RequestMapping(value = "getPassword", method = RequestMethod.GET)
	public String forgetPassword() {

		return "password/getPassword.ftl";
	}

	@RequestMapping(value = "getPassword", method = RequestMethod.POST)
	public String forgetPassword(Map<String, Object> map,
			@RequestParam("verifyName") String verifyName,
			@RequestParam(value = "username", required = true) String username)
			throws Exception {

		List<String> messages = new ArrayList<>();
		// 每一步都无法后退执行
		// 第一步,发送email
		// 检查验证码

		if (!verifyName.equalsIgnoreCase((String) getRequest().getSession()
				.getAttribute("sessionVerifyName"))) {
			messages.add("验证码不对");
			map.put("messages", messages);
			return "password/getPassword.ftl";
		}

		// 获取并验证用户信息
		AdminUser adminUser = null;
		if (!ValidateUtil.isValid(username)) {
			messages.add("无效用户信息");
			map.put("messages", messages);
			return "password/getPassword.ftl";
		}

		// 发送修改密码链接
		if (username.indexOf("@") == -1) {
			adminUser = adminUserService.validateByName(username);

		} else {
			// 检测email是否存在
			adminUser = adminUserService.validateByEmail(username);
		}

		if (adminUser == null) {
			messages.add("无效用户信息");
			map.put("messages", messages);
			return "password/getPassword.ftl";
		}

		String email = adminUser.getEmail();
		if (!ValidateUtil.isEmail(email)) {
			messages.add("无效邮箱");
			map.put("messages", messages);
			return "password/getPassword.ftl";
		}

		SetPassword password = new SetPassword();
		password.setEmail(email);
		password.setUid(adminUser.getId());
		password.setEndTime(DateUtil.getDateAfter(new Date(), 3));

		String privateKey = request.getServletContext().getInitParameter(
				"privateKey");
		byte[] data = (email + System.currentTimeMillis()).getBytes();
		// 产生签名
		byte[] signByte = RSACoder.sign(data, Base64.decodeBase64(privateKey));
		// 保存相关信息
		password.setSign(Hex.encodeHexString(signByte));
		passwordService.saveEntiry(password);
		Mail mail = new Mail();
		// 读取配置文件中的用户名
		if (sender.getUsername().indexOf("@") == -1) {
			mail.setMailFrom(sender.getUsername() + "@"
					+ sender.getHost().substring(5));
		} else {
			mail.setMailFrom(sender.getUsername());
		}

		mail.setMailTo(email);
		mail.setSubject("密码修改");

		String url = getBasePath() + "password/setPassword?sign="
				+ Hex.encodeHexString(signByte);
		String passwordUrl = getBasePath() + "password/getPassword";

		StringBuffer buffer = new StringBuffer();
		buffer.append("<p>亲爱的用户，您好：</p>");
		buffer.append("<p>请您点击下面链接来修复登录密码:<p>");
		buffer.append("<a href=\"");
		buffer.append(url);
		buffer.append("\" target=\"_blank\">");
		buffer.append(url);
		buffer.append("</a>");
		buffer.append("<p>为了确保您的帐号安全，该链接仅3天内访问有效。</p>");
		buffer.append("<p>如果该链接已经失效，请您点击 <a href=\"");
		buffer.append(passwordUrl);
		buffer.append("\" target=\"_blank\">这里</a> 重新获取修复密码邮件。</p>");
		buffer.append("<p>如果点击链接不工作...</p>");
		buffer.append("<p>请您选择并复制整个链接，打开浏览器窗口并将其粘贴到地址栏中。然后单击\"转到\"按钮或按键盘上的 Enter 键。</p>");

		String emailHtml = buffer.toString();
		mail.setContent(buffer.toString());
		MailUtil mailUtil = new MailUtil();
		mailUtil.setSender(sender);

		try {

			System.out.println(mail);
			mail = mailUtil.sendHtmlMail(mail);
			mailService.saveEntiry(mail);

			messages.add("邮件发送成功,点击去<a href=\"https://" + "mail."
					+ mail.getMailTo().substring(mail.getMailTo().indexOf("@")) + "\">我的邮箱.</a>");

		} catch (Exception e) {
			messages.add("邮件发送失败,请确认你的邮箱是否有效,如果无效请联系管理员.");
			messages.add(e.getMessage());
		}

		map.put("emailHtml", emailHtml);
		map.put("messages", messages);
		return "password/getResult.ftl";
	}

	/**
	 * 用于接受用户传递过来的修改用户的url
	 * 
	 * @return
	 * @throws SignatureException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws DecoderException
	 */
	@RequestMapping(value = "setPassword", method = RequestMethod.GET)
	public String setPassword(Map<String, Object> map,
			@RequestParam("sign") String sign) throws InvalidKeyException,
			NoSuchAlgorithmException, InvalidKeySpecException,
			SignatureException, DecoderException {

		List<String> messages = new ArrayList<>();

		if (!ValidateUtil.isValid(sign)) {
			messages.add("签名无效");
			map.put("messages", messages);
			return "password/getSuccess.ftl";
		}

		SetPassword setPassword = passwordService.validBySign(sign);
		if (setPassword == null) {
			messages.add("签名不存在");
			map.put("messages", messages);
			return "password/getSuccess.ftl";
		}

		if (setPassword.isSignaTrue()) {
			messages.add("此签名已经被使用");
			map.put("messages", messages);
			return "password/getSuccess.ftl";
		}

		Date endtime = setPassword.getEndTime();
		if (endtime.before(new Date())) {
			messages.add("签名已经过期无法验证");
			map.put("messages", messages);
			return "password/getSuccess.ftl";
		}

		String publicKey = request.getServletContext().getInitParameter(
				"publicKey");

		// 验证签名,多此一举,自我感觉因为保存的时候已经验证过一次了
		boolean status = RSACoder.verify(setPassword.getEmail().getBytes(),
				Base64.decodeBase64(publicKey),
				Hex.decodeHex(sign.toCharArray()));
		if (status == false) {
			messages.add("签名无法验证");
			map.put("messages", messages);
		}
		// 下面是修改用户名的操作
		return "password/getSuccess.ftl";

	}

	@RequestMapping(value = "setPassword", method = RequestMethod.POST)
	public String setPassword(Map<String, Object> map,
			@RequestParam("sign") String sign,
			@RequestParam("passwordC") String passwordC,
			@RequestParam("password") String password)
			throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, SignatureException, DecoderException {

		List<String> messages = new ArrayList<>();
		if (!ValidateUtil.isValid(sign)) {
			messages.add("签名无效");
			map.put("messages", messages);
			return "password/getSuccess.ftl";
		}

		SetPassword setPassword = passwordService.validBySign(sign);
		if (setPassword == null) {
			messages.add("签名不存在");
			map.put("messages", messages);
			return "password/getSuccess.ftl";
		}

		if (setPassword.isSignaTrue()) {
			messages.add("此签名已经被使用");
			map.put("messages", messages);
			return "password/getSuccess.ftl";
		}

		Date endtime = setPassword.getEndTime();
		if (endtime.before(new Date())) {
			messages.add("签名已经过期无法验证");
			map.put("messages", messages);
			return "password/getSuccess.ftl";
		}

		String publicKey = request.getServletContext().getInitParameter(
				"publicKey");

		// 验证签名,多此一举,自我感觉因为保存的时候已经验证过一次了
		boolean status = RSACoder.verify(setPassword.getEmail().getBytes(),
				Base64.decodeBase64(publicKey),
				Hex.decodeHex(sign.toCharArray()));
		if (status == false) {
			messages.add("签名无法验证");
			map.put("messages", messages);
			return "password/getSuccess.ftl";
		}
		// 下面是修改用户名的操作

		if (!passwordC.equals(password)) {
			messages.add("密码输入不一致");
			map.put("messages", messages);
			return "password/getSuccess.ftl";
		}
		// 一切都ok修改用户名密码
		AdminUser adminUser = adminUserService.getEntity(setPassword.getId());

		adminUser.setPassword(passwordC);
		setPassword.setSignaTrue(true);

		try {
			passwordService.updateEntiry(setPassword);
			adminUserService.saveOrUpdateEntiry(adminUser);
			return "redirect:/admin/login";
		} catch (Exception e) {
			messages.add("密码修改失败");
			map.put("messages", messages);
		}

		return "password/getSuccess.ftl";

	}
	
	public static void main(String[] args) {
		String email = "laolang81@126.com";
		
		System.out.println(email.substring(email.indexOf("@")));
	}

}
