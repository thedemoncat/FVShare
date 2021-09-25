package com.freevisiontech.fvmobile.model.resolver;

import org.opencv.p011ml.DTrees;

public class BluetoothClassResolver {
    public static String resolveDeviceClass(int btClass) {
        switch (btClass) {
            case 256:
                return "Computer, Uncategorized";
            case CompanyIdentifierResolver.PLUS_LOCATIONS_SYSTEMS_PTY_LTD /*260*/:
                return "Computer, Desktop";
            case CompanyIdentifierResolver.CHICONY_ELECTRONICS_CO_LTD /*264*/:
                return "Computer, Server";
            case CompanyIdentifierResolver.TRANSDUCERS_DIRECT_LLC /*268*/:
                return "Computer, Laptop";
            case CompanyIdentifierResolver.NIPPON_SEIKI_CO_LTD /*272*/:
                return "Computer, Handheld PC/PDA";
            case CompanyIdentifierResolver.XENSR /*276*/:
                return "Computer, Palm Size PC/PDA";
            case CompanyIdentifierResolver.RADIUS_NETWORKS_INC /*280*/:
                return "Computer, Wearable";
            case 512:
                return "Phone, Uncategorized";
            case 516:
                return "Phone, Cellular";
            case 520:
                return "Phone, Cordless";
            case 524:
                return "Phone, Smart";
            case 528:
                return "Phone, Modem or Gateway";
            case 532:
                return "Phone, ISDN";
            case 1024:
                return "A/V, Uncategorized";
            case 1028:
                return "A/V, Video Wearable Headset";
            case 1032:
                return "A/V, Handsfree";
            case 1040:
                return "A/V, Microphone";
            case 1044:
                return "A/V, Loudspeaker";
            case 1048:
                return "A/V, Headphones";
            case 1052:
                return "A/V, Portable Audio";
            case 1056:
                return "A/V, Car Audio";
            case 1060:
                return "A/V, Set Top Box";
            case 1064:
                return "A/V, HiFi Audio";
            case 1068:
                return "A/V, VCR";
            case 1072:
                return "A/V, Video Camera";
            case 1076:
                return "A/V, Camcorder";
            case 1080:
                return "A/V, Video Monitor";
            case 1084:
                return "A/V, Video Display and Loudspeaker";
            case 1088:
                return "A/V, Video Conferencing";
            case 1096:
                return "A/V, Video Gaming Toy";
            case 1792:
                return "Wearable, Uncategorized";
            case 1796:
                return "Wearable, Wrist Watch";
            case 1800:
                return "Wearable, Pager";
            case 1804:
                return "Wearable, Jacket";
            case 1808:
                return "Wearable, Helmet";
            case 1812:
                return "Wearable, Glasses";
            case 2048:
                return "Toy, Uncategorized";
            case 2052:
                return "Toy, Robot";
            case 2056:
                return "Toy, Vehicle";
            case 2060:
                return "Toy, Doll/Action Figure";
            case 2064:
                return "Toy, Controller";
            case 2068:
                return "Toy, Game";
            case 2304:
                return "Health, Uncategorized";
            case 2308:
                return "Health, Blood Pressure";
            case 2312:
                return "Health, Thermometer";
            case 2316:
                return "Health, Weighting";
            case 2320:
                return "Health, Glucose";
            case 2324:
                return "Health, Pulse Oximeter";
            case 2328:
                return "Health, Pulse Rate";
            case 2332:
                return "Health, Data Display";
            default:
                return "Unknown, Unknown (class=" + btClass + ")";
        }
    }

    public static String resolveMajorDeviceClass(int majorBtClass) {
        switch (majorBtClass) {
            case 0:
                return "Misc";
            case 256:
                return "Computer";
            case 512:
                return "Phone";
            case DTrees.PREDICT_MASK /*768*/:
                return "Networking";
            case 1024:
                return "Audio/ Video";
            case 1280:
                return "Peripheral";
            case 1536:
                return "Imaging";
            case 1792:
                return "Wearable";
            case 2048:
                return "Toy";
            case 2304:
                return "Health";
            case 7936:
                return "Uncategorized";
            default:
                return "Unknown (" + majorBtClass + ")";
        }
    }
}
