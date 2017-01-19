package com.bfa.app.service.impl;

import java.time.LocalDate;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bfa.app.domain.BookingRecord;
import com.bfa.app.domain.Passenger;
import com.bfa.app.repository.BookingRecordRepository;
import com.bfa.app.service.BookingRecordService;
import com.bfa.app.service.InventoryService;
import com.bfa.app.service.dto.BookingRecordDTO;
import com.bfa.app.service.dto.InventoryDTO;
import com.bfa.app.service.dto.PassengerDTO;
import com.bfa.app.service.mapper.BookingRecordMapper;

/**
 * Service Implementation for managing BookingRecord.
 */
@Service
@Transactional
public class BookingRecordServiceImpl implements BookingRecordService {

	private final Logger log = LoggerFactory.getLogger(BookingRecordServiceImpl.class);

	@Inject
	private BookingRecordRepository bookingRecordRepository;

	@Inject
	private BookingRecordMapper bookingRecordMapper;
	
	@Inject
	private InventoryService invService;

	/**
	 * Save a bookingRecord.
	 *
	 * @param bookingRecordDTO
	 *            the entity to save
	 * @return the persisted entity
	 */
	public BookingRecordDTO save(BookingRecordDTO bookingRecordDTO) {
		log.debug("Request to save BookingRecord : {}", bookingRecordDTO);
		BookingRecord bookingRecord = bookingRecordMapper.bookingRecordDTOToBookingRecord(bookingRecordDTO);

		// populate passenger separately. Take care of mapper issue for one to
		// many.
		if (bookingRecordDTO.getPdto() != null) {
			Passenger pas = new Passenger();
			pas.setFirstName(bookingRecordDTO.getPdto().getFirstName());
			pas.setLastName(bookingRecordDTO.getPdto().getLastName());
			pas.setGender(bookingRecordDTO.getPdto().getGender());
			bookingRecord.addBookPsr(pas);
		}
		
		bookingRecord.setBookingDate(LocalDate.now());
		bookingRecord = bookingRecordRepository.save(bookingRecord);
		
		// Less 1 for the inventory.
		
		InventoryDTO invDto = invService.findByFlightNumberAndFlightDate(bookingRecord.getFlightNumber(), bookingRecord.getFlightDate());
		
		if ( invDto != null ) {
			invDto.setAvailable(invDto.getAvailable() - 1);
			invService.save(invDto);
		}
		
		
		BookingRecordDTO result = bookingRecordMapper.bookingRecordToBookingRecordDTO(bookingRecord);
		return result;
	}

	/**
	 * Get all the bookingRecords.
	 * 
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<BookingRecordDTO> findAll(Pageable pageable) {
		log.debug("Request to get all BookingRecords");
		Page<BookingRecord> result = bookingRecordRepository.findAll(pageable);
		return result.map(bookingRecord -> bookingRecordMapper.bookingRecordToBookingRecordDTO(bookingRecord));
	}

	/**
	 * Get one bookingRecord by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public BookingRecordDTO findOne(Long id) {
		log.debug("Request to get BookingRecord : {}", id);
		PassengerDTO pdto = new PassengerDTO();
		BookingRecord bookingRecord = bookingRecordRepository.findOne(id);
		
		if ( bookingRecord != null ) {
			Set<Passenger> psr = bookingRecord.getBookPsrs(); 
			if ( psr != null && psr.size() == 1 ){
				Passenger[] pasArray = psr.toArray(new Passenger[psr.size()] );
				pdto.setFirstName(pasArray[0].getFirstName());
				pdto.setLastName(pasArray[0].getLastName());
				pdto.setGender(pasArray[0].getGender());
				
			}
		}
		BookingRecordDTO bookingRecordDTO = bookingRecordMapper.bookingRecordToBookingRecordDTO(bookingRecord);
		if ( pdto != null ) {
			bookingRecordDTO.setPdto(pdto);
		}
		return bookingRecordDTO;
	}

	/**
	 * Delete the bookingRecord by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete BookingRecord : {}", id);
		bookingRecordRepository.delete(id);
	}
}
