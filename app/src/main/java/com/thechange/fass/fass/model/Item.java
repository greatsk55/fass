package com.thechange.fass.fass.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user on 2017. 4. 20..
 */

public class Item extends RealmObject{
    @PrimaryKey
    public String url;
    public String category;
    public String urlImage;
    public String urlTitle;
    public String date;

}

