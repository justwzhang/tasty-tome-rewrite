package com.justwzhang.tastytome.shared;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;

import com.justwzhang.tastytome.shared.interfaces.ICrudRepository;

/**
 * 
 * BaseRepository
 * @param <R> The Record 
 * @param <M> The Model Class
 * @param <ID> The id type for the class, most cases are Long
 */
public abstract class BaseRepository <R extends Record, M, ID> implements ICrudRepository<R, M, ID>{

    private final DSLContext ctx;
    final Table<R> table;
    final Class<M> modelClass;

    public BaseRepository(DSLContext ctx, Table<R> table, Class<M> modelClass){
        this.ctx = ctx;
        this.table = table; 
        this.modelClass = modelClass;
    }

    public abstract Field<ID> getIdField(); 

    public Field<?>[] defaultSelect() {
        return table.fields();
    }

    public Condition defaultWhere() {
        return DSL.trueCondition();
    }

    @Override
    public List<M> list() {
        return ctx.select(defaultSelect())
            .from(table)
            .where(defaultWhere())
            .fetchInto(modelClass);
    }
    @Override
    public List<M> list(Condition condition){
        return ctx.select(defaultSelect())
            .from(table)
            .where(defaultWhere())
            .and(condition)
            .fetchInto(modelClass);
    }
    @Override
    public Optional<M> get(ID id) {
        return Optional.ofNullable(ctx.select(defaultSelect())
            .from(table).where(defaultWhere())
            .and(getIdField().eq(id))
            .fetchOneInto(modelClass));
    }
    @Override 
    public Optional<M> get(ID id, Condition condition){
        return Optional.ofNullable(ctx.select(defaultSelect())
            .from(table).where(defaultWhere())
                .and(getIdField().eq(id))
                .and(condition)
            .fetchOneInto(modelClass));
    }

    @Override 
    public M create(R record) {
        return ctx.insertInto(table).set(record).returning().fetchOneInto(modelClass);
    }

    @Override 
    public List<M> create(List<R> records) {
        return ctx.insertInto(table).set(records).returning().fetchInto(modelClass);
    }

    @Override
    public M update(R record) {
        ID id = record.get(getIdField());
        return ctx.update(table)
            .set(record)
            .where(getIdField().eq(id))
            .and(defaultWhere())
            .returning()
            .fetchOneInto(modelClass);
    }

    @Override
    public List<M> update(List<R> records) {
        List<ID> ids = records.stream()
            .map(r -> r.get(getIdField()))
            .collect(Collectors.toList());
        records.forEach(record -> {
            ctx.update(table)
                .set(record)
                .where(getIdField().eq(record.get(getIdField())))
                .and(defaultWhere())
                .execute();
        });
        return ctx.select(defaultSelect())
            .from(table)
            .where(getIdField().in(ids))
            .and(defaultWhere())
            .fetchInto(modelClass);
    }

    @Override
    public boolean delete(ID id) {
        int affected = ctx.delete(table)
            .where(getIdField().eq(id))
            .and(defaultWhere())
            .execute();
        return affected > 0;
    }
    
}
