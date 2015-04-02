package com.hearthsim.gui;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class SortedListModel<E extends Comparable> extends AbstractListModel<E> {

    private static final long serialVersionUID = 1L;

    ArrayList<E> model; //can't use SortedSet... we need to support duplicates

    public SortedListModel() {
        model = new ArrayList<>();
    }

    @Override
    public int getSize() {
        return model.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public E getElementAt(int index) {
        return (E) model.toArray()[index];
    }

    public void addElement(E element) {
        if (model.add(element)) {
            Collections.sort(model);
            fireContentsChanged(this, 0, getSize());
        }
    }

    public void clear() {
        model.clear();
        fireContentsChanged(this, 0, getSize());
    }

    public boolean contains(E element) {
        return model.contains(element);
    }

    public Iterator<E> iterator() {
        return model.iterator();
    }

    public boolean removeElement(E element) {
        boolean removed = model.remove(element);
        if (removed) {
            fireContentsChanged(this, 0, getSize());
        }
        return removed;
    }

    public void remove(int index) {
        model.remove(index);
        fireContentsChanged(this, 0, getSize());
    }
}
