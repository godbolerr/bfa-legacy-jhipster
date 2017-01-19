package com.bfa.app.service.impl;

import java.util.HashSet;
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
import com.bfa.app.service.dto.BookingRecordDTO;
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
			Set<Passenger> passengers = new HashSet<Passenger>();
			passengers.add(pas);
			bookingRecord.setBookPsrs(passengers);
		}
		bookingRecord = bookingRecordRepository.save(bookingRecord);
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
		BookingRecord bookingRecord = bookingRecordRepository.findOne(id);
		BookingRecordDTO bookingRecordDTO = bookingRecordMapper.bookingRecordToBookingRecordDTO(bookingRecord);
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
