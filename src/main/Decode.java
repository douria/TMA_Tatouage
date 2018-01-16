package main;

import java.awt.Frame;
import java.awt.Image;
import java.io.IOException;

import javax.swing.ImageIcon;

import pixmap.BytePixmap;

public class Decode {
	static int startTitle = 16;
	static int startFile = 144;
	/**
	 * 
	 * @param origine
	 * @return
	 */
	public static int readSize(byte[] origine){
		int res = 0;
		for(int i=0; i<16; i++){
			res = res + (origine[i]>>1 & 1)*(int)Math.pow(2, (15-i)*2+1) + (origine[i] &1)*(int)Math.pow(2, (15-i)*2);
		}
		return res;
	}
	
	/**
	 * 
	 * @param origine
	 * @return
	 */
	public static String readTitle(byte[] origine){
		
		byte[] tabcharTitle = new byte[14];
		for(int i=0; i<14; i++){
			tabcharTitle[i] = (byte) (((origine[(i*4)+startTitle]&0x3)<<6) | ((origine[(i*4)+startTitle+1]&0x3)<<4) | ((origine[(i*4)+startTitle+2]&0x3)<<2) | (origine[(i*4)+startTitle+3]&0x3));
		}
		String title = new String(tabcharTitle);
		return title;
	}
	
	public static byte[] decode(byte[] origine, int taillefichiercache){
		byte[] tabcharTitle = new byte[taillefichiercache];
		for (int i=0; i<taillefichiercache; i++){
			tabcharTitle[i] = (byte) (((origine[(i*4)+startFile]&0x3)<<6) | ((origine[(i*4)+startFile+1]&0x3)<<4) | ((origine[(i*4)+startFile+2]&0x3)<<2) | (origine[(i*4)+startFile+3]&0x3));
		}
		return tabcharTitle;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			// Generate a BytePixmap of the image and a tab of byte
			BytePixmap origineBytePixmap = new BytePixmap(args[0]);
			byte[] origineByte = origineBytePixmap.getBytes();
			//Get the size of the Image
			int res = readSize(origineByte);
			//Get the name of the Image
			String name = readTitle(origineByte);
			//byte tab of the hide image to display
			byte[] resultByte = decode(origineByte,res);
			// Creation of the image
			Image imageResult = new ImageIcon(resultByte).getImage();
			//Display the image
			DisplayImage dI= new DisplayImage(imageResult);
			Frame f = new Frame("Image: "+name);
			f.add(dI);
			f.pack();
			f.setVisible(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

