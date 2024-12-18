package com.jeesite.modules.ems.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.concurrent.Callable;

/**
 * 自定义sql执行
 *
 * @param <T>
 */
public class SqlExecuteCallable<T> implements Callable {
    /**
     * 数据库访问
     */
    private JdbcTemplate jdbcTemplate;
    /**
     * 待执行sql
     */
    private String sql;
    /**
     * 执行参数
     */
    private Object[] args;
    /**
     * 封装策略
     */
    private ResultSetExtractor<T> rse;

    public SqlExecuteCallable(JdbcTemplate jdbcTemplate, String sql, Object[] args, ResultSetExtractor rse) {
        this.jdbcTemplate = jdbcTemplate;
        this.sql = sql;
        this.args = args;
        this.rse = rse;
    }

    @Override
    public T call() {
        return jdbcTemplate.query(sql, args, rse);
    }
}
