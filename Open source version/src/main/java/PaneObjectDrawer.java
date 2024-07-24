/**************************************************************************
 * Copyright (C) OcellO B.V. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Kuan Yan <kuan.yan@ocello.nl>, May 2, 2020 
 **************************************************************************/


import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ByteProcessor;

/**
 * @author Kuan Yan
 * @since May 2, 2020
 * @version 1.0
 */
public class PaneObjectDrawer implements MouseListener, KeyListener
{
	/**
	 * May 2, 2020
	 * @param args
	 */
	public static void main(String[] args) 
	{	
		String rawimgpath = "C:\\temp\\autotuner\\P1_Well_I04_s1_c_TRITC_t1-s14.tif";
		
		new PaneObjectDrawer(rawimgpath);
	}

	
	private final String imgpath;
	private final ImagePlus rawimg, binimg;
	private final int imgWidth, imgHeight, imgStackSize;
	public PaneObjectDrawer(String imgpath)
	{
		this.imgpath = imgpath;
		
		new ImageJ(ImageJ.NO_SHOW);//invisible mode so window tools can still work
		IJ.setTool("freehand");
		
		this.rawimg = IJ.openImage(imgpath);
		this.imgWidth = this.rawimg.getWidth();
		this.imgHeight = this.rawimg.getHeight();
		this.imgStackSize = this.rawimg.getStackSize();
		
		ImageStack bStack = new ImageStack(this.imgWidth, this.imgHeight);
		for(int s=1;s<=this.imgStackSize;s++)
		{
			ByteProcessor bip = new ByteProcessor(this.imgWidth, this.imgHeight);
			bStack.addSlice(s+"", bip);
		}
		this.binimg = new ImagePlus("", bStack);
		IJ.run(this.binimg, "Red", "");
		this.binimg.show();
		
		this.rawimg.show();		
		OverlayUtils.addOverlay(this.rawimg, this.binimg, 0.2);
		
		this.rawimg.getWindow().getCanvas().addMouseListener(this);
		this.rawimg.getWindow().getCanvas().addKeyListener(this);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) 
	{
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) 
	{	
		if (e.getKeyCode()==KeyEvent.VK_ENTER)
		{
			int slice = this.rawimg.getCurrentSlice();
            System.out.println("Hello");
            int minX = (int)rawimg.getRoi().getBounds().getMinX();
            int minY = (int)rawimg.getRoi().getBounds().getMinY();
            int maxX = (int)rawimg.getRoi().getBounds().getMaxX();
            int maxY = (int)rawimg.getRoi().getBounds().getMaxY();
            for(int x = minX ; x<=maxX;x++)
            {
            	for(int y = minY ; y<=maxY;y++)
            	{
            		boolean inside = rawimg.getRoi().contains(x, y);
            		if(inside)
            		{
            			this.binimg.getStack().getProcessor(slice).set(x, y, 255);
            			System.out.println(x+","+y);
            		}
            	}
            }
            this.binimg.updateAndRepaintWindow();
            this.rawimg.updateAndRepaintWindow();
        }
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
