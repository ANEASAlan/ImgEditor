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
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.ToggleButton;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
    static int GALERY_REQUEST = 1;
    static int PHOTO_REQUEST = 0;
    static float ScaleFactor = 1.0f;

    static SeekBar LumiBar;
    static boolean isRs =false;

    ImageView Img;
    Button Galerie;
    Button AppPhoto;
    Button Save;
    Bitmap MonImg;
    ToggleButton RS_Button;

    Spinner myspinner;

    private ScaleGestureDetector scaleGestureDetector;      //Outil d'Android permettant d'éviter les calculs de matrices "à la main"


    boolean render_script = false;

    @Override
        protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());


        initImg();
        myspinner = findViewById(R.id.spinner1);
        LumiBar = findViewById(R.id.ColorB);

        Save.setVisibility(View.INVISIBLE);
        LumiBar.setVisibility(View.INVISIBLE);
        myspinner.setVisibility(View.INVISIBLE);
        RS_Button.setVisibility(View.INVISIBLE);

        ArrayAdapter<String> monadaptater = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Monspinner));
        monadaptater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myspinner.setAdapter(monadaptater);
        myspinner.setOnItemSelectedListener(this);

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

    /*
         MENU DEROULANT///




        }

*/
        @SuppressLint("ClickableViewAccessibility")
        private void initImg() {

        Img = (ImageView) findViewById(R.id.ImgPhoto);
        Img.setOnTouchListener(this);
        Galerie = findViewById(R.id.Galerie);
        AppPhoto = findViewById(R.id.Photo);
        Save = findViewById(R.id.Save);
        RS_Button = findViewById(R.id.RS);
        createButtonGalerie();
        createButtonPhoto();
        createButtonSave();
        createButtonRS();
        }


        //// PRENDRE PHOTO DEPUIS L'APPAREIL PHOTO ///


        private  void createButtonPhoto(){
        AppPhoto.setOnClickListener(new Button.OnClickListener(){
           @Override
           public void onClick(View v) {
               Intent photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
               startActivityForResult(photo,0);
           }
        });
        }


        ////////////////////////

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            scaleGestureDetector.onTouchEvent(motionEvent);
            return true;
        }

        ///////////////////////

        ////// DRAG AND DROP //////

        float x,y = 0.0f;
        boolean moving = false;

        public boolean onTouch (View view, MotionEvent event){
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    if(moving) {
                        x = event.getRawX() - Img.getWidth() / 2;
                        y = event.getRawY() - (Img.getHeight() * 3) / 4;
                        Img.setX(x);
                        Img.setY(y);
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    moving = true;
                    break;
                case MotionEvent.ACTION_UP:
                    moving = false;
                    break;
                default:
                    break;
            }
            return true;
        }

        //////////////////////////////

        private void createButtonSave(){
            Save.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MediaStore.Images.Media.insertImage(getContentResolver(),MonImg,"nom image","description");
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

        // ** RENDER SCRIPT ** //
        private void createButtonRS(){
            RS_Button.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isRs =!isRs;
                    //Log.i( MainActivity.class.getSimpleName(), Boolean.toString(isRs));
                }
            });
        }

        public void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PHOTO_REQUEST && resultCode == RESULT_OK) {
               MonImg= (Bitmap) data.getExtras().get("data");
                Img.setImageBitmap(MonImg);
                myspinner.setVisibility(View.VISIBLE);
                Save.setVisibility(View.VISIBLE);
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
                myspinner.setVisibility(View.VISIBLE);
                Save.setVisibility(View.VISIBLE);
                RS_Button.setVisibility(View.VISIBLE);
                }

        }


        ////////



        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            LumiBar.setVisibility(View.INVISIBLE);
            Long time = System.currentTimeMillis();

            // matrice pour flou gaussien


            switch (position) {

                case 0:
                    Img.setImageBitmap(MonImg);
                    break;
                case 1:
                    new Gris(MonImg, Img, isRs, this);
                    long timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Gris image1 = " + timeafter + " ms");
                    break;
                /*case 2:
                    new Gris(MonImg,Img, "RS", this);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Gris RS image1= " + timeafter + " ms");
                    break;*/
                case 3:

                    new Couleurs(MonImg,Img,"Teinte",this, 10);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Coloriser image1= " + timeafter + " ms");
                    break;
                case 4:
                    new Couleurs(MonImg,Img,"TeinteRS",this, 0);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Coloriser RS image1= " + timeafter + " ms");
                    break;
                case 5:
                    double color = Math.random();
                    new Couleurs(MonImg,Img,"ConserveRouge",this, 10);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Conserve image1= " + timeafter + " ms");
                    break;
                case 6:
                    new Couleurs(MonImg,Img,"ConserveRS",this, 0);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Conserve RS image1= " + timeafter + " ms");
                    break;
                case 7:
                    new Contraste(MonImg, Img,"Dynamique",this);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Contraste image2= " + timeafter + " ms");
                    break;

                case 8:
                    new Contraste(MonImg,Img, "Egaliseur",this);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Contraste image2= " + timeafter + " ms");
                    break;

                case 9:
                    new Contraste(MonImg,Img,"DRS",this);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Contraste image2= " + timeafter + " ms");
                    break;
                case 10:
                    new Contraste(MonImg,Img,"ERS",this);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Contraste image2= " + timeafter + " ms");
                    break;
                case 11:
                    new Flous(MonImg, "Flou",this, Img, 10,false); // (n taille du masque)
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution flou image1= " + timeafter + " ms");
                    break;

                case 12:
                    new Flous(MonImg, "Flou",this, Img, 0, true);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution flou gaussien image2= " + timeafter + " ms");
                    break;
                case 13:
                    new Flous(MonImg, "Flou RS",this, Img, 0, true);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution flou gaussien image2= " + timeafter + " ms");
                    break;
                case 14:
                    LumiBar.setVisibility(View.VISIBLE);
                    render_script = isRs;
                    LumiColor();
                    break;
                case 15:
                    LumiBar.setVisibility(View.VISIBLE);
                    render_script = isRs;
                    LumiColor();
                    break;
                case 16:
                    new Gris(MonImg, Img,true, this); //?
                    new Contours(MonImg, "Sobel", Img);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Contours Sobel image1 = " + timeafter + " ms");
                    break;
                case 17:
                    new Gris(MonImg, Img,true, this); //?
                    new Contours(MonImg, "Laplace", Img);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Contours Laplace image1 = " + timeafter + " ms");
                    break;
            }




        }


        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

        public void LumiColor(){
            LumiBar.setOnSeekBarChangeListener(
                    new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                            if (render_script == false) {
                                new Luminosite(MonImg, "Luminosite", MainActivity.this, Img, i);
                            } else {
                                new Luminosite(MonImg, "LuminositeRS", MainActivity.this, Img, i);

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
