package comregistration.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Technology {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String techType;
	private String technology;
	@ManyToOne(fetch = FetchType.LAZY) // Many technologies can belong to one server config
	@JoinColumn(name = "server_config_id") // This is the foreign key column name in the technology table
	private ServerConfig serverConfig;

	
	
	public ServerConfig getServerConfig() {
		return serverConfig;
	}

	public void setServerConfig(ServerConfig serverConfig) {
		this.serverConfig = serverConfig;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTechType() {
		return techType;
	}

	public void setTechType(String techType) {
		this.techType = techType;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	@Override
	public String toString() {
		return "Technology [id=" + id + ", techType=" + techType + ", technology=" + technology + "]";
	}
	
	 

}
