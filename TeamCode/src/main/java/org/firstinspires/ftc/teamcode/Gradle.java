package org.firstinspires.ftc.teamcode;

@SuppressWarnings("unused")
public class Gradle {
    private final Object flatDir;

    /**
     * build.common.gradle
     * <p>
     * Try to avoid editing this file, as it may be updated from time to time as the FTC SDK
     * evolves. Rather, if it is necessary to customize the build process, do those edits in
     * the build.gradle file in TeamCode.
     * <p>
     * This file contains the necessary content of the 'build.gradle' files for robot controller
     * applications built using the FTC SDK. Each individual 'build.gradle' in those applications
     * can simply contain the one line:
     * <p>
     * apply from: '../build.common.gradle'
     * <p>
     * which will pick up this file here. This approach allows makes it easier to integrate
     * updates to the FTC SDK into your code.
     * @param signingConfigs /
     * @param sourceSets /
     * @param sourceSets1 /
     * @param jni /
     * @param flatDir /
     */

    Gradle(Object signingConfigs, Object sourceSets, Object sourceSets1, Object jni, Object flatDir) {
        this.jni = jni;
        this.flatDir = flatDir;
        debug = null;
        storeFile = null;
    }

    private final Object debug;

    private final Object jni;

    Gradle() {
        storeFile = null;
        jni = null;
        debug = null;
        flatDir = null;
    }

    private Object release;

    private final Object storeFile;

    private Object storePassword;

    private Object keyAlias;

    private Object keyPassword;
}