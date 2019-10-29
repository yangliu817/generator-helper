package cn.yangliu.mybatis.gennerator;

import cn.yangliu.comm.tools.ReflectUtils;
import cn.yangliu.mybatis.tools.FileUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * The type Code template.
 */
@Data
@Component
@Slf4j
public class CodeTemplate {

    /**
     * The T annotation.
     */
    protected String t_annotation;
    /**
     * The T entity.
     */
    protected String t_entity;
    /**
     * The T equals.
     */
    protected String t_equals;
    /**
     * The T equal method.
     */
    protected String t_equal_method;
    /**
     * The T extends.
     */
    protected String t_extends;
    /**
     * The T field.
     */
    protected String t_field;
    /**
     * The T getter.
     */
    protected String t_getter;
    /**
     * The T hash.
     */
    protected String t_hash;
    /**
     * The T implement.
     */
    protected String t_implement;
    /**
     * The T import.
     */
    protected String t_import;
    /**
     * The T mapper.
     */
    protected String t_mapper;
    /**
     * The T service.
     */
    protected String t_service;
    /**
     * The T service.
     */
    protected String t_service_impl;
    /**
     * The T setter.
     */
    protected String t_setter;
    /**
     * The T setter chain.
     */
    protected String t_setter_chain;
    /**
     * The T controller.
     */
    protected String t_controller;
    /**
     * The T constructor.
     */
    protected String t_constructor;
    /**
     * The T to string.
     */
    protected String t_toString;
    /**
     * The T if.
     */
    protected String t_if;
    /**
     * The T if end.
     */
    protected String t_if_end;
    /**
     * The T controller delete jpa.
     */
    protected String t_controller_delete_jpa;
    /**
     * The T controller delete mybatis.
     */
    protected String t_controller_delete_mybatis;
    /**
     * The T controller delete mybatis plus.
     */
    protected String t_controller_delete_mybatis_plus;
    /**
     * The T controller get by id jpa.
     */
    protected String t_controller_getById_jpa;
    /**
     * The T controller get by id mybatis.
     */
    protected String t_controller_getById_mybatis;
    /**
     * The T controller get by id mybatis plus.
     */
    protected String t_controller_getById_mybatis_plus;
    /**
     * The T controller save jpa.
     */
    protected String t_controller_save_jpa;
    /**
     * The T controller save mybatis.
     */
    protected String t_controller_save_mybatis;
    /**
     * The T controller save mybatis plus.
     */
    protected String t_controller_save_mybatis_plus;
    /**
     * The T controller list jpa.
     */
    protected String t_controller_list_jpa;
    /**
     * The T controller list mybatis.
     */
    protected String t_controller_list_mybatis;
    /**
     * The T controller list mybatis plus.
     */
    protected String t_controller_list_mybatis_plus;
    /**
     * The T controller update.
     */
    protected String t_controller_update;
    /**
     * The T abstract methods needprimarykey.
     */
    protected String t_abstract_methods_needprimarykey;
    /**
     * The T abstract methods normal.
     */
    protected String t_abstract_methods_normal;
    /**
     * The T service impl methods needprimarykey.
     */
    protected String t_service_impl_methods_needprimarykey;
    /**
     * The T service impl methods normal.
     */
    protected String t_service_impl_methods_normal;
    /**
     * The T comment.
     */
    protected String t_comment;
    /**
     * The T repository.
     */
    protected String t_repository;
    /**
     * The T abstract methods jpa.
     */
    protected String t_abstract_methods_jpa;
    /**
     * The T service impl methods jpa.
     */
    protected String t_service_impl_methods_jpa;
    /**
     * The T if jpa.
     */
    protected String t_if_jpa;
    /**
     * The T service base jpa.
     */
    protected String t_service_base_jpa;
    /**
     * The T service impl base jpa.
     */
    protected String t_service_impl_base_jpa;
    /**
     * The T service base mybatis.
     */
    protected String t_service_base_mybatis;
    /**
     * The T service impl base mybatis.
     */
    protected String t_service_impl_base_mybatis;
    /**
     * The T mapper base mybatis.
     */
    protected String t_mapper_base_mybatis;
    /**
     * The T base repository.
     */
    protected String t_base_repository;


    /**
     * Instantiates a new Code template.
     */
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
