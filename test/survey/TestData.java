package survey;

import com.sniper.springmvc.utils.DataUtil;

public class TestData {

	public static void main(String[] args) {
		long millis = System.currentTimeMillis();
		String str = "s121fdfs";
		for (int i = 0; i < 29999999; i++) {
			//DataUtil.firstLetterToUpper("s121fdfs" + i);
			DataUtil.letterToUpper(str);
			
		}
		System.out.println((System.currentTimeMillis() - millis) / 1000 + "秒");
	}
}
