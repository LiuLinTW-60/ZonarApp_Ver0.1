package com.zonar.zonarapp.utils;

import com.xround_app_sdk.Controller;
import com.zonar.zonarapp.ZaApplication;

public class ZonarUtils {

    private static Controller controller;

    public static double[] EQ_CTRLtable(boolean isWrite, int numero, int mode) {
        try {
            if (isWrite) {
                new Controller(ZaApplication.getGlobalContext()).EQ_CTRLtable(numero, mode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        double[] EQ;
        switch (numero) {
            case 1:
                EQ = new double[]{6.0D, 3.0D, 2.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.5D, 1.5D, 0.0D};
                break;
            case 2:
                EQ = new double[]{4.5D, 3.75D, 3.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.5D, 1.5D, 3.75D};
                break;
            case 3:
                EQ = new double[]{3.0D, 4.5D, 4.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.5D, 1.5D, 4.5D};
                break;
            case 4:
                EQ = new double[]{1.5D, 5.25D, 5.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.5D, 1.5D, 3.0D};
                break;
            case 5:
                EQ = new double[]{1.5D, 6.0D, 6.0D, 3.0D, 2.0D, 0.0D, 0.0D, 1.5D, 1.5D, 1.125D};
                break;
            case 6:
                EQ = new double[]{1.125D, 4.5D, 4.5D, 3.75D, 3.0D, 0.0D, 0.0D, 1.125D, 1.125D, -3.0D};
                break;
            case 7:
                EQ = new double[]{0.75D, 3.0D, 3.0D, 4.5D, 4.0D, 0.0D, 0.0D, 0.75D, 0.75D, -4.5D};
                break;
            case 8:
                EQ = new double[]{0.375D, 1.5D, 1.5D, 5.25D, 5.0D, 0.0D, 0.0D, 0.375D, 0.375D, 3.0D};
                break;
            case 9:
                EQ = new double[]{-1.5D, 1.5D, 1.5D, 6.0D, 6.0D, 3.0D, 2.0D, 0.375D, 0.375D, -2.5D};
                break;
            case 10:
                EQ = new double[]{-3.0D, 1.125D, 1.125D, 4.5D, 4.5D, 3.75D, 3.0D, 0.75D, 0.75D, 6.0D};
                break;
            case 11:
                EQ = new double[]{-4.5D, 0.75D, 0.75D, 3.0D, 3.0D, 4.5D, 4.0D, 1.125D, 1.125D, -4.5D};
                break;
            case 12:
                EQ = new double[]{-6.0D, 0.375D, 0.375D, 1.5D, 1.5D, 5.25D, 5.0D, 1.5D, 1.5D, -1.5D};
                break;
            case 13:
                EQ = new double[]{-6.0D, -1.5D, -1.5D, 1.5D, 1.5D, 6.0D, 6.0D, 3.0D, 2.0D, -6.0D};
                break;
            case 14:
                EQ = new double[]{-4.5D, -3.0D, -3.0D, 1.125D, 1.125D, 4.5D, 4.5D, 3.75D, 3.0D, 0.0D};
                break;
            case 15:
                EQ = new double[]{-3.0D, -4.5D, -4.5D, 0.75D, 0.75D, 3.0D, 3.0D, 4.5D, 4.0D, -5.25D};
                break;
            case 16:
                EQ = new double[]{-1.5D, -6.0D, -6.0D, 0.375D, 0.375D, 1.5D, 1.5D, 5.25D, 5.0D, 0.375D};
                break;
            case 17:
                EQ = new double[]{1.5D, -6.0D, -6.0D, 0.0D, 0.0D, 1.5D, 1.5D, 6.0D, 6.0D, 5.0D};
                break;
            case 18:
                EQ = new double[]{3.0D, -4.5D, -4.5D, 0.0D, 0.0D, 1.125D, 1.125D, 4.5D, 4.5D, -3.0D};
                break;
            case 19:
                EQ = new double[]{4.5D, -3.0D, -3.0D, 0.0D, 0.0D, 0.75D, 0.75D, 3.0D, 3.0D, -0.75D};
                break;
            case 20:
                EQ = new double[]{6.0D, -1.5D, -1.5D, 0.0D, 0.0D, 0.375D, 0.375D, 1.5D, 1.5D, -6.0D};
                break;
            default:
                EQ = new double[]{0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D};
        }

        int[] zonar_EQ = new int[EQ.length];
        double mode_value;
        if (mode == 0) {
            mode_value = 0.5D;
        } else if (mode == 2) {
            mode_value = 1.5D;
        } else {
            mode_value = 1.0D;
        }

        for (int i = 0; i < EQ.length; ++i) {
            EQ[i] *= mode_value;
            zonar_EQ[i] = (int) Math.round(EQ[i] * 0.0D + 5.0D * mode_value);
        }

        return EQ;

    }

    public static double[] getEQData(boolean isWrite, int numero, int mode) {
        if (controller == null) {
            controller = new Controller(ZaApplication.getGlobalContext());
        }

        double[] eq_double = EQ_CTRLtable(isWrite, numero, mode);
        double max = 0;
        double min = 0;
        for (int i = 0; i < eq_double.length; i++) {
            if (i == 0) {
                max = eq_double[i];
                min = eq_double[i];
            } else {
                if (max < eq_double[i]) {
                    max = eq_double[i];
                }
                if (min > eq_double[i]) {
                    min = eq_double[i];
                }
            }
        }

        double[] eq = new double[eq_double.length];
        for (int i = 0; i < eq_double.length; i++) {
            eq[i] = (eq_double[i] - min) / (max - min) * 10;
        }

        return eq;
    }

//    public static void write_EQ(int numero, int mode) {
//        if (controller == null) {
//            controller = new Controller(ZaApplication.getGlobalContext());
//        }
//
//        try {
//            controller.write_EQ();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
