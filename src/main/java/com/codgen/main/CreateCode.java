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
    private static String templatePath;
    private static String projectName;

    public static void main(String[] args) throws IOException {
        Properties properties = helper.getProperties("jdbc.properties");

        //初始化表结构
        tableModelList = CreateCode.initTableModel(properties);
        //初始化VelocityContext
        velocityContext = CreateCode.initVelocityContext(properties);

        //模板路径
        templatePath = properties.getProperty("templetFilePath");
        //项目名
        projectName = properties.get("projectName").toString();
        //文件生成路径
        String createFilePath = properties.getProperty("genDirPath");
        String javaPath = createFilePath + File.separator + projectName + "/java/" + projectName;
        String testPath = createFilePath + File.separator + projectName + "/test/" + projectName;
        String configPath = createFilePath + File.separator + projectName + "/config";

        //实体类
        createModel(javaPath);
        //控制层
        createController(javaPath);
        //逻辑处理层
        createService(javaPath);
        //数据库访问层
        createDao(javaPath);
        //mybatis
        createMybatis(configPath);
        //SpringXml
        createSpringXml(configPath);
        //Test
        createTest(testPath);

        String webPath = createFilePath + "web/" + projectName;
        //HTML
        createHtml(webPath);
        //Language
        createLanguage(webPath);
        //JS
        //createJs(webPath);
    }

    private static void createModel(String javaPath) throws IOException {
        CreateCode.createFiles(templatePath + "Model.vm", javaPath + "/model", ".java");
    }

    private static void createController(String javaPath) throws IOException {
        CreateCode.createFiles(templatePath + "Controller.vm", javaPath + "/controller", "Controller.java");
    }

    private static void createService(String javaPath) throws IOException {
        CreateCode.createFiles(templatePath + "Service.vm", javaPath + "/service", "Service.java");
        CreateCode.createFiles(templatePath + "ServiceImpl.vm", javaPath + "/service/impl", "ServiceImpl.java");
    }

    private static void createDao(String javaPath) throws IOException {
        CreateCode.createFiles(templatePath + "Dao.vm", javaPath + "/dao", "Dao.java");
        CreateCode.createFiles(templatePath + "DaoImpl.vm", javaPath + "/dao/impl", "DaoImpl.java");
    }

    private static void createSpringXml(String configPath) throws IOException {
        CreateCode.createFile(templatePath + "Dao-config.vm", configPath + "/spring", projectName + "-dao.xml");
        CreateCode.createFile(templatePath + "Service-config.vm", configPath + "/spring", projectName + "-service.xml");
    }

    private static void createTest(String testPath) throws IOException {
        CreateCode.createFiles(templatePath + "DaoTest.vm", testPath + "/dao", "DaoTest.java");
        CreateCode.createFiles(templatePath + "ServiceTest.vm", testPath + "/service", "ServiceTest.java");
        CreateCode.createFiles(templatePath + "ControllerTest.vm", testPath + "/controller", "ControllerTest.java");
    }

    private static void createMybatis(String configPath) throws IOException {
        CreateCode.createFiles(templatePath + "mybatis.vm", configPath + "/mybatis/" + projectName, "Mapper.xml");
        CreateCode.createFile(templatePath + "mybatis-config.vm", configPath + "/mybatis", "mybatis.config." + projectName + ".xml");
        CreateCode.createFile(templatePath + "mybatis-spring.vm", configPath + "/spring", "spring-mybatis-" + projectName + "-config.xml");
    }

    private static void createHtml(String webPath) throws IOException {
        String path = webPath + "/html";
        CreateCode.createFiles(templatePath + "web-manhtml.vm", path, "Man.html");
        CreateCode.createFiles(templatePath + "web-addhtml.vm", path, "Add.html");
        CreateCode.createFiles(templatePath + "web-modifyhtml.vm", path, "Modify.html");
        CreateCode.createFiles(templatePath + "web-detailhtml.vm", path, "Detail.html");
    }

    private static void createJs(String webPath) throws IOException {
        String path = webPath + "/js";
        CreateCode.createFiles(templatePath + "web-manjs.vm", path, "Man,js");
        CreateCode.createFiles(templatePath + "web-addjs.vm", path, "Add.js");
        CreateCode.createFiles(templatePath + "web-modifyjs.vm", path, "Modify.js");
        CreateCode.createFiles(templatePath + "web-detailjs.vm", path, "Detail.js");
    }

    private static void createLanguage(String webPath) throws IOException {
        String path = webPath + "/language";
        CreateCode.createFiles(templatePath + "web-languageEn.vm", path, "Page.js");
        CreateCode.createFiles(templatePath + "web-languageZn.vm", path, "Page.js");
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
    private static void createFiles(String templateName, String dirName, String suffix) throws IOException {
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
    private static void createFile(String templateName, String dirName, String fileName) throws IOException {
        Template template = Velocity.getTemplate(templateName, "UTF-8");
        StringWriter sw = new StringWriter();
        template.merge(velocityContext, sw);
        helper.writeToFile(dirName + File.separator, fileName, sw.toString());
    }

}