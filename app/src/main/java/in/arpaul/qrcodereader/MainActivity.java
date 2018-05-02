package in.arpaul.qrcodereader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arpaul.utilitieslib.PermissionUtils;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = "MainActivity";
    TextView tvData;
    Button btnScan;
    SurfaceView svScan;
    int BARCODE_READER_REQUEST_CODE = 11;
    BarcodeDetector barcodeDetect;
    CameraSource cameraSource;
//    Camera camera;
    //https://code.tutsplus.com/tutorials/reading-qr-codes-using-the-mobile-vision-api--cms-24680

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvData = (TextView) findViewById(R.id.tvData);
        btnScan = (Button) findViewById(R.id.btnScan);
        svScan = (SurfaceView) findViewById(R.id.svScan);

        barcodeDetect = new BarcodeDetector.Builder(MainActivity.this).setBarcodeFormats(Barcode.QR_CODE).build();
        CameraSource.Builder builder = new CameraSource.Builder(this, barcodeDetect);
        builder.setAutoFocusEnabled(true);
        builder.setRequestedPreviewSize(640, 480);
        cameraSource = builder.build();
//        cameraSource = new CameraSource.Builder(this, barcodeDetect).setRequestedPreviewSize(640, 480).build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (new PermissionUtils().checkPermission(MainActivity.this, new String[]{
                    Manifest.permission.CAMERA}) != PackageManager.PERMISSION_GRANTED) {
                new PermissionUtils().requestPermission(MainActivity.this, new String[]{
                        Manifest.permission.CAMERA}, 101);
            } else
                setupSurface();
        } else {
            setupSurface();
        }



//        btnScan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Frame frame = new Frame.Builder()
//
//                Intent intent = Intent(MainActivity.this, BarcodeCaptureActivity.class);
//                startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
//            }
//        });
    }

    void setupSurface() {
        svScan.requestFocus();
        svScan.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
//                    camera=Camera.open();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        getHolder().addCallback(this);
//                        setFocusable(true);
                        if (new PermissionUtils().checkPermission(MainActivity.this, new String[]{Manifest.permission.CAMERA}) == PackageManager.PERMISSION_GRANTED) {
                            cameraSource.start(svScan.getHolder());

                        } else {
                            cameraSource.start(svScan.getHolder());
                        }
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                doZoom(width, height);


            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetect.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

//                Log.d(LOG_TAG, barcodes.size() + "");
                if(barcodes.size() > 0) {
                    tvData.post(new Runnable() {
                        @Override
                        public void run() {
                            tvData.setText(barcodes.valueAt(0).displayValue);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            setupSurface();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        if (requestCode == BARCODE_READER_REQUEST_CODE) {
//            if (resultCode == CommonStatusCodes.SUCCESS) {
//                if (data != null) {
//                    BarcodeObject barcode = data.getParcelableExtra<Barcode>(BarcodeCaptureActivity.BarcodeObject)
//                            val p = barcode.cornerPoints
//                    tvData.setText(barcode.displayValue);
//                } else
//                    tvData.setText(R.string.no_barcode_captured);
//            } else
//                Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format),
//                        CommonStatusCodes.getStatusCodeString(resultCode)))
//        } else
//            super.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);

    }

    private final String MCAMERALOCK = "MCAMERALOCK";

    private void doZoom(int width, int height) {
        synchronized (MCAMERALOCK) {
//            if (camera == null) {
//                return;
//            }

            int currentZoom = 0;
            int maxZoom;
//            Camera.Parameters params=camera.getParameters();
//            if (!params.isZoomSupported()) {
//                Log.w(LOG_TAG, "Zoom is not supported on this device");
//                return;
//            }
//            maxZoom = params.getMaxZoom();
//
//            currentZoom = params.getZoom() + 1;
//            float newZoom;
//
//            params.setPreviewSize(width, height);
//            params.setPictureFormat(PixelFormat.JPEG);
//
//            //if you want the preview to be zoomed from start :
//            params.setZoom(params.getMaxZoom());
//
//            camera.setParameters(params);
//            camera.startPreview();
        }
    }
}
