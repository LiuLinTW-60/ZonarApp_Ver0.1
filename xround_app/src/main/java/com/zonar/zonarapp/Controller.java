package com.zonar.zonarapp;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;

public class Controller {

    PendingIntent mPermissionIntent;
    UsbManager mUsbManager;
    UsbDevice device;
    UsbDeviceConnection mUsbDeviceConnection;
    final AlertDialog.Builder dlgBuilder;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    byte[] barrybuffer = new byte[255];
    int nlengthbcdDevice;
    int THREE_BANDS_DEFAULT = 25368;
    int TWO_BANDS_DEFAULT = 25344;
    int NUM = 0, MODE = 0;
    boolean connected = false;

    public Controller(final Context context) {
        super();
        mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(
                ACTION_USB_PERMISSION), 0);
        //IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        //context.registerReceiver(mUsbReceiver, filter);
        dlgBuilder = new AlertDialog.Builder(context);
        dlgBuilder.setTitle("Notice");
        dlgBuilder.setMessage("Please make sure Zonar is plugged in!");
        dlgBuilder.setNeutralButton("Retry", null);
        dlgBuilder.setCancelable(false);
        if (!deviceConnected()){
            Alert(context);
        }
        //context.unregisterReceiver(mUsbReceiver);
    }

    public void Alert(final Context context){
        final AlertDialog alertDialog = dlgBuilder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deviceConnected() && device.getVendorId()==9770){
                    Toast.makeText(context, "Device connected", Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                }else{
                    Toast.makeText(context, "Device not connected", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean deviceConnected(){
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        if (deviceIterator.hasNext()) {
            do {
                device = deviceIterator.next();
                mUsbManager.requestPermission(device, mPermissionIntent);
            } while (deviceIterator.hasNext());
            return true;
        } else {
            return false;
        }
    }
    private void OpenDevice() {
        boolean hasPermision = mUsbManager.hasPermission(device);
        if (hasPermision) {
            mUsbDeviceConnection = mUsbManager.openDevice(device);
            connected = true;
        } else {
            mUsbManager.requestPermission(device, mPermissionIntent);
            connected = false;
        }
    }

    public String getDeviceName() {
        if (device.getVendorId() == 9770) {
            return device.getDeviceName();
            /*if (device.getProductId() == 37634) {
                return "ZONAR";
            } else {
                return "Unrecognized";
            }*/
        } else {
            return "Device Not Supported";
        }
    }

    public double[] EQ_table(int numero) {
        double[] EQ;
        switch (numero) {
            case 1:
                EQ = new double[]{6, 3, 2, 0, 0, 0, 0, 1.5, 1.5, 0};
                break;
            case 2:
                EQ = new double[]{4.5, 3.75, 3, 0, 0, 0, 0, 1.5, 1.5, 3.75};
                break;
            case 3:
                EQ = new double[]{3, 4.5, 4, 0, 0, 0, 0, 1.5, 1.5, 4.5};
                break;
            case 4:
                EQ = new double[]{1.5, 5.25, 5, 0, 0, 0, 0, 1.5, 1.5, 3};
                break;
            case 5:
                EQ = new double[]{1.5, 6, 6, 3, 2, 0, 0, 1.5, 1.5, 1.125};
                break;
            case 6:
                EQ = new double[]{1.125, 4.5, 4.5, 3.75, 3, 0, 0, 1.125, 1.125, -3};
                break;
            case 7:
                EQ = new double[]{0.75, 3, 3, 4.5, 4, 0, 0, 0.75, 0.75, -4.5};
                break;
            case 8:
                EQ = new double[]{0.375, 1.5, 1.5, 5.25, 5, 0, 0, 0.375, 0.375, 3};
                break;
            case 9:
                EQ = new double[]{-1.5, 1.5, 1.5, 6, 6, 3, 2, 0.375, 0.375, -2.5};
                break;
            case 10:
                EQ = new double[]{-3, 1.125, 1.125, 4.5, 4.5, 3.75, 3, 0.75, 0.75, 6};
                break;
            case 11:
                EQ = new double[]{-4.5, 0.75, 0.75, 3, 3, 4.5, 4, 1.125, 1.125, -4.5};
                break;
            case 12:
                EQ = new double[]{-6, 0.375, 0.375, 1.5, 1.5, 5.25, 5, 1.5, 1.5, -1.5};
                break;
            case 13:
                EQ = new double[]{-6, -1.5, -1.5, 1.5, 1.5, 6, 6, 3, 2, -6};
                break;
            case 14:
                EQ = new double[]{-4.5, -3, -3, 1.125, 1.125, 4.5, 4.5, 3.75, 3, 0};
                break;
            case 15:
                EQ = new double[]{-3, -4.5, -4.5, 0.75, 0.75, 3, 3, 4.5, 4, -5.25};
                break;
            case 16:
                EQ = new double[]{-1.5, -6, -6, 0.375, 0.375, 1.5, 1.5, 5.25, 5, 0.375};
                break;
            case 17:
                EQ = new double[]{1.5, -6, -6, 0, 0, 1.5, 1.5, 6, 6, 5};
                break;
            case 18:
                EQ = new double[]{3, -4.5, -4.5, 0, 0, 1.125, 1.125, 4.5, 4.5, -3};
                break;
            case 19:
                EQ = new double[]{4.5, -3, -3, 0, 0, 0.75, 0.75, 3, 3, -0.75};
                break;
            case 20:
                EQ = new double[]{6, -1.5, -1.5, 0, 0, 0.375, 0.375, 1.5, 1.5, -6};
                break;
            default:
                EQ = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        }

        return EQ;
    }

    public void write_ZonarEQ(double[] ZonarEQ, int numero, int mode){

        if (!connected){
            OpenDevice();
            return;
        }

        if (numero == NUM  && mode == MODE) return;


        NUM = numero;
        MODE = mode;
        int[] EQ = new int[ZonarEQ.length];
        double mode_value;

        if (mode == 0) {
            mode_value = 0.5;
        } else if (mode == 2) {
            mode_value = 1.5;
        } else {
            mode_value = 1;
        }
        for (int i = 0; i < EQ.length; i++){
            EQ[i] = (int) Math.round(ZonarEQ[i] * mode_value * 5 / 12 + 11 * mode_value / 2);
        }
        //Toast.makeText(context, Arrays.toString(EQ), Toast.LENGTH_LONG).show();

        int GAIN = (EQ[0] << 11) + (EQ[1] << 6) + (EQ[2] << 1);
        int sarryEQData = THREE_BANDS_DEFAULT + GAIN + 1;
        int convertData = Convert(sarryEQData);
        write_command(0x100E, convertData);
        write_command(0x3C0E, convertData);

        GAIN = (EQ[3] << 11) + (EQ[4] << 6);
        sarryEQData = TWO_BANDS_DEFAULT + GAIN + 1;
        convertData = Convert(sarryEQData);
        write_command(0x110E, convertData);
        write_command(0x3D0E, convertData);

        GAIN = (EQ[5] << 11) + (EQ[6] << 6) + (EQ[7] << 1);
        sarryEQData = THREE_BANDS_DEFAULT + GAIN + 1;
        convertData = Convert(sarryEQData);
        write_command(0x260E, convertData);
        write_command(0x520E, convertData);

        GAIN = (EQ[8] << 11) + (EQ[9] << 6);
        sarryEQData = TWO_BANDS_DEFAULT + GAIN + 1;
        convertData = Convert(sarryEQData);
        write_command(0x270E, convertData);
        write_command(0x530E, convertData);
    }

    public void write_EQ(int[] EQ, int ENABLE) {
        if (connected) {
            int GAIN = (EQ[0] << 11) + (EQ[1] << 6) + (EQ[2] << 1);
            int sarryEQData = THREE_BANDS_DEFAULT + GAIN * ENABLE + 1;
            int convertData = Convert(sarryEQData);
            write_command(0x100E, convertData);
            write_command(0x3C0E, convertData);

            GAIN = (EQ[3] << 11) + (EQ[4] << 6);
            sarryEQData = TWO_BANDS_DEFAULT + GAIN * ENABLE + 1;
            convertData = Convert(sarryEQData);
            write_command(0x110E, convertData);
            write_command(0x3D0E, convertData);

            GAIN = (EQ[5] << 11) + (EQ[6] << 6) + (EQ[7] << 1);
            sarryEQData = THREE_BANDS_DEFAULT + GAIN * ENABLE + 1;
            convertData = Convert(sarryEQData);
            write_command(0x260E, convertData);
            write_command(0x520E, convertData);

            GAIN = (EQ[8] << 11) + (EQ[9] << 6);
            sarryEQData = TWO_BANDS_DEFAULT + GAIN * ENABLE + 1;
            convertData = Convert(sarryEQData);
            write_command(0x270E, convertData);
            write_command(0x530E, convertData);
        } else {
            OpenDevice();
        }
    }

    public void Switch_MIC(int item) {
        if(connected) {
            int value;
            switch (item) {
                case 1:
                    value = 0x0000;//VOICE
                    break;
                case 2:
                    value = 0x0002;//ERR
                    break;
                case 3:
                    value = 0x0004;//REF
                    break;
                default:
                    return;
            }
            nlengthbcdDevice = mUsbDeviceConnection.controlTransfer(0x40, 0x55, value, 0x0000, barrybuffer,
                    0x0000, 1000);
            if (nlengthbcdDevice != 0x0000) {
                mUsbDeviceConnection.close();
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void write_command(int index, int convertData) {
        /**Write SPI Offset*/
        nlengthbcdDevice = mUsbDeviceConnection.controlTransfer(0x40, 0x50, 0x0000, index, barrybuffer,
                0x0000, 1000);
        if (nlengthbcdDevice != 0x0000) {
            mUsbDeviceConnection.close();
        }
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /**Write SPI Data*/
        nlengthbcdDevice = mUsbDeviceConnection.controlTransfer(0x40, 0x51, convertData, 0x0000, barrybuffer,
                0x0000, 1000);
        if (nlengthbcdDevice != 0x0000) {
            mUsbDeviceConnection.close();
        }
    }

    private int Convert (int data) {
        byte [] strAsByteArray = Integer.toHexString(data).getBytes();
        byte [] result = new byte [strAsByteArray.length];
        result[0] = strAsByteArray[2];
        result[1] = strAsByteArray[3];
        result[2] = strAsByteArray[0];
        result[3] = strAsByteArray[1];
        return Integer.parseInt(new String(result),16);
    }

    /*private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent
                            .getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(
                            UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            // call method to set up device communication
                        }
                    } else {
                        Log.d("ERROR", "permission denied for device " + device);
                    }
                }
            }
            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                // Device removed
                synchronized (this) {
                    // ... Check to see if usbDevice is yours and cleanup ...
                    Toast.makeText(context, "Device disconnected", Toast.LENGTH_LONG).show();
                    connected = false;

                }
            }
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                // Device attached
                synchronized (this) {
                    // Qualify the new device to suit your needs and request permission
                    if ((device.getVendorId() == 9770) && (device.getProductId() == 37634)) {
                        mUsbManager.requestPermission(device, mPermissionIntent);
                        Toast.makeText(context, "Device connected", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    };*/
}
