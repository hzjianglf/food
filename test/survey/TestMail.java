package survey;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class TestMail {

	private static ApplicationContext ctx;

	@Before
	public void init() {
		ctx = new ClassPathXmlApplicationContext("beans.xml");

	}
	@Test
	public void test() {
		
		JavaMailSenderImpl sender = (JavaMailSenderImpl) ctx.getBean("mailSender");
		
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setSubject("测试");
		mailMessage.setFrom("zhaoqiang@trade.gov.cn");
		mailMessage.setText("测试");
		mailMessage.setTo("laolang81@126.com");
		
		sender.send(mailMessage);
		//System.out.println(ctx);
	}

}
