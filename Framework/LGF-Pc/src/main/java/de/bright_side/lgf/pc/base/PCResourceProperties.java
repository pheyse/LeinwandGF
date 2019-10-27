package de.bright_side.lgf.pc.base;

public class PCResourceProperties {
	private String name;
	private String path;
	
	public PCResourceProperties(String name, String path) {
		this.name = name;
		this.path = path;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPath() {
		return path;
	}
}
