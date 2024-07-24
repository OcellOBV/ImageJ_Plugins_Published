/**************************************************************************
 * Copyright (C) OcellO B.V. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Kuan Yan <kuan.yan@ocello.nl>, May 2, 2020 
 **************************************************************************/


import ij.ImagePlus;
import ij.gui.ImageRoi;
import ij.gui.Overlay;

/**
 * @author Kuan Yan
 * @since May 2, 2020
 * @version 1.0
 */
public class OverlayUtils 
{
	public static void addOverlay(ImagePlus destImg, ImagePlus overlayImg, double opacity)
	{
		addOverlay(destImg, overlayImg, opacity, true);
	}
	/**
	 * putting the overlay image as overlay ROI on top of the destImg, the destImg must be flatten to combine overlay really into the image itself
	 * @param destImg destination image
	 * @param overlayImg overlay image
	 * @param opacity of overlay image, between 0 (invisible) and 1 (mask-like)
	 * @param cleanPrevious true if all previous overlay to be removed before putting on new overlay
	 */
	public static void addOverlay(ImagePlus destImg, ImagePlus overlayImg, double opacity, boolean cleanPrevious)
	{
		int ss = destImg.getStackSize();
		
		Overlay overlayList = destImg.getOverlay();//get current overlay info
    	if (overlayList!=null)//overlay instance defined before
    	{
    		if(cleanPrevious)//if to clean previous overlay
    			overlayList.clear();//clean all previous overlays
    	}
    	else//missing overlay instance
    	{
    		overlayList = new Overlay();//create one if empty
    	}
    	
		for(int s=1;s<=ss;s++)//for each section from overlay image, ignore stack size mismatch
		{				
	    	ImageRoi roi = new ImageRoi(0, 0, overlayImg.getStack().getProcessor(s));//get current section
	    	roi.setName(overlayImg.getTitle());
	    	roi.setOpacity(opacity);//set 
	    	roi.setPosition(s);//set to proper z-stack position
	    	overlayList.add(roi);//add new overlay
		}
		destImg.setOverlay(overlayList);//put back to section
	}
}
