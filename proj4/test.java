import java.util.*;
import java.io.*;

class test {
	private static void shift(int[][] a) {
		int[][] b = new int[4][4];
    	int row_len = b.length;
    	int col_len = b[0].length;
    	int offset = 0;

    	for(int i = 0; i < row_len; ++i) {
    		for(int j = 0; j < col_len; ++j) {
    			b[i][j] = a[i][j];
    		}
    	}

    	for(int i = 0; i < row_len; ++i) {
    		for(int j = 0; j < col_len; ++j) {
    			a[i][j] = b[i][(j + offset) % 4];
    		}
    		++offset;
    	}
	}

  ////////////////////////  the mixColumns Tranformation ////////////////////////
    final static int[] LogTable = {
    0,   0,  25,   1,  50,   2,  26, 198,  75, 199,  27, 104,  51, 238, 223,   3,
    100,   4, 224,  14,  52, 141, 129, 239,  76, 113,   8, 200, 248, 105,  28, 193,
    125, 194,  29, 181, 249, 185,  39, 106,  77, 228, 166, 114, 154, 201,   9, 120,
    101,  47, 138,   5,  33,  15, 225,  36,  18, 240, 130,  69,  53, 147, 218, 142,
    150, 143, 219, 189,  54, 208, 206, 148,  19,  92, 210, 241, 64,  70, 131,  56,
    102, 221, 253,  48, 191,   6, 139,  98, 179,  37, 226, 152,  34, 136, 145,  16,
    126, 110,  72, 195, 163, 182,  30,  66,  58, 107,  40,  84, 250, 133,  61, 186,
    43, 121,  10,  21, 155, 159,  94, 202,  78, 212, 172, 229, 243, 115, 167,  87,
    175,  88, 168,  80, 244, 234, 214, 116,  79, 174, 233, 213, 231, 230, 173, 232,
    44, 215, 117, 122, 235,  22,  11, 245,  89, 203,  95, 176, 156, 169,  81, 160,
    127,  12, 246, 111,  23, 196,  73, 236, 216,  67,  31,  45, 164, 118, 123, 183,
    204, 187,  62,  90, 251,  96, 177, 134,  59,  82, 161, 108, 170,  85,  41, 157,
    151, 178, 135, 144,  97, 190, 220, 252, 188, 149, 207, 205, 55,  63,  91, 209,
    83,  57, 132,  60,  65, 162, 109,  71,  20,  42, 158,  93,  86, 242, 211, 171,
    68,  17, 146, 217,  35,  32,  46, 137, 180, 124, 184,  38, 119, 153, 227, 165,
    103,  74, 237, 222, 197,  49, 254,  24,  13,  99, 140, 128, 192, 247, 112,   7 };

    final static int[] AlogTable = {
    1,   3,   5,  15,  17,  51,  85, 255,  26,  46, 114, 150, 161, 248,  19,  53,
    95, 225,  56,  72, 216, 115, 149, 164, 247,   2,   6,  10,  30, 34, 102, 170,
    229,  52,  92, 228,  55,  89, 235,  38, 106, 190, 217, 112, 144, 171, 230,  49,
    83, 245,   4,  12,  20,  60,  68, 204,  79, 209, 104, 184, 211, 110, 178, 205,
    76, 212, 103, 169, 224,  59,  77, 215,  98, 166, 241,   8,  24, 40, 120, 136,
    131, 158, 185, 208, 107, 189, 220, 127, 129, 152, 179, 206,  73, 219, 118, 154,
    181, 196,  87, 249,  16,  48,  80, 240,  11,  29,  39, 105, 187, 214,  97, 163,
    254,  25,  43, 125, 135, 146, 173, 236,  47, 113, 147, 174, 233,  32,  96, 160,
    251,  22,  58,  78, 210, 109, 183, 194,  93, 231,  50,  86, 250,  21,  63,  65,
    195,  94, 226,  61,  71, 201,  64, 192,  91, 237,  44, 116, 156, 191, 218, 117,
    159, 186, 213, 100, 172, 239,  42, 126, 130, 157, 188, 223, 122, 142, 137, 128,
    155, 182, 193,  88, 232,  35, 101, 175, 234,  37, 111, 177, 200,  67, 197,  84,
    252,  31,  33,  99, 165, 244,   7,   9,  27,  45, 119, 153, 176, 203,  70, 202,
    69, 207,  74, 222, 121, 139, 134, 145, 168, 227,  62,  66, 198, 81, 243,  14,
    18,  54,  90, 238,  41, 123, 141, 140, 143, 138, 133, 148, 167, 242,  13,  23,
    57,  75, 221, 124, 132, 151, 162, 253,  28,  36, 108, 180, 199, 82, 246,   1 };

    private static int mul(int a, int b) {
        int inda = (a < 0) ? (a + 256) : a;
        int indb = (b < 0) ? (b + 256) : b;

        if ( (a != 0) && (b != 0) ) {
            int index = LogTable[inda] + LogTable[indb];
            int val = AlogTable[ index % 255 ];
            return val;
        }
        else
            return 0;
    } // mul

    // In the following two methods, the input c is the column number in
    // your evolving state matrix st (which originally contained
    // the plaintext input but is being modified).  Notice that the state here is defined as an
    // array of bytes.  If your state is an array of integers, you'll have
    // to make adjustments.

    private static void mixColumns(int[][] st) {
      // This is another alternate version of mixColumn, using the
      // logtables to do the computation.
      int c = 0;

      while(c < 4) {
        int a[] = new int[4];

        // note that a is just a copy of st[.][c]
        for (int i = 0; i < 4; i++)
            a[i] = st[i][c];

        // This is exactly the same as mixColumns1, if
        // the mul columns somehow match the b columns there.
        st[0][c] = mul(2,a[0]) ^ a[2] ^ a[3] ^ mul(3,a[1]);
        st[1][c] = mul(2,a[1]) ^ a[3] ^ a[0] ^ mul(3,a[2]);
        st[2][c] = mul(2,a[2]) ^ a[0] ^ a[1] ^ mul(3,a[3]);
        st[3][c] = mul(2,a[3]) ^ a[1] ^ a[2] ^ mul(3,a[0]);
        ++c;
      }
    } // mixColumn2

    private static void invMixColumns(int[][] st) {
      int c = 0;

      while(c < 4) {
        int a[] = new int[4];

        // note that a is just a copy of st[.][c]
        for (int i = 0; i < 4; i++)
            a[i] = st[i][c];

        st[0][c] = mul(0xE,a[0]) ^ mul(0xB,a[1]) ^ mul(0xD, a[2]) ^ mul(0x9,a[3]);
        st[1][c] = mul(0xE,a[1]) ^ mul(0xB,a[2]) ^ mul(0xD, a[3]) ^ mul(0x9,a[0]);
        st[2][c] = mul(0xE,a[2]) ^ mul(0xB,a[3]) ^ mul(0xD, a[0]) ^ mul(0x9,a[1]);
        st[3][c] = mul(0xE,a[3]) ^ mul(0xB,a[0]) ^ mul(0xD, a[1]) ^ mul(0x9,a[2]);
        ++c;
      }
    } // invMixColumn2

	public static void main(String[] args) {
    	final int[] input = { 0xD4, 0x27, 0x11, 0xAE,
                            0xE0, 0xBF, 0x98, 0xF1,
                            0xB8, 0xB4, 0x5D, 0xE5,
                            0x1E, 0x41, 0x52, 0x30 };

		int[][] state = new int[4][4];

		// Initialize state array
    	for(int i = 0; i < 4; ++i) {
      		for(int j = 0; j < 4; ++j) {
        		state[i][j] = input[i + 4*j];
        		System.out.print(Integer.toHexString(state[i][j]) + " ");
      		}
      		System.out.println();
    	}
    	System.out.println();
    	shift(state);
    	System.out.println();
      mixColumns(state);
     	for(int i = 0; i < 4; ++i) {
      		for(int j = 0; j < 4; ++j) {
      			System.out.print(Integer.toHexString(state[i][j]) + " ");
      		}
      		System.out.println();
      }
	}
}