package mobi.garden.bottomnavigationtest.Model;

public class ModelPromo {

    String ProductID,PromoNameProduct, ProductNameUrl;
    int PriceProduct, ProductPriceAfterDC;
    public ModelPromo(String promoNameProduct, String productNameUrl, int priceProduct,int productPriceAfterDC) {
        this.PromoNameProduct = promoNameProduct;
        this.ProductNameUrl = productNameUrl;
        this.PriceProduct = priceProduct;
        this.ProductPriceAfterDC = productPriceAfterDC;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public ModelPromo(String productID, String promoNameProduct, String productNameUrl, int productPriceAfterDC) {
        ProductID = productID;
        PromoNameProduct = promoNameProduct;
        ProductNameUrl = productNameUrl;
        ProductPriceAfterDC = productPriceAfterDC;
    }

    public ModelPromo(String promoNameProduct, String productNameUrl, int priceProduct) {
        PromoNameProduct = promoNameProduct;
        ProductNameUrl = productNameUrl;
        PriceProduct = priceProduct;
    }

    public ModelPromo(String promoNameProduct, String productNameUrl) {
        this.PromoNameProduct = promoNameProduct;
        this.ProductNameUrl = productNameUrl;
    }

    public int getProductPriceAfterDC() {
        return ProductPriceAfterDC;
    }

    public void setProductPriceAfterDC(int productPriceAfterDC) {
        ProductPriceAfterDC = productPriceAfterDC;
    }

    public int getPriceProduct() {
        return PriceProduct;
    }
    public void setPriceProduct(int priceProduct) {
        PriceProduct = priceProduct;
    }

    public String getProductNameUrl() {
        return ProductNameUrl;
    }
    public void setProductNameUrl(String productNameUrl) {
        this.ProductNameUrl = productNameUrl;
    }

    public ModelPromo() {

    }


    public String getPromoNameProduct() {
        return PromoNameProduct;
    }

    public void setPromoNameProduct(String promoNameProduct) {
        this.PromoNameProduct = promoNameProduct;
    }
}
