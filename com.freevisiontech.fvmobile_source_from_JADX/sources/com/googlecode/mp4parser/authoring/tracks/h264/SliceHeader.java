package com.googlecode.mp4parser.authoring.tracks.h264;

import com.googlecode.mp4parser.h264.model.PictureParameterSet;
import com.googlecode.mp4parser.h264.model.SeqParameterSet;
import com.googlecode.mp4parser.h264.read.CAVLCReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class SliceHeader {
    public boolean bottom_field_flag = false;
    public int colour_plane_id;
    public int delta_pic_order_cnt_0;
    public int delta_pic_order_cnt_1;
    public int delta_pic_order_cnt_bottom;
    public boolean field_pic_flag = false;
    public int first_mb_in_slice;
    public int frame_num;
    public int idr_pic_id;
    public int pic_order_cnt_lsb;
    public int pic_parameter_set_id;
    PictureParameterSet pps;
    public SliceType slice_type;
    SeqParameterSet sps;

    public enum SliceType {
        P,
        B,
        I,
        SP,
        SI
    }

    public SliceHeader(InputStream is, Map<Integer, SeqParameterSet> spss, Map<Integer, PictureParameterSet> ppss, boolean IdrPicFlag) {
        try {
            is.read();
            CAVLCReader reader = new CAVLCReader(is);
            this.first_mb_in_slice = reader.readUE("SliceHeader: first_mb_in_slice");
            switch (reader.readUE("SliceHeader: slice_type")) {
                case 0:
                case 5:
                    this.slice_type = SliceType.P;
                    break;
                case 1:
                case 6:
                    this.slice_type = SliceType.B;
                    break;
                case 2:
                case 7:
                    this.slice_type = SliceType.I;
                    break;
                case 3:
                case 8:
                    this.slice_type = SliceType.SP;
                    break;
                case 4:
                case 9:
                    this.slice_type = SliceType.SI;
                    break;
            }
            this.pic_parameter_set_id = reader.readUE("SliceHeader: pic_parameter_set_id");
            this.pps = ppss.get(Integer.valueOf(this.pic_parameter_set_id));
            this.sps = spss.get(Integer.valueOf(this.pps.seq_parameter_set_id));
            if (this.sps.residual_color_transform_flag) {
                this.colour_plane_id = reader.readU(2, "SliceHeader: colour_plane_id");
            }
            this.frame_num = reader.readU(this.sps.log2_max_frame_num_minus4 + 4, "SliceHeader: frame_num");
            if (!this.sps.frame_mbs_only_flag) {
                this.field_pic_flag = reader.readBool("SliceHeader: field_pic_flag");
                if (this.field_pic_flag) {
                    this.bottom_field_flag = reader.readBool("SliceHeader: bottom_field_flag");
                }
            }
            if (IdrPicFlag) {
                this.idr_pic_id = reader.readUE("SliceHeader: idr_pic_id");
            }
            if (this.sps.pic_order_cnt_type == 0) {
                this.pic_order_cnt_lsb = reader.readU(this.sps.log2_max_pic_order_cnt_lsb_minus4 + 4, "SliceHeader: pic_order_cnt_lsb");
                if (this.pps.bottom_field_pic_order_in_frame_present_flag && !this.field_pic_flag) {
                    this.delta_pic_order_cnt_bottom = reader.readSE("SliceHeader: delta_pic_order_cnt_bottom");
                }
            }
            if (this.sps.pic_order_cnt_type == 1 && !this.sps.delta_pic_order_always_zero_flag) {
                this.delta_pic_order_cnt_0 = reader.readSE("delta_pic_order_cnt_0");
                if (this.pps.bottom_field_pic_order_in_frame_present_flag && !this.field_pic_flag) {
                    this.delta_pic_order_cnt_1 = reader.readSE("delta_pic_order_cnt_1");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        return "SliceHeader{first_mb_in_slice=" + this.first_mb_in_slice + ", slice_type=" + this.slice_type + ", pic_parameter_set_id=" + this.pic_parameter_set_id + ", colour_plane_id=" + this.colour_plane_id + ", frame_num=" + this.frame_num + ", field_pic_flag=" + this.field_pic_flag + ", bottom_field_flag=" + this.bottom_field_flag + ", idr_pic_id=" + this.idr_pic_id + ", pic_order_cnt_lsb=" + this.pic_order_cnt_lsb + ", delta_pic_order_cnt_bottom=" + this.delta_pic_order_cnt_bottom + '}';
    }
}
