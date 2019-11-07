package com.zonar.zonarapp.utils;

import android.content.Context;

//import com.xround_app_sdk.Controller;
import com.xround_lib.Controller;

public class ZonarUtils {

    private static final boolean IS_SIMULATE = false;

    private static Context context;
    private static Controller controller;

    private static double[] sZonarEQ;
    private static int sNumero;
    private static int sMode;

    private static boolean flagWriting = false;

    public static void init(Context ctx) {
        context = ctx;
        if (!IS_SIMULATE) {
            controller = new Controller(context);
        }
    }

    public static double[] getEQData(boolean isWrite, int numero, int mode) {
        double[] eq_double = !IS_SIMULATE ? EQ_CTRLtable(numero) : simulate_EQ_CTRLtable(numero);

        if (isWrite && !IS_SIMULATE) {
            write_ZonarEQ(eq_double, numero, mode);
        }

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

    private static double[] EQ_CTRLtable(int numero) {
        return controller.EQ_table(numero);
    }

    private synchronized static void write_ZonarEQ(final double[] ZonarEQ, final int numero, final int mode) {
        sZonarEQ = ZonarEQ;
        sNumero = numero;
        sMode = mode;

        if (flagWriting) {
            return;
        }
        flagWriting = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    controller.write_ZonarEQ(ZonarEQ, numero, mode);
                    if (sNumero != numero || sMode != mode) {
                        controller.write_ZonarEQ(sZonarEQ, sNumero, sMode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                flagWriting = false;
            }
        }).start();
    }

    public static double[] simulate_EQ_CTRLtable(int numero) {
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

        return EQ;
    }

}
