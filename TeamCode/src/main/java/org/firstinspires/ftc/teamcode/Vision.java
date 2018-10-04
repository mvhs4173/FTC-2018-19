package org.firstinspires.ftc.teamcode;
import android.app.Application;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraCharacteristics;


import android.content.Context;
import android.app.Activity;

import org.opencv.*;


public class Vision {
    CameraManager cameraManager;
    Context context;
    /*private String getFrontFacingCameraId(){
        for(String cameraId : cameraManager.getCameraIdList()){
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            int cOrientation = characteristics.get(CameraCharacteristics.LENS_FACING);
            if(cOrientation == CameraCharacteristics.LENS_FACING_FRONT) return cameraId;
        }
        return null;
    }*/

    public Vision() {

    }
}
