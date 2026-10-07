package com.justwzhang.tastytome.shared;

import java.util.List;

import org.jooq.Record;

import com.justwzhang.tastytome.shared.interfaces.ICrudHook;

public class BaseCrudHook<ID, R extends Record, C, U, M> implements ICrudHook<ID, R, C, U, M>{


    @Override
    public M populate(M model) {
        return model;
    }

    @Override
    public List<M> populateList(List<M> models) {
        return models;
    }

    @Override
    public R prepareCreate(C form) {
        throw new UnsupportedOperationException("Unimplemented method 'prepareCreate'");
    }

    @Override
    public R beforePersistCreate(R record, C form) {
        return record;
    }

    @Override
    public M afterPersistCreate(M model, R record, C form) {
        return model;
    }

    @Override
    public R prepareUpdate(U form) {
        throw new UnsupportedOperationException("Unimplemented method 'prepareUpdate'");
    }

    @Override
    public R beforePersistUpdate(R record, U form) {
        return record;
    }

    @Override
    public M afterPersistUpdate(M model, R record, U form) {
        return model;
    }

    @Override
    public boolean beforePersistDelete(ID id) {
        return true;
    }

    @Override
    public void afterPersistDelete(ID id) {}
    
}
