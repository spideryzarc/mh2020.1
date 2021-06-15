#include <stdio.h>
#include <limits.h>
#include <float.h>
int main(){
   printf("TIPO           BYTES                  MIN MAX \n");
   printf("char           %5lu %20d %d \n",sizeof(char) ,SCHAR_MIN,SCHAR_MAX );
   printf("short          %5lu %20d %d \n",sizeof(short) ,SHRT_MIN,SHRT_MAX );
   printf("int            %5lu %20d %d \n",sizeof(int) ,INT_MIN,INT_MAX );
   printf("long           %5lu %20ld %ld \n",sizeof(long) ,LONG_MIN,LONG_MAX );
   printf("unsigned char  %5lu %20d %d \n",sizeof(unsigned char) ,0,UCHAR_MAX );
   printf("unsigned short %5lu %20d %d \n",sizeof(unsigned short) ,0,USHRT_MAX );
   printf("unsigned int   %5lu %20d %u \n",sizeof(unsigned int) ,0,UINT_MAX );
   printf("unsigned long  %5lu %20d %lu \n\n",sizeof(unsigned long) ,0,ULONG_MAX );
   //float, double, long double
   printf("TIPO         BYTES DIGITOS   MINexp MAXexp\n");
   printf("float:       %5lu %7u   %6d %d\n",sizeof(float) ,FLT_DIG, FLT_MIN_EXP,FLT_MAX_EXP);
   printf("double:      %5lu %7d   %6d %d\n",sizeof(double) ,DBL_DIG, DBL_MIN_EXP,DBL_MAX_EXP);
   printf("long double: %5lu %7d   %6d %d\n",sizeof(long double) ,LDBL_DIG, LDBL_MIN_EXP,LDBL_MAX_EXP);
}