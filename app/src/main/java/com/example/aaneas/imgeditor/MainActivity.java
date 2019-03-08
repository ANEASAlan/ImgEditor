package com.example.aaneas.imgeditor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnTouchListener {
    static int GALERY_REQUEST = 1;
    static int PHOTO_REQUEST = 0;
    static SeekBar LumiBar;

    ImageView Img;
    Button Galerie;
    Button AppPhoto;
    Button Save;
    Bitmap MonImg;

    Spinner myspinner;
    private ScaleGestureDetector scaleGestureDetector;      //Outil d'Android permettant d'éviter les calculs de matrices "à la main"
    private float scaleFactor = 1.0f;


    boolean render_script = false;

    @Override
        protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());


        initImg();
        myspinner = findViewById(R.id.spinner1);
        LumiBar = (SeekBar) findViewById(R.id.ColorB);

        Save.setVisibility(View.INVISIBLE);
        LumiBar.setVisibility(View.INVISIBLE);
        myspinner.setVisibility(View.INVISIBLE);

        ArrayAdapter<String> monadaptater = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Monspinner));
        monadaptater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myspinner.setAdapter(monadaptater);
        myspinner.setOnItemSelectedListener(this);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            scaleFactor *= scaleGestureDetector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));        //On limite le zoom entre 0.1 fois et 5.0 fois la taille de l'image initiale
            Img.setScaleX(scaleFactor);
            Img.setScaleY(scaleFactor);
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
        Galerie = (Button) findViewById(R.id.Galerie);
        AppPhoto = (Button) findViewById(R.id.Photo);
        Save = (Button) findViewById(R.id.Save);
        createButtonGalerie();
        createButtonPhoto();
        createButtonSave();
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
                    //MediaStore.Images.Media.insertImage(getContentResolver(),tempo,"nom image","description");
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

        public void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PHOTO_REQUEST && resultCode == RESULT_OK) {
               MonImg= (Bitmap) data.getExtras().get("data");
                Img.setImageBitmap(MonImg);
                myspinner.setVisibility(View.VISIBLE);
                Galerie.setVisibility(View.INVISIBLE);
                AppPhoto.setVisibility(View.INVISIBLE);
                Save.setVisibility(View.VISIBLE);
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
                Galerie.setVisibility(View.INVISIBLE);
                AppPhoto.setVisibility(View.INVISIBLE);
                Save.setVisibility(View.VISIBLE);
                }

        }


        ////////



        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            LumiBar.setVisibility(View.INVISIBLE);
            ImageView tempo = findViewById(R.id.ImgPhoto);
            Long time = System.currentTimeMillis();


            // matrice pour flou gaussien


            switch (position) {

                case 0:
                    tempo.setImageBitmap(MonImg);
                    break;
                case 1:
                    new Gris(MonImg, tempo,"Gris", this);
                    long timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Gris image1 = " + timeafter + " ms");
                    break;
                case 2:
                    new Gris(MonImg,tempo, "RS", this);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Gris RS image 1= " + timeafter + " ms");
                    break;
                case 3:

                    new Couleurs(MonImg,tempo,"Teinte",this, 10);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Coloriser image1= " + timeafter + " ms");
                    break;
                case 4:
                    new Couleurs(MonImg,tempo,"TeinteRS",this, 0);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Coloriser RS image1= " + timeafter + " ms");
                    break;
                case 5:
                    double color = Math.random();
                    new Couleurs(MonImg,tempo,"ConserveRouge",this, 10);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Conserve image1= " + timeafter + " ms");
                    break;
                case 6:
                    new Couleurs(MonImg,tempo,"ConserveRS",this, 0);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Conserve RS image1= " + timeafter + " ms");
                    break;
                case 7:
                    new Contraste(MonImg, tempo,"Dynamique",this);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Contraste image2= " + timeafter + " ms");
                    break;

                case 8:
                    new Contraste(MonImg,tempo, "Egaliseur",this);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Contraste image2= " + timeafter + " ms");
                    break;

                case 9:
                    new Contraste(MonImg,tempo,"DRS",this);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Contraste image2= " + timeafter + " ms");
                    break;
                case 10:
                    new Contraste(MonImg,tempo,"ERS",this);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Contraste image2= " + timeafter + " ms");
                    break;
                case 11:
                    new Flous(MonImg, "Flou",this, tempo, 10,false); // (n taille du masque)
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution flou image1= " + timeafter + " ms");
                    break;

                case 12:
                    new Flous(MonImg, "Flou",this, tempo, 0, true);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution flou gaussien image2= " + timeafter + " ms");
                    break;
                case 13:
                    //FLOU RS//
                    break;
                case 14:
                    LumiBar.setVisibility(View.VISIBLE);
                    render_script = false;
                    LumiColor();
                    break;
                case 15:
                    LumiBar.setVisibility(View.VISIBLE);
                    render_script = true;
                    LumiColor();
                    break;
                case 16:
                    new Gris(MonImg, tempo,"Gris", this);
                    new Contours(MonImg, "Sobel", tempo);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Contours Sobel image1 = " + timeafter + " ms");
                    break;
                case 17:
                    new Gris(MonImg, tempo,"Gris", this);
                    new Contours(MonImg, "Laplace", tempo);
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
