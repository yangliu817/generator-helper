package cn.yangliu.mybatis.gennerator;

import cn.yangliu.mybatis.source.Source;

public interface Generator<S extends Source> {

    void generate(S source);

}
