package ku.cs.services;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class MapSetOfIntroduction {
    private Map<String,String > page;

    public MapSetOfIntroduction(){ page=new TreeMap<> (); }

    public void addPage(String page,String imagePath){
        this.page.put(page,imagePath);
    }

    public String getImagePath(String page){
        return this.page.get(page);
    }

    public ArrayList<String> getAllKey() {
        ArrayList<String> keyList = new ArrayList<>();
        for(String key : page.keySet()) {
            keyList.add(key);
        }
        return keyList;
    }
}
