package com.example.t23new.visual;

import com.example.t23new.tree.TwoThreeTree;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private VBox vBox;
    @FXML
    private TextField keyField;
    @FXML
    private TextField valField;


    private TwoThreeTree<Integer, String> tree = new TwoThreeTree<>(Integer.class);

    private BstPane<Integer, String> treePane = new BstPane<>(tree);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        treePane.setPrefWidth(1280);
        treePane.setPrefHeight(1024);
        vBox.getChildren().add(0, treePane);
        treePane.displayTree();
    }

    @FXML
    private void put() {
        Integer key = parseInt(keyField.getText());
        if (key == null)
            return;
        tree.insert(key, valField.getText());
        treePane.displayTree();
    }

    @FXML
    private void delete() {
        Integer key = parseInt(keyField.getText());
        if (key == null)
            return;
        tree.remove(key);
        treePane.displayTree();
    }

    @FXML
    private void change() {
        Integer key = parseInt(keyField.getText());
        if (key == null)
            return;
        tree.set(key, valField.getText());
        treePane.displayTree();
    }

    @FXML
    private void clear() {
        tree.clear();
        treePane.displayTree();
    }

    private static Integer parseInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (Exception exception) {
            return null;
        }
    }
}