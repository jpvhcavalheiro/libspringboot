package io.altar.lib.business;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.altar.lib.model.Book;
import io.altar.lib.model.History;
import io.altar.lib.model.User;
import io.altar.lib.repositories.BookRepository;
import io.altar.lib.repositories.HistoryRepository;
import io.altar.lib.repositories.UserRepository;



@Component
public class HistoryBusiness {
	@Autowired
	BookRepository bookRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	HistoryRepository historyRepository;

	
	@Transactional
	public History reserveBookHistory(History newHistory){
		Book formerBook=bookRepository.findById(newHistory.getHistoryBook().getId());
		if(formerBook.getState().equals("available")){
			formerBook.setState("reserved");
			bookRepository.update(formerBook);
			User formerUser=userRepository.findById(newHistory.getHistoryUser().getId());
			newHistory.setHistoryBook(formerBook);
			newHistory.setHistoryUser(formerUser);
			newHistory.setDeliveryDate(null);
			newHistory.setPickupDate(null);
			return historyRepository.save(newHistory);
		}
		return null; 
	}
	
	@Transactional
	public History pickUpBook(Book bookToPickUp){
		for(History item:historyRepository.getAll()){
			if(item.getHistoryBook().getId()==bookToPickUp.getId() && item.getPickupDate()==null){
				Book bookInUse=bookRepository.findById(bookToPickUp.getId());
				bookInUse.setState("inUse");
				bookRepository.update(bookInUse);
				item.setHistoryBook(bookInUse);
				item.setPickupDate(new Date());
				historyRepository.update(item);
				return item;
			}
			
		}
		return null;
	}
	
	@Transactional
	public History deliverBook(Book bookToDeliver) {
		for(History item:historyRepository.getAll()) {
			if(item.getHistoryBook().getId()==bookToDeliver.getId() && item.getDeliveryDate()==null) {
				item.setDeliveryDate(new Date());
				Book bookAvailableAgain=bookRepository.findById(bookToDeliver.getId());
				bookAvailableAgain.setState("available");
				bookRepository.update(bookAvailableAgain);
				item.setHistoryBook(bookAvailableAgain);
				historyRepository.update(item);
				return item;
			}
		}
		return null;
	}

	public User getUserWithBook(long idBook) {
		
		for (History item:historyRepository.getAll()) {
			if(item.getDeliveryDate()==null && item.getReservationDate()!=null && item.getHistoryBook().getId()==idBook) {
				return item.getHistoryUser();
			}
		}
			return null;
	}
	
	public ArrayList<Book> getBooksWithUser(long idUser){
		ArrayList<Book> resultToBooksWithUser = new ArrayList<Book>();
		for(History item:historyRepository.getAll()){
			if(item.getDeliveryDate()==null && item.getReservationDate()!=null && item.getHistoryUser().getId()==idUser){
				resultToBooksWithUser.add(item.getHistoryBook());
			}
		}
		return resultToBooksWithUser;
	}
	
	public ArrayList<History> getAllHstoryOfUser(long idUser){
		ArrayList<History> resultAllHistory = new ArrayList<History>();
		for(History item:historyRepository.getAll()){
			if(item.getHistoryUser().getId()==idUser){
				resultAllHistory.add(item);
			}
		}
		return resultAllHistory;
	}
	
	
}
