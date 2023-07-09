package me.corruptionsniper.compass;

import java.util.HashMap;

public interface SerializeMap<K, V> {

    String getFileName();

    HashMap<K, V> getMap();

    void setMap(HashMap<K, V> dataMap);
}
