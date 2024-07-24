package imagepath;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import utility.FileUtil;


public abstract class MoldevPathModel extends PathModel {
	protected String plate;
	protected String well_row;
	protected String well_col;
	protected String site;
	protected String channel;
	protected String time;
	protected String listOfAvailablePaths;
	protected String[] times = new String[] {}; 
	protected String[] plates = new String[] {};
	protected String[] all_well_rows = new String[] {};
	protected String[] all_well_cols = new String[] {};
	protected String[] well_rows = new String[] {};
	protected String[] well_cols = new String[] {};
	protected String[] sites = new String[] {};
	protected String[] channels = new String[] {};


	private final Pattern pattern = Pattern.compile("P(\\d+)_Well_([A-Z])(\\d+)_s(\\d+)_c_(.+)_t(\\d+).tif", Pattern.CASE_INSENSITIVE);

	protected enum FilenameGroups {
		ALL,
		PLATE,
		WELL_ROW,
		WELL_COL,
		SITE,
		CHANNEL,
		TIME
	}

	public MoldevPathModel(String value,sortingMethodNames sortingMethod) {
		this.sortingMethod = sortingMethod;
		setRoot(value);
	}



	public String getPlate() {
		return plate;
	}

	public String getWell() {
		return well_row + well_col;
	}

	public String getWellRow() {
		return well_row;
	}

	public String getWellCol() {
		return well_col;
	}

	public String getSite() {
		return site;
	}

	public String getTime() {
		return time;
	}

	public String getChannel() {
		return channel;
	}

	public String[] getPlates() {
		return plates;
	}

	public String[] getWellRows() {
		return well_rows;
	}

	public String[] getWellCols() {
		return well_cols;
	}

	public String[] getSites() {
		return sites;
	}

	public String[] getChannels() {
		return channels;
	}

	public String[] getTimes() {
		return times;
	}



	public String getDefaultFilename() {
		return "P" + plate + "_Well_" + getWell() + "_s" + site + "_c_" + channel + "_t" + time + ".tif";
	}

	public abstract String getFullFile();

	public abstract void setPlate(String value);

	public abstract void setRow(String wellRow);

	public abstract void setCol(String wellCol);

	public abstract void setSite(String value);

	public abstract void setTime(String value);

	public abstract void setChannel(String value);




	protected String[] getUniqueFilenameGroupsFromPath(String path, FilenameGroups groupNr) {
		File currentDir = new File(path);
		File[] files = currentDir.listFiles();
		HashSet<String> matches = new HashSet<>();

		if (files == null) return new String[0];

		for(File file: files) {
			String name = file.getName();
			Matcher matcher = pattern.matcher(name);
			if (matcher.find()) {
				String match = matcher.group(groupNr.ordinal());
				matches.add(match);
			}
		}

		List<String> matchesList = new ArrayList<String>(matches);
		Collections.sort(matchesList);
		return matchesList.toArray(new String[0]);

	}


	@Override
	public String[] getFilesnamesFromPath(String currentPath) {
		File directory = new File(currentPath);
		File[] files;
		files = directory.listFiles((File file) -> file.getName().endsWith(".tif") && file.getName().contains(getWell()) && file.getName().contains(channel) && file.getName().contains("s" + site) && file.getName().contains("t" + time));

		if (files == null) return new String[0];

		List<String> fileList = sortFilenamesAction(files);

		filenames = fileList.toArray(new String[0]);
		return filenames;
	}


	protected String[] getSublistbySubstring(String[] list, int beginIndex, int endIndex) {
		Stream<String> stream = Arrays.stream(list);
		List<String> sublist = stream.map(x -> x.substring(beginIndex, endIndex)).distinct().collect(Collectors.toList());
		return sublist.toArray(new String[0]);
	}


	public static MoldevPathModel setImageFromFullImagePath(String fullOutputPath) {
		MoldevPathModel moldevOutputPath=getMoldevPathModel(fullOutputPath);
		if(moldevOutputPath!=null) {
			int startImageName=fullOutputPath.lastIndexOf("\\", fullOutputPath.length())+1;
			String fileName=fullOutputPath.substring(startImageName, fullOutputPath.length());

			if(fileName.contains("_Well_")){
				int indexOfWell=fileName.indexOf("_Well_", 0);
				String plate=fileName.substring(1, indexOfWell);
				String wellRow=fileName.substring(indexOfWell+6, indexOfWell+7);
				String wellCol=fileName.substring(indexOfWell+7, indexOfWell+9);


				int indexOfSite = indexOfWell+11;
				StringBuilder siteBuilder = new StringBuilder();
				while (indexOfSite < fileName.length() && Character.isDigit(fileName.charAt(indexOfSite))) {
					siteBuilder.append(fileName.charAt(indexOfSite));
					indexOfSite++;
				}


				String site=fileName.substring(indexOfWell+11, indexOfWell+12);

				int indexOfTime=fileName.indexOf("t", 0) + 1;
				StringBuilder timeBuilder = new StringBuilder();
				while (indexOfTime < fileName.length() && Character.isDigit(fileName.charAt(indexOfTime))) {
					timeBuilder.append(fileName.charAt(indexOfTime));
					indexOfTime++;
				}
				String time = timeBuilder.toString();


				moldevOutputPath.setPlate(plate);
				moldevOutputPath.setRow(wellRow);
				moldevOutputPath.setCol(wellCol);
				moldevOutputPath.setSite(site);
				moldevOutputPath.setTime(time);
				if(fileName.contains("_c_")) {
					int endChannelPart=fileName.lastIndexOf("_t" + time, fullOutputPath.length());
					String channel=fileName.substring(indexOfWell+15, endChannelPart);
					moldevOutputPath.setChannel(channel);
				}
			}
			moldevOutputPath.setFilename(fileName);
			return moldevOutputPath;
		}
		return null;
	}

	private static MoldevPathModel getMoldevPathModel(String fullOutputPath) {
		String folder=getFolderFromFullImagePath(fullOutputPath, 3);
		MoldevPathModel moldevOutputPath = MoldevPathModelFactory.createMoldevPath(folder,sortingMethodNames.LastModified);
		if(moldevOutputPath!=null) {
			return moldevOutputPath;
		}
		else {
			folder=getFolderFromFullImagePath(fullOutputPath, 2);
			return moldevOutputPath = MoldevPathModelFactory.createMoldevPath(folder,sortingMethodNames.LastModified);

		}
	}

	private static String getFolderFromFullImagePath(String fullOutputPath, int numberOfFoldersAboveIt) {
		String folder="";
		int lastcharFolder= FileUtil.nthLastIndexOf(fullOutputPath, "\\", numberOfFoldersAboveIt);
		if(lastcharFolder!=-1) {
			folder=fullOutputPath.substring(0, lastcharFolder); 
		}
		return folder;
	}
}


