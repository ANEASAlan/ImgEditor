#pragma  version (1)
#pragma  rs  java_package_name(com.android.rssample)

static  const  float4  weight = {0.299f, 0.587f, 0.114f, 0.0f};


double rand;

//Cette fonction permet de conserver une couleur, elle contient un paramètre rand1 qui
//indique la valeur qu'elle doit conserver et grise tout le reste

uchar4 RS_KERNEL colorKeep(uchar4 in){
float4  pixelf = rsUnpackColor8888(in);
int r = pixelf.r * 255;
int g = pixelf.g * 255;
int b = pixelf.b * 255;
if((rand < 0.33 && (r < 80 || g > 80 || b > 100)) || ((rand >= 0.33 && rand < 0.66) && (r > 120 || g < 80 || b > 120)) || (rand >= 0.66 && (r > 120 || g > 170 || b < 100)))
            {
                float avgcolor = ((0.3 * pixelf.r) + (0.59 * pixelf.g) + (0.11 * pixelf.b));
                pixelf.r = avgcolor;
                pixelf.g = avgcolor;
                pixelf.b = avgcolor;
            }
    return  rsPackColorTo8888(pixelf.r , pixelf.g , pixelf.b , pixelf.a);
}