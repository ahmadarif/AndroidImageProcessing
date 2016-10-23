package com.ahmadarif.imageprocessing;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ahmadarif.imageprocessing.process.basic.ImageProcessing;
import com.ahmadarif.imageprocessing.process.chaincode.ChainCode;
import com.ahmadarif.imageprocessing.process.chaincode.ChainCodeResult;
import com.ahmadarif.imageprocessing.process.matcher.Matcher;
import com.ahmadarif.imageprocessing.process.mathscanner.MathScanner;
import com.ahmadarif.imageprocessing.process.thinning.ZhangSuenThinning;
import com.ahmadarif.imageprocessing.process.utils.BitmapScaler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private Context mContext = this;

    @BindView(R.id.btnBasicImageProcessing) Button btnBasicImageProcessing;
    @BindView(R.id.btnReset) Button btnReset;
    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.txtChainCode) EditText txtChainCode;

    private Bitmap bmpOriginal;
    private Bitmap bmpLast;

    private static final String TAG = "MainActivity";
    private static final int SELECT_PICTURE = 100;
    private static final int SCALE_WIDTH = 600;
    private static final int SCALE_HEIGHT = 840;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        BitmapDrawable bmpDrawable = (BitmapDrawable) imageView.getDrawable();
        bmpOriginal = bmpDrawable.getBitmap();
        bmpLast = Bitmap.createBitmap(bmpOriginal);

        // Initialize Realm
        Realm.init(mContext);
    }

    @OnClick(R.id.btnBasicImageProcessing)
    void btnBasicImageProcClicked() {
        final CharSequence colors[] = new CharSequence[] {"Grayscale", "Treshold", "Number of Colors", "Chain Code", "Thinning"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0 || which == 1 || which == 2)
                    new BasicAsync(which).execute(bmpOriginal);
                else if (which == 3)
                    new ChainCodeAsync().execute(bmpOriginal);
                else if (which == 4)
                    new ThinningAsync().execute(bmpOriginal);
            }
        });
        builder.show();
    }

    @OnClick(R.id.btnMath)
    void btnMathClick() {
        new MathAsync().execute(bmpOriginal);
    }

    @OnClick(R.id.btnReset)
    void btnResetImageClick() {
        bmpLast = Bitmap.createBitmap(bmpOriginal);
        setDisplay(bmpLast);
    }

    @OnClick(R.id.imageView)
    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            txtChainCode.setVisibility(View.GONE);

            if (requestCode == SELECT_PICTURE) {
                // Get the url from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    String path = getPathFromURI(selectedImageUri);
                    Log.i(TAG, "Image Path : " + path);
                    // Set the image in ImageView

                    // change bitmap status
                    bmpOriginal = BitmapFactory.decodeFile(path);
                    bmpLast = Bitmap.createBitmap(bmpOriginal);
                    setDisplay(bmpOriginal);
                }
            }
        }
    }

    /* Get the real path from the URI */
    private String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        assert cursor != null;
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private void setDisplay(Bitmap bmp) {
        Bitmap bmpDisplay;
        if (bmp.getWidth() < bmp.getHeight()) {
            bmpDisplay = BitmapScaler.scaleToFitWidth(bmp, SCALE_WIDTH);
        } else {
            bmpDisplay = BitmapScaler.scaleToFitWidth(bmp, SCALE_HEIGHT);
        }
        imageView.setImageBitmap(bmpDisplay);
    }

    class BasicAsync extends AsyncTask<Bitmap, Void, Bitmap> {

        private ProgressDialog dialog;

        private int optionIndex;
        private Map<Integer, Integer> colors;

        // bisa custom passing parameter
        BasicAsync(int optionIndex) {
            this.optionIndex = optionIndex;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(mContext);
            dialog.setMessage("Processing...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Bitmap doInBackground(Bitmap... params) {
            /* kamus */
            Bitmap result = null;

            /* algoritma */

            // proses sesuai dengan id tombol tombol yang ditekan
            switch (optionIndex) {
                case 0:
                    result = ImageProcessing.grayscale(params[0]);
                    break;
                case 1:
                    result = ImageProcessing.treshold(params[0]);
                    break;
                case 2:
                    colors = ImageProcessing.countColor(bmpLast);
                    result = Bitmap.createBitmap(bmpLast);
                    break;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            bmpLast = Bitmap.createBitmap(bitmap);
            setDisplay(bmpLast);
            dialog.dismiss();

            if (optionIndex == 2) {
                new AlertDialog.Builder(mContext)
                    .setTitle("Status")
                    .setMessage("Color count = " + colors.size())
                    .setPositiveButton("Detail", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ChartActivity.start(mContext, colors);
                        }
                    })
                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
            }

            txtChainCode.setVisibility(View.GONE);
        }
    }

    class ChainCodeAsync extends AsyncTask<Bitmap, Void, ChainCodeResult> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(mContext);
            dialog.setMessage("Processing...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected ChainCodeResult doInBackground(Bitmap... params) {
            return ChainCode.singleObject(params[0]);
        }

        @Override
        protected void onPostExecute(ChainCodeResult results) {
            dialog.dismiss();

            if (results.dir.size() > 0) {
                txtChainCode.setText(results.toString());
                txtChainCode.setVisibility(View.VISIBLE);
                setTitle(Matcher.process(results.toString()));
            } else {
                Log.i(TAG, "NULL GAN");
            }
        }
    }

    class ThinningAsync extends AsyncTask<Bitmap, Void, Bitmap> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(mContext);
            dialog.setMessage("Processing...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Bitmap doInBackground(Bitmap... params) {
            return ZhangSuenThinning.process(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            bmpLast = Bitmap.createBitmap(bitmap);
            setDisplay(bmpLast);
            imageView.refreshDrawableState();
            dialog.dismiss();
        }
    }

    class MathAsync extends AsyncTask<Bitmap, Void, List<String>> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(mContext);
            dialog.setMessage("Processing...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected List<String> doInBackground(Bitmap... params) {
            Bitmap bmp;
            List<String> chars = new ArrayList<>();
            List<Bitmap> bitmaps;
            ChainCodeResult chainCodeResult;
            String checkStr;

            Log.i(TAG, " ");
            Log.i(TAG, "BEGIN Grayscale");
            bmp = ImageProcessing.grayscale(params[0]);
            Log.i(TAG, "END Grayscale");

            Log.i(TAG, " ");
            Log.i(TAG, "BEGIN Treshold");
            bmp = ImageProcessing.treshold(bmp);
            Log.i(TAG, "END Treshold");

            Log.i(TAG, " ");
            Log.i(TAG, "BEGIN Segment");
            bitmaps = MathScanner.segment(bmp);
            Log.i(TAG, "END Segment");

            Log.i(TAG, " ");
            Log.i(TAG, "BEGIN ChainCode and Detection");
            for (Bitmap bmp2 : bitmaps) {
                chainCodeResult = ChainCode.singleObject(bmp2);
                checkStr = Matcher.process(chainCodeResult.toString());
                Log.i(TAG, "Result = " + checkStr);
                chars.add(checkStr);
            }
            Log.i(TAG, "END ChainCode and Detection");

            return chars;
        }

        @Override
        protected void onPostExecute(List<String> chars) {
//            for (String myChar : chars) {
//                Log.i(TAG, myChar);
//            }

            bmpLast = ZhangSuenThinning.process(bmpOriginal);
            setDisplay(bmpLast);
            imageView.refreshDrawableState();
            dialog.dismiss();
        }
    }

}