package labs.rfa.dmngen;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Map.entry;

import labs.rfa.dmngen.dsl.BaseDmnElement;
import labs.rfa.dmngen.dsl.BasicItemDefinition;
import labs.rfa.dmngen.dsl.EnumDefinition;
import labs.rfa.dmngen.dsl.ItemComponent;
import labs.rfa.dmngen.dsl.ItemDefinition;

public class DmnItemBuilder {
	
	private static String fBoolean = "boolean";
	private static String fNumber = "number";
	private static String fString = "string";

	private DmnItemBuilder() {}

	public static Map<String, BaseDmnElement> build(Class<?> tClass) {
		Map<String, BaseDmnElement> definitions = new HashMap<>();
		
		if (tClass.isEnum()) {
			EnumDefinition def = buildEnumDefinition(tClass);
			definitions.put(tClass.getTypeName(), def);
			return definitions;
		}

		if (basicTypeMappings.containsKey(tClass.getTypeName())) {
			BasicItemDefinition def = new BasicItemDefinition(
				UUID.randomUUID().toString().toUpperCase(), 
				tClass.getName(), 
				basicTypeMappings.get(tClass.getTypeName()),
				false
			);
			definitions.put(tClass.getTypeName(), def);
			return definitions;
		}

		/* TODO: Arrays/Lists at global scope */
		// if (tClass.isArray()) {
		// 
		// } else if (tClass.getTypeName() == "java.util.List") {
		//	
		// }
		
		// Otherwise map any other types to structure:
		Map<String, BaseDmnElement> structureDefinitions = buildStructureDefinition(tClass);
		structureDefinitions.forEach((k, v) -> {
			if (!definitions.containsKey(k)) {
				definitions.put(k, v);
			}
		});
		
		return definitions;
	}

	private static Map<String, BaseDmnElement> buildStructureDefinition(Class<?> tClass) {
		Map<String, BaseDmnElement> definitions = new HashMap<>();
		
		ItemDefinition structure = new ItemDefinition(UUID.randomUUID().toString().toUpperCase(), getShortName(tClass.getName()), false);
		List<ItemComponent> components = new ArrayList<>();

		Field[] fields = tClass.getDeclaredFields();
		for (Field field : fields) {
			ItemComponent component = transformField(field);
			components.add(component);
		}

		ItemComponent[] compList = new ItemComponent[components.size()];
		compList = components.toArray(compList);
		structure.setCompoments(compList);

		definitions.put(tClass.getName(), structure);

		return definitions;
	}

	// Returns a new Enum Definition with string values
	private static EnumDefinition buildEnumDefinition(Class<?> tClass) {
		List<String> values = new ArrayList<>();
		Field[] fields = tClass.getDeclaredFields();

		for (Field field : fields) {
			if (field.getType().getName().equals(tClass.getTypeName())) {
				values.add(field.getName());
			}
		}
		
		return new EnumDefinition(fString, UUID.randomUUID().toString().toUpperCase(), getShortName(tClass.getName()), values);
	}

	private static ItemComponent transformField(Field field) {
		String fieldName = field.getType().getName();
		String typeName = getShortName(fieldName);
		
		if (field.getType().isArray()) {
			fieldName = field.getType().getComponentType().getName();
			typeName = getShortName(fieldName);
			if (basicTypeMappings.containsKey(typeName)) {
				typeName = basicTypeMappings.get(typeName);
			}
			return new ItemComponent(typeName, fieldName, UUID.randomUUID().toString().toUpperCase(), field.getName(), true);

		} else if (field.getType().getTypeName().equals("java.util.List")) {
			ParameterizedType listType = (ParameterizedType) field.getGenericType();
			Class<?> listTypeClass = (Class<?>) listType.getActualTypeArguments()[0];

			fieldName = listTypeClass.getName();
			typeName = getShortName(fieldName);
			if (basicTypeMappings.containsKey(fieldName)) {
				typeName = basicTypeMappings.get(fieldName);
			}
			return new ItemComponent(typeName, fieldName, UUID.randomUUID().toString().toUpperCase(), field.getName(), true);
		}

		if (basicTypeMappings.containsKey(fieldName)) {
			typeName = basicTypeMappings.get(fieldName);
		}
		return new ItemComponent(typeName, fieldName, UUID.randomUUID().toString().toUpperCase(), field.getName(), false);
	}

	private static String getShortName(String name) {
		int index = name.lastIndexOf('.');
		return name.substring(index + 1, name.length());
	}

	// Here's a map of all of the basic Java data types and the mappings to DMN types:
	private static Map<String, String> basicTypeMappings = Map.ofEntries(
		// Boolean:	
		entry("java.lang.Boolean", fBoolean),
		entry("boolean", fBoolean),
		
		// Number:
		entry("java.lang.Short", fNumber),
		entry("java.lang.Number", fNumber),
		entry("java.lang.Long", fNumber),
		entry("java.lang.Integer", fNumber),
		entry("java.lang.Float", fNumber),
		entry("java.lang.Double", fNumber),
		entry("byte", fNumber),
		entry("short", fNumber),
		entry("long", fNumber),
		entry("int", fNumber),
		entry("float", fNumber),
		entry("double", fNumber),
		
		// String:		
		entry("java.lang.String", fString),
		entry("java.lang.Character", fString),
		entry("String", fString),
		entry("char", fString),
		entry("java.util.UUID", fString),

		// DateTime:
		entry("java.time.OffsetDateTime", "date and time")

		// DMN also has supports following types:
		//  - date 
		//  - days and time duration
		//  - time
		//  - years and months duration
	);
}
