package org.csf.htmlfeed;

public class ElementProperties {

	private String elementStyle;
	private String elementType;

	private String elementSearchString;

	public String getElementStyle() {
		return elementStyle;
	}

	public void setElementStyle(String elementStyle) {
		this.elementStyle = elementStyle;
	}

	public String getElementType() {
		return elementType;
	}

	public void setElementType(String elementType) {
		this.elementType = elementType;
	}

	public String getElementSearchString() {
		return elementSearchString;
	}

	public void setElementSymbol(String elementSymbol) {
		this.elementSearchString = elementSymbol;
	}

	public static final String STYLE_CLASS = "class";
	public static final String STYLE_ID = "id";

	public static final String TYPE_TABLE = "table";
	public static final String TYPE_TH = "th";
	public static final String TYPE_div = "div";
	public static final String TYPE_TR = "tr";
	public static final String TYPE_TD = "td";

	public static final String DOT = ".";
	public static final String HASH = "#";

	/**
	 * 
	 * @param elementStyle
	 *            Element style is the style attributes, e.g. class, id
	 * @param elementType
	 *            Element type is the type, e.g. table, tr, td, div
	 */

	public ElementProperties(String elementStyle, String elementType,
			String elementUniqueName) {
		this.elementStyle = elementStyle;
		this.elementType = elementType;
		switch (this.elementStyle) {
		case STYLE_CLASS:
			this.elementSearchString = DOT + elementUniqueName;
			break;
		case STYLE_ID:
			this.elementSearchString = HASH + elementUniqueName;
			break;
		default:
			this.elementSearchString = "" + elementUniqueName;
		}
	}

}
