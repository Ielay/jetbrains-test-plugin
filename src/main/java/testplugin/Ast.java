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

    public Ast(DefaultMutableTreeNode rootNode, int exceptionsCount, int variablesInitCount, int variablesAccessCount) {
        panel = new JPanel();
        left_panel = new JPanel();
        right_panel = new JPanel(new GridLayout(3, 1));

        panel.add(left_panel, LEFT_ALIGNMENT);
        panel.add(right_panel, RIGHT_ALIGNMENT);

        //create the tree by passing in the root node
        tree = new JTree(rootNode);

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();

        tree.setCellRenderer(renderer);
        tree.setShowsRootHandles(true);
        tree.setRootVisible(true);

        left_panel.add(new JScrollPane(tree));

        JLabel exceptionsNumber = new JLabel("Exceptions: " + String.valueOf(exceptionsCount));
        JLabel variablesInitNumber = new JLabel("Variables init: " + String.valueOf(variablesInitCount));
        JLabel variablesAccessNumber = new JLabel("Variables access: " + String.valueOf(variablesAccessCount));

        right_panel.add(exceptionsNumber);
        right_panel.add(variablesInitNumber);
        right_panel.add(variablesAccessNumber);

        this.add(panel);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle("AST");
        this.setSize(800, 600);
        this.setVisible(true);
    }
}
