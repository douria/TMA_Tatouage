package main;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import pixmap.BytePixmap;
import java.nio.file.Files;


public class Encode {
	static int startTitle = 16;
	static int startFile = 128+startTitle; //144
	
	public static byte[] extractBytes (String ImageName) throws IOException {
		File fi = new File(ImageName);
		byte[] fileContent = Files.readAllBytes(fi.toPath());		
		return ( fileContent );
	}
	
	/**
	 * 
	 * @param size
	 * @param origine
	 * @return
	 */
	public static byte[] encodeSize(int size, byte[] origine){
		byte[] tatouSize = new byte[startTitle];
		for(int i=0; i<startTitle; i++){
			tatouSize[i] = (byte)( (origine[i]&0xfc) | ( (size>>(15-i)*2) & 0x3 ) );
		}
		return tatouSize;
	}
	
	/**
	 * 
	 * @param name
	 * @param origine
	 * @return
	 */
	public static byte[] encodeName(String name, byte[] origine){
		byte[] tatouName = new byte[startFile-startTitle];
		for (int i=0; i<name.length(); i++){
			int codechar = (int) name.charAt(i);
			tatouName[4*i] =(byte) ( (origine[(4*i)+startTitle]&0xfc) | ((codechar>>6)&0x3) );
			tatouName[4*i+1] = (byte) ( (origine[(4*i)+1+startTitle]&0xfc) | ((codechar>>4)&0x3) );
			tatouName[4*i+2] = (byte) ( (origine[(4*i)+2+startTitle]&0xfc) | ((codechar>>2)&0x3) );
			tatouName[4*i+3] = (byte) ( (origine[(4*i)+3+startTitle]&0xfc) | (codechar&0x3) );			
		}
		for (int i=4*name.length(); i<startFile-startTitle; i++){
			tatouName[i] = origine[i+startTitle];
		}
		return tatouName;
	}
	
	/**
	 * 
	 * @param size
	 * @param origine
	 * @param data
	 * @return
	 */
	public static byte[] encodeFile(int size, byte[] origine, byte[] data){
		byte[] tatouFile = new byte[size*4];
		for (int i=0; i<size; i++){
			tatouFile[4*i] = (byte) ( (origine[4*i + startFile] & 0xfc) | ((data[i]>>6)&0x3) );
			tatouFile[4*i+1] = (byte) ( (origine[4*i+1 + startFile] & 0xfc) | ((data[i]>>4) & 0x3) );
			tatouFile[4*i+2] = (byte) ( (origine[4*i+2 + startFile] & 0xfc) | ((data[i]>>2) & 0x3) );
			tatouFile[4*i+3] = (byte) ( (origine[4*i+3 + startFile] & 0xfc) | (data[i]& 0x3) );
			if (i<10){
				System.out.println(tatouFile[4*i]+" et origine: "+origine[4*i + startFile]+" et data: "+((data[i]>>6)&0x3));
				System.out.println(tatouFile[4*i+1]+" et origine: "+origine[4*i +1+ startFile]+" et data: "+((data[i]>>4) & 0x3));
				System.out.println(tatouFile[4*i+2]+" et origine: "+origine[4*i +2+ startFile]+" et data: "+((data[i]>>2) & 0x3));
				System.out.println(tatouFile[4*i+3]+" et origine: "+origine[4*i +3+ startFile]+" et data: "+(data[i]&0x3));
			}
		}
		return tatouFile;
	}
	
	/**
	 * 
	 * @param tatouSize
	 * @param tatouName
	 * @param tatouFile
	 * @param origine
	 * @return
	 */
	public static byte[] encodeAll(byte[] tatouSize, byte[] tatouName, byte[] tatouFile, byte[] origine){
		int endFile = startFile+ tatouFile.length;
		byte[] tatou = new byte[origine.length];
		for (int i=0; i<startTitle; i++){
			tatou[i] = tatouSize[i];
		}
		for (int i=startTitle; i<startFile; i++){
			tatou[i] = tatouName[i-startTitle];
		}
		for (int i=startFile; i<endFile; i++){
			tatou[i] = tatouFile[i-startFile];
		}
		for (int i=endFile; i<origine.length; i++){
			tatou[i] = origine[i];
		}
		return tatou;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			int tailletatoumina1 = 61393;
			BytePixmap origineBytePixmap = new BytePixmap(args[0]);
			int height = origineBytePixmap.height;
			int width = origineBytePixmap.width;
			
			byte[] imgToHide = extractBytes(args[1]);
			System.out.println(imgToHide.length);
			for (int i=0; i<imgToHide.length; i++){
				//System.out.println(imgToHide[i]);
			}
			//InputStream fToHide = new FileInputStream(args[0]);
			byte[] origineByte = origineBytePixmap.getBytes();
			byte[] tatouSize = encodeSize(tailletatoumina1, origineByte);
			byte[] tatouName = encodeName("tatoumina1.jpg", origineByte);
			byte[] tatouFile = encodeFile(tailletatoumina1, origineByte, imgToHide);
			byte[] tatou = encodeAll(tatouSize, tatouName, tatouFile, origineByte);
			
			BytePixmap resultTatou = new BytePixmap(width, height, tatou);
			resultTatou.write("monTatou.pgm");
			// DisplayPixmapAWT dP = new DisplayPixmapAWT(monTatou.pgm);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
