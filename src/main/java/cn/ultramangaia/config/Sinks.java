package cn.ultramangaia.config;

/**
 * @author: Gaia
 * @date: 2019/4/16
 * @description: Sinks类，Sink的工具类
 */
public class Sinks {
    public final static Sink [][] SENSITIVE_SINKS = {
            // COMMAND_EXEC
            {
                    new Sink(VulnType.COMMAND_EXEC,"system", new int[]{0},
                            SecureFunction.VulnTypeSecureFunctions[VulnType.COMMAND_EXEC.ordinal()]),
            },
            // CODE_EXEC
            {

            },
            // SQL_INJECTION
            {

            },
            // FILE_INCLUTION
            {

            },
            // XSS
            {
                    new Sink(VulnType.XSS,"echo", new int[]{-1},
                            SecureFunction.VulnTypeSecureFunctions[VulnType.XSS.ordinal()]),
                    new Sink(VulnType.XSS,"print", new int[]{-1},
                            SecureFunction.VulnTypeSecureFunctions[VulnType.XSS.ordinal()]),
            },
            // FILE_DISCLOSURE
            {

            },
            // FILE_MANIPULATION
            {

            },
            // XPATH_INJECTION
            {

            },
            // LDAP_INJECTION
            {

            }
    } ;

    public static Sink getRelatedSink(String functionName, int paramIndex, VulnType vulnType){
        if(vulnType == VulnType.ALL_TYPE){
            for(VulnType vulnType1: VulnType.values()){
                if(vulnType1 != VulnType.ALL_TYPE) {
                    Sink sink = getRelatedSink(functionName, paramIndex, vulnType1);
                    if(sink != null){
                        return sink;
                    }
                }
            }
        }else {
            for (Sink sink : SENSITIVE_SINKS[vulnType.ordinal()]) {
                if (sink.functionName.equals(functionName)) {
                    // all params
                    if (-1 == sink.importentParams[0]) {
                        return sink;
                    }

                    for (int i : sink.importentParams) {
                        if (i == paramIndex) {
                            return sink;
                        }
                    }

                    return null;
                }
            }
        }
        return null;
    }
}
