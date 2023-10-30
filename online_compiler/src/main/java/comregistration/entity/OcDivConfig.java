package comregistration.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class OcDivConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String language;
	private String title;
	private String iconPath;
	private String option;

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIconPath() {
		return iconPath;
	}

	@Override
	public String toString() {
		return "OcDivConfig [id=" + id + ", language=" + language + ", title=" + title + ", iconPath=" + iconPath
				+ ", option=" + option + "]";
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

}
