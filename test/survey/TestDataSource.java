package survey;

import java.io.File;
import java.net.URLDecoder;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sniper.springmvc.hibernate.service.impl.ChannelService;
import com.sniper.springmvc.model.Channel;

public class TestDataSource {

	private static ChannelService cs;

	@Before
	public void iniChannelService() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
		cs = (ChannelService) ctx.getBean("channelService");
	}
	@Test
	public void save()
	{
		Channel channel = new Channel();
		System.out.println(cs);
		//System.out.println(cs.getEntity(1));
	}
	
	public void setFile() {

		
		
		String path = "G:\\JSP\\上硅谷\\数据采集系统 视频教程【主讲：徐培成】";
		File file = new File(path);
		File[] lists = file.listFiles();
		
		for(File f: lists){
			
			try {
				String oldName = f.getName();
				String newName = URLDecoder.decode(oldName, "utf-8");
				File newFile = new File(file, newName);
				
				f.renameTo(newFile);
				System.out.println(f.getName());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

}
