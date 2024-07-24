/**************************************************************************
 * Copyright (C) OcellO B.V. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Kuan Yan <kuan.yan@ocello.nl>, Jul 25, 2017 
 **************************************************************************/
package utility;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * @author Kuan Yan
 * @since Jul 25, 2017
 * @version 1.0
 */
public class FileUtil 
{
	public static void main(String[] args)
	{
		Collection<String> paths = scanFolderForFile("d:\\temp", ".tif");
		for(String path: paths)
		{
			System.out.println(path);
		}
	}
	
	public static Collection<String> scanFolderForFile(String rootpath)
	{
		return scanFolderForFile(rootpath, null);
	}
	
	/**
	 * 
	 * Jul 25, 2017
	 * @param rootpath
	 * @param fileEXT such as ".txt" or ".tif", if no extension-based filter, use null 
	 * @return
	 */
	public static Collection<String> scanFolderForFile(String rootpath, String fileEXT)
	{
		Collection<File> files = scanFolderForFile(new File(rootpath), fileEXT);
		ArrayList<String> filepaths = new ArrayList<String>();
		
		for(File f: files)
		{
			filepaths.add(f.getAbsolutePath());
		}
		
		return filepaths;
	}
	
	/**
	 * 
	 * Jul 25, 2017
	 * @param rootdir
	 * @return
	 */
	public static Collection<File> scanFolderForFile(File rootdir)
	{
		return scanFolderForFile(rootdir, null);
	}
	
	/**
	 * 
	 * Jul 25, 2017
	 * @param rootdir
	 * @param fileEXT such as ".txt" or ".tif", if no extension-based filter, use null 
	 * @return
	 */
	public static Collection<File> scanFolderForFile(File rootdir, String fileEXT)
	{
		String ext = new String(fileEXT);//make clone
		if(!ext.startsWith("."))//if not start with dot
			ext="."+ext;//add dot
		
		ext = ext.toLowerCase();//match case
		
		
		ArrayList<File> allFiles = new ArrayList<File>();//initialize storage
		
		if(rootdir.exists()&&rootdir.isDirectory())//if root directory exists
		{
			File[] stuffs = rootdir.listFiles();//scan first layer children
			for(File stuff: stuffs)
			{
				if(stuff.isFile())//a file
				{
					boolean addFile = false;
					if(ext!=null)//extension set
					{
						addFile = stuff.getName().toLowerCase().endsWith(ext);//check if extension match, case matched
					}
					else
					{
						addFile = true;//always true
					}
					
					if(addFile)
						allFiles.add(stuff);
				}
				else//a directory
				{
					allFiles.addAll(scanFolderForFile(stuff, ext));
				}
			}
			
		}
		
		return allFiles;
	}
	
	public static int nthLastIndexOf(String str, String c, int n) { // TODO move this function to a better location.
        if (str == null || n < 1)
            return -1;
        int pos = str.length();
        while (n-- > 0 && pos != -1)
            pos = str.lastIndexOf(c, pos - 1);
        return pos;
}
	
	public static boolean isValidPath(String folder) {
		Path path = Paths.get(folder);
		if (!Files.exists(path) || folder.equals("")) {
			System.out.println("Path does not exists");
			//add watch info
			return false;
		}
		return true;
	}
	
	public static String getDateAndTimeAsString() {
		DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		return df.format(new Date());
	}
}
