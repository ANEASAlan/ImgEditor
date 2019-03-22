#pragma version(1)
#pragma rs java_package_name(com.android.rssample)

#include "rs_debug.rsh"

bool gaussian;
double div;
int * matrix;
int matrixLength;
//int reds[];
//int greens[];
//int blues[];
int width;
int height;

uchar4 RS_KERNEL floubasique(uchar4 in, uint32_t x, uint32_t y){
const  float4  pixelf = rsUnpackColor8888(in);
            double red = pixelf.r;
            double green = pixelf.g;
            double blue = pixelf.b;
    //penser à faire bitmap temporaire ?
    if (div != 0){

        /*int ** matrix;
        if (gaussian) {
            matrix = {
                    {1, 2, 3, 2, 1},
                    {2, 6, 8, 6, 2},
                    {3, 8, 10, 8, 3},
                    {2, 6, 8, 6, 2},
                    {1, 2, 3, 2, 1}
            };
            matrixLength = 5;
        } else {
            matrix = {
                    {1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1}
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
            };
            matrixLength = 6;
        }*/

        int na = matrixLength / 2;
        /*rsDebug("matrix :",matrix[0]);
        rsDebug("y :",y);
        rsDebug("x :",x);
        rsDebug("na :",na);
        rsDebug("width - na - 1 :",width - na - 1);
        rsDebug("height - na - 1 :",height - na - 1);
        rsDebug("x good :",x > na && x < width - na - 1);
        rsDebug("y good :",y > na && y < height - na - 1);*/
        //if((x > na*width) && (x < width*height - na*width) && (x % width >= na) && (x % width < width - na)){
        if(x > na && x < width - na - 1 && y > na && y < height - na - 1){
            /// Va chercher les valeurs r g b des pixels autours //
            //rsDebug("milieu2 :",pixelf.r);
            red = 0;
            green = 0;
            blue = 0;

            for (int x2 = 0; x2 < matrixLength; x2++) {
                for (int y2 = 0; y2 < matrixLength; y2++) {
                    uchar4 tmp_in = in - na - (na*width) + x2 + (y2*width);
                    const  float4  tmp_pixelf = rsUnpackColor8888(tmp_in);
                    red = red + (tmp_pixelf.r * matrix[x2+matrixLength*y2]);
                    //rsDebug("tmp r :",tmp_pixelf.r);
                    //rsDebug("matrix :",matrix[x2+matrixLength*y2]);
                    green = green + (tmp_pixelf.g * matrix[x2+matrixLength*y2]);
                    blue = blue + (tmp_pixelf.b * matrix[x2+matrixLength*y2]);
                }
            }

//rsDebug("div :", div);
//rsDebug("a fin :", red);
            const  float4  pixelf = rsUnpackColor8888(in);
            red = (red / div);
            green = (green / div);
            blue = (blue / div);
            //rsDebug("fin :",pixelf.r);
        }
    }
    return rsPackColorTo8888(red , green , blue , pixelf.a);
}
