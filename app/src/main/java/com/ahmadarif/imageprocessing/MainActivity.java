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
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ahmadarif.imageprocessing.process.BitmapScaler;
import com.ahmadarif.imageprocessing.process.ImageProcessing;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private Context mContext = this;

    @BindView(R.id.btnGray1) Button b1;
    @BindView(R.id.btnGray2) Button b2;
    @BindView(R.id.btnTresh) Button b3;
    @BindView(R.id.btnCountColor) Button b4;
    @BindView(R.id.btnGreen) Button b5;
    @BindView(R.id.btnBlue) Button b6;
    @BindView(R.id.imageView) ImageView imageView;

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

        Toast.makeText(mContext, "Size = " + bmpOriginal.getWidth() + " x " + bmpOriginal.getHeight(), Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.btnGray1, R.id.btnGray2, R.id.btnTresh, R.id.btnCountColor, R.id.btnGreen, R.id.btnBlue, R.id.btnReset})
    void convertColor(final View view) {
        if (view.getId() == R.id.btnBlue) {
            Intent intent = new Intent(mContext, ChartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

            return;
        }
        new ProccessAsync(view.getId()).execute(bmpOriginal);
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
                    if (bmpOriginal.getWidth() < bmpOriginal.getHeight()) {
                        bmpOriginal = BitmapScaler.scaleToFitWidth(bmpOriginal, SCALE_WIDTH);
                    } else {
                        bmpOriginal = BitmapScaler.scaleToFitWidth(bmpOriginal, SCALE_HEIGHT);
                    }
                    bmpLast = Bitmap.createBitmap(bmpOriginal);
                    imageView.setImageBitmap(bmpOriginal);

                    Toast.makeText(mContext, "Size = " + bmpOriginal.getWidth() + " x " + bmpOriginal.getHeight(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    class ProccessAsync extends AsyncTask<Bitmap, Void, Bitmap> {

        private ProgressDialog dialog;

        private int btnId;
        private Map<Integer, Integer> colors;

        // bisa custom passing parameter
        public ProccessAsync(int btnId) {
            this.btnId = btnId;
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
            switch (btnId) {
                case R.id.btnGray1:
                    result = ImageProcessing.grayscale(params[0], 1);
                    break;
                case R.id.btnGray2:
                    result = ImageProcessing.grayscale(params[0], 2);
                    break;
                case R.id.btnTresh:
                    result = ImageProcessing.treshold(params[0]);
                    break;
                case R.id.btnCountColor:
                    colors = ImageProcessing.countColor(bmpLast);
                    result = Bitmap.createBitmap(bmpLast);
                    break;
                case R.id.btnGreen :
                    result = ImageProcessing.convertToGreen(params[0]);
                    break;
                case R.id.btnBlue :
                    result = ImageProcessing.convertToBlue(params[0]);
                    break;
                case R.id.btnReset :
                    result = Bitmap.createBitmap(bmpOriginal);
                    break;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            bmpLast = Bitmap.createBitmap(bitmap);
            imageView.setImageBitmap(bmpLast);
            dialog.dismiss();

            if (btnId == R.id.btnCountColor) {
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
        }
    }

}