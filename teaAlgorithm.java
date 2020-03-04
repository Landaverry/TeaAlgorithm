import java.util.*;
import java.math.*;
public class teaAlgorithm
{
    public static final int DELTA = 0x9e3779b9; //2^32 golden ratio.
    public static void main(String[] args) 
    {
        int [] k = {0xAF6BABCD,0xEF00F000,0xFEAFFFFF,0xABCDEF01};
        // 128-bit key split into an array, this allows the lines of code within the for loop to decrypt/encrypt the text
        long key[] = {0x01CA4567,0x89ABCDEF};
        //splitting the 64 bit key because long data type was to small for entire key
        long cipherText = encrypt(key,k);
        decrypt(cipherText,k);
    }

    public static long encrypt(long key[], int [] k)
    // key will be the 64 bit plain text block. k is the 128 bit key divided into 4 segments
    {
        int left = (int) key[0];
        //left 32 digits of key
        int right = (int)key[1];
        //takes key and shifts it logically right 32 places
        int sum = 0; 
        final long BITMASK = (left << 32) - 1;
        //arithmetic shift of the variable left 32 place to the left.
        for(int i = 0; i<32;i++) //32 is for the number of rounds being preformed.
        //for tea alogorithm 32 rounds or more are needed for it to be considered secured.
        {
            sum+=DELTA;
            //delta is added to ensure that all subkeys are different.
            left += ((right<<4)+k[0])^(sum+right)^((right>>>5)+k[1]);
            //adjustment of left and right variable.
            right += ((left<<4)+k[2])^(sum+left)^((left>>>5)+k[3]);
        }
        System.out.println("CipherText: " + Integer.toHexString(left) + Integer.toHexString(right));
        long cipherText[] = {left,right};
        return (right&BITMASK)<<32 | (left&BITMASK);
        //returns the or bitwise operation of the and operation of the BitMask and left, and the product of BitMask and Right, followed by being
        //shifted to the left arithmetically 32 positions to the left. 
    }

    public static long decrypt(long cipherKey, int [] k)
    {
        int left = (int)cipherKey;
        // cipher key is split in half just like the key is when using the encryption function.
        int right = (int)(cipherKey>>>32);
        int sum = DELTA<<5;
        //sum is equal to Delta arithmetically shifted to the left 5 positions
        final long BITMASK = (left << 32) - 1;
        // a Bitmask is created just like in the encryption function.
        for(int i = 0; i<32;i++)
        {
            right -= ((left<<4)+k[2])^(left+sum)^((left>>>5)+k[3]);
            //adjusts the left and right portions of the key over the course of the 32 rounds to return the proper plaintext
            left -=((right<<4)+k[0])^(right+sum)^((right>>>5)+k[1]);
            //sum is subtracted by delta every round of the algorithm 
            sum-=DELTA;
        }
        System.out.println("PlainText: " + Integer.toHexString(left) +Integer.toHexString(right));
        return 0;
        //returns the or bitwise operation of the and operation of the BitMask and left, and the product of BitMask and Right, followed by being
        //shifted to the left arithmetically 32 positions to the left. 
    }
}