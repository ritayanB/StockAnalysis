package org.csf.service;

import java.io.IOException;
import java.sql.SQLException;

import org.csf.pojo.Ratios;

public interface IStockFundamental {
	public Ratios[] getFundamantalRatios(String secutiryCode) throws IOException, SQLException;
}
