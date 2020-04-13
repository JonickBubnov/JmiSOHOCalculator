/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.DAO;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
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
//            query.setParameter("del", "1");
//        else
//            query.setParameter("del", "0");
        loyalty = query.getResultList();
    return loyalty;
}
    
}
