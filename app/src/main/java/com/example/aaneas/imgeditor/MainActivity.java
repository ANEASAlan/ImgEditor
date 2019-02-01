package com.example.aaneas.imgeditor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;




public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            /// MENU DEROULANT///


            Spinner myspinner = findViewById(R.id.spinner1);
            ArrayAdapter<String> monadaptater = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Monspinner));
            monadaptater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            myspinner.setAdapter(monadaptater);
            myspinner.setOnItemSelectedListener(this);



        }



        
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            boolean swap = true;
            TextView tv = (TextView) findViewById(R.id.taille);
            ImageView i = (ImageView) findViewById(R.id.imageView5);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            Bitmap image1 = BitmapFactory.decodeResource(getResources(), R.drawable.index, options);

            ImageView i2 = (ImageView) findViewById(R.id.imageView);
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            options2.inMutable = true;
            Bitmap image2 = BitmapFactory.decodeResource(getResources(), R.drawable.index2, options);
            Long time = System.currentTimeMillis();

            switch (position) {
                case 0:
                    tv.setText(" ");
                    break;
                case 1:
                    new Gris(image1, "Gris");
                    long timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Gris image1 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Gris image1 = " + timeafter + " ms");

                    break;
                case 2:
                    new Gris(image2, "Gris");
                    timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Gris image2 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Gris image2 = " + timeafter + " ms");

                    break;
                case 3:
                    new Gris(image1, "RS");
                    timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Gris RS image1 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Gris RS image 1= " + timeafter + " ms");

                    break;
                case 4:
                    new Gris(image2, "RS");
                    timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Gris RS image2 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Gris RS image 2= " + timeafter + " ms");

                    break;
                case 5:
                    new Couleurs(image1,"Teinte");
                    timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Coloriser image1 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Coloriser image1= " + timeafter + " ms");
                    break;
                case 6:
                    new Couleurs(image2,"Teinte");
                    timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Coloriser image2 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Coloriser image2= " + timeafter + " ms");
                    break;
                case 7:
                    new Couleurs(image1,"TeinteRS");
                    timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Coloriser RS image1 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Coloriser RS image1= " + timeafter + " ms");
                    break;
                case 8:
                    new Couleurs(image2,"TeinteRS");
                    timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Coloriser RS image2 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Coloriser RS image2= " + timeafter + " ms");
                    break;
                case 9:
                    new Couleurs(image1,"ConserveRouge");
                    timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Conserve image1 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Conserve image1= " + timeafter + " ms");
                    break;
                case 10:
                    new Couleurs(image2,"ConserveRouge");
                    timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Conserve image2 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Conserve image2= " + timeafter + " ms");
                    break;
                case 11:
                    new Couleurs(image1,"ConserveRS");
                    timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Conserve RS image1 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Conserve RS image1= " + timeafter + " ms");
                    break;
                case 12:
                    new Couleurs(image1,"ConserveRS");
                    timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Conserve RS image2 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Conserve RS image2= " + timeafter + " ms");
                    break;
                case 13:
                    new Contraste(image2, "Dynamique");
                    timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Contraste image2 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Contraste image2= " + timeafter + " ms");
                    break;
                case 14:
                    new Contraste(image2, "Egaliseur");
                    timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Contraste image2 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Contraste image2= " + timeafter + " ms");
                    break;
                case 15:
                    new Contraste(image2, "RS");
                    timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Contraste image2 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Contraste image2= " + timeafter + " ms");
                    break;

            }
            if(swap ==true) {
                i.setImageBitmap(image1);
            }
            i2.setImageBitmap(image2);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }


    }
