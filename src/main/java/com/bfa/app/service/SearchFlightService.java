package com.bfa.app.service;

import com.bfa.app.service.dto.SearchFlightDTO;
import java.util.List;

/**
 * Service Interface for managing SearchFlight.
 */
public interface SearchFlightService {

    /**
     * Save a searchFlight.
     *
     * @param searchFlightDTO the entity to save
     * @return the persisted entity
     */
    SearchFlightDTO save(SearchFlightDTO searchFlightDTO);

    /**
     *  Get all the searchFlights.
     *  
     *  @return the list of entities
     */
    List<SearchFlightDTO> findAll();

    /**
     *  Get the "id" searchFlight.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    SearchFlightDTO findOne(Long id);

    /**
     *  Delete the "id" searchFlight.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
