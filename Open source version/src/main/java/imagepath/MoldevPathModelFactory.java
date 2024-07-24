package imagepath;

import imagepath.PathModel.sortingMethodNames;

public class MoldevPathModelFactory {

	public static MoldevPathModel createMoldevPath(String root,sortingMethodNames sortingMethodNames) {

		MoldevPathModel moldevPath = new MoldevInputPathModel(root, sortingMethodNames);
		
		System.out.println(moldevPath.getFilenames().length );
		
		if (moldevPath.getFilenames().length == 1) return moldevPath;
	
		moldevPath = new MoldevOutputPathModel(root,sortingMethodNames);
		
		return moldevPath;
	}
	
}
