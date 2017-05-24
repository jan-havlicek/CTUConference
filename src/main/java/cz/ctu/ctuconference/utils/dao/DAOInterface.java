/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.ctu.ctuconference.utils.dao;

import java.util.List;

/**
 *
 * @author Nick nemame
 */
public interface DAOInterface<ENTITY extends AbstractEntity> {

    ENTITY getById(long id);
    void removeById(long id);
    List<ENTITY> getAll();
    List<ENTITY> getPage(int page, int resultsCount);
    int getCount();
    boolean exists(long id);
}
