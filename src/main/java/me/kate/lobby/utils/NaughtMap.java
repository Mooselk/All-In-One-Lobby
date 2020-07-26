package me.kate.lobby.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NaughtMap<KeyType, ValueType> {

	private HashMap<KeyType, ValueType> wrappedHashMap = new HashMap<>();

	public boolean containsKey(KeyType key) {
		return this.wrappedHashMap.containsKey(key);
	}

	public boolean containsValue(ValueType key) {
		return this.wrappedHashMap.containsValue(key);
	}

	public Set<Map.Entry<KeyType, ValueType>> entrySet() {
		return this.wrappedHashMap.entrySet();
	}

	public Set<KeyType> keySet() {
		return this.wrappedHashMap.keySet();
	}

	public Collection<ValueType> values() {
		return this.wrappedHashMap.values();
	}

	public ValueType get(KeyType key) {
		return this.wrappedHashMap.get(key);
	}

	public ValueType remove(KeyType key) {
		return this.wrappedHashMap.remove(key);
	}

	public ValueType put(KeyType key, ValueType value) {
		return this.wrappedHashMap.put(key, value);
	}

	public void clear() {
		this.wrappedHashMap.clear();
	}

	public int size() {
		return this.wrappedHashMap.size();
	}

	public boolean isEmpty() {
		return this.wrappedHashMap.isEmpty();
	}
}
