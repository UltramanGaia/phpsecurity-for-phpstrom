package cn.ultramangaia.core.slice;

import cn.ultramangaia.utils.UiHelper;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiFile;
import com.intellij.slicer.*;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author: Gaia
 * @date: 2019/4/17
 * @description:
 */
public class MySliceUtil {
    private final static Logger logger = Logger.getInstance(MySliceUtil.class);

    public static MySliceNode convertSliceNode(SliceNode sliceRootNode){
        logger.info("convert slice node ");
        logger.info(sliceRootNode.targetEqualUsages.toString());
        logger.info(sliceRootNode.getValue().toString());
        logger.info(sliceRootNode.getNodeText());
        logger.info(sliceRootNode.getValue().getElement().getText());
        MySliceNode mySliceNode = new MySliceNode(sliceRootNode.getProject(), sliceRootNode.targetEqualUsages, sliceRootNode.getValue());
        mySliceNode.mySetValue(sliceRootNode.getValue());
        Collection<SliceNode> sliceNodes = sliceRootNode.getChildren();
        ArrayList<SliceNode> sliceNodeArrayList = new ArrayList<>();
        for(SliceNode node: sliceNodes){
            MySliceNode mySliceNode1 = MySliceUtil.convertSliceNode(node);
            sliceNodeArrayList.add(mySliceNode1);
        }
        mySliceNode.setChildren(sliceNodeArrayList);

        return mySliceNode;
    }

    public static void mySliceNodeAddPath(MySliceNode myRootSliceNode, ArrayList<SliceNode> sliceNodeArrayList){
        MySliceNode tmpMySliceNode = myRootSliceNode;
        for(SliceNode sliceNode: sliceNodeArrayList){
            MySliceNode mySliceNode = new MySliceNode(sliceNode);
            if(!(mySliceNode.getValue().getElement() instanceof PsiFile)) {
                MySliceNode tt = tmpMySliceNode.findChildren(mySliceNode.getValue().getElement());
                if(tt == null){
                    tmpMySliceNode.addChildren(mySliceNode);
                    tmpMySliceNode = mySliceNode;
                }else{
                    tmpMySliceNode = tt;
                }
            }
        }
    }

    public static void createWindow(Project project, MySliceNode mySliceNode){
        /*
         * @author: Gaia
         * @description: 使用mySliceNode创建结果窗口
         * @params: [project, mySliceNode]
         * @return: void
         */
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("PHP Security");
        final SliceToolwindowSettings sliceToolwindowSettings = SliceToolwindowSettings.getInstance(project);
        final ContentManager contentManager = toolWindow.getContentManager();

        final Content[] myContent = new Content[1];
        final SlicePanel slicePanel = new SlicePanel(project, true, mySliceNode, false, toolWindow) {
            @Override
            protected void close() {
                super.close();
                contentManager.removeContent(myContent[0], true);
            }

            @Override
            public boolean isAutoScroll() {
                return sliceToolwindowSettings.isAutoScroll();
            }

            @Override
            public void setAutoScroll(boolean autoScroll) {
                sliceToolwindowSettings.setAutoScroll(autoScroll);
            }

            @Override
            public boolean isPreview() {
                return sliceToolwindowSettings.isPreview();
            }

            @Override
            public void setPreview(boolean preview) {
                sliceToolwindowSettings.setPreview(preview);
            }
        };
        slicePanel.getBuilder().getTree().setRootVisible(true);

        Content oldContent = contentManager.findContent("Results");
        if(null != oldContent){
            contentManager.removeContent(oldContent, true);
        }
        myContent[0] = contentManager.getFactory().createContent(slicePanel, "Results", true);
        contentManager.addContent(myContent[0]);
        contentManager.setSelectedContent(myContent[0]);
        toolWindow.activate(null);
    }

    public static void cleanResultTree(){
        UiHelper.mySliceRootNode = null;
    }

    public static void initResultTree(SliceNode node){
        UiHelper.mySliceRootNode = new MySliceNode(node, "Results");

    }

    public static void showResultTree(Project project){
        MySliceUtil.createWindow(project, UiHelper.mySliceRootNode);
    }


}
