package Software_Testing.VV_Project;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.factory.Factory;
import spoon.reflect.visitor.CtAbstractBiScanner;
import spoon.reflect.visitor.CtAbstractVisitor;
import spoon.reflect.visitor.CtBiScannerDefault;
import spoon.reflect.visitor.CtScanner;
import spoon.support.JavaOutputProcessor;

import java.io.File;

public class InstructionModifier {

	public static void main(String... args) {

		Launcher launcher = new Launcher();
		launcher.addInputResource("/home/noureddine/Projets/VV_Project/src/main/java/classToTest");
		launcher.buildModel();

		CtModel model = launcher.getModel();
		Factory factory = launcher.getFactory();

		CtType functionsClass = model.getElements((CtType type) -> type.getSimpleName().equals("Person")).get(0);

		CtMethod getNameMethod  = functionsClass.getMethod("getName");
		
		System.out.println("Methode : "+getNameMethod);
		getNameMethod .accept(new CtScanner() {
			@Override
			public <T> void visitCtBinaryOperator(CtBinaryOperator<T> operator) {
				super.visitCtBinaryOperator(operator);
				if (operator.getKind() == BinaryOperatorKind.MUL)
					operator.setKind(BinaryOperatorKind.PLUS);
			}
		});

		launcher.setSourceOutputDirectory("./manipulation-target/target/generated-sources/");
		JavaOutputProcessor processor = new JavaOutputProcessor(launcher.createPrettyPrinter());
		processor.setFactory(launcher.getFactory());
		processor.createJavaFile(functionsClass);
	}

}