package Chapter.Chapter_14;

import org.openimaj.data.dataset.GroupedDataset;
import org.openimaj.data.dataset.ListDataset;
import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.experiment.dataset.sampling.GroupSampler;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.annotation.evaluation.datasets.Caltech101;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.processing.resize.ResizeProcessor;
import org.openimaj.time.Timer;
import org.openimaj.util.function.Operation;
import org.openimaj.util.parallel.Parallel;
import org.openimaj.util.parallel.partition.RangePartitioner;

import java.util.ArrayList;
import java.util.Iterator;

public class Chapter_14 {
    public static void main(String[] args) throws Exception {

        Parallel.forIndex(0, 10, 1, new Operation<Integer>() {
            public void perform(Integer i) {
                System.out.println(i);
            }
        });

        VFSGroupDataset<MBFImage> allImages = Caltech101.getImages(ImageUtilities.MBFIMAGE_READER);
        GroupedDataset<String, ListDataset<MBFImage>, MBFImage> images = GroupSampler.sample(allImages, 8, false);

        final ArrayList<MBFImage> output = new ArrayList<MBFImage>();
        final ResizeProcessor resize = new ResizeProcessor(200);


        Timer t1 = Timer.timer();

        Parallel.forEachPartitioned(new RangePartitioner<ListDataset<MBFImage>>(images.values()), new Operation<Iterator<ListDataset<MBFImage>>>() {
            public void perform(Iterator<ListDataset<MBFImage>> listDatasetIterator) {

                final MBFImage current = new MBFImage(200, 200, ColourSpace.RGB);
                ListDataset<MBFImage> dataset = listDatasetIterator.next();


                for (MBFImage i : dataset) {
                    MBFImage tmp = new MBFImage(200, 200, ColourSpace.RGB);
                    tmp.fill(RGBColour.WHITE);

                    MBFImage small = i.process(resize).normalise();
                    int x = (200 - small.getWidth()) / 2;
                    int y = (200 - small.getHeight()) / 2;
                    tmp.drawImage(small, x, y);

                    current.addInplace(tmp);
                }
                current.divideInplace((float) dataset.size());
                output.add(current);



            }
        });
        DisplayUtilities.display("Images", output);
        System.out.println("Time: " + t1.duration() + "ms");
    }
    // Parallelising the outer loop made the code faster but not faster than the code with a parallelised inner loop.
    // The pros are that the code runs faster and the work load is better distributed among the CPUs but at the cost of having processes executed in a seemingly random order, which for some processes
    // is unacceptable.

}
