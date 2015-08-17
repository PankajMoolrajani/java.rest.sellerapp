package order;

import java.util.ArrayList;
import java.util.Map;

public class BeanOrder {
	
	//table order
	int id, id_user, id_marketplace, amount_total_taxable, amount_total_untaxable, amount_total_tax, amount_total_shipping, amount_total;
	String marketplace_orderid;
	//table order_line
	ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	int id_order_line, id_order, id_inventory, id_inventory_marketplace, marektplace_suborder_id, amount_taxable, amount_untaxable, amount_tax, amount_shipping;

	public BeanOrder() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BeanOrder(int id, int id_user, int id_marketplace,
			String marketplace_orderid, int amount_total_taxable,
			int amount_total_untaxable, int amount_total_tax, 
			int amount_total_shipping, int amount_total, ArrayList<Map<String, Object>> list) {
		super();
		System.out.println("inside constructor");
		this.id = id;
		this.id_user = id_user;
		this.id_marketplace = id_marketplace;
		this.marketplace_orderid = marketplace_orderid;
		this.amount_total_taxable = amount_total_taxable;
		this.amount_total_untaxable = amount_total_untaxable;
		this.amount_total_tax = amount_total_tax;
		this.amount_total_shipping = amount_total_shipping;
		this.amount_total = amount_total;
		this.list = list;
		
		System.out.println(list);
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdUser() {
		return id_user;
	}

	public void setIdUser(int id_user) {
		this.id_user = id_user;
	}

	public int getIdMarketplace() {
		return id_marketplace;
	}

	public void setIdMarketplace(int id_marketplace) {
		this.id_marketplace = id_marketplace;
	}

	public String getMarketplaceOrderid() {
		return marketplace_orderid;
	}

	public void setMarketplaceOrderid(String marketplace_orderid) {
		this.marketplace_orderid = marketplace_orderid;
	}

	public int getAmountTotalTaxable() {
		return amount_total_taxable;
	}

	public void setAmountTotalTaxable(int amount_total_taxable) {
		this.amount_total_taxable = amount_total_taxable;
	}

	public int getAmountTotalUntaxable() {
		return amount_total_untaxable;
	}

	public void setAmountTotalUntaxable(int amount_total_untaxable) {
		this.amount_total_untaxable = amount_total_untaxable;
	}

	public int getAmountTotalTax() {
		return amount_total_tax;
	}

	public void setAmountTotalTax(int amount_total_tax) {
		this.amount_total_tax = amount_total_tax;
	}

	public int getAmountTotalShipping(){
		return amount_total_shipping;
	}
	
	public void setAmountTotalShipping(int amount_total_shipping){
		this.amount_total_shipping = amount_total_shipping;
	}
	
	public int getAmountTotal() {
		return amount_total;
	}

	public void setAmountTotal(int amount_total) {
		this.amount_total = amount_total;
	}

}
