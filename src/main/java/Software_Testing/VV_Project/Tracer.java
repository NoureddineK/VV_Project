package Software_Testing.VV_Project;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.factory.Factory;
import spoon.support.JavaOutputProcessor;

import java.io.File;
import java.util.List;
import java.util.Set;

public class Tracer {

	public static void main(String... args) {

		Launcher launcher = new Launcher();
		launcher.addInputResource("/home/noureddine/Projets/VV_Project/src/main/java/classToTest");
		
		launcher.buildModel();

		CtModel model = launcher.getModel();

		List<CtType> filteredTypes = model.getElements((CtType type) -> type.getSimpleName().equals("Person"));

		CtType functionsClass = filteredTypes.get(0);
		System.out.println("m : "+functionsClass.getMethods());

		Factory factory = launcher.getFactory();

		for (CtMethod method : (Set<CtMethod<?>>) functionsClass.getMethods()) {
			method.getBody().insertBegin(
					factory.createCodeSnippetStatement("System.out.println(\"" + method.getSimpleName() + "\")"));
		}
		launcher.setSourceOutputDirectory("./manipulation-target/target/generated-sources/");
		JavaOutputProcessor processor = new JavaOutputProcessor(launcher.createPrettyPrinter());
		processor.setFactory(factory);
		processor.createJavaFile(functionsClass);
	}
}