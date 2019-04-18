package cn.ultramangaia.core;

import cn.ultramangaia.config.SecureFunction;
import cn.ultramangaia.config.Sink;
import cn.ultramangaia.config.Sinks;
import cn.ultramangaia.config.VulnType;
import cn.ultramangaia.utils.UiHelper;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import com.jetbrains.php.lang.psi.elements.*;
/**
 * @author: Gaia
 * @date: 2019/4/16
 * @description: PsiElement分析器，负责处理Psi文件的PsiElement节点
 */
public class PsiElementAnalyzer extends PsiRecursiveElementWalkingVisitor {
    private final static Logger logger = Logger.getInstance(PsiElementAnalyzer.class);

    @Override
    public void visitElement(PsiElement element) {
        // 这里遍历PHP文件的所有PsiElements，判断是否sink
        super.visitElement(element);
        if(element instanceof FunctionReference){
            FunctionReference functionReference = ((FunctionReference)element);
            String funcName = functionReference.getName();
            logger.info("Function Name = " + funcName);
            PsiElement [] params = functionReference.getParameters();
            for(int i = 0; i < params.length; i++){
                Sink sink = Sinks.getRelatedSink(funcName, i, VulnType.valueOf((String) UiHelper.vulnTypeModel.getSelectedItem()));
                if(null != sink){
                    // 如果是sink，则进行数据流分析，判断是否源自污染源
                    PsiElement param = params[i];

                    Project project = param.getProject();
                    DataFlowAnalyzer.analyze(project, param, sink);

                }
            }
        }
        else if(element instanceof PhpPrintExpression){
            // TODO 待完善的echo类语句处理
            VulnType vulnType = VulnType.valueOf((String) UiHelper.vulnTypeModel.getSelectedItem());
            if(vulnType == VulnType.XSS || vulnType == VulnType.ALL_TYPE){
                Sink sink = new Sink(VulnType.XSS,"print", new int[]{-1},
                        SecureFunction.VulnTypeSecureFunctions[VulnType.XSS.ordinal()]);
                PhpPrintExpression phpPrintExpression = (PhpPrintExpression)element;
                phpPrintExpression.accept(new TmpAnalyzer());
            }
        }
        else if(element instanceof PhpShellCommandExpression){
            // TODO 对于命令执行表达式有稍微不同的处理方式
        }
    }
}
