package com.justwzhang.tastytome.shared.interfaces;

import java.util.List;
import java.util.Optional;

import org.jooq.Condition;

/**
 * 
 * CrudRepository
 * @param <R> The Record 
 * @param <M> The Model Class
 * @param <ID> The id type for the class, most cases are Long
 */
public interface CrudRepository<R, M, ID> {
    public List<M> list();
    public List<M> list(Condition condition);

    public Optional<M> get(ID id);
    public Optional<M> get(ID id, Condition condition);

    public M create(R record);
    public List<M> create(List<R> createForms);

    public M update(R record);
    public List<M> update(List<R> updateForms);

    public boolean delete(ID id);
}