package Software_Testing.VV_Project;

import spoon.Launcher;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.factory.Factory;
import spoon.reflect.reference.CtArrayTypeReference;
import spoon.support.JavaOutputProcessor;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ClassGenerator {

    public static void main(String[] args) throws Exception {

        Launcher launcher = new Launcher();


        Factory factory = launcher.getFactory();

        CtClass theClass = factory.createClass();

        theClass.setSimpleName("HelloWorld");

        CtMethod mainMethod = factory.createMethod();

        Set modifiers = new HashSet();
        modifiers.add(ModifierKind.STATIC);
        modifiers.add(ModifierKind.PUBLIC);

        mainMethod.setSimpleName("main");
        mainMethod.setModifiers(modifiers);

        CtArrayTypeReference reference = factory.createArrayTypeReference();
        reference.setComponentType(factory.createCtTypeReference(String.class));


        factory.createParameter(
                mainMethod,
               reference,
                "args"
        );

        mainMethod.setBody(factory.createCodeSnippetStatement("System.out.println(\"Hello World\")"));

        theClass.addMethod(mainMethod);


		launcher.setSourceOutputDirectory("./manipulation-target/target/generated-sources/");
        JavaOutputProcessor processor = new JavaOutputProcessor(launcher.createPrettyPrinter());
        processor.setFactory(factory);
        processor.createJavaFile(theClass);

    }
}
