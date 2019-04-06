package common.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Item> list;
	public ItemList(ArrayList<Item> list) {
		this.setList(list);
	}
	public ArrayList<Item> getList() {
		return list;
	}
	public void setList(ArrayList<Item> list) {
		this.list = list;
	}
}
