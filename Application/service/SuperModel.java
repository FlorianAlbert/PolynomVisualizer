package service;

import java.util.ArrayList;

public class SuperModel {
	private final ArrayList<ValueChangedListener> listener = new ArrayList<>();
	
	public void addValueChangedListener(ValueChangedListener listener) {
		this.listener.add(listener);
		ValueChanged();
	}

	public void ValueChanged() {
		for (ValueChangedListener listener: this.listener) {
			listener.onValueChanged();
		}
	}
}
