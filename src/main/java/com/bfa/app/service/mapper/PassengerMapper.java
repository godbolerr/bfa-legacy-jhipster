package com.bfa.app.service.mapper;

import com.bfa.app.domain.*;
import com.bfa.app.service.dto.PassengerDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Passenger and its DTO PassengerDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PassengerMapper {

    @Mapping(source = "bookingRecord.id", target = "bookingRecordId")
    @Mapping(source = "psrBook.id", target = "psrBookId")
    PassengerDTO passengerToPassengerDTO(Passenger passenger);

    List<PassengerDTO> passengersToPassengerDTOs(List<Passenger> passengers);

    @Mapping(source = "bookingRecordId", target = "bookingRecord")
    @Mapping(source = "psrBookId", target = "psrBook")
    Passenger passengerDTOToPassenger(PassengerDTO passengerDTO);

    List<Passenger> passengerDTOsToPassengers(List<PassengerDTO> passengerDTOs);

    default BookingRecord bookingRecordFromId(Long id) {
        if (id == null) {
            return null;
        }
        BookingRecord bookingRecord = new BookingRecord();
        bookingRecord.setId(id);
        return bookingRecord;
    }
}
