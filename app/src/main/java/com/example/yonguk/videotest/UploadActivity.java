package com.example.yonguk.videotest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView iv;
    String path;
    private Button btnUpload;
    private TextView tvPath, tvUrl;

    private static final String TAG = "UploadActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        btnUpload = (Button) findViewById(R.id.btn_upload);
        tvPath = (TextView) findViewById(R.id.tv_path);
        tvUrl = (TextView) findViewById(R.id.tv_url);
        iv = (ImageView) findViewById(R.id.iv);

        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(path,
                MediaStore.Images.Thumbnails.MINI_KIND);
        iv.setImageBitmap(thumbnail);

        tvPath.setText(path);
        btnUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_upload:
                uploadVideo();
                break;
        }
    }

    private void uploadVideo(){
        class UploadVideo extends AsyncTask<Void, Void, String> {
            ProgressDialog uploading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                uploading = ProgressDialog.show(UploadActivity.this, "Uploading File", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                uploading.dismiss();
                tvUrl.setText(Html.fromHtml("<b>Uploaded at <a href='" + s + "'>" + s + "</a></b>"));
            }

            @Override
            protected String doInBackground(Void... params) {
                Upload uv = new Upload();
                String msg = uv.uploadVideo(path);
                return msg;
            }
        }
        UploadVideo uv = new UploadVideo();
        uv.execute();
    }
}
