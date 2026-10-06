package com.justwzhang.tastytome.shared;

import java.util.Arrays;
import java.util.Optional;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;

/**
 * 
 * DefaultRepository
 * @param <R> The Record 
 * @param <M> The Model Class
 * 
 * This provides the basic implementation of the repository. 
 * It provides the CRUD operations that are universal across all tables
 */
public class DefaultRepository<R extends Record, M> extends BaseRepository<R, M, Long>{
    private final Field<Long> idField;

    public DefaultRepository(DSLContext ctx, Table<R> table, Class<M> modelClass){
        super(ctx, table, modelClass);
        this.idField = findIdField().orElse(null);
    }


    private Optional<Field<Long>> findIdField(){
        return Arrays.stream(this.table.fields()).filter(field ->field.getDataType().isNumeric()).filter(field->field.getName().endsWith("_id")).map(field->(Field<Long>)field).findFirst();
    }
    @Override
    public Field<Long> getIdField() {
        return this.idField;
    }
    
}
