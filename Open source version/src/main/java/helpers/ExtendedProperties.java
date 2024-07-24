package helpers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ExtendedProperties extends Properties {
	public void putWithException(String key, String value) {
		try {
			put(key, value);
		}
		catch (Exception e) {
			// TODO Ignoring this property
		}
	}
	
	public void write(String filename) {
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(filename);
		} catch (FileNotFoundException e) {
			// TODO catch
			e.printStackTrace();
		}

		try {
			store(outputStream, "Store ExtendeProperties");
		} catch (IOException e) {
			// TODO catch
			e.printStackTrace();
		}

	}
}
