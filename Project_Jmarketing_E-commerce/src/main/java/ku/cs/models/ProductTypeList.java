package ku.cs.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class ProductTypeList
{
    private Map<String, ArrayList<String>> productType;

    public ProductTypeList()
    { productType = new TreeMap<>(); }

    public void addProductType(String productType, ArrayList<String> value)
    { this.productType.put(productType,value); }

    public ArrayList<String> getAllKey()
    {
        ArrayList<String> productTypeList = new ArrayList<>();
        for(String productType : productType.keySet())
        {
            productTypeList.add(productType);
        }
        return productTypeList;
    }

    public ArrayList<String> getProductTypeAttribute(String productType)
    {
        return this.productType.get(productType);
    }

    public Boolean isHasAttribute(String productType)
    {
        ArrayList<String> attributeList = getProductTypeAttribute(productType);
        if(attributeList.get(0).isBlank()){return false;}
        return true;
    }

    public ArrayList<String> getAttributeSpaceFilter(String productType)
    {
        ArrayList<String> arrayList = new ArrayList<>();

        for(String attribute : getProductTypeAttribute(productType))
        {
            if(attribute.isBlank()) { break; }
            arrayList.add(attribute);
        }
        return arrayList;
    }

    public void removeAttribute(String productType ,String attribute)
    {
        System.out.println("Remove attribute : "+attribute);
        ArrayList<String> productAttribute = this.productType.get(productType);
        ArrayList<String> removedAttribute = new ArrayList<>();
        for(String attributes : productAttribute)
        { if(!attributes.equals(attribute)) { removedAttribute.add(attributes); } }
        while(removedAttribute.size() != 4) { removedAttribute.add(" "); }

        productAttribute.clear();
        productAttribute.addAll(removedAttribute);
    }


    public void addNewAttribute(String productType,String attribute) {
        System.out.println("New attribute : " + attribute);
        ArrayList<String> productAttribute = this.productType.get(productType);
        if (productAttribute.get(0).equals(" ")) {
            productAttribute.clear();
            Collections.addAll(productAttribute, attribute, " ", " "," ");
            System.out.println("After : " + productAttribute);
        } else if (productAttribute.get(1).equals(" ")) {
            String s1 = productAttribute.get(0);
            productAttribute.clear();
            Collections.addAll(productAttribute, s1, attribute, " "," ");
            System.out.println("After : " + productAttribute);
        } else if (productAttribute.get(2).equals(" ")) {
            String s1 = productAttribute.get(0);
            String s2 = productAttribute.get(1);
            productAttribute.clear();
            Collections.addAll(productAttribute, s1, s2, attribute," ");
            System.out.println("After : " + productAttribute);
        } else if (productAttribute.get(3).equals(" ")) {
            String s1 = productAttribute.get(0);
            String s2 = productAttribute.get(1);
            String s3 = productAttribute.get(2);
            productAttribute.clear();
            Collections.addAll(productAttribute, s1, s2, s3, attribute);
            System.out.println("After : " + productAttribute);
        }
    }


    public Map<String,ArrayList<String>> getAllProductType()
    { return  productType; }



    @Override
    public String toString() {
        return "ProductType{" +
                "productType=" + productType +
                '}';
    }

    public String toCsvPattern()
    {
        String product ="";
        for(String productType : productType.keySet())
        {   ArrayList<String> attribute = this.productType.get(productType);
            product += productType + "," + attribute.get(0) +","+ attribute.get(1) +","+ attribute.get(2) + "," + attribute.get(3) + ",END" + "\n"; }

        System.out.println(product);
        return  product;
    }
}
