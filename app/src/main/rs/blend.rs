#pragma version(1)
#pragma  rs  java_package_name(com.android.rssample)

rs_allocation inBitmap2;

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


uchar4 RS_KERNEL blend(uchar4 in, uint32_t x, uint32_t y){
uchar4 p2 = rsGetElementAt_uchar4(inBitmap2, x, y);
const  float4  p1 = rsUnpackColor8888(in);
int r1 = (int) (p1.r * 255);
int g1 = (int) (p1.g * 255);
int b1 = (int) (p1.b * 255);
int r2 = (int) (p2.r);
int g2 = (int) (p2.g);
int b2 = (int) (p2.b);
r2 = BlendIntermediary(r1,r2);
g2 = BlendIntermediary(g1,g2);
b2 = BlendIntermediary(b1,b2);
return  rsPackColorTo8888( ((float) r2) / 255.0 , ((float) g2) / 255.0 , ((float) b2) / 255.0 , p2.a);
}

