package cn.yangliu.mybatis.gennerator;

import cn.yangliu.mybatis.source.Source;

public interface Generator<S extends Source> {

    /**
     * 生成代码
     * @param source 包含当前代码所有信息
     */
    void generate(S source);

}
