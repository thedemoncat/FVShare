package com.tuyenmonkey.mkloader.util;

import com.tuyenmonkey.mkloader.exception.InvalidNumberOfPulseException;
import com.tuyenmonkey.mkloader.type.ClassicSpinner;
import com.tuyenmonkey.mkloader.type.FishSpinner;
import com.tuyenmonkey.mkloader.type.LineSpinner;
import com.tuyenmonkey.mkloader.type.LoaderView;
import com.tuyenmonkey.mkloader.type.PhoneWave;
import com.tuyenmonkey.mkloader.type.Pulse;
import com.tuyenmonkey.mkloader.type.Radar;
import com.tuyenmonkey.mkloader.type.Sharingan;
import com.tuyenmonkey.mkloader.type.TwinFishesSpinner;
import com.tuyenmonkey.mkloader.type.Whirlpool;
import com.tuyenmonkey.mkloader.type.Worm;

public class LoaderGenerator {
    public static LoaderView generateLoaderView(int type) {
        switch (type) {
            case 0:
                return new ClassicSpinner();
            case 1:
                return new FishSpinner();
            case 2:
                return new LineSpinner();
            case 3:
                try {
                    return new Pulse(3);
                } catch (InvalidNumberOfPulseException e) {
                    e.printStackTrace();
                    break;
                }
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                return new TwinFishesSpinner();
            case 8:
                return new Worm();
            case 9:
                return new Whirlpool();
            case 10:
                return new PhoneWave();
            case 11:
                return new Sharingan();
            default:
                return new ClassicSpinner();
        }
        try {
            return new Pulse(5);
        } catch (InvalidNumberOfPulseException e2) {
            e2.printStackTrace();
        }
        try {
            return new Pulse(4);
        } catch (InvalidNumberOfPulseException e3) {
            e3.printStackTrace();
        }
        return new Radar();
    }

    public static LoaderView generateLoaderView(String type) {
        char c = 65535;
        switch (type.hashCode()) {
            case -1984395789:
                if (type.equals("FourPulse")) {
                    c = 4;
                    break;
                }
                break;
            case -1566594943:
                if (type.equals("FishSpinner")) {
                    c = 1;
                    break;
                }
                break;
            case -805352437:
                if (type.equals("TwinFishesSpinner")) {
                    c = 7;
                    break;
                }
                break;
            case 2702131:
                if (type.equals("Worm")) {
                    c = 8;
                    break;
                }
                break;
            case 50472805:
                if (type.equals("LineSpinner")) {
                    c = 2;
                    break;
                }
                break;
            case 78717670:
                if (type.equals("Radar")) {
                    c = 6;
                    break;
                }
                break;
            case 299449070:
                if (type.equals("Whirlpool")) {
                    c = 9;
                    break;
                }
                break;
            case 426399209:
                if (type.equals("Sharingan")) {
                    c = 11;
                    break;
                }
                break;
            case 776041799:
                if (type.equals("ClassicSpinner")) {
                    c = 0;
                    break;
                }
                break;
            case 905524411:
                if (type.equals("ThreePulse")) {
                    c = 3;
                    break;
                }
                break;
            case 958968807:
                if (type.equals("FivePulse")) {
                    c = 5;
                    break;
                }
                break;
            case 975043943:
                if (type.equals("PhoneWave")) {
                    c = 10;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return new ClassicSpinner();
            case 1:
                return new FishSpinner();
            case 2:
                return new LineSpinner();
            case 3:
                try {
                    return new Pulse(3);
                } catch (InvalidNumberOfPulseException e) {
                    e.printStackTrace();
                    break;
                }
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                return new TwinFishesSpinner();
            case 8:
                return new Worm();
            case 9:
                return new Whirlpool();
            case 10:
                return new PhoneWave();
            case 11:
                return new Sharingan();
            default:
                return new ClassicSpinner();
        }
        return new Radar();
        try {
            return new Pulse(5);
        } catch (InvalidNumberOfPulseException e2) {
            e2.printStackTrace();
        }
        try {
            return new Pulse(4);
        } catch (InvalidNumberOfPulseException e3) {
            e3.printStackTrace();
        }
    }
}
