package com.sachin.karthik.sachinist;

/**
 * Created by Karthik on 15-09-2016.
 */
public class Details {
    private String name;
    private int imageResourceId;
    public static int currPos = 0;

    public static final Details[] cenDetails = {
        new Details("119* v England",R.drawable.test1),
            new Details("148* v Australia",R.drawable.test2),
            new Details("114 v Australia",R.drawable.test3),
            new Details("111 v South Africa",R.drawable.test4),
            new Details("165 v England",R.drawable.test5)
    };

    public static final Details[] lastTest = {
        new Details("Gallery",R.drawable.last1),
            new Details("Speech-Text",R.drawable.last2),
            new Details("Speech-Video",R.drawable.last3),
            new Details("Highlights",R.drawable.last4),
            new Details("Final Walk",R.drawable.last5)
    };

    public static Details[] recent = {
           cenDetails[0],lastTest[0]
    };

    private Details(String name, int image){
        this.name = name;
        this.imageResourceId = image;
    }

    public String getName(){
        return name;
    }

    public int getImageResourceId(){
        return imageResourceId;
    }

    public static void setRecent(Details rec){
        recent[currPos%2] = rec;
        ++currPos;
    }
}
