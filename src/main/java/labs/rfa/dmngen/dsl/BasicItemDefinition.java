package labs.rfa.dmngen.dsl;

public class BasicItemDefinition extends BaseDmnElement {
	private String type;

	public BasicItemDefinition(String id, String name, String type, boolean isCollection) {
		super(id, name, isCollection);
		this.type = type;
	}

	private ItemComponent[] components;
	public ItemComponent[] getComponents() {
		return components;
	}
	public void setCompoments(ItemComponent[] components) {
		this.components = components;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		str.append(String.format(
			"\t<dmn:itemDefinition id=\"_%s\" name=\"%s\" isCollection=\"%s\">",
			this.Id, this.Name, this.IsCollection)
		).append(System.lineSeparator());
		
		str.append(String.format("<dmn:typeRef>%s</dmn:typeRef>", this.type))
		   .append(System.lineSeparator());
		
		str.append("\t</dmn:itemDefinition>");

		return str.toString();
	}
}
