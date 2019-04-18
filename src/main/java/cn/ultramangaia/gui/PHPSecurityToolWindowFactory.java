package cn.ultramangaia.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.wm.*;
import com.intellij.ui.content.*;

/**
 * @author: Gaia
 * @date: 2019/4/16
 * @description: 工具窗口的工厂类
 */
public class PHPSecurityToolWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // 这个方法定义窗口的内容
        PHPSecurityToolWindow phpSecurityToolWindow = new PHPSecurityToolWindow(toolWindow);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(phpSecurityToolWindow.getContent(), "Index", false);
        toolWindow.getContentManager().addContent(content);
    }
}
