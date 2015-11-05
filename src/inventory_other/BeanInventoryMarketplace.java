package inventory_other;

public class BeanInventoryMarketplace {
	
	//for inventory_marketplace table
	int id, id_inventory, id_inventory_marketplace, id_price, id_stock, sell_price_inventory_marketplace, stock_inventory_marketplace, status_listing;
	String url_inventory_marketplace,status_inventory_marketplace;
	
	//for marketplace table
	int id_marketplace;
	String name,type,url_marketplace;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_inventory() {
		return id_inventory;
	}

	public void setId_inventory(int idInventory) {
		id_inventory = idInventory;
	}

	public int getId_marketplace() {
		return id_marketplace;
	}

	public void setId_marketplace(int idMarketplace) {
		id_marketplace = idMarketplace;
	}

	public int getId_price() {
		return id_price;
	}

	public void setId_price(int idPrice) {
		id_price = idPrice;
	}

	public int getId_stock() {
		return id_stock;
	}

	public void setId_stock(int idStock) {
		id_stock = idStock;
	}

	public int getStatus_listing() {
		return status_listing;
	}

	public void setStatus_listing(int statusListing) {
		status_listing = statusListing;
	}

	public String getUrl_inventory_marketplace() {
		return url_inventory_marketplace;
	}

	public void setUrl_inventory_marketplace(String urlInventoryMarketplace) {
		url_inventory_marketplace = urlInventoryMarketplace;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl_marketplace() {
		return url_marketplace;
	}

	public void setUrl_marketplace(String urlMarketplace) {
		url_marketplace = urlMarketplace;
	}

	public int getId_inventory_marketplace() {
		return id_inventory_marketplace;
	}

	public void setId_inventory_marketplace(int idInventoryMarketplace) {
		id_inventory_marketplace = idInventoryMarketplace;
	}

	public int getSell_price_inventory_marketplace() {
		return sell_price_inventory_marketplace;
	}

	public void setSell_price_inventory_marketplace(
			int sellPriceInventoryMarketplace) {
		sell_price_inventory_marketplace = sellPriceInventoryMarketplace;
	}

	public int getStock_inventory_marketplace() {
		return stock_inventory_marketplace;
	}

	public void setStock_inventory_marketplace(int stockInventoryMarketplace) {
		stock_inventory_marketplace = stockInventoryMarketplace;
	}
	
	public void setStatus_inventory_marketplace(String statusInventoryMarketplace) {
		status_inventory_marketplace = statusInventoryMarketplace;
	}

	public String getStatus_inventory_marketplace() {
		return status_inventory_marketplace;
	}
}
