package com.justwzhang.tastytome.shared.interfaces;
import java.util.List;

import org.jooq.Record;

public interface ICrudHook<ID, R extends Record, C, U, M> {
    // public List<M> list();
    // public List<M> list(Condition condition);

    // public M get(ID id);
    // public M get(ID id, Condition condition);


    // public M create(C form);
    // public List<M> create(List<C> forms);

    // public M update(U form);
    // public List<M> update(List<U> form);

    

    // public boolean delete(ID id);

    public M populate(M model);
    public List<M> populateList(List<M> models);

    public R prepareCreate(C form);
    public R beforePersistCreate(R record, C form);
    public M afterPersistCreate(M model, R record, C form);

    public R prepareUpdate(U form);
    public R beforePersistUpdate(R record, U form);
    public M afterPersistUpdate(M model, R record, U form);

    public boolean beforePersistDelete(ID id);
    public void afterPersistDelete(ID id);
    
}
