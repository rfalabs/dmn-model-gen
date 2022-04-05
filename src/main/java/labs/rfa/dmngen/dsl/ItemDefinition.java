package labs.rfa.dmngen.dsl;

public class ItemDefinition extends BaseDmnElement {
	
	public ItemDefinition(String id, String name, boolean isCollection) {
		super(id, name, isCollection);
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

		for (int i = 0; i < components.length; i++) {
			str.append(components[i].toString())
			   .append(System.lineSeparator());
		}

		str.append("\t</dmn:itemDefinition>");

		return str.toString();
	}
}
