package Chapter.Chapter_6;


import com.esotericsoftware.kryo.util.IdentityMap;
import org.openimaj.data.dataset.MapBackedDataset;
import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.data.dataset.VFSListDataset;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.dataset.BingImageDataset;
import org.openimaj.image.dataset.FlickrImageDataset;
import org.openimaj.util.api.auth.DefaultTokenFactory;
import org.openimaj.util.api.auth.common.BingAPIToken;
import org.openimaj.util.api.auth.common.FlickrAPIToken;

import java.io.IOException;
import java.util.Map;

public class Chapter_6 {
    public static void main(String[] args) throws Exception {
        VFSListDataset<FImage> images = new VFSListDataset<FImage>("C:\\Users\\Spektra\\Chapter 3\\src\\main\\image_dir", ImageUtilities.FIMAGE_READER);
        System.out.println(images.size());
        DisplayUtilities.display(images.getRandomInstance(), "A random image from the dataset");

        DisplayUtilities.display("My images", images);

        VFSListDataset<FImage> faces = new VFSListDataset<FImage>("zip:http://datasets.openimaj.org/att_faces.zip", ImageUtilities.FIMAGE_READER);
        DisplayUtilities.display("ATT faces", faces);


        VFSGroupDataset<FImage> groupedFaces = new VFSGroupDataset<FImage>("zip:http://datasets.openimaj.org/att_faces.zip", ImageUtilities.FIMAGE_READER);
        System.out.println(groupedFaces.entrySet().size());
//
//        for (final Map.Entry<String, VFSListDataset<FImage>> entry : groupedFaces.entrySet()) {
//            DisplayUtilities.display(entry.getKey(), entry.getValue());
//        }
//
//        FlickrAPIToken flickrToken = DefaultTokenFactory.get(FlickrAPIToken.class);
//        FlickrImageDataset<FImage> cats =
//                FlickrImageDataset.create(ImageUtilities.FIMAGE_READER, flickrToken, "cat", 10);
//        DisplayUtilities.display("Cats", cats);

        // Exercise 1
        // Random picture from each person
        for (final Map.Entry<String, VFSListDataset<FImage>> entry : groupedFaces.entrySet()) {
            DisplayUtilities.display(entry.getValue().getRandomInstance());

        }

        // Exercise 2
        // VFS supports a multitude of sources including BZIP2, File, FTP, FTPS, GZIP ,HDFS, HTTP ,HTTPS, Jar, Tar, Zip


        //Exercise 3
        BingAPIToken bingToken = DefaultTokenFactory.get(BingAPIToken.class);
        BingImageDataset<FImage> cats = BingImageDataset.create(ImageUtilities.FIMAGE_READER, bingToken, "Cats", 10);
        DisplayUtilities.display("cats", cats);

        //Exercise 4
        MapBackedDataset famousPeople = new MapBackedDataset();
        famousPeople.add("Marilyn", BingImageDataset.create(ImageUtilities.FIMAGE_READER, bingToken, "Marilyn Monroe", 10));
        famousPeople.add("Madonna", BingImageDataset.create(ImageUtilities.FIMAGE_READER, bingToken, "Madonna", 10));
        famousPeople.add("Gates", BingImageDataset.create(ImageUtilities.FIMAGE_READER, bingToken, "Bill Gates", 10));
        famousPeople.add("Musk", BingImageDataset.create(ImageUtilities.FIMAGE_READER, bingToken, "Elon Musk", 10));
        famousPeople.add("Keanu", BingImageDataset.create(ImageUtilities.FIMAGE_READER, bingToken, "Keanu Reeves", 10));
        famousPeople.add("Arteezy", BingImageDataset.create(ImageUtilities.FIMAGE_READER, bingToken, "Artour Babaev", 10));
        famousPeople.add("Elvis", BingImageDataset.create(ImageUtilities.FIMAGE_READER, bingToken, "Elvis Presley", 10));
        famousPeople.add("Albert", BingImageDataset.create(ImageUtilities.FIMAGE_READER, bingToken, "Albert Einstein", 10));
        famousPeople.add("Francis", BingImageDataset.create(ImageUtilities.FIMAGE_READER, bingToken, "Pope Francis", 10));
        famousPeople.add("Leonardo", BingImageDataset.create(ImageUtilities.FIMAGE_READER, bingToken, "Leonardo da Vinci", 10));

    }
}
