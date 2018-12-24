package cn.yangliu.mybatis.gennerator;

import cn.yangliu.comm.tools.ReflectUtils;
import cn.yangliu.mybatis.tools.FileUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

@Data
@Component
@Slf4j
public class CodeTemplate {

    protected String t_annontation;
    protected String t_entity;
    protected String t_equals;
    protected String t_equal_method;
    protected String t_extends;
    protected String t_field;
    protected String t_getter;
    protected String t_hash;
    protected String t_implement;
    protected String t_import;
    protected String t_mapper;
    protected String t_service;
    protected String t_service_impl;
    protected String t_setter;
    protected String t_setter_chain;
    protected String t_controller;
    protected String t_constructor;
    protected String t_toString;
    protected String t_if;
    protected String t_if_end;
    protected String t_controller_delete;
    protected String t_controller_getById;
    protected String t_controller_insert;
    protected String t_controller_list;
    protected String t_controller_update;


    public CodeTemplate() {
        List<Field> fields = ReflectUtils.getFields(CodeTemplate.class, Object.class);
        fields.parallelStream().forEach(f -> {
            if (!Modifier.isStatic(f.getModifiers())) {
                String filepath = FileUtils.getFullPath("templates/java", f.getName() + ".java");
                String temp = FileUtils.read(filepath, true);
                ReflectUtils.setValueByField(f, this, temp);
            }
        });
    }
}
