package com.example.aaneas.imgeditor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
    static int GALERY_REQUEST = 1;
    static int PHOTO_REQUEST = 0;
    static float ScaleFactor = 1.0f;

    static SeekBar LumiBar;
    static ImageView Img;


    Button Galerie;
    Button AppPhoto;
    Button Save;
    Button Undo;
    Bitmap MonImg;
    Bitmap[] savedImg = new Bitmap[10];
    int savedImgIndex=0;
    ToggleButton RS_Button;

    Spinner spinnerJava;
    Spinner spinnerRS;

    private ScaleGestureDetector scaleGestureDetector;      //Outil d'Android permettant d'éviter les calculs de matrices "à la main"


    boolean render_script;

    @Override
        protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());


        initImg();
        spinnerJava = findViewById(R.id.spinner1);
        spinnerRS = findViewById(R.id.spinner2);
        LumiBar = findViewById(R.id.ColorB);

        Save.setVisibility(View.INVISIBLE);
        LumiBar.setVisibility(View.INVISIBLE);
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

        Img = (ImageView) findViewById(R.id.ImgPhoto);
        RS_Button = findViewById(R.id.RS);
        Img.setOnTouchListener(this);
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

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            scaleGestureDetector.onTouchEvent(motionEvent);
            return true;
        }

        ///////////////////////

        ////// DRAG AND DROP //////

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

        //////////////////////////////
        private void createButtonRS(){
            RS_Button.setChecked(false);
            RS_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        spinnerRS.setVisibility(View.VISIBLE);
                        spinnerJava.setVisibility(View.INVISIBLE);

                    }else{

                        spinnerRS.setVisibility(View.INVISIBLE);
                        spinnerJava.setVisibility(View.VISIBLE);

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

        private  void createButtonPhoto(){
            AppPhoto.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(photo,0);
                }
            });
        }


         // ** GALERIE ** //


        private void createButtonGalerie() {

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
                        MainActivity.Img.setImageBitmap(MonImg);
                    }
                }
            });
        }



        public void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PHOTO_REQUEST && resultCode == RESULT_OK) {
               MonImg= (Bitmap) data.getExtras().get("data");
                Img.setImageBitmap(MonImg);
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
                Img.setImageBitmap(MonImg);
                spinnerJava.setVisibility(View.VISIBLE);
                Save.setVisibility(View.VISIBLE);
                Undo.setVisibility(View.VISIBLE);
                RS_Button.setVisibility(View.VISIBLE);
                }

        }


        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            LumiBar.setVisibility(View.INVISIBLE);
            if(position!=0){
                savedImg[savedImgIndex]=Bitmap.createBitmap(MonImg);
                savedImgIndex++;
            }
            if (parent.getId() == R.id.spinner1) {
                render_script = false;
                switch (position) {
                    case 0:
                        Img.setImageBitmap(savedImg[0]);
                        break;
                    case 1:
                        MonImg=Bitmap.createBitmap(Gris.toGrey(MonImg));
                        Gris.toGrey(MonImg);
                        break;
                    case 2:
                        Couleurs.Coloriser(MonImg);
                        break;
                    case 3:
                        Couleurs.Conserve(MonImg);
                        break;
                    case 4:
                        Contraste.ContrasteCouleurDynamique(MonImg);
                        break;
                    case 5:
                        Contraste.ContrasteCouleurEgaliseur(MonImg);
                        break;
                    case 6:
                        Flous.Flougaussien(MonImg,false);
                        break;
                    case 7:
                        Flous.Flougaussien(MonImg,true);
                        break;
                    case 8:
                        LumiBar.setVisibility(View.VISIBLE);
                        LumiColor();
                        break;
                    case 9:
                        Gris.toGreyRS(MonImg,this);
                        Contours.ContoursSobel(MonImg);
                        break;
                    case 10:
                        Gris.toGreyRS(MonImg,this);
                        Contours.ContoursLaplace(MonImg);
                        break;
                    }

                }else if(parent.getId() == R.id.spinner2){
                    render_script = true;
                    switch (position){
                        case 0:
                            Img.setImageBitmap(MonImg);
                            break;
                        case 1:
                            Gris.toGreyRS(MonImg, this);
                            break;
                        case 2:
                            Couleurs.ColoriserRS(MonImg, this);
                            break;
                        case 3:
                            Couleurs.ConserveRS(MonImg,this);
                            break;
                        case 4:
                            Contraste.ContrasteDynamiqueRS(MonImg,this);
                            break;
                        case 5:
                            Contraste.contrastEgaliseurRS(MonImg,this);
                            break;
                        case 6:
                            Flous.FlouRS(MonImg,this);
                            break;
                        case 7:
                            LumiBar.setVisibility(View.VISIBLE);
                            LumiColor();
                            break;
                    }
                }


            }






        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

        public void LumiColor(){
            LumiBar.setOnSeekBarChangeListener(
                    new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int scale, boolean b) {
                            if (render_script == false) {
                                Luminosite.changeBrightness(MonImg,scale);
                            } else {
                                Luminosite.changeBrightnessRS(MonImg,MainActivity.this,scale);

                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    }
            );
        }

    }
