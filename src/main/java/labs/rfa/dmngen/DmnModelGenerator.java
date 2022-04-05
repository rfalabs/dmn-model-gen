package labs.rfa.dmngen;

import java.util.Collection;
import java.util.UUID;

import labs.rfa.dmngen.dsl.BaseDmnElement;

public class DmnModelGenerator {
	public static String GenerateDmnFile(Collection<BaseDmnElement> modelDefs) {
		StringBuilder str = new StringBuilder();

		str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(System.lineSeparator())
		.append("<dmn:definitions xmlns:dmn=\"http://www.omg.org/spec/DMN/20180521/MODEL/\" xmlns=\"https://kiegroup.org/dmn/_05163D5E-06B1-4F7A-9906-831BCCC99C91\" xmlns:feel=\"http://www.omg.org/spec/DMN/20180521/FEEL/\" xmlns:kie=\"http://www.drools.org/kie/dmn/1.2\" xmlns:dmndi=\"http://www.omg.org/spec/DMN/20180521/DMNDI/\" xmlns:di=\"http://www.omg.org/spec/DMN/20180521/DI/\" xmlns:dc=\"http://www.omg.org/spec/DMN/20180521/DC/\" id=\"_8A3E6681-A16E-4EC3-A766-7A8A523AC0B5\" name=\"types\" typeLanguage=\"http://www.omg.org/spec/DMN/20180521/FEEL/\" namespace=\"https://kiegroup.org/dmn/_05163D5E-06B1-4F7A-9906-831BCCC99C91\">")
			.append(System.lineSeparator())
  		    .append("\t<dmn:extensionElements/>").append(System.lineSeparator());
		
		for (BaseDmnElement item : modelDefs) {
			str.append(item.toString())
			   .append(System.lineSeparator());
		}
		
		str.append("\t<dmndi:DMNDI>").append(System.lineSeparator())
		   .append(String.format("\t\t<dmndi:DMNDiagram id=\"_%s\" name=\"DRG\">", UUID.randomUUID().toString().toUpperCase()))
		   .append(System.lineSeparator())
		   .append("\t\t\t<di:extension>").append(System.lineSeparator())
		   .append("\t\t\t\t<kie:ComponentsWidthsExtension/>").append(System.lineSeparator())
		   .append("\t\t\t</di:extension>").append(System.lineSeparator())
		   .append("\t\t</dmndi:DMNDiagram>").append(System.lineSeparator())
		   .append("\t</dmndi:DMNDI>").append(System.lineSeparator())
		   .append("</dmn:definitions>");
		
		return str.toString();
	}
}
