package com.codgen.main;

import com.codgen.helper.DataHelper;
import com.codgen.model.JdbcConfig;
import com.codgen.model.TableConfig;
import com.codgen.model.TableModel;
import com.codgen.helper.BuilderHelper;
import com.codgen.helper.FileHelper;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class CreateCode {
    private static List<TableModel> tableModelList;
    private static VelocityContext velocityContext;
    private static FileHelper helper = new FileHelper();

    public static void main(String[] args) throws IOException {
        Properties properties = helper.getProperties("jdbc.properties");

        //初始化表结构
        tableModelList = CreateCode.initTableModel(properties);
        //初始化VelocityContext
        velocityContext = CreateCode.initVelocityContext(properties);

        String projectName = properties.get("projectName").toString();
        String languagePath = properties.get("languagePath").toString();
        String templatePath = properties.getProperty("templetFilePath");
        String createFilePath = properties.getProperty("genDirPath") + File.separator + projectName + File.separator;
        String javaPath = createFilePath + "java/" + projectName;
        String testPath = createFilePath + "test/" + projectName;
        String configPath = createFilePath + "config";

        //实体类
        CreateCode.createFiles(templatePath + "Model.vm", javaPath + "/model", ".java");

        //数据库访问层
        CreateCode.createFiles(templatePath + "Dao.vm", javaPath + "/dao", "Dao.java");
        CreateCode.createFiles(templatePath + "DaoImpl.vm", javaPath + "/dao/impl", "DaoImpl.java");
        CreateCode.createFiles(templatePath + "DaoTest.vm", testPath + "/dao", "DaoTest.java");
        CreateCode.createFile(templatePath + "Dao-config.vm", configPath + "/spring", projectName + "-dao.xml");

        //mybatis
        CreateCode.createFiles(templatePath + "mybatis.vm", configPath + "/mybatis/" + languagePath, "Mapper.xml");
        CreateCode.createFile(templatePath + "mybatis-config.vm", configPath + "/mybatis", "mybatis.config." + projectName + ".xml");
        CreateCode.createFile(templatePath + "mybatis-spring.vm", configPath + "/spring", "spring-mybatis-" + projectName + "-config.xml");

        //逻辑处理层
        CreateCode.createFiles(templatePath + "Service.vm", javaPath + "/service", "Service.java");
        CreateCode.createFiles(templatePath + "ServiceImpl.vm", javaPath + "/service/impl", "ServiceImpl.java");
        CreateCode.createFiles(templatePath + "ServiceTest.vm", testPath + "/service", "ServiceTest.java");
        CreateCode.createFile(templatePath + "Service-config.vm", configPath + "/spring", projectName + "-service.xml");

        //控制层
        CreateCode.createFiles(templatePath + "Controller.vm", javaPath + "/controller", "Controller.java");
        CreateCode.createFiles(templatePath + "ControllerTest.vm", testPath + "/controller", "ControllerTest.java");

        String webPath = createFilePath + "web/" + projectName;
        CreateCode.createFiles(templatePath + "web-manhtml.vm", webPath + "/html", "Man.html");
        //CreateCode.createFiles(templatePath + "web-manjs.vm", webPath + "/js", "Man,js");
        CreateCode.createFiles(templatePath + "web-addhtml.vm", webPath + "/html", "Add.html");
        //CreateCode.createFiles(templatePath + "web-addjs.vm", webPath + "/js", "Add.js");
        CreateCode.createFiles(templatePath + "web-modifyhtml.vm", webPath + "/html", "Modify.html");
        //CreateCode.createFiles(templatePath + "web-modifyjs.vm", webPath + "/js", "Modify.js");
        CreateCode.createFiles(templatePath + "web-detailhtml.vm", webPath + "/html", "Detail.html");
        //CreateCode.createFiles(templatePath + "web-detailjs.vm", webPath + "/js", "Detail.js");
        CreateCode.createFiles(templatePath + "web-languageEn.vm", webPath + "/language/en", "Page.js");
        CreateCode.createFiles(templatePath + "web-languageZn.vm", webPath + "/language/zh-cn", "Page.js");
    }

    /**
     * 初始化表结构
     *
     * @param properties
     * @return
     */
    private static List<TableModel> initTableModel(Properties properties) {
        List<TableModel> tableList = new ArrayList<>();
        JdbcConfig jdbcConfig = new JdbcConfig(properties);
        TableConfig tableConfig = new TableConfig(properties);
        DataHelper DataHelper = new DataHelper(jdbcConfig);
        tableList = DataHelper.initTableModelList(tableConfig);
        return tableList;
    }

    /**
     * 初始化VelocityContext
     *
     * @return
     */
    private static VelocityContext initVelocityContext(Properties properties) {
        VelocityContext context = new VelocityContext();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateStr = formatter.format(new Date());
        //配置
        context.put("builder", new BuilderHelper());
        context.put("author", properties.get("author"));
        context.put("time", dateStr);
        //引用
        context.put("basePath", properties.get("basePath"));
        context.put("pageBean", properties.get("pageBean"));
        context.put("resultBean", properties.get("resultBean"));
        context.put("keyBean", properties.get("keyBean"));
        context.put("ruleBean", properties.get("ruleBean"));
        context.put("jsonBean", properties.get("jsonBean"));
        //路径
        context.put("databaseType", properties.get("databaseType"));
        context.put("languagePath", properties.get("languagePath"));
        context.put("packageName", properties.get("packageName"));
        context.put("projectName", properties.get("projectName"));
        //表集合
        context.put("tableModelList", tableModelList);
        return context;
    }

    /**
     * 生成多个文件,每个表对应一个文件
     *
     * @param templateName 模板文件名
     * @param dirName      目录
     * @param suffix       文件名后缀
     */
    public static void createFiles(String templateName, String dirName, String suffix) throws IOException {
        FileHelper helper = new FileHelper();

        // 初始化一个模板引擎
        Velocity.init();
        // 实例化一个VelocityEngine对象
        Template template = null;
        for (TableModel tableModel : tableModelList) {
            StringWriter sw = new StringWriter();
            velocityContext.put("tableModel", tableModel);
            // 将需要的数据加载到模板引擎的上下文中
            // 从vm目录下加载hello.vm模板,在eclipse工程中该vm目录与src目录平级
            template = Velocity.getTemplate(templateName, "UTF-8");
            template.merge(velocityContext, sw);
            String fileName = tableModel.getTableLabel();
            fileName = BuilderHelper.firstToUpper(fileName);
            helper.writeToFile(dirName + File.separator, fileName + suffix, sw.toString());
        }
    }

    /**
     * 生成文件,对应一个文件
     *
     * @param templateName
     * @param dirName      文件路径
     * @param fileName
     */
    public static void createFile(String templateName, String dirName, String fileName) throws IOException {
        Template template = Velocity.getTemplate(templateName, "UTF-8");
        StringWriter sw = new StringWriter();
        template.merge(velocityContext, sw);
        helper.writeToFile(dirName + File.separator, fileName, sw.toString());
    }

}