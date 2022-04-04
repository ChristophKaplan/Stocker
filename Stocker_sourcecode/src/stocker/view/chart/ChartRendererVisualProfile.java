package stocker.view.chart;

import java.awt.BasicStroke;
import java.awt.Color;

/**
 * Visual profile for the {@code ChartPainter} class.
 * 
 * @author Christoph Kaplan
 *
 */
public class ChartRendererVisualProfile {
	
	//colors
	private Color alarmColor = new Color(254, 72, 101);
		
	private Color backgroundColor = Color.white;
	private Color backgroundGradientColor = new Color(235, 232, 223, 255);
	
	private Color lineChartColor = new Color(72, 209, 254);
	
	private Color downCandleColor = new Color(254, 72, 101);
	private Color upCandleColor = new Color(72, 254, 165);
	
	private Color candleOutlineColor = Color.black;
	private Color descriptionColor = new Color(19, 66, 89,255);
	
	private Color cellColor = Color.lightGray;
	private Color dotsColor = Color.gray;
	
	private Color crosshairColor =descriptionColor; // new Color(254, 209, 72);
	private Color hoverColor = descriptionColor;      //new Color(254, 209, 72)
		
	private Color hoverLableTextColor = Color.white;
	
	private Color errorColor = new Color(254, 72, 101);
	
	//STROKES
	private BasicStroke crosshairStroke = new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 7 }, 0);
	private BasicStroke hoverStroke = new BasicStroke(2f);
	
	private BasicStroke hoverLableStroke = new BasicStroke(2f, BasicStroke.JOIN_BEVEL, BasicStroke.JOIN_BEVEL);
	
	private BasicStroke candleStroke = new BasicStroke(1.0f);
	private BasicStroke borderlineStroke = new BasicStroke(3.0f);
	private BasicStroke cellStroke = new BasicStroke(0.8f);

	
	private BasicStroke bollingerStroke = new BasicStroke(1.25f);
	private BasicStroke smaStroke = new BasicStroke(1.25f);
		
	private int dotSize = 2;

	
	public ChartRendererVisualProfile(){
		
	}
	
	/**
	 * Clamp method
	 * @param val value
	 * @param min min
	 * @param max max
	 * @return result
	 */
	private float clamp(float val, float min, float max) {
	    return Math.max(min, Math.min(max, val));
	}
	
	/**
	 * Gets a "nice" shade of the given color
	 * @param color given color
	 * @return changed color
	 */
	public Color getShade(Color color) {
	    int red = color.getRed();
	    int green = color.getGreen();
	    int blue = color.getBlue();
	    float[] hsb = Color.RGBtoHSB(red, green, blue, null);
	    float hue = hsb[0]; 
	    float saturation = hsb[1];
	    float brightness = hsb[2];
		    
	    //hue = Math.abs(hue-1.0f); //hue flip
	    hue = clamp(hue+0.025f,0.0f,1.0f);
	    saturation = clamp(saturation+0.10f,0.0f,1.0f);
	    brightness = clamp(brightness-0.05f,0.0f,1.0f);
	    
		Color shade = Color.getHSBColor(hue, saturation, brightness);
		
		return shade;
	}
	
	/**
	 * Sets the alpha channel of a color
	 * @param color a color
	 * @param alpha the alpha value to set
	 * @return the color with the new alpha channel
	 */
	public Color changeAlpha(Color color, int alpha) {
		return  new Color(color.getRed(),color.getGreen(),color.getBlue(),alpha);
	}
	
	/**
	 * Get the alarm color.
	 * @return the alarm color
	 */
	public Color getAlarmColor() {
		return this.alarmColor;
	}

	/**
	 * Gets the background color.
	 * @return the background color
	 */
	public Color getBackgroundColor() {
		return this.backgroundColor;
	}
	
	/**
	 * Gets the background gradient color.
	 * @return the background gradient color
	 */
	public Color getBackgroundGradientColor() {
		return this.backgroundGradientColor;
	}
	
	/**
	 * Gets the line chart color.
	 * @return the line chart color
	 */
	public Color getLineChartColor() {
		return this.lineChartColor;
	}

	/**
	 * Gets the down-candle color.
	 * @return the down-candle color
	 */
	public Color getDownCandleColor() {
		return this.downCandleColor;
	}

	/**
	 * Gets the up-candle color.
	 * @return the up-candle color
	 */
	public Color getUpCandleColor() {
		return this.upCandleColor;
	}

	/**
	 * Gets the candle outline color.
	 * @return the candle outline color
	 */
	public Color getCandleOutlineColor() {
		return this.candleOutlineColor;
	}

	/**
	 * Gets the description color.
	 * @return the description color
	 */
	public Color getDescriptionColor() {
		return this.descriptionColor;
	}

	/**
	 * Gets the cell color.
	 * @return the cell color
	 */
	public Color getCellColor() {
		return this.cellColor;
	}

	/**
	 * Gets the dot color.
	 * @return the dot color
	 */
	public Color getDotsColor() {
		return this.dotsColor;
	}

	/**
	 * Gets the crosshair color.
	 * @return the crosshair color
	 */
	public Color getCrosshairColor() {
		return this.crosshairColor;
	}

	/**
	 * Gets the hover color.
	 * @return the hover color
	 */
	public Color getHoverColor() {
		return this.hoverColor;
	}

	/**
	 * Gets the hover lable color.
	 * @return the hover lable color
	 */
	public Color getHoverLableTextColor() {
		return this.hoverLableTextColor;
	}
	
	/**
	 * Gets the error color.
	 * @return the error color
	 */
	public Color getErrorColor() {
		return this.errorColor;
	}

	/**
	 * Gets the crosshair stroke.
	 * @return the crosshair stroke
	 */
	public BasicStroke getCrosshairStroke() {
		return this.crosshairStroke;
	}
	
	/**
	 * Gets the hover stroke.
	 * @return the hover stroke
	 */
	public BasicStroke getHoverStroke() {
		return this.hoverStroke;
	}

	/**
	 * Gets the hover lable stroke.
	 * @return the hover lable  stroke
	 */
	public BasicStroke getHoverLableStroke() {
		return this.hoverLableStroke;
	}
	
	
	/**
	 * Gets the candle stroke.
	 * @return the candle stroke
	 */
	public BasicStroke getCandleStroke() {
		return this.candleStroke;
	}

	/**
	 * Gets the borderline stroke.
	 * @return the borderline stroke
	 */
	public BasicStroke getBorderlineStroke() {
		return this.borderlineStroke;
	}

	
	/**
	 * Gets the cell stroke
	 * @return the cell stroke
	 */
	public BasicStroke getCellStroke() {
		return this.cellStroke;
	}


	/**
	 * Gets the Bollinger Band stroke.
	 * @return the Bollinger Band stroke
	 */
	public BasicStroke getBBStroke() {
		return this.bollingerStroke;
	}
	

	/**
	 * Gets the SMA stroke.
	 * @return the  SMA stroke
	 */
	public BasicStroke getSMAStroke() {
		return this.smaStroke;
	}

	/**
	 * Gets the dot size.
	 * @return the dot size
	 */
	public int getDotSize() {
		return this.dotSize;
	}
}
