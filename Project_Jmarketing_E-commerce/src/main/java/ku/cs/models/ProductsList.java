package ku.cs.models;

import java.util.ArrayList;
import java.util.Comparator;


public class ProductsList {
    private ArrayList<Product> products;

    public ProductsList() {
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public ArrayList<Product> getAllProduct(){return products;}


    public ArrayList<Product> getSearchProduct(String nameProduct) {
        ArrayList<Product> productArrayList = new ArrayList<>();
        for (Product product : products) {
            if (product.getNameProduct().equals(nameProduct)) {
                productArrayList.add(product);
            }
        }
        return productArrayList;
    }



    public ArrayList<Product> getUserProduct(String nameShop) {
        ArrayList<Product> productArrayList = new ArrayList<>();
        for (Product products : products) {
            if (products.getNameShop().equals(nameShop)) {
                productArrayList.add(products);
            }
        }
        return productArrayList;
    }


    public ArrayList<Product> getProductSameShopSameName(String productName,String shopname)
    {
        ArrayList<Product> productArrayList = new ArrayList<>();
        for(Product product : getUserProduct(shopname))
        {
            if(product.getNameProduct().equals(productName))
            {
                productArrayList.add(product);
            }
        }
        return productArrayList;
    }

    public ArrayList<String> getProductAttributeSameShopSameName(String productName, String shopName)
    {
        ArrayList<String> attributeArrayList = new ArrayList<>();
        for(Product product : getProductSameShopSameName(productName,shopName))
        {
            attributeArrayList.add(product.getAttribute());
        }
        return attributeArrayList;
    }



    public ArrayList<Product> getUserProductByProductType(String shopName,String productType)
    {
        ArrayList<Product> productArrayList = new ArrayList<>();
        for(Product product : getUserProduct(shopName))
        {
            if(product.getProductType().equals(productType)){productArrayList.add(product);}
        }
        return productArrayList;
    }

    public String getProductPathSameNameSameShop(String shopName, String productName)
    {
        for(Product product : getUserProduct(shopName))
        {
            if(product.getNameProduct().equals(productName))
            {
                return product.getPathImage();
            }
        }
        return "images/productimage/default-Product.jpg";
    }




    public String writeCSVPattern()
    {
        String csvPattern  = "";
        for(Product products : products)
        {
            csvPattern += products.toCSV() + "\n";
        }
        System.out.println(csvPattern);
        return csvPattern;
    }


    public Boolean isSameProductNameSameShop(ArrayList<Product> productArrayList,String productName,String shopName)
    {
        for(Product product : productArrayList){if(product.getNameProduct().equals(productName) && product.getNameShop().equals(shopName)){return true;} }
        return false;
    }

    public ArrayList<Product> getAllProductWIthDifferentNameInSameShop()
    {
        ArrayList<Product> productArrayList = new ArrayList<>();
        for(Product product : products)
        {
            if(!isSameProductNameSameShop(productArrayList,product.getNameProduct(),product.getNameShop()))
            {
                productArrayList.add(product);
            }
        }
        return  productArrayList;
    }


    public ArrayList<Product> getProductListByType(String type)
    {
        ArrayList<Product> productsArrayList = new ArrayList<Product>();
        for(Product products : products)
        {
            if(products.getProductType().equals(type)) { productsArrayList.add(products); }
        }
        return productsArrayList;
    }
    public int countProductWithSameName(ArrayList<Product> productArrayList, String productName) {
        int count = 0;
        for (Product product : productArrayList) {
            if (product.getNameProduct().equals(productName)) {
                count++;
            }
        }
        return count;
    }

    public ArrayList<Product> getUserProductWithDifferenceName(String shopName)
    {
        ArrayList<Product> productArrayList = new ArrayList<>();
        for(Product product : getUserProduct(shopName))
        {
            if(!isSameProductNameSameShop(productArrayList,product.getNameProduct(),shopName))
            {
                productArrayList.add(product);
            }
        }
        return productArrayList;
    }

    public Boolean isProductAlreadySell(String nameProduct, String nameShop,String attribute)
    {
        for(Product product : products) {
            if(product.getNameProduct().equals(nameProduct) && (product.getNameShop().equals(nameShop)) &&(product.getAttribute().equals(attribute))) {
                return true;
            }
        }
        return false;
    }

    public int getNewID(){
        return products.size()+1;
    }

    public void removeAttribute(String attribute)
    {
        for(Product products : products)
        {   if(products.getAttribute().equals(attribute))
        { products.setDefaultAttribute(); } }
    }


    public void sortLatestProduct(){
        Comparator<Product> comparator = (o1, o2) -> {
            if ( o1.getId() < o2.getId()) return 1;
            else if (o1.getId() > o2.getId()) return -1;
            return 0;
        };
        products.sort(comparator);
    }

    public Product getAProductByAttribute(String attribute,String productName,String productShopName)
    {
        for(Product product : products)
        {
            if(product.getNameProduct().equals(productName) && product.getAttribute().equals(attribute) && product.getNameShop().equals(productShopName))
            {
                return product;
            }
        }
        return null;
    }


    public void sortExpensivePrice(){
        Comparator<Product> comparator = (o1, o2) -> {
            if ( o1.getProductPrice() < o2.getProductPrice()) return 1;
            else if (o1.getProductPrice() > o2.getProductPrice()) return -1;
            return 0;
        };
        products.sort(comparator);
    }

    public void sortCheapPrice(){
        Comparator<Product> comparator = (o1, o2) -> {
            if ( o1.getProductPrice() > o2.getProductPrice()) return 1;
            else if (o1.getProductPrice() < o2.getProductPrice()) return -1;
            return 0;
        };
        products.sort(comparator);
    }

    public ArrayList<Product> getProductArrayListByType(String type)
    {
        ArrayList<Product> productsArrayList = new ArrayList<Product>();
        for(Product products : products)
        {
            if(products.getProductType().equals(type)) { productsArrayList.add(products); }
        }
        return productsArrayList;
    }

    public ArrayList<Product> scopeProductInStorePriceRange(String productType, double lowPriceRange, double highPriceRange) {
        ArrayList<Product> priceRange = new ArrayList<>();
        ArrayList<Product> productArrayList;
        if(productType.equals("ทั้งหมด"))
        { productArrayList = getAllProduct(); }
        else
        { productArrayList = getProductArrayListByType(productType); }
        for (Product products : productArrayList) {
            if (products.getProductPrice() > lowPriceRange && products.getProductPrice() <= highPriceRange) {
                priceRange.add(products);
            }
        }
        return priceRange;
    }

    public ArrayList<Product> scopePriceRangeForUserStore(String productType, String shopName, double lowPriceRange, double highPriceRange) {
        ArrayList<Product> priceRange = new ArrayList<>();
        ArrayList<Product> productArrayList;
        if(productType.equals("ทั้งหมด"))
        { productArrayList = getUserProduct(shopName); }
        else
        { productArrayList = getUserProductByProductType(shopName,productType); }
        for (Product products : productArrayList) {
            if (products.getProductPrice() > lowPriceRange && products.getProductPrice() <= highPriceRange) {
                priceRange.add(products);
            }
        }
        return priceRange;
    }



}