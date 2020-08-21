// Java program to create open or
// save dialog using JFileChooser

import javax.swing.*;
import java.awt.event.*;
import javax.swing.filechooser.*;

/**
 * Source code: https://www.geeksforgeeks.org/java-swing-jfilechooser/
 * @author andrew: https://auth.geeksforgeeks.org/user/andrew1234/articles
 * @author gauravsatija
 */




public class File_Chooser_Class extends JFrame implements ActionListener {

    // Jlabel to show the files user selects
    static JLabel label;

    File_Chooser_Class() {

    }

    public static void main(String args[]){
        File_Chooser_Class file_chooser_class = new File_Chooser_Class();
        file_chooser_class.get_file_location();
        // frame to contains GUI elements
        JFrame frame = new JFrame("file chooser");

        // set the size of the frame
        frame.setSize(400, 400);

        // set the frame's visibility
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // button to open save dialog
        JButton button1 = new JButton("save");

        // button to open open dialog
        JButton button2 = new JButton("open");

        // make an object of the class file chooser
        File_Chooser_Class f1 = new File_Chooser_Class();

        // add action listener to the button to capture user
        // response on buttons
        button1.addActionListener(f1);
        button2.addActionListener(f1);

        // make a panel to add the buttons and labels
        JPanel p = new JPanel();

        // add buttons to the frame
        p.add(button1);
        p.add(button2);

        // set the label to its initial value
        label = new JLabel("no file selected");

        // add panel to the frame
        p.add(label);
        frame.add(p);

        frame.show();
    }

    public void actionPerformed(ActionEvent evt) {
        // if the user presses the save button show the save dialog
        String com = evt.getActionCommand();

        if (com.equals("save")) {
            // create an object of JFileChooser class
            JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

            // invoke the showsSaveDialog function to show the save dialog
            int r = j.showSaveDialog(null);

            // if the user selects a file
            if (r == JFileChooser.APPROVE_OPTION)

            {
                // set the label to the path of the selected file
                label.setText(j.getSelectedFile().getAbsolutePath());
            }
            // if the user cancelled the operation
            else
                label.setText("the user cancelled the operation");
        }

        // if the user presses the open dialog show the open dialog
        if (com.equals("open")) {
            // create an object of JFileChooser class
            JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

            // invoke the showsOpenDialog function to show the save dialog
            int r = j.showOpenDialog(null);

            // if the user selects a file
            if (r == JFileChooser.APPROVE_OPTION) {
                // set the label to the path of the selected file
                label.setText(j.getSelectedFile().getAbsolutePath());
            }
            // if the user cancelled the operation
            else
                label.setText("the user cancelled the operation");
        }
    }

    public static String get_file_location(){
        JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        // invoke the showsOpenDialog function to show the save dialog
        int r = j.showSaveDialog(null);

        // if the user selects a file
        if (r == JFileChooser.APPROVE_OPTION) {
            // set the label to the path of the selected file
            System.out.println(j.getSelectedFile().getAbsolutePath());
            label.setText(j.getSelectedFile().getAbsolutePath());
        }
        // if the user cancelled the operation
        else
            label.setText("the user cancelled the operation");

        return label.getText();
    }
}
