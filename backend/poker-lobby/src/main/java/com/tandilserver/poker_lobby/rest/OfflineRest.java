package com.tandilserver.poker_lobby.rest;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tandilserver.poker_intercom.genericResponse.ExceptionResponse;
import com.tandilserver.poker_intercom.genericResponse.StatusCodes;
import com.tandilserver.poker_lobby.dataBase.domain.Users;
import com.tandilserver.poker_lobby.dataBase.repository.UsersRepository;
import com.tandilserver.poker_lobby.rest.dto.LoginIn;
import com.tandilserver.poker_lobby.rest.dto.LoginOut;
import com.tandilserver.poker_lobby.rest.dto.SignupIn;
import com.tandilserver.poker_lobby.rest.dto.SignupOut;

@RestController
@RequestMapping("/offlineRest")
public class OfflineRest {

	@Autowired
	UsersRepository usersRepository;
	
	// TODO: change return type to ResponseEntity<Type>
	@RequestMapping(path="/signup", method=RequestMethod.POST)
	public SignupOut signup(@RequestBody SignupIn registroData) {
		SignupOut out = new SignupOut();
		try {
			if(usersRepository.findByEmail(registroData.email) != null) throw new ExceptionResponse(StatusCodes.ERR, "Email is used");
			if(usersRepository.findByNick(registroData.nick) != null) throw new ExceptionResponse(StatusCodes.ERR, "Nick is used");
			if(registroData.nick.length() < 3) throw new ExceptionResponse(StatusCodes.ERR, "NICK < 3");
			if(registroData.nick.length() > 15) throw new ExceptionResponse(StatusCodes.ERR, "NICK > 15");
			Users user = new Users();
			user.setEmail(registroData.email);
			user.setSignup_date(new Date());
			user.setCoins(5000L); // fichas iniciales
			user.setNick(registroData.nick);
			// TODO: the password must be encrypted in any hash. 
			user.setPassword(registroData.password);
			user.setLast_activity(new Date());
			user.setHashSignature(UUID.randomUUID().toString());
			user.setId_user_recommend(0L);
			Users userOut = usersRepository.create(user);
			out.upgrade = userOut.getHashSignature();
			out.id_usuario = userOut.getId_usuario();
		} catch(ExceptionResponse e) {
			out.statusCode = e.getCode();
			out.message = e.getMessage();
		}
		return out;
	}
	
	// TODO: change return type to ResponseEntity<Type>
	@RequestMapping(path="/login", method=RequestMethod.POST) 
	public LoginOut login(@RequestBody LoginIn data) {
		LoginOut out = new LoginOut();
		Users user = usersRepository.findByEmail(data.email);
		// TODO: the password must be encrypted in any hash.
		if(user == null || !user.getPassword().equals(data.password)) {
			out.statusCode = StatusCodes.ERR;
			out.message = "Email or password invalid";
		} else {
			user.setHashSignature(UUID.randomUUID().toString());
			out.id = user.getId_usuario();
			out.upgrade = user.getHashSignature();
			user.setLast_activity(new Date());
			usersRepository.update(user);
		}
		return out;
	}
	
}
