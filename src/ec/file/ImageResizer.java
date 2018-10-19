package ec.file;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageResizer {
	
	public static void toSmallSize(String srcFileUri,String smallSizeSaveUri){
		try {
            File fi = new File(srcFileUri); //大圖文件
            File fo = new File(smallSizeSaveUri); //將要轉換出的小圖文件

            AffineTransform transform = new AffineTransform();
            BufferedImage bis = ImageIO.read(fi);

            int w = bis.getWidth();
            int h = bis.getHeight();
            double scale = (double)w/h;

            int nw = 120;
            int nh = (nw * h) / w;
            if(nh>120) {
                nh = 120;
                nw = (nh * w) / h;
            }

            double sx = (double)nw / w;
            double sy = (double)nh / h;

            transform.setToScale(sx,sy);

            AffineTransformOp ato = new AffineTransformOp(transform, null);
            BufferedImage bid = new BufferedImage(nw, nh, BufferedImage.TYPE_3BYTE_BGR);
            ato.filter(bis,bid);
            ImageIO.write(bid, "jpeg", fo);
        } catch(Exception e) {
            e.printStackTrace();
        }
	}
	
}
