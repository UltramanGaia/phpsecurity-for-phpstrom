package cn.ultramangaia.core.slice;

import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.slicer.*;
import com.intellij.usageView.UsageViewBundle;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author: Gaia
 * @date: 2019/4/16
 * @description: 实现自己的SliceNode
 */
public class MySliceNode extends SliceRootNode {
    private final static Logger logger = Logger.getInstance(MySliceNode.class);
    private String nodeText = null;
    public MySliceNode(@NotNull Project project, @NotNull DuplicateMap targetEqualUsages, @NotNull SliceUsage rootUsage) {
        super(project, targetEqualUsages, rootUsage);
    }

    public MySliceNode(SliceNode sliceNode){
        this(sliceNode.getProject(), sliceNode.targetEqualUsages, sliceNode.getValue());
        mySetValue(sliceNode.getValue());
    }

    public MySliceNode(SliceNode sliceNode, String nodeText){
        this(sliceNode);
        this.nodeText = nodeText;

    }

    public void mySetValue(Object o) {
        Class<AbstractTreeNode> clazz = AbstractTreeNode.class;
        try {
            Field field = clazz.getDeclaredField("myValue");
            field.setAccessible(true);
            field.set(this, o);
        }catch (Exception e){
            logger.info(e);
        }
    }


    public void addChildren(MySliceNode child){
        ArrayList<SliceNode> tmp = myCachedChildren==null?new ArrayList<>():new ArrayList<>(myCachedChildren);
        tmp.add(child);
        myCachedChildren = tmp;
    }

    public MySliceNode findChildren(PsiElement element){
        if(myCachedChildren == null) {
            return null;
        }
        for(SliceNode node: myCachedChildren){
            if(node.getValue().getElement() == element && node instanceof MySliceNode) {
                return (MySliceNode) node;
            }
        }

        return null;
    }

    @Override
    public void customizeCellRenderer(@NotNull SliceUsageCellRendererBase renderer, @NotNull JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        renderer.setIcon(getPresentation().getIcon(expanded));
        if(nodeText != null) {
            renderer.append(nodeText);
        }else if (isValid()) {
            SliceUsage sliceUsage = getValue();
            renderer.customizeCellRendererFor(sliceUsage);
            renderer.setToolTipText(sliceUsage.getPresentation().getTooltipText());
        }
        else {
            renderer.append(UsageViewBundle.message("node.invalid") + " ");
        }
    }
}
