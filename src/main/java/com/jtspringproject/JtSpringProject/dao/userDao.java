package com.jtspringproject.JtSpringProject.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.sound.midi.Soundbank;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jtspringproject.JtSpringProject.models.User;


@Repository
public class userDao {
	@Autowired
    private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sf) {
        this.sessionFactory = sf;
    }
   @Transactional
    public List<User> getAllUser() {
        Session session = this.sessionFactory.getCurrentSession();
		List<User>  userList = session.createQuery("from CUSTOMER").list();
        return userList;
    }
    
    @Transactional
	public User saveUser(User user) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(user);
		System.out.println("User added" + user.getId());
        return user;
	}
	@Transactional
	public User saveUserProfile(User user, User currentUser) {
		Session session = this.sessionFactory.getCurrentSession();
		String updateQuery = "UPDATE CUSTOMER SET username = :newUsername, email = :newEmail, password = :newPassword, address = :newAddress WHERE username = :oldUsername";

		Query query = session.createSQLQuery(updateQuery)
				.setParameter("newUsername", user.getUsername())
				.setParameter("newEmail", user.getEmail())
				.setParameter("newPassword", user.getPassword())
				.setParameter("newAddress", user.getAddress())
				.setParameter("oldUsername", currentUser.getUsername());

		int result = query.executeUpdate();

		if (result > 0) {
			System.out.println("User profile updated for username: " + currentUser.getUsername());
			return user;
		} else {
			// Handle the case where the update did not occur
			System.out.println("User not found or update failed for username: " + currentUser.getUsername());
			return null;
		}
	}


	//    public User checkLogin() {
//    	this.sessionFactory.getCurrentSession().
//    }
    @Transactional
    public User getUser(String username,String password) {
    	Query query = sessionFactory.getCurrentSession().createQuery("from CUSTOMER where username = :username");
    	query.setParameter("username",username);
    	
    	try {
			User user = (User) query.getSingleResult();
			System.out.println(user.getPassword());
			if(password.equals(user.getPassword())) {
				return user;
			}else {		
				return null;
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
			return null;
		}
    	
    }
}
