package stocker.view.stockertable;


/**
 * The PriceWrapper class contains 3 pieces of information, a double for the
 * price, a double for the price difference (compared to the previous price),
 * and a boolean flag for the status of the timer. A PriceWrapper instance is
 * created in the overwritten method getValueAt () in the {@code StockerTableModel} and transferred to the
 * table renderer, {@code StockerDefaultRenderer} (a subtype of
 * DefaultTableCellRenderer). The received cell objects of the type Object are
 * then converted into the corresponding subtype and the information required
 * for rendering is obtained.
 * 
 * @author Christoph Kaplan
 *
 */
public class PriceWrapper implements Comparable<PriceWrapper> {
	private double price;
	private double diff;
	private boolean blink;

	/**
	 * constructor.
	 * 
	 * @param p the price
	 * @param d the prcie difference
	 * @param b the blink flag, if the timer is running
	 */
	public PriceWrapper(double p, double d, boolean b) {
		this.price = p;
		this.diff = d;
		this.blink = b;
	}

	/**
	 * Overrides compareTo().
	 */
	@Override
	public int compareTo(PriceWrapper o) {
		return Double.compare(price, o.price);
	}

	/**
	 * Gets the price.
	 * 
	 * @return the price
	 */
	public double getPrice() {
		return this.price;
	}

	/**
	 * Gets the price difference.
	 * 
	 * @return the price difference
	 */
	public double getDiff() {
		return this.diff;
	}

	/**
	 * Gets the blink flag.
	 * 
	 * @return the blink flag
	 */
	public boolean getBlink() {
		return this.blink;
	}


}