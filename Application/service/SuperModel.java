package service;

import java.util.ArrayList;

public class SuperModel {
	ArrayList<ValueChangedListener> listener = new ArrayList<>();
	
	public void addValueChangedListener(ValueChangedListener listener) {
		this.listener.add(listener);
		ValueChanged();
	}
	
	public void removeValueChangedListener(ValueChangedListener listener) {
		this.listener.remove(listener);
	}
	
	public void ValueChanged() {
		for (ValueChangedListener listener: this.listener) {
			listener.onValueChanged();
		}
	}
}
