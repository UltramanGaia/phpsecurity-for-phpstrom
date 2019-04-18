package cn.ultramangaia.actions;

import cn.ultramangaia.core.PsiElementAnalyzer;
import cn.ultramangaia.core.slice.MySliceUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectFileIndex;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.jetbrains.php.lang.PhpFileType;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.diagnostic.Logger;

/**
 * @author: Gaia
 * @date: 2019/4/16
 * @description: 对当前项目所有文件进行数据流分析
 */
@SuppressWarnings("ALL")
public class ScanProjectAction extends AnAction {

    private final static Logger logger = Logger.getInstance(ScanProjectAction.class);
    @Override
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getProject();
        if(project == null){
            return;
        }
        logger.info("Scan Project Start");
        // 清除 结果树 的节点
        MySliceUtil.cleanResultTree();

        // 获取了所有项目中PHP文件
        ProjectFileIndex.SERVICE.getInstance(project).iterateContent(new        ContentIterator() {

            @Override
            public boolean processFile(VirtualFile fileInProject) {
                if(fileInProject.getFileType() == PhpFileType.INSTANCE) {
                    logger.info(fileInProject.getPath());
                    PsiFile psiFile = PsiManager.getInstance(project).findFile(fileInProject);
                    if (psiFile == null) {
                        logger.info("IT'S NULL!");
                    }
                    else
                    {
                        // 遍历PsiElement，进行分析
                        psiFile.accept(new PsiElementAnalyzer());
                    }
                }
                return true;
            }
        });

        MySliceUtil.showResultTree(project);

    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        if(e == null) {
            return;
        }
        Project project = e.getProject();
        // 如果 project 为空，则设置不可点击
        if(project == null){
            e.getPresentation().setEnabled(false);
            return;
        }
    }


}
