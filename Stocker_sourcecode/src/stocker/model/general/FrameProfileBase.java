package stocker.model.general;

import com.google.gson.annotations.SerializedName;


/**
 * This defines the base of the frame profiles
 * 
 * @author Christoph Kaplan
 *
 */
public abstract class FrameProfileBase implements Comparable<FrameProfile>{
	@SerializedName("id")
	private final int id;
	@SerializedName("name")
	private String name;
	@SerializedName("type")
	private ViewType type;
		
	/**
	 * {@code FrameProfileBase} constructor
	 * @param id the id
	 * @param name the name
	 * @param type the type
	 */
	public FrameProfileBase(int id, String name, ViewType type) {
		this.id = id;
		this.name = name;
		this.type = type;
	}


	/**
	 * Gets the id.
	 * @return the id
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * Gets the name.
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the view type.
	 * @return the view type
	 */
	public ViewType getViewType() {
		return this.type;
	}

	
	
	
	/**
	 * Overrides compareTo()
	 */
	@Override
	public int compareTo(FrameProfile o) {
		return Integer.compare(this.getID(), o.getID());
	}
}
