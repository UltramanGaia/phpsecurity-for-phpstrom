package cn.ultramangaia.config;

public class SecureFunction {
    public final static String [] AllVulnTypeSecureFunctions = {
            "intval",
            "floatval",
            "doubleval",
            "filter_input",
            "ord",
            "sizeof",
            "round",
            "floor",
            "strlen",
            "hexdec",
            "strrpos",
            "strpos",
            "md5",
            "sha1",
            "crypt",
            "crc32",
            "hash",
            "count",
            "bin2hex",
            "levenshtein",
            "abs",
            "bindec",
            "decbin",
            "hexdec",
            "rand",
            "max",
            "min",
    };
    public final static String [][] VulnTypeSecureFunctions = {
            // COMMAND_EXEC
            {
                    "escapeshellarg",
                    "escapeshellcmd",
            },
            // CODE_EXEC
            {

            },
            // SQL_INJECTION
            {
                    "addslashes",
                    "dbx_escape_string",
                    "db2_escape_string",
                    "ingres_escape_string",
                    "maxdb_escape_string`",
                    "maxdb_real_escape_string",
                    "mysql_escape_string",
                    "mysql_real_escape_string",
                    "mysqli_escape_string",
                    "mysqli_real_escape_string",
                    "pg_escape_string",
                    "pg_escape_bytea",
                    "sqlite_escape_string",
                    "sqlite_udf_encode_binary",

            },
            // FILE_INCLUTION
            {
                    "basename",
                    "pathinfo",
            },
            // XSS
            {
                    "htmlentities",
                    "htmlspecialchars",
            },
            // FILE_DISCLOSURE
            {
                    "basename",
                    "pathinfo",
            },
            // FILE_MANIPULATION
            {
                    "basename",
                    "pathinfo",
            },
            // XPATH_INJECTION
            {
                    "addslashes",
            },
            // LDAP_INJECTION
            {

            }
    };



}
