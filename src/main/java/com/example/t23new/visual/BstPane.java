package com.example.t23new.visual;


import com.example.t23new.tree.TwoThreeTreeGovno;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;


public class BstPane<K extends Comparable<K>, V> extends Pane {
    private TwoThreeTreeGovno<K, V> tree;
    private double radius = 15;
    private double vGap = 50;

    protected BstPane() {
    }

    public BstPane(TwoThreeTreeGovno<K, V> tree) {
        this.tree = tree;
        setBackground(new Background(new BackgroundFill(Color.web("#" + "40E0D0"), CornerRadii.EMPTY, Insets.EMPTY)));
    }


    public void displayTree() {
        this.getChildren().clear();
        if (tree.getRoot() != null) {
            displayTree(tree.getRoot(), getPrefWidth() / 2, vGap, getPrefWidth() / 4, Color.MEDIUMPURPLE);
        }
    }

    protected void displayTree(TwoThreeTreeGovno<K, V>.Node node, double x, double y, double hGap, Color color) {
        if (node instanceof TwoThreeTreeGovno<K, V>.TreeNode treeNode) {
            if (treeNode.children[0] != null) {
                getChildren().add(new Line(x - hGap, y + vGap, x, y));
                displayTree(treeNode.children[0], x - hGap, y + vGap, hGap / 2, color);
            }

            if (treeNode.children[1] != null) {
                getChildren().add(new Line(x, y + vGap, x, y));
                displayTree(treeNode.children[1], x, y + vGap, hGap / 2, color);
            }

            if (treeNode.children[2] != null) {
                getChildren().add(new Line(x + hGap, y + vGap, x, y));
                displayTree(treeNode.children[2], x + hGap, y + vGap, hGap / 2, color);
            }
        }


        Circle circle = new Circle(x, y, radius);
        circle.setFill(color);
        circle.setStroke(Color.BLACK);

        Label info = new Label(node.toString());

        info.setLayoutX(x);
        info.setLayoutY(y);
        getChildren().addAll(circle, info);
    }

}