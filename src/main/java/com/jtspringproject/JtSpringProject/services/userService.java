package com.jtspringproject.JtSpringProject.services;

import com.jtspringproject.JtSpringProject.models.*;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jtspringproject.JtSpringProject.dao.userDao;
import com.jtspringproject.JtSpringProject.models.User;

@Service
public class userService {
	@Autowired
	private userDao userDao;

	private User currentUser;

	public List<User> getUsers(){
		return this.userDao.getAllUser();
	}
	
	public User addUser(User user) {
		User newUser = this.userDao.saveUser(user);
		this.currentUser = newUser;
		return newUser;
	}

	public User checkLogin(String username,String password) {
		User loggedInUser = this.userDao.getUser(username, password);
		this.currentUser = loggedInUser;
		return loggedInUser;
	}

	public User getCurrentUser() {
		return this.currentUser;
	}

	public User updateUserProfile(User user){
		if(userDao.saveUserProfile(user, currentUser) == null){
			return null;
		}
		currentUser = user;
		return currentUser;
	}

}
