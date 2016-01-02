package it.jaschke.alexandria.activity;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import it.jaschke.alexandria.CameraPreview.CameraPreview;
import it.jaschke.alexandria.R;

/* Import ZBar Class files */


/**
 * Created by roide on 12/28/15.
 */
public class ScannerActivity extends Activity
{
    public static final String RESULT_BARCODE = "barcode";

    public static void launchForResult(Fragment fragment, int requestCode)
    {
        Intent i = new Intent(fragment.getActivity(), ScannerActivity.class);
        fragment.startActivityForResult(i, requestCode);
    }

    private View mScannerBar;
    int mSize = 0;
    int mDuration = 2000;

    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;
    ImageScanner scanner;

    private boolean barcodeScanned = false;
    private boolean previewing = true;

    static
    {
        System.loadLibrary("iconv");
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

        mScannerBar = findViewById(R.id.scanner_bar);
        firstDownAnimation();
    }

    public void onPause()
    {
        super.onPause();
        releaseCamera();
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance()
    {
        Camera c = null;
        try
        {
            c = Camera.open();
        }
        catch(Exception e)
        {
        }
        return c;
    }

    private void releaseCamera()
    {
        if(mCamera != null)
        {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable()
    {
        public void run()
        {
            if(previewing)
            {
                mCamera.autoFocus(autoFocusCB);
            }
        }
    };

    Camera.PreviewCallback previewCb = new Camera.PreviewCallback()
    {
        public void onPreviewFrame(byte[] data, Camera camera)
        {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if(result != 0)
            {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();

                SymbolSet syms = scanner.getResults();
                for(Symbol sym : syms)
                {
                    barcodeScanned = true;
                    String bCode = sym.getData();
                    //Toast.makeText(getApplicationContext(), "" + barcode, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent();
                    i.putExtra(RESULT_BARCODE, bCode);
                    ScannerActivity.this.setResult(RESULT_OK, i);
                    ScannerActivity.this.finish();
                    Log.d("kaushik", "code=" + bCode);
                }
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback()
    {
        public void onAutoFocus(boolean success, Camera camera)
        {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    private void firstDownAnimation()
    {
        Resources res = getResources();
        mSize = res.getDimensionPixelSize(R.dimen.scanner_view_port_size);
        mScannerBar.animate().translationY(mSize/2).setDuration(mDuration).setListener(
                new Animator.AnimatorListener()
                {
                    @Override
                    public void onAnimationStart(Animator animation)
                    {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        upMotion();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation)
                    {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation)
                    {

                    }
                });
    }

    private void downMotion()
    {
        mScannerBar.animate().translationY(mSize/2).setDuration(mDuration)
                .setListener(new Animator.AnimatorListener()
                {
                    @Override
                    public void onAnimationStart(Animator animation)
                    {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        upMotion();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation)
                    {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation)
            {

            }
        });
    }

    private void upMotion()
    {
        mScannerBar.animate().translationY(-mSize/2).setDuration(mDuration)
                .setListener(new Animator.AnimatorListener()
                {
                    @Override
                    public void onAnimationStart(Animator animation)
                    {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        downMotion();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation)
                    {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation)
            {

            }
        });
    }
}
