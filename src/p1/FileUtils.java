package p1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.hadoop.hbase.util.Bytes;

public class FileUtils {
	public static byte[] getFileBytes(File file) throws Exception {
		FileInputStream fis;
		fis = new FileInputStream(file);
		byte[] data = new byte[fis.available()];
		fis.read(data);
		fis.close();
		return data;
	}

	public static File getFileFromByte(byte[] bytes, String path) {
		if (bytes == null) {
			return null;
		}
		File f = new File(path);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			fos.write(bytes);
			fos.close();
/*			PrintWriter pt =new PrintWriter(fos);
			pt.println(Bytes.toString(bytes));
			pt.close();*/
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				fos = null;
			}
		}
		return f;
	}
}
