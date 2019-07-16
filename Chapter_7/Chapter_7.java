package Chapter.Chapter_7;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.video.Video;
import org.openimaj.video.capture.VideoCaptureException;
import org.openimaj.video.xuggle.XuggleVideo;

import java.io.File;

public class Chapter_7 {
    public static void main(String[] args) throws VideoCaptureException {
        Video<MBFImage> video;
        video = new XuggleVideo(new File("src/main/keyboardcat.flv"));
        //video = new VideoCapture(320, 240);
        for (MBFImage mbfImage : video) {
            DisplayUtilities.displayName(RedColour(mbfImage), "videoFrames");
            //DisplayUtilities.displayName(mbfImage.process(new CannyEdgeDetector()), "videoFrames");
        }

        //VideoDisplay<MBFImage> display = VideoDisplay.createVideoDisplay(video);

    }

    public static MBFImage RedColour (MBFImage Image)
    {
        Image.getBand(1).fill(0);
        Image.getBand(0).fill(0);
        return Image;
    }



}
