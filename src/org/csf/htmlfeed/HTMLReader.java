package org.csf.htmlfeed;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class HTMLReader {

	/**
	 * 
	 * @param URL
	 * @param styleTag
	 * @param styleName
	 * @return
	 * @throws Exception
	 */

	public ArrayList<String> getTableRowsFromURL(String URL,
			ElementProperties elementProps, QueryColumn qryCols)
			throws Exception {
		ArrayList<String> retArrayList = new ArrayList<String>();
		try {
			
			System.out.println("Web query : " + URL);
			
			Connection con = Jsoup.connect(URL); 
			con.timeout(6000);
			Document doc = con.get();

			Elements tableRowElements = null;

			if (ElementProperties.TYPE_TABLE.equals(elementProps
					.getElementType())) {
				tableRowElements = doc.select(
						elementProps.getElementSearchString()).select(
						ElementProperties.TYPE_TR);
				for (int i = 0; i < tableRowElements.size(); i++) {
					Elements rowItems = tableRowElements.get(i).select(
							ElementProperties.TYPE_TD);
					for (int j = 0; j < rowItems.size(); j++) {

						if (qryCols.getColumnNumbers().contains(
								new Integer(j + 1))) {
							retArrayList.add(rowItems.get(j).text());
						}
					}
				}
			} else if (ElementProperties.TYPE_TR.equals(elementProps
					.getElementType())
					|| ElementProperties.TYPE_TD.equals(elementProps
							.getElementType())) {
				tableRowElements = doc.select(elementProps
						.getElementSearchString());

				for (int i = 0; i < tableRowElements.size(); i++) {
					if (qryCols.getColumnNumbers().contains(
							new Integer(
									(i % qryCols.getElementCountPerRow()) + 1))) {
						retArrayList.add(tableRowElements.get(i).text());
					}
				}
			}
		} catch (IOException e) {
			throw new Exception(e);
		}
		return retArrayList;
	}
}