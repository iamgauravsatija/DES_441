package GUI_DES;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.FontWeight;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import static javafx.geometry.Pos.CENTER;

public class Contents_Collection extends Node {

    /*
        When the users want to add a new tab, they will be asked if it is for encryption or decryption.
        Based on the selection the initial text inside the components will be on of the following:
            1. Enter the plain text here
            2. Enter the cipher text here

    */

        /**
         * NOTE: need a constructor to give the string variables values
         */

        String string_top_left = "Your Plaintext/Encrypted text goes here.",
               string_bottom_left = "Decrypted/Encrypted message appears here",
               string_bottom_right = "Your key goes here";


        // Components based on their positions in the Application Window

        // TOP LEFT
        public TextArea textArea_top_left = new textField_with_focusListener(string_top_left);

        // BOTTOM LEFT
        public TextArea textArea_bottom_left = new textField_with_focusListener(string_bottom_left);

        // TOP RIGHT
        public Label label_top_right = new label_top_right_class();
        public TextArea textArea_right = new textArea_right_class();
        public Button button_right = new button_right_class();


        // BOTTOM RIGHT
        public TextArea textArea_bottom_right = new textField_with_focusListener(string_bottom_right);


        @Override
        protected NGNode impl_createPeer() {
            return null;
        }

        @Override
        public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
            return null;
        }

        @Override
        protected boolean impl_computeContains(double localX, double localY) {
            return false;
        }

        @Override
        public Object impl_processMXNode(MXNodeAlgorithm alg, MXNodeAlgorithmContext ctx) {
            return null;
        }


        /**
         * The following text idea/inspiration is taken from
         *
         * @author Bart Kiers: https://stackoverflow.com/users/50476/bart-kiers
         * from the following link: https://stackoverflow.com/questions/1738966/java-jtextfield-with-input-hint
         */

        class textField_with_focusListener extends TextArea implements FocusListener {

            private String text_inside;
            private boolean showing_default_text;

            public textField_with_focusListener(String text_inside) {
                super(text_inside);
                this.text_inside = text_inside;
                this.showing_default_text = true;
                //this.addFocusListener(this);

            }

            @Override
            public void focusGained(FocusEvent e) {
                if (this.getText().isEmpty()) {
                    super.setText(text_inside);
                    showing_default_text = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (this.getText().isEmpty()) {
                    super.setText(text_inside);
                    showing_default_text = true;
                }
            }

        }

        class label_top_right_class extends Label {

            public label_top_right_class() {
                Label label = new Label("Enter Key Below:");
                label.setFont(javafx.scene.text.Font.font("Arial", FontWeight.BOLD, 20));
                label.setAlignment(CENTER);

                this.setVisible(true);
            }
        }


        public class textArea_right_class extends TextArea{

            public textArea_right_class() {

                TextArea textArea = new TextArea("TextArea for Key");
                textArea.setWrapText(true);
                textArea.getScrollTop();
                textArea.setPrefRowCount(5);
                textArea.setWrapText(true);
//            textArea.setPrefSize();

            }
        }


        class button_right_class extends Button{

            public button_right_class() {

                Button button_encrypt_or_decrypt = new Button("encrypt/decrypt");
                // button_encrypt_or_decrypt.addActionListener();
                button_encrypt_or_decrypt.setBackground(Color.RED);
                button_encrypt_or_decrypt.setFont(java.awt.Font.getFont(java.awt.Font.SANS_SERIF));

            }
        }


    }



