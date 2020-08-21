import GUI_DES.GUI_DES_javafx;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;

public class rough {
    public static void main(String[] args) {

        JFrame temp_frame = new JFrame();

        String file_location = JOptionPane.showInputDialog(temp_frame,"Enter the file location");
        temp_frame.setBackground(Color.black);
        temp_frame.setVisible(true);
        try {

            System.out.println(file_location);
        }catch (Exception e){
            JDialog dialog_box = new JDialog();
            dialog_box.setTitle("Exception");

            JLabel temp_label = new JLabel("Exception Occurred!!!"+
                    "\nSystem Could not load the file. Please try later. ");
            dialog_box.add(temp_label);
            dialog_box.setSize(300,200);
            dialog_box.setVisible(true);

        }

    }
}