package me.corruptionsniper.compass.compassPoints;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;

public class CompassPointSet extends AbstractSet<CompassPoint> {

    private transient CompassPoint[] elementData;
    private int size;

    public CompassPointSet() {
        elementData = new CompassPoint[]{};
        size = 0;
        System.out.println();
    }

    @Override
    public int size() {
        return size;
    }

    private void increment() {elementData = Arrays.copyOf(elementData,size + 1);}

    public CompassPoint get(Object o) {
        CompassPoint c = elementData[binarySearch(o)];
        return c.equals(o) ? c : null;
    }

    //not tested
    @Override
    public boolean contains(Object o) {
        return elementData[binarySearch(o)].equals(o);
    }

    //not tested
    private int binarySearch(Object o) {
        if (size() == 0) return 0;
        int lb = 0; //Lower Bound for which index the compass point should be located at.
        int ub = size - 1; //Upperbound for which index the compass point should be located at.
        boolean isFound = false;
        int mid = 0;

        while (!isFound) {
            mid = (lb + ub) / 2;
            if (ub - lb == 0) {
                System.out.println(mid);
                isFound = true;
            }
            int compare = elementData[mid].compareTo(o);
            if (compare == 0) {
                isFound = true;
            }
            if (compare > 0) {
                lb = mid + 1;
            } else {
                ub = mid;
            }
            System.out.println("r");
        }
        System.out.println(mid);
        return mid;
    }

    //should work
    private void insert(int index, CompassPoint c) {
        if (size > index) {
            if (!elementData[index].equals(c)) {
                increment();
                System.arraycopy(elementData, index, elementData, index + 1, size - index);
            }
        } else increment();
        elementData[index] = c;
        size++;
    }

    //should work
    private boolean erase(int index) {
        if (size - 1 >= index) {
            System.arraycopy(elementData, index + 1, elementData, index, size - index);
            size--;
            return true;
        } else return false;
    }

    //should work
    @Override
    public boolean add(CompassPoint c) {
        insert(binarySearch(c),c);
        return true;
    }


    //should work
    @Override
    public boolean remove(Object o) {
        if (size == 0) {
            int index = binarySearch(o);
            if (elementData[index].equals(o)) return erase(index);
        }
        return false;
    }

    private class compassPointIterator implements Iterator<CompassPoint>{
        int index = -1;

        @Override
        public boolean hasNext() {
            return index != size - 1;
        }

        @Override
        public CompassPoint next() {
            return elementData[index + 1];
        }
    }

    @Override
    public Iterator<CompassPoint> iterator() {
        return new compassPointIterator();
    }
}
