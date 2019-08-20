package szklimek.diettracker.data.local;

/**
 * Contract of using products data from products
 */

public interface ProductsDataSource {

    void insertProductsDataToDatabase();

    void restoreProductsData();

}
