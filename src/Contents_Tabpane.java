import DES.DES_decryption;
import DES.DES_encryption;
import GUI_DES.Contents_Collection;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import static javafx.geometry.Pos.CENTER;

public class Contents_Tabpane extends Pane{

    Button button_key,
            button_encryption = new Button("Encryption"),
            button_decryption = new Button("Decryption");

    TextField text_goes_here;
    Contents_Collection contents_collection;
    DES_encryption des_encryption;
    DES_decryption des_decryption;

    Contents_Tabpane(){
//        button_key = new Button("Click me");


        text_goes_here = new TextField();


//        StackPane layout = new StackPane();
//        layout.getChildren().add(button_key);
//        Scene scene = new Scene(layout, 300, 250);

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





        ScrollPane scrollPane_window = new ScrollPane();
        scrollPane_window.setFitToHeight(true);
        scrollPane_window.setFitToWidth(true);


        VBox vBox = new VBox();


        MenuBar menuBar = new MenuBar();

        Menu menu_file = new Menu("File"),
                menu_edit = new Menu("Edit"),
                menu_help = new Menu("Help");

        menuBar.getMenus().addAll(menu_file, menu_edit, menu_help);

        TabPane tabPane = new TabPane();

        SplitPane splitPane_horizontal = new SplitPane();
        SplitPane splitPane_vertical_left = new SplitPane(),
                splitPane_vertical_right= new SplitPane();



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
        splitPane_vertical_left.getItems().addAll( contents_collection.textArea_top_left,
                contents_collection.textArea_bottom_left
        );




        // Setting margin
        splitPane_horizontal.setDividerPositions(0.8);
        splitPane_vertical_left.setDividerPositions(0.8);
        splitPane_vertical_right.setDividerPositions(0.8);

        // setting horizontal pane divider's range
        /**
         * Things to do:
         * Set the divider's position
         */

        // Setting properties of the split pane and divider
        // splitPane_horizontal.setMouseTransparent(true);

        /**
         * HERE look for new model
         */


        /*
        From the following line we are going to use functions which help in transferring data between
        the file, GUI and DES algorithm.
        */

        String file_location = "/Users/gauravsatija/IdeaProjects/DistributedSystem_CPSC441/src/roughWork";
        String string_input = "the secret",//getInputString(file_location),// contents_collection.textArea_right.getText(),
                string_key   =  "secretkey";

        contents_collection.textArea_top_left.setText(string_input);

        // Due to the following error we have to make string_output as a final String array
        // java: local variables referenced from an inner class must be final or effectively final
        final String[] string_output = new String[1];
        des_encryption = new DES_encryption();
        des_decryption = new DES_decryption();


        /**
         * Button- constructor, Action listener
         */

        button_encryption = new Button("Encryption");
        button_decryption = new Button("Decryption");
        button_encryption.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("working");
                String string_output = des_encryption.main_method_for_gui(contents_collection.textArea_top_left.getText(), contents_collection.textArea_right.getText());
                contents_collection.textArea_bottom_left.setText(string_output);
                System.out.println("working");
            }
        });

        button_decryption.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                System.out.println("working1");
                String string_output = des_decryption.main_method_for_gui(contents_collection.textArea_top_left.getText(), contents_collection.textArea_right.getText());
                contents_collection.textArea_bottom_left.setText(string_output);
                System.out.println("working1");
            }
        });



        Label label_local = new Label("Enter Key Below:");
        label_local.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        label_local.setAlignment(CENTER);

        Label label_instruction = new Label("Enter key greater than 8 characters");
        label_instruction.setFont(Font.font("Arial", FontWeight.NORMAL, 15));

        button_decryption.setPrefSize(300, 75);
        button_encryption.setPrefSize(300, 75);


        VBox pane2 = new VBox(14);
        pane2.getChildren().addAll( label_local, label_instruction ,contents_collection.textArea_right,
                // contents_collection.textArea_bottom_right, // Add this when you are ready to do triple DES for
                // now I am focusing on DES only
                button_encryption, button_decryption
        );

        // Alignment for the
        button_encryption.setAlignment(CENTER);
        button_decryption.setAlignment(CENTER);
        //pane2.setAlignment(CENTER);



        splitPane_horizontal.getItems().addAll(splitPane_vertical_left, pane2);

        Pane pane = new Pane();
        pane.getChildren().add(splitPane_horizontal);
        //splitPane_horizontal.resize(window_main.getMaxWidth()-200, window_main.getMaxHeight()-200);



        this.getChildren().addAll(pane);
    }




    public String getInputString(String file_location) throws IOException {
        File file = new File(file_location);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String string, input_list = "";
        while ((string = br.readLine()) != null) {
            input_list = input_list + string;
        }

        return input_list;
    }

}
