/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.DAO;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.query.Query;
import ru.jmirazors.jmiСalculator.entity.Loyalty;

/**
 *
 * @author User
 */
public class LoyaltyDAO extends DAO {
    
public List<Loyalty> list(boolean showDel){
    List<Loyalty> loyalty = new ArrayList<>();
    begin();
    Query query = getSession().createQuery("FROM Loyalty");
//        if (showDel)
//            
//        else
//            query.setParameter("del", "0");
        loyalty = query.getResultList();
        commit();
    return loyalty;
}
public void updateLoyalty(Loyalty l) {
    begin();
    getSession().saveOrUpdate(l);
    commit();
}
public Loyalty getLoyalty(Long id) {
    Loyalty l = null;
    begin();
    l = getSession().get(Loyalty.class, id);
    commit();
    return l;
}
public Loyalty getByCode(String code) {
    Loyalty l = null;
    begin();
    Query query = getSession().createQuery("FROM Loyalty WHERE code=:code");
        query.setParameter("code", code);
    l = (Loyalty)query.uniqueResult();
    commit();
    return l;
}
    
}
