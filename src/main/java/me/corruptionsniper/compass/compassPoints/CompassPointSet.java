package me.corruptionsniper.compass.compassPoints;

import java.util.AbstractSet;
import java.util.Iterator;

public class CompassPointSet extends AbstractSet {

    transient CompassPoint[] elementData;
    private int size;

    public CompassPointSet() {
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    private void incrementSize() {
        size++;
    }

    //not tested
    @Override
    public boolean contains(Object o) {
        return elementData[binarySearch(o)].equals(o);
    }

    //not tested
    private int binarySearch(Object o) {
        if (!(o instanceof CompassPoint | o instanceof String)) throw new IllegalArgumentException();
        if (size() == 0) return 0;
        int lb = 0;
        int ub = size;

        while (true) {
            int middle = (lb + ub)/2;
            CompassPoint target = elementData[middle];
            if (elementData[middle].equals(o)) {
                return middle;
            }
            if (target.compareTo(elementData) > 0) {
                lb = middle + 1;
            } else {
                ub = middle - 1;
            }
            if (ub - lb == 0) {
                return middle;
            }
        }
    }

    //not tested
    private void insert(int index, CompassPoint o) {
        for (int i = size - 1; i > index; i--) {
            elementData[i] = elementData[i + 1];
        }
        elementData[index] = o;
        incrementSize();
    }

    //not tested
    private void erase(int index) {
        for (int i = index; i < size - 1; i++) {
            elementData[i] = elementData[i + 1];
        }
    }

    //not tested
    @Override
    public boolean add(Object o) {
        if (!(o instanceof CompassPoint)) {
            return false;
        }
        CompassPoint c = (CompassPoint) o;
        insert(binarySearch(c),c);
        return true;
    }


    //not tested
    @Override
    public boolean remove(Object o) {
        int index = binarySearch(o);
        if (elementData[binarySearch(o)].equals(o)) {
            erase(index);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Iterator iterator() {
        return null;
    }
}
