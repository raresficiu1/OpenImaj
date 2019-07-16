package Chapter.Chapter_3;

import cern.colt.Arrays;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.connectedcomponent.GreyscaleConnectedComponentLabeler;
import org.openimaj.image.pixel.ConnectedComponent;
import org.openimaj.image.processor.PixelProcessor;
import org.openimaj.image.segmentation.FelzenszwalbHuttenlocherSegmenter;
import org.openimaj.image.segmentation.SegmentationUtilities;
import org.openimaj.image.typography.hershey.HersheyFont;
import org.openimaj.ml.clustering.FloatCentroidsResult;
import org.openimaj.ml.clustering.assignment.HardAssigner;
import org.openimaj.ml.clustering.kmeans.FloatKMeans;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Chapter_3 {

    public static void main(String[] args) throws IOException {

        MBFImage input = ImageUtilities.readMBF(new URL("http://static.openimaj.org/media/tutorial/sinaface.jpg"));
        //transforming our image from RGB to LAB colour space
        input = ColourSpace.convert(input, ColourSpace.CIE_Lab);
        FloatKMeans cluster = FloatKMeans.createExact(2);
        float[][] imageData = input.getPixelVectorNative(new float[input.getWidth() * input.getHeight()][3]);
        FloatCentroidsResult result = cluster.cluster(imageData);
        final float[][] centroids = result.centroids;
        for (float[] fs : centroids) {
            System.out.println(Arrays.toString(fs));
        }


        final HardAssigner<float[], ?, ?> assigner = result.defaultHardAssigner();
                    /*
                    for (int y=0; y<input.getHeight(); y++) {
                        for (int x=0; x<input.getWidth(); x++) {
                          float[] pixel = input.getPixelNative(x, y);
                          int centroid = assigner.assign(pixel);
                         input.setPixelNative(x, y, centroids[centroid]);
                        }
                    }
                    */
        //Exercise 1
        input.processInplace(new PixelProcessor<Float[]>() {
            public Float[] processPixel(Float[] pixel) {
                int length = pixel.length;
                float[] floatPixel = new float[length];
                for (int i = 0; i < pixel.length; i++) {
                    floatPixel[i] = pixel[i].floatValue();
                }
                int centroid = assigner.assign(floatPixel);
                floatPixel = centroids[centroid];
                for (int i = 0; i < length; i++) {
                    pixel[i] = floatPixel[i];
                }
                return pixel;
            }
        });


        input = ColourSpace.convert(input, ColourSpace.RGB);
        DisplayUtilities.display(input);

        //grouping together all pixels with the same class that touch each other
        GreyscaleConnectedComponentLabeler labeler = new GreyscaleConnectedComponentLabeler();
        List<ConnectedComponent> components = labeler.findComponents(input.flatten());

        int i = 0;
        for (ConnectedComponent comp : components) {
            if (comp.calculateArea() < 50)
                continue;
            input.drawText("Point:" + (i++), comp.calculateCentroidPixel(), HersheyFont.TIMES_MEDIUM, 20);
        }



        DisplayUtilities.display(input);

        MBFImage input2 = ImageUtilities.readMBF(new URL("http://static.openimaj.org/media/tutorial/sinaface.jpg"));

        FelzenszwalbHuttenlocherSegmenter segmenter2 = new FelzenszwalbHuttenlocherSegmenter();

        MBFImage segments = SegmentationUtilities.renderSegments(input2, segmenter2.segment(input2));

        DisplayUtilities.display(segments);
    }
}
