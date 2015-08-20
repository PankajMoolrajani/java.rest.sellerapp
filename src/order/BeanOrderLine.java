package order;

import java.util.ArrayList;
import java.util.Map;

public class BeanOrderLine {

	int id, id_order, id_inventory;
	float amount_taxable, amount_untaxable, amount_tax, quantity;
	String marektplace_suborderid;
	public BeanOrderLine() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BeanOrderLine(int id, int id_order, int id_inventory,
			float amount_taxable, float amount_untaxable, float amount_tax,
			float quantity, String marektplace_suborderid) {
		super();
		this.id = id;
		this.id_order = id_order;
		this.id_inventory = id_inventory;
		this.amount_taxable = amount_taxable;
		this.amount_untaxable = amount_untaxable;
		this.amount_tax = amount_tax;
		this.quantity = quantity;
		this.marektplace_suborderid = marektplace_suborderid;
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
	public int getIdInventory() {
		return id_inventory;
	}
	public void setIdInventory(int id_inventory) {
		this.id_inventory = id_inventory;
	}
	public float getAmountTaxable() {
		return amount_taxable;
	}
	public void setAmountTaxable(float amount_taxable) {
		this.amount_taxable = amount_taxable;
	}
	public float getAmountUntaxable() {
		return amount_untaxable;
	}
	public void setAmountUntaxable(float amount_untaxable) {
		this.amount_untaxable = amount_untaxable;
	}
	public float getAmountTax() {
		return amount_tax;
	}
	public void setAmountTax(float amount_tax) {
		this.amount_tax = amount_tax;
	}
	public float getQuantity(){
		return quantity;
	}
	public void setQuantity(float quantity){
		this.quantity = quantity;
	}
	public String getMarektplaceSuborderid() {
		return marektplace_suborderid;
	}
	public void setMarektplaceSuborderid(String marektplace_suborderid) {
		this.marektplace_suborderid = marektplace_suborderid;
	}
	
	
}
