package cn.ultramangaia.config;

/**
 * @author: Gaia
 * @date: 2019/4/16
 * @description: Sink类，存储Sink结构信息
 */
public class Sink {
    public VulnType category;
    public String functionName;
    public int [] importentParams;
    public String [] secureFunctions;
    public Sink(VulnType category, String functionName, int [] importentParams, String [] secureFunctions) {
        this.category = category;
        this.functionName = functionName;
        this.importentParams = importentParams;
        this.secureFunctions = secureFunctions;
    }
}
