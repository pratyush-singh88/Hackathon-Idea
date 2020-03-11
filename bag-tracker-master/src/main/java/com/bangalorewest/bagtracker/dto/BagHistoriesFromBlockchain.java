/**
 * 
 */
package com.bangalorewest.bagtracker.dto;

import java.util.List;

/**
 * @author sudhanshu.singh
 *
 */
public class BagHistoriesFromBlockchain {

	private List<BagHistoryFromBlockchain> data;

	/**
	 * @return the data
	 */
	public final List<BagHistoryFromBlockchain> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public final void setData(List<BagHistoryFromBlockchain> data) {
		this.data = data;
	}

}
