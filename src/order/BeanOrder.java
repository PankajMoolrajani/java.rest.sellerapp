package order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import order.BeanOrderLine;
public class BeanOrder {
	
	//table order
	int id, id_user, id_marketplace, id_inventory_marketplace, amount_total_taxable, amount_total_untaxable, amount_total_tax, amount_total_shipping, amount_total;
	String marketplace_orderid;
	//table order_line
	
	ArrayList<BeanOrderLine> list;

	public BeanOrder() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BeanOrder(int id, int id_user, int id_marketplace, int id_inventory_marketplace,
			int amount_total_taxable, int amount_total_untaxable,
			int amount_total_tax, int amount_total_shipping, int amount_total,
			String marketplace_orderid, ArrayList<BeanOrderLine> bean_order_line) {
		super();
		this.id = id;
		this.id_user = id_user;
		this.id_marketplace = id_marketplace;
		this.id_inventory_marketplace = id_inventory_marketplace;
		this.amount_total_taxable = amount_total_taxable;
		this.amount_total_untaxable = amount_total_untaxable;
		this.amount_total_tax = amount_total_tax;
		this.amount_total_shipping = amount_total_shipping;
		this.amount_total = amount_total;
		this.marketplace_orderid = marketplace_orderid;
		this.list = bean_order_line;
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
	
	public int getIdInventoryMarketplace() {
		return id_inventory_marketplace;
	}

	public void setIdInventoryMarketplace(int id_inventory_marketplace) {
		this.id_inventory_marketplace = id_inventory_marketplace;
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

	public int getAmountTotalShipping() {
		return amount_total_shipping;
	}

	public void setAmountTotalShipping(int amount_total_shipping) {
		this.amount_total_shipping = amount_total_shipping;
	}

	public int getAmountTotal() {
		return amount_total;
	}

	public void setAmountTotal(int amount_total) {
		this.amount_total = amount_total;
	}

	public String getMarketplaceOrderid() {
		return marketplace_orderid;
	}

	public void setMarketplaceOrderid(String marketplace_orderid) {
		this.marketplace_orderid = marketplace_orderid;
	}

	public ArrayList<BeanOrderLine> getBeanOrderLine() {
		return list;
	}

	public void setBeanOrderLine(ArrayList<BeanOrderLine> bean_order_line) {
		this.list = bean_order_line;
	}
	
}
