package p1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Picture {

	public static void main(String[] args) throws Exception {
		File file = new File("1.gif");

		FileInputStream fis;
		fis = new FileInputStream(file);
		byte[] bytes = new byte[fis.available()];
		fis.read(bytes);
		for (int i = 1; i <= 70000; i++) {
			File f = new File("result/" + i + ".gif");
			FileOutputStream fos = null;
			fos = new FileOutputStream(f);
			fos.write(bytes);
			fos.close();
			System.out.println(i);

		}
	}

}
