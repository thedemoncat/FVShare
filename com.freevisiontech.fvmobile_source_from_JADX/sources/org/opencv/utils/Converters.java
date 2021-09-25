package org.opencv.utils;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.CvType;
import org.opencv.core.DMatch;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.core.Rect;

public class Converters {
    public static Mat vector_Point_to_Mat(List<Point> pts) {
        return vector_Point_to_Mat(pts, 4);
    }

    public static Mat vector_Point2f_to_Mat(List<Point> pts) {
        return vector_Point_to_Mat(pts, 5);
    }

    public static Mat vector_Point2d_to_Mat(List<Point> pts) {
        return vector_Point_to_Mat(pts, 6);
    }

    public static Mat vector_Point_to_Mat(List<Point> pts, int typeDepth) {
        int count;
        if (pts != null) {
            count = pts.size();
        } else {
            count = 0;
        }
        if (count <= 0) {
            return new Mat();
        }
        switch (typeDepth) {
            case 4:
                Mat res = new Mat(count, 1, CvType.CV_32SC2);
                int[] buff = new int[(count * 2)];
                for (int i = 0; i < count; i++) {
                    Point p = pts.get(i);
                    buff[i * 2] = (int) p.f1125x;
                    buff[(i * 2) + 1] = (int) p.f1126y;
                }
                res.put(0, 0, buff);
                return res;
            case 5:
                Mat res2 = new Mat(count, 1, CvType.CV_32FC2);
                float[] buff2 = new float[(count * 2)];
                for (int i2 = 0; i2 < count; i2++) {
                    Point p2 = pts.get(i2);
                    buff2[i2 * 2] = (float) p2.f1125x;
                    buff2[(i2 * 2) + 1] = (float) p2.f1126y;
                }
                res2.put(0, 0, buff2);
                return res2;
            case 6:
                Mat res3 = new Mat(count, 1, CvType.CV_64FC2);
                double[] buff3 = new double[(count * 2)];
                for (int i3 = 0; i3 < count; i3++) {
                    Point p3 = pts.get(i3);
                    buff3[i3 * 2] = p3.f1125x;
                    buff3[(i3 * 2) + 1] = p3.f1126y;
                }
                res3.put(0, 0, buff3);
                return res3;
            default:
                throw new IllegalArgumentException("'typeDepth' can be CV_32S, CV_32F or CV_64F");
        }
    }

    public static Mat vector_Point3i_to_Mat(List<Point3> pts) {
        return vector_Point3_to_Mat(pts, 4);
    }

    public static Mat vector_Point3f_to_Mat(List<Point3> pts) {
        return vector_Point3_to_Mat(pts, 5);
    }

    public static Mat vector_Point3d_to_Mat(List<Point3> pts) {
        return vector_Point3_to_Mat(pts, 6);
    }

    public static Mat vector_Point3_to_Mat(List<Point3> pts, int typeDepth) {
        int count;
        if (pts != null) {
            count = pts.size();
        } else {
            count = 0;
        }
        if (count <= 0) {
            return new Mat();
        }
        switch (typeDepth) {
            case 4:
                Mat res = new Mat(count, 1, CvType.CV_32SC3);
                int[] buff = new int[(count * 3)];
                for (int i = 0; i < count; i++) {
                    Point3 p = pts.get(i);
                    buff[i * 3] = (int) p.f1127x;
                    buff[(i * 3) + 1] = (int) p.f1128y;
                    buff[(i * 3) + 2] = (int) p.f1129z;
                }
                res.put(0, 0, buff);
                return res;
            case 5:
                Mat res2 = new Mat(count, 1, CvType.CV_32FC3);
                float[] buff2 = new float[(count * 3)];
                for (int i2 = 0; i2 < count; i2++) {
                    Point3 p2 = pts.get(i2);
                    buff2[i2 * 3] = (float) p2.f1127x;
                    buff2[(i2 * 3) + 1] = (float) p2.f1128y;
                    buff2[(i2 * 3) + 2] = (float) p2.f1129z;
                }
                res2.put(0, 0, buff2);
                return res2;
            case 6:
                Mat res3 = new Mat(count, 1, CvType.CV_64FC3);
                double[] buff3 = new double[(count * 3)];
                for (int i3 = 0; i3 < count; i3++) {
                    Point3 p3 = pts.get(i3);
                    buff3[i3 * 3] = p3.f1127x;
                    buff3[(i3 * 3) + 1] = p3.f1128y;
                    buff3[(i3 * 3) + 2] = p3.f1129z;
                }
                res3.put(0, 0, buff3);
                return res3;
            default:
                throw new IllegalArgumentException("'typeDepth' can be CV_32S, CV_32F or CV_64F");
        }
    }

    public static void Mat_to_vector_Point2f(Mat m, List<Point> pts) {
        Mat_to_vector_Point(m, pts);
    }

    public static void Mat_to_vector_Point2d(Mat m, List<Point> pts) {
        Mat_to_vector_Point(m, pts);
    }

    public static void Mat_to_vector_Point(Mat m, List<Point> pts) {
        if (pts == null) {
            throw new IllegalArgumentException("Output List can't be null");
        }
        int count = m.rows();
        int type = m.type();
        if (m.cols() != 1) {
            throw new IllegalArgumentException("Input Mat should have one column\n" + m);
        }
        pts.clear();
        if (type == CvType.CV_32SC2) {
            int[] buff = new int[(count * 2)];
            m.get(0, 0, buff);
            for (int i = 0; i < count; i++) {
                pts.add(new Point((double) buff[i * 2], (double) buff[(i * 2) + 1]));
            }
        } else if (type == CvType.CV_32FC2) {
            float[] buff2 = new float[(count * 2)];
            m.get(0, 0, buff2);
            for (int i2 = 0; i2 < count; i2++) {
                pts.add(new Point((double) buff2[i2 * 2], (double) buff2[(i2 * 2) + 1]));
            }
        } else if (type == CvType.CV_64FC2) {
            double[] buff3 = new double[(count * 2)];
            m.get(0, 0, buff3);
            for (int i3 = 0; i3 < count; i3++) {
                pts.add(new Point(buff3[i3 * 2], buff3[(i3 * 2) + 1]));
            }
        } else {
            throw new IllegalArgumentException("Input Mat should be of CV_32SC2, CV_32FC2 or CV_64FC2 type\n" + m);
        }
    }

    public static void Mat_to_vector_Point3i(Mat m, List<Point3> pts) {
        Mat_to_vector_Point3(m, pts);
    }

    public static void Mat_to_vector_Point3f(Mat m, List<Point3> pts) {
        Mat_to_vector_Point3(m, pts);
    }

    public static void Mat_to_vector_Point3d(Mat m, List<Point3> pts) {
        Mat_to_vector_Point3(m, pts);
    }

    public static void Mat_to_vector_Point3(Mat m, List<Point3> pts) {
        if (pts == null) {
            throw new IllegalArgumentException("Output List can't be null");
        }
        int count = m.rows();
        int type = m.type();
        if (m.cols() != 1) {
            throw new IllegalArgumentException("Input Mat should have one column\n" + m);
        }
        pts.clear();
        if (type == CvType.CV_32SC3) {
            int[] buff = new int[(count * 3)];
            m.get(0, 0, buff);
            for (int i = 0; i < count; i++) {
                pts.add(new Point3((double) buff[i * 3], (double) buff[(i * 3) + 1], (double) buff[(i * 3) + 2]));
            }
        } else if (type == CvType.CV_32FC3) {
            float[] buff2 = new float[(count * 3)];
            m.get(0, 0, buff2);
            for (int i2 = 0; i2 < count; i2++) {
                pts.add(new Point3((double) buff2[i2 * 3], (double) buff2[(i2 * 3) + 1], (double) buff2[(i2 * 3) + 2]));
            }
        } else if (type == CvType.CV_64FC3) {
            double[] buff3 = new double[(count * 3)];
            m.get(0, 0, buff3);
            for (int i3 = 0; i3 < count; i3++) {
                pts.add(new Point3(buff3[i3 * 3], buff3[(i3 * 3) + 1], buff3[(i3 * 3) + 2]));
            }
        } else {
            throw new IllegalArgumentException("Input Mat should be of CV_32SC3, CV_32FC3 or CV_64FC3 type\n" + m);
        }
    }

    public static Mat vector_Mat_to_Mat(List<Mat> mats) {
        int count;
        if (mats != null) {
            count = mats.size();
        } else {
            count = 0;
        }
        if (count <= 0) {
            return new Mat();
        }
        Mat res = new Mat(count, 1, CvType.CV_32SC2);
        int[] buff = new int[(count * 2)];
        for (int i = 0; i < count; i++) {
            long addr = mats.get(i).nativeObj;
            buff[i * 2] = (int) (addr >> 32);
            buff[(i * 2) + 1] = (int) (-1 & addr);
        }
        res.put(0, 0, buff);
        return res;
    }

    public static void Mat_to_vector_Mat(Mat m, List<Mat> mats) {
        if (mats == null) {
            throw new IllegalArgumentException("mats == null");
        }
        int count = m.rows();
        if (CvType.CV_32SC2 == m.type() && m.cols() == 1) {
            mats.clear();
            int[] buff = new int[(count * 2)];
            m.get(0, 0, buff);
            for (int i = 0; i < count; i++) {
                mats.add(new Mat((((long) buff[i * 2]) << 32) | (((long) buff[(i * 2) + 1]) & 4294967295L)));
            }
            return;
        }
        throw new IllegalArgumentException("CvType.CV_32SC2 != m.type() ||  m.cols()!=1\n" + m);
    }

    public static Mat vector_float_to_Mat(List<Float> fs) {
        int count;
        if (fs != null) {
            count = fs.size();
        } else {
            count = 0;
        }
        if (count <= 0) {
            return new Mat();
        }
        Mat res = new Mat(count, 1, CvType.CV_32FC1);
        float[] buff = new float[count];
        for (int i = 0; i < count; i++) {
            buff[i] = fs.get(i).floatValue();
        }
        res.put(0, 0, buff);
        return res;
    }

    public static void Mat_to_vector_float(Mat m, List<Float> fs) {
        if (fs == null) {
            throw new IllegalArgumentException("fs == null");
        }
        int count = m.rows();
        if (CvType.CV_32FC1 == m.type() && m.cols() == 1) {
            fs.clear();
            float[] buff = new float[count];
            m.get(0, 0, buff);
            for (int i = 0; i < count; i++) {
                fs.add(Float.valueOf(buff[i]));
            }
            return;
        }
        throw new IllegalArgumentException("CvType.CV_32FC1 != m.type() ||  m.cols()!=1\n" + m);
    }

    public static Mat vector_uchar_to_Mat(List<Byte> bs) {
        int count;
        if (bs != null) {
            count = bs.size();
        } else {
            count = 0;
        }
        if (count <= 0) {
            return new Mat();
        }
        Mat res = new Mat(count, 1, CvType.CV_8UC1);
        byte[] buff = new byte[count];
        for (int i = 0; i < count; i++) {
            buff[i] = bs.get(i).byteValue();
        }
        res.put(0, 0, buff);
        return res;
    }

    public static void Mat_to_vector_uchar(Mat m, List<Byte> us) {
        if (us == null) {
            throw new IllegalArgumentException("Output List can't be null");
        }
        int count = m.rows();
        if (CvType.CV_8UC1 == m.type() && m.cols() == 1) {
            us.clear();
            byte[] buff = new byte[count];
            m.get(0, 0, buff);
            for (int i = 0; i < count; i++) {
                us.add(Byte.valueOf(buff[i]));
            }
            return;
        }
        throw new IllegalArgumentException("CvType.CV_8UC1 != m.type() ||  m.cols()!=1\n" + m);
    }

    public static Mat vector_char_to_Mat(List<Byte> bs) {
        int count;
        if (bs != null) {
            count = bs.size();
        } else {
            count = 0;
        }
        if (count <= 0) {
            return new Mat();
        }
        Mat res = new Mat(count, 1, CvType.CV_8SC1);
        byte[] buff = new byte[count];
        for (int i = 0; i < count; i++) {
            buff[i] = bs.get(i).byteValue();
        }
        res.put(0, 0, buff);
        return res;
    }

    public static Mat vector_int_to_Mat(List<Integer> is) {
        int count;
        if (is != null) {
            count = is.size();
        } else {
            count = 0;
        }
        if (count <= 0) {
            return new Mat();
        }
        Mat res = new Mat(count, 1, CvType.CV_32SC1);
        int[] buff = new int[count];
        for (int i = 0; i < count; i++) {
            buff[i] = is.get(i).intValue();
        }
        res.put(0, 0, buff);
        return res;
    }

    public static void Mat_to_vector_int(Mat m, List<Integer> is) {
        if (is == null) {
            throw new IllegalArgumentException("is == null");
        }
        int count = m.rows();
        if (CvType.CV_32SC1 == m.type() && m.cols() == 1) {
            is.clear();
            int[] buff = new int[count];
            m.get(0, 0, buff);
            for (int i = 0; i < count; i++) {
                is.add(Integer.valueOf(buff[i]));
            }
            return;
        }
        throw new IllegalArgumentException("CvType.CV_32SC1 != m.type() ||  m.cols()!=1\n" + m);
    }

    public static void Mat_to_vector_char(Mat m, List<Byte> bs) {
        if (bs == null) {
            throw new IllegalArgumentException("Output List can't be null");
        }
        int count = m.rows();
        if (CvType.CV_8SC1 == m.type() && m.cols() == 1) {
            bs.clear();
            byte[] buff = new byte[count];
            m.get(0, 0, buff);
            for (int i = 0; i < count; i++) {
                bs.add(Byte.valueOf(buff[i]));
            }
            return;
        }
        throw new IllegalArgumentException("CvType.CV_8SC1 != m.type() ||  m.cols()!=1\n" + m);
    }

    public static Mat vector_Rect_to_Mat(List<Rect> rs) {
        int count;
        if (rs != null) {
            count = rs.size();
        } else {
            count = 0;
        }
        if (count <= 0) {
            return new Mat();
        }
        Mat res = new Mat(count, 1, CvType.CV_32SC4);
        int[] buff = new int[(count * 4)];
        for (int i = 0; i < count; i++) {
            Rect r = rs.get(i);
            buff[i * 4] = r.f1130x;
            buff[(i * 4) + 1] = r.f1131y;
            buff[(i * 4) + 2] = r.width;
            buff[(i * 4) + 3] = r.height;
        }
        res.put(0, 0, buff);
        return res;
    }

    public static void Mat_to_vector_Rect(Mat m, List<Rect> rs) {
        if (rs == null) {
            throw new IllegalArgumentException("rs == null");
        }
        int count = m.rows();
        if (CvType.CV_32SC4 == m.type() && m.cols() == 1) {
            rs.clear();
            int[] buff = new int[(count * 4)];
            m.get(0, 0, buff);
            for (int i = 0; i < count; i++) {
                rs.add(new Rect(buff[i * 4], buff[(i * 4) + 1], buff[(i * 4) + 2], buff[(i * 4) + 3]));
            }
            return;
        }
        throw new IllegalArgumentException("CvType.CV_32SC4 != m.type() ||  m.rows()!=1\n" + m);
    }

    public static Mat vector_KeyPoint_to_Mat(List<KeyPoint> kps) {
        int count;
        if (kps != null) {
            count = kps.size();
        } else {
            count = 0;
        }
        if (count <= 0) {
            return new Mat();
        }
        Mat res = new Mat(count, 1, CvType.CV_64FC(7));
        double[] buff = new double[(count * 7)];
        for (int i = 0; i < count; i++) {
            KeyPoint kp = kps.get(i);
            buff[i * 7] = kp.f1124pt.f1125x;
            buff[(i * 7) + 1] = kp.f1124pt.f1126y;
            buff[(i * 7) + 2] = (double) kp.size;
            buff[(i * 7) + 3] = (double) kp.angle;
            buff[(i * 7) + 4] = (double) kp.response;
            buff[(i * 7) + 5] = (double) kp.octave;
            buff[(i * 7) + 6] = (double) kp.class_id;
        }
        res.put(0, 0, buff);
        return res;
    }

    public static void Mat_to_vector_KeyPoint(Mat m, List<KeyPoint> kps) {
        if (kps == null) {
            throw new IllegalArgumentException("Output List can't be null");
        }
        int count = m.rows();
        if (CvType.CV_64FC(7) == m.type() && m.cols() == 1) {
            kps.clear();
            double[] buff = new double[(count * 7)];
            m.get(0, 0, buff);
            for (int i = 0; i < count; i++) {
                kps.add(new KeyPoint((float) buff[i * 7], (float) buff[(i * 7) + 1], (float) buff[(i * 7) + 2], (float) buff[(i * 7) + 3], (float) buff[(i * 7) + 4], (int) buff[(i * 7) + 5], (int) buff[(i * 7) + 6]));
            }
            return;
        }
        throw new IllegalArgumentException("CvType.CV_64FC(7) != m.type() ||  m.cols()!=1\n" + m);
    }

    public static Mat vector_vector_Point_to_Mat(List<MatOfPoint> pts, List<Mat> mats) {
        if ((pts != null ? pts.size() : 0) <= 0) {
            return new Mat();
        }
        for (MatOfPoint vpt : pts) {
            mats.add(vpt);
        }
        return vector_Mat_to_Mat(mats);
    }

    public static void Mat_to_vector_vector_Point(Mat m, List<MatOfPoint> pts) {
        if (pts == null) {
            throw new IllegalArgumentException("Output List can't be null");
        } else if (m == null) {
            throw new IllegalArgumentException("Input Mat can't be null");
        } else {
            List<Mat> mats = new ArrayList<>(m.rows());
            Mat_to_vector_Mat(m, mats);
            for (Mat mi : mats) {
                pts.add(new MatOfPoint(mi));
                mi.release();
            }
            mats.clear();
        }
    }

    public static void Mat_to_vector_vector_Point2f(Mat m, List<MatOfPoint2f> pts) {
        if (pts == null) {
            throw new IllegalArgumentException("Output List can't be null");
        } else if (m == null) {
            throw new IllegalArgumentException("Input Mat can't be null");
        } else {
            List<Mat> mats = new ArrayList<>(m.rows());
            Mat_to_vector_Mat(m, mats);
            for (Mat mi : mats) {
                pts.add(new MatOfPoint2f(mi));
                mi.release();
            }
            mats.clear();
        }
    }

    public static Mat vector_vector_Point2f_to_Mat(List<MatOfPoint2f> pts, List<Mat> mats) {
        if ((pts != null ? pts.size() : 0) <= 0) {
            return new Mat();
        }
        for (MatOfPoint2f vpt : pts) {
            mats.add(vpt);
        }
        return vector_Mat_to_Mat(mats);
    }

    public static void Mat_to_vector_vector_Point3f(Mat m, List<MatOfPoint3f> pts) {
        if (pts == null) {
            throw new IllegalArgumentException("Output List can't be null");
        } else if (m == null) {
            throw new IllegalArgumentException("Input Mat can't be null");
        } else {
            List<Mat> mats = new ArrayList<>(m.rows());
            Mat_to_vector_Mat(m, mats);
            for (Mat mi : mats) {
                pts.add(new MatOfPoint3f(mi));
                mi.release();
            }
            mats.clear();
        }
    }

    public static Mat vector_vector_Point3f_to_Mat(List<MatOfPoint3f> pts, List<Mat> mats) {
        if ((pts != null ? pts.size() : 0) <= 0) {
            return new Mat();
        }
        for (MatOfPoint3f vpt : pts) {
            mats.add(vpt);
        }
        return vector_Mat_to_Mat(mats);
    }

    public static Mat vector_vector_KeyPoint_to_Mat(List<MatOfKeyPoint> kps, List<Mat> mats) {
        if ((kps != null ? kps.size() : 0) <= 0) {
            return new Mat();
        }
        for (MatOfKeyPoint vkp : kps) {
            mats.add(vkp);
        }
        return vector_Mat_to_Mat(mats);
    }

    public static void Mat_to_vector_vector_KeyPoint(Mat m, List<MatOfKeyPoint> kps) {
        if (kps == null) {
            throw new IllegalArgumentException("Output List can't be null");
        } else if (m == null) {
            throw new IllegalArgumentException("Input Mat can't be null");
        } else {
            List<Mat> mats = new ArrayList<>(m.rows());
            Mat_to_vector_Mat(m, mats);
            for (Mat mi : mats) {
                kps.add(new MatOfKeyPoint(mi));
                mi.release();
            }
            mats.clear();
        }
    }

    public static Mat vector_double_to_Mat(List<Double> ds) {
        int count;
        if (ds != null) {
            count = ds.size();
        } else {
            count = 0;
        }
        if (count <= 0) {
            return new Mat();
        }
        Mat res = new Mat(count, 1, CvType.CV_64FC1);
        double[] buff = new double[count];
        for (int i = 0; i < count; i++) {
            buff[i] = ds.get(i).doubleValue();
        }
        res.put(0, 0, buff);
        return res;
    }

    public static void Mat_to_vector_double(Mat m, List<Double> ds) {
        if (ds == null) {
            throw new IllegalArgumentException("ds == null");
        }
        int count = m.rows();
        if (CvType.CV_64FC1 == m.type() && m.cols() == 1) {
            ds.clear();
            double[] buff = new double[count];
            m.get(0, 0, buff);
            for (int i = 0; i < count; i++) {
                ds.add(Double.valueOf(buff[i]));
            }
            return;
        }
        throw new IllegalArgumentException("CvType.CV_64FC1 != m.type() ||  m.cols()!=1\n" + m);
    }

    public static Mat vector_DMatch_to_Mat(List<DMatch> matches) {
        int count;
        if (matches != null) {
            count = matches.size();
        } else {
            count = 0;
        }
        if (count <= 0) {
            return new Mat();
        }
        Mat res = new Mat(count, 1, CvType.CV_64FC4);
        double[] buff = new double[(count * 4)];
        for (int i = 0; i < count; i++) {
            DMatch m = matches.get(i);
            buff[i * 4] = (double) m.queryIdx;
            buff[(i * 4) + 1] = (double) m.trainIdx;
            buff[(i * 4) + 2] = (double) m.imgIdx;
            buff[(i * 4) + 3] = (double) m.distance;
        }
        res.put(0, 0, buff);
        return res;
    }

    public static void Mat_to_vector_DMatch(Mat m, List<DMatch> matches) {
        if (matches == null) {
            throw new IllegalArgumentException("Output List can't be null");
        }
        int count = m.rows();
        if (CvType.CV_64FC4 == m.type() && m.cols() == 1) {
            matches.clear();
            double[] buff = new double[(count * 4)];
            m.get(0, 0, buff);
            for (int i = 0; i < count; i++) {
                matches.add(new DMatch((int) buff[i * 4], (int) buff[(i * 4) + 1], (int) buff[(i * 4) + 2], (float) buff[(i * 4) + 3]));
            }
            return;
        }
        throw new IllegalArgumentException("CvType.CV_64FC4 != m.type() ||  m.cols()!=1\n" + m);
    }

    public static Mat vector_vector_DMatch_to_Mat(List<MatOfDMatch> lvdm, List<Mat> mats) {
        if ((lvdm != null ? lvdm.size() : 0) <= 0) {
            return new Mat();
        }
        for (MatOfDMatch vdm : lvdm) {
            mats.add(vdm);
        }
        return vector_Mat_to_Mat(mats);
    }

    public static void Mat_to_vector_vector_DMatch(Mat m, List<MatOfDMatch> lvdm) {
        if (lvdm == null) {
            throw new IllegalArgumentException("Output List can't be null");
        } else if (m == null) {
            throw new IllegalArgumentException("Input Mat can't be null");
        } else {
            List<Mat> mats = new ArrayList<>(m.rows());
            Mat_to_vector_Mat(m, mats);
            lvdm.clear();
            for (Mat mi : mats) {
                lvdm.add(new MatOfDMatch(mi));
                mi.release();
            }
            mats.clear();
        }
    }

    public static Mat vector_vector_char_to_Mat(List<MatOfByte> lvb, List<Mat> mats) {
        if ((lvb != null ? lvb.size() : 0) <= 0) {
            return new Mat();
        }
        for (MatOfByte vb : lvb) {
            mats.add(vb);
        }
        return vector_Mat_to_Mat(mats);
    }

    public static void Mat_to_vector_vector_char(Mat m, List<List<Byte>> llb) {
        if (llb == null) {
            throw new IllegalArgumentException("Output List can't be null");
        } else if (m == null) {
            throw new IllegalArgumentException("Input Mat can't be null");
        } else {
            List<Mat> mats = new ArrayList<>(m.rows());
            Mat_to_vector_Mat(m, mats);
            for (Mat mi : mats) {
                List<Byte> lb = new ArrayList<>();
                Mat_to_vector_char(mi, lb);
                llb.add(lb);
                mi.release();
            }
            mats.clear();
        }
    }
}
