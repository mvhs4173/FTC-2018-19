package org.firstinspires.ftc.teamcode;
import android.app.Application;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraCharacteristics;

import org.firstinspires.ftc.robotcore.internal.vuforia.externalprovider.VuforiaWebcam;
import org.firstinspires.ftc.teamcode.HardwarePushbot;


import android.content.Context;
import android.app.Activity;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.vuforia.Vuforia;

import org.opencv.*;


public class Vision {
    CameraManager cameraManager;
    String cameraId;
    CameraDevice.StateCallback callback;

    //Gets the id of the back camera on the phone
    private String getBackCameraId(){
        String[] cameraList = {};

        //Requires a try catch loop to handle errors otherwise the function call will throw an error
        try {
           cameraList = cameraManager.getCameraIdList();
        } catch (CameraAccessException e){
            e.printStackTrace();
        }

        //Loop through the list of camera ids
        for (String cameraId : cameraList) {
            CameraCharacteristics camCharacteristics = null;

            try {
                camCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

            int camOrientation = camCharacteristics.get(CameraCharacteristics.LENS_FACING);
            if (camOrientation == CameraCharacteristics.LENS_FACING_BACK) {
                return cameraId;
            }
        }

        return null;
    }

    //Gets the id of the front camera on the phone
    private String getFrontCameraId(){
        String[] cameraList = {};

        //Requires a try catch loop to handle errors otherwise the function call will throw an error
        try {
            cameraList = cameraManager.getCameraIdList();
        } catch (CameraAccessException e){
            e.printStackTrace();
        }

        //Loop through the list of camera ids
        for (String cameraId : cameraList) {
            CameraCharacteristics camCharacteristics = null;

            try {
                camCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

            int camOrientation = camCharacteristics.get(CameraCharacteristics.LENS_FACING);
            if (camOrientation == CameraCharacteristics.LENS_FACING_FRONT) {
                return cameraId;
            }
        }

        return null;
    }

    public Vision(HardwareMap hwMap) {
        //Get the camera manager
        cameraManager = (CameraManager)hwMap.appContext.getSystemService(Context.CAMERA_SERVICE);
        cameraId = getBackCameraId();//Get the id of the camera we will be using

    }
}
