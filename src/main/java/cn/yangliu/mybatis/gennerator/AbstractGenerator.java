package cn.yangliu.mybatis.gennerator;

import cn.yangliu.mybatis.bean.ColumnType;
import cn.yangliu.mybatis.bean.JavaType;
import cn.yangliu.mybatis.service.ColumnTypeService;
import cn.yangliu.mybatis.service.JavaTypeService;
import cn.yangliu.mybatis.source.Source;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;


@Data
public abstract class AbstractGenerator<S extends Source> implements Generator<S> {

    @Autowired
    protected CodeTemplate template;


    @Autowired
    private JavaTypeService javaTypeService;
    @Autowired
    private ColumnTypeService columnTypeService;

    @PostConstruct
    @Autowired
    public void init() {

        if (javaTypeMap == null) {
            synchronized (this) {
                if (javaTypeMap == null) {
                    javaTypeMap = new HashMap<>();
                    javaFullTypeMap = new HashMap<>();
                    List<JavaType> javaTypes = javaTypeService.selectList(new EntityWrapper<>());
                    javaTypes.forEach(t -> {
                        javaTypeMap.put(t.getId(), t);
                        javaFullTypeMap.put(t.getFullName(), t);
                    });

                    columnMap = new HashMap<>();
                    List<ColumnType> columnTypes = columnTypeService.selectList(new EntityWrapper<>());
                    columnTypes.forEach(t -> columnMap.put(t.getColumnType(), t.getId()));
                }
            }
        }


    }

    protected static Map<String, Long> columnMap;

    protected static Map<Long, JavaType> javaTypeMap;

    protected static Map<String, JavaType> javaFullTypeMap;


    protected String generateImports(String source, List<String> imports) {
        Set<String> set = new HashSet<>(imports);
        imports = new ArrayList<>(set);
        Collections.sort(imports);

        StringBuilder sb = new StringBuilder();
        for (String anImport : imports) {
            if (anImport.startsWith("java.lang")){
                continue;
            }
            String code = template.t_import.replace("[import]", anImport);
            sb.append(code);
        }
        String code = sb.toString().trim();

        return source.replace("[imports]", code);
    }


    protected String generateAnnontations(String source, List<String> annontations) {
        Set<String> set = new HashSet<>(annontations);
        annontations = new ArrayList<>(set);
        Collections.sort(annontations);

        StringBuilder sb = new StringBuilder();
        for (String s : annontations) {
            String code = template.t_annontation.replace("[annotation]", s);
            sb.append(code);
        }
        String code = sb.toString().trim();

        return source.replace("[annontations]", code);
    }

    protected String getClassPackage(String fullName) {

        if (fullName.contains(".")) {
            return fullName.substring(0, fullName.lastIndexOf("."));
        }

        return "";
    }

    protected String getClassShortName(String fullName) {

        if (fullName.contains(".")) {
            return fullName.substring(fullName.lastIndexOf(".") + 1);
        }

        return fullName;
    }


    protected boolean checkPackageIsSame(String firstPackage, String classFullName) {
        if (classFullName.contains(".") && !Objects.equals(firstPackage, "")) {
            String classPackage = classFullName.substring(classFullName.lastIndexOf(".") + 1);
            return Objects.equals(firstPackage, classPackage);
        }
        return false;
    }

}
