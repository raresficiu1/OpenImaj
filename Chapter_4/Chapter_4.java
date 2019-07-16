package Chapter.Chapter_4;

import org.openimaj.feature.DoubleFVComparison;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.pixel.statistics.HistogramModel;
import org.openimaj.math.statistics.distribution.MultidimensionalHistogram;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Chapter_4 {
    public static void main(String[] args) throws IOException {
            URL[] imageURLs = new URL[]{
                    new URL("http://openimaj.org/tutorial/figs/hist1.jpg"),
                    new URL("http://openimaj.org/tutorial/figs/hist2.jpg"),
                    new URL("http://openimaj.org/tutorial/figs/hist3.jpg")
            };

            List<MultidimensionalHistogram> histograms = new ArrayList<MultidimensionalHistogram>();
            HistogramModel model = new HistogramModel(4, 4, 4);

            for (URL u : imageURLs) {
                model.estimateModel(ImageUtilities.readMBF(u));
                histograms.add(model.histogram.clone());
            }

            int firstImage = 0, secondImage = 0;
            double minDistance = 5;
            for (int i = 0; i < histograms.size(); i++) {
                for (int j = i + 1; j < histograms.size(); j++) {
                    double distance = histograms.get(i).compare(histograms.get(j), DoubleFVComparison.INTERSECTION);
                    double distance2 = histograms.get(i).compare(histograms.get(j), DoubleFVComparison.EUCLIDEAN);
                    System.out.println("For [" + i + "][" + j + "] the Euclidian distance is " + distance2 + " and the Intersection distance is " + distance);

                    if (distance < minDistance) {
                        minDistance = distance;
                        firstImage = i;
                        secondImage = j;

                    }




                }
            }
            DisplayUtilities.display(ImageUtilities.readMBF(imageURLs[firstImage]));
            DisplayUtilities.display(ImageUtilities.readMBF(imageURLs[secondImage]));
        }
    }
    /*
    For [0][1] the Euclidian distance is 0.3504197936957126 and the Intersection distance is 0.39642057681511983
    For [0][2] the Euclidian distance is 0.5969851199618375 and the Intersection distance is 0.15605975198300384
    For [1][2] the Euclidian distance is 0.6611774763580472 and the Intersection distance is 0.09343237207855803
     */