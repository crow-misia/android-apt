package com.github.crowmisia.sample.processor;

import com.github.crowmisia.sample.annotation.Sample;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class SampleProcessor extends AbstractProcessor {
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.filer = processingEnv.getFiler();
        this.messager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Sample.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        for (final Element element : roundEnv.getElementsAnnotatedWith(Sample.class)) {
            final Sample annotation = element.getAnnotation(Sample.class);

            // when element is class
            final String packageName = element.getEnclosingElement().toString();
            final String className = element.getSimpleName().toString();
            // when element is method
            // final String className = element.getEnclosingElement().toString();

            outputSampleClass(packageName, className);
        }

        // return true if we successfully processed the Annotation.
        return true;
    }

    private void error(Element e, String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }

    private void outputSampleClass(final String packageName, final String className) {
        try {
            messager.printMessage(Diagnostic.Kind.NOTE, className);

            TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(className + "$Sample")
                    .addModifiers(Modifier.PUBLIC)
                    .addJavadoc("Original Package $S\n", packageName);

            JavaFile.builder(packageName, typeBuilder.build())
                    .build()
                    .writeTo(this.filer);
        } catch (final IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
    }
}
