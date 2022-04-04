package stocker.model.general;



import com.google.gson.annotations.SerializedName;

/**
 * The frame profiles are used to contain data about all the different gui frames i.e. size, position.
 * They are also used to determine if a frame is present or not.
 * @author Christoph Kaplan
 */
public class FrameProfile extends FrameProfileBase {
	
	@SerializedName("sizeX")
	private int sizeX;
	@SerializedName("sizeY")
	private int sizeY;
	@SerializedName("posX")
	private int posX;
	@SerializedName("posY")
	private int posY;

	@SerializedName("minSizeX")
	private int minSizeX;
	@SerializedName("minSizeY")
	private int minSizeY;
		
	
	
	
	/**
	 * {@code FrameProfile} constructor
	 * @param id the id
	 * @param name the name
	 * @param type the type
	 * @param posX the position X
	 * @param posY the position Y
	 * @param sizeX the size X
	 * @param sizeY the size Y
	 * @param minSizeX the minimum size X
	 * @param minSizeY the minimum size Y
	 */
	public FrameProfile(int id, String name, ViewType type, int posX, int posY, int sizeX, int sizeY, int minSizeX,int minSizeY) {
		super(id, name, type);
		
		this.posX = posX;
		this.posY = posY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.minSizeX = minSizeX;
		this.minSizeY = minSizeY;
	}
	
	
	

	/**
	 * Gets the size X
	 * @return the size X
	 */
	public int getSizeX() {
		return this.sizeX;
	}

	/**
	 * Gets the size Y
	 * @return the size Y
	 */
	public int getSizeY() {
		return this.sizeY;
	}

	/**
	 * Gets the minimum size X
	 * @return the minimum size X
	 */
	public int getMinSizeX() {
		return this.minSizeX;
	}
	/**
	 * Gets the minimum size Y
	 * @return the minimum size Y
	 */
	public int getMinSizeY() {
		return this.minSizeY;
	}

	/**
	 * Gets the position X
	 * @return the position X
	 */	
	public int getPosX() {
		return this.posX;
	}

	/**
	 * Gets the position Y
	 * @return the position Y
	 */	
	public int getPosY() {
		return this.posY;
	}

	/**
	 * Sets the size X
	 * @param sizeX the size X
	 */
	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}

	/**
	 * Sets the size Y
	 * @param sizeY the size Y
	 */
	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}
	
	
	/**
	 * Sets the minimum size X
	 * @param minSizeX the minimum size X
	 */
	public void setMinSizeX(int minSizeX) {
		this.minSizeX = minSizeX;
	}
	/**
	 * Sets the minimum size Y
	 * @param minSizeY the minimum size Y
	 */
	public void setMinSizeY(int minSizeY) {
		this.minSizeY = minSizeY;
	}
	
	
	/**
	 * Sets the position X
	 * @param posX the position X
	 */
	public void setPosX(int posX) {
		this.posX = posX;
	}
	
	/**
	 * Sets the position Y
	 * @param posY the position Y
	 */
	public void setPosY(int posY) {
		this.posY = posY;
	}
	
	

	
	
	
}
