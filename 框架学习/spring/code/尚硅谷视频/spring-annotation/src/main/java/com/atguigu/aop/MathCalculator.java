package com.atguigu.aop;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.xml.XmlBeanDefinitionStoreException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class MathCalculator {
	
	public int div(int i,int j){
		System.out.println("MathCalculator...div...");
		return i/j;	
	}


	public void div2(){
		System.out.println("这个不用使用aop");
	}

//	protected int getValidationModeForResource(Resource resource) {
//		// 获取默认的 ValidationMode 
//		int validationModeToUse = getValidationMode();
//		// 如果条件成立，那么说明修改过默认值，一般不会这样做，都是使用默认值
//		if (validationModeToUse != VALIDATION_AUTO) {
//			return validationModeToUse;
//		}
//		// 进行自动检查
//		int detectedMode = detectValidationMode(resource);
//		if (detectedMode != VALIDATION_AUTO) {
//			return detectedMode;
//		}
//		// Hmm, we didn't get a clear indication... Let's assume XSD,
//		// since apparently no DTD declaration has been found up until
//		// detection stopped (before finding the document's root tag).
//		return VALIDATION_XSD;
//	}
}
