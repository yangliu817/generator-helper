package cn.yangliu.mybatis.gennerator;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.source.RepositorySource;
import cn.yangliu.mybatis.tools.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Repository generator.
 *
 * @author 杨柳
 * @date 2019 -01-11
 */
@Component
public class RepositoryGenerator extends AbstractDaoGnerator<RepositorySource> {

    @Autowired
    private CodeTemplate template;

    @Override
    public void generate(RepositorySource source) {
        String code = template.t_repository;
        gennerateBaseRepository(source);
        code = generateComments(code, source);
        code = generatePackage(source, code);
        code = generateCopyRight(code,source);
        List<String> annotations = new ArrayList<>();
        List<String> imports = new ArrayList<>();
        if (source.getUseRepositoryAnonntation()) {
            imports.add(ApplicationContant.config.getProperty("Repository"));
            annotations.add("Repository");
        }

        code = code.replace("[className]", source.getShortName());
        code = code.replace("[primaryKeyType]", getClassShortName(source.getPrimaryKeyType()));
        code = code.replace("[entityClassName]", source.getEntitySource().getEntityName());

        imports.add(source.getEntitySource().getClassFullName());

        code = generateAnnotations(code, annotations);
        code = generateImports(code, imports);

        FileUtils.output(code, source.getFilepath(), source.getFilename());
    }

    /**
     * Gennerate base repository.
     *
     * @param source the source
     */
    protected void gennerateBaseRepository(RepositorySource source){
        String fileName = "BaseRepository.java";
        String filePathName = source.getFilepath() + File.separator + fileName;
        if (new File(filePathName).exists()){
            return;
        }
        String packageName = source.getFullPackage();

        String code = template.t_base_repository;
        code = generateCopyRight(code,source);
        code = generateComments(code, source);
        String packageCode = "";
        if (StringUtils.isNotEmpty(packageName)) {
            packageCode = "package " + packageName + ";";
        }
        code = code.replace("[package]", packageCode);
        FileUtils.output(code, source.getFilepath(), fileName);
    }
}
