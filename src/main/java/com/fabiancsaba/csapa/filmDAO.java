package com.fabiancsaba.csapa;

import org.hibernate.Session;
import org.hibernate.Transaction;

class saveFilm {

    public static void saveFilm(Film film) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(film);
            transaction.commit();
            System.out.println("Film hozzáadva az adatbázishoz.");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}