package com.justwzhang.tastytome.shared;

import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.Record;

import com.justwzhang.tastytome.shared.interfaces.ICrudHook;

public class CrudHookPipeline<ID, R extends Record, C, U, M> implements ICrudHook<ID, R, C, U, M>{

    private final BaseRepository<R, M, ID> repository;
    private final ICrudHook<ID, R, C, U, M>[] hooks;

    @SafeVarargs
    public CrudHookPipeline(BaseRepository<R, M, ID> repository, ICrudHook<ID, R, C, U, M>... hooks){
        this.repository = repository;
        this.hooks = hooks;
    }

    public List<M> list() {
        return repository.list();
    }

    public List<M> list(Condition condition) {
        return repository.list(condition);
    }

    public M get(ID id) {
        return repository.get(id).orElse(null);
    }

    public M get(ID id, Condition condition) {
        return repository.get(id, condition).orElse(null);
    }

    public M create(C form) {
        R record = prepareCreate(form);
        record = beforePersistCreate(record, form);
        M model = repository.create(record);
        return afterPersistCreate(model, record, form);
    }

    public List<M> create(List<C> forms) {
        List<R> records = forms.stream()
            .map(this::prepareCreate)
            .map(r -> beforePersistCreate(r, null))
            .collect(Collectors.toList());
        List<M> models = repository.create(records);
        return populateList(models);
    }

    public M update(U form) {
        R record = prepareUpdate(form);
        record = beforePersistUpdate(record, form);
        M model = repository.update(record);
        return afterPersistUpdate(model, record, form);
    }

    public List<M> update(List<U> forms) {
        List<R> records = forms.stream()
            .map(this::prepareUpdate)
            .map(r -> beforePersistUpdate(r, null))
            .collect(Collectors.toList());
        List<M> models = repository.update(records);
        return populateList(models);
    }

    public boolean delete(ID id) {
        if (!beforePersistDelete(id)) {
            return false;
        }
        boolean deleted = repository.delete(id);
        if (deleted) {
            afterPersistDelete(id);
        }
        return deleted;
    }

    @Override
    public M populate(M model) {
        M result = model;
        for (ICrudHook<ID, R, C, U, M> hook : hooks) {
            result = hook.populate(result);
        }
        return result;
    }

    @Override
    public List<M> populateList(List<M> models) {
        List<M> result = models;
        for (ICrudHook<ID, R, C, U, M> hook : hooks) {
            result = hook.populateList(result);
        }
        return result;
    }

    @Override
    public R prepareCreate(C form) {
        R record = null;
        for (ICrudHook<ID, R, C, U, M> hook : hooks) {
            record = hook.prepareCreate(form);
        }
        return record;
    }

    @Override
    public R beforePersistCreate(R record, C form) {
        for (ICrudHook<ID, R, C, U, M> hook : hooks) {
            record = hook.beforePersistCreate(record, form);
        }
        return record;
    }

    @Override
    public M afterPersistCreate(M model, R record, C form) {
        for (ICrudHook<ID, R, C, U, M> hook : hooks) {
            model = hook.afterPersistCreate(model, record, form);
        }
        return model;
    }

    @Override
    public R prepareUpdate(U form) {
        R record = null;
        for (ICrudHook<ID, R, C, U, M> hook : hooks) {
            record = hook.prepareUpdate(form);
        }
        return record;
    }

    @Override
    public R beforePersistUpdate(R record, U form) {
        for (ICrudHook<ID, R, C, U, M> hook : hooks) {
            record = hook.beforePersistUpdate(record, form);
        }
        return record;
    }

    @Override
    public M afterPersistUpdate(M model, R record, U form) {
        for (ICrudHook<ID, R, C, U, M> hook : hooks) {
            model = hook.afterPersistUpdate(model, record, form);
        }
        return model;
    }

    @Override
    public boolean beforePersistDelete(ID id) {
        boolean deleted = true;
        for (ICrudHook<ID, R, C, U, M> hook : hooks) {
            hook.beforePersistDelete(id);
        }
        return deleted;
    }

    @Override
    public void afterPersistDelete(ID id) {
        for (ICrudHook<ID, R, C, U, M> hook : hooks) {
            hook.afterPersistDelete(id);
        }
    }
    
}
