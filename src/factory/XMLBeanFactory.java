package factory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import bean.BeanDefinition;
import bean.BeanUtil;
import bean.PropertyValue;
import bean.PropertyValues;
import resource.Resource;

public class XMLBeanFactory extends AbstractBeanFactory{
	
	
	
	public XMLBeanFactory(Resource r)
	{
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
			Document document = dbBuilder.parse(r.getInputStream());
            NodeList beanList = document.getElementsByTagName("bean");
            for(int i = 0 ; i < beanList.getLength(); i++)
            {
            	Node bean = beanList.item(i);
            	BeanDefinition beandef = new BeanDefinition();
            	String beanClassName = bean.getAttributes().getNamedItem("class").getNodeValue();
            	String beanName = bean.getAttributes().getNamedItem("id").getNodeValue();
            	
        		beandef.setBeanClassName(beanClassName);//设置对象名
        		
				try {
					Class<?> beanClass = Class.forName(beanClassName);//根据对象名获取对象
					beandef.setBeanClass(beanClass);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}     		
        		PropertyValues propertyValues = new PropertyValues();	
        		NodeList propertyList = bean.getChildNodes();
            	for(int j = 0 ; j < propertyList.getLength(); j++)
            	{
            		Node property = propertyList.item(j);
            		if (property instanceof Element) {
        				Element ele = (Element) property;
        				Object obj=null;
        				String name = ele.getAttribute("name");//获取name属性
        				Class<?> type;
						try {
							type = beandef.getBeanClass().getDeclaredField(name).getType();		
							if(ele.hasAttribute("value"))//如果存在value属性，则获取value属性
							{						
							Object value=ele.getAttribute("value");
	        				if(type == Integer.class)
	        				{
	        					value = Integer.parseInt((String) value);
	        				}     				
	        				propertyValues.AddPropertyValue(new PropertyValue(
	        						name,value));
	                    	beandef.setPropertyValues(propertyValues);	                    	
	                    	this.registerBeanDefinition(beanName, beandef);
	        				}	       					
							else
							{		
								String value=ele.getAttribute("ref");
		        			     obj=this.getBean(value);//根据ref获得类的实体
		        			   propertyValues.AddPropertyValue(new PropertyValue(name,obj));
		                   	beandef.setPropertyValues(propertyValues);      	
		                	this.registerBeanDefinition(beanName, beandef);
							}
						} catch (NoSuchFieldException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        				
        				
        			}
            	}
            	//beandef.setPropertyValues(propertyValues);
            	
            	//this.registerBeanDefinition(beanName, beandef);
            }
            
		} catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}

	@Override
	protected BeanDefinition GetCreatedBean(BeanDefinition beanDefinition) {
		
		try {
			// set BeanClass for BeanDefinition
			
			Class<?> beanClass = beanDefinition.getBeanClass();
			// set Bean Instance for BeanDefinition
			Object bean = beanClass.newInstance();	
			
			List<PropertyValue> fieldDefinitionList = beanDefinition.getPropertyValues().GetPropertyValues();
			for(PropertyValue propertyValue: fieldDefinitionList)
			{
				BeanUtil.invokeSetterMethod(bean, propertyValue.getName(), propertyValue.getValue());
				
			}
			
			beanDefinition.setBean(bean);
			
			return beanDefinition;
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
