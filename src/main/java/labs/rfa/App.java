package labs.rfa;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import labs.rfa.dmngen.DmnItemBuilder;
import labs.rfa.dmngen.DmnModelGenerator;
import labs.rfa.dmngen.dsl.BaseDmnElement;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 *
 */
@Command(name = "dmn-model-gen", mixinStandardHelpOptions = true,
        version = "alpha 1.0", description = "Command line utility for turning Java classes into DMN Models.")
public class App implements Runnable
{
    @Option(names = { "-j", "--jar" }, description = "Jar file container Java classes to be transformed into DMN Model.", required = true) 
    private File jarFile;

    @Option(names = { "-p", "--package-prefix"}, 
        description = "Specify the prefix of targeted packages. If more than one root package prefix exists, it is recommended to split these into different DMN Model files.", 
        required = true)
    private String packagePrefix;

    @Option(names = { "-o", "--output" }, description = "Specify output file for the DMN Model - must have extension .dmn", required = true) 
    private String outputFile;

    @Option(names = { "-d", "--dry-run" }, description = "Specify dry run = true (-d=true, --dry-run=true) in order to see what files will be included in the model.")
    private Boolean dryRun = false;

    @Override
    public void run() {
        try (JarFile jar = new JarFile(this.jarFile)) {
			ClassLoader classLoader = new URLClassLoader(new URL[]{ new URL("file:/" + jarFile.getAbsolutePath()) });
			
            EnumerationHelper.enumerationAsStream(jar.entries())
				.filter(entry -> entry.getName().endsWith(".class"))
				.forEach(entry -> {
					try {
						loadItemDef(entry, classLoader, this.packagePrefix);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				});

            if (this.dryRun == true) {
                System.out.println("Classes to be included in DMN Model:");
                definitions.forEach((k, v) -> System.out.println(k));
                return;
            }

			String output = DmnModelGenerator.GenerateDmnFile(definitions.values());
			
            File outFile = new File(this.outputFile);

			try (FileOutputStream fos = new FileOutputStream(outFile);
				OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
				BufferedWriter writer = new BufferedWriter(osw))
            {
				writer.write(output);
			} catch (Exception e) {
                throw e;
            }
		} catch (Exception e) {
            System.out.println("Error writing model to output: " + e.toString());
        }
    }

    private Map<String, BaseDmnElement> definitions = new HashMap<String, BaseDmnElement>();
    public void loadItemDef(JarEntry entry, ClassLoader classLoader, String packagePrefix) throws ClassNotFoundException {
		String name = entry.getName();
		name = name.replace('/', '.');
		name = name.replace(".class", "");

		if (!name.startsWith(packagePrefix)) {
			return;
		}
		
		Class<?> myClass = classLoader.loadClass(name);

		Map<String, BaseDmnElement> itemDefs = DmnItemBuilder.build(myClass);

		itemDefs.forEach((k, v) -> {
			if (!definitions.containsKey(k)) {
				definitions.put(k, v);
			}
		});
    }

    public static void main( String[] args )
    {
        System.exit(new CommandLine(new App()).execute(args));
    }

}
