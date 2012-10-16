package com.example.storm.apt;

public class TableHelperTemplate extends ClassTemplate {

	public TableHelperTemplate(EntityModel model) {
		super(model);
	}

	@Override
	public String getTemplatePath() {
		return "TableHelper.ftl";
	}

	@Override
	public String getPackage() {
		return ((EntityModel) model).getDaoPackage();
	}

	@Override
	public String getGeneratedClass() {
		return ((EntityModel) model).getTableHelperClass();
	}

}
