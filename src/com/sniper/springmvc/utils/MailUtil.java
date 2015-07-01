package com.sniper.springmvc.utils;

import java.io.File;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.sniper.springmvc.model.Files;
import com.sniper.springmvc.model.Mail;

public class MailUtil {

	/**
	 * 日志记录 log4j
	 */
	protected static Log log = LogFactory.getLog(MailUtil.class);

	private JavaMailSenderImpl sender;

	public void setSender(JavaMailSenderImpl sender) {
		this.sender = sender;
	}

	/**
	 * 文本邮件发送
	 * 
	 * @param mail
	 */
	public Mail sendTextMail(Mail mail) {

		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setSubject(mail.getSubject());
			mailMessage.setFrom(mail.getMailFrom());
			mailMessage.setText(mail.getContent());
			mailMessage.setTo(DataUtil.StringToStringArray(mail.getMailTo()));
			if (ValidateUtil.isValid(mail.getMailCc())) {
				String[] cc = DataUtil.StringToStringArray(mail.getMailCc());
				mailMessage.setCc(cc);
			}
			if (ValidateUtil.isValid(mail.getMailBcc())) {
				String[] bcc = DataUtil.StringToStringArray(mail.getMailBcc());
				mailMessage.setBcc(bcc);
			}
			mail.setMimeMessage(false);
			mail.setSend(true);
			sender.send(mailMessage);
			mail.setErrorMessage("Success");
		} catch (Exception e) {
			log.error(e, e);
			mail.setSend(false);
			mail.setErrorMessage(e.getMessage());
		}

		return mail;
	}

	/**
	 * 文本邮件发送
	 * 
	 * @param mail
	 */
	public Mail sendHtmlMail(Mail mail) {

		MimeMessage m = sender.createMimeMessage();

		try {

			MimeMessageHelper helper = new MimeMessageHelper(m, true, "UTF-8");
			helper.setTo(DataUtil.StringToStringArray(mail.getMailTo()));
			helper.setSubject(mail.getSubject());
			helper.setFrom(mail.getMailFrom());
			helper.setText(mail.getContent(), true);

			if (ValidateUtil.isValid(mail.getMailCc())) {
				helper.setCc(DataUtil.StringToStringArray(mail.getMailCc()));
			}
			if (ValidateUtil.isValid(mail.getMailBcc())) {
				helper.setBcc(DataUtil.StringToStringArray(mail.getMailBcc()));
			}

			List<Files> files = mail.getFiles();
			for (Files f : files) {
				File file = new File(f.getNewPath());
				FileSystemResource resource = new FileSystemResource(file);
				helper.addAttachment(f.getOldName(), resource);
			}
			mail.setMimeMessage(true);
			sender.send(m);
			mail.setSend(true);
			mail.setErrorMessage("Success");
		} catch (Exception e) {
			log.error(e, e);
			mail.setSend(false);
			mail.setErrorMessage(e.getMessage());
		}
		return mail;
	}

}
