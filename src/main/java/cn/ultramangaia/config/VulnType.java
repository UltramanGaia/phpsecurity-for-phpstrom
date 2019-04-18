package cn.ultramangaia.config;
/**
 * @author: Gaia
 * @date: 2019/4/16
 * @description: 漏洞类型枚举类型
 */
public enum VulnType {
    // 命令执行
    COMMAND_EXEC,
    // 代码执行
    CODE_EXEC,
    // SQL注入
    SQL_INJECTION,
    // 文件包含
    FILE_INCLUTION,
    // XSS
    XSS,
    // 文件泄露
    FILE_DISCLOSURE,
    // 文件操作
    FILE_MANIPULATION,
    // XPath注入
    XPATH_INJECTION,
    // LDAP注入
    LDAP_INJECTION,
    // 所有类型
    ALL_TYPE,
}
