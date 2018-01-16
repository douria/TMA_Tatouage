package main;

import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.io.IOException;

import pixmap.BytePixmap;
import pixmap.Pixmap;


public class DisplayPixmapAWT extends Frame {
  
  // constructeur pour un argument Pixmap
  public DisplayPixmapAWT(String name, BytePixmap p) {
    super(name);
    setLocation(50, 50);
    // fabrication des pixels gris au format usuel AWT : ColorModel.RGBdefault
    int[] pixels = new int[p.size];
    for (int i = 0; i < pixels.length; i++)
      pixels[i] = 0xFF000000 + Pixmap.intValue(p.data[i]) * 0x010101; // réplique l'octet 3 fois
    // construit une image avec ces pixels
    MemoryImageSource source = new MemoryImageSource(p.width, p.height, pixels, 0, p.width);
    Image img = Toolkit.getDefaultToolkit().createImage(source);
    add(new DisplayImage(img));
    pack();
    show();
  }

  // constructeur pour un argument fichier PGM
  public DisplayPixmapAWT(String filename) throws IOException {
    this(filename, new BytePixmap(filename));
  }

  public static void main(String[] args) {
    try {
      new DisplayPixmapAWT(args[0]);
    }
    catch (IOException e) {System.err.println(e);}
  }

}
