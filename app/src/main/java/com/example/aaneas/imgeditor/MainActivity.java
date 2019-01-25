package com.example.aaneas.imgeditor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.android.rssample.*;



public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);



            ImageView i2 = (ImageView) findViewById(R.id.imageView);
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            options2.inMutable = true;
            Bitmap image2 = BitmapFactory.decodeResource(getResources(), R.drawable.index2, options2);

            ImageView i = (ImageView) findViewById(R.id.imageView5);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            Bitmap image1 = BitmapFactory.decodeResource(getResources(), R.drawable.index, options);
            i.setImageBitmap(image1);

        //test//
        /*
        int largeur = options.outWidth;  /// * 2.6
        int hauteur = options.outHeight;   /// *2.6
        System.out.println("largeur:" + largeur + " hauteur : " + hauteur); */


            /// MENU DEROULANT///


            Spinner myspinner = findViewById(R.id.spinner1);
            ArrayAdapter<String> monadaptater = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Monspinner));
            monadaptater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            myspinner.setAdapter(monadaptater);
            myspinner.setOnItemSelectedListener(this);

            /////

        }

        // CONFIGURATION MENU ///



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

            int [][] matrix = new int[][]{
                    {1,2,3,2,1},
                    {2,6,8,6,2},
                    {3,8,10,8,3},
                    {2,6,8,6,2},
                    {1,2,3,2,1}
            };

            switch (position) {
                case 0:
                    tv.setText(" ");
                    break;
                case 1:
                    ToGrey(image1);
                    long timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Gris image1 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Gris image1 = " + timeafter + " ms");

                    break;
                case 2:
                    ToGrey(image2);
                    timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Gris image2 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Gris image2 = " + timeafter + " ms");

                    break;
                case 3:
                    toGreyRS(image1);
                    timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Gris RS image1 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Gris RS image 1= " + timeafter + " ms");

                    break;
                case 4:
                    toGreyRS(image2);
                    timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Gris RS image2 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Gris RS image 2= " + timeafter + " ms");

                    break;
                case 5:
                    Coloriser(image1);
                    timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Coloriser image1 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Coloriser image1= " + timeafter + " ms");
                    break;
                case 6:
                    Coloriser(image2);
                    timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Coloriser image2 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Coloriser image2= " + timeafter + " ms");
                    break;
                case 7:
                    ColoriserRS(image1);
                    timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Coloriser RS image1 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Coloriser RS image1= " + timeafter + " ms");
                    break;
                case 8:
                    ColoriserRS(image2);
                    timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Coloriser RS image2 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Coloriser RS image2= " + timeafter + " ms");
                    break;
                case 9:
                    Conserve(image1, "red");
                    timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Conserve image1 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Conserve image1= " + timeafter + " ms");
                    break;
                case 10:
                    Conserve(image2, "green");
                    timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Conserve image2 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Conserve image2= " + timeafter + " ms");
                    break;
                case 11:
                    ConserveRS(image1);
                    timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Conserve RS image1 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Conserve RS image1= " + timeafter + " ms");
                    break;
                case 12:
                    ConserveRS(image2);
                    timeafter = System.currentTimeMillis() - time;
                    tv.setText("temps d'execution Conserve RS image2 = " + timeafter + " ms");
                    System.out.println( "temps d'execution Conserve RS image2= " + timeafter + " ms");
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



        /// RENDRE UNE IMAGE GRISE///


        ///JAVA VERSION///


        protected void ToGrey(Bitmap bmp) {


            int [] pixel = new int[bmp.getWidth()*bmp.getHeight()];
            int [] greytab = new int[bmp.getWidth()*bmp.getHeight()];
            bmp.getPixels(pixel,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());

            for (int i =0; i < pixel.length ; i++){
                int Grey = (int)( 0.3 * Color.red(pixel[i]) +0.59 *Color.green(pixel[i])+0.11* Color.blue(pixel[i]));
                greytab[i] = Color.argb(1,Grey,Grey,Grey);
            }
            bmp.setPixels(greytab,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());



            /// Version plus coûteuse ///


        /*
        for (int x = 0; x < bmp.getWidth(); x++) {
            for (int y = 0; y < bmp.getHeight(); y++) {
                int a = bmp.getPixel(x,y);
                double Grey = 0.3 * Color.red(a) + 0.59 * Color.green(a) + 0.11 * Color.blue(a);
                bmp.setpixel(x,y,(Grey,Grey,Grey));
            }
        }*/


            ////

        }


        ///RENDERSCRIPT VERSION///


        private void toGreyRS(Bitmap bmp) {
            RenderScript rs = RenderScript.create(this);

            Allocation input = Allocation.createFromBitmap(rs, bmp);
            Allocation output = Allocation.createTyped(rs, input.getType());

            ScriptC_grey Grey = new ScriptC_grey(rs);

            Grey.forEach_toGrey(input, output);
            output.copyTo(bmp);

            input.destroy();
            output.destroy();
            Grey.destroy();
            rs.destroy();

        }



        /// CHANGER LA TEINTE D'UNE IMAGE ///


        ///JAVA VERSION///


        protected void Coloriser(Bitmap bmp) {
            float random = (float) (Math.random() * 360);
            float HSV[] = {0, 0, 0};

            int [] pixel = new int[bmp.getWidth()*bmp.getHeight()];
            int [] color = new int[bmp.getWidth()*bmp.getHeight()];
            bmp.getPixels(pixel,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());

            for (int i =0; i < pixel.length ; i++){
                int a = pixel[i];

                /// Je convertis en HSV puis réinsert le pixel en "Color" ///



                Color.RGBToHSV(Color.green(a), Color.red(a), Color.blue(a), HSV);
                HSV[0] = random;
                color[i] = Color.HSVToColor(HSV);


                ///
            }
            bmp.setPixels(color,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());



            // Version plus coûteuse ///



        /*
        for (int x = 0; x < bmp.getWidth(); x++) {
            for (int y = 0; y < bmp.getHeight(); y++) {
                int a = bmp.getPixel(x, y);

                Color.RGBToHSV(Color.green(a), Color.red(a), Color.blue(a), HSV);
                HSV[0] = random;
                bmp.setPixel(x, y, Color.HSVToColor(HSV));
            }
        }
        */

        }


        /// RENDERSCRIPT COLORISER VERSION///


        private void ColoriserRS(Bitmap bmp) {
            RenderScript rs = RenderScript.create(this);

            Allocation input = Allocation.createFromBitmap(rs, bmp);
            Allocation output = Allocation.createTyped(rs, input.getType());

            ScriptC_teinte ColorScript = new ScriptC_teinte(rs);

            // J'envoie un nombre aléatoire en paramètre ///


            int rand1 =(int) (Math.random() * 360) ;
            ColorScript.set_rand1(rand1);


            ///

            ColorScript.forEach_toColor(input, output);
            output.copyTo(bmp);

            input.destroy();
            output.destroy();
            ColorScript.destroy();
            rs.destroy();

        }




        /// CONSERVER UNE COULEUR ///



        /// JAVA VERSION ///

        private void Conserve(Bitmap bmp, String color) {

            int [] pixel = new int[bmp.getWidth()*bmp.getHeight()];
            int [] colortab = new int[bmp.getWidth()*bmp.getHeight()];
            bmp.getPixels(pixel,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());



            for (int y = 0; y < pixel.length; y++) {
                int a = pixel[y];

                /// Conserve uniquement le rouge ///

                if ( color == "red"){
                    double Grey = 0.3 * Color.red(a) + 0.59 * Color.green(a) + 0.11 * Color.blue(a);
                    if (Color.green(a)<= 100 && Color.blue(a) <= 100 && Color.red(a)> Color.green(a) && Color.red(a)>Color.blue(a)){
                        colortab[y] = Color.argb(1,Color.red(a), Color.green(a) ,Color.blue(a) );
                    }else{
                        colortab[y] = Color.argb(1,(int) Grey, (int) Grey, (int) Grey);
                    }

                    /// Conserve uniquement le vert ///


                }else if( color == "green"){
                    double Grey = 0.3 * Color.red(a) + 0.59 * Color.green(a) + 0.11 * Color.blue(a);
                    if (Color.blue(a)<= 200 && Color.red(a) <= 200 && Color.green(a)> Color.red(a) && Color.green(a)>Color.blue(a)){
                        colortab[y] = Color.argb(1,Color.red(a), Color.green(a) ,Color.blue(a) );
                    }else{
                        colortab[y] = Color.argb(1,(int) Grey, (int) Grey, (int) Grey);
                    }
                    /// Conserve uniquement le bleu ///


                }else if( color == "blue"){
                    double Grey = 0.3 * Color.red(a) + 0.59 * Color.green(a) + 0.11 * Color.blue(a);
                    if (Color.green(a)<= 200 && Color.red(a) <= 200 && Color.blue(a)> Color.red(a) && Color.blue(a)>Color.green(a)){
                        colortab[y] = Color.argb(1,Color.red(a), Color.green(a) ,Color.blue(a) );
                    }else{
                        colortab[y] = Color.argb(1,(int) Grey, (int) Grey, (int) Grey);
                    }

                }

            }
            bmp.setPixels(colortab,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());




        }

        /// RENDERSCRIPT VERSION ///


        private void ConserveRS(Bitmap bmp) {
            RenderScript rs = RenderScript.create(this);

            Allocation input = Allocation.createFromBitmap(rs, bmp);
            Allocation output = Allocation.createTyped(rs, input.getType());

            ScriptC_conserve Conserve = new ScriptC_conserve(rs);

            Conserve.forEach_toConserve(input, output);
            output.copyTo(bmp);

            input.destroy();
            output.destroy();
            Conserve.destroy();
            rs.destroy();

        }

    }
