package com.xround_lib;


import java.util.ArrayList;

public class Analysis {

    int[] f1 = {1000, 400, 700, 1100, 1700, 2500, 3500, 4900, 6600, 8900};
    //int[] f1 = {400, 500, 900, 1400, 2000, 3200, 4400, 6300, 8700, 12000};
    double[] MEAN_DP_SNR_L = new double[f1.length], MEAN_DP_SNR_R = new double[f1.length];
    ArrayList<Double> f1_level_L = new ArrayList<Double>();
    ArrayList<Double> f2_level_L = new ArrayList<Double>();
    ArrayList<Double> f3_level_L = new ArrayList<Double>();
    ArrayList<Double> DP_SNR_L = new ArrayList<Double>();
    ArrayList<Double> f1_level_R = new ArrayList<Double>();
    ArrayList<Double> f2_level_R = new ArrayList<Double>();
    ArrayList<Double> f3_level_R = new ArrayList<Double>();
    ArrayList<Double> DP_SNR_R = new ArrayList<Double>();

    public int[] analysis(ArrayList<Short> total_signal_L, ArrayList<Short> total_signal_R,int samplerate) {
        int fs = samplerate;

        int[] f2 = new int[f1.length], f3 = new int[f1.length], f4 = new int[f1.length];
        for (int f = 0; f < f1.length; f++) {
            f2[f] = (int) (f1[f] * 1.2);
            f3[f] = 2 * f1[f] - f2[f];
            f4[f] = 2 * f2[f] - f1[f];
        }
        int frame_length = 9600;
        double step_ratio = 0.5;
        int step = (int) (step_ratio * frame_length);
        int signal_length = total_signal_L.size() / f1.length;

        int NFFT_1 = 96000;
        int NFFT = 9600;
        double gain = 52;
        int dF = fs / NFFT;
        double dF_1 = (double) fs / NFFT_1;

        //gain mock up
        for (int i = 0; i < f1.length; i++) {
            int noise_points = (int) (0.15 * f1[i] / dF);
            MEAN_DP_SNR_L[i] = 0;
            MEAN_DP_SNR_R[i] = 0;
            float[] signal_L = new float[signal_length], signal_R = new float[signal_length];

            for (int s = 24000; s < 72000; s++) {
                signal_L[s] = total_signal_L.get(i * signal_length + s);
                signal_R[s] = total_signal_R.get(i * signal_length + s);
            }

            double[] spectrum_L = calculateFFT(signal_L, NFFT_1);
            double[] spectrum_R = calculateFFT(signal_R, NFFT_1);

            for (int k = 0; k < NFFT_1 / 2; k++) {
                spectrum_L[k] = 20 * Math.log10(spectrum_L[k]) + gain - (20 * Math.log10(NFFT_1 / 4800));
                spectrum_R[k] = 20 * Math.log10(spectrum_R[k]) + gain - (20 * Math.log10(NFFT_1 / 4800));
            }
            f1_level_L.add(spectrum_L[(int) (f1[i] / dF_1)]);
            f2_level_L.add(spectrum_L[(int) (f2[i] / dF_1)]);
            f3_level_L.add(spectrum_L[(int) (f3[i] / dF_1)]);

            f1_level_R.add(spectrum_R[(int) (f1[i] / dF_1)]);
            f2_level_R.add(spectrum_R[(int) (f2[i] / dF_1)]);
            f3_level_R.add(spectrum_R[(int) (f3[i] / dF_1)]);
            double sumL = 0, sumR = 0;
            for (int k = 0; k < 2 * noise_points + 1; k++) {
                sumL = sumL + spectrum_L[(int) (f3[i] / dF_1) - noise_points + k];
                sumR = sumR + spectrum_R[(int) (f3[i] / dF_1) - noise_points + k];
            }
            DP_SNR_L.add(f3_level_L.get(i) - ((sumL - f3_level_L.get(i)) / (2 * noise_points)));
            DP_SNR_R.add(f3_level_R.get(i) - ((sumR - f3_level_R.get(i)) / (2 * noise_points)));
        }


        int MAXdBcompensation = 10;
        double[] DPOAE_mean_SNR_L = new double[MEAN_DP_SNR_L.length - 2];
        double[] DPOAE_mean_SNR_R = new double[MEAN_DP_SNR_R.length - 2];
        double DPOAE_mean_SNR_MAX_L = MEAN_DP_SNR_L[2], DPOAE_mean_SNR_MAX_R = MEAN_DP_SNR_R[2];

        double[] DPOAE_SNR_L = new double[DP_SNR_L.size() - 2];
        double[] DPOAE_SNR_R = new double[DP_SNR_R.size() - 2];
        double DPOAE_SNR_MAX_L = DP_SNR_L.get(2), DPOAE_SNR_MAX_R = DP_SNR_R.get(2);

        double[] DPOAE_amp_L = new double[f3_level_L.size() - 2];
        double[] DPOAE_amp_R = new double[f3_level_R.size() - 2];
        double DPOAE_amp_MAX_L = f3_level_L.get(2), DPOAE_amp_MAX_R = f3_level_R.get(2);

        double[] f2level_L = new double[f2_level_L.size()];
        double[] f2level_R = new double[f2_level_R.size()];
        double f2level_MAX_L = f2_level_L.get(0) - 65, f2level_MAX_R = f2_level_R.get(0) - 55;

        for (int i = 0; i < f1.length; i++) {
            f2level_L[i] = f2_level_L.get(i) - 65;
            f2level_R[i] = f2_level_R.get(i) - 55;
            f2level_MAX_L = f2level_L[i] > f2level_MAX_L ? f2level_L[i] : f2level_MAX_L;
            f2level_MAX_R = f2level_R[i] > f2level_MAX_R ? f2level_R[i] : f2level_MAX_R;
            if (i < f1.length - 2) {
                DPOAE_mean_SNR_L[i] = MEAN_DP_SNR_L[i + 2];
                DPOAE_mean_SNR_R[i] = MEAN_DP_SNR_R[i + 2];
                DPOAE_mean_SNR_MAX_L = DPOAE_mean_SNR_L[i] > DPOAE_mean_SNR_MAX_L ? DPOAE_mean_SNR_L[i] : DPOAE_mean_SNR_MAX_L;
                DPOAE_mean_SNR_MAX_R = DPOAE_mean_SNR_R[i] > DPOAE_mean_SNR_MAX_R ? DPOAE_mean_SNR_R[i] : DPOAE_mean_SNR_MAX_R;
                DPOAE_SNR_L[i] = DP_SNR_L.get(i + 2);
                DPOAE_SNR_R[i] = DP_SNR_R.get(i + 2);
                DPOAE_SNR_MAX_L = DPOAE_SNR_L[i] > DPOAE_SNR_MAX_L ? DPOAE_SNR_L[i] : DPOAE_SNR_MAX_L;
                DPOAE_SNR_MAX_R = DPOAE_SNR_R[i] > DPOAE_SNR_MAX_R ? DPOAE_SNR_R[i] : DPOAE_SNR_MAX_R;
                DPOAE_amp_L[i] = f3_level_L.get(i + 2);
                DPOAE_amp_R[i] = f3_level_R.get(i + 2);
                DPOAE_amp_MAX_L = DPOAE_amp_L[i] > DPOAE_amp_MAX_L ? DPOAE_amp_L[i] : DPOAE_amp_MAX_L;
                DPOAE_amp_MAX_R = DPOAE_amp_R[i] > DPOAE_amp_MAX_R ? DPOAE_amp_R[i] : DPOAE_amp_MAX_R;
            }
        }

        double[] DPmeanSNRdiff_L = new double[DPOAE_mean_SNR_L.length];
        double[] DPmeanSNRdiff_R = new double[DPOAE_mean_SNR_R.length];
        double DPmeanSNRdiff_MAX_L = 0, DPmeanSNRdiff_MAX_R = 0;

        double[] DPSNRdiff_L = new double[DPOAE_SNR_L.length];
        double[] DPSNRdiff_R = new double[DPOAE_SNR_R.length];
        double DPSNRdiff_MAX_L = 0, DPSNRdiff_MAX_R = 0;

        double[] DPampdiff_L = new double[DPOAE_amp_L.length];
        double[] DPampdiff_R = new double[DPOAE_amp_R.length];
        double DPampdiff_MAX_L = 0, DPampdiff_MAX_R = 0;

        double[] f2diff_L = new double[f2level_L.length];
        double[] f2diff_R = new double[f2level_R.length];
        double f2diff_MAX_L = 0, f2diff_MAX_R = 0;

        for (int i = 0; i < f1.length; i++) {
            f2diff_L[i] = f2level_MAX_L - f2level_L[i];
            f2diff_R[i] = f2level_MAX_R - f2level_R[i];
            f2diff_MAX_L = Math.abs(f2diff_L[i]) > f2diff_MAX_L ? Math.abs(f2diff_L[i]) : f2diff_MAX_L;
            f2diff_MAX_R = Math.abs(f2diff_R[i]) > f2diff_MAX_R ? Math.abs(f2diff_R[i]) : f2diff_MAX_R;
            if (i < f1.length - 2) {
                DPmeanSNRdiff_L[i] = DPOAE_mean_SNR_MAX_L - DPOAE_mean_SNR_L[i];
                DPmeanSNRdiff_R[i] = DPOAE_mean_SNR_MAX_R - DPOAE_mean_SNR_R[i];
                DPmeanSNRdiff_MAX_L = Math.abs(DPmeanSNRdiff_L[i]) > DPmeanSNRdiff_MAX_L ? Math.abs(DPmeanSNRdiff_L[i]) : DPmeanSNRdiff_MAX_L;
                DPmeanSNRdiff_MAX_R = Math.abs(DPmeanSNRdiff_R[i]) > DPmeanSNRdiff_MAX_R ? Math.abs(DPmeanSNRdiff_R[i]) : DPmeanSNRdiff_MAX_R;
                DPSNRdiff_L[i] = DPOAE_SNR_MAX_L - DPOAE_SNR_L[i];
                DPSNRdiff_R[i] = DPOAE_SNR_MAX_R - DPOAE_SNR_R[i];
                DPSNRdiff_MAX_L = Math.abs(DPSNRdiff_L[i]) > DPSNRdiff_MAX_L ? Math.abs(DPSNRdiff_L[i]) : DPSNRdiff_MAX_L;
                DPSNRdiff_MAX_R = Math.abs(DPSNRdiff_R[i]) > DPSNRdiff_MAX_R ? Math.abs(DPSNRdiff_R[i]) : DPSNRdiff_MAX_R;
                DPampdiff_L[i] = DPOAE_amp_MAX_L - DPOAE_amp_L[i];
                DPampdiff_R[i] = DPOAE_amp_MAX_R - DPOAE_amp_R[i];
                DPampdiff_MAX_L = Math.abs(DPampdiff_L[i]) > DPampdiff_MAX_L ? Math.abs(DPampdiff_L[i]) : DPampdiff_MAX_L;
                DPampdiff_MAX_R = Math.abs(DPampdiff_R[i]) > DPampdiff_MAX_R ? Math.abs(DPampdiff_R[i]) : DPampdiff_MAX_R;
            }
        }
        for (int i = 0; i < f1.length; i++) {
            f2level_L[i] = (f2_level_L.get(i) - 50);
            f2level_R[i] = (f2_level_R.get(i) - 50);
            f2level_MAX_L = f2level_L[i] > f2level_MAX_L ? f2level_L[i] : f2level_MAX_L;
            f2level_MAX_R = f2level_R[i] > f2level_MAX_R ? f2level_R[i] : f2level_MAX_R;
        }

        int para[] = new int[f1.length];
        for (int i = 0; i < f1.length; i++) {
            int L = ((int) Math.round(f2level_L[i] / f2level_MAX_L * (MAXdBcompensation / 2))) + 3;
            int R = ((int) Math.round(f2level_R[i] / f2level_MAX_R * (MAXdBcompensation / 2))) + 3;
            para[i] = Math.round((L + R) / 2);
        }
        for (int i = 0; i < 10; i++) {
            if (i < 9) {
                para[i] = para[i + 1];
            } else {
                para[i] = 0;
            }
        }
        return para;
    }

    private double[] calculateFFT(float[] signal, int NFFT)
    {
        final int mNumberOfFFTPoints = NFFT;
        double mMaxFFTSample;

        double[] temp = new double[mNumberOfFFTPoints],img = new double[mNumberOfFFTPoints];
        double[] absSignal = new double[mNumberOfFFTPoints/2];

        for(int i = 0; i < mNumberOfFFTPoints; i++){
            temp[i] = (double)signal[i]/32768.f;
            img[i] = 0;
        }

        FFT.transform(temp,img); // --> Here I use FFT class

        mMaxFFTSample = 0.0;
        for(int i = 0; i < (mNumberOfFFTPoints/2); i++)
        {
            absSignal[i] = Math.sqrt(Math.pow(temp[i], 2) + Math.pow(img[i], 2));
            if(absSignal[i] > mMaxFFTSample)
            {
                mMaxFFTSample = absSignal[i];
            }
        }
        return absSignal;
    }
}