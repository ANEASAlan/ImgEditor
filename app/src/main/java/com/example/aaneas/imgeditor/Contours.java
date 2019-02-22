package com.example.aaneas.imgeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;


public class Contours extends MainActivity{

    public Contours(Bitmap map , String type, Context context, ImageView i, int n, int [][] matrix){
        if (type == "Sobel"){
            //Floubasique(map,i,n);
        }else if (type == "Laplace"){
            //Flougaussien(map,matrix, i);
        }
        int r = 0;
        int g = 0;
        int b = 0;
    }

    void addColor(int r, int g, int b, int x, int y, double width, int [] pixel, int sobel){
        int c = pixel[x + (y* ((int)width))];
        r += Color.red(c) * sobel;
        g += Color.green(c) * sobel;
        b += Color.blue(c) * sobel;
    }

    /// Flou Lisse (n = taille du masque) ///

    private Bitmap ContoursSobel(Bitmap bmp, ImageView i){

        Bitmap newimg = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig() );

        //int div = (2*n +1)*(2*n+1);

        int [] pixel = new int[bmp.getWidth()*bmp.getHeight()];
        int [] newpixel = new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixel,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());

        for (int x = 1; x < newimg.getWidth()-1; x++){
            for (int y = 1; y < newimg.getHeight()-1; y++ ){

                /// va chercher les valeurs r g b des pixels autours //
                int r = 0;
                int g = 0;
                int b = 0;
                addColor( r,g, b,x-1, y-1, bmp.getWidth(), pixel, -1);

               /// newpixel[x + (y*newimg.getWidth())] = Color.argb(255,r/div,g/div,b/div);



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
