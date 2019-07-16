package Chapter.Chapter_1;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.processing.convolution.FGaussianConvolve;
import org.openimaj.image.typography.hershey.HersheyFont;

public class Chapter_1 {
    public static void main(String[] args) {
        //Create an image
        MBFImage image = new MBFImage(500, 70, ColourSpace.RGB);

        //Fill the image with white
        image.fill(RGBColour.WHITE);

        //Render some test into the image
        image.drawText("Hello World!", 10, 60, HersheyFont.TIMES_BOLD_ITALIC, 50, RGBColour.RED);

        //Apply a Gaussian blur
        image.processInplace(new FGaussianConvolve(2f));

        //Display the image
        DisplayUtilities.display(image);

        float[][] a = new float[10][12];
        for(int i =0 ; i<10;i++) {
            for(int j= 0 ; j<10;j++) {
                a[i][j] = i+j;

            }
        }
        System.out.println(a.length);

    }
}
