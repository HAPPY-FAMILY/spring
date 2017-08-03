package com.transam.utils;

import java.util.List;

public class PageResult<T> {
	/**页码*/
	private Integer page;
	/**行数*/
	private List<T> rows;
	/**记录数*/
	private Long records;
	/**总页数*/
	private Integer total;

	
	public PageResult() {
		super();
	}

	public PageResult(Integer page, List<T> rows, Long records, Integer total) {
		super();
		this.page = page;
		this.rows = rows;
		this.records = records;
		this.total = total;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public Long getRecords() {
		return records;
	}

	public void setRecords(Long records) {
		this.records = records;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
	
	
}
