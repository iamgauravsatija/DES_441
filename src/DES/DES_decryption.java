package DES;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class DES_decryption {

    protected static DES_boxes des_boxes_object = new DES_boxes();
    protected static DES_encryption desEncryption = new DES_encryption();

    private long[] key_48_list = new long[desEncryption.number_of_rounds];
    // The length of the list is based on number of rounds.
    // Standard number of rounds are 16 but the programmer can modify it for any reason.

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
//        System.out.println(utf_message + " " +binaryResult);

        return binaryResult;
    }



    /**
     * The following function takes the 64 bit key entered by the user as the input and generates 16 more
     * keys using Permutation and substitution methods. These 16 keys are then used for encryption and decryption later.
     *
     * @param key_64 - the key entered by the user.
     * @return this method doesn't return anything but it stores all the key in the key_48_list member.
     */
    public void setKeyList(String key_64){

        //Convert:  64-bit key -> (PC-1) -> 56-bit key
        String key_56="";

        // Here the 64-bit key is converted into the 56-bit key and permutation happens
        // using PC1 box from DES_boxes.java
        for (int i = 0; i < des_boxes_object.PC1.length; i++)
            key_56 = key_56 + key_64.charAt(des_boxes_object.PC1[i] - 1);


        //56 bit -> Two 28-bit keys -> binary Left-shift operations -> 56bit key

        String key_28_left = key_56.substring(0, key_56.length()/2),
                key_28_right= key_56.substring(key_56.length()/2);


        // Example of parseInt function:
        // parseInt("1100110", 2) returns 102

        for(int i = 0; i < desEncryption.number_of_rounds; i++){

            // Left shift based on the key_shift array in DES_boxes
            key_28_left = key_28_left.substring( des_boxes_object.key_shift[i]) +
                    key_28_left.substring(0, des_boxes_object.key_shift[i]);

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
     * @return the 32 bit message after permutation is returned as a String data type.
     * message_permutation_32 : this is the right block of 32 bits
     *  after all the 4 operations happened
     */
    public String f(String message_right_32, int round_number){


        // Expansion box
        // This expands the 32 bit message into a 48 bit message
        String message_expan_48 ="";
        for (int j = 0; j < des_boxes_object.expansion_box.length; j++)
            message_expan_48 = message_expan_48 + message_right_32.charAt( des_boxes_object.expansion_box[j] - 1);

        long message_int_48 = Long.parseLong(message_expan_48,2);


        // XOR
        // This function is used to do XOR operation between 48 bits expanded message and the keys from the keyList
        /**
         * The following like is one of the differences between encryption and decryption algorithms
         */
        long message_xor_48 = message_int_48 ^ key_48_list[ desEncryption.number_of_rounds - round_number - 1];


        // Substitution

        // Split the message into eight 6bit messages
        String[] messages_sbox_6 = new String[8],
                messages_sbox_4 = new String[8];
        String message_xor_48_string = Long.toBinaryString(message_xor_48);

        while( message_xor_48_string.length() % 48 != 0 )
            message_xor_48_string = "0" + message_xor_48_string;

        for(int i=0, j=0; i < messages_sbox_6.length-1; i++, j=j+6)
            messages_sbox_6[i] = message_xor_48_string.substring(j, j+6);

        // IndexOutOfBoundsException at last index as the array ends at 47 not 48
        // To avoid it the 6 bit message at the 7th index is done with following line
        messages_sbox_6[7] =  message_xor_48_string.substring(7*6);

        // S-box calculations
        for(int i =0; i < messages_sbox_6.length; i++){

            // Get columns
            int row   = Integer.parseInt(messages_sbox_6[i].charAt(0) + "" + messages_sbox_6[i].charAt(5), 2),
                    column= Integer.parseInt(messages_sbox_6[i].substring(1,5),2);

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


        return message_permutation_32;
    }


    /**
     * Feistel Network is implemented in the following method. It takes the encrypted text as the input
     * and divides it into different 64 bit long messages and decrypt it based on that.
     *
     * @param message_plainText
     * @return the encrypted message
     */
    public String feistelNetwork_decrypt(String message_plainText){

        // - Check if the text length is a multiple of 64
        // - Do padding to make the text length = 64*n. Add 0s in the starting
        if(message_plainText.length() % 64 != 0) {

            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input. \n" +
                    "Please try again. ", ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK)
            throw new RuntimeException("Invalid input");
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


            for( int round = 0; round < desEncryption.number_of_rounds; round++) {

                // Put the left 32 bit into f function
                String message_f_32 = f(message_left_32, round);

                long message_f_32_long = Long.parseLong(message_f_32, 2),
                        message_right_32_long = Long.parseLong(message_right_32, 2);

                long message_right_32_xor = message_right_32_long ^ message_f_32_long;

                message_right_32 = message_left_32;

                message_left_32 = Long.toBinaryString(message_right_32_xor);


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
     *  This is the main method which can be used to understand how to make calls and how the algorithm works.
     * @param args
     */
    public static void main(String[] args) {

        DES_decryption des_decryption = new DES_decryption();

        String plaintext, key;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter encrypted message then key:");
        plaintext = scanner.nextLine(); // assuming the encrypted message is in binary format

        key = scanner.nextLine(); // key is in UTF-8 format

        key = des_decryption.utf_to_bin("" + key.hashCode() );
        System.out.println("key1: "+key);
        des_decryption.setKeyList(key);

        for(int i =0; i<des_decryption.key_48_list.length;i++)
            System.out.println(i+" index: "+des_decryption.key_48_list[i]);

        String decrypt_message = des_decryption.feistelNetwork_decrypt(plaintext);

        bin_to_utf(decrypt_message);


    }


    /**
     * This method is used by GUIs to get the decrypted text by giving encrypted text and the gui as the input.
     *
     * @param encrypt_text_gui Encrypted text in binary format entered by the user.
     * @param key_gui Key entered by the user.
     * @return Returns the decrypted text in UTF-8 format.
     */
    public String main_method_for_gui(String encrypt_text_gui, String key_gui) {

        DES_decryption des_decryption = new DES_decryption();

        //plaintext = des_decryption.utf_to_bin(plaintext);

        key_gui = des_decryption.utf_to_bin("" + key_gui.hashCode() );

        des_decryption.setKeyList(key_gui);

        String decrypt_message = des_decryption.feistelNetwork_decrypt(encrypt_text_gui);

        //       System.out.println("decrypted message: " + decrypt_message + "\nlength: " + decrypt_message.length());
        //       System.out.println("Decrypted message: "+decrypt_message +"\n");

        decrypt_message = bin_to_utf(decrypt_message);
        return decrypt_message;
    }


    /**
     * It takes the string representing binary message in 0s and 1s. After it converts that binary
     * message into a UTF-8 encoded message which is what humans are capable of reading
     *
     * @param bin
     * @return UTF-8 encoded message
     */
    private static String bin_to_utf(String bin) {

        // Convert back to String
        byte[] ciphertextBytes = new byte[bin.length() / 8];
//        System.out.println("bin len:" + bin.length());
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

        int i = 0;
//        while(ciphertext.length() > i)
//           System.out.println(ciphertext.charAt(i) + " " + ciphertextBytes[i++]);
//
//        i=0;

        String decrypted_message_utf = "";
        while (ciphertext.length() > i) {
            char c_local = ciphertext.charAt(i);
            decrypted_message_utf = decrypted_message_utf+c_local;
            System.out.print(ciphertext.charAt(i++));
        }

        return decrypted_message_utf;
    }



}

/**
 * Sample 1
 *  Enter encrypted message then key:
 *  010101101001110001000101011001111011001101101001001000100101110001101010011011011111001110011111000011110111010111101011111101000010001011000000011110110000100001100111001001000011011000110100
 *  secretkey
 *  Decrypted message:
 *  This is a secret message
 *
 *
 * Sample 2
 *  Enter encrypted message then key:
 *  0001010101010010001010100101110101110001010010001100010101110110011011101100101101101111110110000011010011110010111100010010111000100011010010101110011011100101011101001011000101110000010101100101100011001011101000110101010000110101101000011111111101011001
 *  message
 *  Decrypted message:
 *  this is a really secret message
 *
 */
