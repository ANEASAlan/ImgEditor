package com.example.aaneas.imgeditor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    static int GALERY_REQUEST = 1;
    static int PHOTO_REQUEST = 0;
    static float ScaleFactor = 1.0f;

    static SeekBar LumiBar;
    static RadioGroup RadioColor;
    static ImageView Img;
    static String color="";


    Button Galerie;
    Button AppPhoto;
    Button Save;
    Button Undo;
    Bitmap MonImg;
    Bitmap BasicImg;
    final int SAVED_LENGTH = 3;
    Bitmap[] savedImg = new Bitmap[SAVED_LENGTH];
    int savedImgIndex=0;
    ToggleButton RS_Button;

    Spinner spinnerJava;
    Spinner spinnerRS;

    // public ScaleGestureDetector scaleGestureDetector;      //Outil d'Android permettant d'éviter les calculs de matrices "à la main"


    boolean render_script = false;

    @Override
        protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());


        initImg();
        spinnerJava = findViewById(R.id.spinner1);
        spinnerRS = findViewById(R.id.spinner2);
        LumiBar = findViewById(R.id.ColorB);
        RadioColor =  findViewById(R.id.RadioColor);

        Save.setVisibility(View.INVISIBLE);
        LumiBar.setVisibility(View.INVISIBLE);
        //RadioColor.setVisibility(View.INVISIBLE);
        spinnerJava.setVisibility(View.INVISIBLE);
        spinnerRS.setVisibility(View.INVISIBLE);
        Undo.setVisibility(View.INVISIBLE);
        RS_Button.setVisibility(View.INVISIBLE);


        ArrayAdapter<String> monadaptater = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.spinner1));
        monadaptater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJava.setAdapter(monadaptater);
        spinnerJava.setOnItemSelectedListener(this);


        ArrayAdapter<String> monadaptaterRS = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.spinner2));
        monadaptaterRS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRS.setAdapter(monadaptaterRS);
        spinnerRS.setOnItemSelectedListener(this);



    }

        private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            ScaleFactor *= scaleGestureDetector.getScaleFactor();
            ScaleFactor = Math.max(0.1f, Math.min(ScaleFactor, 5.0f));        //On limite le zoom entre 0.1 fois et 5.0 fois la taille de l'image initiale
            Img.setScaleX(ScaleFactor);
            Img.setScaleY(ScaleFactor);
            return true;
        }
     }

        @SuppressLint("ClickableViewAccessibility")
        private void initImg() {
        checkPremission();
        Img = (PhotoView) findViewById(R.id.photo_view);
        // Img = (ImageView) findViewById(R.id.ImgPhoto);
        RS_Button = findViewById(R.id.RS);
        // Img.setOnTouchListener(this);
        Galerie = findViewById(R.id.Galerie);
        AppPhoto = findViewById(R.id.Photo);
        Save = findViewById(R.id.Save);
        Undo = findViewById(R.id.Undo);
        RS_Button = findViewById(R.id.RS);
        createButtonGalerie();
        createButtonPhoto();
        createButtonSave();
        createButtonUndo();
        createButtonRS();

        }


        //// PRENDRE PHOTO DEPUIS L'APPAREIL PHOTO ///


        ////////////////////////
        /*
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            scaleGestureDetector.onTouchEvent(motionEvent);
            return true;
        }
        */
        ///////////////////////

        ////// DRAG AND DROP //////

        /*
        float x,y,x_tmp,y_tmp = 0.0f;

        public boolean onTouch (View view, MotionEvent event){
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = event.getRawX();
                    y = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    x_tmp = x - event.getRawX();
                    y_tmp = y - event.getRawY();
                    x = event.getRawX();
                    y = event.getRawY();
                    Img.setX(Img.getX() - x_tmp);
                    Img.setY(Img.getY() - y_tmp);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                default:
                    break;
            }
            return true;
        }
        */
        //////////////////////////////
        private void createButtonRS(){
            RS_Button.setChecked(false);
            RS_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    render_script = !render_script;
                    if (isChecked){
                        spinnerRS.setVisibility(View.VISIBLE);
                        spinnerJava.setVisibility(View.INVISIBLE);
                        //spinnerJava.setSelection(0);

                    }else{

                        spinnerRS.setVisibility(View.INVISIBLE);
                        spinnerJava.setVisibility(View.VISIBLE);
                        //spinnerRS.setSelection(0);

                    }

                    if(LumiBar.getVisibility()==View.VISIBLE){
                        LumiBar.setVisibility(View.INVISIBLE);
                        //RadioColor.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }

        private void createButtonSave(){
            Save.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MediaStore.Images.Media.insertImage(getContentResolver(),MonImg,"nom image","description");
                }
            });
        }

        String currentPhotoPath;

        private File createImageFile() throws IOException {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = image.getAbsolutePath();
            return image;
        }

    private static final int REQUEST_RUNTIME_PERMISSION = 1;

        void checkPremission() {
            //select which permission you want
            final String permission = Manifest.permission.CAMERA;
            //final String permission = Manifest.permission.Storage;
            // if in fragment use getActivity()
            if (ContextCompat.checkSelfPermission(MainActivity.this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_RUNTIME_PERMISSION);
                }
            } else {
                return;
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode) {
                case REQUEST_RUNTIME_PERMISSION:
                    final int numOfRequest = grantResults.length;
                    final boolean isGranted = numOfRequest == 1
                            && PackageManager.PERMISSION_GRANTED == grantResults[numOfRequest - 1];
                    if (isGranted) {
                        // you have permission go ahead
                        return;
                    } else {
                        // you dont have permission show toast
                    }
                    break;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }

        }
        Uri photoUri;
        private  void createButtonPhoto(){
            AppPhoto.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            photoUri = (Uri) FileProvider.getUriForFile(MainActivity.this,
                                    "com.example.android.fileprovider",
                                    photoFile);


                            startActivityForResult(takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri), PHOTO_REQUEST);
                        }
                    }
                }
            });
        }


         // ** GALERIE ** //


        private void createButtonGalerie() {
            // Permissions galery //
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions,GALERY_REQUEST);
            }
            Galerie.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent galerie = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(galerie,1);
                }
            });
        }

        private void createButtonUndo() {
            Undo.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(savedImgIndex!=0){
                        savedImgIndex--;
                        MonImg=savedImg[savedImgIndex];
                        Img.setImageBitmap(MonImg);
                    }
                }
            });
        }



        public void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == PHOTO_REQUEST && resultCode == RESULT_OK) {
                try {
                    MonImg = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                    BasicImg = Bitmap.createBitmap(MonImg);
                    Img.setImageBitmap(MonImg);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                spinnerJava.setVisibility(View.VISIBLE);
                Save.setVisibility(View.VISIBLE);
                Undo.setVisibility(View.VISIBLE);
                RS_Button.setVisibility(View.VISIBLE);
            }else if (requestCode == GALERY_REQUEST && resultCode == RESULT_OK) {
                Uri selectedImg = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = this.getContentResolver().query(selectedImg, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgPath = cursor.getString(columnIndex);
                cursor.close();


                MonImg = BitmapFactory.decodeFile(imgPath);
                BasicImg=Bitmap.createBitmap(MonImg);
                Img.setImageBitmap(MonImg);
                spinnerJava.setVisibility(View.VISIBLE);
                Save.setVisibility(View.VISIBLE);
                Undo.setVisibility(View.VISIBLE);
                RS_Button.setVisibility(View.VISIBLE);
                }

        }

        public void saveImg(Bitmap img){
            if(savedImgIndex< SAVED_LENGTH){
                savedImg[savedImgIndex]=Bitmap.createBitmap(img);
                savedImgIndex++;
            }else{
                for(int i = 0; i< SAVED_LENGTH-1; i++){
                    savedImg[i]=Bitmap.createBitmap(savedImg[i+1]);
                }
                savedImg[SAVED_LENGTH-1]=Bitmap.createBitmap(img);
            }
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            LumiBar.setVisibility(View.INVISIBLE);
            //RadioColor.setVisibility(View.INVISIBLE);
            if(position!=0){
                saveImg(MonImg);
            }
            if (parent.getId() == R.id.spinner1) { //pas RS
                switch (position) {
                    case 0:
                        savedImgIndex=0;
                        if(MonImg!=null && !MonImg.equals(BasicImg)){
                            MonImg=Bitmap.createBitmap(BasicImg);
                            Img.setImageBitmap(MonImg);
                        }
                        break;
                    case 1:
                        MonImg=Bitmap.createBitmap(Gris.toGrey(MonImg));
                        break;
                    case 2:
                        //LumiBar.setVisibility(View.VISIBLE);
                        //RadioColor.setVisibility(View.VISIBLE);
                        //Color();
                        MonImg=Bitmap.createBitmap(Couleurs.Coloriser(MonImg));
                        break;
                    case 3:
                        MonImg=Bitmap.createBitmap(Couleurs.Conserve(MonImg));
                        break;
                    case 4:
                        MonImg=Bitmap.createBitmap(Contraste.ContrasteCouleurDynamique(MonImg));
                        break;
                    case 5:
                        MonImg=Bitmap.createBitmap(Contraste.ContrasteCouleurEgaliseur(MonImg));
                        break;
                    case 6:
                        MonImg=Bitmap.createBitmap(Flous.Flougaussien(MonImg,false)); //flou égalisateur / moyenneur
                        break;
                    case 7:
                        MonImg=Bitmap.createBitmap(Flous.Flougaussien(MonImg,true)); // flou gaussien
                        break;
                    case 8:
                        LumiBar.setVisibility(View.VISIBLE);
                        LumiColor();
                        break;
                    case 9:
                        MonImg=Bitmap.createBitmap(Contours.ContoursSobel(Bitmap.createBitmap(Gris.toGrey(MonImg))));
                        break;
                    case 10:
                        MonImg=Bitmap.createBitmap(Contours.ContoursLaplace(Bitmap.createBitmap(Gris.toGrey(MonImg))));
                        break;
                    case 11:
                        Bitmap Greyscale = Bitmap.createBitmap(Gris.toGrey(MonImg));
                        Bitmap InvertedGrey = Bitmap.createBitmap(Couleurs.invert(Greyscale));
                        Bitmap Blurred = Bitmap.createBitmap(Flous.Flougaussien(InvertedGrey,true));
                        MonImg=Bitmap.createBitmap(Crayon.BlendColorDodge(Blurred,Greyscale));
                        break;
                    case 12:
                        MonImg = Bitmap.createBitmap(Contours.ContoursLaplace(MonImg));
                        MonImg = Bitmap.createBitmap(Couleurs.invert(MonImg));
                        break;
                    }

                }else if(parent.getId() == R.id.spinner2){ //RS
                    switch (position){
                        case 0:
                            savedImgIndex=0;
                            if(MonImg!=null && !MonImg.equals(BasicImg)){
                                MonImg=Bitmap.createBitmap(BasicImg);
                                Img.setImageBitmap(MonImg);
                            }
                            break;
                        case 1:
                            MonImg=Bitmap.createBitmap(Gris.toGreyRS(MonImg, this));
                            break;
                        case 2:
                            MonImg=Bitmap.createBitmap(Couleurs.ColoriserRS(MonImg, this));
                            break;
                        case 3:
                            MonImg=Bitmap.createBitmap(Couleurs.ConserveRS(MonImg,this));
                            break;
                        case 4:
                            MonImg=Bitmap.createBitmap(Contraste.ContrasteDynamiqueRS(MonImg,this));
                            break;
                        case 5:
                            MonImg=Bitmap.createBitmap(Contraste.contrastEgaliseurRS(MonImg,this));
                            break;
                        case 6:
                            MonImg=Bitmap.createBitmap(Flous.FlouRS(MonImg,this,false));
                            break;
                        case 7:
                            LumiBar.setVisibility(View.VISIBLE);
                            LumiColor();
                            break;
                        case 8:
                            Bitmap Greyscale = Bitmap.createBitmap(Gris.toGreyRS(MonImg));
                            Bitmap InvertedGrey = Bitmap.createBitmap(Couleurs.invert(Greyscale)); //version RS à écrire
                            Bitmap Blurred = Bitmap.createBitmap(Flous.Flougaussien(InvertedGrey,true)); //version RS à écrire
                            MonImg=Bitmap.createBitmap(Crayon.BlendColorDodge(Blurred,Greyscale)); //version RS à écrire
                            break;
                        case 9:
                            MonImg = Bitmap.createBitmap(Contours.ContoursLaplace(MonImg));
                            MonImg = Bitmap.createBitmap(Couleurs.invert(MonImg)); //version RS à écrire
                            break;
                    }
                }


            }






        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

        public void buttonCLicked(View view){
            saveImg(MonImg);
            LumiBar.setProgress(0);

            boolean checked = ((RadioButton) view).isChecked();
            switch (view.getId()){
                case R.id.radioR:
                    if(checked){
                        color="red";
                    }
                    break;
                case R.id.radioG:
                    if(checked){
                        color="green";
                    }
                    break;
                case R.id.radioB:
                    if(checked){
                        color="blue";
                    }
            }
        }

        public void Color(){
            LumiBar.setOnSeekBarChangeListener(
                    new SeekBar.OnSeekBarChangeListener() {
                        Bitmap n=Bitmap.createBitmap(savedImgIndex==0?BasicImg:savedImg[savedImgIndex-1]);
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int scale, boolean b) {
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            if(color!=""){
                                n=Luminosite.changeColor(n,LumiBar.getProgress(), color);
                                MonImg=Bitmap.createBitmap(n);
                            }
                        }
                    }
                );
        }

        public void LumiColor(){
            LumiBar.setOnSeekBarChangeListener(
                    new SeekBar.OnSeekBarChangeListener() {
                        Bitmap n=Bitmap.createBitmap(savedImgIndex==0?BasicImg:savedImg[savedImgIndex-1]);
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int scale, boolean b) {
                            if(savedImgIndex==0){
                                n=Bitmap.createBitmap(BasicImg);
                            }else{
                                n=Bitmap.createBitmap(savedImg[savedImgIndex-1]);
                            }
                            if (!render_script) {
                                n=Luminosite.changeBrightness(n,scale);
                            } else {
                                n=Luminosite.changeBrightnessRS(n,MainActivity.this,scale);

                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            MonImg=Bitmap.createBitmap(n);
                        }
                    }
            );
        }

    }
