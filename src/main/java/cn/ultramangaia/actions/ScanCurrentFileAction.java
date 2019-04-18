package cn.ultramangaia.actions;

import cn.ultramangaia.core.PsiElementAnalyzer;
import cn.ultramangaia.core.slice.MySliceUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

/**
 * @author: Gaia
 * @date: 2019/4/16
 * @description: 对当前文件进行数据流分析
 */
public class ScanCurrentFileAction extends AnAction {
    private final static Logger logger = Logger.getInstance(ScanCurrentFileAction.class);
    @Override
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getProject();
        if(project == null) {
            return;
        }
        logger.info("Scan Current File Start");
        // 清除 结果树 的节点
        MySliceUtil.cleanResultTree();

        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);

        // 遍历PsiElement，进行分析
        psiFile.accept(new PsiElementAnalyzer());

        MySliceUtil.showResultTree(project);
    }
}
