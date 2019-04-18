package cn.ultramangaia.core;

import cn.ultramangaia.config.Sink;
import cn.ultramangaia.config.Sources;
import cn.ultramangaia.core.slice.MySliceUtil;
import cn.ultramangaia.utils.UiHelper;
import com.intellij.analysis.AnalysisScope;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.slicer.*;
import com.jetbrains.php.lang.psi.elements.ArrayAccessExpression;
import com.jetbrains.php.lang.psi.elements.ConcatenationExpression;
import com.jetbrains.php.lang.psi.elements.ParenthesizedExpression;
import com.jetbrains.php.lang.psi.elements.UnaryExpression;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author: Gaia
 * @date: 2019/4/16
 * @description: 数据流分析器，从sink点回溯检查参数是否来自污染源且未进行恰当净化处理
 */
public class DataFlowAnalyzer {
    private final static Logger logger = Logger.getInstance(DataFlowAnalyzer.class);

    public static ArrayList<SliceNode> sliceNodeArray = new ArrayList<>();

    public static SliceRootNode dataflowAnalyze(PsiElement element){
        /*
         * @author: Gaia
         * @description: 给定PsiElement，进行数据流分析，返回结果树的根节点
         * @params: [element]
         * @return: javax.swing.tree.DefaultMutableTreeNode
         */
        Project project = element.getProject();
        if(project == null) { return null; }
        AnalysisScope analysisScope =new AnalysisScope(project);
        SliceAnalysisParams params = new SliceAnalysisParams();
        params.scope = analysisScope;
        params.dataFlowToThis = true;
        SliceUsage sliceUsage = LanguageSlicing.getProvider(element).createRootUsage(element, params);
        SliceRootNode rootNode = new SliceRootNode(project, new DuplicateMap(), sliceUsage);

        return rootNode;
    }


    public static void analyze(Project project, PsiElement element, Sink sink){
        /*
         * @author: Gaia
         * @description: 对element进行污点分析，检测到达sink点的数据传播路径
         * @params: [project, element, sink]
         * @return: void
         */
        SliceRootNode rootNode = dataflowAnalyze(element);

        analyze(rootNode, 0, sink);
    }


    public static void analyze(SliceNode node, int depth, Sink sink){
        /*
         * @author: Gaia
         * @description: 此函数进行深度遍历搜索分析，sliceNodeArray存储当前路径
         * @params: [node, depth, sink]
         * @return: void
         */
        sliceNodeArray.add(node);

        PsiElement psiElement = node.getValue().getElement();
        // 该节点是否是污染源
        if(Sources.isSources(psiElement, Integer.parseInt((String) UiHelper.sourceLevelModel.getSelectedItem()))){
            if(null == UiHelper.mySliceRootNode){
                MySliceUtil.initResultTree(node);
            }
            MySliceUtil.mySliceNodeAddPath(UiHelper.mySliceRootNode, sliceNodeArray);
        }else{
            Collection<SliceNode> sliceNodes = node.getChildren();
            if(sliceNodes.size() != 0){
                // 还有子节点，继续分析
                for(SliceNode sliceNode: sliceNodes){
                    analyze(sliceNode, depth + 1, sink);
                }
            }else{
                // 无子节点，查看下是否需要继续数据流分析
                // 逗点连接表达式
                if(psiElement instanceof ConcatenationExpression){
                    ConcatenationExpression concatenationExpression = (ConcatenationExpression)psiElement;
                    PsiElement [] psiElements = concatenationExpression.getChildren();
                    for(PsiElement element: psiElements) {
                        logger.info("ConcatenationExpression:"+element.getText());
                        SliceRootNode rootNode = dataflowAnalyze(element);
                        analyze(rootNode, depth + 1, sink);
                    }
                }
                else if(psiElement instanceof ArrayAccessExpression){
                    ArrayAccessExpression arrayAccessExpression = (ArrayAccessExpression)psiElement;
                    PsiElement element = arrayAccessExpression.getFirstPsiChild();
                    logger.info("ArrayAccessExpression:" + element.getText());
                    SliceRootNode rootNode = dataflowAnalyze(element);
                    analyze(rootNode, depth + 1, sink);
                }
                else if(psiElement instanceof UnaryExpression){
                    UnaryExpression unaryExpression = (UnaryExpression)psiElement;
                    PsiElement element = unaryExpression.getLastChild();
                    logger.info("UnaryExpression:"+element.getText());
                    SliceRootNode rootNode = dataflowAnalyze(element);
                    analyze(rootNode, depth + 1, sink);
                }
                else if(psiElement instanceof ParenthesizedExpression){
                    ParenthesizedExpression parenthesizedExpression = (ParenthesizedExpression)psiElement;
                    PsiElement element = parenthesizedExpression.unparenthesize();
                    logger.info("ParenthesizedExpression:"+element.getText());
                    SliceRootNode rootNode = dataflowAnalyze(element);
                    analyze(rootNode, depth + 1, sink);
                }
            }
        }

        sliceNodeArray.remove(node);
    }
}
