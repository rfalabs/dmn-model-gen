package labs.rfa.dmngen.dsl;

import java.util.List;
import java.util.UUID;

public class EnumDefinition extends BaseDmnElement {
	private String fieldType;
	private List<String> values;

	public EnumDefinition(String itemType, String id, String name, List<String> values) {
		super(id, name, false);
		this.fieldType = itemType;
		this.values = values;
	}
	
	@Override
	public String toString() { 
		StringBuilder str = new StringBuilder();

		str.append(String.format(
			"\t<dmn:itemDefinition id=\"_%s\" name=\"%s\" isCollection=\"%s\">",
			this.Id, this.Name, this.IsCollection)
		).append(System.lineSeparator());

		str.append(String.format(
			"\t\t<dmn:typeRef>%s</dmn:typeRef>", this.fieldType)
		).append(System.lineSeparator());

		str.append(String.format(
			"\t\t<dmn:allowedValues kie:constraintType=\"enumeration\" id=\"_%s\">", UUID.randomUUID().toString().toUpperCase()))
		.append(System.lineSeparator())
		.append(String.format(
			"\t\t\t<dmn:text>%s</dmn:text>", getValuesAsString()))
		.append(System.lineSeparator())
		.append("\t\t</dmn:allowedValues>")
		.append(System.lineSeparator());
		
		str.append("\t</dmn:itemDefinition>");
		
		return str.toString();
	}

	private String getValuesAsString() {
		if (this.values == null || this.values.isEmpty()) {
			return "";
		}
		
		StringBuilder result = new StringBuilder("\"" + this.values.get(0) + "\"");
		for (int i = 1; i < this.values.size(); i++) {
			result.append(", \"" + this.values.get(i) + "\"");
		}
		return result.toString();
	} 
}
