package imagepath;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


public class MoldevInputPathModel extends MoldevPathModel {

	public MoldevInputPathModel(String value, sortingMethodNames sortingMethodNames) {
		super(value,sortingMethodNames);
	}

	@Override
	public void setRoot(String value) {
		if (!exists(value)) return;
		root = value;
		plates = getDirectoriesFromFolder(root);
		plates = getFilesFromFilter(plates, "\\d+");
		//plates = getInputPathChoices(getDefaultFilename().substring(0,1) , plates);
		if (plates.length > 0) setPlate(plates[0]);
	}
	
	@Override
	public void setPlate(String value) {
		if (!Arrays.asList(plates).contains(value)) return;
		plate = value;
		
		Path currentPath = Paths.get(root, plate);
		well_rows = getUniqueFilenameGroupsFromPath(currentPath.toString(), FilenameGroups.WELL_ROW);
		//well_rows = getInputPathChoices(this.getDefaultFilename().substring(0,7 + plate.length()), well_rows);
		if (well_rows.length > 0) {
			if (Arrays.asList(well_rows).contains(well_row))
				setRow(well_row);
			else
				setRow(well_rows[0]);
		}
	}
	
	@Override
	public void setRow(String row) {
		well_row = row;
		
		Path currentPath = Paths.get(root, plate);
		well_cols = getUniqueFilenameGroupsFromPath(currentPath.toString(), FilenameGroups.WELL_COL);
		//well_cols = getInputPathChoices(this.getDefaultFilename().substring(0,8 + plate.length()), well_cols);
		if (well_cols.length > 0) {
			if (Arrays.asList(well_cols).contains(well_col))
				setCol(well_col);
			else
				setCol(well_cols[0]);
		}
	}
	
	@Override
	public void setCol(String col) {
		well_col = col;
		
		Path currentPath = Paths.get(root, plate);
		
		sites = getUniqueFilenameGroupsFromPath(currentPath.toString(), FilenameGroups.SITE);
		//sites = getInputPathChoices(getDefaultFilename().substring(0,12 + plate.length()), sites);
		if (sites.length > 0) {
			if (Arrays.asList(sites).contains(site))
				setSite(site);
			else
				setSite(sites[0]);
		}
		
		channels = getUniqueFilenameGroupsFromPath(currentPath.toString(), FilenameGroups.CHANNEL);
		//channels = getInputPathChoices(getDefaultFilename().substring(0,15 + plate.length() + site.length()), channels);
		if (channels.length > 0) {
			if (Arrays.asList(channels).contains(channel))
				setChannel(channel);
			else
				setChannel(channels[0]);
		}
	}
	
	@Override
	public void setSite(String value) {
		if (!Arrays.asList(sites).contains(value)) return;
		site = value;
	}
	
	@Override
	public void setChannel(String value) {
		if (!Arrays.asList(channels).contains(value)) return;
		channel = value;
		Path currentPath = Paths.get(root, plate);
		times = getUniqueFilenameGroupsFromPath(currentPath.toString(), FilenameGroups.TIME);
		//times =  getInputPathChoices(getDefaultFilename().substring(0,17 + plate.length() + site.length() + channel.length()), times);
		if (times.length > 0) {
			if (Arrays.asList(times).contains(time))
				setTime(time);
			else
				setTime(times[0]);
		}
	}
	
	@Override
	public void setTime(String value) {
		if (!Arrays.asList(times).contains(value)) return;
		time = value;
		Path currentPath = Paths.get(root, plate);
		filenames = getFilesnamesFromPath(currentPath.toString());
		if (filenames.length > 0) {
			if (Arrays.asList(filenames).contains(filename))
				setFilename(filename);
			else
				setFilename(filenames[0]);
		}
	}
	
	@Override
	public String getFullFile() {
		Path fullFile = Paths.get(root, plate, getDefaultFilename());
		return fullFile.toString();
	}
	
	@Override
	public String getPath() {
		Path path = Paths.get(root, plate);
		return path.toString();
	}
	
	public static void main(String[] args) throws Exception {

		MoldevPathModel moldevPath = new MoldevInputPathModel("D:\\images\\genmab04\\input",sortingMethodNames.LastModified);
		System.out.println(moldevPath.getFullFile());
		System.out.println(moldevPath.getSites()[0]);
		
		moldevPath.setChannel("TRITC");
		System.out.println(moldevPath.getFullFile());
		
		moldevPath.setPlate("2");
		System.out.println(moldevPath.getFullFile());

		moldevPath.setRow("D");
		moldevPath.setCol("03");
		System.out.println(moldevPath.getFullFile());
		
		System.out.println(moldevPath.getFilenames().length);
		System.out.println(moldevPath.getFilename());
		
		moldevPath = new MoldevOutputPathModel("",sortingMethodNames.LastModified);
		
	}

}

