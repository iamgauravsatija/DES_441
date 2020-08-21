package Triple_DES;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.UnsupportedEncodingException;

public class Algorithm_Triple_DES {


    static Algorithm_Triple_DES object = new Algorithm_Triple_DES();
    int number_of_rounds =16;
    DES_boxes des_boxes_triple_des = new DES_boxes();

    private long[] key_48_list = new long[number_of_rounds];
    // The length of the list is based on number of rounds.
    // Standard number of rounds are 16 but the programmer can modify it for any reason.



    /**
     *  This is the main method which can be used to understand how to make calls and how the algorithm works.
     * @param args
     */
    public static void main(String[] args) {

        Algorithm_Triple_DES object = new Algorithm_Triple_DES();

        System.out.println("Encryption:");
        //tripleDES_encryption("This is a message", "-k1 this\n-k2 lol\n-k3 kjh");

        System.out.println("\n\n\nDecryption:");
        //object.tripleDES_decryption( object.tripleDES_encryption("This is a message", "-k1 this\n-k2 lol\n-k3 kjh"), "-k1 this\n-k2 lol\n-k3 kjh");


    }

    /**
     * /
     * This method is used to do decryption using triple DES
     *      Formula for Decryption: Encrypted text = Encrypt( Decrypt( Encrypt(plaintext,K1), K2), k3)
     *
     * Note: K1 in encrypt becomes K3 in decrypt and visa-versa.
     *
     * @param plaintext This will be the message (in UTF-8 format) to be encrypted text (in binary format)
     *        key1, key2 and key3 are in utf format
     * @param keyList: List of all the three keys from the GUI as entered by the user
     * @return returns the encrypted text in binary format
     */
    public  String tripleDES_encryption(String plaintext, String keyList){

        String key1, key2, key3;

        // splitting the given string containing all the keys based on new line regular expression

        /*
            \r = CR (Carriage Return) → Used as a new line character in Mac OS before X
            \n = LF (Line Feed) → Used as a new line character in Unix/Mac OS X
            \r\n = CR + LF → Used as a new line character in Windows
        */

        String keyArray[] = keyList.split("\n");

        key1 = keyArray[0].substring(4);
        key2 = keyArray[1].substring(4);
        key3 = keyArray[2].substring(4);

        if(key1.length() < 1 || key2.length() < 1 || key3.length() < 1 ){
            Alert alert = new Alert(Alert.AlertType.WARNING, "No file Selected \n" +
                    "Please try again later ", ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK)
                throw new RuntimeException("Invalid input");
        }

        /**
         *  DES encrypt with K1
         */
        /* This gets the plaintext from UTF-8 to binary format
//        plaintext = object.utf_to_bin(plaintext);
//        System.err.println("1*******\n"+ plaintext);
//
//        // Key this gets a unique hashcode of the key then it gets the utf-8 to binary format.
//        key1 = object.utf_to_bin("" + hash(key1) );
//
//        // Using the binary format of the key it creates a list of 16 keys for individual DES rounds
//        object.setKeyList(key1);
//
//        // Encrypts the message and return it as a string
//        String encrypt_message = object.feistelNetwork(plaintext);
*/
        plaintext = object.utf_to_bin(plaintext);
        //    System.err.println("plaintext*******\n"+ plaintext);
        String encrypt_message_key1 = object.encryption_method(plaintext, key1);
        //    System.out.println("Encrypt message: " + encrypt_message_key1);

        /**
         * DES decrypt with K2
         */

        String decrypt_message_key2 = object.decryption_method(encrypt_message_key1, key2);
        //     System.out.println("decrypt_message_key2 " + decrypt_message_key2 +"\n\n");



        /**
         * DES encrypt with K3
         */

        String encrypt_message_key3 = object.encryption_method(decrypt_message_key2, key3);
        System.out.println("\nencrypt_message_key3 " + encrypt_message_key3);

        return encrypt_message_key3;

    }

    /**
     * This method is used to do decryption using triple DES
     *      Formula for Decryption: Plaintext = Decrypt( Encrypt( Decrypt(encrypted,K1), K2), k3)
     * Note: K1 in encrypt becomes K3 in decrypt and visa-versa.
     *
     * @param encrypted_text: This will be the encrypted text in binary format
     *                        key1, key2 and key3 are in utf format
     * @param keyList: List of all the three keys from the GUI as entered by the user
     * @return returns the decrypted text in UTF-8 format
     */
    public  String tripleDES_decryption(String encrypted_text, String keyList){

        String key1, key2, key3;

        // splitting the given string containing all the keys based on new line regular expression

        String keyArray[] = keyList.split("\n");

        key1 = keyArray[0].substring(4);
        key2 = keyArray[1].substring(4);
        key3 = keyArray[2].substring(4);

        if(key1.length() < 1 || key2.length() < 1 || key3.length() < 1 ){
            Alert alert = new Alert(Alert.AlertType.WARNING, "No file Selected \n" +
                    "Please try again later ", ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK)
                throw new RuntimeException("Invalid input");
        }


        /**
         *  DES decrypt with K1
         */

        String decrypt_message_1 = object.decryption_method(encrypted_text, key1);
        //       decrypt_message_1 = bin_to_utf(decrypt_message_1);
        //       System.out.println("Decrypt message 1: \n" + decrypt_message_1 + "\n");



        /**
         * DES Encryptcrypt with K2
         */
        String encrypt_message_key2 = object.encryption_method(decrypt_message_1, key2);
        //       System.out.println("encrypt_message_key2: " + encrypt_message_key2);



        /**
         * DES decrypt with K3
         */
        String decrypt_message_key3 = object.decryption_method(encrypt_message_key2, key3);
        System.out.println("\ndecrypt_message_key3 " + decrypt_message_key3);

        System.err.println("final: " + object.bin_to_utf(decrypt_message_key3));

        return object.bin_to_utf(decrypt_message_key3);
    }


    /**
     * @param utf_message
     * @return binaryResult - binary representation of the message entered
     *
     * @author billjamesdev StackOverFlow - https://stackoverflow.com/questions/36534346/converting-text-to-binary-in-java
     * @author gauravSatija
     */
    private String utf_to_bin(String utf_message){

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
     * Hash Function from user <b>sfussenegger</b> on stackoverflow
     *
     * @param string : String to hash
     * @return 64-bit long hash value
     * @source http://stackoverflow.com/questions/1660501/what-is-a-good-64bit-hash-function-in-java-for-textual-strings
     *        & https://github.com/deadlytea/DES
     */

    // adapted from String.hashCode()
    private static long hash(String string) {
        long h = 1125899906842597L; // prime
        int len = string.length();

        for (int i = 0; i < len; i++) {
            h = 31*h + string.charAt(i);
        }
        return h;
    }


    /** CHECK how to create a 64_bit key
     *
     *
     * @param key_64 - the key entered by the user.
     * @return this method doesn't return anything but it stores all the key in the key list function.
     */
    private void setKeyList(String key_64){

        //Convert:  64-bit key -> (PC-1) -> 56-bit key
        String key_56="";

        // Here the 64-bit key is converted into the 56-bit key and permutation happens

        // Conversion of 64 bit key to 56 bit key happens using PC1 box from DES_boxes.java
        for (int i = 0; i < des_boxes_triple_des.PC1.length; i++)
            key_56 = key_56 + key_64.charAt(des_boxes_triple_des.PC1[i] - 1);


        //56 bit -> Two 28-bit keys (key-left & key-right) -> binary Left-shift operations -> 56bit key

        String key_28_left = key_56.substring(0, key_56.length()/2),
                key_28_right= key_56.substring(key_56.length()/2);

        // parseInt("1100110", 2) returns 102

        for(int i = 0; i < number_of_rounds; i++){

            // Left shift based on the key_shift array in DES_boxes
            key_28_left = key_28_left.substring( des_boxes_triple_des.key_shift[i]) +
                    key_28_left.substring(0, des_boxes_triple_des.key_shift[i]);

            // Right shift based on the key_shift array in DES_boxes
            key_28_right = key_28_right.substring(des_boxes_triple_des.key_shift[i]) +
                    key_28_right.substring(0, des_boxes_triple_des.key_shift[i]);


            key_56 = key_28_left + key_28_right;

            String key_48 ="";


            // 56-bit key -> (PC-2) -> 48 bit key
            for (int j = 0; j < des_boxes_triple_des.PC2.length; j++)
                key_48 = key_48 + key_56.charAt( des_boxes_triple_des.PC2[j] - 1);

            // Add the 48_bit keys into the array key_48_bit
            // This convert the String into Long
            key_48_list[i] = Long.parseLong(key_48,2);


        }
    }



    /**
     * Feistel Network implementation
     *
     * @param message_plainText
     * @return the encrypted message
     */
    private String feistelNetwork(String message_plainText, enum_encrypt_or_decrypt enum_value){

        // - Check if the text length is a multiple of 64
        // - Do padding to make the text length = 64*n. Add 0s in the starting.
        // !!! You can throw error here based on the person if they want specifically 64 bits !!! But this code is flexible
        while(message_plainText.length() % 64 != 0)
            message_plainText = "0" + message_plainText;



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
                String message_f_32="";
                if(enum_value == enum_encrypt_or_decrypt.DECRYPTION) {
                    message_f_32 = f_decrypt(message_right_32, round, enum_value);
                }
                if(enum_value == enum_encrypt_or_decrypt.ENCRYPTION) {
                    message_f_32 = f(message_right_32, round, enum_value);

                }
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
     *
     * @param message_right_32
     * @param round_number
     *
     * @return message_permutation_32 : this is the right block of 32 bits
     *         after all the 4 operations happened
     */
    private String f(String message_right_32, int round_number, enum_encrypt_or_decrypt enum_value){

        // Expansion box or Expansion function

        // String that contain the expanded message
        String message_expan_48 ="";
        for (int j = 0; j < des_boxes_triple_des.expansion_box.length; j++)
            message_expan_48 = message_expan_48 + message_right_32.charAt( des_boxes_triple_des.expansion_box[j] - 1);

        long message_int_48 = Long.parseLong(message_expan_48,2);
        long message_xor_48 = 0;

        // XOR
        /**
         * This is the only difference between encryption and decryption
         */
        if(enum_value == enum_encrypt_or_decrypt.DECRYPTION) {
            message_xor_48 = message_int_48 ^ key_48_list[number_of_rounds - round_number - 1];
        }
        if(enum_value == enum_encrypt_or_decrypt.ENCRYPTION) {
            message_xor_48 = message_int_48 ^ key_48_list[round_number];
        }

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
            int s_box_value = des_boxes_triple_des.s_boxes[i][row][column];
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
        for(int i=0; i < des_boxes_triple_des.permutation_sbox.length; i++)
            message_permutation_32 = message_permutation_32.substring( 0, des_boxes_triple_des.permutation_sbox[i]-1) +
                    message_sbox_32.charAt( des_boxes_triple_des.permutation_sbox[i]-1) +
                    message_permutation_32.substring( des_boxes_triple_des.permutation_sbox[i]);


        /** CHECK for error when  DES_boxes.permutation_sbox[i]+1 = 64 i.e
         * Index out of bound. One possible solution might be to add another '-' at the end
         * */

        return message_permutation_32;
    }



    /**
     * @param encrypt_text_gui
     * @param key_gui
     * @return
     */
    private String decryption_method(String encrypt_text_gui, String key_gui) {

        key_gui = utf_to_bin("" + hash(key_gui) );
        setKeyList(key_gui);

        String decrypt_message = feistelNetwork_decrypt(encrypt_text_gui);//, enum_decrypt_or_decrypt.DECRYPTION);

//       System.out.println("decrypted message: " + decrypt_message + "\n length: " + decrypt_message.length());
//       System.out.println("Decrypted message: "+decrypt_message +"\n");
//       bin_to_utf(decrypt_message);

        return decrypt_message;
    }



    /**
     * The following method is same as the main(String[] args) method but is used to by the GUI class and other methods
     *
     * Input format:  Binary
     * Output format: Binary
     *
     * @param binary_text
     * @param key
     * @return
     */
    private String encryption_method(String binary_text, String key) {

        //plaintext_gui = des_encryptionTripleDES.utf_to_bin(plaintext_gui);
        key = utf_to_bin("" + hash(key)  );

        setKeyList(key);

        String encrypt_message = feistelNetwork(binary_text, enum_encrypt_or_decrypt.ENCRYPTION);

        return encrypt_message;
    }


    /**
     * This converts the string in binary format to UTF-8 format
     * @param bin: String in binary format
     * @return string in UTF-8 format
     */
    public String bin_to_utf(String bin) {


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


    /**
     *
     * @param message_right_32
     * @param round_number
     * @param enum_value
     * @return
     */
    public String f_decrypt(String message_right_32, int round_number, enum_encrypt_or_decrypt enum_value){


        // Expansion box
        String message_expan_48 ="";
        for (int j = 0; j < des_boxes_triple_des.expansion_box.length; j++)
            message_expan_48 = message_expan_48 + message_right_32.charAt( des_boxes_triple_des.expansion_box[j] - 1);

        long message_int_48 = Long.parseLong(message_expan_48,2);


        // XOR
        /**
         * This is the only difference between encryption and decryption
         */
        long message_xor_48 = message_int_48 ^ key_48_list[ number_of_rounds - round_number - 1];


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
        messages_sbox_6[7] =  message_xor_48_string.substring(7*6);

        // S-box calculations
        for(int i =0; i < messages_sbox_6.length; i++){

            // Get columns
            int row   = Integer.parseInt(messages_sbox_6[i].charAt(0) + "" + messages_sbox_6[i].charAt(5), 2),
                    column= Integer.parseInt(messages_sbox_6[i].substring(1,5),2);

            // Find the value in the S-box based on row and column
            int s_box_value = des_boxes_triple_des.s_boxes[i][row][column];
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
        // Remeber permutation indexes start from 1
        for(int i=0; i < des_boxes_triple_des.permutation_sbox.length; i++)
            message_permutation_32 = message_permutation_32.substring( 0, des_boxes_triple_des.permutation_sbox[i]-1) +
                    message_sbox_32.charAt( des_boxes_triple_des.permutation_sbox[i]-1) +
                    message_permutation_32.substring( des_boxes_triple_des.permutation_sbox[i]);


        /** CHECK for error when  DES_boxes.permutation_sbox[i]+1 = 64 i.e
         * Index out of bound. One possible solution might be to add another '-' at the end
         * */

        return message_permutation_32;
    }



    /**
     * Feistel Network implementation
     *
     * @param message_plainText
     * @return the encrypted message
     */
    public String feistelNetwork_decrypt(String message_plainText){

        // - Check if the text length is a multiple of 64
        // - Do padding to make the text length = 64*n. Add 0s in the starting
        if(message_plainText.length() % 64 != 0)
            throw new RuntimeException("Invalid input");


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

                // Put the left 32 bit into f function
                String message_f_32 = f_decrypt(message_left_32, round, enum_encrypt_or_decrypt.DECRYPTION);

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


}

