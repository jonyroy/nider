package io.jonyroy.nider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends Activity {

    private static final int CAMERA_REQUEST = 1888;
    ImageView captureImgView, cropImgView;

    Bitmap capturedImg;
    Button cropButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        captureImgView = (ImageView) findViewById(R.id.captureImgView);
        cropImgView = (ImageView) findViewById(R.id.cropImageView);
        Button photoButton = (Button) findViewById(R.id.captureButton);
        //cropButton = (Button) findViewById(R.id.button2);

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

    }

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    void postRequest(String postUrl, String postBody) throws IOException {

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(postBody, JSON);

        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("##############################");
                System.out.print(response.body().string());
                //Log.d("TAG",response.body().string());
            }
        });
    }

    void getRequest(String url) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                System.out.println(response.body().string());
                System.out.println("##############################");
                System.out.print(response.body().toString());
                //Log.d("TAG",response.body().string());
            }
        });
    }

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private String getRequestBody(String imgStr) {
        JSONObject json = new JSONObject();

        try {
            json.put("data", imgStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json.toString();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            capturedImg = (Bitmap) data.getExtras().get("data");

            String imgString = encodeImage(capturedImg);
            String requestBody = getRequestBody(imgString);

            try {
                //getRequest("http://192.168.0.102:8000");
                postRequest("http://192.168.0.102:8000/api/v1/nid-front-image-info", requestBody);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}