package order;

public class BeanCreateOrder {
	private int marketplaceId;
	private String marketplaceOrderId;
	private double amountTaxable,amountUntaxable,amountTax,amountTotal,amountShipping;
	public BeanCreateOrder() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BeanCreateOrder(int marketplaceId, String marketplaceOrderId,
			double amountTaxable, double amountUntaxable, double amountTax,
			double amountTotal, double amountShipping) {
		super();
		this.marketplaceId = marketplaceId;
		this.marketplaceOrderId = marketplaceOrderId;
		this.amountTaxable = amountTaxable;
		this.amountUntaxable = amountUntaxable;
		this.amountTax = amountTax;
		this.amountTotal = amountTotal;
		this.amountShipping = amountShipping;
	}
	public int getMarketplaceId() {
		return marketplaceId;
	}
	public void setMarketplaceId(int marketplaceId) {
		this.marketplaceId = marketplaceId;
	}
	public String getMarketplaceOrderId() {
		return marketplaceOrderId;
	}
	public void setMarketplaceOrderId(String marketplaceOrderId) {
		this.marketplaceOrderId = marketplaceOrderId;
	}
	public double getAmountTaxable() {
		return amountTaxable;
	}
	public void setAmountTaxable(double amountTaxable) {
		this.amountTaxable = amountTaxable;
	}
	public double getAmountUntaxable() {
		return amountUntaxable;
	}
	public void setAmountUntaxable(double amountUntaxable) {
		this.amountUntaxable = amountUntaxable;
	}
	public double getAmountTax() {
		return amountTax;
	}
	public void setAmountTax(double amountTax) {
		this.amountTax = amountTax;
	}
	public double getAmountTotal() {
		return amountTotal;
	}
	public void setAmountTotal(double amountTotal) {
		this.amountTotal = amountTotal;
	}
	public double getAmountShipping() {
		return amountShipping;
	}
	public void setAmountShipping(double amountShipping) {
		this.amountShipping = amountShipping;
	}
}
