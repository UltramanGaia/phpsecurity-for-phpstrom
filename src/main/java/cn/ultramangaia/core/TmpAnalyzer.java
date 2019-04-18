package cn.ultramangaia.core;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;

public class TmpAnalyzer extends PsiRecursiveElementWalkingVisitor {
    private final static Logger logger = Logger.getInstance(TmpAnalyzer.class);
    @Override
    public void visitElement(PsiElement element) {
        super.visitElement(element);
        logger.info(element.getClass().toString());
        logger.info(element.getText());

    }
}
