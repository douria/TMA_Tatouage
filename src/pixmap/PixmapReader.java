package pixmap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

class PixmapReader extends FileInputStream {

	private char c;
	
	public PixmapReader(String fileName) throws FileNotFoundException {
		super(fileName);
	}
	
	public boolean matchKey(String key) throws IOException {
		byte[] buf = new byte[key.length()];
		read(buf, 0, key.length());
		return key.compareTo(new String(buf)) == 0;
	}
	
	public void getChar() throws IOException {
		c = (char)read();
	}
	
	public int getInt() throws IOException {
		String s = "";
		while ( (c != '\n') && Character.isSpaceChar(c) ) 
			getChar();
		while ( (c != '\n') && !Character.isSpaceChar(c) ) {
			s = s + c;
			getChar();
		}      
		return Integer.parseInt(s);
	}
	
	public void skipLine() throws IOException {
		while ( c != '\n' )
			getChar();
	}
	
	public void skipComment(char code) throws IOException {
		getChar();
		while ( c == code ) {
			skipLine();
			getChar();
		}
	}
	
	public byte[] loadData(int size) throws IOException {
		byte[] data = new byte[size];
		read(data, 0, size);
		return data;
	}
	
	public void close() {
		try {
			super.close();
		} catch (IOException e) {}
	}
}

