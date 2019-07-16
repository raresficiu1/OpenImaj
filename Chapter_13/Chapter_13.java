package Chapter.Chapter_13;

import org.apache.commons.vfs2.FileSystemException;
import org.openimaj.data.dataset.GroupedDataset;
import org.openimaj.data.dataset.ListDataset;
import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.experiment.dataset.split.GroupedRandomSplitter;
import org.openimaj.experiment.dataset.util.DatasetAdaptors;
import org.openimaj.feature.DoubleFV;
import org.openimaj.feature.DoubleFVComparison;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.model.EigenImages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Chapter_13 {
    public static void main( String[] args ) throws FileSystemException {

        VFSGroupDataset<FImage> dataset = new VFSGroupDataset<FImage>("zip:http://datasets.openimaj.org/att_faces.zip", ImageUtilities.FIMAGE_READER);
        int nTraining = 5;
        int nTesting = 5;
        GroupedRandomSplitter<String, FImage> splits = new GroupedRandomSplitter<String, FImage>(dataset, nTraining, 0, nTesting);
        GroupedDataset<String, ListDataset<FImage>, FImage> training = splits.getTrainingDataset();
        GroupedDataset<String, ListDataset<FImage>, FImage> testing = splits.getTestDataset();

        List<FImage> basisImages = DatasetAdaptors.asList(training);
        int nEigenvectors = 100;
        EigenImages eigen = new EigenImages(nEigenvectors);
        eigen.train(basisImages);

        List<FImage> eigenFaces = new ArrayList<FImage>();
        for (int i = 0; i < 12; i++) {
            eigenFaces.add(eigen.visualisePC(i));
        }
        DisplayUtilities.display("EigenFaces", eigenFaces);

        Map<String, DoubleFV[]> features = new HashMap<String, DoubleFV[]>();
        for (final String person : training.getGroups()) {
            final DoubleFV[] fvs = new DoubleFV[nTraining];

            for (int i = 0; i < nTraining; i++) {
                final FImage face = training.get(person).get(i);
                fvs[i] = eigen.extractFeature(face);
            }
            features.put(person, fvs);
        }


        double correct = 0, incorrect = 0, threshold=15;
        for (String truePerson : testing.getGroups()) {
            for (FImage face : testing.get(truePerson)) {
                DoubleFV testFeature = eigen.extractFeature(face);

                String bestPerson = null;
                double minDistance = 100;
                for (final String person : features.keySet()) {
                    for (final DoubleFV fv : features.get(person)) {
                        double distance = fv.compare(testFeature, DoubleFVComparison.EUCLIDEAN);

                        if (distance < minDistance) {
                            minDistance = distance;
                            bestPerson = person;
                        }
                    }
                }

                if(minDistance<threshold) {
                    System.out.println("Actual: " + truePerson + "\tguess: " + bestPerson);

                    if (truePerson.equals(bestPerson))
                        correct++;
                    else
                        incorrect++;
                }
                else
                    System.out.println("Actual: " + truePerson + "\tguess: Unknown");

            }
        }

        System.out.println("Accuracy: " + (correct / (correct + incorrect)));

        //Exercise 1
        //By using the features of a random image from the dataset we could reconstruct an approximation of the original image
        FImage a = dataset.getRandomInstance();
        DoubleFV b = eigen.extractFeature(a);
        DisplayUtilities.display(eigen.reconstruct(b).normalise());
        DisplayUtilities.display(a);
    }
    // Exercise 2
    // For nTraining = 1 the accuracy was  69
    // For nTraining = 2 the accuracy was  84.5
    // For nTraining = 3 the accuracy was  88.5
    // For nTraining = 4 the accuracy was  91.5
    // As expected, by providing the model more training data, its accuracy increases


    // Exercise 3
    // By using a really small threshold we can make the model accept only the faces it is really sure about and discard the rest.
    // The threshold value should be decided by what is the program intended to do and on how many false positives are admitted.
    // With threshold of 3 the accuracy is 100%
    // With threshold of 10 the accuracy is 98%
    // With threshold of 15 the accuracy is 93%
    // Without a threshold the accuraccy is 96%


}
