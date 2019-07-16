package Chapter.Chapter_2;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.processing.edges.CannyEdgeDetector;
import org.openimaj.image.typography.hershey.HersheyFont;
import org.openimaj.math.geometry.shape.Ellipse;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Chapter_2 {
    public static void main(String[] args) throws IOException, InterruptedException {

        MBFImage image;
        JFrame frame = DisplayUtilities.createNamedWindow("Pictures", "All of them", true);
        image = ImageUtilities.readMBF(new File("src/main/sinaface.jpg"));
        System.out.println(image.colourSpace);
        DisplayUtilities.display(image,frame);
        TimeUnit.SECONDS.sleep(1);
        DisplayUtilities.display(image.getBand(0),frame);
        TimeUnit.SECONDS.sleep(1);

        MBFImage clone = image.clone();
        for (int y=0; y<image.getHeight(); y++) {
            for(int x=0; x<image.getWidth(); x++) {
                clone.getBand(1).pixels[y][x] = 0;
                clone.getBand(0).pixels[y][x] = 0;
            }
        }

        //clone.getBand(1).fill(0);
        //clone.getBand(0).fill(0);
        image.processInplace(new CannyEdgeDetector());
        DisplayUtilities.display(clone,frame);
        TimeUnit.SECONDS.sleep(1);
        DisplayUtilities.display(image,frame);
        TimeUnit.SECONDS.sleep(1);

        image.drawShapeFilled(new Ellipse(700f, 450f, 20f, 10f, 0f), RGBColour.WHITE);
        image.drawShapeFilled(new Ellipse(650f, 425f, 25f, 12f, 0f), RGBColour.WHITE);
        image.drawShapeFilled(new Ellipse(600f, 380f, 30f, 15f, 0f), RGBColour.WHITE);
        image.drawShapeFilled(new Ellipse(500f, 300f, 100f, 70f, 0f), RGBColour.WHITE);
        image.drawShape(new Ellipse(700f, 450f, 20f, 10f, 0f), RGBColour.BLUE);
        image.drawShape(new Ellipse(650f, 425f, 25f, 12f, 0f), RGBColour.BLUE);
        image.drawShape(new Ellipse(600f, 380f, 30f, 15f, 0f), RGBColour.BLUE);
        image.drawShape(new Ellipse(500f, 300f, 100f, 70f, 0f), RGBColour.BLUE);
        image.drawText("OpenIMAJ is", 425, 300, HersheyFont.ASTROLOGY, 20, RGBColour.BLACK);
        image.drawText("Awesome", 425, 330, HersheyFont.ASTROLOGY, 20, RGBColour.BLACK);
        DisplayUtilities.display(image,frame);
    }
}
