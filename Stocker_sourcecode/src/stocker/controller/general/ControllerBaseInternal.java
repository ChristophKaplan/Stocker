package stocker.controller.general;

import stocker.model.database.DatabaseModel;
import stocker.model.general.FrameProfile;
import stocker.model.general.FrameProfileBase;
import stocker.model.general.FrameProfileChart;
import stocker.model.general.ViewType;
import stocker.model.properties.PropertiesModel;
import stocker.view.general.DesktopViewBase;
import stocker.view.general.InternalViewBase;

/**
 * This class sets up the base for the gui controllers
 * {@code WatchlistController}, {@code SearchController},
 * {@code ChartController} and {@code PropertiesController}.
 * 
 * @author Christoph Kaplan
 */
public abstract class ControllerBaseInternal extends ControllerBase {
	//private InternalViewBase internalViewBase;
	private DatabaseModel databaseModel;

	/**
	 * {@code ControllerBaseInternal} constructor
	 * 
	 * @param viewType        desktop view type
	 * @param desktopViewBase the desktop view
	 * @param propertiesModel the properties model
	 * @param databaseModel   the database model
	 */
	protected ControllerBaseInternal(ViewType viewType, DesktopViewBase desktopViewBase,
			PropertiesModel propertiesModel, DatabaseModel databaseModel) {
		super(viewType, desktopViewBase, propertiesModel);
		this.databaseModel = databaseModel;
	}

	/**
	 * Creates a corresponding view instance based on a frame profile
	 * 
	 * @param frameProfileBase the frame profile
	 */
	protected abstract void createOwnView(FrameProfileBase frameProfileBase);

	/**
	 * Gets the corresponding {@code InternalViewBase} view object based on a frame
	 * profile
	 * 
	 * @param frameProfileBase the frame profile
	 * @return the {@code InternalViewBase} view object
	 * @throws Exception when no {@code InternalViewBase} view object is available
	 */
	protected abstract InternalViewBase getOwnView(FrameProfileBase frameProfileBase) throws Exception;

	/**
	 * Gets the database model.
	 * 
	 * @return the database model
	 */
	protected DatabaseModel getDatabaseModel() {
		return this.databaseModel;
	}

	/**
	 * invokes {@link #createOwnView} when view type corresponds
	 */
	@Override
	public void onAddFrame(FrameProfileBase frameProfileBase) {
		if (frameProfileBase.getViewType() != this.getViewType())
			return;
		createOwnView(frameProfileBase);

	}
	
	/**
	 * invokes {@link #createOwnView} when view type corresponds
	 */
	@Override
	public void onFrameUpdate(FrameProfileBase frameProfileBase) {
		if (frameProfileBase.getViewType() != this.getViewType())
			return;
		
		try {
			getOwnView(frameProfileBase).setFrameProfile((FrameProfile)frameProfileBase);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	/**
	 * When frame is removed
	 */
	@Override
	public void onRemoveFrame(FrameProfileBase frameProfileBase) {
		if (frameProfileBase.getViewType() != this.getViewType())
			return;
		

		
	}
	
	/**
	 * Brings frame to front, when view type corresponds
	 */
	@Override
	public void onMoveToFront(FrameProfileBase frameProfileBase) {
		if (frameProfileBase.getViewType() != this.getViewType())
			return;

		try {
			getOwnView(frameProfileBase).setShow();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	/**
	 * When indicator is added/removed
	 */
	@Override
	public void onUpdateIndicator(FrameProfileChart frameProfileChart) {
		if (frameProfileChart.getViewType() != this.getViewType())
			return;

	}

}