package imagepath;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class PathModel {

	public enum sortingMethodNames {
		AZ,
		ZA,
		LastModified
	}
	protected sortingMethodNames sortingMethod;
	protected String root;
	protected String filename;
	protected String[] filenames = new String[] {};
	
	private FileFilter directoryFileFilter = new FileFilter() {
		public boolean accept(File file) {
			return file.isDirectory();
		}
	};
	
	public String[] getFilenames() {
		return filenames;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public String getRoot() {
		return root;
	}

	public void setFilename(String value) {
		if (!Arrays.asList(filenames).contains(value)) return;
		filename = value;
		System.out.println(filename);
	}
	
	public void setSortingMethod(sortingMethodNames  sortingMethod) {
		this.sortingMethod = sortingMethod;
		sortFilenames();
	}

	public void sortFilenames() {
		if (! (this instanceof MoldevEmptyPath) && ! (this instanceof RegularEmptyPath)) {
			String currentPath = getPath();
			filenames = getFilesnamesFromPath(currentPath);
		}
	}

	protected String[] getDirectoriesFromFolder(String folder) {
		File directory = new File(folder);
		File[] files = directory.listFiles(directoryFileFilter);
		return getDirectoryNames(files);
	}


	public String[] getFilesFromFilter(String[] arr, String pattern) {
		Stream<String> stream = Arrays.stream(arr);
		List<String> list = stream.filter(i -> i.matches(pattern)).collect(Collectors.toList());
		return list.toArray(new String[0]);
	}
	
	protected String[] getDirectoryNames(File[] files) {
		Stream<File> stream = Arrays.stream(files);
		List<String> directories = stream.map(x -> x.getName()).collect(Collectors.toList());
		return directories.toArray(new String[0]);
	}
	
	public String[] getFilesnamesFromPath(String currentPath) {
		File directory = new File(currentPath);
		File[] files = directory.listFiles((File file) -> file.getName().endsWith(".tif"));

		if (files == null) return new String[0];

		List<String> fileList = sortFilenamesAction(files);

		filenames = fileList.toArray(new String[0]);
		return filenames;
	}

	protected boolean exists(String value) {
		File file = new File(value);
		boolean exists = file.isDirectory();
		if (!exists) {
			// TODO throw exception?
		}
		return exists;
	}
	
	public List<String> sortFilenamesAction(File[] files) {
		if (sortingMethod == sortingMethodNames.LastModified) {
			Arrays.sort(files, new Comparator<File>() {
				public int compare(File f1, File f2)
				{
					return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
				} });
		}
		Stream<File> stream = Arrays.stream(files);
		List<String> fileList = stream.map(x -> x.getName()).collect(Collectors.toList());
		switch(sortingMethod) {
		case AZ :
			Collections.sort(fileList);
			break;
		case ZA :
			Collections.sort(fileList, Comparator.reverseOrder());
			break;
		default:
			break;
		}
		return fileList;
	}
	
	public abstract void setRoot(String value);
	public abstract String getPath();
	public abstract String getDefaultFilename();
	public abstract String getFullFile();


	
}
