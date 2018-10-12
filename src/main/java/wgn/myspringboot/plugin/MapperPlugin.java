package wgn.myspringboot.plugin;


import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.util.StringUtility;
import tk.mybatis.mapper.util.StringUtil;


import java.io.File;
import java.util.*;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * Company        :   mamahao.com
 * author         :   wangguannan
 * Date           :   2018/7/9
 * Time           :   下午7:09
 * Description    :
 */
public class MapperPlugin extends PluginAdapter {
    public class MyClass extends InnerClass implements CompilationUnit {
        private Set<FullyQualifiedJavaType> importedTypes;
        private Set<String> staticImports;
        private List<String> fileCommentLines;

        public MyClass(FullyQualifiedJavaType type) {
            super(type);
            this.importedTypes = new TreeSet();
            this.fileCommentLines = new ArrayList();
            this.staticImports = new TreeSet();
        }

        public MyClass(String type) {
            this(new FullyQualifiedJavaType(type));
        }

        public Set<FullyQualifiedJavaType> getImportedTypes() {
            return this.importedTypes;
        }

        public void addImportedType(FullyQualifiedJavaType importedType) {
            if (importedType.isExplicitlyImported() && !importedType.getPackageName().equals(this.getType().getPackageName())) {
                this.importedTypes.add(importedType);
            }

        }

        public String getFormattedContent() {
            return this.getFormattedContent(0, this);
        }

        public String getFormattedContent(int indentLevel, CompilationUnit compilationUnit) {
            StringBuilder sb = new StringBuilder();
            Iterator var4 = this.fileCommentLines.iterator();

            String staticImport;
            while (var4.hasNext()) {
                staticImport = (String) var4.next();
                sb.append(staticImport);
                OutputUtilities.newLine(sb);
            }

            if (StringUtility.stringHasValue(this.getType().getPackageName())) {
                sb.append("package ");
                sb.append(this.getType().getPackageName());
                sb.append(';');
                OutputUtilities.newLine(sb);
                OutputUtilities.newLine(sb);
            }

            var4 = this.staticImports.iterator();

            while (var4.hasNext()) {
                staticImport = (String) var4.next();
                sb.append("import static ");
                sb.append(staticImport);
                sb.append(';');
                OutputUtilities.newLine(sb);
            }

            if (this.staticImports.size() > 0) {
                OutputUtilities.newLine(sb);
            }

            Set<String> importStrings = OutputUtilities.calculateImports(this.importedTypes);
            Iterator var8 = importStrings.iterator();

            while (var8.hasNext()) {
                String importString = (String) var8.next();
                sb.append(importString);
                OutputUtilities.newLine(sb);
            }

            if (importStrings.size() > 0) {
                OutputUtilities.newLine(sb);
            }

            sb.append(super.getFormattedContent(0, this));
            return sb.toString();
        }

        public void addFileCommentLine(String commentLine) {
            this.fileCommentLines.add(commentLine);
        }

        public List<String> getFileCommentLines() {
            return this.fileCommentLines;
        }

        public void addImportedTypes(Set<FullyQualifiedJavaType> importedTypes) {
            this.importedTypes.addAll(importedTypes);
        }

        public Set<String> getStaticImports() {
            return this.staticImports;
        }

        @Override
        public boolean isJavaInterface() {
            return false;
        }

        @Override
        public boolean isJavaEnumeration() {
            return false;
        }

        public void addStaticImport(String staticImport) {
            this.staticImports.add(staticImport);
        }

        public void addStaticImports(Set<String> staticImports) {
            this.staticImports.addAll(staticImports);
        }
    }

    private static final String DEFAULT_DAO_SUPER_CLASS = "IMapper";
    private static final String DEFAULT_EXPAND_DAO_SUPER_CLASS = "IMapper";
    private String daoTargetDir;
    private String daoTargetPackage;

    private String daoSuperClass;

    private String voTargetPackage;
    private String voTargetDir;

    // 扩展
    private String expandDaoTargetPackage;
    private String expandDaoSuperClass;

    private String entityTargetProject;
    private String entityTargetPackage;

    private ShellCallback shellCallback = null;

    public MapperPlugin() {
        shellCallback = new DefaultShellCallback(false);
    }

    /**
     * 验证参数是否有效
     *
     * @param warnings
     * @return
     */
    public boolean validate(List<String> warnings) {
        daoTargetDir = properties.getProperty("targetProject");
        boolean valid = stringHasValue(daoTargetDir);

        daoTargetPackage = properties.getProperty("targetPackage");
        boolean valid2 = stringHasValue(daoTargetPackage);

        daoSuperClass = properties.getProperty("daoSuperClass");
        if (!stringHasValue(daoSuperClass)) {
            daoSuperClass = DEFAULT_DAO_SUPER_CLASS;
        }

        expandDaoTargetPackage = properties.getProperty("expandTargetPackage");
        expandDaoSuperClass = properties.getProperty("expandDaoSuperClass");
        if (!stringHasValue(expandDaoSuperClass)) {
            expandDaoSuperClass = DEFAULT_EXPAND_DAO_SUPER_CLASS;
        }

        voTargetDir = properties.getProperty("voTargetProject");
        voTargetPackage = properties.getProperty("voTargetPackage");

        entityTargetProject = properties.getProperty("entityTargetProject");
        entityTargetPackage = properties.getProperty("entityTargetPackage");

        return valid && valid2;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement select = new XmlElement("select");
        select.addAttribute(new Attribute("id", "selectAll"));
        select.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        select.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
        select.addElement(new TextElement(" select * from " + introspectedTable.getFullyQualifiedTableNameAtRuntime()));

        XmlElement parentElement = document.getRootElement();
        parentElement.addElement(select);
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    private void createVO(IntrospectedTable introspectedTable, List<GeneratedJavaFile> mapperJavaFiles,
                          String shortName, JavaFormatter javaFormatter, CompilationUnit unit, String pix) {
        String replace = pix + "VO";
        MyClass voUnit = new MyClass(
                voTargetPackage + "." + shortName.replace("Entity", replace));
        voUnit.setVisibility(JavaVisibility.PUBLIC);
        voUnit.addJavaDocLine("/**");
        voUnit.addJavaDocLine(" * " + shortName + "扩展");
        voUnit.addJavaDocLine(" */");
        FullyQualifiedJavaType Serializable = new FullyQualifiedJavaType("Serializable");
        voUnit.addSuperInterface(Serializable);
        InnerClass unittmp = (InnerClass) unit;

        for (Field field : unittmp.getFields()) {
            Field field1 = new Field(field);
/*                        if(field1.getType().toString().equals("java.util.Date")) {
                            field1.addJavaDocLine("@JsonFormat(timezone = \"GMT+8\", pattern = \"yyyy-MM-dd HH:mm:ss\")");
                        }*/

            List<String> javaDocLines = field1.getJavaDocLines();
            String remark = javaDocLines.get(1).replace("*", "").trim();
            String[] strings1 = remark.split("#");

            String strApiModel = "@ApiModelProperty(value=\"" + strings1[0] + "\"";

            Boolean hidden=false;

            if (pix.equals("")) {

                String[] strings = strings1[0].split("|");

                if (strings.length > 1) {
                    if (strings[1].indexOf("必填") != -1) {
                        strApiModel += ",required=true";
                    }
                    if (strings[1].indexOf("隐藏") != -1) {
                        strApiModel += ",hidden=true";
                        hidden=true;
                    }
                }


            } else {
                Integer index = -1;
                for (int i = 0; i < strings1.length; i++) {
                    String[] strings2 = strings1[i].split("|");
                    for (int j = 0; j < strings2.length; j++) {
                        if (strings2.length > 0 && strings2[0].equals(pix)) {
                            index = i;
                        }
                    }
                }
                if (index > -1) {
                    String[] strings = strings1[index].split("|");
                    if (strings.length > 1) {
                        if (strings[1].indexOf("必填") != -1) {
                            strApiModel += ",required=true";
                        }
                        if (strings[1].indexOf("显示") == -1){
                            strApiModel += ",hidden=true";
                            hidden=true;
                        }
                    }
                } else if (index == -1) {
                    strApiModel += ",hidden=true";
                    hidden=true;
                }

            }


            strApiModel += ")";

            field1.addJavaDocLine(strApiModel);
            if(hidden!=true) voUnit.addField(field1);


        }


        for (Method method : unittmp.getMethods()) {
            Method method1 = new Method(method);


            List<String> javaDocLines = method1.getJavaDocLines();
            String remark = javaDocLines.get(1).replace("*", "").trim();
            String[] strings1 = remark.split("#");

            String strApiModel = "@ApiModelProperty(value=\"" + strings1[0] + "\"";

            Boolean hidden=false;

            if (pix.equals("")) {

                String[] strings = strings1[0].split("|");

                if (strings.length > 1) {
                    if (strings[1].indexOf("隐藏") != -1) {
                        strApiModel += ",hidden=true";
                        hidden=true;
                    }
                }


            } else {
                Integer index = -1;
                for (int i = 0; i < strings1.length; i++) {
                    String[] strings2 = strings1[i].split("|");
                    for (int j = 0; j < strings2.length; j++) {
                        if (strings2.length > 0 && strings2[0].equals(pix)) {
                            index = i;
                        }
                    }
                }
                if (index > -1) {
                    String[] strings = strings1[index].split("|");
                    if (strings.length > 1) {
                        if (strings[1].indexOf("显示") == -1){
                            strApiModel += ",hidden=true";
                            hidden=true;
                        }
                    }
                } else if (index == -1) {
                    strApiModel += ",hidden=true";
                    hidden=true;
                }

            }


            strApiModel += ")";

            method1.addJavaDocLine(strApiModel);
            if(hidden!=true) voUnit.addMethod(method1);
        }

        String tableName = introspectedTable.getTableConfiguration().getTableName();

        for (int j = 0; j < 20; j++) {
            if (stringHasValue(properties.getProperty(tableName + "_" + String.valueOf(j) + "_extand_name"))) {
                FullyQualifiedJavaType fullyQualifiedJavaType = new FullyQualifiedJavaType("private " + properties.getProperty(tableName + "_" + String.valueOf(j) + "_extand_type").replace("VO", replace));
                Field field = new Field(properties.getProperty(tableName + "_" + String.valueOf(j) + "_extand_name"), fullyQualifiedJavaType);
                for (int i = 0; i < 3; i++) {
                    if (stringHasValue(properties.getProperty(tableName + "_" + String.valueOf(j) + "_extand_docline" + String.valueOf(i))))
                        field.addJavaDocLine(properties.getProperty(tableName + "_" + String.valueOf(j) + "_extand_docline" + String.valueOf(i)));
                }

                String remark = field.getJavaDocLines().get(0).replace("*", "").trim();
                String[] strings1 = remark.split("#");

                String strApiModel = "@ApiModelProperty(value=\"" + strings1[0] + "\"";

                Boolean hidden=false;

                if (pix.equals("")) {

                    String[] strings = strings1[0].split("|");

                    if (strings.length > 1) {
                        if (strings[1].indexOf("隐藏") != -1) {

                            hidden=true;
                        }
                    }


                } else {
                    Integer index = -1;
                    for (int i = 0; i < strings1.length; i++) {
                        String[] strings2 = strings1[i].split("|");
                        for (int k = 0; k < strings2.length; k++) {
                            if (strings2.length > 0 && strings2[0].equals(pix)) {
                                index = i;
                            }
                        }
                    }
                    if (index > -1) {
                        String[] strings = strings1[index].split("|");
                        if (strings.length > 1) {
                            if (strings[1].indexOf("显示") == -1){
                                hidden=true;
                            }
                        }
                    } else if (index == -1) {
                        hidden=true;
                    }
                }



                if(hidden==false) voUnit.addField(field);

                String fieldName = field.getName();

                String function = null;
                if (fieldName.substring(1, 2).toUpperCase().equals(fieldName.substring(1, 2))) {
                    function = fieldName;
                } else {
                    function = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
                }

                Method method = new Method();
                method.setName("get" + function);
                FullyQualifiedJavaType fullyQualifiedJavaType1 = new FullyQualifiedJavaType("public " + properties.getProperty(tableName + "_" + String.valueOf(j) + "_extand_type").replace("VO", replace));
                method.setReturnType(fullyQualifiedJavaType1);
                method.addBodyLine("return " + fieldName + ";");
                if(hidden==false) voUnit.addMethod(method);

                Method method1 = new Method();
                method1.setName("set" + function);
                FullyQualifiedJavaType fullyQualifiedJavaType2 = new FullyQualifiedJavaType("public void");
                method1.setReturnType(fullyQualifiedJavaType2);

                FullyQualifiedJavaType fullyQualifiedJavaType3 = new FullyQualifiedJavaType(properties.getProperty(tableName + "_" + String.valueOf(j) + "_extand_type").replace("VO", replace));
                Parameter parameter = new Parameter(fullyQualifiedJavaType3, fieldName);
                method1.addParameter(parameter);
                method1.addBodyLine("this." + fieldName + "=" + fieldName + ";");
                if(hidden==false) voUnit.addMethod(method1);

            }

        }
        Method method2 = new Method();

        method2.addJavaDocLine("@Override");
        FullyQualifiedJavaType fullyQualifiedJavaType = new FullyQualifiedJavaType("public String");

        method2.setReturnType(fullyQualifiedJavaType);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("return \"" + shortName.replace("Entity", "VO") + "{\"+\n");

        List<Field> fields = voUnit.getFields();
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            if (i == 0) {
                stringBuffer.append("           \"" + field.getName() + "=\"+" + field.getName() + "\n");
            } else {
                stringBuffer.append("           +\"" + field.getName() + "=\"+" + field.getName() + "\n");
            }
        }
        stringBuffer.append("           +\"}\";");
        method2.setName("toString");
        method2.addBodyLine(stringBuffer.toString());
        voUnit.addMethod(method2);

        //导入依赖的包
        FullyQualifiedJavaType daoSuperType0 = new FullyQualifiedJavaType("com.fasterxml.jackson.annotation.JsonFormat");
        voUnit.addImportedType(daoSuperType0);

        FullyQualifiedJavaType daoSuperType1 = new FullyQualifiedJavaType("io.swagger.annotations.ApiModelProperty");
        voUnit.addImportedType(daoSuperType1);

        FullyQualifiedJavaType daoSuperType2 = new FullyQualifiedJavaType("java.util.List");
        voUnit.addImportedType(daoSuperType2);

        FullyQualifiedJavaType daoSuperType3 = new FullyQualifiedJavaType("java.io.Serializable");
        voUnit.addImportedType(daoSuperType3);



        GeneratedJavaFile mapperJavafile = new GeneratedJavaFile(voUnit, voTargetDir, javaFormatter);
        try {
            File mapperDir = shellCallback.getDirectory(voTargetDir, voTargetPackage);
            File mapperFile = new File(mapperDir, mapperJavafile.getFileName());
            // 文件不存在
            mapperJavaFiles.add(mapperJavafile);
        } catch (ShellException e) {
            e.printStackTrace();
        }
    }

    ;

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        JavaFormatter javaFormatter = context.getJavaFormatter();
        List<GeneratedJavaFile> mapperJavaFiles = new ArrayList<GeneratedJavaFile>();
        for (GeneratedJavaFile javaFile : introspectedTable.getGeneratedJavaFiles()) {
            CompilationUnit unit = javaFile.getCompilationUnit();
            FullyQualifiedJavaType baseModelJavaType = unit.getType();

            String shortName = baseModelJavaType.getShortName();

            GeneratedJavaFile mapperJavafile = null;

            if (shortName.endsWith("Entity")) { // 扩展Mapper

                //生产mapper
                if (stringHasValue(expandDaoTargetPackage)) {

                    Interface mapperInterface = new Interface(
                            expandDaoTargetPackage + "." + shortName.replace("Entity", "Mapper"));
                    mapperInterface.setVisibility(JavaVisibility.PUBLIC);
                    mapperInterface.addJavaDocLine("/**");
                    mapperInterface.addJavaDocLine(" * " + shortName + "扩展");
                    mapperInterface.addJavaDocLine(" */");

                    FullyQualifiedJavaType daoSuperType0 = new FullyQualifiedJavaType(expandDaoSuperClass);
                    daoSuperType0.addTypeArgument(baseModelJavaType);
                    mapperInterface.addImportedType(daoSuperType0);
                    mapperInterface.addSuperInterface(daoSuperType0);

                    mapperInterface.addImportedType(baseModelJavaType);


                    FullyQualifiedJavaType daoSuperType1 = new FullyQualifiedJavaType("com.mamahao.data.framework.mybatis.bean.IMapper");
                    mapperInterface.addImportedType(daoSuperType1);


                    mapperJavafile = new GeneratedJavaFile(mapperInterface, daoTargetDir, javaFormatter);
                    try {
                        File mapperDir = shellCallback.getDirectory(daoTargetDir, daoTargetPackage);
                        File mapperFile = new File(mapperDir, mapperJavafile.getFileName());
                        // 文件不存在
                        if (!mapperFile.exists()) {
                            mapperJavaFiles.add(mapperJavafile);
                        }
                    } catch (ShellException e) {
                        e.printStackTrace();
                    }
                }

                //生成Entity
                if (stringHasValue(entityTargetProject) && stringHasValue(entityTargetPackage)) {

                    InnerClass unittmp = (InnerClass) unit;

                    MyClass voUnit = new MyClass(
                            entityTargetPackage + "." + shortName);
                    voUnit.setVisibility(JavaVisibility.PUBLIC);

                    for (String strTmp : unittmp.getJavaDocLines()) {
                        voUnit.addJavaDocLine(strTmp);
                    }
                    voUnit.addJavaDocLine("@Entity");
                    voUnit.addJavaDocLine("@Table(name=\"" + introspectedTable.getTableConfiguration().getTableName() + "\")");
                    voUnit.addJavaDocLine("@Alias(\"" + shortName + "\")");

                    for (Field field : unittmp.getFields()) {
                        Field field1 = new Field(field);
                        field1.getJavaDocLines().remove(field1.getJavaDocLines().size() - 1);
                        voUnit.addField(field1);
                    }

                    for (Method method : unittmp.getMethods()) {
                        voUnit.addMethod(method);
                    }

                    //导入依赖的包
                    FullyQualifiedJavaType daoSuperType0 = new FullyQualifiedJavaType("com.fasterxml.jackson.annotation.JsonFormat");
                    voUnit.addImportedType(daoSuperType0);

                    FullyQualifiedJavaType daoSuperType1 = new FullyQualifiedJavaType("org.apache.ibatis.type.Alias");
                    voUnit.addImportedType(daoSuperType1);


                    FullyQualifiedJavaType daoSuperType2 = new FullyQualifiedJavaType("javax.persistence.Entity");
                    voUnit.addImportedType(daoSuperType2);

                    FullyQualifiedJavaType daoSuperType3 = new FullyQualifiedJavaType("javax.persistence.Table");
                    voUnit.addImportedType(daoSuperType3);

                    mapperJavafile = new GeneratedJavaFile(voUnit, entityTargetProject, javaFormatter);
                    try {
                        File mapperDir = shellCallback.getDirectory(entityTargetProject, entityTargetPackage);
                        File mapperFile = new File(mapperDir, mapperJavafile.getFileName());
                        // 文件不存在
                        mapperJavaFiles.add(mapperJavafile);
                    } catch (ShellException e) {
                        e.printStackTrace();
                    }
                }

                //生成VO
                if (stringHasValue(voTargetDir) && stringHasValue(voTargetPackage)) {

                    createVO(introspectedTable, mapperJavaFiles, shortName, javaFormatter, unit, "");

                    InnerClass unittmp = (InnerClass) unit;
                    if (unittmp.getJavaDocLines().get(1) != null) {
                        String[] strings = unittmp.getJavaDocLines().get(1).split("#");

                        for (int i = 1; i < strings.length; i++) {
                            createVO(introspectedTable, mapperJavaFiles, shortName, javaFormatter, unit, strings[i]);
                        }
                    }
                }


            }
        }
        return mapperJavaFiles;
    }
}