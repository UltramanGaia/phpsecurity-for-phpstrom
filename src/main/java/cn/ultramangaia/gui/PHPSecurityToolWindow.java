package cn.ultramangaia.gui;

import cn.ultramangaia.config.VulnType;
import cn.ultramangaia.utils.UiHelper;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;

/**
 * @author: Gaia
 * @date: 2019/4/16
 * @description: 工具窗口，非常丑
 */
public class PHPSecurityToolWindow {
    private JPanel myToolWindowContent;
    private JComboBox sourceLevelComboBox;
    private JComboBox vulnTypeComboBox;
    private JEditorPane editorPane1;
    private final static Logger logger = Logger.getInstance(PHPSecurityToolWindow.class);

    public PHPSecurityToolWindow(ToolWindow toolWindow) {

        DefaultComboBoxModel sourceLevelModel = new DefaultComboBoxModel(new String [] {"0", "1", "2"});
        sourceLevelComboBox.setModel(sourceLevelModel);


        DefaultComboBoxModel vulnTypeModel = new DefaultComboBoxModel(new String [] {
                VulnType.ALL_TYPE.name(),
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
        });
        vulnTypeComboBox.setModel(vulnTypeModel);

        editorPane1.setEditable(false);
        editorPane1.setContentType("text/html");
        editorPane1.setText("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "\t<title></title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p><img src=\"http://ultramangaia.cn/usr/uploads/2019/04/4013527322.png\"></p>\n" +
                "</body>\n" +
                "</html>");

        UiHelper.sourceLevelModel = sourceLevelModel;
        UiHelper.vulnTypeModel = vulnTypeModel;

    }

    public JPanel getContent() {
        return myToolWindowContent;
    }

    private void createUIComponents() {
        myToolWindowContent = new JPanel();
    }
}
