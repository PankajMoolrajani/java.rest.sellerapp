package inventory;

public class BeanInventory {
	
	//table - inventory
	int id, id_category, id_stock, id_price;
	String sku, name, status_listing, image_dir;
	
	//table stock
	int available, incoming, outgoing;
	String aisle, rack, row, case_box;
	
	//table price
	int price_sell, price_mrp;

	public BeanInventory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BeanInventory(int id, int id_category, int id_stock, int id_price,
			String sku, String name, String status_listing,
			int available, int incoming, int outgoing, int price_sell,
			int price_mrp, String aisle, String rack, String row, String case_box, String image_dir) {
		super();
		this.id = id;
		this.id_category = id_category;
		this.id_stock = id_stock;
		this.id_price = id_price;
		this.sku = sku;
		this.name = name;
		this.status_listing = status_listing;
		this.available = available;
		this.incoming = incoming;
		this.outgoing = outgoing;
		this.price_sell = price_sell;
		this.price_mrp = price_mrp;
		this.aisle = aisle;
		this.rack = rack;
		this.row = row;
		this.case_box = case_box;
		this.image_dir = image_dir;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdCategory() {
		return id_category;
	}

	public void setIdCategory(int id_category) {
		this.id_category = id_category;
	}

	public int getIdStock() {
		return id_stock;
	}

	public void setIdStock(int id_stock) {
		this.id_stock = id_stock;
	}

	public int getIdPrice() {
		return id_price;
	}

	public void setIdPrice(int id_price) {
		this.id_price = id_price;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatusListing() {
		return status_listing;
	}

	public void setStatusListing(String status_listing) {
		this.status_listing = status_listing;
	}

	public int getAvailable() {
		return available;
	}

	public void setAvailable(int available) {
		this.available = available;
	}

	public int getIncoming() {
		return incoming;
	}

	public void setIncoming(int incoming) {
		this.incoming = incoming;
	}

	public int getOutgoing() {
		return outgoing;
	}

	public void setOutgoing(int outgoing) {
		this.outgoing = outgoing;
	}

	public int getPriceSell() {
		return price_sell;
	}

	public void setPriceSell(int price_sell) {
		this.price_sell = price_sell;
	}

	public int getPriceMrp() {
		return price_mrp;
	}

	public void setPriceMrp(int price_mrp) {
		this.price_mrp = price_mrp;
	}
	
	public void setAisle(String aisle){
		this.aisle = aisle;
	}
	
	public String getAisle(){
		return aisle;
	}
	
	public void setRow(String row){
		this.row = row;
	}
	
	public String getRow(){
		return row;
	}
	
	public void getRack(String rack){
		this.rack = rack;
	}
	
	public String getRack(){
		return rack;
	}
	
	public void setCaseBox(String case_box){
		this.case_box = case_box;
	}
	
	public String getCaseBox(){
		return case_box;
	}
	
	public void setImageDir(String image_dir){
		this.image_dir = image_dir;
	}
	
	public String getImageDir(){
		return image_dir;
	}
}
