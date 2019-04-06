package common.model;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderLineList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<OrderLine> list;
	public OrderLineList(ArrayList<OrderLine> list) {
		this.setList(list);
	}
	public ArrayList<OrderLine> getList() {
		return list;
	}
	public void setList(ArrayList<OrderLine> list) {
		this.list = list;
	}
}
