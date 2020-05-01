/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.jmirazors.jmiСalculator.jmiframes.Documents;

/**
 *
 * @author User
 */
public interface DocumentImpl {

  void toggleState(); 
  void closeButtons(); 
  void initTableColumns();
  void initVisualComponents();
  void docInit();
  void repaintDocument();
  void saveDocument(long satus);
  void executeDocument();
   
}
