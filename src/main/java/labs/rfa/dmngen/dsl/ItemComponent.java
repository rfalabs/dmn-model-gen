package labs.rfa.dmngen.dsl;

public class ItemComponent extends BaseDmnElement {
	private String fieldType;
	private String classType;
	
	public ItemComponent(String itemType, String classType, String id, String name, boolean isCollection) {
		super(id, name, isCollection);
		this.fieldType = itemType;
		this.classType = classType;
	}

	public String getClassType() {
		return this.classType;
	}
	
	@Override
	public String toString() { 
		StringBuilder str = new StringBuilder();

		str.append(String.format(
			"\t\t<dmn:itemComponent id=\"_%s\" name=\"%s\" isCollection=\"%s\">",
			this.Id, this.Name, this.IsCollection)
		).append(System.lineSeparator());

		str.append(String.format(
			"\t\t\t<dmn:typeRef>%s</dmn:typeRef>", this.fieldType)
		).append(System.lineSeparator());

		str.append("\t\t</dmn:itemComponent>");

		return str.toString();
	}
}
