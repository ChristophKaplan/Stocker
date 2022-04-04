package stocker.view.stockertable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


/**
 * Table renderer used for watchlist view table
 * @author Christoph Kaplan
 *
 */
public class StockerDefaultRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 102768485724792066L;

	private Color rowColor = new Color(229, 236, 239);
	private Color downColor = new Color(254, 72, 101);
	private Color upColor = new Color(72, 254, 165);

	
	/**
	 * Renders the table cells according to the content that comes through the value parameter.
	 * Uses the price wrapper trick, {@code PriceWrapper} object is send through the value parameter
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		cell.setBackground(null);
		cell.setForeground(null);
		cell.setFont(cell.getFont().deriveFont(Font.PLAIN));
		
		if(row%2==0) {
			//straight numbers will be different color for visual orientation
			cell.setBackground(rowColor);
		}

		
		if(value instanceof String) {
			cell.setText((String)value);
		}
		
		if (value instanceof PriceWrapper) {
			PriceWrapper wrapper = (PriceWrapper) value;
			String valAsString = String.format("%,.3f", wrapper.getPrice());
			
			//if timer is running
			if (wrapper.getBlink()) {
				//on positive difference show green background
				if (wrapper.getDiff() > 0) {
					cell.setBackground(upColor);
				//on negative difference show red background
				} else if (wrapper.getDiff() < 0) {
					cell.setBackground(downColor);
				}
			}
			cell.setText(valAsString);
		}

		//format the change percentage
		if (value instanceof Double) {
			double newVal = (double) value;
			
			cell.setFont(cell.getFont().deriveFont(Font.BOLD));
			
			String valAsString = String.format("%,.3f", newVal);
			if (newVal >= 0) {
				cell.setForeground(upColor.darker().darker());
				cell.setText("+" + valAsString + " %");
			} else {
				cell.setForeground(downColor.darker());
				cell.setText(valAsString + " %");
			}
		}

		return cell;
	}
}



