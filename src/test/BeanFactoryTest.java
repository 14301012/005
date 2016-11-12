package test;

import resource.LocalFileResource;
import factory.BeanFactory;
import factory.XMLBeanFactory;


public class BeanFactoryTest {

	
	public static void main(String[] args) {
		
        LocalFileResource resource = new LocalFileResource("beans.xml");
    	
    	BeanFactory beanFactory = new XMLBeanFactory(resource);
    	// the BeanDefinition doesn`t create the real bean yet
    	boss b=(boss)beanFactory.getBean("boss");
    	System.out.println(b.tostring());
		
	}

}
