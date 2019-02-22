package com.codgen.main;

import com.codgen.model.JdbcConfig;
import com.codgen.model.TableConfig;
import com.codgen.model.TableModel;
import com.codgen.service.DbProvider;
import com.codgen.service.impl.MysqlProvider;
import com.codgen.service.impl.OracleProvider;
import com.codgen.util.BuilderHelper;
import com.codgen.util.FileHelper;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class CreateCode {

    public static void main(String[] args) throws IOException {
        List<TableModel> tableModelList = CreateCode.initTableModel();

        FileHelper helper = new FileHelper();
        Properties properties = helper.getProperties("jdbc.properties");
        String projectName = properties.get("projectName").toString();
        String languagePath = properties.get("languagePath").toString();

        String javaPath = "java/" + projectName;
        String testPath = "test/" + projectName;
        String configPath = "config/spring";

        //实体类
        CreateCode.createFiles(tableModelList, "Model.vm", javaPath + "/model", ".java");

        //数据库访问层
        CreateCode.createFiles(tableModelList, "Dao.vm", javaPath + "/dao", "Dao.java");
        CreateCode.createFiles(tableModelList, "DaoImpl.vm", javaPath + "/dao/impl", "DaoImpl.java");
        CreateCode.createFile(tableModelList, "Dao-spring.vm", configPath, projectName + "-dao.xml");
        CreateCode.createFiles(tableModelList, "DaoTest.vm", testPath + "/dao", "DaoTest.java");

        CreateCode.createFiles(tableModelList, "mybatis.vm", "config/mybatis/" + languagePath, "Mapper.xml");
        CreateCode.createFile(tableModelList, "mybatis-config.vm", "config/mybatis", "mybatis-" + projectName + "-config.xml");
        CreateCode.createFile(tableModelList, "mybatis-spring.vm", "config/spring", "spring-mybatis-" + projectName + "-config.xml");

        //逻辑处理层
        CreateCode.createFiles(tableModelList, "Service.vm", javaPath + "/service", "Service.java");
        CreateCode.createFiles(tableModelList, "ServiceImpl.vm", javaPath + "/service/impl", "ServiceImpl.java");
        CreateCode.createFile(tableModelList, "Service-spring.vm", configPath, projectName + "-service.xml");
        CreateCode.createFiles(tableModelList, "ServiceTest.vm", testPath + "/service", "ServiceTest.java");

        //控制层
        CreateCode.createFiles(tableModelList, "Controller.vm", javaPath + "/controller", "Controller.java");
        CreateCode.createFiles(tableModelList, "ControllerTest.vm", testPath + "/controller", "ControllerTest.java");


        String webPath = "webapp/" + projectName;
//        CreateCode.createFiles(tableModelList, "web-manhtml.vm", webPath+"/jsp", "Man.jsp");
//        CreateCode.createFiles(tableModelList, "web-manjs.vm", webPath+"/js", "Man,js");
//        CreateCode.createFiles(tableModelList, "web-web-addhtml.vm", webPath+"/jsp", "Add.jsp");
//        CreateCode.createFiles(tableModelList, "web-web-addjs.vm", webPath+"/js", "Add.js");
//        CreateCode.createFiles(tableModelList, "web-modifyhtml.vm", webPath+"/jsp", "Modify.jsp");
//        CreateCode.createFiles(tableModelList, "web-modifyjs.vm", webPath+"/js", "Modify.js");
//        CreateCode.createFiles(tableModelList, "web-detailhtml.vm", webPath+"/jsp", "Detail.jsp");
//        CreateCode.createFiles(tableModelList, "web-detailjs.vm", webPath+"/js", "Detail.js");

    }

    /**
     * 初始化表模型
     *
     * @return
     */
    public static List<TableModel> initTableModel() {
        List<TableModel> tableModelList = new ArrayList<TableModel>();
        FileHelper helper = new FileHelper();
        Properties properties = helper.getProperties("jdbc.properties");

        //数据库连接
        DbProvider provider;
        JdbcConfig jdbcConfig = new JdbcConfig();
        jdbcConfig.setDatabaseType(properties.getProperty("databaseType"));
        jdbcConfig.setDriver(properties.getProperty("driverClass"));
        jdbcConfig.setUrl(properties.getProperty("jdbcUrl"));
        jdbcConfig.setUser(properties.getProperty("user"));
        jdbcConfig.setPassword(properties.getProperty("password"));
        jdbcConfig.setSchema(properties.getProperty("schema"));
        if ("mysql".equalsIgnoreCase(jdbcConfig.getDatabaseType())) {
            provider = new MysqlProvider(jdbcConfig);
        } else if ("oracle".equalsIgnoreCase(jdbcConfig.getDatabaseType())) {
            provider = new OracleProvider(jdbcConfig);
        } else {
            return null;
        }

        TableConfig tableConfig = new TableConfig();
        tableConfig.setPrefix(properties.getProperty("prefix"));
        tableConfig.setValidFlags(properties.getProperty("validFlags"));
        tableConfig.setExcludeFields(properties.getProperty("excludeFields").split(","));

        List<String> tableNameList = provider.getTableNames();
        for (String tableName : tableNameList) {
            System.out.println(tableName);
            if (tableName.startsWith(tableConfig.getPrefix())) {
                TableModel tableModel = provider.createTableModel(tableName, tableConfig);
                tableModelList.add(tableModel);
            }
        }
        return tableModelList;
    }

    /**
     * 生成多个文件,每个表对应一个文件
     *
     * @param templateName 模板文件名
     * @param dirName      目录
     * @param suffix       文件名后缀
     */
    public static void createFiles(List<TableModel> tableModelList, String templateName, String dirName, String suffix) throws IOException {
        FileHelper helper = new FileHelper();
        Properties properties = helper.getProperties("jdbc.properties");

        String genDirPath = properties.getProperty("genDirPath") + File.separator;
        String projectName = properties.getProperty("projectName") + File.separator;
        String genDir = genDirPath + projectName + dirName + File.separator;
        String templatePath = properties.getProperty("templetFilePath");
        // 初始化一个模板引擎
        Velocity.init();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(new Date());
        VelocityContext context = new VelocityContext();
        context.put("builderHelper", new BuilderHelper());
        context.put("tablesInfo", tableModelList);
        context.put("time", dateString);
        context.put("languagePath", properties.get("languagePath"));
        context.put("packageName", properties.get("packageName"));
        context.put("author", properties.get("author"));
        context.put("projectName", properties.get("projectName"));
        // 实例化一个VelocityEngine对象
        Template template = null;
        for (TableModel tablesinfo : tableModelList) {
            StringWriter sw = new StringWriter();
            context.put("tableInfo", tablesinfo);
            context.put("packageName", properties.get("packageName"));
            // 将需要的数据加载到模板引擎的上下文中
            // 从vm目录下加载hello.vm模板,在eclipse工程中该vm目录与src目录平级
            template = Velocity.getTemplate(templatePath + templateName, "UTF-8");
            template.merge(context, sw);
            String fileName = tablesinfo.getTableLabel();
            fileName = BuilderHelper.firstToUpper(fileName);
            helper.writeToFile(genDir, fileName + suffix, sw.toString());
        }
    }

    /**
     * 生成文件,对应一个文件
     *
     * @param templateName
     * @param dirName      文件路径
     * @param fileName
     */
    public static void createFile(List<TableModel> tableModelList, String templateName, String dirName, String fileName) throws IOException {
        FileHelper helper = new FileHelper();
        Properties properties = helper.getProperties("jdbc.properties");
        Date date = new Date();
        String genDirPath = properties.getProperty("genDirPath") + File.separator;
        String projectName = properties.getProperty("projectName") + File.separator;
        String genDir = genDirPath + projectName + dirName + File.separator;

        String templatePath = properties.getProperty("templetFilePath");

        VelocityContext context = new VelocityContext();
        context.put("time", date.toLocaleString());
        context.put("packageName", properties.get("packageName"));
        Template template = Velocity.getTemplate(templatePath + templateName, "UTF-8");
        StringWriter sw = new StringWriter();
        Map map = new HashMap();
        map.put("databasename", properties.getProperty("database"));
        context.put("builderHelper", new BuilderHelper());
        context.put("tablesInfo", tableModelList);
        context.put("databaseType", properties.get("databaseType"));
        context.put("languagePath", properties.get("languagePath"));
        context.put("packageName", properties.get("packageName"));
        context.put("author", properties.get("author"));
        context.put("projectName", properties.get("projectName"));
        template.merge(context, sw);
        helper.writeToFile(genDir + File.separator, fileName, sw.toString());
    }

}