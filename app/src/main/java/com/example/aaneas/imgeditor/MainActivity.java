package com.example.aaneas.imgeditor;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;




public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ImageView Img;
    Button galerie;
    Bitmap MonImg;
    @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);



        /// MENU DEROULANT///
            initImg();

            Spinner myspinner = findViewById(R.id.spinner1);
            ArrayAdapter<String> monadaptater = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Monspinner));
            monadaptater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            myspinner.setAdapter(monadaptater);
            myspinner.setOnItemSelectedListener(this);




        }


        /// recherche img dans la galerie //
        private void initImg() {
        Img = (ImageView) findViewById(R.id.ImgPhoto);
        galerie = (Button) findViewById(R.id.Galerie);
        createButtonGalerie();
        }

        private void createButtonGalerie() {
            galerie.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent galerie = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galerie,1);
                }
            });
        }

        public void onActivityResult (int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode,resultCode,data);

            if (requestCode == 1 && resultCode == RESULT_OK){
                Uri selectedImg = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = this.getContentResolver().query(selectedImg,filePathColumn,null,null,null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgPath = cursor.getString(columnIndex);
                cursor.close();
                MonImg = BitmapFactory.decodeFile(imgPath);

                Img.setImageBitmap(MonImg);

            }
        }




        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            ImageView tempo = findViewById(R.id.ImgPhoto);

            Long time = System.currentTimeMillis();


            // matrice pour flou gaussien
            int [][] gaussien = new int[][]{
                    {1,2,3,2,1},
                    {2,6,8,6,2},
                    {3,8,10,8,3},
                    {2,6,8,6,2},
                    {1,2,3,2,1}
            };

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
                    new Couleurs(MonImg,tempo,"Teinte",this);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Coloriser image1= " + timeafter + " ms");
                    break;
                case 4:
                    new Couleurs(MonImg,tempo,"TeinteRS",this);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Coloriser RS image1= " + timeafter + " ms");
                    break;
                case 5:
                    new Couleurs(MonImg,tempo,"ConserveRouge",this);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Conserve image1= " + timeafter + " ms");
                    break;
                case 6:
                    new Couleurs(MonImg,tempo,"ConserveRS",this);
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
                    new Contraste(MonImg,tempo,"RS",this);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Contraste image2= " + timeafter + " ms");
                    break;

                case 10:
                    new Flous(MonImg, "Flou basique",this, tempo, 10,gaussien); // (n taille du masque)
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution flou image1= " + timeafter + " ms");
                    break;

                case 11:
                    new Flous(MonImg, "Flou gaussien",this, tempo, 0, gaussien);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution flou gaussien image2= " + timeafter + " ms");
                    break;
                case 12:
                    new Luminosite(MonImg, "Luminosite", this);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution Luminosite image1 = " + timeafter + " ms");
                    break;
                case 13:
                    new Luminosite(MonImg, "LuminositeRS", this);
                    timeafter = System.currentTimeMillis() - time;
                    System.out.println( "temps d'execution LuminositeRS image1 = " + timeafter + " ms");
                    break;

            }




        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }


    }
