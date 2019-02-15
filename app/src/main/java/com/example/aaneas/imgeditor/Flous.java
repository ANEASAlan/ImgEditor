package com.example.aaneas.imgeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;


public class Flous extends MainActivity{

    public Flous(Bitmap map ,String type, Context context, ImageView i, int n, int [][] maxtrix){
        if (type == "Flou basique"){
            Floubasique(map,i,n);
        }else if (type == "Flou gaussien"){
            Flougaussien(map,maxtrix, i);
        }
    }


    /// Flou Lisse (n = taille du masque) ///

    private Bitmap Floubasique(Bitmap bmp, ImageView i, int n  ){

        Bitmap newimg = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig() );

        int div = (2*n +1)*(2*n+1);

        int [] pixel = new int[bmp.getWidth()*bmp.getHeight()];
        int [] newpixel = new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixel,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());

        for (int x = n; x < newimg.getWidth()-n; x++){
            for (int y = n; y < newimg.getHeight()-n; y++ ){

                /// va chercher les valeurs r g b des pixels autours //


                int r =0;
                int g = 0;
                int b = 0;
                for (int x2 = x -n; x2 <= x+n; x2++) {
                    for (int y2 = y - n; y2 <= y + n; y2++) {
                        int e = pixel[x2 + (y2*bmp.getWidth())];
                        r = r + Color.red(e);
                        g = g + Color.green(e);
                        b = b + Color.blue(e);

                    }
                }

                newpixel[x + (y*newimg.getWidth())] = Color.argb(255,r/div,g/div,b/div);



            }
        }
        newimg.setPixels(newpixel,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
        i.setImageBitmap(newimg);
        return newimg;

    }


    /// Flou gaussien ////

    private Bitmap Flougaussien(Bitmap bmp, int [][] matrix, ImageView i){

        Bitmap n = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig() );

        int na = matrix.length/2;
        int div = 0;

        // Parcours la matrice ///


        for (int e =0;  e< matrix.length; e++){
            for (int f =0;  f< matrix.length; f++) {
                div += matrix[e][f];
            }
        }



        int [] pixel = new int[bmp.getWidth()*bmp.getHeight()];
        int [] newpixel = new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixel,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());



        for (int x = na; x < n.getWidth()-na; x++){
            for (int y = na; y < n.getHeight()-na; y++ ){

                /// Va chercher les valeurs r g b des pixels autours //


                int a =0;
                int b = 0;
                int c = 0;
                for (int x2 = 0; x2 < matrix.length; x2++) {
                    for (int y2 = 0; y2 < matrix.length; y2++) {
                        int e = pixel[(x - matrix.length/2)+x2 + ((y - matrix.length/2)+y2)*bmp.getWidth()];
                        a = a + Color.red(e) * matrix[x2][y2];
                        b = b + Color.green(e)  * matrix[x2][y2];
                        c = c + Color.blue(e)  * matrix[x2][y2];

                    }
                }

                newpixel[x + (y*n.getWidth())] = Color.argb(255,a/div,b/div,c/div);



            }
        }
        n.setPixels(newpixel,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
        i.setImageBitmap(n);
        return n;

    }





}
