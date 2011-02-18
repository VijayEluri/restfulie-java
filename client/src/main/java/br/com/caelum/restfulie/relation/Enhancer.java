package br.com.caelum.restfulie.relation;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import br.com.caelum.restfulie.Resource;

public class Enhancer {

	public <T> Class enhanceResource(Class<T> originalType) {
		ClassPool pool = ClassPool.getDefault();
		if (pool.find(Enhancer.class.getName()) == null) {
			pool.appendClassPath(new LoaderClassPath(getClass().getClassLoader()));
		}
		try {
			// TODO extract this enhancement to an interface and test it appart
			CtClass newType =   pool.makeClass("br.com.caelum.restfulie." + originalType.getSimpleName() + "_" + System.currentTimeMillis());
			newType.setSuperclass(pool.get(originalType.getName()));
			newType.addInterface(pool.get(Resource.class.getName()));
//			enhanceMustIgnore(newType);
			enhanceLinks(newType);
			return newType.toClass();
		} catch (NotFoundException e) {
			throw new IllegalStateException("Unable to extend type " + originalType.getName(), e);
		} catch (CannotCompileException e) {
			throw new IllegalStateException("Unable to extend type " + originalType.getName(), e);
		}
	}

	private void enhanceLinks(CtClass newType) throws CannotCompileException {
		CtField field = CtField.make("public java.util.List link = new java.util.ArrayList();", newType);
		newType.addField(field);
		newType.addMethod(CtNewMethod.make("public java.util.List getLinks() { return link; }", newType));
		newType.addMethod(CtNewMethod.make("public java.util.List getLinks(String rel) { java.util.List links = new java.util.ArrayList(); for(int i=0;i<link.size();i++) {br.com.caelum.restfulie.Link t = link.get(i); if(t.getRel().equals(rel)) links.add(t); } return links; }", newType));
		newType.addMethod(CtNewMethod.make("public br.com.caelum.restfulie.Link getLink(String rel) { for(int i=0;i<link.size();i++) {br.com.caelum.restfulie.Link t = link.get(i); if(t.getRel().equals(rel)) return t; } return null; }", newType));
		newType.addMethod(CtNewMethod.make("public boolean hasLink(String link) { return getLink(link)!=null; }", newType));
	}

//	private void enhanceMustIgnore(CtClass newType)
//			throws CannotCompileException {
//		CtField field = CtField.make("public java.util.Map _mustIgnoreProperties = new java.util.HashMap();", newType);
//		newType.addField(field);
//		newType.addMethod(CtNewMethod.make("public java.lang.String getUnknownProperty(java.lang.String key) { return (java.lang.String) _mustIgnoreProperties.get(key); }", newType));
//		newType.addMethod(CtNewMethod.make("public java.util.Map getUnknownProperties() { return _mustIgnoreProperties; }", newType));
//	}

}
