package cn.yangliu.mybatis.gennerator;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.bean.ColumnType;
import cn.yangliu.mybatis.bean.JavaType;
import cn.yangliu.mybatis.bean.MappingSetting;
import cn.yangliu.mybatis.enums.OrmTypeEnum;
import cn.yangliu.mybatis.service.ColumnTypeService;
import cn.yangliu.mybatis.service.JavaTypeService;
import cn.yangliu.mybatis.service.MappingSettingService;
import cn.yangliu.mybatis.source.CodeSource;
import cn.yangliu.mybatis.source.EntitySource;
import cn.yangliu.mybatis.source.Source;
import cn.yangliu.mybatis.tools.CodeUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;


/**
 * @author mechrevo
 */
@Data
public abstract class AbstractGenerator<S extends Source> implements Generator<S> {

    @Autowired
    protected CodeTemplate template;

    @Autowired
    private JavaTypeService javaTypeService;
    @Autowired
    private ColumnTypeService columnTypeService;
    @Autowired
    private MappingSettingService mappingSettingService;

    @PostConstruct
    @Autowired
    public void init() {

        if (javaFullTypeMap == null) {
            synchronized (this) {
                if (javaFullTypeMap == null) {
                    column2javaTypeMap = new HashMap<>();
                    javaFullTypeMap = new HashMap<>();
                    mysqlColumnMap = new HashMap<>();
                    mariadbColumnMap = new HashMap<>();
                    oracleColumnMap = new HashMap<>();
                    sqlserverColumnMap = new HashMap<>();

                    List<JavaType> javaTypes = javaTypeService.selectList(new EntityWrapper<>());
                    javaTypes.forEach(t -> {
                        javaFullTypeMap.put(t.getFullName(), t);
                    });

                    List<ColumnType> columnTypes = columnTypeService.selectList(new EntityWrapper<>());
                    columnTypes.forEach(columnType -> {
                        switch (columnType.getDbType()) {
                            case "mysql":
                                mysqlColumnMap.put(columnType.getColumnType(), columnType);
                                break;
                            case "mariadb":
                                mariadbColumnMap.put(columnType.getColumnType(), columnType);
                                break;
                            case "oracle":
                                oracleColumnMap.put(columnType.getColumnType(), columnType);
                                break;
                            case "sqlserver":
                                sqlserverColumnMap.put(columnType.getColumnType(), columnType);
                                break;
                            default:
                                break;
                        }
                    });

                    MappingSetting queryPojo = new MappingSetting(0L, null);
                    List<MappingSetting> mappingSettings = mappingSettingService.loadMapping(queryPojo);

                    mappingSettings.forEach(m -> {
                        switch (m.getDbType()) {
                            case "mysql":
                                column2javaTypeMap.put(mysqlColumnMap.get(m.getColumnType()), javaFullTypeMap.get(m.getFullName()));
                                break;
                            case "mariadb":
                                column2javaTypeMap.put(mariadbColumnMap.get(m.getColumnType()), javaFullTypeMap.get(m.getFullName()));
                                break;
                            case "oracle":
                                column2javaTypeMap.put(oracleColumnMap.get(m.getColumnType()), javaFullTypeMap.get(m.getFullName()));
                                break;
                            case "sqlserver":
                                column2javaTypeMap.put(sqlserverColumnMap.get(m.getColumnType()), javaFullTypeMap.get(m.getFullName()));
                                break;
                            default:
                                break;
                        }
                    });
                }
            }
        }


    }

    public static Map<String, ColumnType> mysqlColumnMap;
    public static Map<String, ColumnType> mariadbColumnMap;
    public static Map<String, ColumnType> oracleColumnMap;
    public static Map<String, ColumnType> sqlserverColumnMap;

    public static Map<ColumnType, JavaType> column2javaTypeMap;

    public static Map<String, JavaType> javaFullTypeMap;


    /**
     * 生成导包代码
     */
    protected String generateImports(String source, List<String> imports) {
        Set<String> set = new HashSet<>(imports);
        imports = new ArrayList<>(set);
        Collections.sort(imports);

        StringBuilder sb = new StringBuilder();
        for (String anImport : imports) {
            if (anImport.startsWith("java.lang")) {
                continue;
            }
            String code = template.t_import.replace("[import]", anImport);
            sb.append(code);
        }
        String code = sb.toString().trim();

        return source.replace("[imports]", code);
    }

    /**
     * 生成类注释代码
     */
    protected String generateComments(String source, CodeSource codeSource) {
        String author = codeSource.getAuthor();
        String contact = codeSource.getContact();
        String date = codeSource.getDate();

        source = source.replace("[contact]", contact);
        source = source.replace("[author]", author);
        source = source.replace("[date]", date);
        return source;
    }

    /**
     * 生成注解代码
     */
    protected String generateAnnotations(String source, List<String> annotations) {
        Set<String> set = new HashSet<>(annotations);
        annotations = new ArrayList<>(set);
        Collections.sort(annotations);

        StringBuilder sb = new StringBuilder();
        for (String s : annotations) {
            String code = template.t_annotation.replace("[annotation]", s);
            sb.append(code);
        }
        String code = sb.toString().trim();
        source = source.replace("[annotations]", code);

        return source;
    }

    /**
     * 获取类的包名
     */
    protected String getClassPackage(String fullName) {

        if (fullName.contains(ApplicationContant.PACKAGE_SEPARATOR)) {
            return fullName.substring(0, fullName.lastIndexOf(ApplicationContant.PACKAGE_SEPARATOR));
        }

        return "";
    }

    /**
     * 获取类的短类名
     */
    protected String getClassShortName(String fullName) {

        if (fullName.contains(ApplicationContant.PACKAGE_SEPARATOR)) {
            return fullName.substring(fullName.lastIndexOf(ApplicationContant.PACKAGE_SEPARATOR) + 1);
        }

        return fullName;
    }


    /**
     * 比较类所在的包和目标包是否一致
     */
    protected boolean checkPackageIsSame(String firstPackage, String classFullName) {
        if (classFullName.contains(ApplicationContant.PACKAGE_SEPARATOR) && !Objects.equals(firstPackage, "")) {
            String classPackage = classFullName.substring(classFullName.lastIndexOf(ApplicationContant.PACKAGE_SEPARATOR) + 1);
            return Objects.equals(firstPackage, classPackage);
        }
        return false;
    }

    /**
     *  生成log实例
     */
    protected String checkUseLombok(CodeSource source, String code, List<String> imports, List<String> anontations) {
        if (source.getUseLombok()) {
            imports.add(ApplicationContant.config.getProperty("Slf4j"));
            anontations.add("Slf4j");
            code = code.replace("[logger]", "");
        } else {
            imports.add(ApplicationContant.config.getProperty("Logger"));
            imports.add(ApplicationContant.config.getProperty("LoggerFactory"));
            code = code.replace("[logger]", "private static final Logger log = LoggerFactory.getLogger(" + source.getShortName() + ".class);");
        }
        return code;
    }

    /**
     * 生成组件类属性代码
     */
    protected String generateComponentFields(String code, String fieldType) {
        String fieldName = CodeUtils.firstChar2Lowercase(fieldType);

        String fieldCode = template.t_field;

        fieldCode = fieldCode.replace("[comment]", "").replace("[annotations]", "@Autowired");
        fieldCode = fieldCode.replace("[fieldName]", fieldName).replace("[fieldType]", fieldType);

        code = code.replace("[fields]", fieldCode);
        return code;
    }

    /**
     * 生成接口公共抽象方法
     */
    protected String generateAbstractMethods(String code, EntitySource source, Collection<String> imports) {
        String templateCode = template.t_abstract_methods_normal;

        if (source.isContainsPrimaryKey()) {
            templateCode = templateCode + template.t_abstract_methods_needprimarykey;
        }
        if (Objects.equals(source.getOrmType(), OrmTypeEnum.JPA)) {
            imports.add(ApplicationContant.config.getProperty("Pageable"));
            imports.add(ApplicationContant.config.getProperty("PageJpa"));
            templateCode = template.t_abstract_methods_jpa;
        }

        imports.add(source.getClassFullName());
        imports.add(ApplicationContant.config.getProperty("List"));
        code = code.replace("[methods]", templateCode);
        code = code.replace("[entityClass]", source.getShortName());
        code = code.replace("[primaryKeyType]", getClassShortName(source.getPrimaryKeyType()));
        return code;
    }

    /**
     * 生成包代码
     */
    protected String generatePackage(CodeSource source, String code) {
        String packageCode = "";
        if (StringUtils.isNotEmpty(source.getFullPackage())) {
            packageCode = "package " + source.getFullPackage() + ";";
        }
        code = code.replace("[package]", packageCode);
        return code;
    }
}
