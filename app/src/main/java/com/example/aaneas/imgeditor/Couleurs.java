package com.example.aaneas.imgeditor;

import android.support.v7.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;

import com.android.rssample.*;

public class Couleurs extends AppCompatActivity {

    public Couleurs(Bitmap map, String type){
        if(type == "Teinte"){
            Coloriser(map);
        }else if(type == "TeinteRS"){ //// MARCHE PAS
            ColoriserRS(map);
        }else if(type == "ConserveRouge"){
            Conserve(map, "red");
        }else if(type == "ConserveRS"){ /// MARCHE PAS
            ConserveRS(map);
        }
    }




    /// CHANGER LA TEINTE D'UNE IMAGE ///

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
