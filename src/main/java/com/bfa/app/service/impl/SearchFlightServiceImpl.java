package com.bfa.app.service.impl;

import com.bfa.app.service.SearchFlightService;
import com.bfa.app.domain.SearchFlight;
import com.bfa.app.repository.SearchFlightRepository;
import com.bfa.app.service.dto.SearchFlightDTO;
import com.bfa.app.service.mapper.SearchFlightMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing SearchFlight.
 */
@Service
@Transactional
public class SearchFlightServiceImpl implements SearchFlightService{

    private final Logger log = LoggerFactory.getLogger(SearchFlightServiceImpl.class);
    
    @Inject
    private SearchFlightRepository searchFlightRepository;

    @Inject
    private SearchFlightMapper searchFlightMapper;

    /**
     * Save a searchFlight.
     *
     * @param searchFlightDTO the entity to save
     * @return the persisted entity
     */
    public SearchFlightDTO save(SearchFlightDTO searchFlightDTO) {
        log.debug("Request to save SearchFlight : {}", searchFlightDTO);
        SearchFlight searchFlight = searchFlightMapper.searchFlightDTOToSearchFlight(searchFlightDTO);
        searchFlight = searchFlightRepository.save(searchFlight);
        SearchFlightDTO result = searchFlightMapper.searchFlightToSearchFlightDTO(searchFlight);
        return result;
    }

    /**
     *  Get all the searchFlights.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<SearchFlightDTO> findAll() {
        log.debug("Request to get all SearchFlights");
        List<SearchFlightDTO> result = searchFlightRepository.findAll().stream()
            .map(searchFlightMapper::searchFlightToSearchFlightDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one searchFlight by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public SearchFlightDTO findOne(Long id) {
        log.debug("Request to get SearchFlight : {}", id);
        SearchFlight searchFlight = searchFlightRepository.findOne(id);
        SearchFlightDTO searchFlightDTO = searchFlightMapper.searchFlightToSearchFlightDTO(searchFlight);
        return searchFlightDTO;
    }

    /**
     *  Delete the  searchFlight by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SearchFlight : {}", id);
        searchFlightRepository.delete(id);
    }
}
