package DES;

import java.io.UnsupportedEncodingException;
import java.util.Scanner;



    public class DES_encryption {

        /**
         *
         * This file is dedicated to do encryption of the message provided in the GUI
         * and use the key provided to do it.
         *
         * Encryption happens in the following steps:
         *
         *      1. Plaintext is converted from UTF-8 encoding to a binary string, representing
         *         that message in form of bits
         *          - Padding is done if the length of string is not a multiple of 8.
         *          - utf_to_bin(String utf_message)
         *
         *      2. Convert the Key to a binary string using te same function in Step 1
         *          - utf_to_bin(String utf_message)
         *          - Key length = 64 bits
         *
         *      3. The plaintext is divided into smaller messages: feistelNetwork(String message_plainText)
         *          - Check if the text length is a multiple of 64
         *          - Do padding to make the text length = 64*n. Add 0s in the starting
         *          - Text is divided into 64-bits/8-characters of messages
         *          - Text is divided into two part of 32bits, left and right
         *          - f function is called
         *
         *      4. Now, the keyScheduler is used to convert the 64-bit key into 16 keys of
         *         length 48 bits
         *         - Steps:
         *               64-bit key -> (PC-1) -> 56-bit key
         *               56 bit -> Two 28-bit keys -> binary Left-shift operations -> 56bit key
         *               56-bit key -> (PC-2) -> 48 bit key
         *
         *      5. Creating the f function: f(String message_bin_32)
         *         - Expansion box on the 32 bit message
         *         - XOR with the key (48bits)
         *         - Substitution box
         *         - Permutation
         *
         *
         *      6. XOR the output of the f function with the left part(32-bit)
         *
         *      7. Swap the left and the right part (happens in fiestel network)
         *
         *
         *
         *
         *
         * @author Chris Stubbs aka deadlytea on github
         * @author Bora Sabuncu aka bsabuncu
         * @author gauravsatija
         *
         */

        private DES_boxes des_boxes_object = new DES_boxes();

        protected int number_of_rounds = 16; // specifying number of rounds

        private long[] key_48_list = new long[number_of_rounds];
        // The length of the list is based on number of rounds

        /**
         *
         * @param utf_message
         * @return binaryResult - binary representation of the message entered
         *
         * @author billjamesdev StackOverFlow - https://stackoverflow.com/questions/36534346/converting-text-to-binary-in-java
         * @author gauravSatija
         */
        public String utf_to_bin(String utf_message){

            String binaryResult = "",  // store the binary value of the message in this String
                    binaryTemp="";      // temporary storage of binary value for every character

            for (char c : utf_message.toCharArray()){
                binaryTemp += Integer.toBinaryString( (int) c);
                // As it is converted to an integer first the starting '0's might be removed before it converts to string
                // So some padding is required

                // padding to make the binary string, of the character c, into 8 bits
                while(binaryTemp.length() % 8 != 0)
                    binaryTemp = "0" + binaryTemp;

                binaryResult = binaryResult + binaryTemp;
                binaryTemp="";
            }
            //  System.out.println(utf_message + " " +binaryResult);

            return binaryResult;
        }

        /**
         *
         * @param key_64 - the key entered by the user.
         * @return this method doesn't return anything but it stores all the key in the key_48_list member.
         */
        public void setKeyList(String key_64){

            //Convert:  64-bit key -> (PC-1) -> 56-bit key
            String key_56="";

            // Here the 64-bit key is converted into the 56-bit key and permutation happens

            // Conversion of 64 bit key to 56 bit key happens using PC1 box from DES_boxes.java
            for (int i = 0; i < des_boxes_object.PC1.length; i++)
                key_56 = key_56 + key_64.charAt(des_boxes_object.PC1[i] - 1);


            //56 bit -> Two 28-bit keys (key-left & key-right) -> binary Left-shift operations -> 56bit key

            String key_28_left = key_56.substring(0, key_56.length()/2),
                    key_28_right= key_56.substring(key_56.length()/2);

            // parseInt("1100110", 2) returns 102
            //int il,iR;

            for(int i = 0; i < number_of_rounds; i++){

                // Left shift based on the key_shift array in DES_boxes
                key_28_left = key_28_left.substring( des_boxes_object.key_shift[i]) +
                        key_28_left.substring(0, des_boxes_object.key_shift[i]);

                // Right shift based on the key_shift array in DES_boxes
                key_28_right = key_28_right.substring(des_boxes_object.key_shift[i]) +
                        key_28_right.substring(0, des_boxes_object.key_shift[i]);


                key_56 = key_28_left + key_28_right;

                String key_48 ="";


                // 56-bit key -> (PC-2) -> 48 bit key
                for (int j = 0; j < des_boxes_object.PC2.length; j++)
                    key_48 = key_48 + key_56.charAt( des_boxes_object.PC2[j] - 1);

                // Add the 48_bit keys into the array key_48_bit
                // This convert the String into Long
                key_48_list[i] = Long.parseLong(key_48,2);


            }
        }


        /**
         * The following is f() "The f method" of the DES algorithm. It has following four functions:
         *      Expansion box - expanding the 32 bit message into s 48 bit message by repeating 16 bits
         *      XOR - Applying XOR operation between the 48 bit message and the 48 bit key
         *      Substitution - Using 8 s-boxes to convert the 48 bit XORed message into a 32 bit message
         *                     The 48 bit message is first divided into eight 6bit smaller parts and
         *                     then into 4 bit messages and then joined together.
         *          S-box
         *      Permutation -  The 32 bit message is then again goes through permutation
         * @param message_right_32
         * @param round_number
         * @return the 32 bit message after permutation is returned as a String data type
         * message_permutation_32 : this is the right block of 32 bits
         *         after all the 4 operations happened
         */
        public String f(String message_right_32, int round_number){

            // Expansion box or Expansion function
            // This expands the 32 bit message into a 48 bit message
            // String that contain the expanded message
            String message_expan_48 ="";
            for (int j = 0; j < des_boxes_object.expansion_box.length; j++)
                message_expan_48 = message_expan_48 + message_right_32.charAt( des_boxes_object.expansion_box[j] - 1);

            long message_int_48 = Long.parseLong(message_expan_48,2);


            // XOR
            // This function is used to do XOR operation between 48 bits expanded message and the keys from the keyList
            long message_xor_48 = message_int_48 ^ key_48_list[round_number];


            // Substitution

            // Split the message into eight 6bit messages
            String[] messages_sbox_6 = new String[8],
                    messages_sbox_4 = new String[8];
            String message_xor_48_string = Long.toBinaryString(message_xor_48);

            while( message_xor_48_string.length() % 48 != 0 )
                message_xor_48_string = "0" + message_xor_48_string;

            for(int i=0, j=0; i < messages_sbox_6.length-1; i++, j+=6)
                messages_sbox_6[i] = message_xor_48_string.substring(j, j+6);

            // IndexOutOfBoundsException at last index as the array ends at 47 not 48
            messages_sbox_6[7] =  message_xor_48_string.substring(7*6);
            //       System.out.println("--"+message_xor_48_string.substring(7*6));

            // S-box calculations
            for(int i =0; i < messages_sbox_6.length; i++){

                // Get row and column
//            System.out.println( " " + i + " " + messages_sbox_6[i] ) ;
                int row   = Integer.parseInt(messages_sbox_6[i].charAt(0) + "" + messages_sbox_6[i].charAt(5), 2);
                int column= Integer.parseInt(messages_sbox_6[i].substring(1,5),2);

                // Find the value in the S-box based on row and column
                int s_box_value = des_boxes_object.s_boxes[i][row][column];
                messages_sbox_4[i] = Integer.toBinaryString(s_box_value);

                // Make sure the string is 4 bits
                while (messages_sbox_4[i].length() < 4)
                    messages_sbox_4[i] = "0" + messages_sbox_4[i];

            }

            // Add all the strings together
            String message_sbox_32 = "";
            for ( int i = 0; i < messages_sbox_4.length; i++)
                message_sbox_32 = message_sbox_32 + messages_sbox_4[i];

            // Permutation

            // All 32-bit characters as kept as '-' as it will help in error checking in future
            String message_permutation_32="--------------------------------";

            // Replacing characters
            // Remember permutation indexes start from 1
            for(int i=0; i < des_boxes_object.permutation_sbox.length; i++)
                message_permutation_32 = message_permutation_32.substring( 0, des_boxes_object.permutation_sbox[i]-1) +
                        message_sbox_32.charAt( des_boxes_object.permutation_sbox[i]-1) +
                        message_permutation_32.substring( des_boxes_object.permutation_sbox[i]);


            /** Alternate
             * CHECKED for error when  DES_boxes.permutation_sbox[i]+1 = 64 i.e
             * Index out of bound. One possible solution might be to add another '-' at the end
             * */

            return message_permutation_32;
        }


        /**
         * Feistel Network is implemented in the following method. It takes the plaintext as the input
         * and divides it into different 64 bit long messages and encrypt it based on keys.
         *
         * @param message_plainText
         * @return the encrypted message
         */
        public String feistelNetwork(String message_plainText){

            // - Check if the text length is a multiple of 64
            // - Do padding to make the text length = 64*n. Add 0s in the starting.
            // !!! You can throw error here based on the person if they want specifically 64 bits !!! But this code is flexible
            System.err.println(message_plainText);
            while(message_plainText.length() % 64 != 0) {
                message_plainText = "0" + message_plainText;
            }



            // - Text is divided into 64bits/characters of messages
            // - Text is divided into two part of 32bits, left and right

            int total_messages_64 = (message_plainText.length() / 64);
            String[] message_list_64 = new String[total_messages_64];



            for(int i=0; i < message_list_64.length ; i++)
                message_list_64[i] = message_plainText.substring(i*64, (i+1)*64 );

            String message_left_32, message_right_32;


            // This loop is for the Feistel Network
            for(int i = 0; i < message_list_64.length; i++){

                // Divide individual string into smaller messages of 32bits/ 32 characters
                message_left_32 = message_list_64[i].substring(0, message_list_64[i].length()/2);
                message_right_32= message_list_64[i].substring( message_list_64[i].length()/2 );


                for( int round = 0; round < number_of_rounds; round++) {

                    // Put the right 32 bit into f function
                    String message_f_32 = f(message_right_32, round);
                    long message_f_32_long = Long.parseLong(message_f_32, 2),
                            message_left_32_long = Long.parseLong(message_left_32, 2);

                    long message_left_32_xor = message_left_32_long ^ message_f_32_long;

                    message_left_32 = message_right_32;

                    message_right_32 = Long.toBinaryString(message_left_32_xor);


                    // padding
                    while (message_left_32.length() < 32)
                        message_left_32 = "0" + message_left_32;

                    while (message_right_32.length() < 32)
                        message_right_32 = "0" + message_right_32;
                }

                message_list_64[i] = message_left_32 + message_right_32;
//            System.out.println("message: " + message_list_64[i]);

            }



            String message_encrypt = "";

            for( int i = 0; i < message_list_64.length; i++)
                message_encrypt = message_encrypt + message_list_64[i];



            return message_encrypt;

        }



        /**
         * Hash Function from user sfussenegger on stackoverflow
         * It returns a unique hashcode for unique keys.
         * @param string : String to hash
         * @return 64-bit long hash value
         * @source http://stackoverflow.com/questions/1660501/what-is-a-good-64bit-hash-function-in-java-for-textual-strings
         *        & https://github.com/deadlytea/DES
         */

        // adapted from String.hashCode()
        // You can also use hashcode it will produce the same result.
        // The reason to write is explicitly is for better understanding
        public static long hash(String string) {
            long h = 1125899906842597L; // prime
            int len = string.length();

            for (int i = 0; i < len; i++) {
                h = 31*h + string.charAt(i);
            }
            return h;
        }

        public static void main(String[] args) {

            DES_encryption des_encryption = new DES_encryption();

            String plaintext, key;
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter plaintext: ");
            plaintext = scanner.nextLine();
            System.out.print("Enter key: ");
            key = scanner.nextLine();

            plaintext = des_encryption.utf_to_bin(plaintext);
            key = des_encryption.utf_to_bin("" + key.hashCode()  );

            des_encryption.setKeyList(key);

            String encrypt_message = des_encryption.feistelNetwork(plaintext);

            System.out.println("Encrypted message: \n" + encrypt_message);

//        System.out.println("encrypted message (in UTF) ");
//        bin_to_utf(encrypt_message);




        }



        /*
            The following method is same as the main(String[] args) method but is used to by the GUI class and other methods
        */
        public String main_method_for_gui(String plaintext_gui, String key_gui) {

            DES_encryption des_encryption = new DES_encryption();

            Scanner scanner = new Scanner(System.in);

//        System.out.print("Enter plaintext: ");
            plaintext_gui = plaintext_gui;
//        System.out.print("Enter key: ");
            key_gui = key_gui;

            plaintext_gui = des_encryption.utf_to_bin(plaintext_gui);
            key_gui = des_encryption.utf_to_bin("" + key_gui.hashCode()  );

            des_encryption.setKeyList(key_gui);

            String encrypt_message = des_encryption.feistelNetwork(plaintext_gui);

//        System.out.println("Encrypted message: \n" + encrypt_message);
            bin_to_utf(encrypt_message);

            return encrypt_message;
        }



        /**
         * It takes the string representing binary message in 0s and 1s. After it converts that binary
         * message into a UTF-8 encoded message which is what humans are capable of reading
         *
         * @param bin
         * @return UTF-8 encoded message
         */
        private static void bin_to_utf(String bin) {

            // Convert back to String
            byte[] ciphertextBytes = new byte[bin.length() / 8];
            // System.out.println("bin len:" + bin.length());
            String ciphertext = null;
            for (int j = 0; j < ciphertextBytes.length; j++) {
                String temp = bin.substring(0, 8);
                byte b = (byte) Integer.parseInt(temp, 2);
                ciphertextBytes[j] = b;
                bin = bin.substring(8);
            }

            try {
                ciphertext = new String(ciphertextBytes, "utf-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            int i=0;
//            while(ciphertext.length() > i)
//                System.out.println(ciphertext.charAt(i++) + " " + ciphertextBytes[i]);

            i=0;
            while(ciphertext.length() > i)
                System.out.print(ciphertext.charAt(i++));
        }

    }


/**
 *
 *Sample1:
 *  Enter plaintext:This is a secret message
 *  Enter key:secretkey
 *  Encrypted message:
 *  010101101001110001000101011001111011001101101001001000100101110001101010011011011111001110011111000011110111010111101011111101000010001011000000011110110000100001100111001001000011011000110100
 *
 *
 *
 *
 *
 * Sample 2:
 *  Enter plaintext: this is a really secret message
 *  Enter key: message
 *  Encrypted message:
 *  0101111010010111111000110011000101110001001101111010101111011110000110010011001000011010101101100011000101000100110101001010001101101000010010000000001101010001000101010001000001001111001100111100111001101101001010000100000010110001100000110011010001011001
 *
 **/





