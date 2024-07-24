package imagepath;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class RegularPathModel extends PathModel {


	public RegularPathModel(String value,sortingMethodNames sortingMethod) {
		this.sortingMethod = sortingMethod;
		setRoot(value);
	}
	
	@Override
	public String getPath() {
		Path path = Paths.get(root);
		return path.toString();
	}
	
	@Override
	public String getFullFile() {
		Path fullFile = Paths.get(root, filename);
		return fullFile.toString();
	}

	@Override
	public void setRoot(String value) {
		if (!exists(value)) return;
		root = value;
		filenames = getFilesnamesFromPath(root);

		if (filenames.length > 0) {
			if (Arrays.asList(filenames).contains(filename))
				setFilename(filename);
			else
				setFilename(filenames[0]);
		}
	}

	@Override
	public String getDefaultFilename() {
		return root;
	}



}
