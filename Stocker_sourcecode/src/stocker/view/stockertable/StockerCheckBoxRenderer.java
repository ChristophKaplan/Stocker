package stocker.view.stockertable;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class StockerCheckBoxRenderer extends JCheckBox implements TableCellRenderer {
	private static final long serialVersionUID = 7893379363086485578L;
	
	private Color rowColor = new Color(229, 236, 239, 255);

	StockerCheckBoxRenderer() {
		setHorizontalAlignment(JLabel.CENTER);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		if (isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}
		if (row % 2 == 0) {
			// straight numbers will be different color for visual orientation
			setBackground(rowColor);
		}

		setSelected((value != null && ((Boolean) value).booleanValue()));
		return this;
	}
}