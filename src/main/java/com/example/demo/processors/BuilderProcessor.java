package com.example.demo.processors;

//import com.google.auto.service.AutoService;

import com.example.demo.annotations.SetterBuilder;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("com.example.demo.annotations.SetterBuilder")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class BuilderProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(SetterBuilder.class);

        Map<Boolean, ? extends List<? extends Element>> annotatedMethods = annotatedElements.stream().collect(
                Collectors.partitioningBy(el ->
                        ((ExecutableType) el.asType()).getParameterTypes().size() == 1 &&
                                el.getSimpleName().toString().startsWith("set")));

        List<? extends Element> setters = annotatedMethods.get(true);
        List<? extends Element> otherMethods = annotatedMethods.get(false);

        for (Element el : otherMethods) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@SetterBuilder must be applied to a setXxx method " + "with a single argument", el);
        }

        if (setters.isEmpty()) {
            return false;
        }

        String className = ((TypeElement) setters.get(0).getEnclosingElement()).getQualifiedName().toString();

        Map<String, String> setterMap = setters.stream().collect(Collectors.toMap(
                setter -> setter.getSimpleName().toString(),
                setter -> ((ExecutableType) setter.asType())
                        .getParameterTypes().get(0).toString()));

        try {
            String packageName = null;
            int lastDot = className.lastIndexOf('.');

            if (lastDot > 0) {
                packageName = className.substring(0, lastDot);
            }

            String simpleClassName = className.substring(lastDot + 1);
            String builderClassName = className + "SetterBuilder";
            String builderSimpleClassName = builderClassName
                    .substring(lastDot + 1);

            JavaFileObject builderFile = processingEnv.getFiler()
                    .createSourceFile(builderClassName);

            writeCode(builderFile, packageName, builderSimpleClassName, simpleClassName, setterMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    private static void writeCode(JavaFileObject builderFile, String packageName, String builderSimpleClassName, String simpleClassName, Map<String, String> setterMap) {
        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            if (packageName != null) {
                out.print("package ");
                out.print(packageName);
                out.println(";");
                out.println();
            }

            out.print("public class ");
            out.print(builderSimpleClassName);
            out.println(" {");
            out.println();

            out.print("    private ");
            out.print(simpleClassName);
            out.print(" object = new ");
            out.print(simpleClassName);
            out.println("();");
            out.println();

            out.print("    public ");
            out.print(simpleClassName);
            out.println(" build() {");
            out.println("        return object;");
            out.println("    }");
            out.println();


            setterMap.forEach((methodName, argumentType) -> {
                out.print("    public ");
                out.print(builderSimpleClassName);
                out.print(" ");
                out.print(methodName);
                out.print("(");
                out.print(argumentType);
                out.println(" value) {");
                out.print("        object.");
                out.print(methodName);
                out.println("(value);");
                out.println("        return this;");
                out.println("    }");
                out.println();
            });

            out.println("}");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
