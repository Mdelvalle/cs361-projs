import java.util.*;
import java.io.*;

class AES {
  // Look-up table
  private static final int[][] lT = {
    //      0     1     2     3     4     5     6     7     8     9     a     b     c     d     e     f
    /* 0 */ {0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76},
    /* 1 */ {0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0},
    /* 2 */ {0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15},
    /* 3 */ {0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75},
    /* 4 */ {0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84},
    /* 5 */ {0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf},
    /* 6 */ {0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8},
    /* 7 */ {0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2},
    /* 8 */ {0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73},
    /* 9 */ {0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb},
    /* a */ {0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79},
    /* b */ {0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08},
    /* c */ {0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a},
    /* d */ {0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e},
    /* e */ {0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf},
    /* f */ {0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16} };

  // Inverse Look-up table
  private static final int[][] inverse_lT = {
    //      0     1     2     3     4     5     6     7     8     9     a     b     c     d     e     f
    /* 0 */ {0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb},
    /* 1 */ {0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb},
    /* 2 */ {0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e},
    /* 3 */ {0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25},
    /* 4 */ {0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92},
    /* 5 */ {0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84},
    /* 6 */ {0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06},
    /* 7 */ {0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b},
    /* 8 */ {0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73},
    /* 9 */ {0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e},
    /* a */ {0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b},
    /* b */ {0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4},
    /* c */ {0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f},
    /* d */ {0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef},
    /* e */ {0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61},
    /* f */ {0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d} };


  private static void subBytes(int[][] s) {
    int row_len = s.length;
    int col_len = s[0].length;

    for(int i = 0; i < row_len; ++i) {
      for(int j = 0; j < col_len; ++j) {

        int left = s[i][j] / 16;
        int right = s[i][j] % 16;
        s[i][j] = lT[left][right];
      }
    }
  }

  private static void invSubBytes(int[][] s) {
    int row_len = s.length;
    int col_len = s[0].length;

    for(int i = 0; i < row_len; ++i) {
      for(int j = 0; j < col_len; ++j) {

        int left = s[i][j] / 16;
        int right = s[i][j] % 16;
        s[i][j] = inverse_lT[left][right];
      }
    }
  }

  private static void shiftRows(int[][] a) {
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

  private static void invShiftRows(int[][] a) {
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
            a[i][(j + offset) % 4] = b[i][j];
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


    static int[][] Rcon = { {0x01, 0x00, 0x00, 0x00},   // col 0
                            {0x02, 0x00, 0x00, 0x00},   // col 1
                            {0x04, 0x00, 0x00, 0x00},   // col 2
                            {0x08, 0x00, 0x00, 0x00},   // col 3
                            {0x10, 0x00, 0x00, 0x00},   // col 4
                            {0x20, 0x00, 0x00, 0x00},   // col 5
                            {0x40, 0x00, 0x00, 0x00},   // col 6
                            {0x80, 0x00, 0x00, 0x00},   // col 7
                            {0x1B, 0x00, 0x00, 0x00},   // col 8
                            {0x36, 0x00, 0x00, 0x00} }; // col 9


    private static void subWord(int[] sW) {
        for(int i = 0; i < 4; ++i) {
            int left = sW[i] / 16;
            int right = sW[i] % 16;
            sW[i] = lT[left][right];
        }
    }

    private static void rotWord(int[] rW) {
        int[] temp = new int[4];
        for(int i = 0; i < 4; ++i) {
            temp[i] = rW[i];
        }
        rW[0] = temp[1];
        rW[1] = temp[2];
        rW[2] = temp[3];
        rW[3] = temp[0];
    }

    private static void fillarray(int[] a, int[][] b, int col) {
        for(int i = 0; i < 4; ++i) {
            a[i] = b[i][col];
        }
    }

    private static void xor(int[] a, int[] b, int[][] c, int col) {
        for(int i = 0; i < 4; ++i) {
            c[i][col] = a[i] ^ b[i];
        }
    }

    private static void keyExpansion(int[][] cKey) {
        int words = 44;
        int current = 4;
        int rcon_col = 0;
        int[] wi_1 = new int[4];
        int[] wi_4 = new int[4];

        while(current < 44) {
            fillarray(wi_1, cKey, current-1);
            rotWord(wi_1);
            subWord(wi_1);

            fillarray(wi_4, cKey, current-4);
            for(int i = 0; i < 4; ++i) {
                cKey[i][current] = wi_4[i] ^ wi_1[i] ^ Rcon[rcon_col][i];
            }
            fillarray(wi_1, cKey, current);
            ++rcon_col;
            ++current;
            for(int i = 0; i < 3; ++i) {
                fillarray(wi_4, cKey, current-4);
                xor(wi_4, wi_1, cKey, current);
                fillarray(wi_1, cKey, current);
                ++current;
            }
        }
    }

    private static void addRoundKey(int[][] s, int[][] w, int startCol) {
      int col = startCol;
      for(int i = 0; i < 4; ++i) {
        for(int j = 0; j < 4; ++j) {
          s[i][j] = s[i][j] ^ w[i][col];
          ++col;
        }
        col = startCol;
      }
    }

    private static void printState(int[][] s) {
        for(int i = 0; i < 4; ++i) {
            for(int j = 0; j < 4; ++j) {
                System.out.print(Integer.toHexString(s[i][j]) + " ");
            }
            System.out.println();
        }
    }

  /*********** 
  *** MAIN ***
  ***********/
  public static void main(String[] args) throws IOException {
    String option;
    String keyFile;
    String inputFile;
    BufferedReader bR;
    BufferedWriter bW;

    option = args[0];
    keyFile = args[1];
    inputFile = args[2];

    bR = new BufferedReader(new FileReader(keyFile));

    // Fill cipherKey from keyFile
    final int[] cipherKey = new int[16];
    String line = null;
    for(int i = 0; (line = bR.readLine()) != null; ++i) {
        if((line.length() > 32) || !line.matches("[0-9A-Fa-f]+"))
            continue;
        if(line.length() < 32) {
            int diff = 32 - line.length();
            while(diff > 0) {
                line += "0";
                --diff;
            }
        }
        for (int j = 0; j < 32; j+=2) {
            String temp = line.substring(j, j+2);
            cipherKey[j/2] = Integer.parseInt(temp, 16);
        }
    }

    // Fill input from inputFile
    bR = new BufferedReader(new FileReader(inputFile));

    int[][] keySchedule = new int[4][44];

    // Initialize keySchedule array
    for(int i = 0; i < 4; ++i) {
      for(int j = 0; j < 4; ++j) {
        keySchedule[i][j] = cipherKey[i + 4*j];
      }
    }


    if(option.equals("e")) {
        final int[] input = new int[16];
        line = null;
        FileWriter writer = new FileWriter(inputFile + ".enc");
        PrintWriter pW = new PrintWriter(writer);

        for(int i = 0; (line = bR.readLine()) != null; ++i) {
            if(i != 0)
                pW.println();
            if((line.length() > 32) || (line.length() < 32) || !line.matches("[0-9A-Fa-f]+"))
                continue;
            for (int j = 0; j < 32; j+=2) {
                String temp = line.substring(j, j+2);
                input[j/2] = Integer.parseInt(temp, 16);
            }

            keyExpansion(keySchedule);

            // rounds / cycles
            final int rounds = 10;

            int[][] state = new int[4][4];

            // Initialize state array
            for(int m = 0; m < 4; ++m) {
                for(int n = 0; n < 4; ++n) {
                    state[m][n] = input[m + 4*n];
                }
            }

            addRoundKey(state, keySchedule, 0);

            for(int m = 1; m < rounds; ++m) {
                subBytes(state);
                shiftRows(state);
                mixColumns(state);
                addRoundKey(state, keySchedule, m*4);
            }
            subBytes(state);
            shiftRows(state);
            addRoundKey(state, keySchedule, rounds*4);

            for(int m = 0; m < 4; ++m) {
                for(int n = 0; n < 4; ++n) {
                    pW.printf("%02x", state[n][m]);
                }
            }
        }
        pW.flush();
        pW.close();
    }

    else if(option.equals("d")) {
        final int[] input = new int[16];
        line = null;
        FileWriter writer = new FileWriter(inputFile + ".dec");
        PrintWriter pW = new PrintWriter(writer);

        for(int i = 0; (line = bR.readLine()) != null; ++i) {
            if(i != 0)
                pW.println();
            if((line.length() > 32) || (line.length() < 32) || !line.matches("[0-9A-Fa-f]+"))
                continue;
            for (int j = 0; j < 32; j+=2) {
                String temp = line.substring(j, j+2);
                input[j/2] = Integer.parseInt(temp, 16);
            }

            keyExpansion(keySchedule);


            // rounds / cycles
            final int rounds = 10;

            int[][] state = new int[4][4];

            // Initialize state array
                for(int m = 0; m < 4; ++m) {
                    for(int n = 0; n < 4; ++n) {
                        state[m][n] = input[m + 4*n];
                    }
                }

            addRoundKey(state, keySchedule, rounds*4);

            for(int m = rounds-1; m > 0; --m) {
                invShiftRows(state);
                invSubBytes(state);
                addRoundKey(state, keySchedule, m*4);
                invMixColumns(state);
            }
            invShiftRows(state);
            invSubBytes(state);
            addRoundKey(state, keySchedule, 0);

                for(int m = 0; m < 4; ++m) {
                    for(int n = 0; n < 4; ++n) {
                        pW.printf("%02x", state[n][m]);
                    }
                }
        }
        pW.flush();
        pW.close();
    }
  }
}