package stocker.view.general;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DocumentFilter;


/**
 * text field class that extends {@code JTextField} and adds a document filter to check user input
 * @author Christoph Kaplan
 *
 */
public class StockerTextField extends JTextField {
	private static final long serialVersionUID = -134666609631475088L;

	/**
	 * Interface that implements the filter method to check if user input is valid.
	 * @author Christoph Kaplan
	 *
	 */
	public interface FilterMethod {
		boolean isValid(String text);
	}

	/**
	 * Inner class that extends a {@code DocumentFilter} to filter the user input
	 * @author Christoph Kaplan
	 *
	 */
	private class StockerDocumentFilter extends DocumentFilter {
		private int allowedCharacterAmount = 10;
		private FilterMethod filter;

		public StockerDocumentFilter(int charAmount, FilterMethod filter) {
			this.allowedCharacterAmount = charAmount;
			this.filter = filter;
		}

		@Override
		public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)	throws BadLocationException {
			if (outOfRange(fb.getDocument().getLength() + string.length())) {
				return;
			}

			String currentString = fb.getDocument().getText(0, fb.getDocument().getLength());

			if (filter.isValid(currentString + string)) {
				super.insertString(fb, offset, string, attr);
			} else {
				super.insertString(fb, offset, "", attr);
			}
		}

		@Override
		public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
			if (outOfRange(fb.getDocument().getLength() + text.length())) {
				return;
			}
			
			String currentString = fb.getDocument().getText(0, fb.getDocument().getLength());

			if (filter.isValid(currentString + text)) {
				super.replace (fb, offset, length, text, attrs);
			} else {

				super.replace(fb, offset, length, "", attrs);
			}
			

		}

		/**
		 * Checks if the amount of characters has exceeded
		 * @param amount current amount of characters
		 * @return true if exceeded
		 */
		private boolean outOfRange(int amount) {
			if (amount <= this.allowedCharacterAmount) {
				return false;
			}
			return true;
		}
	}

	/**
	 * Constructor of StockerTextField
	 * @param text the text/value to start with
	 * @param characterAmount the amount of characters allowed
	 * @param filterMethod the validation method
	 */
	public StockerTextField(String text,int characterAmount,FilterMethod filterMethod) {
		setFilter(characterAmount,filterMethod);
		setPopupMenuFor();
		this.setText(text);
	}

	private void setFilter(int characterAmount,FilterMethod filterMethod) {
		((AbstractDocument) this.getDocument()).setDocumentFilter(new StockerDocumentFilter(characterAmount, filterMethod));
	}

	private void setPopupMenuFor() {
		JPopupMenu popup = new JPopupMenu();
		popup.removeAll();

		JMenuItem menuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
		menuItem.setText("Copy");
		popup.add(menuItem);

		menuItem = new JMenuItem(new DefaultEditorKit.PasteAction());
		menuItem.setText("Paste");
		popup.add(menuItem);

		menuItem = new JMenuItem(new DefaultEditorKit.CutAction());
		menuItem.setText("Cut");
		popup.add(menuItem);

		setComponentPopupMenu(popup);
	}

}
