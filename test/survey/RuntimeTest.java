package survey;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.junit.Test;

public class RuntimeTest {

	@Test
	public void test() {

		try {
			String commands = "ls -l /";

			Process process = Runtime.getRuntime().exec(commands);

			// for showing the info on screen

			InputStreamReader ir = new InputStreamReader(
					process.getInputStream());

			BufferedReader input = new BufferedReader(ir);

			String line;

			while ((line = input.readLine()) != null) {
				System.out.println(line);
			}
		} catch (Exception e) {
			System.err.println("IOException " + e.getMessage());
		}

	}
}
