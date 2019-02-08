package com.example.aaneas.imgeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.widget.ImageView;

import com.android.rssample.*;



public class Contraste  extends MainActivity {

    public Contraste(Bitmap map, ImageView i, String type, Context context){

        if( type == "Dynamique") {
            ContrasteCouleurDynamique(map,i);
        }else if (type == " Egaliseur"){


            ContrasteCouleurEgaliseur(map); /// A FAIRE


        }else if (type == "RS") {
            ContrasteDynamiqueRS(map, context);
        }
    }



    private Bitmap ContrasteDynamiqueRS(Bitmap bmp,Context context) {
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_contrasteD contraste = new ScriptC_contrasteD(rs);

        contraste.forEach_GetMinMaxColor(input);
        contraste.invoke_createlutred();
        contraste.invoke_createlutgreen();
        contraste.invoke_createlutblue();
        contraste.forEach_Final(input,output);


        output.copyTo(bmp);

        input.destroy();
        output.destroy();
        contraste.destroy();
        rs.destroy();

        return bmp;
    }



    /// Fonction intermédiaire pour le contraste ///

    private int[] Couleurlevel(int[] pixels , int height , int width ,char c) {
        int[] Couleurlevel = new int[width * height];
        if (c == 'r'){
            for (int i = 0; i < pixels.length; i++) {
                Couleurlevel[i] = Color.red(pixels[i]);
            }
        }else if(c == 'g'){
            for (int i =0; i < pixels.length; i++){
                Couleurlevel[i] = Color.green(pixels[i]);
            }
        }else if(c == 'b'){
            for (int i =0; i< pixels.length; i++){
                Couleurlevel[i] = Color.blue(pixels[i]);
            }
        }


        return Couleurlevel;

    }




    /// CONTRASTE DYNAMIQUE EN COULEUR ///

    private Bitmap ContrasteCouleurDynamique(Bitmap bmp, ImageView i) {

        Bitmap newimg = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig() );


        int[] pixel = new int[bmp.getHeight() * bmp.getWidth()];
        bmp.getPixels(pixel, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        int[] redtab = Couleurlevel(pixel, bmp.getHeight(), bmp.getWidth(), 'r');
        int[] greentab = Couleurlevel(pixel, bmp.getHeight(), bmp.getWidth(), 'g');
        int[] bluetab = Couleurlevel(pixel, bmp.getHeight(), bmp.getWidth(), 'b');


        //// Méthode Dynamique ////



            int[] histred = new int[256];
            int[] histgreen = new int[256];
            int[] histblue = new int[256];

            /// Créer 3 histogrammes pour chaque couleur ///


            for (int index = 0; index < redtab.length; index++) {
                histred[redtab[index]]++;
            }

            for (int index = 0; index < greentab.length; index++) {
                histgreen[greentab[index]]++;
            }

            for (int index = 0; index < bluetab.length; index++) {
                histblue[bluetab[index]]++;
            }


            //Recupère le max et min de chaque histogramme  //

            int maxred = 0;
            int minred = 0;
            int var = 0;

            while (histred[var] == 0) {
                var++;
            }
            minred = var;
            var = 255;

            while (histred
                    [var] == 0) {
                var--;
            }
            maxred = var;

            //

            int maxgreen = 0;
            int mingreen = 0;
            var = 0;

            while (histgreen[var] == 0) {
                var++;
            }
            mingreen = var;
            var = 255;

            while (histgreen[var] == 0) {
                var--;
            }
            maxgreen = var;


            int maxblue = 0;
            int minblue = 0;
            var = 0;

            while (histblue[var] == 0) {
                var++;
            }
            minblue = var;
            var = 255;

            while (histblue[var] == 0) {
                var--;
            }
            maxblue = var;


            int[] newpixel = new int[bmp.getWidth() * bmp.getHeight()];

            for (int x = 0; x < pixel.length; x++) {

                int redpixel = 255*(redtab[x] - minred)/(maxred - minred);
                int greenpixel = 255*(greentab[x] - mingreen)/(maxgreen - mingreen);
                int bluepixel = 255*(bluetab[x] - minblue)/(maxblue - minblue);

                newpixel[x] = Color.argb(255, redpixel, greenpixel, bluepixel);


              }
            newimg.setPixels(newpixel, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
            i.setImageBitmap(newimg);
            return newimg;

    }


    /// Faire fonction de contraste en egalisant l'histogramme /////


    private void ContrasteCouleurEgaliseur(Bitmap bmp) {

    }








    /////// DIMINUE LE CONTRASTE ( NON DEMANDE PAR LES PROFS !!!)  ///// (a voir si enlever ou pas)


    /*
    private void DiminutionContraste(Bitmap bmp) {


        // Initialise un tableau avec les pixels de l'image //

        int[] pixel = new int[bmp.getHeight() * bmp.getWidth()];
        bmp.getPixels(pixel, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());


        // calcul le niveau de de rouge, vert et bleue de chaque pixel et le place dans  3 tableaux différents//

        int[] redtab = Couleurlevel(pixel, bmp.getHeight(), bmp.getWidth(), 'r');
        int[] greentab = Couleurlevel(pixel, bmp.getHeight(), bmp.getWidth(), 'g');
        int[] bluetab = Couleurlevel(pixel, bmp.getHeight(), bmp.getWidth(), 'b');


        // Ressert l'histogramme (diminituion du constraste)  //


        for (int x = 0; x < redtab.length; x++) {
            if (redtab[x] < 127) {
                redtab[x] = redtab[x] + 15;
            } else if (redtab[x] > 127) {
                redtab[x] = redtab[x] - 15;
            }

        }

        for (int x = 0; x < redtab.length; x++) {
            if (greentab[x] < 127) {
                greentab[x] = greentab[x] + 15;
            } else if (greentab[x] > 127) {
                greentab[x] = greentab[x] - 15;
            }

        }

        for (int x = 0; x < redtab.length; x++) {
            if (bluetab[x] < 127) {
                bluetab[x] = bluetab[x] + 15;
            } else if (bluetab[x] > 127) {
                bluetab[x] = bluetab[x] - 15;
            }

        }

        int[] histred = new int[256];
        int[] histgreen = new int[256];
        int[] histblue = new int[256];

        /// Créer 3 histogrammes pour chaque couleurs ///


        for (int index = 0; index < redtab.length; index++) {
            histred[redtab[index]]++;
        }

        for (int index = 0; index < greentab.length; index++) {
            histgreen[greentab[index]]++;
        }

        for (int index = 0; index < bluetab.length; index++) {
            histblue[bluetab[index]]++;
        }


        int[] newpixel = new int[bmp.getWidth() * bmp.getHeight()];

        for (int x = 0; x < pixel.length; x++) {

            int redpixel = redtab[x];
            int greenpixel = greentab[x];
            int bluepixel = bluetab[x];

            newpixel[x] = Color.argb(1, redpixel, greenpixel, bluepixel);
        }
        bmp.setPixels(newpixel, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());


    }
    */




}