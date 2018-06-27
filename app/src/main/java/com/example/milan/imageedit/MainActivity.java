package com.example.milan.imageedit;


import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.mukesh.image_processing.ImageProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    static final int REQUEST_IMAGE_CAPTURE =1;
    private static final int GALLERY = 1;
    Button b;
    ImageView image;
    static Bitmap bitmap;
    static Bitmap newphoto;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = findViewById(R.id.imageView);

        //Spinner element
        Spinner spinner = findViewById(R.id.spinner);

        //disable buttom if no camera on device
        if (!hasCamera())
            spinner.setEnabled(false);

        //Spinner click listener
        spinner.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add("original");
        categories.add("doInvert");
        categories.add("doGreyScale");
        categories.add("applyGaussianBlur");
        categories.add("applyMeanRemoval");
        categories.add("emboss");
        categories.add("engrave");
        categories.add("applyFleaEffect");
        categories.add("applyBlackFilter");
        categories.add("applySnowEffect");
        categories.add("applyReflection");
        categories.add("Save");

        //adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,categories);

        //Drop down layout style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }


    //checking device has camera or not
    private boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }


    //taking image
    public void launchCamera(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY);


       // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
    }

    //saving image and saving it
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Uri mImageUri = data.getData();
            try {
                  bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //use the bitmap as you like

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        if(bitmap != null)
        {
           effect(item);
        }else
        {
            Toast.makeText(this, "Select a Image", Toast.LENGTH_SHORT).show();
        }

    }

    private void effect(String item) {

        Bitmap original = bitmap;

        ImageProcessor imageProcessor = new ImageProcessor();

        if (item == "original") {
            image.setImageBitmap(original);
        } else if (item == "doInvert") {
            newphoto = imageProcessor.doInvert(original);
            image.setImageBitmap(newphoto);
        } else if (item == "doGreyScale") {
            newphoto = imageProcessor.doGreyScale(original);
            image.setImageBitmap(newphoto);
        } else if (item == "applyGaussianBlur") {
            newphoto = imageProcessor.applyGaussianBlur(original);
            image.setImageBitmap(newphoto);
        } else if (item == "applyMeanRemoval") {
            newphoto = imageProcessor.applyMeanRemoval(original);
            image.setImageBitmap(newphoto);
        } else if (item == "emboss") {
            newphoto = imageProcessor.emboss(original);
            image.setImageBitmap(newphoto);
        } else if (item == "engrave") {
            newphoto = imageProcessor.engrave(original);
            image.setImageBitmap(newphoto);
        } else if (item == "applyFleaEffect") {
            newphoto = imageProcessor.applyFleaEffect(original);
            image.setImageBitmap(newphoto);
        } else if (item == "applyBlackFilter") {
            newphoto = imageProcessor.applyBlackFilter(original);
            image.setImageBitmap(newphoto);
        } else if (item == "applySnowEffect") {
            newphoto = imageProcessor.applySnowEffect(original);
            image.setImageBitmap(newphoto);
        } else if (item == "applyReflection") {
            newphoto = imageProcessor.applyReflection(original);
            image.setImageBitmap(newphoto);
        }


        if (item == "Save" && newphoto != null) {

            String savedImageURL = MediaStore.Images.Media.insertImage(
                    getContentResolver(),
                    newphoto,
                    "item",
                    item + "image"
            );
            // Parse the gallery image url to uri
            Uri savedImageURI = Uri.parse(savedImageURL);

            Toast.makeText(this, "Image saved to gallery.\n" + savedImageURL, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}