/*
 * Enfili
 * Project hosted at https://github.com/ryanhosp/enfili/
 * Copyright 2013 Ho Siaw Ping Ryan
 *    
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.r573.enfili.common.resource.db.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HibernateManager {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(HibernateManager.class);
	private static final ThreadLocal<Session> threadLocal = new ThreadLocal<Session>();
	private static SessionFactory sessionFactory = null;

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}	

	public static void init() {
		sessionFactory = new Configuration().configure().buildSessionFactory();
	}
	public static void init(Configuration configuration) {
		sessionFactory = configuration.buildSessionFactory();
	}

	public static Session currentSession() throws HibernateException {
		Session session = (Session)threadLocal.get();
		if (session == null) {
			session = sessionFactory.openSession();
			session.beginTransaction();
			threadLocal.set(session);
		}
		return session;
	}

	public static void closeSession() throws HibernateException {
		Session session = (Session) threadLocal.get();
		if (session != null) {
			if (session.isOpen()) {
				if((session.getTransaction() != null) && (session.getTransaction().isActive())) {
					session.getTransaction().commit();						
				}
				session.close(); 
			}
			threadLocal.set(null);
		}
	}	
	
	public static void rollback() {
		Session session = (Session)threadLocal.get();
		if(session != null) {
			Transaction transaction = session.getTransaction();
			if(transaction != null) {
				transaction.rollback();
			}
		}
	}
}