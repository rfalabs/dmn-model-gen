package labs.rfa.dmngen.dsl;

public class BaseDmnElement {
	protected String Id;
	protected String Name;
	protected boolean IsCollection;
	public BaseDmnElement(String id, String name, boolean isCollection) {
		this.Id = id;
		this.Name = name;
		this.IsCollection = isCollection;
	}
}
