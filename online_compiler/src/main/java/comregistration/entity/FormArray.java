package comregistration.entity;

import java.util.List;

public class FormArray {
	
	private List<String> techList;

	public List<String> getTechList() {
		return techList;
	}

	public void setTechList(List<String> techList) {
		this.techList = techList;
	}

	

	@Override
	public String toString() {
		return "FormArray [techList=" + techList + "]";
	}

	public FormArray(List<String> techList) {
		super();
		this.techList = techList;
	}

	public FormArray() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
