package com.freevisiontech.mediaproclib;

import android.util.Log;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AACTrackImpl;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;
import com.googlecode.mp4parser.authoring.tracks.TextTrackImpl;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Mp4ParseUtil {
    private static List<Track> audioTracks = new ArrayList();
    private static List<Movie> moviesList = new ArrayList();
    private static List<Track> videoTracks = new ArrayList();

    public static void appendMp4List(List<String> mp4PathList, String outPutPath) {
        try {
            List<Movie> mp4MovieList = new ArrayList<>();
            for (String mp4Path : mp4PathList) {
                mp4MovieList.add(MovieCreator.build(mp4Path));
            }
            List<Track> audioTracks2 = new LinkedList<>();
            List<Track> videoTracks2 = new LinkedList<>();
            for (Movie mp4Movie : mp4MovieList) {
                for (Track inMovieTrack : mp4Movie.getTracks()) {
                    if ("soun".equals(inMovieTrack.getHandler())) {
                        audioTracks2.add(inMovieTrack);
                    }
                    if ("vide".equals(inMovieTrack.getHandler())) {
                        videoTracks2.add(inMovieTrack);
                    }
                }
            }
            Movie resultMovie = new Movie();
            if (!audioTracks2.isEmpty()) {
                resultMovie.addTrack(new AppendTrack((Track[]) audioTracks2.toArray(new Track[audioTracks2.size()])));
            }
            if (!videoTracks2.isEmpty()) {
                resultMovie.addTrack(new AppendTrack((Track[]) videoTracks2.toArray(new Track[videoTracks2.size()])));
            }
            Container outContainer = new DefaultMp4Builder().build(resultMovie);
            FileChannel fileChannel = new RandomAccessFile(String.format(outPutPath, new Object[0]), "rw").getChannel();
            outContainer.writeContainer(fileChannel);
            fileChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void appendAacList(List<String> aacPathList, String outPutPath) {
        try {
            List<Track> audioTracks2 = new LinkedList<>();
            for (int i = 0; i < aacPathList.size(); i++) {
                audioTracks2.add(new AACTrackImpl(new FileDataSourceImpl(aacPathList.get(i))));
            }
            Movie resultMovie = new Movie();
            if (!audioTracks2.isEmpty()) {
                resultMovie.addTrack(new AppendTrack((Track[]) audioTracks2.toArray(new Track[audioTracks2.size()])));
            }
            Container outContainer = new DefaultMp4Builder().build(resultMovie);
            FileChannel fileChannel = new RandomAccessFile(String.format(outPutPath, new Object[0]), "rw").getChannel();
            outContainer.writeContainer(fileChannel);
            fileChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void appendMp4(List<String> mMp4List, String outputpath) {
        int i = 0;
        while (i < mMp4List.size()) {
            try {
                moviesList.add(MovieCreator.build(mMp4List.get(i)));
                i++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (Movie m : moviesList) {
            for (Track t : m.getTracks()) {
                if (t.getHandler().equals("soun")) {
                    audioTracks.add(t);
                }
                if (t.getHandler().equals("vide")) {
                    videoTracks.add(t);
                }
            }
        }
        Movie result = new Movie();
        try {
            if (audioTracks.size() > 0) {
                result.addTrack(new AppendTrack((Track[]) audioTracks.toArray(new Track[audioTracks.size()])));
            }
            if (videoTracks.size() > 0) {
                result.addTrack(new AppendTrack((Track[]) videoTracks.toArray(new Track[videoTracks.size()])));
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        Container out = new DefaultMp4Builder().build(result);
        try {
            FileChannel fc = new FileOutputStream(new File(outputpath)).getChannel();
            out.writeContainer(fc);
            fc.close();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        moviesList.clear();
    }

    public static boolean muxAacMp4(String aacPath, String mp4Path, String outPath) {
        try {
            AACTrackImpl aacTrack = new AACTrackImpl(new FileDataSourceImpl(aacPath));
            Track videoTracks2 = null;
            for (Track videoMovieTrack : MovieCreator.build(mp4Path).getTracks()) {
                if ("vide".equals(videoMovieTrack.getHandler())) {
                    videoTracks2 = videoMovieTrack;
                }
            }
            Movie resultMovie = new Movie();
            resultMovie.addTrack(videoTracks2);
            resultMovie.addTrack(aacTrack);
            Container out = new DefaultMp4Builder().build(resultMovie);
            FileOutputStream fos = new FileOutputStream(new File(outPath));
            out.writeContainer(fos.getChannel());
            fos.close();
            Log.e("update_tag", "merge finish");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void muxM4AMp4(String m4aPath, String mp4Path, String outPath) throws IOException {
        Track audioTracks2 = null;
        for (Track audioMovieTrack : MovieCreator.build(m4aPath).getTracks()) {
            if ("soun".equals(audioMovieTrack.getHandler())) {
                audioTracks2 = audioMovieTrack;
            }
        }
        Track videoTracks2 = null;
        for (Track videoMovieTrack : MovieCreator.build(mp4Path).getTracks()) {
            if ("vide".equals(videoMovieTrack.getHandler())) {
                videoTracks2 = videoMovieTrack;
            }
        }
        Movie resultMovie = new Movie();
        resultMovie.addTrack(videoTracks2);
        resultMovie.addTrack(audioTracks2);
        Container out = new DefaultMp4Builder().build(resultMovie);
        FileOutputStream fos = new FileOutputStream(new File(outPath));
        out.writeContainer(fos.getChannel());
        fos.close();
    }

    public static void splitMp4(String mp4Path, String outPath) {
        try {
            Track videoTracks2 = null;
            for (Track videoMovieTrack : MovieCreator.build(mp4Path).getTracks()) {
                if ("vide".equals(videoMovieTrack.getHandler())) {
                    videoTracks2 = videoMovieTrack;
                }
            }
            Movie resultMovie = new Movie();
            resultMovie.addTrack(videoTracks2);
            Container out = new DefaultMp4Builder().build(resultMovie);
            FileOutputStream fos = new FileOutputStream(new File(outPath));
            out.writeContainer(fos.getChannel());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void splitAac(String mp4Path, String outPath) {
        try {
            Track videoTracks2 = null;
            for (Track videoMovieTrack : MovieCreator.build(mp4Path).getTracks()) {
                if ("soun".equals(videoMovieTrack.getHandler())) {
                    videoTracks2 = videoMovieTrack;
                }
            }
            Movie resultMovie = new Movie();
            resultMovie.addTrack(videoTracks2);
            Container out = new DefaultMp4Builder().build(resultMovie);
            FileOutputStream fos = new FileOutputStream(new File(outPath));
            out.writeContainer(fos.getChannel());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void splitVideo(String mp4Path, String mp4OutPath, String aacOutPath) {
        try {
            Track videTracks = null;
            Track sounTracks = null;
            for (Track videoMovieTrack : MovieCreator.build(mp4Path).getTracks()) {
                if ("vide".equals(videoMovieTrack.getHandler())) {
                    videTracks = videoMovieTrack;
                }
                if ("soun".equals(videoMovieTrack.getHandler())) {
                    sounTracks = videoMovieTrack;
                }
            }
            Movie videMovie = new Movie();
            videMovie.addTrack(videTracks);
            Movie sounMovie = new Movie();
            sounMovie.addTrack(sounTracks);
            Container videout = new DefaultMp4Builder().build(videMovie);
            FileOutputStream videfos = new FileOutputStream(new File(mp4OutPath));
            videout.writeContainer(videfos.getChannel());
            videfos.close();
            Container sounout = new DefaultMp4Builder().build(sounMovie);
            FileOutputStream sounfos = new FileOutputStream(new File(aacOutPath));
            sounout.writeContainer(sounfos.getChannel());
            sounfos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addSubtitles(String mp4Path, String outPath) throws IOException {
        Movie videoMovie = MovieCreator.build(mp4Path);
        TextTrackImpl subTitleEng = new TextTrackImpl();
        subTitleEng.getTrackMetaData().setLanguage("eng");
        subTitleEng.getSubs().add(new TextTrackImpl.Line(0, 1000, "Five"));
        subTitleEng.getSubs().add(new TextTrackImpl.Line(1000, 2000, "Four"));
        subTitleEng.getSubs().add(new TextTrackImpl.Line(2000, 3000, "Three"));
        subTitleEng.getSubs().add(new TextTrackImpl.Line(3000, 4000, "Two"));
        subTitleEng.getSubs().add(new TextTrackImpl.Line(4000, 5000, "one"));
        subTitleEng.getSubs().add(new TextTrackImpl.Line(5001, 5002, " "));
        videoMovie.addTrack(subTitleEng);
        Container out = new DefaultMp4Builder().build(videoMovie);
        FileOutputStream fos = new FileOutputStream(new File(outPath));
        out.writeContainer(fos.getChannel());
        fos.close();
    }

    public static void cropMp4(String mp4Path, long fromSample, long toSample, String outPath) {
        try {
            Movie mp4Movie = MovieCreator.build(mp4Path);
            Track videoTracks2 = null;
            for (Track videoMovieTrack : mp4Movie.getTracks()) {
                if ("vide".equals(videoMovieTrack.getHandler())) {
                    videoTracks2 = videoMovieTrack;
                }
            }
            Track audioTracks2 = null;
            for (Track audioMovieTrack : mp4Movie.getTracks()) {
                if ("soun".equals(audioMovieTrack.getHandler())) {
                    audioTracks2 = audioMovieTrack;
                }
            }
            Movie resultMovie = new Movie();
            Movie movie = resultMovie;
            movie.addTrack(new AppendTrack(new CroppedTrack(videoTracks2, fromSample, toSample)));
            resultMovie.addTrack(new AppendTrack(new CroppedTrack(audioTracks2, fromSample, toSample)));
            Container out = new DefaultMp4Builder().build(resultMovie);
            FileOutputStream fos = new FileOutputStream(new File(outPath));
            out.writeContainer(fos.getChannel());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
