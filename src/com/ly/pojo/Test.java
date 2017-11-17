package com.ly.pojo;

import com.ly.myspring.ApplicationContext;
import com.ly.myspring.ClassPathXml;

public class Test {
	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXml();
		//DateSource dateSource = (DateSource) applicationContext.getBean("dateSource");
		//System.out.println(dateSource);
		SessionFactory sessionFactory = (SessionFactory) applicationContext.getBean("sessionFactory");
		System.out.println(sessionFactory);
	}
}
