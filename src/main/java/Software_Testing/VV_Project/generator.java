package Software_Testing.VV_Project;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.Arrays;
import java.io.File;

import org.junit.runner.Request;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

import spoon.Launcher;
import spoon.SpoonModelBuilder;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtTypeAccess;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtExecutableReference;
import spoon.support.JavaOutputProcessor;

import static Software_Testing.VV_Project.Util.getGetters;
import static Software_Testing.VV_Project.Util.getKey;
import static Software_Testing.VV_Project.Util.invok;

/**
 * @author Fahim MERZOUK & Noureddine KADRI
 *
 */

public class generator {
	private Factory factory;

	
	public generator(Factory factory) {
		this.factory = factory;
	}

	
	/**
	 * create observer of getters in test cases
	 * @param testMethod the test method where create observers of getters
	 * @param localVariable the variable that we want to observe
	 */
	public void instrument (CtMethod testMethod , CtLocalVariable localVariable) {
		List<CtMethod> getters = getGetters(localVariable);
		getters.forEach(getter -> {
			CtInvocation invocationToGetter = 
					invok(getter , localVariable);
			CtInvocation invocationToObserve =
					createObserve(getter , invocationToGetter);
			testMethod.getBody().insertEnd(invocationToObserve);
		});
	}

	
	/**
	 * @param testMethod
	 * @param ctLocalVariables
	 */
	public void instrument(CtMethod<?> testMethod, List<CtLocalVariable> ctLocalVariables) {
		ctLocalVariables.forEach(ctLocalVariable -> this.instrument(testMethod, ctLocalVariable));
	}


	
	/**
	 * @param launcher 
	 * @param testMethod
	 * @param localVariables
	 */
	public void collect(Launcher launcher, CtMethod<?> testMethod, List<CtLocalVariable> localVariables) {
		final CtClass testClass = testMethod.getParent(CtClass.class);
		testClass.removeMethod(testMethod);
		final CtMethod<?> clone = testMethod.clone();
		instrument(clone, localVariables);
		testClass.addMethod(clone);
		run(launcher, testClass, clone);
		testClass.removeMethod(clone);
		testClass.addMethod(testMethod);
	}

	/**
	 * @param launcher
	 * @param testClass
	 * @param clone
	 */
	public void run(Launcher launcher, CtClass testClass, CtMethod<?> clone) {
		final String fullQualifiedName = testClass.getQualifiedName();
		final String testMethodName = clone.getSimpleName();
		try {
			final SpoonModelBuilder compiler = launcher.createCompiler();
			compiler.compile(SpoonModelBuilder.InputType.CTTYPES);
			runTest(fullQualifiedName, testMethodName, new String[]{"spooned-classes"});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}
	public CtInvocation createObserve(CtMethod getter, CtInvocation invocationToGetter) {
		CtTypeAccess accessToObserver =
				factory.createTypeAccess(factory.createCtTypeReference(Observer.class));
		System.out.println("observer : "+factory.Type().get(Observer.class).getMethodsByName("observe").get(0));
		CtExecutableReference refObserve = factory.Type().get(Observer.class)
				.getMethodsByName("observe").get(0).getReference();
		return factory.createInvocation(
				accessToObserver,
				refObserve,
				factory.createLiteral(getKey(getter)),
				invocationToGetter
				);
	}
	private static Function<String[], URL[]> arrayStringToArrayUrl = (arrayStr) ->
	Arrays.stream(arrayStr)
	.map(File::new)
	.map(File::toURI)
	.map(uri -> {
		try {
			return uri.toURL();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	})
	.toArray(URL[]::new);
	public static void runTest(String fullQualifiedName, String testCaseName, String[] classpath) throws MalformedURLException, ClassNotFoundException {
		ClassLoader classLoader = new URLClassLoader(
				arrayStringToArrayUrl.apply(classpath),
				ClassLoader.getSystemClassLoader()
				);
		Request request = Request.method(classLoader.loadClass(fullQualifiedName), testCaseName);
		Runner runner = request.getRunner();
		RunNotifier fNotifier = new RunNotifier();
		fNotifier.fireTestRunStarted(runner.getDescription());
		runner.run(fNotifier);
	}




	public static void main(String[] args) {
		Analyzer a = new Analyzer();
	//	/home/fmerzouk/eclipse-workspace/v_v/VV_Project
		Launcher launcher = new Launcher();
		if(args.length < 1 || args.length > 1) throw new IllegalArgumentException("Entrez le chemin vers le projet que vous voulez testez");
		launcher.addInputResource(args[0]+"/src/main/java/");
		launcher.addInputResource(args[0]+"/src/test/java/");
		launcher.setSourceOutputDirectory("./generated_test_sources/");
		launcher.buildModel();
		CtModel model = launcher.getModel();
		Factory factory = launcher.getFactory();
		generator g = new generator(factory);
		JavaOutputProcessor processor = new JavaOutputProcessor(launcher.createPrettyPrinter());
		processor.setFactory(factory);
		AssertionAdder assertionAdder = new AssertionAdder(factory);  

		for (CtType type : model.getAllTypes()) {
			CtClass theClass  = (CtClass) type.clone();
			String path = type.getPosition().getFile().getAbsolutePath();
			CompilationUnit cu = factory.CompilationUnit().getOrCreate(path);
			// Analyze
			Map<CtMethod, List<CtLocalVariable>> localVariablesPerTestMethod = a.analyze(type);
			localVariablesPerTestMethod.keySet().stream().forEach(key -> System.out.println("{"+ key.getParent(CtClass.class).getSimpleName() + "#" + key.getSimpleName() + "=["+ localVariablesPerTestMethod.get(key) +"]"));
			if (!localVariablesPerTestMethod.isEmpty()) {
				// Collect
				localVariablesPerTestMethod.keySet().forEach(ctMethod -> g.collect(launcher, ctMethod, localVariablesPerTestMethod.get(ctMethod)));
				// Generate
				localVariablesPerTestMethod.keySet().forEach(ctMethod -> {
					CtMethod methodSpooned = assertionAdder.addAssertion(ctMethod, localVariablesPerTestMethod.get(ctMethod));

					theClass.setSimpleName(type.getSimpleName()+"_generated");
					//theClass.removeMethod(ctMethod);
					theClass.addMethod(methodSpooned);
					processor.createJavaFile(theClass);
				}
						);
			}


			final SpoonModelBuilder compiler = launcher.createCompiler();
			compiler.compile(SpoonModelBuilder.InputType.CTTYPES);
		}
	}
}
