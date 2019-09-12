package testplugin;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * @author petrov
 * @since 12.09.19
 */
public class Ast extends JFrame {

    private JTree tree;
    private JPanel panel;
    private JPanel left_panel;
    private JPanel right_panel;

//    private JLabel selectedLabel;

    public Ast(DefaultMutableTreeNode rootNode, int exceptionsCount, int variablesInitCount, int variablesAccessCount) {
        JLabel exceptionsNumber = new JLabel("Exceptions: " + String.valueOf(exceptionsCount));
        JLabel variablesInitNumber = new JLabel("Variables init: " + String.valueOf(variablesInitCount));
        JLabel variablesAccessNumber = new JLabel("Variables access: " + String.valueOf(variablesAccessCount));

        //create the tree by passing in the root node
        tree = new JTree(rootNode);

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();

        tree.setCellRenderer(renderer);
        tree.setShowsRootHandles(true);
        tree.setRootVisible(true);

        add(new JScrollPane(tree));

//        selectedLabel = new JLabel();
//        add(selectedLabel, BorderLayout.SOUTH);
//        tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
//            @Override
//            public void valueChanged(TreeSelectionEvent e) {
//                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
//                selectedLabel.setText(selectedNode.getUserObject().toString());
//            }
//        });

        add(exceptionsNumber);
        add(variablesInitNumber);
        add(variablesAccessNumber);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle("AST");
        this.setSize(800, 600);
        this.setVisible(true);
    }
}