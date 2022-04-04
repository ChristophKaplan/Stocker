package stocker.view.chart;


import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import stocker.view.general.DesktopViewBase;

/**
 * Defines the alarm triggered dialog.
 * Shows, an alarm triggered, to the user.
 * @author Christoph Kaplan
 *
 */
public class DialogAlarmTriggered extends DialogViewBase implements ActionListener{
	private static final long serialVersionUID = 2668602201963501755L;
		
	private JLabel msgLabel;
	private JButton okButton;
	private String msg;
	
	/**
	 * {@code DialogAlarmTriggered} constructor
	 * @param msg the message to the user
	 * @param desktopViewBase the owner view
	 */
	public DialogAlarmTriggered(String msg,DesktopViewBase desktopViewBase){
		super(msg,200,150,desktopViewBase);
		this.msg = msg;
		setUp();
	}
	
	/**
	 * Sets the components.
	 */
	private void setUp() {
		msgLabel = new JLabel(msg);
		okButton = new JButton ("OK");
		okButton.addActionListener(this);
		
		setToGridBag(msgLabel , 0, 0, 1, 1,0.5f,0.0f,GridBagConstraints.BOTH);
		setToGridBag(okButton , 0, 1, 1, 1,0.5f,1.0f,GridBagConstraints.HORIZONTAL);
		
		this.setShow();
	}
	
	/**
	 * Dispose this dialog when action occurred.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == okButton) {
			this.dispose();
		}
	}
	
	
}
