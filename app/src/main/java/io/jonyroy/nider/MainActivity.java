package io.jonyroy.nider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

    private static final int CAMERA_REQUEST = 1888;
    ImageView imageView, imageView2;

    Bitmap photo;
    Button crop;

    @SuppressLint("CutPasteId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView1);
        Button photoButton = (Button) findViewById(R.id.button1);
        crop = (Button) findViewById(R.id.button2);

        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
        performCrop();
    }

    private void performCrop() {
        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap croppedBmp = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight() - 30);
                imageView2.setImageBitmap(croppedBmp);
            }
        });
    }
}