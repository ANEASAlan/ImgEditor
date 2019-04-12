#pragma version(1)
#pragma  rs  java_package_name(com.android.rssample)

rs_allocation inBitmap2;

/*BlendIntermediary() fusionne les pixels si la fusion ne donne pas une valeur dépassant 255*/
    static int BlendIntermediary (int color1, int color2){
        if(color2 == 255){
            return color2;
        }else{
            if(255 < ((color1 << 8) / (255 - color2))){
                return 255;
            }else{
                return (((color1 << 8) / (255 - color2)));
            }
        }
    }

/*blend() prend deux images et demande pixel par pixel à la fonction intermédiaire d'en faire la fusion*/
uchar4 RS_KERNEL blend(uchar4 in, uint32_t x, uint32_t y){
uchar4 p2 = rsGetElementAt_uchar4(inBitmap2, x, y);
const  float4  p1 = rsUnpackColor8888(in);
int g1 = (int) (p1.g * 255);
int g2 = (int) (p2.g);
g2 = BlendIntermediary(g1,g2);
return  rsPackColorTo8888( ((float) g2) / 255.0 , ((float) g2) / 255.0 , ((float) g2) / 255.0 , p2.a);
}

