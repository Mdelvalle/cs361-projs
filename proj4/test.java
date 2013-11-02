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

	public static void main(String[] args) {
    	final int[] input = { 0xD4, 0x27, 0x11, 0xAE,
                              0xE0, 0xBF, 0X98, 0XF1,
                              0XB8, 0xB4, 0x5D, 0xE5,
                              0X1E, 0x41, 0x52, 0x30 };

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
     	for(int i = 0; i < 4; ++i) {
      		for(int j = 0; j < 4; ++j) {
      			System.out.print(Integer.toHexString(state[i][j]) + " ");
      		}
      		System.out.println();
      	}
	}
}