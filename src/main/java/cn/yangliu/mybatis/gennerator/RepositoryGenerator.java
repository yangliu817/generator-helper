package cn.yangliu.mybatis.gennerator;

import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.source.RepositorySource;
import cn.yangliu.mybatis.tools.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 杨柳
 * @date 2019-01-11
 */
@Component
public class RepositoryGenerator extends AbstractDaoGnerator<RepositorySource> {

    @Autowired
    private CodeTemplate template;

    @Override
    public void generate(RepositorySource source) {
        String code = template.t_repository;

        code = generateComments(code, source);
        code = generatePackage(source, code);

        List<String> annotations = new ArrayList<>();
        List<String> imports = new ArrayList<>();
        if (source.getUseRepositoryAnonntation()) {
            imports.add(ApplicationContant.config.getProperty("Repository"));
            annotations.add("Repository");
        }

        code = code.replace("[className]", source.getShortName());
        code = code.replace("[primaryKeyType]", getClassShortName(source.getPrimaryKeyType()));
        code = code.replace("[entityClassName]", source.getEntitySource().getShortName());

        imports.add(source.getEntitySource().getClassFullName());
        imports.add(ApplicationContant.config.getProperty("JpaRepository"));
        imports.add(ApplicationContant.config.getProperty("JpaSpecificationExecutor"));

        code = generateAnnotations(code, annotations);
        code = generateImports(code, imports);

        FileUtils.output(code, source.getFilepath(), source.getFilename());
    }
}
