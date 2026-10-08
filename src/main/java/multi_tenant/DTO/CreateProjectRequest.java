package multi_tenant.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProjectRequest {

    private String name;

    private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	
}
