package order;

import java.util.ArrayList;
import java.util.Map;

public class BeanOrderLine {

	int id, id_order, id_inventory_marketplace, quantity;
	double amount_taxable, amount_untaxable, amount_tax, amount_shipping;
	String marketplace_suborderid;
	public BeanOrderLine() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BeanOrderLine(int id, int id_order, int id_inventory,
			float amount_taxable, float amount_untaxable, float amount_tax,
			int quantity, String marektplace_suborderid) {
		super();
		this.id = id;
		this.id_order = id_order;
		this.id_inventory_marketplace = id_inventory;
		this.amount_taxable = amount_taxable;
		this.amount_untaxable = amount_untaxable;
		this.amount_tax = amount_tax;
		this.quantity = quantity;
		this.marketplace_suborderid = marektplace_suborderid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdOrder() {
		return id_order;
	}
	public void setIdOrder(int id_order) {
		this.id_order = id_order;
	}
	public int getIdInventoryMarketplace() {
		return id_inventory_marketplace;
	}
	public void setIdInventoryMarketplace(int id_inventory_marketplace) {
		this.id_inventory_marketplace = id_inventory_marketplace;
	}
	public double getAmountTaxable() {
		return amount_taxable;
	}
	public void setAmountTaxable(float amount_taxable) {
		this.amount_taxable = amount_taxable;
	}
	public double getAmountUntaxable() {
		return amount_untaxable;
	}
	public void setAmountUntaxable(float amount_untaxable) {
		this.amount_untaxable = amount_untaxable;
	}
	public double getAmountTax() {
		return amount_tax;
	}
	public void setAmountTax(float amount_tax) {
		this.amount_tax = amount_tax;
	}
	public double getAmountShipping() {
		return amount_shipping;
	}
	public void setAmountShipping(float amount_shipping) {
		this.amount_shipping = amount_shipping;
	}
	public int getQuantity(){
		return quantity;
	}
	public void setQuantity(int quantity){
		this.quantity = quantity;
	}
	public String getMarektplaceSuborderid() {
		return marketplace_suborderid;
	}
	public void setMarektplaceSuborderid(String marektplace_suborderid) {
		this.marketplace_suborderid = marektplace_suborderid;
	}
	
	
}
