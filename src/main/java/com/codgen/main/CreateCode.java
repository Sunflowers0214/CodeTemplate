package com.codgen.main;

import com.codgen.helper.DataHelper;
import com.codgen.model.Convert;
import com.codgen.model.JdbcConfig;
import com.codgen.model.TableModel;
import com.codgen.helper.BuilderHelper;
import com.codgen.helper.FileHelper;
import com.codgen.model.VelocityConfig;
import org.apache.commons.lang.StringUtils;
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

        //模板路径
        String templatePath = properties.getProperty("templetFilePath");
        //项目名
        String projectName = properties.get("projectName").toString();
        //文件生成路径
        String createFilePath = properties.getProperty("genDirPath");
        String javaPath = createFilePath + File.separator + projectName + "/java/" + projectName;
        String testPath = createFilePath + File.separator + projectName + "/test/" + projectName;
        String configPath = createFilePath + File.separator + projectName + "/config";
        String webPath = createFilePath + File.separator + projectName + "/web/" + projectName;
        List<VelocityConfig> velocityList = new ArrayList<>();
        //实体类
        velocityList.add(new VelocityConfig(templatePath + "Model.vm", javaPath + "/model", ".java"));
        //控制层
        velocityList.add(new VelocityConfig(templatePath + "Controller.vm", javaPath + "/controller", "Controller.java"));
        velocityList.add(new VelocityConfig(templatePath + "ControllerTest.vm", testPath + "/controller", "ControllerTest.java"));
        //逻辑处理层
        velocityList.add(new VelocityConfig(templatePath + "Service.vm", javaPath + "/service", "Service.java"));
        velocityList.add(new VelocityConfig(templatePath + "ServiceImpl.vm", javaPath + "/service/impl", "ServiceImpl.java"));
        velocityList.add(new VelocityConfig(templatePath + "Service-config.vm", configPath + "/spring/", projectName + "-service.xml", true));
        velocityList.add(new VelocityConfig(templatePath + "ServiceTest.vm", testPath + "/service", "ServiceTest.java"));
        //数据库访问层
        velocityList.add(new VelocityConfig(templatePath + "Dao.vm", javaPath + "/dao", "Dao.java"));
        velocityList.add(new VelocityConfig(templatePath + "DaoImpl.vm", javaPath + "/dao/impl", "DaoImpl.java"));
        velocityList.add(new VelocityConfig(templatePath + "Dao-config.vm", configPath + "/spring/", projectName + "-dao.xml", true));
        velocityList.add(new VelocityConfig(templatePath + "DaoTest.vm", testPath + "/dao", "DaoTest.java"));
        //mybatis
        velocityList.add(new VelocityConfig(templatePath + "mybatis.vm", configPath + "/mybatis/" + projectName, "Mapper.java"));
        velocityList.add(new VelocityConfig(templatePath + "mybatis-config.vm", configPath + "/mybatis", "mybatis.config." + projectName + ".xml", true));
        velocityList.add(new VelocityConfig(templatePath + "mybatis-spring.vm", configPath + "/spring", "spring-mybatis-" + projectName + "-config.xml", true));

        //HTML
        velocityList.add(new VelocityConfig(templatePath + "web-html-man.vm", webPath + "/html", "Man.html"));
        velocityList.add(new VelocityConfig(templatePath + "web-html-add.vm", webPath + "/html", "Add.html"));
        velocityList.add(new VelocityConfig(templatePath + "web-html-modify.vm", webPath + "/html", "Modify.html"));
        velocityList.add(new VelocityConfig(templatePath + "web-html-detail.vm", webPath + "/html", "Detail.html"));
        //JS
        velocityList.add(new VelocityConfig(templatePath + "web-js-man.vm", webPath + "/js", "Man.js"));
        velocityList.add(new VelocityConfig(templatePath + "web-js-add.vm", webPath + "/js", "Add.js"));
        velocityList.add(new VelocityConfig(templatePath + "web-js-modify.vm", webPath + "/js", "Modify.js"));
        velocityList.add(new VelocityConfig(templatePath + "web-js-detail.vm", webPath + "/js", "Detail.js"));
        //Language
        velocityList.add(new VelocityConfig(templatePath + "web-language-en.vm", webPath + "/language", "Page.js"));
        velocityList.add(new VelocityConfig(templatePath + "web-language-zn.vm", webPath + "/language", "Page.js"));

        createFile(velocityList);
    }

    /**
     * 初始化表结构
     *
     * @param properties
     * @return
     */
    private static List<TableModel> initTableModel(Properties properties) {
        JdbcConfig jdbcConfig = new JdbcConfig(properties);
        DataHelper DataHelper = new DataHelper(jdbcConfig);
        List<TableModel> tableList = DataHelper.initTableModelList();
        for (TableModel tableModel : tableList) {
            handleTableConvert(tableModel);
        }
        return tableList;
    }

    /**
     * 定义生成文件名称
     *
     * @param table
     */
    public static void handleTableConvert(TableModel table) {
        Convert convert = new Convert();
        convert.setModel(BuilderHelper.firstToUpper(table.getTableLabel()) + ".java");
        convert.setController(BuilderHelper.firstToUpper(table.getTableLabel()) + "Controller.java");
        convert.setControllerTest(BuilderHelper.firstToUpper(table.getTableLabel()) + "ControllerTest.java");
        convert.setService(BuilderHelper.firstToUpper(table.getTableLabel()) + "Service.java");
        convert.setServiceImpl(BuilderHelper.firstToUpper(table.getTableLabel()) + "ServiceImpl.java");
        convert.setServiceTest(BuilderHelper.firstToUpper(table.getTableLabel()) + "ServiceTest.java");
        convert.setDao(BuilderHelper.firstToUpper(table.getTableLabel()) + "Dao.java");
        convert.setDaoImpl(BuilderHelper.firstToUpper(table.getTableLabel()) + "DaoImpl.java");
        convert.setDaoTest(BuilderHelper.firstToUpper(table.getTableLabel()) + "DaoTest.java");
        convert.setMybatis(BuilderHelper.firstToUpper(table.getTableLabel()) + "Mapper.xml");
        convert.setHtmlMan(BuilderHelper.firstToUpper(table.getTableLabel()) + "Man.html");
        convert.setHtmlAdd(BuilderHelper.firstToUpper(table.getTableLabel()) + "Add.html");
        convert.setHtmlModify(BuilderHelper.firstToUpper(table.getTableLabel()) + "Modify.html");
        convert.setHtmlDetail(BuilderHelper.firstToUpper(table.getTableLabel()) + "Detail.html");
        convert.setJsMan(BuilderHelper.firstToUpper(table.getTableLabel()) + "Man.js");
        convert.setJsAdd(BuilderHelper.firstToUpper(table.getTableLabel()) + "Add.js");
        convert.setJsModify(BuilderHelper.firstToUpper(table.getTableLabel()) + "Modify.js");
        convert.setJsDetail(BuilderHelper.firstToUpper(table.getTableLabel()) + "Detail.js");
        convert.setLanguage(BuilderHelper.firstToUpper(table.getTableLabel()) + "Page.js");
        table.setConvert(convert);
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
     * 生成文件,对应一个文件
     *
     * @param velocityList
     */
    private static void createFile(List<VelocityConfig> velocityList) throws IOException {
        // 初始化一个模板引擎
        Velocity.init();
        // 实例化一个VelocityEngine对象
        Template template = null;
        for (VelocityConfig velocity : velocityList) {
            String templateName = velocity.getTemplateName();
            template = Velocity.getTemplate(templateName, "UTF-8");
            String filePath = velocity.getFilePath() + File.separator;
            if (velocity.isConfigFlag()) {
                String fileName = velocity.getSuffix();
                StringWriter sw = new StringWriter();
                template.merge(velocityContext, sw);
                helper.writeToFile(filePath, fileName, sw.toString());
                System.out.println("生成文件：" + filePath + fileName);
            } else {
                for (TableModel tableModel : tableModelList) {
                    velocityContext.put("tableModel", tableModel);
                    StringWriter sw = new StringWriter();
                    template.merge(velocityContext, sw);
                    String fileName = BuilderHelper.firstToUpper(tableModel.getTableLabel());
                    if (StringUtils.isNotEmpty(velocity.getPrefix())) {
                        fileName = velocity.getPrefix() + fileName;
                    }
                    if (StringUtils.isNotEmpty(velocity.getSuffix())) {
                        fileName = fileName + velocity.getSuffix();
                    }
                    helper.writeToFile(filePath, fileName, sw.toString());
                    System.out.println("生成文件：" + filePath + fileName);
                }
            }
        }
    }

}