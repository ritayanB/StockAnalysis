package org.csf.pojo;

public class Exchange extends TableObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int Key;
	String Name;
	String Prefix;
	String Suffix;

	public int getKey() {
		return Key;
	}

	public void setKey(int key) {
		Key = key;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getPrefix() {
		return Prefix;
	}

	public void setPrefix(String prefix) {
		Prefix = prefix;
	}

	public String getSuffix() {
		return Suffix;
	}

	public void setSuffix(String suffix) {
		Suffix = suffix;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String currency) {
		Currency = currency;
	}

	public String getCountryKey() {
		return CountryKey;
	}

	public void setCountryKey(String countryKey) {
		CountryKey = countryKey;
	}

	public String getCurrencySymbol() {
		return CurrencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		CurrencySymbol = currencySymbol;
	}

	String Currency;
	String CountryKey;
	String CurrencySymbol;
}
