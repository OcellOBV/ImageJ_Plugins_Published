package imagepath;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class MoldevOutputPathModel extends MoldevPathModel {

	public MoldevOutputPathModel(String root, sortingMethodNames lastmodified) {
		super(root,lastmodified);
	}


	@Override
	public void setRoot(String value) {
		if (!exists(value)) return;
		root = value;
		plates = getDirectoriesFromFolder(root);
		plates = getFilesFromFilter(plates, "\\d+");
		//plates = getInputPathChoices(getDefaultFilename().substring(0,1) , plates);
		if (plates.length > 0) {
			if (Arrays.asList(plates).contains(plate))
				setPlate(plate);
			else
				setPlate(plates[0]);
		}
	}

	@Override
	public void setPlate(String value) {
		if (!Arrays.asList(plates).contains(value)) return;
		plate = value;
		Path currentPath = Paths.get(root, plate);

		String[] wells = getDirectoriesFromFolder(currentPath.toString());

		well_rows = getSublistbySubstring(wells, 0, 1);

		//well_rows = getInputPathChoices(getDefaultFilename().substring(0,7 + plate.length()), well_rows);

		if (well_rows.length > 0) {
			if (Arrays.asList(well_rows).contains(well_row))
				setRow(well_row);
			else
				setRow(well_rows[0]);
		}
	}
	@Override
	public void setRow(String row) {
		if (!Arrays.asList(well_rows).contains(row)) return;
		well_row = row;
		Path currentPath = Paths.get(root, plate);
		String[] wells = getDirectoriesFromFolder(currentPath.toString());
		well_cols = getSublistbySubstring(wells, 1, 3);
		currentPath = Paths.get(root, plate, row);
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
		if (!Arrays.asList(well_cols).contains(col)) return;
		well_col = col;
		Path currentPath = Paths.get(root, plate, getWell());
		sites = getUniqueFilenameGroupsFromPath(currentPath.toString(), FilenameGroups.SITE);
		//sites = getInputPathChoices(getDefaultFilename().substring(0,12 + plate.length()), sites);
		if (sites!=null && sites.length > 0) {
			if (Arrays.asList(sites).contains(site))
				setSite(site);
			else
				setSite(sites[0]);
		}
	}

	@Override
	public void setSite(String value) {
		if (!Arrays.asList(sites).contains(value)) return;

		site = value;
		Path currentPath = Paths.get(root, plate, getWell());
		channels = getUniqueFilenameGroupsFromPath(currentPath.toString(), FilenameGroups.CHANNEL);
		//channels = getInputPathChoices(getDefaultFilename().substring(0,15 + plate.length() + site.length()), channels); //Not sure
		if (channels.length > 0) {
			if (Arrays.asList(channels).contains(channel))
				setChannel(channel);
			else
				setChannel(channels[0]);
		}
	}


	@Override
	public void setChannel(String value) {
		if (!Arrays.asList(channels).contains(value)) return;
		channel = value;
		Path currentPath = Paths.get(root, plate, getWell());
		times = getUniqueFilenameGroupsFromPath(currentPath.toString(), FilenameGroups.TIME);
		//times =  getInputPathChoices(getDefaultFilename().substring(0,17 + plate.length() + site.length() + channel.length()), times); //Not sure
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
		Path currentPath = Paths.get(root, plate, getWell());
		filenames = getFilesnamesFromPath(currentPath.toString());
		if (filenames.length > 0) {
			String suffix = null;
			if (filename != null)
				if (filename.contains("DAPI")) {
					suffix = filename.substring(filename.indexOf("DAPI") + 7);
				} else if (filename.contains("Cy5")) {
					suffix = filename.substring(filename.indexOf("Cy5") + 6);
				} else if (filename.contains("FITC")) {
					suffix = filename.substring(filename.indexOf("FITC") + 7);
				} else if (filename.contains("TRITC")) {
					suffix = filename.substring(filename.indexOf("TRITC") + 8);
				}
			if (suffix != null) {
				for (String file : filenames) {
					if (file.endsWith(suffix)) {
						setFilename(file);
						return;
					}
				}
			}
			setFilename(filenames[0]);
		}
	}


	@Override
	public String getFullFile() {
		Path fullFile = Paths.get(root, plate, getWell(), filename);
		return fullFile.toString();
	}
	
	@Override
	public String getPath() {
		Path path = Paths.get(root, plate, getWell());
		return path.toString();
	}

	public static void main(String[] args) throws Exception {
		MoldevOutputPathModel moldevPath = new MoldevOutputPathModel("D:\\images\\genmab04\\output",sortingMethodNames.LastModified);
		System.out.println(moldevPath.getFullFile());

		moldevPath.setPlate("1");
		System.out.println(moldevPath.getFullFile());

		moldevPath.setRow("C");
		moldevPath.setCol("03");
		System.out.println(moldevPath.getFullFile());

		moldevPath.setChannel("DAPI");
		System.out.println(moldevPath.getFullFile());

		moldevPath.setFilename(moldevPath.getFilenames()[3]);
		System.out.println(moldevPath.getFullFile());

		moldevPath.setRow("DDD");
		moldevPath.setRow("000333");
		System.out.println(moldevPath.getFullFile());

		moldevPath.setFilename("Ik denk dus ik best.tif");
		System.out.println(moldevPath.getFullFile());
	}



}


