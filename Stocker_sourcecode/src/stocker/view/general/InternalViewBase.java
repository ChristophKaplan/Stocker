package stocker.view.general;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import stocker.controller.general.ControllerBase;
import stocker.model.database.DatabaseModel;
import stocker.model.database.DatabaseStockListener;
import stocker.model.general.FrameProfile;

/**
 * Basic layout class for the (internal) child frames.
 * Part of the MVC View.
 * @author Christoph Kaplan
 */
public abstract class InternalViewBase extends JInternalFrame implements ViewBase, DatabaseStockListener{
	private static final long serialVersionUID = 5455395972967835767L;

	//current frame profile in screenstate, will be updated before persistence.
	private FrameProfile frameProfile_SCREENSTATE;
		
	//gridbag layout (swing)
	private GridBagLayout gbl = new GridBagLayout();
	private GridBagConstraints gbc = new GridBagConstraints();
	
	//reference to the database model.
	private DatabaseModel databaseModel;

	
	/**
	 * Constructor 
	 * @param frameProfile the associated frame profile
	 * @param databaseModel the database model
	 */
	public InternalViewBase(FrameProfile frameProfile,DatabaseModel databaseModel) {
		super( frameProfile.getName(), true, true, true, true);
		this.frameProfile_SCREENSTATE = frameProfile;
		this.databaseModel = databaseModel;
		setFrameProfile(this.frameProfile_SCREENSTATE);
		setUp();

		this.databaseModel.addDatabaseStockObserver(this);
	}
	/**
	 * Removes this listener from the database
	 */
	private void removeObserver() {
		this.databaseModel.removeDatabaseStockObserver(this);
	}

	/**
	 * Gets the frame profile
	 */
	public FrameProfile getFrameProfile() {
		return frameProfile_SCREENSTATE;
	}
	/**
	 * Gets the id of the frame profile
	 */
	public int getID() {
		return this.frameProfile_SCREENSTATE.getID();
	}	
	
	/**
	 * Gets the database model
	 * @return the database model
	 */
	public DatabaseModel getDatabaseModel() {
		return this.databaseModel;
	}
	
	/**
	 * Sets the components and layout.
	 */
	public void setUp() {
		setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		setLayout(gbl);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		setInset(5);
	}
	/**
	 * Sets the gridbag layout inset.
	 * @param ins gridbag layout inset
	 */
	public void setInset(int ins) {
		gbc.insets = new Insets(ins, ins, ins, ins);
	}
	
	/**
	 * Sets the frame profile values to this frame.
	 */
	public void setFrameProfile(FrameProfile frameProfile) {
		//System.out.println("setFrameProfile()" + frameProfile.getName());
		
		int curSizeX =Math.max(frameProfile.getSizeX(), frameProfile.getMinSizeX());
		int curSizeY =Math.max(frameProfile.getSizeY(), frameProfile.getMinSizeY());
		
		Dimension minDimension = new Dimension(frameProfile.getMinSizeX(),frameProfile.getMinSizeY());		
		Dimension currentDimension = new Dimension(curSizeX,curSizeY);
		
		setLocation(frameProfile.getPosX(), frameProfile.getPosY());	
				
		setMinimumSize(minDimension);
		setPreferredSize(currentDimension);	
		setSize(currentDimension);

	}
	
	/**
	 * Sets the gridbag layout values for a component
	 * @param component the component
	 * @param x the gridx
	 * @param y the gridy
	 * @param w the gridwidth
	 * @param h the gridheight
	 * @param wx the weightx
	 * @param wy the weighty
	 * @param f the fill
	 */
	protected void setToGridBag(JComponent component, int x, int y, int w, int h,float wx, float wy,int f) {
		gbc.fill = f;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridheight = h;
		gbc.gridwidth = w;
		gbc.weightx = wx;
		gbc.weighty = wy;
		gbl.setConstraints(component, gbc);
		add(component);
	}
	
	/**
	 * Updates the frame profile by the current size and location
	 */
	public void updateFrameProfile(){
		frameProfile_SCREENSTATE.setSizeX(this.getSize().width);
		frameProfile_SCREENSTATE.setSizeY(this.getSize().height);
		frameProfile_SCREENSTATE.setPosX(this.getLocation().x);
		frameProfile_SCREENSTATE.setPosY(this.getLocation().y);
	}
	
	/**
	 * Adds the listener
	 */
	public void addListener(ControllerBase controller) {
		this.addInternalFrameListener(new InternalFrameAdapter(){
        	@Override
            public void internalFrameClosed(InternalFrameEvent e) {
        		removeObserver();
        		controller.onInternalFrameClosed(e);
            }
        });
		
	}

	/**
	 * Sets this frame visible.
	 */
	public void setShow() {
		//this.setVisible(true);
		show();
		moveToFront();
	}
	

}