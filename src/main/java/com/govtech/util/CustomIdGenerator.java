package com.govtech.util;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class CustomIdGenerator implements IdentifierGenerator {

	private static final long serialVersionUID = 1L;
	private static int counter = 0;

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object)throws HibernateException {
		String prefix = "user";
		counter++;
		return prefix + counter;
	}

}
