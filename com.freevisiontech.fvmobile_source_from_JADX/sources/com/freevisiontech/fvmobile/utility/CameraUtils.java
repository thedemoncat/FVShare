package com.freevisiontech.fvmobile.utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Range;
import android.view.MotionEvent;
import com.freevisiontech.cameralib.Size;
import com.freevisiontech.cameralib.impl.Camera2.Camera2Constants;
import com.freevisiontech.fvmobile.model.resolver.CompanyIdentifierResolver;
import com.freevisiontech.fvmobile.utils.BleByteUtil;
import com.freevisiontech.fvmobile.utils.Event;
import com.freevisiontech.fvmobile.utils.EventBusUtil;
import com.freevisiontech.fvmobile.utils.MoveTimelapseUtil;
import com.freevisiontech.fvmobile.widget.view.IntentKey;
import com.google.android.vending.expansion.downloader.Constants;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CameraUtils {
    public static int MAX_LEVEL = 1;
    public static int RECOMMEND_LEVEL = 0;
    public static final int VIVO_FILLET = 8;
    public static final int VIVO_NOTCH = 32;
    private static boolean apkIsFromGoogle = false;
    private static boolean apkIsFromNormal = false;
    public static float appMaxZoom = 3.0f;
    private static boolean blueConnectBooleanS = false;
    private static boolean booRecordStarted = false;
    private static boolean booleanTimeLapseUIShow = false;
    private static boolean bosIsResume = false;
    private static boolean btnBackBgColorIsYellow = false;
    private static boolean btnCameraBgColorIsYellow = false;
    private static int btnCameraStatus = 0;
    private static boolean btnSettingBgColorIsYellow = false;
    private static boolean btnViltaBgColorIsYellow = false;
    private static int btnViltaStatus = 0;
    private static int camExposureLengthProgress = 50;
    private static int camLengthProgress = -1;
    private static int camWtProRealTimeValue = 0;
    private static int cameraDeviceSum = 0;
    private static boolean cameraFocusing = false;
    private static boolean cameraHandModel = false;
    private static boolean cameraHandModelBgColorIsYellow = false;
    private static boolean cameraHandModelSmallImage = false;
    private static int cameraPreviewWidth = 1080;
    private static int cameraResolutionLevel = RECOMMEND_LEVEL;
    private static int checkMediaHighSpeedRecordFrontSize = 6;
    private static Map<Range<Integer>, Size[]> checkMediaHighSpeedRecordMapSize = null;
    private static int checkMediaHighSpeedRecordSize = 6;
    private static int checkMediaRecordFrontSize = 6;
    private static Size checkMediaRecordFrontSizeTwo = new Size(1920, 1080);
    private static int checkMediaRecordSize = 6;
    private static Size checkMediaRecordSizeTwo = new Size(1920, 1080);
    private static int currentPageIndex = 2;
    private static long currentTimeMillis = 0;
    private static long currentTimeMillisHandModel = 0;
    private static boolean delayPhotoIng = false;
    private static boolean delayTakePhotoIng = false;
    private static float displayDen = 3.0f;
    private static boolean followIng = false;
    private static int frameLayerNumber = 0;
    private static int frontCameraResolutionLevel = RECOMMEND_LEVEL;
    private static String fullCameraErrorCode = "";
    private static boolean fullShotIng = false;
    private static boolean fvAdvancedSettingActivityIsShow = false;
    private static MotionEvent gestureDownMotionEvent;
    public static int handModelScaleValueMF = 0;
    public static int handModelScaleValueWT = 0;
    public static int handModelVisibleStateValue = 1;
    private static boolean hasTakePhotoOrVideo = false;
    private static int heightPingMu = 720;
    private static double hitchCockEndPlaceMF = 1.0d;
    private static int hitchCockEndPlacePageAppears = 0;
    private static String hitchCockEndPlaceStringMF = "AF";
    private static float hitchCockEndPlaceWT = 1.0f;
    private static boolean hitchCockHoriScaleMF = true;
    private static double hitchCockStartPlaceMF = 1.0d;
    private static String hitchCockStartPlaceStringMF = "AF";
    private static float hitchCockStartPlaceWT = 1.0f;
    private static int hitchCockVideoTime = 1;
    private static boolean isCameraOpened = true;
    private static boolean isHitchCockUI = false;
    private static float kXPerScale = 0.2f;
    private static float kYPerScale = 0.2f;
    private static float kcfMoveX = 0.2f;
    private static float kcfMoveY = 0.2f;
    private static int labelTopBarSelect = -1;
    private static int labelTopBarSelectMemory = 3;
    private static List<String> listScaleIso = new ArrayList(Arrays.asList(new String[]{"29", "40", "50", "64", "80", "100", "125", "160", "200", "250", "320", "400", "500", "640", "800", "1000", "1250", "1600", "1856", "2000", "2200", "2300"}));
    private static float llMarkPointMfA = 0.0f;
    private static float llMarkPointMfB = 0.0f;
    private static float llMarkPointWtA = 1.0f;
    private static float llMarkPointWtB = 1.0f;
    private static boolean llRootBackgroundColor = true;
    private static boolean longExposureIng = false;
    public static int mMidCountScale = 22;
    public static int mRectHeight = 160;
    public static int mScaleHeight = 20;
    public static int mScaleMargin = 20;
    public static int mScaleMaxHeight = 40;
    public static int mTimeLapseDuration = 30;
    private static boolean markPointMfIsFirst = true;
    private static boolean markPointUIIsVisible = false;
    private static boolean markPointWtIsFirst = true;
    private static String maxSupPictureSize = null;
    private static int[] mediaRecordSize = null;
    private static Size[] mediaRecordSizeTwo = null;
    private static boolean menuKeyIsLongPress = false;
    private static long moveOrDelayTimeLapseCurrentTime = 0;
    private static boolean moveOrDelayTimeLapseIng = false;
    private static String moveOrDelayTimeLapsePath = null;
    private static float moveOrDelayTimeLapseShutter = 0.0f;
    private static boolean moveTimelapseIng = false;
    private static boolean moveTimelapseRecording = false;
    private static MotionEvent onDownMotionEvent;
    private static Point onDownMotionEventMarkAAA = null;
    private static Point onDownMotionEventMarkBBB = null;
    private static Point onDownOneFingerMotionEvent = null;
    private static int phoneAngle = 0;
    private static int photoNums = 0;
    private static int photoSelectNums = 0;
    private static Range<Integer> rangeScaleIso = null;
    private static Range<Long> rangeScaleShutter = null;
    private static String reComPictureSize = null;
    private static boolean recordingIng = false;
    private static String scaleScrollShutterAuto = "AUTO";
    private static double scaleScrollViewEV = 1.6d;
    private static double scaleScrollViewMF = 11.0d;
    private static double scaleScrollViewTradWT = 90.0d;
    private static double scaleScrollViewWB = 1.4d;
    private static double scaleScrollViewWT = 3.8d;
    private static boolean scaleTouchState = true;
    private static boolean scaleTouchStateEV = true;
    private static boolean scaleTouchStateISO = true;
    private static boolean scaleTouchStateMF = true;
    private static boolean scaleTouchStateShutter = true;
    private static boolean scaleTouchStateWB = true;
    private static boolean scaleTouchStateWT = true;
    public static int thumbWheelSelectionWTOrMF = 0;
    private static boolean timelapseIng = false;
    private static boolean topBarStatusHaveAnimation = false;
    private static int userCameraOneOrTwo = 1;
    private static int videoNums = 0;
    private static int videoSelectNums = 0;
    private static boolean videoStabilizationSupport = false;
    private static int widthPingMu = 1280;
    private static float zoomValue = 1.0f;

    public static int getCurrentPageIndex() {
        return currentPageIndex;
    }

    public static void setCurrentPageIndex(int currentPageIndex2) {
        currentPageIndex = currentPageIndex2;
    }

    public static void clearData() {
        fullShotIng = false;
        timelapseIng = false;
        moveTimelapseIng = false;
        moveTimelapseRecording = false;
        delayPhotoIng = false;
        longExposureIng = false;
        recordingIng = false;
        markPointUIIsVisible = false;
        llMarkPointMfA = 0.0f;
        llMarkPointMfB = 0.0f;
        llMarkPointWtA = 1.0f;
        llMarkPointWtB = 1.0f;
        markPointWtIsFirst = true;
        markPointMfIsFirst = true;
        thumbWheelSelectionWTOrMF = 0;
        labelTopBarSelectMemory = 3;
        labelTopBarSelect = -1;
        topBarStatusHaveAnimation = false;
        isHitchCockUI = false;
        hasTakePhotoOrVideo = false;
        delayTakePhotoIng = false;
        cameraPreviewWidth = 1080;
        kXPerScale = 0.2f;
        kYPerScale = 0.2f;
        kcfMoveX = 0.2f;
        kcfMoveY = 0.2f;
        photoNums = 0;
        videoNums = 0;
        photoSelectNums = 0;
        videoSelectNums = 0;
        scaleScrollViewMF = 11.0d;
        scaleScrollViewWB = 1.4d;
        scaleScrollViewEV = 1.6d;
        scaleScrollViewWT = 3.8d;
        scaleScrollViewTradWT = 90.0d;
        scaleScrollShutterAuto = "AUTO";
        scaleTouchState = true;
        scaleTouchStateEV = true;
        scaleTouchStateShutter = true;
        scaleTouchStateISO = true;
        scaleTouchStateWB = true;
        scaleTouchStateMF = true;
        scaleTouchStateWT = true;
        cameraHandModel = false;
        widthPingMu = 1280;
        heightPingMu = 720;
        isHitchCockUI = false;
        cameraFocusing = false;
        camLengthProgress = -1;
        camWtProRealTimeValue = 0;
        camExposureLengthProgress = 50;
        moveOrDelayTimeLapsePath = null;
        moveOrDelayTimeLapseShutter = 0.0f;
        moveOrDelayTimeLapseIng = false;
        moveOrDelayTimeLapseCurrentTime = 0;
        videoStabilizationSupport = false;
        userCameraOneOrTwo = 1;
        checkMediaRecordFrontSize = 6;
        checkMediaHighSpeedRecordFrontSize = 6;
        bosIsResume = false;
        booRecordStarted = false;
        isCameraOpened = true;
        cameraHandModelSmallImage = false;
        hitchCockStartPlaceWT = 1.0f;
        hitchCockStartPlaceMF = 1.0d;
        hitchCockEndPlaceWT = 1.0f;
        hitchCockEndPlaceMF = 1.0d;
        hitchCockEndPlacePageAppears = 0;
        hitchCockVideoTime = 1;
        hitchCockHoriScaleMF = true;
        hitchCockStartPlaceStringMF = "AF";
        hitchCockEndPlaceStringMF = "AF";
        frameLayerNumber = 0;
        llRootBackgroundColor = true;
        fvAdvancedSettingActivityIsShow = false;
        btnBackBgColorIsYellow = false;
        btnCameraBgColorIsYellow = false;
        btnViltaBgColorIsYellow = false;
        btnSettingBgColorIsYellow = false;
        btnCameraStatus = 0;
        btnViltaStatus = 0;
        zoomValue = 1.0f;
        cameraHandModelBgColorIsYellow = false;
        menuKeyIsLongPress = false;
        booleanTimeLapseUIShow = false;
        topBarStatusHaveAnimation = false;
        currentTimeMillis = 0;
        currentTimeMillisHandModel = 0;
        thumbWheelSelectionWTOrMF = 0;
        handModelVisibleStateValue = 1;
        handModelScaleValueMF = 0;
        handModelScaleValueWT = 0;
        mTimeLapseDuration = 30;
    }

    public static boolean getBosIsResume() {
        return bosIsResume;
    }

    public static void setBosIsResume(boolean bosIsResume2) {
        bosIsResume = bosIsResume2;
    }

    public static boolean getBooRecordStarted() {
        return booRecordStarted;
    }

    public static void setBooRecordStarted(boolean booRecordStarted2) {
        booRecordStarted = booRecordStarted2;
    }

    public static int getMaxSupOrReComPictureSize() {
        return cameraResolutionLevel;
    }

    public static void setMaxSupOrReComPictureSize(int level) {
        cameraResolutionLevel = level;
    }

    public static int getMaxSupOrReComPictureFrontSize() {
        return frontCameraResolutionLevel;
    }

    public static void setMaxSupOrReComPictureFrontSize(int level) {
        frontCameraResolutionLevel = level;
    }

    public static String getMaxSupPictureSize() {
        return maxSupPictureSize;
    }

    public static void setMaxSupPictureSize(String maxSupPictureSize2) {
        maxSupPictureSize = maxSupPictureSize2;
    }

    public static String getReComPictureSize() {
        return reComPictureSize;
    }

    public static void setReComPictureSize(String reComPictureSize2) {
        reComPictureSize = reComPictureSize2;
    }

    public static void setIsCameraOpened(boolean isCameraOpened2) {
        isCameraOpened = isCameraOpened2;
    }

    public static boolean getIsCameraOpened() {
        return isCameraOpened;
    }

    public static int getCheckMediaRecordSize() {
        return checkMediaRecordSize;
    }

    public static void setCheckMediaRecordSize(int checkMediaRecordSize2) {
        checkMediaRecordSize = checkMediaRecordSize2;
    }

    public static Map<Range<Integer>, Size[]> getCheckMediaHighSpeedRecordMapSize() {
        return checkMediaHighSpeedRecordMapSize;
    }

    public static void setCheckMediaHighSpeedRecordMapSize(Map<Range<Integer>, Size[]> highSpeedRecordMapSize) {
        checkMediaHighSpeedRecordMapSize = highSpeedRecordMapSize;
    }

    public static int getCheckMediaHighSpeedRecordSize() {
        return checkMediaHighSpeedRecordSize;
    }

    public static void setCheckMediaHighSpeedRecordSize(int checkMediaHighSpeedRecordSize2) {
        checkMediaHighSpeedRecordSize = checkMediaHighSpeedRecordSize2;
    }

    public static boolean getVideoStabilizationSupport() {
        return videoStabilizationSupport;
    }

    public static void setVideoStabilizationSupport(Boolean videoStabilizatSupport) {
        videoStabilizationSupport = videoStabilizatSupport.booleanValue();
    }

    public static Size getCheckMediaRecordSizeTwo() {
        return checkMediaRecordSizeTwo;
    }

    public static void setCheckMediaRecordSizeTwo(Size checkMediaRecordSizeT) {
        checkMediaRecordSizeTwo = checkMediaRecordSizeT;
    }

    public static int getUserCameraOneOrTwo() {
        return userCameraOneOrTwo;
    }

    public static void setUserCameraOneOrTwo(int userCameraOneOrT) {
        userCameraOneOrTwo = userCameraOneOrT;
    }

    public static int getCheckMediaRecordFrontSize() {
        return checkMediaRecordFrontSize;
    }

    public static void setCheckMediaRecordFrontSize(int checkMediaRecordFrontSize2) {
        checkMediaRecordFrontSize = checkMediaRecordFrontSize2;
    }

    public static int getCheckMediaHighSpeedRecordFrontSize() {
        return checkMediaHighSpeedRecordFrontSize;
    }

    public static void setCheckMediaHighSpeedRecordFrontSize(int checkMediaHighSpeedRecordFrontSize2) {
        checkMediaHighSpeedRecordFrontSize = checkMediaHighSpeedRecordFrontSize2;
    }

    public static Size getCheckMediaRecordFrontSizeTwo() {
        return checkMediaRecordFrontSizeTwo;
    }

    public static void setCheckMediaRecordFrontSizeTwo(Size checkMediaRecordFrontSizeT) {
        checkMediaRecordFrontSizeTwo = checkMediaRecordFrontSizeT;
    }

    public static int[] getMediaRecordSize() {
        return mediaRecordSize;
    }

    public static void setMediaRecordSize(int[] mediaRecordSize2) {
        mediaRecordSize = mediaRecordSize2;
    }

    public static Size[] getMediaRecordSizeTwo() {
        return mediaRecordSizeTwo;
    }

    public static void setMediaRecordSizeTwo(Size[] mediaRecordSizeT) {
        mediaRecordSizeTwo = mediaRecordSizeT;
    }

    public static String getMoveOrDelayTimeLapsePath() {
        return moveOrDelayTimeLapsePath;
    }

    public static void setMoveOrDelayTimeLapsePath(String moveOrDelayTimeLapsePath2) {
        moveOrDelayTimeLapsePath = moveOrDelayTimeLapsePath2;
    }

    public static float getMoveOrDelayTimeLapseShutter() {
        return moveOrDelayTimeLapseShutter;
    }

    public static void setMoveOrDelayTimeLapseShutter(float moveOrDelayTimeLapseShutter2) {
        moveOrDelayTimeLapseShutter = moveOrDelayTimeLapseShutter2;
    }

    public static boolean isMoveOrDelayTimeLapseIng() {
        return moveOrDelayTimeLapseIng;
    }

    public static void setMoveOrDelayTimeLapseIng(boolean moveOrDelayTimeLapseIng2) {
        moveOrDelayTimeLapseIng = moveOrDelayTimeLapseIng2;
    }

    public static long getMoveOrDelayTimeLapseCurrentTime() {
        return moveOrDelayTimeLapseCurrentTime;
    }

    public static void setMoveOrDelayTimeLapseCurrentTime(long moveOrDelayTimeLapseCurrentTime2) {
        moveOrDelayTimeLapseCurrentTime = moveOrDelayTimeLapseCurrentTime2;
    }

    public static int getCamLengthProgress() {
        return camLengthProgress;
    }

    public static void setCamLengthProgress(int camLengthProgress2) {
        camLengthProgress = camLengthProgress2;
    }

    public static int getCamExposureLengthProgress() {
        return camExposureLengthProgress;
    }

    public static void setCamExposureLengthProgress(int camExposureLengthProgress2) {
        camExposureLengthProgress = camExposureLengthProgress2;
    }

    public static int getWidthPingMu() {
        return widthPingMu;
    }

    public static void setWidthPingMu(int widthPingMu2) {
        widthPingMu = widthPingMu2;
    }

    public static int getHeightPingMu() {
        return heightPingMu;
    }

    public static void setHeightPingMu(int heightPingMu2) {
        heightPingMu = heightPingMu2;
    }

    public static boolean isRecordingIng() {
        return recordingIng;
    }

    public static void setRecordingIng(boolean recordingIng2) {
        recordingIng = recordingIng2;
    }

    public static boolean isMoveTimelapseRecording() {
        return moveTimelapseRecording;
    }

    public static void setMoveTimelapseRecording(boolean moveTimelapseRecording2) {
        moveTimelapseRecording = moveTimelapseRecording2;
    }

    public static boolean isFollowIng() {
        return followIng;
    }

    public static void setFollowIng(boolean followIng2) {
        followIng = followIng2;
    }

    public static boolean isHasTakePhotoOrVideo() {
        return hasTakePhotoOrVideo;
    }

    public static boolean isDelayTakePhotoIng() {
        return delayTakePhotoIng;
    }

    public static void setDelayTakePhotoIng(boolean delayTakePhotoIng2) {
        delayTakePhotoIng = delayTakePhotoIng2;
    }

    public static boolean isFullShotIng() {
        return fullShotIng;
    }

    public static void setFullShotIng(boolean fullShotIng2) {
        fullShotIng = fullShotIng2;
    }

    public static boolean isTimelapseIng() {
        return timelapseIng;
    }

    public static void setTimelapseIng(boolean timelapseIng2) {
        timelapseIng = timelapseIng2;
    }

    public static boolean isMoveTimelapseIng() {
        return moveTimelapseIng;
    }

    public static void setMoveTimelapseIng(boolean moveTimelapseIng2) {
        moveTimelapseIng = moveTimelapseIng2;
    }

    public static boolean isHitchCockRecordUI() {
        return isHitchCockUI;
    }

    public static void setIsHitchCockRecordUI(boolean hitchCockUI) {
        isHitchCockUI = hitchCockUI;
    }

    public static boolean isDelayPhotoIng() {
        return delayPhotoIng;
    }

    public static void setDelayPhotoIng(boolean delayPhotoIng2) {
        delayPhotoIng = delayPhotoIng2;
    }

    public static boolean isLongExposureIng() {
        return longExposureIng;
    }

    public static void setLongExposureIng(boolean longExposureIng2) {
        longExposureIng = longExposureIng2;
    }

    public static boolean isCameraFocusing() {
        return cameraFocusing;
    }

    public static void setCameraFocusing(boolean cameraFocusing2) {
        cameraFocusing = cameraFocusing2;
    }

    public static void setHasTakePhotoOrVideo(boolean change) {
        hasTakePhotoOrVideo = change;
    }

    public static boolean getHasTakePhotoOrVideo() {
        return hasTakePhotoOrVideo;
    }

    public static void setCameraPreviewWidth(int with) {
        cameraPreviewWidth = with;
    }

    public static int getCameraPreviewWidth() {
        return cameraPreviewWidth;
    }

    public static void setkXPerScale(float kXPer) {
        kXPerScale = kXPer;
    }

    public static float getkXPerScale() {
        return kXPerScale;
    }

    public static void setkYPerScale(float kYPer) {
        kYPerScale = kYPer;
    }

    public static float getkYPerScale() {
        return kYPerScale;
    }

    public static void setKcfMoveX(float moveX) {
        kcfMoveX = moveX;
    }

    public static float getKcfMoveX() {
        return kcfMoveX;
    }

    public static void setKcfMoveY(float moveY) {
        kcfMoveY = moveY;
    }

    public static float getKcfMoveY() {
        return kcfMoveY;
    }

    public static void setPhotoNums(int numsP) {
        photoNums = numsP;
    }

    public static int getPhotoNums() {
        return photoNums;
    }

    public static void setVideoNums(int numsV) {
        videoNums = numsV;
    }

    public static int getVideoNums() {
        return videoNums;
    }

    public static void setPhotoSelectNums(int numsSelectP) {
        photoSelectNums = numsSelectP;
    }

    public static int getPhotoSelectNums() {
        return photoSelectNums;
    }

    public static void setVideoSelectNums(int numsSelectV) {
        videoSelectNums = numsSelectV;
    }

    public static int getVideoSelectNums() {
        return videoSelectNums;
    }

    public static void setScaleScrollViewMFMaxNums(double scaleScrollViewM) {
        scaleScrollViewMF = scaleScrollViewM;
    }

    public static double getScaleScrollViewMFMaxNums() {
        return scaleScrollViewMF;
    }

    public static boolean getScaleScrollTouchState() {
        return scaleTouchState;
    }

    public static void setScaleScrollTouchState(boolean scaleTouchS) {
        scaleTouchState = scaleTouchS;
    }

    public static boolean getScaleScrollTouchStateEV() {
        return scaleTouchStateEV;
    }

    public static void setScaleScrollTouchStateEV(boolean scaleTouchE) {
        scaleTouchStateEV = scaleTouchE;
    }

    public static boolean getScaleScrollTouchStateShutter() {
        return scaleTouchStateShutter;
    }

    public static void setScaleScrollTouchStateShutter(boolean scaleTouchShutter) {
        scaleTouchStateShutter = scaleTouchShutter;
    }

    public static boolean getScaleScrollTouchStateISO() {
        return scaleTouchStateISO;
    }

    public static void setScaleScrollTouchStateISO(boolean scaleTouchIS) {
        scaleTouchStateISO = scaleTouchIS;
    }

    public static boolean getScaleScrollTouchStateWB() {
        return scaleTouchStateWB;
    }

    public static void setScaleScrollTouchStateWB(boolean scaleTouchW) {
        scaleTouchStateWB = scaleTouchW;
    }

    public static boolean getScaleScrollTouchStateMF() {
        return scaleTouchStateMF;
    }

    public static void setScaleScrollTouchStateMF(boolean scaleTouchM) {
        scaleTouchStateMF = scaleTouchM;
    }

    public static boolean getScaleScrollTouchStateWT() {
        return scaleTouchStateWT;
    }

    public static void setScaleScrollTouchStateWT(boolean scaleTouchWt) {
        scaleTouchStateWT = scaleTouchWt;
    }

    public static boolean getCameraHandModel() {
        return cameraHandModel;
    }

    public static void setCameraHandModel(boolean caHandModel) {
        cameraHandModel = caHandModel;
    }

    public static void setScaleScrollViewWBMaxNums(double scaleScrollViewW) {
        scaleScrollViewWB = scaleScrollViewW;
    }

    public static double getScaleScrollViewWBMaxNums() {
        return scaleScrollViewWB;
    }

    public static void setScaleScrollViewEVMaxNums(double scaleScrollViewE) {
        scaleScrollViewEV = scaleScrollViewE;
    }

    public static double getScaleScrollViewEVMaxNums() {
        return scaleScrollViewEV;
    }

    public static void setScaleScrollViewWTMaxNums(double scaleScrollViewW) {
        scaleScrollViewWT = scaleScrollViewW;
    }

    public static double getScaleScrollViewWTMaxNums() {
        return scaleScrollViewWT;
    }

    public static void setScaleScrollViewTradWTMaxNums(double scaleScrollViewTradW) {
        scaleScrollViewTradWT = scaleScrollViewTradW;
    }

    public static double getScaleScrollViewTradWTMaxNums() {
        return scaleScrollViewTradWT;
    }

    public static void setScaleScrollShutterAuto(String scaleScrollShutterAu) {
        scaleScrollShutterAuto = scaleScrollShutterAu;
    }

    public static boolean getCameraHandModelSmallImage() {
        return cameraHandModelSmallImage;
    }

    public static void setCameraHandModelSmallImage(boolean small_image) {
        cameraHandModelSmallImage = small_image;
    }

    public static String getScaleScrollShutterAuto() {
        return scaleScrollShutterAuto;
    }

    public static void setCameraDeviceSum(int device_Sum) {
        cameraDeviceSum = device_Sum;
    }

    public static int getCameraDeviceSum() {
        return cameraDeviceSum;
    }

    public static void setDisplayDensity(float display) {
        displayDen = display;
    }

    public static float getDisplayDensity() {
        return displayDen;
    }

    public static void setFullCameraErrorCode(String fullCameraErrorCo) {
        fullCameraErrorCode = fullCameraErrorCo;
    }

    public static String getFullCameraErrorCode() {
        return fullCameraErrorCode;
    }

    public static void setGestureDownMotionEvent(MotionEvent mEvent) {
        gestureDownMotionEvent = mEvent;
    }

    public static MotionEvent getGestureDownMotionEvent() {
        return gestureDownMotionEvent;
    }

    public static void setOnDownMotionEvent(MotionEvent motionEvent) {
        onDownMotionEvent = motionEvent;
    }

    public static MotionEvent getOnDownMotionEvent() {
        return onDownMotionEvent;
    }

    public static void setOnDownOneFingerMotionEvent(Point xy) {
        onDownOneFingerMotionEvent = xy;
    }

    public static Point getOnDownOneFingerMotionEvent() {
        return onDownOneFingerMotionEvent;
    }

    public static void setOnDownMotionEventMarkAAA(Point mEventXaaa) {
        onDownMotionEventMarkAAA = mEventXaaa;
    }

    public static Point getOnDownMotionEventMarkAAA() {
        return onDownMotionEventMarkAAA;
    }

    public static void setOnDownMotionEventMarkBBB(Point mEventXbbb) {
        onDownMotionEventMarkBBB = mEventXbbb;
    }

    public static Point getOnDownMotionEventMarkBBB() {
        return onDownMotionEventMarkBBB;
    }

    public static void setScaleIsoList(List<String> listIso) {
        listScaleIso = listIso;
    }

    public static List<String> getScaleIsoList() {
        return listScaleIso;
    }

    public static void setScaleIsoRange(Range<Integer> rangeIso) {
        rangeScaleIso = rangeIso;
    }

    public static Range<Integer> getScaleIsoRange() {
        return rangeScaleIso;
    }

    public static void setScaleShutterRange(Range<Long> shutterIso) {
        rangeScaleShutter = shutterIso;
    }

    public static Range<Long> getScaleShutterRange() {
        return rangeScaleShutter;
    }

    public static void setHitchCockStartPlaceWT(float startPlaceWT) {
        hitchCockStartPlaceWT = startPlaceWT;
    }

    public static float getHitchCockStartPlaceWT() {
        return hitchCockStartPlaceWT;
    }

    public static void setHitchCockStartPlaceMF(double startPlaceMF) {
        hitchCockStartPlaceMF = startPlaceMF;
    }

    public static double getHitchCockStartPlaceMF() {
        return hitchCockStartPlaceMF;
    }

    public static void setHitchCockEndPlaceWT(float endPlaceWT) {
        hitchCockEndPlaceWT = endPlaceWT;
    }

    public static float getHitchCockEndPlaceWT() {
        return hitchCockEndPlaceWT;
    }

    public static void setHitchCockEndPlaceMF(double endPlaceMF) {
        hitchCockEndPlaceMF = endPlaceMF;
    }

    public static double getHitchCockEndPlaceMF() {
        return hitchCockEndPlaceMF;
    }

    public static void setHitchCockEndPlacePageAppears(int endPlacePageAppears) {
        hitchCockEndPlacePageAppears = endPlacePageAppears;
    }

    public static int getHitchCockEndPlacePageAppears() {
        return hitchCockEndPlacePageAppears;
    }

    public static void setHitchCockVideoTime(int time) {
        hitchCockVideoTime = time;
    }

    public static int getHitchCockVideoTime() {
        return hitchCockVideoTime;
    }

    public static void setBlueConnectBoolean300(boolean blueConnect300) {
        blueConnectBooleanS = blueConnect300;
    }

    public static boolean getBlueConnectBoolean300() {
        return blueConnectBooleanS;
    }

    public static void setHitchCockHorizontalScaleMF(boolean scaleMF) {
        hitchCockHoriScaleMF = scaleMF;
    }

    public static boolean getHitchCockHorizontalScaleMF() {
        return hitchCockHoriScaleMF;
    }

    public static void setHitchCockStartPlaceStringMF(String startPlaceStrMF) {
        hitchCockStartPlaceStringMF = startPlaceStrMF;
    }

    public static String getHitchCockStartPlaceStringMF() {
        return hitchCockStartPlaceStringMF;
    }

    public static void setHitchCockEndPlaceStringMF(String endPlaceStrMF) {
        hitchCockEndPlaceStringMF = endPlaceStrMF;
    }

    public static String getHitchCockEndPlaceStringMF() {
        return hitchCockEndPlaceStringMF;
    }

    public static void setPhoneAngle(int angle) {
        phoneAngle = angle;
    }

    public static int getPhoneAngle() {
        return phoneAngle;
    }

    public static void setLabelTopBarSelect(int check) {
        labelTopBarSelect = check;
    }

    public static int getLabelTopBarSelect() {
        return labelTopBarSelect;
    }

    public static void setFrameLayerNumber(int number) {
        frameLayerNumber = number;
    }

    public static int getFrameLayerNumber() {
        return frameLayerNumber;
    }

    public static void setLlRootBackgroundColor(boolean backGrColor) {
        llRootBackgroundColor = backGrColor;
    }

    public static boolean getLlRootBackgroundColor() {
        return llRootBackgroundColor;
    }

    public static void setLlMarkPointMfA(float pointA) {
        llMarkPointMfA = pointA;
    }

    public static float getLlMarkPointMfA() {
        return llMarkPointMfA;
    }

    public static void setLlMarkPointMfB(float pointB) {
        llMarkPointMfB = pointB;
    }

    public static float getLlMarkPointMfB() {
        return llMarkPointMfB;
    }

    public static void setLlMarkPointWtA(float pointA) {
        llMarkPointWtA = pointA;
    }

    public static float getLlMarkPointWtA() {
        return llMarkPointWtA;
    }

    public static void setLlMarkPointWtB(float pointB) {
        llMarkPointWtB = pointB;
    }

    public static float getLlMarkPointWtB() {
        return llMarkPointWtB;
    }

    public static void setMarkPointWtIsFirst(boolean isFirst) {
        markPointWtIsFirst = isFirst;
    }

    public static boolean getMarkPointWtIsFirst() {
        return markPointWtIsFirst;
    }

    public static void setMarkPointMfIsFirst(boolean isFirst) {
        markPointMfIsFirst = isFirst;
    }

    public static boolean getMarkPointMfIsFirst() {
        return markPointMfIsFirst;
    }

    public static boolean getFvAdvancedSettingActivityIsShow() {
        return fvAdvancedSettingActivityIsShow;
    }

    public static void setFvAdvancedSettingActivityIsShow(boolean isShow) {
        fvAdvancedSettingActivityIsShow = isShow;
    }

    public static boolean getBtnBackBgColorIsYellow() {
        return btnBackBgColorIsYellow;
    }

    public static void setBtnBackBgColorIsYellow(boolean isBackYellow) {
        btnBackBgColorIsYellow = isBackYellow;
    }

    public static boolean getBtnCameraBgColorIsYellow() {
        return btnCameraBgColorIsYellow;
    }

    public static void setBtnCameraBgColorIsYellow(boolean isCameraYellow) {
        btnCameraBgColorIsYellow = isCameraYellow;
    }

    public static boolean getBtnViltaBgColorIsYellow() {
        return btnViltaBgColorIsYellow;
    }

    public static void setBtnViltaBgColorIsYellow(boolean isViltaYellow) {
        btnViltaBgColorIsYellow = isViltaYellow;
    }

    public static boolean getBtnSettingBgColorIsYellow() {
        return btnSettingBgColorIsYellow;
    }

    public static void setBtnSettingBgColorIsYellow(boolean isSettingYellow) {
        btnSettingBgColorIsYellow = isSettingYellow;
    }

    public static int getBtnCameraStatus() {
        return btnCameraStatus;
    }

    public static void setBtnCameraStatus(int cameraStatus) {
        btnCameraStatus = cameraStatus;
    }

    public static int getBtnViltaStatus() {
        return btnViltaStatus;
    }

    public static void setBtnViltaStatus(int viltaStatus) {
        btnViltaStatus = viltaStatus;
    }

    public static boolean getCameraHandModelBgColorIsYellow() {
        return cameraHandModelBgColorIsYellow;
    }

    public static void setCameraHandModelBgColorIsYellow(boolean colorIsYellow) {
        cameraHandModelBgColorIsYellow = colorIsYellow;
    }

    public static float getZoomValue() {
        return zoomValue;
    }

    public static void setZoomValue(float value) {
        zoomValue = value;
    }

    public static int getCamWtProRealTimeValue() {
        return camWtProRealTimeValue;
    }

    public static void setCamWtProRealTimeValue(int realTimeValue) {
        camWtProRealTimeValue = realTimeValue;
    }

    public static boolean getMenuKeyIsLongPress() {
        return menuKeyIsLongPress;
    }

    public static void setMenuKeyIsLongPress(boolean isLongPress) {
        menuKeyIsLongPress = isLongPress;
    }

    public static void setLabelTopBarSelectMemory(int chMemory) {
        labelTopBarSelectMemory = chMemory;
    }

    public static int getLabelTopBarSelectMemory() {
        return labelTopBarSelectMemory;
    }

    public static void setIsBooleanTimeLapseUIShow(boolean isUIShow) {
        booleanTimeLapseUIShow = isUIShow;
    }

    public static boolean getIsBooleanTimeLapseUIShow() {
        return booleanTimeLapseUIShow;
    }

    public static void setMarkPointUIIsVisible(boolean isVisible) {
        markPointUIIsVisible = isVisible;
    }

    public static boolean getMarkPointUIIsVisible() {
        return markPointUIIsVisible;
    }

    public static void setTopBarStatusHaveAnimation(boolean haveAnimation) {
        topBarStatusHaveAnimation = haveAnimation;
    }

    public static boolean getTopBarStatusHaveAnimation() {
        return topBarStatusHaveAnimation;
    }

    public static void setThumbWheelSelectionWTOrMF(int wt) {
        thumbWheelSelectionWTOrMF = wt;
    }

    public static int getThumbWheelSelectionWTOrMF() {
        return thumbWheelSelectionWTOrMF;
    }

    public static void setCurrentTimeMillis(long timeMillis) {
        currentTimeMillis = timeMillis;
    }

    public static long getCurrentTimeMillis() {
        return currentTimeMillis;
    }

    public static void setCurrentTimeMillisHandModel(long timeMillisHandModel) {
        currentTimeMillisHandModel = timeMillisHandModel;
    }

    public static long getCurrentTimeMillisHandModel() {
        return currentTimeMillisHandModel;
    }

    public static void setAppMaxZoom(float mZoom) {
        appMaxZoom = mZoom;
    }

    public static float getAppMaxZoom() {
        return appMaxZoom;
    }

    public static void setHandModelVisibleStateValue(int visibleStateValue) {
        handModelVisibleStateValue = visibleStateValue;
    }

    public static float getHandModelVisibleStateValue() {
        return (float) handModelVisibleStateValue;
    }

    public static void setHandModelScaleValueMF(int valueMF) {
        handModelScaleValueMF = valueMF;
    }

    public static float getHandModelScaleValueMF() {
        return (float) handModelScaleValueMF;
    }

    public static void setHandModelScaleValueWT(int valueWT) {
        handModelScaleValueWT = valueWT;
    }

    public static float getHandModelScaleValueWT() {
        return (float) handModelScaleValueWT;
    }

    public static void setScaleMargin(int scaleMargin) {
        mScaleMargin = scaleMargin;
    }

    public static int getScaleMargin() {
        return mScaleMargin;
    }

    public static void setMidCountScale(int midCountScale) {
        mMidCountScale = midCountScale;
    }

    public static int getMidCountScale() {
        return mMidCountScale;
    }

    public static void setRectHeight(int rectHeight) {
        mRectHeight = rectHeight;
    }

    public static int getRectHeight() {
        return mRectHeight;
    }

    public static void setScaleMaxHeight(int scaleMaxHeight) {
        mScaleMaxHeight = scaleMaxHeight;
    }

    public static int getScaleMaxHeight() {
        return mScaleMaxHeight;
    }

    public static void setScaleHeight(int scaleHeight) {
        mScaleHeight = scaleHeight;
    }

    public static int getScaleHeight() {
        return mScaleHeight;
    }

    public static void setTimeLapseDuration(int duration) {
        mTimeLapseDuration = duration;
    }

    public static int getTimeLapseDuration() {
        return mTimeLapseDuration;
    }

    public static void setApkIsFromNormal(boolean isNormal) {
        apkIsFromNormal = isNormal;
        apkIsFromGoogle = false;
    }

    public static boolean getApkIsFromNormal() {
        return apkIsFromNormal;
    }

    public static void setApkIsFromGoogle(boolean isGoogle) {
        apkIsFromGoogle = isGoogle;
        apkIsFromNormal = false;
    }

    public static boolean getApkIsFromGoogle() {
        return apkIsFromGoogle;
    }

    public static boolean getMarkPointKeyCanUse(Context mContext) {
        boolean interceptor = ((Boolean) SPUtils.get(mContext, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, false)).booleanValue();
        int cameraHandModel2 = ((Integer) SPUtils.get(mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_OPEN))).intValue();
        if (getFrameLayerNumber() == 45) {
            interceptor = false;
            Util.sendIntEventMessge(Constants.LABEL_CAMERA_POP_DISMISS_KEY_210);
        }
        if (isFollowIng()) {
            EventBusUtil.sendEvent(new Event(132));
        }
        if (interceptor || MoveTimelapseUtil.getInstance().getCameraProgressLinear() == 1 || MoveTimelapseUtil.getInstance().getCameraProgressLinear() == 2 || MoveTimelapseUtil.getInstance().getCameraProgressLinear() == 3 || MoveTimelapseUtil.getInstance().getIsHitchCockRecord() == 1 || isFollowIng() || getFrameLayerNumber() != 0 || isFullShotIng() || cameraHandModel2 == 107211 || getIsBooleanTimeLapseUIShow()) {
            return false;
        }
        return true;
    }

    public static List<String> setScaleIsoListToPhone(int value) {
        List<String> listIsoValue = new ArrayList<>();
        for (int i = 0; i < listScaleIso.size(); i++) {
            if (value < Integer.valueOf(listScaleIso.get(i)).intValue()) {
                listIsoValue.add(listScaleIso.get(i).toString());
            }
        }
        listIsoValue.add(0, String.valueOf(value));
        return listIsoValue;
    }

    public static void controlTriaxial(int index) {
        byte data = 0;
        switch (index) {
            case 0:
                data = 0;
                break;
            case 1:
                data = 1;
                break;
            case 2:
                data = 2;
                break;
            case 3:
                data = 3;
                break;
            case 4:
                data = 4;
                break;
        }
        BleByteUtil.setPTZParameters((byte) 86, data);
    }

    public static String getRecordSizeText(int num) {
        switch (num) {
            case 2:
                return "176P";
            case 3:
                return "352P";
            case 4:
                return "480P";
            case 5:
                return "720P";
            case 6:
                return "1080P";
            case 8:
                return "2160P";
            default:
                return "1080P";
        }
    }

    public static String getRecordSizeText(Size size) {
        String str = size.getWidth() + " X " + size.getHeight();
        String size2 = size.toString();
        return size.getWidth() + "x" + size.getHeight();
    }

    public static Size getRecordStringToSize(String str) {
        String stri = str;
        return new Size(Integer.valueOf(stri.substring(0, stri.indexOf("x"))).intValue(), Integer.valueOf(stri.substring(stri.indexOf("x") + 1, stri.length())).intValue());
    }

    public static int getQulitySize(int i) {
        switch (i) {
            case 4:
                return 1004;
            case 5:
                return 1005;
            case 6:
                return 1006;
            case 8:
                return 1008;
            default:
                return 1005;
        }
    }

    public static int recordTextToValue(String recordSize) {
        return Integer.valueOf(recordSize.replace("P", "")).intValue();
    }

    public static int getRecordSizeLevel(int num) {
        switch (num) {
            case CompanyIdentifierResolver.PASSIF_SEMICONDUCTOR_CORP:
                return 2;
            case 352:
                return 3;
            case 480:
                return 4;
            case 720:
                return 5;
            case 1080:
                return 6;
            case 2160:
                return 8;
            default:
                return 6;
        }
    }

    public static long readSDCardSizeNumber(Context context) {
        String path;
        int qulity;
        if (!"mounted".equals(Environment.getExternalStorageState())) {
            return 0;
        }
        if (!((String) SPUtils.get(context, SharePrefConstant.SAVE_STORAGE_SD_CARD_PATH, IntentKey.FILE_PATH)).contains(Constants.FILENAME_SEQUENCE_SEPARATOR)) {
            path = Environment.getExternalStorageDirectory().getPath();
        } else {
            path = Util.getParentPath(context);
        }
        StatFs statFs = new StatFs(path);
        long blockSize = (long) statFs.getBlockSize();
        long blockCount = (long) statFs.getBlockCount();
        long sdCardSizeCount = ((((long) statFs.getAvailableBlocks()) * blockSize) / 1024) / 1024;
        if (((Integer) SPUtils.get(context, SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE))).intValue() == 10101) {
            qulity = getCheckMediaRecordFrontSize();
        } else {
            qulity = getCheckMediaRecordSize();
        }
        double sdCardSizeCountEnd = (double) (sdCardSizeCount / 5);
        switch (qulity) {
            case 4:
                sdCardSizeCountEnd = ((double) sdCardSizeCount) / 0.7d;
                break;
            case 5:
                sdCardSizeCountEnd = ((double) sdCardSizeCount) / 1.7d;
                break;
            case 6:
                sdCardSizeCountEnd = ((double) sdCardSizeCount) / 2.5d;
                break;
            case 8:
                sdCardSizeCountEnd = (double) (sdCardSizeCount / 5);
                break;
        }
        return Long.valueOf((long) ((int) sdCardSizeCountEnd)).longValue();
    }

    public static String strSubTwoLength(String str) {
        String mfStr = String.valueOf(str);
        if (mfStr.length() <= 3) {
            return mfStr + "0";
        }
        if (mfStr.substring(0, mfStr.indexOf(".")).length() > 1) {
            if (mfStr.length() > 4) {
                return mfStr.substring(0, 5);
            }
            return mfStr.substring(0, 4) + "0";
        } else if (mfStr.length() > 3) {
            return mfStr.substring(0, 4);
        } else {
            return mfStr.substring(0, 3) + "0";
        }
    }

    public static boolean utilsIsMIUI() {
        if (Camera2Constants.Exclude_List_EnablePreviewFrameSurfaceView.equalsIgnoreCase(Build.BRAND.toLowerCase())) {
            return true;
        }
        return false;
    }

    public static void btnStatusRestart() {
        topBarBtnStatusRestartBlack();
        setLabelTopBarSelect(-1);
        setBtnCameraStatus(0);
        setBtnViltaStatus(0);
    }

    public static void topBarBtnStatusRestartBlack() {
        setBtnSettingBgColorIsYellow(false);
        setBtnViltaBgColorIsYellow(false);
        setBtnCameraBgColorIsYellow(false);
        setBtnBackBgColorIsYellow(false);
    }

    private void initStatusSetting(Context mContext) {
        SPUtils.put(mContext, SharePrefConstant.DELAY_TAKE_PHOTO_MODE, Integer.valueOf(Constants.DELAY_TAKE_PHOTO_0S));
        SPUtils.put(mContext, SharePrefConstant.FULL_SHOT, Integer.valueOf(Constants.FULL_SHOT_NONE));
        SPUtils.put(mContext, SharePrefConstant.LONG_EXPOSURE_MODE, Integer.valueOf(Constants.LONG_EXPOSURE_NONE));
        SPUtils.put(mContext, SharePrefConstant.FILTER_MODE, Integer.valueOf(Constants.FILTER_NONE_MODE));
        SPUtils.put(mContext, SharePrefConstant.BEAUTY_MODE, Integer.valueOf(Constants.BEAUTY_CLOSE));
        SPUtils.put(mContext, SharePrefConstant.BEAUTY_VALUE, 50);
        SPUtils.put(mContext, SharePrefConstant.HDR_MODE, Integer.valueOf(Constants.HDR_CLOSE));
        SPUtils.put(mContext, SharePrefConstant.WB_MODE, Integer.valueOf(Constants.WB_AUTO));
        SPUtils.put(mContext, SharePrefConstant.CAMERA_MODE, 10001);
        SPUtils.put(mContext, SharePrefConstant.FLASH_MODE, 10003);
        SPUtils.put(mContext, SharePrefConstant.CAMERA_LENS_MODE, Integer.valueOf(Constants.LENS_BLACK_MODE));
        SPUtils.put(mContext, SharePrefConstant.POP_SHOW_OUTSIDE_CLICK_INTERCEPTOR, false);
        SPUtils.put(mContext, SharePrefConstant.CAMERA_FOCUS_LOCK_OR_MOVE, Integer.valueOf(Constants.CAMERA_FOCUS_MOVE));
        SPUtils.put(mContext, SharePrefConstant.SET_CAMERA_MOVE_LAPSE_HIGH_OR_LOW, Integer.valueOf(Constants.SET_CAMERA_MOVE_LAPSE_HIGH));
        SPUtils.put(mContext, SharePrefConstant.LABEL_CAMERA_HAND_MODEL, Integer.valueOf(Constants.LABEL_CAMERA_HAND_MODEL_CLOSE));
    }

    public static boolean hasNotchScreen(Activity activity) {
        if (getInt("ro.miui.notch", activity) == 1 || hasNotchAtHuawei(activity) || hasNotchAtOPPO(activity) || hasNotchAtVivo(activity)) {
            return true;
        }
        return false;
    }

    public static int getInt(String key, Activity activity) {
        if (!isXiaomi()) {
            return 0;
        }
        try {
            Class SystemProperties = activity.getClassLoader().loadClass("android.os.SystemProperties");
            return ((Integer) SystemProperties.getMethod("getInt", new Class[]{String.class, Integer.TYPE}).invoke(SystemProperties, new Object[]{new String(key), new Integer(0)})).intValue();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return 0;
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
            return 0;
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
            return 0;
        } catch (IllegalArgumentException e4) {
            e4.printStackTrace();
            return 0;
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
            return 0;
        }
    }

    public static boolean hasNotchAtHuawei(Context context) {
        try {
            Class HwNotchSizeUtil = context.getClassLoader().loadClass("com.huawei.android.util.HwNotchSizeUtil");
            return ((Boolean) HwNotchSizeUtil.getMethod("hasNotchInScreen", new Class[0]).invoke(HwNotchSizeUtil, new Object[0])).booleanValue();
        } catch (ClassNotFoundException e) {
            return false;
        } catch (NoSuchMethodException e2) {
            return false;
        } catch (Exception e3) {
            return false;
        } catch (Throwable th) {
            return false;
        }
    }

    public static boolean hasNotchAtVivo(Context context) {
        try {
            Class FtFeature = context.getClassLoader().loadClass("android.util.FtFeature");
            return ((Boolean) FtFeature.getMethod("isFeatureSupport", new Class[]{Integer.TYPE}).invoke(FtFeature, new Object[]{32})).booleanValue();
        } catch (ClassNotFoundException e) {
            return false;
        } catch (NoSuchMethodException e2) {
            return false;
        } catch (Exception e3) {
            return false;
        } catch (Throwable th) {
            return false;
        }
    }

    public static boolean hasNotchAtOPPO(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    public static boolean isXiaomi() {
        return "Xiaomi".equals(Build.MANUFACTURER);
    }
}
