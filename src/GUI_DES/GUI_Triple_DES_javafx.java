package GUI_DES;

import Triple_DES.Algorithm_Triple_DES;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URL;

import static javafx.geometry.Pos.CENTER;

/**
 *
 * @author gauravsatija
 **/


public class GUI_Triple_DES_javafx extends Application {


    // Initial number of tabs
    int number_of_tabs = 2;

    // Temporary frame object which can be used within functions for trying out new features etc.
    Frame temp_frame;

    // Stage on which the whole Application will be built
    Stage window_main; // stage

    // Buttons for encrypting and decrypting the data
    Button button_encryption = new Button("Encryption"),
            button_decryption = new Button("Decryption");


    // Object of Contents_Collection which contains all the text area related components
    // It is good to have all the text areas under one object as the implementation of all the
    // feature is easy now and in future. Moreover, the troubleshooting also becomes much faster and easier.
    Contents_Collection contents_collection;

    // Tab Pane that will contain all the tabs
    TabPane tabPane = new TabPane();

    // Split_Pane_extended class will contain all the components in a tab
    // For more information on this class scroll down. It is the last class in this file.
    Split_Pane_extended tabs_pane_array[] = new Split_Pane_extended[number_of_tabs];

    // Array of all the tabs.
    Tab tabs_array[] = new Tab[number_of_tabs];

    // Object of Algorithm_Triple_DES for calling encryption and decryption
    // methods
    Algorithm_Triple_DES des_encryption, des_decryption;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // primaryStage aka window_main is the Stage which will contain
        // everything i.e all the panes, components etc.
        window_main = primaryStage;
        window_main.setTitle("Triple DES messenger");


        // object of the Contents_Collection class that contains all the textArea components
        contents_collection = new Contents_Collection();

        /**
         *
         * 1. Define Scroll pane and it's properties
         *  - ADD vbox
         * 2. Define menu and it's properties
         *      - Define it'3 three contents
         *      - Add menu to pane
         * 3. Tab pane {closing policy etc.}
         *      - Define properties
         *      - Add content
         * 4. Split panes 1 horizontal -> 2 vertical
         *  ...
         *
         *
         *
         */


        // Scroll pane is used so that if the user reduce the size of the window
        // the can easily scroll and navigate
        ScrollPane scrollPane_window = new ScrollPane();
        scrollPane_window.setFitToHeight(true);
        scrollPane_window.setFitToWidth(true);

        // Vbox is used to make the components appear in a vertical list like Menu bar then panes etc.
        VBox vBox = new VBox();


        // menuBar object will contain all the 3 menus File, edit and help
        MenuBar menuBar = new MenuBar();

        // The 3 menus inside the menuBar
        Menu menu_file = new Menu("File"),
                menu_edit = new Menu("Edit"),
                menu_help = new Menu("Help");

        // Menu items under File
        // MenuItem new_tab = new MenuItem("New Tab"),
        MenuItem close_window_file = new MenuItem("Close Window"),
                open_file = new MenuItem("Open"),
                save_file = new MenuItem("Save As");

        // Menu items under Edit
        MenuItem clear_all = new MenuItem("Clear Everything"),
                clear_key = new MenuItem("Clear Key"),
                clear_message = new MenuItem("Clear Message");


        // Menu items under Help
        MenuItem see_tutorial_video = new MenuItem("See Video");


        menu_file.getItems().addAll(open_file, save_file, close_window_file);
        menu_edit.getItems().addAll(clear_all, clear_message, clear_key);
        menu_help.getItems().addAll(see_tutorial_video);


        /**
         * Properties of different buttons in the menu bar
         *  File Menu:
         *      open_file : To select the file and get the text inside the file selected
         *      save_file : Saves the data obtained in a .txt format file
         *      close_window_file: Closes the window and stops all the process by calling  System.exit(0);
         *
         *  Edit Menu:
         *      clear_all: Clears the data inside all 3 text areas.
         *      clear_message: Clears the textArea containing the default message or input by the User
         *      clear_key: Clears the textArea containing key
         *
         *  Help:
         *      see_tutorial_video: Opens the video that shows how to use the App
         *
         */


        close_window_file.onActionProperty().setValue(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });

        clear_all.onActionProperty().setValue(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                int index = tabPane.getSelectionModel().getSelectedIndex();
                System.out.println("*** \n\n " + index + "\n\n\n***");
                tabs_pane_array[index].contents_collection.textArea_top_left.setText("");
                tabs_pane_array[index].contents_collection.textArea_bottom_left.setText("");
                tabs_pane_array[index].contents_collection.textArea_right.setText("");

            }
        });

        clear_key.onActionProperty().setValue(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                int index = tabPane.getSelectionModel().getSelectedIndex();
                // you can check the index of the selected tab on the output screen
                //System.out.println("*** \n\n "+index+"\n\n\n***");

                // changing the text to nothing/blank i.e. ""
                tabs_pane_array[index].contents_collection.textArea_right.setText("");

            }
        });

        clear_message.onActionProperty().setValue(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int index = tabPane.getSelectionModel().getSelectedIndex();
                tabs_pane_array[index].contents_collection.textArea_top_left.setText("");
            }
        });
        see_tutorial_video.onActionProperty().setValue(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Hyperlink link = new Hyperlink();
                try {
                    URL url = new URL("https://drive.google.com/file/d/1iZP5AFlFgpOobVFkkAUbBNafHGbrxGC1/view?usp=sharing");
                    openWebpage(url.toURI());
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


/*
        From the following line we are going to use functions which help in transferring data between
        the file, GUI and DES algorithm.
*/
        open_file.onActionProperty().setValue(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                /**
                 * The following code is insipired from the code given on the link.
                 * Source code: https://www.geeksforgeeks.org/java-swing-jfilechooser/
                 *
                 * @author andrew: https://auth.geeksforgeeks.org/user/andrew1234/articles
                 * @author gauravsatija
                 */
                JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

                jFileChooser.setVisible(true);
                // invoke the showsOpenDialog function to show the save dialog
                int result = jFileChooser.showOpenDialog(null);
                String file_data = "";

                // if the user selects a file
                if (result == JFileChooser.APPROVE_OPTION) {
                    // Read the file from the path selected and show the content inside it
                    // in the app's top left text area.
                    try {
                        file_data = getDataInFile(jFileChooser.getSelectedFile().getAbsolutePath());
                        tabs_pane_array[index].contents_collection.textArea_top_left.setText(file_data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // if the user cancelled the operation
                else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "No file Selected \n" +
                            "Please try again later ", ButtonType.OK);
                    alert.showAndWait();

                    if (alert.getResult() == ButtonType.OK)
                        throw new RuntimeException("Invalid input");

                }
            }
        });


        save_file.onActionProperty().setValue(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

/**
 * The following code is insipired from the code given on the link.
 * Source code: https://www.geeksforgeeks.org/java-swing-jfilechooser/
 *
 * @author andrew: https://auth.geeksforgeeks.org/user/andrew1234/articles
 * @author gauravsatija
 */
                int index = tabPane.getSelectionModel().getSelectedIndex();

                JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                jFileChooser.setVisible(true);

                // Invoke the showSaveDialog function to show the save dialog
                int result = jFileChooser.showSaveDialog(null);
                String save_data = tabs_pane_array[index].contents_collection.textArea_bottom_left.getText();
                System.out.println(save_data);
                // if the user selects a file
                if (result == JFileChooser.APPROVE_OPTION) {
                    // Read the text in the bottom left area and save the content inside it
                    // in the area selected. the file format will be .txt
                    PrintWriter writer = null;
                    try {
                        writer = new PrintWriter("" + jFileChooser.getSelectedFile().getAbsolutePath() + ".txt");
                        writer.print(save_data);
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                // if the user cancelled the operation
                else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "No file Selected \n" +
                            "Please try again later ", ButtonType.OK);
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.OK)
                        throw new RuntimeException("Invalid input");
                }
            }
        });


        menuBar.getMenus().addAll(menu_file, menu_edit, menu_help);


        SplitPane splitPane_horizontal = new SplitPane();
        SplitPane splitPane_vertical_left = new SplitPane(),
                splitPane_vertical_right = new SplitPane();


        // setting orientation
        splitPane_horizontal.setOrientation(Orientation.HORIZONTAL);
        splitPane_vertical_left.setOrientation(Orientation.VERTICAL);
        splitPane_vertical_right.setOrientation(Orientation.VERTICAL);

        // set resizable = true
        SplitPane.setResizableWithParent(splitPane_horizontal, true);
        SplitPane.setResizableWithParent(splitPane_vertical_left, true);
        SplitPane.setResizableWithParent(splitPane_vertical_right, true);


        //editable
        contents_collection.textArea_top_left.setEditable(true);
        contents_collection.textArea_bottom_left.setEditable(false);
        contents_collection.textArea_bottom_right.setEditable(true);

        //wrap text
        contents_collection.textArea_top_left.setWrapText(true);
        contents_collection.textArea_bottom_left.setWrapText(true);
        contents_collection.textArea_bottom_right.setWrapText(true);


        // Adding content/items to the Pane
        splitPane_vertical_left.getItems().addAll(contents_collection.textArea_top_left,
                contents_collection.textArea_bottom_left
        );


        // Setting margin
        splitPane_horizontal.setDividerPositions(0.8);
        splitPane_vertical_left.setDividerPositions(0.8);
        splitPane_vertical_right.setDividerPositions(0.8);


        // Due to the following error we have to make string_output as a final String array
        // java: local variables referenced from an inner class must be final or effectively final
        final String[] string_output = new String[1];
        des_encryption = new Algorithm_Triple_DES();
        des_decryption = new Algorithm_Triple_DES();


        /**
         * Button- constructor, Action listener
         */

        button_encryption = new Button("Encryption");
        button_decryption = new Button("Decryption");
        button_encryption.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("working");
                String string_output = des_encryption.tripleDES_encryption(contents_collection.textArea_top_left.getText(), contents_collection.textArea_right.getText());
                contents_collection.textArea_bottom_left.setText(string_output);
                System.out.println("working");
            }
        });

        button_decryption.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                System.out.println("working1");
                String string_output = des_decryption.tripleDES_decryption(contents_collection.textArea_top_left.getText(), contents_collection.textArea_right.getText());
                contents_collection.textArea_bottom_left.setText(string_output);
                System.out.println("working1");
            }
        });


        Label label_local = new Label("Enter Key Below:");
        label_local.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        label_local.setAlignment(CENTER);

        Label label_instruction = new Label("-k1 secretkey1 \n-k2 secretkey2 \n-k3 secretkey3 \n");
        label_instruction.setFont(Font.font("Arial", FontWeight.NORMAL, 15));

        button_decryption.setPrefSize(300, 75);
        button_encryption.setPrefSize(300, 75);


        VBox pane2 = new VBox(14);
        pane2.getChildren().addAll(label_local, label_instruction, contents_collection.textArea_right,
                // contents_collection.textArea_bottom_right, // Add this when you are ready to do triple DES for
                // now I am focusing on DES only
                button_encryption, button_decryption
        );

        // Alignment for the
        button_encryption.setAlignment(CENTER);
        button_decryption.setAlignment(CENTER);
        //pane2.setAlignment(CENTER);


        /**
         * This is where the tabs are added to the TabPane.
         * All the tabs are put in an array so that it is easy for usage in other methods and
         * making changes to it's individual components
         *
         * For this version I am setting up only two tabs and not implementing the 'add a new tab'
         * feature due to some issues.
         */

        for (int i = 0; i < number_of_tabs; i++) {
            // Get the split panes from the Split_Pane_extended class.
            tabs_pane_array[i] = new Split_Pane_extended();

            // Creating a tab of from the
            tabs_array[i] = new Tab();

            // Setting the tab name and number based on its position in the list
            tabs_array[i].setText("Tab " + (i + 1));

            // Setting the content for each tab
            tabs_array[i].setContent(tabs_pane_array[i].getSplitPane());

            // Adding tabs to the tab pane
            tabPane.getTabs().addAll(tabs_array[i]);
        }


        vBox.setFillWidth(true);

        final BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(tabPane);
        Scene scene = new Scene(root);


        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();


        //set Stage boundaries to visible bounds of the main screen
        window_main.setX(primaryScreenBounds.getMinX());
        window_main.setY(primaryScreenBounds.getMinY());
        window_main.setWidth(primaryScreenBounds.getWidth());
        window_main.setHeight(primaryScreenBounds.getHeight());
        window_main.setScene(scene);
        window_main.show();

    }


    /**
     * This method is to read data from the file selected by the user. The method os given the file's
     * location. Object of BufferedReader is used to read the text inside the file.
     *
     * @param file_location - contains the location of the file from where the data has to be read.
     * @return input_list - Returns the data inside the file in form of a String.
     * @throws IOException
     */
    public String getDataInFile(String file_location) throws IOException {

        // Get the file based on file location
        File file = new File(file_location);

        // Creates a reader to read the file instance from above line.
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String string, input_list = "";
        // Read the file line-by-line and add it to the input_list
        while ((string = reader.readLine()) != null) {
            input_list = input_list + string;
        }

        return input_list;
    }




            /**
             * Split_Pane_extended class as the name suggest is an extended version of the SpiltPane class provided
             * by JDK. Because Split_Pane_extended contains all the components that will go inside the tab like
             * buttons, text area etc.
             */
            class Split_Pane_extended extends SplitPane {

                Contents_Collection contents_collection = new Contents_Collection();
                Button button_encryption = new Button("Encryption"),
                        button_decryption = new Button("Decryption");

                /**
                 * @return  A Splitpane which contains all components that directly goes inside a tab.
                 * @throws IOException
                 */
                public SplitPane getSplitPane() throws IOException {

                    SplitPane splitPane_horizontal = new SplitPane();
                    SplitPane splitPane_vertical_left = new SplitPane(),
                            splitPane_vertical_right = new SplitPane();


                    // setting orientation
                    splitPane_horizontal.setOrientation(Orientation.HORIZONTAL);
                    splitPane_vertical_left.setOrientation(Orientation.VERTICAL);
                    splitPane_vertical_right.setOrientation(Orientation.VERTICAL);

                    // set resizable = true
                    SplitPane.setResizableWithParent(splitPane_horizontal, true);
                    SplitPane.setResizableWithParent(splitPane_vertical_left, true);
                    SplitPane.setResizableWithParent(splitPane_vertical_right, true);


                    //editable
                    contents_collection.textArea_top_left.setEditable(true);
                    // The text area that contains the encrypted/decrypted text
                    // can't be edited by the user to maintain the security and
                    // integrity of this sensitive data.
                    contents_collection.textArea_bottom_left.setEditable(false);
                    contents_collection.textArea_bottom_right.setEditable(true);

                    //wrap text
                    contents_collection.textArea_top_left.setWrapText(true);
                    contents_collection.textArea_bottom_left.setWrapText(true);
                    contents_collection.textArea_bottom_right.setWrapText(true);


                    // Adding content/items to the Pane
                    splitPane_vertical_left.getItems().addAll(contents_collection.textArea_top_left,
                            contents_collection.textArea_bottom_left
                    );


                    // Setting margin
                    splitPane_horizontal.setDividerPositions(0.8);
                    splitPane_vertical_left.setDividerPositions(0.8);
                    splitPane_vertical_right.setDividerPositions(0.8);


                    // Due to the following error we have to make string_output as a final String array
                    // java: local variables referenced from an inner class must be final or effectively final

                    final String[] string_output = new String[1];
                    des_encryption = new Algorithm_Triple_DES();
                    des_decryption = new Algorithm_Triple_DES();


                    /**
                     * Buttons - constructor and Action Handler methods
                     *
                     * Below you can see the two buttons and what are the functions that are being called
                     * when we press them.
                     */

                    button_encryption = new Button("Encryption");
                    button_decryption = new Button("Decryption");

                    // button_encryption on click calls the main_method_for_gui inside the des_encryption
                    // It takes two arguments the message and the key from the GUI and returns the output.
                    button_encryption.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            //System.out.println("working");
                            String string_output = des_encryption.tripleDES_encryption(contents_collection.textArea_top_left.getText(), contents_collection.textArea_right.getText());
                            contents_collection.textArea_bottom_left.setText(string_output);
                            //System.out.println("working");
                        }
                    });


                    // button_decryption on click calls the main_method_for_gui inside the des_decryption
                    // It takes two arguments the message and the key from the GUI and returns the output. Just like above
                    button_decryption.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(javafx.event.ActionEvent event) {
                            //System.out.println("working1");
                            String string_output = des_decryption.tripleDES_decryption(contents_collection.textArea_top_left.getText(), contents_collection.textArea_right.getText());
                            contents_collection.textArea_bottom_left.setText(string_output);
                            //System.out.println("working1");
                        }
                    });


                    // The following labels appears on the right side of the app which contains the instructions
                    // on where & how to enter the key.
                    Label label_local = new Label("Enter Keys Below:");
                    label_local.setFont(Font.font("Arial", FontWeight.BOLD, 25));
                    label_local.setAlignment(CENTER);

                    Label label_instruction = new Label("-k1 secretkey1 \n-k2 secretkey2 \n-k3 secretkey3 \n");
                    label_instruction.setFont(Font.font("Arial", FontWeight.NORMAL, 15));

                    // Size of the buttons
                    button_decryption.setPrefSize(300, 75);
                    button_encryption.setPrefSize(300, 75);


                    // Vbox is used to make the components appear in a vertical list
                    VBox pane2 = new VBox(14);
                    pane2.getChildren().addAll(label_local, label_instruction, contents_collection.textArea_right,
                            button_encryption, button_decryption
                    );

                    // Alignment for the buttons in the Vbox
                    button_encryption.setAlignment(CENTER);
                    button_decryption.setAlignment(CENTER);


                    // Adding all the items in the split pane
                    splitPane_horizontal.getItems().addAll(splitPane_vertical_left, pane2);

                    return splitPane_horizontal;
                }

            }


    public static boolean openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}







