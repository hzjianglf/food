package survey;

import org.springframework.beans.BeanUtils;

public class BeanUtilTest {

	public class Abc {
		private String a;
		private int b;
		private boolean c;

		public String getA() {
			return a;
		}

		public void setA(String a) {
			this.a = a;
		}

		public int getB() {
			return b;
		}

		public void setB(int b) {
			this.b = b;
		}

		public boolean isC() {
			return c;
		}

		public void setC(boolean c) {
			this.c = c;
		}

	}

	public void b() {
		Abc abc = new Abc();
		abc.setA("a");
		abc.setB(1);
		abc.setC(true);

		Abc a = new Abc();
		System.out.println(a.isC());
		BeanUtils.copyProperties(abc, a);
		System.out.println(a.isC());
	}

	public static void main(String[] args) {
		BeanUtilTest beanUtilTest = new BeanUtilTest();
		beanUtilTest.b();
	}
}
