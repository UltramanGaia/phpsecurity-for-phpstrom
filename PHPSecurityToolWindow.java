package cn.ultramangaia.gui;

import cn.ultramangaia.config.VulnType;
import cn.ultramangaia.utils.UIHelper;
import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

public class PHPSecurityToolWindow {
    private JPanel myToolWindowContent;
    private JButton expandButton;
    private JTree myTree;
    private JComboBox sourceLevelComboBox;
    private JComboBox vulnTypeComboBox;

    public PHPSecurityToolWindow(ToolWindow toolWindow) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("PHP Security");
        DefaultTreeModel model = new DefaultTreeModel(rootNode);
        myTree.setModel(model);




        expandButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                expandAll(myTree, new TreePath(myTree.getModel().getRoot()),true);
            }
        });

        DefaultComboBoxModel sourceLevelModel = new DefaultComboBoxModel(new String [] {"0", "1", "2"});
        sourceLevelComboBox.setModel(sourceLevelModel);


        DefaultComboBoxModel vulnTypeModel = new DefaultComboBoxModel(new String [] {
                VulnType.COMMAND_EXEC.name(),
                VulnType.CODE_EXEC.name(),
                VulnType.SQL_INJECTION.name(),
                VulnType.FILE_INCLUTION.name(),
                VulnType.XSS.name(),
                VulnType.FILE_DISCLOSURE.name(),
                VulnType.FILE_DISCLOSURE.name(),
                VulnType.FILE_MANIPULATION.name(),
                VulnType.XPATH_INJECTION.name(),
                VulnType.LDAP_INJECTION.name(),
                VulnType.ALL_TYPE.name(),
        });
        vulnTypeComboBox.setModel(vulnTypeModel);


        UIHelper.resultTree = myTree;
        UIHelper.resultModel = model;
        UIHelper.resultTreeRootNode = rootNode;
        UIHelper.sourceLevelModel = sourceLevelModel;
        UIHelper.vulnTypeModel = vulnTypeModel;
    }

    private void expandAll(JTree tree, TreePath parent, boolean expand) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();

        if (node.getChildCount() > 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);

            }
        }
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }



    public JPanel getContent() {
        return myToolWindowContent;
    }

    private void createUIComponents() {
        myToolWindowContent = new JPanel();
    }
}
