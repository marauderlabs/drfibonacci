package com.example.storm.apt;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.example.storm.api.Converter;
import com.example.storm.api.Database;
import com.example.storm.api.Entity;
import com.example.storm.types.java.BooleanConverter;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;

@SupportedAnnotationTypes({ "com.example.storm.api.Entity","com.example.storm.api.Database","com.example.storm.api.Converter" })
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class MainProcessor extends AbstractProcessor {
	private ProcessorLogger logger;
	private Configuration cfg = new Configuration();
	private List<EntityModel> entities = new ArrayList<EntityModel>();

	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {
		this.logger = new ProcessorLogger(processingEnv.getMessager());
		logger.info("Running MainProcessor...");

		cfg.setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/res"));

//		for (TypeElement annotationType : annotations) {}

		for (Element element : roundEnv.getElementsAnnotatedWith(Converter.class)) {
			logger.info("processing " + element.getSimpleName());
			ConverterProcessor cproc = new ConverterProcessor(element, logger);
			cproc.populateModel();
		}
//		TypeMapper.dumpConverters(logger);
		
		for (Element element : roundEnv.getElementsAnnotatedWith(Entity.class)) {
			EntityProcessor eproc = new EntityProcessor(element, logger);
			eproc.populateModel();
			eproc.processTemplate(processingEnv, cfg);
			// retain each entity to add to DatabaseModel 
			entities.add(eproc.getModel());
		}
		
		for (Element element : roundEnv.getElementsAnnotatedWith(Database.class)) {
			DatabaseProcessor dproc = new DatabaseProcessor(element, logger);
			dproc.populateModel();
			dproc.addEntities(entities);
			dproc.processTemplate(processingEnv, cfg);
		}
		
		return true;
	}

}