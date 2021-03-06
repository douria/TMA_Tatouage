package main;

import java.awt.Frame;
import java.awt.Image;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
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
		char[] endtitle = new char[4];
		endtitle[0] =' ';
		endtitle[1] =' ';
		endtitle[2] =' ';
		endtitle[3] =' ';
		byte[] tabcharTitle = new byte[startFile-startTitle];
		int i = 0;
		while((endtitle[0]!='.' && endtitle[1]!='j' && endtitle[2]!='p' && endtitle[3]!='g') || (endtitle[0]!='.' && endtitle[1]!='p' && endtitle[2]!='n' && endtitle[3]!='g')){
			tabcharTitle[i] = (byte) (((origine[(i*4)+startTitle]&0x3)<<6) | ((origine[(i*4)+startTitle+1]&0x3)<<4) | ((origine[(i*4)+startTitle+2]&0x3)<<2) | (origine[(i*4)+startTitle+3]&0x3));
			endtitle[0]= endtitle[1];
			endtitle[1]= endtitle[2];
			endtitle[2]= endtitle[3];
			endtitle[3]= (char)tabcharTitle[i];
			i++;
		}
		byte[] btitle = new byte[i];
		for(int j=0; j<i; j++){
			btitle[j] = tabcharTitle[j];
		}
		String title = new String(btitle);
		return title;
	}
	
	public static byte[] decode(byte[] origine, int taillefichiercache){
		byte[] tabcharTitle = new byte[taillefichiercache];
		for (int i=0; i<taillefichiercache; i++){
			tabcharTitle[i] = (byte) (((origine[(i*4)+startFile]&0x3)<<6) | ((origine[(i*4)+startFile+1]&0x3)<<4) | ((origine[(i*4)+startFile+2]&0x3)<<2) | (origine[(i*4)+startFile+3]&0x3));
		}
		return tabcharTitle;
	}
	
	public static void saveImage(String name, byte[] resultByte){
		
		try {
			// create the file Image 
			File imageFich = new File(name);
			//si l'image n'exsite pas on la cree 
			
			if (!imageFich.exists()) {
				imageFich.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(imageFich);
			fos.write(resultByte);
			fos.flush();
			System.out.println("dans le write pour l'image");
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			// Generate a BytePixmap of the image and a tab of byte
			System.out.println(args[0]);
			BytePixmap origineBytePixmap = new BytePixmap(args[0]);
			byte[] origineByte = origineBytePixmap.getBytes();
			//Get the size of the Image
			int res = readSize(origineByte);
			System.out.println(res);
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
			
			saveImage(name,resultByte);

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

