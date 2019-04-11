package com.example.aaneas.imgeditor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    static ImageView Img;
    static ImageView Rainbow;


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

    boolean render_script = false;

    @Override
        protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initImg();


        spinnerJava = findViewById(R.id.spinner1);
        spinnerRS = findViewById(R.id.spinner2);
        LumiBar = findViewById(R.id.ColorR);
        Rainbow = findViewById(R.id.Rainbow);

        Save.setVisibility(View.INVISIBLE);
        LumiBar.setVisibility(View.INVISIBLE);
        Rainbow.setVisibility(View.INVISIBLE);
        spinnerJava.setVisibility(View.INVISIBLE);
        spinnerRS.setVisibility(View.INVISIBLE);
        Undo.setVisibility(View.INVISIBLE);
        RS_Button.setVisibility(View.INVISIBLE);

        /*Initialisation des deux menus déroulants*/


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

            /*On limite le zoom entre 0.1 fois et 5.0 fois la taille de l'image initiale*/
            ScaleFactor = Math.max(0.1f, Math.min(ScaleFactor, 5.0f));
            Img.setScaleX(ScaleFactor);
            Img.setScaleY(ScaleFactor);
            return true;
        }
     }

        /*initImg() va initialiser tous les boutons de l'application ainsi que les permissions d'accés à la galerie et appareil photo*/


        @SuppressLint("ClickableViewAccessibility")
        private void initImg() {
        checkPermission();
        Img = (PhotoView) findViewById(R.id.photo_view);
        RS_Button = findViewById(R.id.RS);
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

        /*createButtonRS() va créer un bouton RS permettant de changer de menu deroulant,
          un menu déroulant contenant toutes les fonctions java */

        private void createButtonRS(){
            RS_Button.setChecked(false);
            RS_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    render_script = !render_script;
                    if (isChecked){
                        spinnerRS.setVisibility(View.VISIBLE);
                        spinnerJava.setVisibility(View.INVISIBLE);

                    }else{

                        spinnerRS.setVisibility(View.INVISIBLE);
                        spinnerJava.setVisibility(View.VISIBLE);

                    }

                    if(LumiBar.getVisibility()==View.VISIBLE){
                        LumiBar.setVisibility(View.INVISIBLE);
                        //RadioColor.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }



        /*createButtonSave() va créer le bouton pour sauvegarder l'image dans la galerie*/

        private void createButtonSave(){
            Save.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MediaStore.Images.Media.insertImage(getContentResolver(),MonImg,"nom image","description");
                }
            });
        }


        /*createImageFile() est une fonction intermédiaire pour l'enregistrement d'un photo, elle créer un fichier image*/

        String currentPhotoPath;
        private File createImageFile() throws IOException {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
            currentPhotoPath = image.getAbsolutePath();
            return image;
        }


        /*checkPermission() verifie les permissions de l'appli */

    private static final int REQUEST_RUNTIME_PERMISSION = 1;
        void checkPermission() {
            final String permission = Manifest.permission.CAMERA;
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


        /*onRequestPermissionsResult() réagit en fonction de la fonction checkPermissions() ci-dessus*/

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode) {
                case REQUEST_RUNTIME_PERMISSION:
                    final int numOfRequest = grantResults.length;
                    final boolean isGranted = numOfRequest == 1
                            && PackageManager.PERMISSION_GRANTED == grantResults[numOfRequest - 1];
                    if (isGranted) {
                        return;
                    } else {
                    }
                    break;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }

        }

        /*createButtonPhoto() créer un bouton permettant de prendre une photo et l'enregistrer dans la galerie*/

        Uri photoUri;
        private  void createButtonPhoto(){
            AppPhoto.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                        }
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

        /*createButtonGalerie() créer un bouton permettant d'aller chercher une image dans la galerie*/

        private void createButtonGalerie() {
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


        /*createButtonUnda() est un bouton retour, on peut faire seulement 3 fois retour max*/

        private void createButtonUndo() {
            Undo.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(savedImgIndex!=0){
                        savedImgIndex--;
                        MonImg=Bitmap.createBitmap(derniereImage());
                        Img.setImageBitmap(MonImg);
                    }
                }
            });
        }


        /*onActivityResult() interagit en fonction de si on va dans la galerie ou l'appareil photo en fonction de "requestCode",
         cette fonction set le Bitmap et met les visibilitées à jour*/

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


        /*saveImg() permet de stocker les images pour le Undo*/

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


        /*onItemSelected() intereagit avec strings.xml (les noms des menus déroulants), il contient un switch pour chacune des deux spinners.
        Les fonctions sont appélées dans les cases correspondants au nom de leur fonction*/

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            LumiBar.setVisibility(View.INVISIBLE);
            Rainbow.setVisibility(View.INVISIBLE);
            if(position!=0){
                saveImg(MonImg);
            }
            if(LumiBar.getProgress()!=0){
                LumiBar.setProgress(0);
            }

            /* switch JAVA */

            if (parent.getId() == R.id.spinner1) {
                switch (position) {
                    case 0:
                        savedImgIndex=0;
                        if(MonImg!=null && !MonImg.equals(BasicImg)){
                            MonImg=Bitmap.createBitmap(BasicImg);
                            Img.setImageBitmap(MonImg);
                        }
                        break;
                    case 1:
                        MonImg=Bitmap.createBitmap(Grey.toGrey(MonImg));
                        break;
                    case 2:
                        LumiBar.setVisibility(View.VISIBLE);
                        Rainbow.setVisibility(View.VISIBLE);
                        Color();
                        break;
                    case 3:
                        LumiBar.setVisibility(View.VISIBLE);
                        Rainbow.setVisibility(View.VISIBLE);
                        Conserve();
                        break;
                    case 4:
                        MonImg=Bitmap.createBitmap(Contrast.ContrastDynamic(MonImg));
                        break;
                    case 5:
                        MonImg=Bitmap.createBitmap(Contrast.ContrastAverage(MonImg));
                        break;
                    case 6:
                        MonImg=Bitmap.createBitmap(Blur.GaussianBlur(MonImg,false)); //flou égalisateur / moyenneur
                        break;
                    case 7:
                        MonImg=Bitmap.createBitmap(Blur.GaussianBlur(MonImg,true)); // flou gaussien
                        break;
                    case 8:
                        LumiBar.setVisibility(View.VISIBLE);
                        LumiColor();
                        break;
                    case 9:
                        MonImg=Bitmap.createBitmap(Edges.SobelEdges(Bitmap.createBitmap(Grey.toGrey(MonImg))));
                        break;
                    case 10:
                        MonImg=Bitmap.createBitmap(Edges.LaplaceEdges(Bitmap.createBitmap(Grey.toGrey(MonImg))));
                        break;
                    case 11:
                        Bitmap Greyscale = Bitmap.createBitmap(Grey.toGrey(MonImg));
                        Bitmap InvertedGrey = Bitmap.createBitmap(Colors.invert(Greyscale));
                        Bitmap Blurred = Bitmap.createBitmap(Blur.GaussianBlur(InvertedGrey,true));
                        MonImg=Bitmap.createBitmap(Pencil.BlendColorDodge(Blurred,Greyscale));
                        break;
                    case 12:
                        MonImg = Bitmap.createBitmap(Edges.LaplaceEdges(Bitmap.createBitmap(Grey.toGrey(MonImg))));
                        MonImg = Bitmap.createBitmap(Colors.invert(MonImg));
                        break;
                    case 13:
                        MonImg = Bitmap.createBitmap(Edges.SobelEdges(Bitmap.createBitmap(Grey.toGrey(MonImg))));
                        MonImg = Bitmap.createBitmap(Colors.invert(MonImg));
                        break;
                    case 14:
                        MonImg = Bitmap.createBitmap(Colors.invert(MonImg));
                        break;
                    }
                /* switch RS */
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
                            MonImg=Bitmap.createBitmap(Grey.toGreyRS(MonImg, this));
                            break;
                        case 2:
                            MonImg=Bitmap.createBitmap(Colors.ColoriserRS(MonImg, this));
                            break;
                        case 3:
                            MonImg=Bitmap.createBitmap(Colors.ConserveRS(MonImg,this));
                            break;
                        case 4:
                            MonImg=Bitmap.createBitmap(Contrast.ContrastDynamicRS(MonImg,this));
                            break;
                        case 5:
                            MonImg=Bitmap.createBitmap(Contrast.ContrastAverageRS(MonImg,this));
                            break;
                        case 6:
                            MonImg=Bitmap.createBitmap(Blur.BlurRS(MonImg,this ));
                            break;
                        case 7:
                            LumiBar.setVisibility(View.VISIBLE);
                            LumiColor();
                            break;
                        case 8:
                            Bitmap Greyscale = Bitmap.createBitmap(Grey.toGreyRS(MonImg,MainActivity.this));
                            Bitmap InvertedGrey = Bitmap.createBitmap(Colors.invertRS(Greyscale,this));
                            Bitmap Blurred = Bitmap.createBitmap(Blur.BlurRS(InvertedGrey,this));
                            MonImg=Bitmap.createBitmap(Pencil.BlendRS(Blurred,Greyscale,this));
                            break;
                        case 9:
                            MonImg = Bitmap.createBitmap(Edges.LaplaceEdges(Bitmap.createBitmap(Grey.toGreyRS(MonImg, this))));
                            MonImg = Bitmap.createBitmap(Colors.invertRS(MonImg,this));
                            break;
                        case 10:
                            MonImg = Bitmap.createBitmap(Edges.SobelEdges(Bitmap.createBitmap(Grey.toGreyRS(MonImg, this))));
                            MonImg = Bitmap.createBitmap(Colors.invertRS(MonImg,this));
                            break;
                        case 11:
                            MonImg = Bitmap.createBitmap(Colors.invertRS(MonImg,this));
                            break;
                        case 12:
                            MonImg = Bitmap.createBitmap(Edges.contoursRS(Bitmap.createBitmap(Grey.toGreyRS(MonImg,this)),this));
                    }
                }
            }


        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }

        public Bitmap derniereImage(){
            return savedImgIndex==0?BasicImg:savedImg[savedImgIndex-1];
        }

        //Créer une barre de couleurs qui change la teinte lorsque l'on clique dessus
        public void Color(){
            LumiBar.setOnSeekBarChangeListener(
                    new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int scale, boolean b) {
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            MonImg=Colors.changerCouleur(derniereImage(), LumiBar.getProgress());
                        }
                    }
                );
        }

        public void LumiColor(){
            LumiBar.setOnSeekBarChangeListener(
                    new SeekBar.OnSeekBarChangeListener() {
                        Bitmap n=Bitmap.createBitmap(derniereImage());
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int scale, boolean b) {
                            if(savedImgIndex==0){
                                n=Bitmap.createBitmap(BasicImg);
                            }else{
                                n=Bitmap.createBitmap(savedImg[savedImgIndex-1]);
                            }
                            if (!render_script) {
                                n= Lighting.changeBrightness(n,scale);
                            } else {
                                n= Lighting.changeBrightnessRS(n,MainActivity.this,scale);

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

    //Créer une barre de couleur qui conserve une couleur lorsque l'on clique dessus
    public void Conserve(){
        LumiBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int scale, boolean b) {
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        MonImg=Colors.Conserve(derniereImage(), LumiBar.getProgress());
                    }
                }
        );
    }

    }
