package com.freevisiontech.mediaproclib;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.List;

public class VideoClip {
    private static final String TAG = "VideoClip";
    private double endTime;
    private String filePath;
    private String outName;
    private double startTime;
    private String workingPath;

    public void setFilePath(String filePath2) {
        this.filePath = filePath2;
    }

    public void setWorkingPath(String workingPath2) {
        this.workingPath = workingPath2;
    }

    public void setOutName(String outName2) {
        this.outName = outName2;
    }

    public void setEndTime(double endTime2) {
        this.endTime = endTime2 / 1000.0d;
    }

    public void setStartTime(double startTime2) {
        this.startTime = startTime2 / 1000.0d;
    }

    public static synchronized void clip(String filePath2, String workingPath2, String outName2, double startTime2, double endTime2) {
        synchronized (VideoClip.class) {
            try {
                Movie movie = MovieCreator.build(filePath2);
                List<Track> tracks = movie.getTracks();
                movie.setTracks(new LinkedList());
                boolean timeCorrected = false;
                for (Track track : tracks) {
                    if (track.getSyncSamples() != null && track.getSyncSamples().length > 0) {
                        if (timeCorrected) {
                            throw new RuntimeException("The startTime has already been corrected by another track with SyncSample. Not Supported.");
                        }
                        startTime2 = VideoHelper.correctTimeToSyncSample(track, startTime2, false);
                        endTime2 = VideoHelper.correctTimeToSyncSample(track, endTime2, true);
                        timeCorrected = true;
                    }
                }
                for (Track track2 : tracks) {
                    long currentSample = 0;
                    double currentTime = 0.0d;
                    double lastTime = -1.0d;
                    long startSample1 = -1;
                    long endSample1 = -1;
                    for (int i = 0; i < track2.getSampleDurations().length; i++) {
                        long delta = track2.getSampleDurations()[i];
                        if (currentTime > lastTime && currentTime <= startTime2) {
                            startSample1 = currentSample;
                        }
                        if (currentTime > lastTime && currentTime <= endTime2) {
                            endSample1 = currentSample;
                        }
                        lastTime = currentTime;
                        currentTime += ((double) delta) / ((double) track2.getTrackMetaData().getTimescale());
                        currentSample++;
                    }
                    movie.addTrack(new CroppedTrack(track2, startSample1, endSample1));
                }
                Container out = new DefaultMp4Builder().build(movie);
                File file = new File(workingPath2);
                file.mkdirs();
                FileOutputStream fileOutputStream = new FileOutputStream(new File(file, outName2));
                FileChannel fco = fileOutputStream.getChannel();
                out.writeContainer(fco);
                fco.close();
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return;
    }
}
