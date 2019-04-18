package cn.ultramangaia.config;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.lang.psi.elements.*;

import java.util.Arrays;

/**
 * @author: Gaia
 * @date: 2019/4/16
 * @description: Sources类
 */
public class Sources {
    private final static Logger logger = Logger.getInstance(Sources.class);

    public final static String [][] TAINTED_SOURCES_LIST = {
            // 误判等级
            // level 0
            {
                    // 普通输入
                    "_GET",
                    "_POST",
                    "_REQUEST",
                    "_COOKIE",
                    "HTTP_GET_VARS",
                    "HTTP_POST_VARS",
                    "HTTP_REQUEST_VARS",
                    "HTTP_COOKIE_VARS",

                    // 文件输入

                    // 数据库输入

                    // 其他输入
                    "getallheaders",
                    "get_browser",
                    "get_headers",
                    "gethostbyaddr",

            },

            // level 1
            {
                    // 普通输入
                    "_FILES",
                    "HTTP_POST_FILES",
                    "HTTP_RAW_POST_DATA",
                    "_SERVER",
                    "HTTP_SERVER_VARS",

                    // 文件输入
                    "fgets",
                    "fgetss",
                    "file",
                    "file_get_contents",
                    "fread",

                    // 数据库输入
                    "mysql_fetch_array",
                    "mysql_fetch_assoc",
                    "mysql_fetch_object",
                    "mysql_fetch_row",
                    "mysql_fetch_field",
                    "pg_fetch_all",
                    "pg_fetch_array",
                    "pg_fetch_assoc",
                    "pg_fetch_object",
                    "pg_fetch_result",
                    "pg_fetch_row",
                    "sqlite_fetch_all",
                    "sqlite_fetch_array",
                    "sqlite_fetch_object",
                    "sqlite_fetch_single",
                    "sqlite_fetch_string",
                    // 其他输入

            },

            // level 2
            {
                    // 普通输入
                    "_ENV",
                    "HTTP_ENV_VARS",
                    "argv",

                    // 文件输入
                    "bzread",
                    "dio_read",
                    "gzread",
                    "file_get_contents",
                    "zip_read",
                    "get_meta_tags",
                    "glob",
                    "readdir",
                    "scandir",

                    // 数据库输入

                    // 其他输入
                    "getenv",



            }
    };

    private static boolean isSources(String name, int level){
        if(null == name){
            return false;
        }
        for(int i = 0; i <= level; i++){
            if(Arrays.asList(TAINTED_SOURCES_LIST[i]).contains(name)){
                return true;
            }
        }
        return false;
    }

    public static boolean isSources(PsiElement element, int level){
//        // 数组：判断当前数组表达式，然后判断数组变量PisElement
//        // 虽然ArrayAccessExpression是PhpExpression，但是为了便于判断$_SERVER['xxx']
//        // $_GET['cmd']   getallheaders()[0][1]
        if(element instanceof ArrayAccessExpression ){
            ArrayAccessExpression arrayAccessExpression = (ArrayAccessExpression) element;
            if(isSources(arrayAccessExpression.getText(), level)){
                return true;
            }
            return isSources(arrayAccessExpression.getValue(), level);
        }

        // 变量：判断是否污染
        else if(element instanceof Variable){
            Variable variable = (Variable)element;
            return isSources(variable.getName(), level);
        }
        // 函数调用：判断函数名
        // system(file_get_contains('a'))
        else if(element instanceof FunctionReference){
            FunctionReference functionReference = (FunctionReference)element;
            return isSources(functionReference.getName(), level);
        }

        else if(element instanceof PhpExpression){
            PhpExpression phpExpression = (PhpExpression)element;
            for(PsiElement childElement: phpExpression.getChildren()){
                if(isSources(childElement, level)){
                    return true;
                }
            }
        }
        else{
            logger.info("Unhandle element: "+element.getClass());
            return false;
        }

        return false;
    }
}
