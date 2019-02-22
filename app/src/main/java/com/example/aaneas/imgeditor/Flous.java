package com.example.aaneas.imgeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;


public class Flous extends MainActivity{

    public Flous(Bitmap map ,String type, Context context, ImageView i, int n, boolean gauss){
        if (type == "Flou RS"){

        }else if (type == "Flou"){
            Flougaussien(map, i, gauss);
        }
    }


    /// Flou gaussien ////

    private Bitmap Flougaussien(Bitmap bmp, ImageView i, boolean gaussien){
        int [][] matrix;
        Bitmap n = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig() );

        if (gaussien){
            matrix = new int[][]{
                    {1,2,3,2,1},
                    {2,6,8,6,2},
                    {3,8,10,8,3},
                    {2,6,8,6,2},
                    {1,2,3,2,1}
            };
        }else{


            matrix = new int[][]{
                    {1,1,1,1,1},
                    {1,1,1,1,1},
                    {1,1,1,1,1},
                    {1,1,1,1,1},
                    {1,1,1,1,1}
            };
        }

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
                int alpha = 0;
                for (int x2 = 0; x2 < matrix.length; x2++) {
                    for (int y2 = 0; y2 < matrix.length; y2++) {
                        int e = pixel[(x - matrix.length/2)+x2 + ((y - matrix.length/2)+y2)*bmp.getWidth()];
                        a = a + Color.red(e) * matrix[x2][y2];
                        b = b + Color.green(e)  * matrix[x2][y2];
                        c = c + Color.blue(e)  * matrix[x2][y2];
                        alpha = Color.alpha(e);
                    }
                }

                newpixel[x + (y*n.getWidth())] = Color.argb(alpha,a/div,b/div,c/div);



            }
        }
        n.setPixels(newpixel,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
        i.setImageBitmap(n);
        return n;

    }





}
